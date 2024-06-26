package com.tokopedia.product.addedit.preview.presentation.service

import android.graphics.Bitmap
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXT_JPEG
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXT_JPG
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.GQL_ERROR_SUBSTRING
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.DISABLED_LOGGING_DATA_LIST
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.IMAGE_SOURCE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.LOGGING_ERROR_FIELD_NAME
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.LOGGING_TAG
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.REQUEST_DELAY_MILLIS
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.common.util.AddEditProductUploadException
import com.tokopedia.product.addedit.common.util.AddEditSellerReviewHelper
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.data.model.params.add.ProductAddParam
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.preview.domain.mapper.EditProductInputMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_UPLOAD_IMAGE
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_UPLOAD_PRODUCT
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusType
import com.tokopedia.product.manage.common.feature.uploadstatus.data.model.UploadStatusModel
import com.tokopedia.product.manage.common.feature.uploadstatus.domain.SetUploadStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil.DEF_HEIGHT
import com.tokopedia.utils.image.ImageProcessingUtil.DEF_WIDTH
import com.tokopedia.utils.image.ImageProcessingUtil.resizeBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class AddEditProductBaseService : JobIntentServiceX(), CoroutineScope {
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var uploaderUseCase: UploaderUseCase

    @Inject
    lateinit var addProductInputMapper: AddProductInputMapper

    @Inject
    lateinit var productAddUseCase: ProductAddUseCase

    @Inject
    lateinit var productEditUseCase: ProductEditUseCase

    @Inject
    lateinit var editProductInputMapper: EditProductInputMapper

    @Inject
    lateinit var saveProductDraftUseCase: SaveProductDraftUseCase

    @Inject
    lateinit var deleteProductDraftUseCase: DeleteProductDraftUseCase

    @Inject
    lateinit var setUploadStatusUseCase: SetUploadStatusUseCase

    @Inject
    lateinit var getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase

    @Inject
    lateinit var updateProductStockWarehouseUseCase: UpdateProductStockWarehouseUseCase

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var sellerAppReviewHelper: AddEditSellerReviewHelper

    private var notificationManager: AddEditProductNotificationManager? = null

    companion object {
        const val JOB_ID = 13131314
        const val ERROR_IMAGE_ID_IS_EMPTY = "Error upload image because imageId is empty"
    }

    abstract fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager
    abstract fun onUploadProductImagesSuccess(uploadIdList: ArrayList<String>, variantInputModel: VariantInputModel)
    abstract fun onUploadProductImagesFailed(errorMessage: String)

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO
    }

    fun setUploadProductDataSuccess(productId: String = "") {
        notificationManager?.onSuccessUpload()
        setUploadStatus(UploadStatusType.STATUS_DONE.name, productId)
    }

    fun setUploadProductDataError(errorMessage: String) {
        notificationManager?.onFailedUpload(errorMessage)
        setUploadStatus(UploadStatusType.STATUS_ERROR.name)
    }

    fun uploadProductImages(imageUrlOrPathList: List<String>, variantInputModel: VariantInputModel) {
        val imagePathList = filterPathOnly(imageUrlOrPathList)
        val pathImageCount = imagePathList.size
        val uploadIdList: ArrayList<String> = ArrayList()
        val primaryImagePathOrUrl = imageUrlOrPathList.firstOrNull().orEmpty()

        notificationManager = getNotificationManager(pathImageCount + variantInputModel.products.size)
        notificationManager?.onStartUpload(primaryImagePathOrUrl)

        launchCatchError(block = {
            repeat(pathImageCount) { i ->
                val imageId = uploadImageAndGetId(imagePathList.getOrNull(i) ?: return@repeat)
                if (imageId.isNotEmpty()) {
                    notificationManager?.onAddProgress()
                    uploadIdList.add(imageId)
                } else {
                    throw Exception(ERROR_IMAGE_ID_IS_EMPTY)
                }
            }

            variantInputModel.products = uploadProductVariantImages(variantInputModel.products)
            variantInputModel.sizecharts = uploadProductSizechart(variantInputModel.sizecharts)

            onUploadProductImagesSuccess(uploadIdList, variantInputModel)
        }, onError = { throwable ->
            throwable.printStackTrace()
            setUploadProductDataError(cleanErrorMessage(throwable.localizedMessage.orEmpty()))
            logError(TITLE_ERROR_UPLOAD_IMAGE, throwable)
        })
    }

    protected fun getErrorMessage(throwable: Throwable): String {
        // don't display gql error message to user
        return if (throwable.message == null || throwable.message?.contains(GQL_ERROR_SUBSTRING) == true) {
            resourceProvider.getGqlErrorMessage().orEmpty()
        } else {
            val errorMessage = ErrorHandler.getErrorMessage(this, throwable)
            cleanErrorMessage(errorMessage)
        }
    }

    protected fun logError(requestParams: RequestParams, throwable: Throwable) {
        val formattedParam = createErrorLogData(requestParams)
        logError("$TITLE_ERROR_UPLOAD_PRODUCT,$formattedParam", throwable)
    }

    protected fun logError(title: String, throwable: Throwable) {
        val errorMessage = String.format(
            "\"%s.\",\"userId: %s\",\"errorMessage: %s\"",
            title,
            userSession.userId,
            throwable.message.orEmpty()
        )
        val exception = AddEditProductUploadException(errorMessage, throwable)

        AddEditProductErrorHandler.logExceptionToCrashlytics(exception)
        ServerLogger.log(Priority.P2, LOGGING_TAG, mapOf(LOGGING_ERROR_FIELD_NAME to errorMessage))
    }

    private fun initInjector() {
        val baseMainApplication = applicationContext as BaseMainApplication
        DaggerAddEditProductPreviewComponent.builder()
            .addEditProductComponent(AddEditProductComponentBuilder.getComponent(baseMainApplication))
            .addEditProductPreviewModule(AddEditProductPreviewModule())
            .build()
            .inject(this)
    }

    private suspend fun uploadProductSizechart(
        sizecharts: PictureVariantInputModel
    ): PictureVariantInputModel {
        if (sizecharts.picID.isEmpty() && sizecharts.urlOriginal.isNotEmpty()) {
            val uploadId = uploadImageAndGetId(sizecharts.urlOriginal)
            if (uploadId.isNotEmpty()) {
                sizecharts.uploadId = uploadId
                sizecharts.urlOriginal = ""
            }
        }

        return sizecharts
    }

    private suspend fun uploadProductVariantImages(
        productVariants: List<ProductVariantInputModel>
    ): List<ProductVariantInputModel> {
        productVariants.forEach {
            notificationManager?.onAddProgress()
            it.pictures.firstOrNull()?.let { picture ->
                if (picture.picID.isEmpty() && picture.urlOriginal.isNotEmpty()) {
                    delay(REQUEST_DELAY_MILLIS) // add delay when uploading to reduce server load
                    val uploadId = uploadImageAndGetId(picture.urlOriginal)
                    if (uploadId.isNotEmpty()) {
                        picture.uploadId = uploadId

                        // clear existing data
                        picture.picID = ""
                        picture.description = ""
                        picture.filePath = ""
                        picture.fileName = ""
                        picture.width = 0
                        picture.height = 0
                        picture.isFromIG = ""
                        picture.urlOriginal = ""
                        picture.urlThumbnail = ""
                        picture.url300 = ""
                        picture.status = false
                    }
                }
            }
        }
        return productVariants
    }

    private suspend fun uploadImageAndGetId(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
            sourceId = IMAGE_SOURCE_ID,
            filePath = filePath
        )

        when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> {
                return result.uploadId
            }
            is UploadResult.Error -> {
                val message = "Error upload image %s because %s".format(filePath, result.message)
                val exception = AddEditProductUploadException(message = message)
                AddEditProductErrorHandler.logExceptionToCrashlytics(exception)

                onUploadProductImagesFailed(result.message)
                throw AddEditProductUploadException(message = result.message)
            }
        }
    }

    protected fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
        imageUrlOrPathList.filterNot {
            it.startsWith(AddEditProductConstants.HTTP_PREFIX)
        }

    private fun cleanErrorMessage(message: String): String {
        // clean error message from string inside <...> and (...)
        return message.replace("\\(.*?\\)".toRegex(), "")
            .replace("\\<.*?\\>".toRegex(), "")
    }

    private fun setUploadStatus(status: String, productId: String = "") {
        launchCatchError(block = {
            setUploadStatusUseCase.setUploadStatus(
                UploadStatusModel(
                    productId = productId,
                    status = status
                )
            )
        }, onError = {})
    }

    private fun createErrorLogData(requestParams: RequestParams): String {
        val variables = requestParams.parameters[ProductAddUseCase.PARAM_INPUT]
        val json = gson.toJson(variables)
        val jsonMap = gson.fromJson(json, MutableMap::class.java)
        val logDataList = jsonMap
            .filter { it.key !in DISABLED_LOGGING_DATA_LIST }
            .map { "${it.key}:${it.value}" }

        return logDataList.joinToString(";")
    }
}

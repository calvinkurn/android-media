package com.tokopedia.product.addedit.preview.presentation.service

import android.content.Intent
import android.os.Bundle
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.BROADCAST_ADD_PRODUCT
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.GQL_ERROR_SUBSTRING
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.IMAGE_SOURCE_ID
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.preview.domain.mapper.EditProductInputMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.TITLE_ERROR_UPLOAD_IMAGE
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.net.URLEncoder
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class AddEditProductBaseService : JobIntentService(), CoroutineScope {
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
        const val REQUEST_ENCODE = "UTF-8"
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

    fun setUploadProductDataSuccess() {
        notificationManager?.onSuccessUpload()
        sendSuccessBroadcast()
    }

    fun setUploadProductDataError(errorMessage: String) {
        notificationManager?.onFailedUpload(errorMessage)
        sendErrorBroadcast()
    }

    fun uploadProductImages(imageUrlOrPathList: List<String>, variantInputModel: VariantInputModel){
        val imagePathList = filterPathOnly(imageUrlOrPathList)
        val pathImageCount = imagePathList.size
        val uploadIdList: ArrayList<String> = ArrayList()
        val primaryImagePathOrUrl = imageUrlOrPathList.getOrNull(0).orEmpty()

        notificationManager = getNotificationManager(pathImageCount)
        notificationManager?.onStartUpload(primaryImagePathOrUrl)

        launchCatchError(block = {
            repeat(pathImageCount) { i ->
                val imageId = uploadImageAndGetId(imagePathList[i])
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
        val errorMessage = String.format(
                "\"Error upload product.\",\"userId: %s\",\"errorMessage: %s\",params: \"%s\"",
                userSession.userId,
                getErrorMessage(throwable),
                URLEncoder.encode(gson.toJson(requestParams), REQUEST_ENCODE))
        val exception = AddEditProductUploadException(errorMessage, throwable)

        AddEditProductErrorHandler.logExceptionToCrashlytics(exception)
        ServerLogger.log(Priority.P2, "PRODUCT_UPLOAD", mapOf("type" to errorMessage))
    }

    protected fun logError(title: String, throwable: Throwable) {
        val errorMessage = String.format(
                "\"%s.\",\"userId: %s\",\"errorMessage: %s\"",
                title,
                userSession.userId,
                throwable.message.orEmpty())
        val exception = AddEditProductUploadException(errorMessage, throwable)

        AddEditProductErrorHandler.logExceptionToCrashlytics(exception)
        ServerLogger.log(Priority.P2, "PRODUCT_UPLOAD", mapOf("type" to errorMessage))
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
            it.pictures.firstOrNull()?.let { picture ->
                if (picture.picID.isEmpty() && picture.urlOriginal.isNotEmpty()) {
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

        // check picture availability
        if (!filePath.exists()) {
            val message = UploaderUseCase.FILE_NOT_FOUND
            throw Exception(message)
        }

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

    private fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
            imageUrlOrPathList.filterNot {
                it.startsWith(AddEditProductConstants.HTTP_PREFIX)
            }

    private fun cleanErrorMessage(message: String): String {
        // clean error message from string inside <...> and (...)
        return message.replace("\\(.*?\\)".toRegex(), "")
                .replace("\\<.*?\\>".toRegex(), "")
    }

    private fun sendSuccessBroadcast() {
        val result = Intent(BROADCAST_ADD_PRODUCT)
        val bundle = Bundle()
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE)
        result.putExtras(bundle)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(result)
    }

    private fun sendErrorBroadcast() {
        val result = Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT)
        val bundle = Bundle()
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR)
        result.putExtras(bundle)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(result)
    }
}

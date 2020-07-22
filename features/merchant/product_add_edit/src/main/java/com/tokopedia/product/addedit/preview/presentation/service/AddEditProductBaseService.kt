package com.tokopedia.product.addedit.preview.presentation.service

import android.content.Intent
import android.os.Bundle
import androidx.core.app.JobIntentService
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.GQL_ERROR_SUBSTRING
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.IMAGE_SOURCE_ID
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.AddEditProductNotificationManager
import com.tokopedia.product.addedit.common.util.AddEditProductUploadException
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.preview.domain.mapper.EditProductInputMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.ProductEditUseCase
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import timber.log.Timber
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
    lateinit var resourceProvider: ResourceProvider
    @Inject
    lateinit var gson: Gson

    private var notificationManager: AddEditProductNotificationManager? = null

    companion object {
        const val JOB_ID = 13131314
        const val NOTIFICATION_CHANGE_DELAY = 500L
        const val REQUEST_ENCODE = "UTF-8"
    }

    abstract fun getNotificationManager(urlImageCount: Int): AddEditProductNotificationManager
    abstract fun onUploadProductImagesSuccess(uploadIdList: ArrayList<String>, variantInputModel: VariantInputModel)

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
    }

    fun uploadProductImages(imageUrlOrPathList: List<String>, variantInputModel: VariantInputModel){
        val imagePathList = filterPathOnly(imageUrlOrPathList)
        val pathImageCount = imagePathList.size
        val uploadIdList: ArrayList<String> = ArrayList()
        val primaryImagePathOrUrl = imageUrlOrPathList.getOrNull(0).orEmpty()

        launchCatchError(block = {
            notificationManager = getNotificationManager(pathImageCount)
            notificationManager?.onStartUpload(primaryImagePathOrUrl)

            repeat(pathImageCount) { i ->
                val imageId = uploadImageAndGetId(imageUrlOrPathList[i])
                uploadIdList.add(imageId)
            }

            variantInputModel.products = uploadProductVariantImages(variantInputModel.products)
            variantInputModel.sizecharts = uploadProductSizechart(variantInputModel.sizecharts)

            delay(NOTIFICATION_CHANGE_DELAY)
            onUploadProductImagesSuccess(uploadIdList, variantInputModel)
        }, onError = { throwable ->
            setUploadProductDataError(getErrorMessage(throwable))
            logError(RequestParams.EMPTY, throwable)
        })
    }

    protected fun getErrorMessage(throwable: Throwable): String {
        // don't display gql error message to user
        return if (throwable.message == null || throwable.message?.contains(GQL_ERROR_SUBSTRING) == true) {
            resourceProvider.getGqlErrorMessage().orEmpty()
        } else {
            ErrorHandler.getErrorMessage(this, throwable)
        }
    }

    protected fun logError(requestParams: RequestParams, throwable: Throwable) {
        val errorMessage = String.format(
                "\"Error upload product.\",\"userId: %s\",\"userEmail: %s \",\"errorMessage: %s\",params: \"%s\"",
                userSession.userId,
                userSession.email,
                getErrorMessage(throwable),
                URLEncoder.encode(gson.toJson(requestParams), REQUEST_ENCODE))
        val exception = AddEditProductUploadException(errorMessage, throwable)
        AddEditProductErrorHandler.logExceptionToCrashlytics(exception)

        Timber.w("P2#PRODUCT_UPLOAD#%s", errorMessage)
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
        sizecharts.uploadId = uploadImageAndGetId(sizecharts.filePath)
        return sizecharts
    }

    private suspend fun uploadProductVariantImages(
            productVariants: List<ProductVariantInputModel>
    ): List<ProductVariantInputModel> {
        productVariants.forEach {
            it.pictures.firstOrNull()?.let { picture ->
                picture.uploadId = uploadImageAndGetId(picture.filePath)
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
            return ""
        }

        return when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> {
                notificationManager?.onAddProgress()
                result.uploadId
            }
            is UploadResult.Error -> {
                val message = "Error upload image %s because %s".format(filePath, result.message)
                val exception = AddEditProductUploadException(message = message)
                AddEditProductErrorHandler.logExceptionToCrashlytics(exception)

                Timber.w("P2#PRODUCT_UPLOAD#%s", message)

                notificationManager?.onFailedUpload(result.message)
                ""
            }
        }
    }

    private fun filterPathOnly(imageUrlOrPathList: List<String>): List<String> =
            imageUrlOrPathList.filterNot {
                it.startsWith(AddEditProductConstants.HTTP_PREFIX)
            }

    private fun sendSuccessBroadcast() {
        val result = Intent(TkpdState.ProductService.BROADCAST_ADD_PRODUCT)
        val bundle = Bundle()
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE)
        result.putExtras(bundle)
        sendBroadcast(result)
    }
}

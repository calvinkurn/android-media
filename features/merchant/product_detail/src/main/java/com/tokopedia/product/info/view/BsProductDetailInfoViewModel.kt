package com.tokopedia.product.info.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.info.model.productdetail.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDescriptionDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingSpecificationDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable
import com.tokopedia.product.info.usecase.GetProductDetailBottomSheetUseCase
import com.tokopedia.product.info.util.ProductDetailInfoConstant
import com.tokopedia.product.info.util.ProductDetailInfoMapper
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Yehezkiel on 13/10/20
 */
class BsProductDetailInfoViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val getProductDetailBottomSheetUseCase: GetProductDetailBottomSheetUseCase,
    val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val ERROR_TYPE_DESCRIPTION_INFO = "error_description_info"
    }

    private val parcelData = MutableLiveData<ProductInfoParcelData>()

    private val _bottomSheetTitle = MutableLiveData("")
    val bottomSheetTitle: LiveData<String> get() = _bottomSheetTitle

    val bottomSheetDetailData: LiveData<Result<List<ProductDetailInfoVisitable>>> =
        parcelData.switchMap {
            val bottomSheetData = MutableLiveData<Result<List<ProductDetailInfoVisitable>>>()

            bottomSheetData.postValue(getLoadingData(productInfoParcel = it).asSuccess())

            launchCatchError(block = {
                val bottomSheetResponse = getBottomSheetData(it)
                val visitableData = doGenerateVisitable(bottomSheetResponse, it)

                setBottomSheetTitle(data = bottomSheetResponse)
                bottomSheetData.postValue(visitableData.asSuccess())
            }) { t ->
                logProductDetailBottomSheet(t)
                bottomSheetData.postValue(t.asFail())
            }
            bottomSheetData
        }

    fun setParams(parcelData: ProductInfoParcelData) {
        this.parcelData.value = parcelData
    }


    private fun getLoadingData(
        productInfoParcel: ProductInfoParcelData
    ): List<ProductDetailInfoVisitable> = if (productInfoParcel.isOpenSpecification) {
        listOf(ProductDetailInfoLoadingSpecificationDataModel())
    } else if (productInfoParcel.productInfo.catalogBottomSheet != null) {
        listOf(ProductDetailInfoLoadingDescriptionDataModel())
    } else {
        listOf(ProductDetailInfoLoadingDataModel())
    }

    private fun setBottomSheetTitle(data: PdpGetDetailBottomSheet) {
        val title = data.bottomsheetData.firstOrNull {
            it.title == ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
        }?.title.orEmpty()

        _bottomSheetTitle.postValue(title)
    }

    private suspend fun getBottomSheetData(
        productInfoParcel: ProductInfoParcelData
    ): PdpGetDetailBottomSheet {
        val requestParams = GetProductDetailBottomSheetUseCase.createParams(
            productId = productInfoParcel.productId,
            shopId = productInfoParcel.shopId,
            parentId = productInfoParcel.parentId,
            isGiftable = productInfoParcel.isGiftable
        )

        return getProductDetailBottomSheetUseCase.executeOnBackground(
            requestParams = requestParams,
            forceRefresh = true//it.forceRefresh
        )
    }

    private fun doGenerateVisitable(
        responseData: PdpGetDetailBottomSheet,
        productInfoParcel: ProductInfoParcelData
    ) = ProductDetailInfoMapper.generateVisitable(
        responseData = responseData,
        parcelData = productInfoParcel
    )

    private fun logProductDetailBottomSheet(throwable: Throwable) {
        ProductDetailLogger.logThrowable(
            throwable = throwable,
            errorType = ERROR_TYPE_DESCRIPTION_INFO,
            productId = parcelData.value?.productId.orEmpty(),
            deviceId = userSession.deviceId
        )
    }
}
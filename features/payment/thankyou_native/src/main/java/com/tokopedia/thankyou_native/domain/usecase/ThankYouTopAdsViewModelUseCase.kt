package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.model.TopAdsUIModel
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ThankYouTopAdsViewModelUseCase @Inject constructor(
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase
) : UseCase<List<TopAdsUIModel>>() {

    private val KEY_TOP_ADS_PARAM = "TOP_ADS_PARAM"
    private val KEY_THANKS_DATA_PARAM = "KEY_THANKS_DATA_PARAM"


    fun getTopAdsData(
        topAdsParams: TopAdsRequestParams,
        thanksPageData: ThanksPageData,
        onSuccess: (List<TopAdsUIModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val requestParams = RequestParams.create()
        requestParams.putObject(KEY_TOP_ADS_PARAM, topAdsParams)
        requestParams.putObject(KEY_THANKS_DATA_PARAM, thanksPageData)
        this.execute(onSuccess, onError, requestParams)
    }


    override suspend fun executeOnBackground(): List<TopAdsUIModel> {
        val params = useCaseRequestParams.getObject(KEY_TOP_ADS_PARAM) as TopAdsRequestParams
        val thanksPageData = useCaseRequestParams.getObject(KEY_THANKS_DATA_PARAM) as ThanksPageData
        var productId = ""
        if (params.type == FeatureRecommendationMapper.TYPE_TDN_PRODUCT) {
            thanksPageData.shopOrder.forEach { shopOrder ->
                shopOrder.purchaseItemList.forEach {
                    productId = if (productId.isEmpty())
                        "$productId${it.productId}"
                    else
                        "$productId,${it.productId}"
                }
            }
        }
        val topAdsViewModelList = topAdsImageViewUseCase.getImageData(
            topAdsImageViewUseCase.getQueryMap(
                "",
                params.inventoryId,
                "",
                params.itemCount.toIntOrZero(),
                params.dimen.toIntOrZero(), "",
                productId
            )
        )
        return topAdsViewModelList.map {
            TopAdsUIModel(it)
        }
    }


}
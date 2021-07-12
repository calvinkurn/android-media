package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.thankyou_native.data.mapper.FeatureRecommendationMapper
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ThankYouTopAdsViewModelUseCase @Inject constructor(
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase
) : UseCase<ArrayList<TopAdsImageViewModel>>() {

    private val KEY_TOP_ADS_PARAM = "TOP_ADS_PARAM"
    private val KEY_THANKS_DATA_PARAM = "KEY_THANKS_DATA_PARAM"


    fun getAppLinkPaymentInfo(
        topAdsParams: TopAdsRequestParams,
        thanksPageData: ThanksPageData,
        onSuccess: (ArrayList<TopAdsImageViewModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val requestParams = RequestParams.create()
        requestParams.putObject(KEY_TOP_ADS_PARAM, topAdsParams)
        requestParams.putObject(KEY_THANKS_DATA_PARAM, thanksPageData)
        this.execute(onSuccess, onError, requestParams)
    }


    override suspend fun executeOnBackground(): ArrayList<TopAdsImageViewModel> {
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
        return topAdsImageViewUseCase.getImageData(
            topAdsImageViewUseCase.getQueryMap(
                "",
                "1",
                "",
                params.itemCount.toInt(),
                params.dimen.toInt(), "",
                productId
            )
        )
    }


}
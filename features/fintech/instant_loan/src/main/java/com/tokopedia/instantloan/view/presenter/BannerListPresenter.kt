package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer
import com.tokopedia.instantloan.domain.interactor.GetBannersUserCase
import com.tokopedia.instantloan.domain.interactor.GetLendingDataUseCase
import com.tokopedia.instantloan.view.contractor.BannerContractor
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Inject

class BannerListPresenter @Inject
constructor(private val mGetBannersUserCase: GetBannersUserCase, private val mGetLendingDataUseCase: GetLendingDataUseCase)
    : BaseDaggerPresenter<BannerContractor.View>(), BannerContractor.Presenter {

    override fun loadBanners() {

        mGetBannersUserCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(typeRestResponseMap: Map<Type, RestResponse>) {
                val restResponse = typeRestResponseMap[ResponseBannerOffer::class.java]
                val responseBannerOffer = restResponse!!.getData<ResponseBannerOffer>()
//                view.renderBannerList(responseBannerOffer!!.banners)
            }
        })
    }

    override fun getLendingData() {

        mGetLendingDataUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (isViewNotAttached) {
                    return
                }
                val gqlLendingDataResponse = graphqlResponse?.getData(GqlLendingDataResponse::class.java) as GqlLendingDataResponse
                view.renderLendingData(gqlLendingDataResponse)

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

        })
    }
}

package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer
import com.tokopedia.instantloan.domain.interactor.GetBannersUserCase
import com.tokopedia.instantloan.view.contractor.BannerContractor

import java.lang.reflect.Type

import javax.inject.Inject

import rx.Subscriber

class BannerListPresenter @Inject
constructor(private val mGetBannersUserCase: GetBannersUserCase) : BaseDaggerPresenter<BannerContractor.View>(), BannerContractor.Presenter {

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
                view.renderUserList(responseBannerOffer!!.banners)
            }
        })
    }
}

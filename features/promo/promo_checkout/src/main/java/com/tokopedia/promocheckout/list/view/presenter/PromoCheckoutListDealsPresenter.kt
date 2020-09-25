package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.deals.PromoCheckoutDealsRepository
import com.tokopedia.promocheckout.common.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.model.listtravelcollectivebanner.PromoChekoutDealsBannerModel
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListDealsPresenter.Mapper.mapToLastSeen
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*


class PromoCheckoutListDealsPresenter(
        private val repository: PromoCheckoutDealsRepository,
        private val compositeSubscription: CompositeSubscription,
        private val dealsPromoUseCase: GraphqlUseCase
) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListDealsContract.Presenter {

    override fun processCheckDealPromoCode(flag: Boolean, requestParams: JsonObject) {
        view.showProgressLoading()
        compositeSubscription.add(
                repository.postVerify(false, requestParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<DealsVerifyResponse>() {
                            override fun onNext(objects: DealsVerifyResponse) {
                                view.hideProgressLoading()
                                if (objects.message_error.isNotEmpty()) {
                                    view.onErrorCheckPromo(MessageErrorException(objects.data.message))
                                } else {
                                    view.onSuccessCheckPromo(DealsCheckoutMapper.mapData(objects))
                                }
                            }

                            override fun onCompleted() {
                            }

                            override fun onError(e: Throwable) {
                                if (isViewAttached) {
                                    view.hideProgressLoading()
                                    view.onErrorCheckPromo(e)
                                }
                            }

                        })
        )
    }

    override fun getListTravelCollectiveBanner(resources: Resources) {
        val variables = HashMap<String, Any>()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_deals), PromoChekoutDealsBannerModel.Response::class.java, variables, false)
        dealsPromoUseCase.clearRequest()
        dealsPromoUseCase.addRequest(graphqlRequest)
        dealsPromoUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                val promoData = objects.getData<PromoChekoutDealsBannerModel.Response>(PromoChekoutDealsBannerModel.Response::class.java)
                if (!promoData.response.banners.isNullOrEmpty()) view.changeTitle(resources.getString(R.string.promo_title_for_this_category))
                val data = mapToLastSeen(promoData.response.banners)
                view.renderListLastSeen(data, true)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListLastSeenError(e)
                }
            }

        })
    }

    object Mapper {
        fun mapToLastSeen(data: List<PromoChekoutDealsBannerModel.Banner>?): List<PromoCheckoutLastSeenModel> {
            val mapResult = mutableListOf<PromoCheckoutLastSeenModel>()
            data?.forEachIndexed { index, banner ->
                if (!banner.attribute.promoCode.isBlank() && !banner.attribute.promoCode.equals("-")) {
                    val dataMapper = PromoCheckoutLastSeenModel(
                            id = banner.id.toInt(),
                            title = banner.attribute.description,
                            promoCode = banner.attribute.promoCode,
                            subtitle = banner.product
                    )
                    mapResult.add(dataMapper)
                }
            }
            return mapResult
        }
    }

}
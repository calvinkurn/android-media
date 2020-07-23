package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listcoupon.DataPromoCheckoutList
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.HashMap
import kotlin.collections.ArrayList

class PromoCheckoutListPresenter(private val graphqlUseCase: GraphqlUseCase,
                                 private val lastSeenPromoUseCase: GraphqlUseCase) :
        BaseDaggerPresenter<PromoCheckoutListContract.View>(), PromoCheckoutListContract.Presenter {

    override fun getListPromo(serviceId: String, categoryId: Int, page: Int, resources: Resources) {
        val variables = HashMap<String, Any>()
        variables.put(INPUT_GQL, generateInputList(page, serviceId, categoryId))
        variables.put(API_VERSION, "2.0.0")
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_list), DataPromoCheckoutList::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListError(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                val dataDetailCheckoutPromo = objects.getData<DataPromoCheckoutList>(DataPromoCheckoutList::class.java)
                view.renderList(dataDetailCheckoutPromo?.tokopointsCouponList?.tokopointsCouponData
                        ?: ArrayList(),
                        dataDetailCheckoutPromo?.tokopointsCouponList?.tokopointsPaging?.isHasNext
                                ?: false)
            }
        })
    }

    private fun generateInputList(page: Int, serviceId: String, categoryId: Int): JsonObject {
        val input = JsonObject()
        input.addProperty(SERVICE_ID, serviceId)
        input.addProperty(CATEGORY_ID, categoryId)
        input.addProperty(CATEGORY_ID_COUPON, 0)
        input.addProperty(PAGE, page)
        input.addProperty(LIMIT, 10)
        input.addProperty(INCLUDE_EXTRA_INFO, 0)
        return input
    }

    override fun getListLastSeen(categoryIDs: List<Int>, resources: Resources) {
        val variables = HashMap<String, Any>()
        variables.put(CATEGORY_IDS, categoryIDs)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_last_seen), PromoCheckoutLastSeenModel.Response::class.java, variables, false)
        lastSeenPromoUseCase.clearRequest()
        lastSeenPromoUseCase.addRequest(graphqlRequest)
        lastSeenPromoUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListLastSeenError(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                val lastSeenPromoData = objects.getData<PromoCheckoutLastSeenModel.Response>(PromoCheckoutLastSeenModel.Response::class.java)
                view.renderListLastSeen(lastSeenPromoData.promoModels)
            }
        })
    }

    override fun detachView() {
        graphqlUseCase.unsubscribe()
        lastSeenPromoUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        private val API_VERSION = "apiVersion"
        private val INPUT_GQL = "input"
        private val SERVICE_ID = "serviceID"
        private val CATEGORY_ID = "categoryID"
        private val CATEGORY_IDS = "categoryIDs"
        private val CATEGORY_ID_COUPON = "categoryIDCoupon"
        private val PAGE = "page"
        private val LIMIT = "limit"
        private val INCLUDE_EXTRA_INFO = "includeExtraInfo"
    }
}
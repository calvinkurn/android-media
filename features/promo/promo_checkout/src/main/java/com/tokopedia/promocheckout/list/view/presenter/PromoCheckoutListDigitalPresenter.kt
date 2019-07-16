package com.tokopedia.promocheckout.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.CheckVoucherDigitalUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckVoucherDigitalMapper
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*

class PromoCheckoutListDigitalPresenter(private val graphqlUseCase: GraphqlUseCase,
                                        private val checkVoucherUseCase: CheckVoucherDigitalUseCase,
                                        val checkVoucherDigitalMapper: CheckVoucherDigitalMapper) : BaseDaggerPresenter<PromoCheckoutListDigitalContract.View>(), PromoCheckoutListDigitalContract.Presenter {

    private val statusOK = "OK"

    override fun getListLastSeen(categoryIDs: List<Int>, resources: Resources) {
        val variables = HashMap<String, Any>()
        variables.put(CATEGORY_IDS, categoryIDs.toString())
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_last_seen), PromoCheckoutLastSeenModel.Response::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
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

    override fun checkPromoStackingCode(promoCode: String, promoDigitalModel: PromoDigitalModel) {
        view.showProgressLoading()

        checkVoucherUseCase.execute(checkVoucherUseCase.createRequestParams(promoCode, promoDigitalModel), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val checkVoucherData = objects.getData<CheckVoucherDigital.Response>(CheckVoucherDigital.Response::class.java).response
                if (checkVoucherData.voucherData.success) {
                    view.onSuccessCheckPromoStackingCode(checkVoucherDigitalMapper.mapData(checkVoucherData.voucherData))
                } else {
                    view.onErrorCheckPromoCode(MessageErrorException(checkVoucherData.errors.getOrNull(0)?.status))
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromoCode(e)
                }
            }

        })

    }

    override fun detachView() {
        checkVoucherUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        const val CATEGORY_IDS = "categoryIDs"
    }
}

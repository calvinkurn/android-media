package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutResponse
import com.tokopedia.expresscheckout.domain.mapper.DomainModelMapper
import com.tokopedia.expresscheckout.domain.model.AtcExpressCheckoutModel
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter : BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    private val getExpressCheckoutFormUseCase = GraphqlUseCase()
    private lateinit var atcExpressCheckoutModel: AtcExpressCheckoutModel
    private lateinit var domainModelMapper: DomainModelMapper
    private lateinit var viewModelMapper: ViewModelMapper

    override fun attachView(view: CheckoutVariantContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        getExpressCheckoutFormUseCase.unsubscribe()
        super.detachView()
    }

    override fun loadExpressCheckoutData() {
//        var json = FileUtils().readRawTextFile(view.getActivityContext(), R.raw.response_ok_triple_variant)
//        var response: ExpressCheckoutResponse = Gson().fromJson(json, ExpressCheckoutResponse::class.java)
//        val dataMapper: DataMapper = ViewModelMapper()
//        view.showData(dataMapper.convertToViewModels(response.data))

        domainModelMapper = DomainModelMapper()
        viewModelMapper = ViewModelMapper()

        view.showLoading()
        val variables = HashMap<String, Any?>()
        val params = HashMap<String, Any>()
        params.put("product_id", 4975978)
        params.put("quantity", 1)
        params.put("notes", "")
        params.put("shop_id", 153800)
        variables.put("params", params)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.getActivityContext()?.resources,
                R.raw.mutation_atc_express), ExpressCheckoutResponse::class.java, variables)
        getExpressCheckoutFormUseCase.clearRequest()
        getExpressCheckoutFormUseCase.addRequest(graphqlRequest)
        getExpressCheckoutFormUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (isViewAttached) {
                    view.hideLoading()
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                view.hideLoading()
                val expressCheckoutResponse = objects.getData<ExpressCheckoutResponse>(ExpressCheckoutResponse::class.java)
                atcExpressCheckoutModel = domainModelMapper.convertToDomainModel(expressCheckoutResponse)
                view.showData(viewModelMapper.convertToViewModels(atcExpressCheckoutModel))
            }
        })

    }

    override fun checkout() {

    }

}
package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequest
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter : BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    private val getExpressCheckoutFormUseCase = GraphqlUseCase()
    private lateinit var atcResponseModel: AtcResponseModel
    private lateinit var domainModelMapper: AtcDomainModelMapper
    private lateinit var viewModelMapper: ViewModelMapper

    override fun attachView(view: CheckoutVariantContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        getExpressCheckoutFormUseCase.unsubscribe()
        super.detachView()
    }

    override fun loadExpressCheckoutData(atcRequest: AtcRequest) {
//        var json = FileUtils().readRawTextFile(view.getActivityContext(), R.raw.response_ok_triple_variant)
//        var response: ExpressCheckoutResponse = Gson().fromJson(json, ExpressCheckoutResponse::class.java)
//        val dataMapper: DataMapper = ViewModelMapper()
//        view.showData(dataMapper.convertToViewModels(response.data))

        domainModelMapper = AtcDomainModelMapper()
        viewModelMapper = ViewModelMapper()

        view.showLoading()

        val variables = HashMap<String, Any?>()
        val mapParam = HashMap<String, Any>()
        mapParam.put("shop_id", atcRequest.shopId)
        mapParam.put("notes", atcRequest.notes)
        mapParam.put("product_id", atcRequest.productId)
        mapParam.put("quantity", atcRequest.quantity)
        variables.put("params", mapParam)

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.getActivityContext()?.resources,
                R.raw.mutation_atc_express), AtcExpressGqlResponse::class.java, variables)
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
                val expressCheckoutResponse = objects.getData<AtcExpressGqlResponse>(AtcExpressGqlResponse::class.java)
                if (expressCheckoutResponse.atcExpress.status.equals("OK")) {
                    atcResponseModel = domainModelMapper.convertToDomainModel(expressCheckoutResponse.atcExpress)
                    view.showData(viewModelMapper.convertToViewModels(atcResponseModel))
                }
            }
        })

    }

    override fun checkout() {

    }

}
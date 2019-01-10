package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.response.atc.AtcData
import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.expresscheckout.view.variant.subscriber.AtcExpressSubscriber
import com.tokopedia.expresscheckout.view.variant.subscriber.GetRatesSubscriber
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shipping_recommendation.domain.ShippingParam
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequest
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter : BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    private val getExpressCheckoutFormUseCase = GraphqlUseCase()
    private lateinit var getCourierRecommendationUseCase: GetCourierRecommendationUseCase
    private lateinit var atcResponseModel: AtcResponseModel
    private lateinit var viewModelMapper: ViewModelMapper

    override fun attachView(view: CheckoutVariantContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        getExpressCheckoutFormUseCase.unsubscribe()
        super.detachView()
    }

    override fun setAtcResponseModel(atcResponseModel: AtcResponseModel) {
        this.atcResponseModel = atcResponseModel
    }

    override fun prepareViewModel() {
        viewModelMapper = ViewModelMapper()
        view?.showData(viewModelMapper.convertToViewModels(atcResponseModel))
    }

    override fun loadExpressCheckoutData(atcRequest: AtcRequest) {
//        var json = FileUtils().readRawTextFile(view.getActivityContext(), R.raw.response_ok_triple_variant)
//        var response: ExpressCheckoutResponse = Gson().fromJson(json, ExpressCheckoutResponse::class.java)
//        val dataMapper: DataMapper = ViewModelMapper()
//        view.showData(dataMapper.convertToViewModels(response.data))

        getCourierRecommendationUseCase = GetCourierRecommendationUseCase(ShippingDurationConverter())

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
        getExpressCheckoutFormUseCase.execute(RequestParams.create(), AtcExpressSubscriber(view, this))
    }

    override fun loadShippingRates(atcResponseModel: AtcResponseModel, itemPrice: Int, quantity: Int) {
        val query = GraphqlHelper.loadRawString(view.getActivityContext()?.getResources(), R.raw.rates_v3_query)
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.districtId.toString()
        shippingParam.originPostalCode = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.postalCode
        shippingParam.originLatitude = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.latitude
        shippingParam.originLongitude = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.longitude
        shippingParam.destinationDistrictId = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.districtId?.toString()
        shippingParam.destinationPostalCode = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.postalCode
        shippingParam.destinationLatitude = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.latitude
        shippingParam.destinationLongitude = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.longitude
        shippingParam.weightInGrams = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productWeight?.toDouble() ?: 0.0
        shippingParam.shopId = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.shopId.toString()
        shippingParam.token = atcResponseModel.atcDataModel?.keroToken
        shippingParam.ut = atcResponseModel.atcDataModel?.keroUnixTime.toString()
        shippingParam.insurance = 1
        shippingParam.productInsurance = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productFinsurance ?: 0
        shippingParam.orderValue = itemPrice * quantity
        shippingParam.categoryIds = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productCatId.toString()

        val shopShipmentModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
        val serviceId = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.shipmentModel?.serviceId

        view.showLoading()
        getCourierRecommendationUseCase.execute(
                query, shippingParam, 0, shopShipmentModels,
                GetRatesSubscriber(view, this, serviceId ?: 0)
        )
    }

    override fun checkout() {

    }

}
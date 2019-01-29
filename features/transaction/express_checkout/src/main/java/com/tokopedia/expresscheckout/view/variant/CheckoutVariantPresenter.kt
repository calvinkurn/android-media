package com.tokopedia.expresscheckout.view.variant

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.request.Cart
import com.tokopedia.expresscheckout.data.entity.request.CheckoutParam
import com.tokopedia.expresscheckout.data.entity.request.CheckoutRequestParam
import com.tokopedia.expresscheckout.data.entity.request.Profile
import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.expresscheckout.data.entity.response.checkout.CheckoutExpressGqlResponse
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.expresscheckout.view.variant.subscriber.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProductChild
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.shipping_recommendation.domain.ShippingParam
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase
import com.tokopedia.shipping_recommendation.shippingduration.view.ShippingDurationConverter
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transactiondata.entity.request.*
import com.tokopedia.usecase.RequestParams
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter : BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    private val getExpressCheckoutFormUseCase = GraphqlUseCase()
    private val checkoutExpressUseCase = GraphqlUseCase()
    private lateinit var getCourierRecommendationUseCase: GetCourierRecommendationUseCase
    private lateinit var atcResponseModel: AtcResponseModel
    private lateinit var viewModelMapper: ViewModelMapper

    override fun attachView(view: CheckoutVariantContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        getExpressCheckoutFormUseCase.unsubscribe()
        getCourierRecommendationUseCase.unsubscribe()
        checkoutExpressUseCase.unsubscribe()
        super.detachView()
    }

    override fun setAtcResponseModel(atcResponseModel: AtcResponseModel) {
        this.atcResponseModel = atcResponseModel
    }

    override fun prepareViewModel(productData: ProductData) {
        viewModelMapper = ViewModelMapper()
        view?.updateFragmentViewModel(atcResponseModel)
        view?.showData(viewModelMapper.convertToViewModels(atcResponseModel, productData))
    }

    override fun loadExpressCheckoutData(atcRequestParam: AtcRequestParam) {
        view.showLoading()

        val variables = getAtcParams(atcRequestParam)

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.getActivityContext()?.resources,
                R.raw.mutation_atc_express), AtcExpressGqlResponse::class.java, variables)
        getExpressCheckoutFormUseCase.clearRequest()
        getExpressCheckoutFormUseCase.addRequest(graphqlRequest)
        getExpressCheckoutFormUseCase.execute(RequestParams.create(), AtcExpressSubscriber(view, this))
    }

    private fun getAtcParams(atcRequestParam: AtcRequestParam): HashMap<String, Any?> {
        val variables = HashMap<String, Any?>()
        val jsonTreeAtcRequest = Gson().toJsonTree(atcRequestParam)
        val jsonObjectAtcRequest = jsonTreeAtcRequest.asJsonObject
        variables.put("params", jsonObjectAtcRequest)
        return variables
    }

    override fun loadShippingRates(price: Int, quantity: Int, selectedServiceId: Int, selectedSpId: Int) {
        val query = GraphqlHelper.loadRawString(view.getActivityContext()?.getResources(), R.raw.rates_v3_query)
        val shippingParam = getShippingParam(quantity, price)

        val shopShipmentModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels

        view.showLoading()
        getCourierRecommendationUseCase = GetCourierRecommendationUseCase(ShippingDurationConverter())
        getCourierRecommendationUseCase.execute(
                query, shippingParam, 0, 0, shopShipmentModels,
                GetRatesSubscriber(view, this, selectedServiceId, selectedSpId)
        )
    }

    override fun getShippingParam(quantity: Int, price: Int): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.districtId.toString()
        shippingParam.originPostalCode = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.postalCode
        shippingParam.originLatitude = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.latitude
        shippingParam.originLongitude = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.longitude
        shippingParam.destinationDistrictId = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.districtId?.toString()
        shippingParam.destinationPostalCode = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.postalCode
        shippingParam.destinationLatitude = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.latitude
        shippingParam.destinationLongitude = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.addressModel?.longitude
        shippingParam.shopId = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.shopId.toString()
        shippingParam.token = atcResponseModel.atcDataModel?.keroToken
        shippingParam.ut = atcResponseModel.atcDataModel?.keroUnixTime.toString()
        shippingParam.insurance = 1
        shippingParam.categoryIds = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productCatId.toString()

        shippingParam.weightInKilograms = quantity * (atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productWeight
                ?: 0) / 1000.0
        shippingParam.productInsurance = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productFinsurance ?: 0
        shippingParam.orderValue = price * quantity
        return shippingParam
    }

    override fun checkoutExpress(fragmentViewModel: FragmentViewModel) {
        view.showLoadingDialog()

        if (fragmentViewModel.getProfileViewModel()?.isStateHasRemovedProfile == false) {
            val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.getActivityContext()?.resources,
                    R.raw.mutation_checkout_express), CheckoutExpressGqlResponse::class.java, getCheckoutExpressParams(fragmentViewModel))
            checkoutExpressUseCase.clearRequest()
            checkoutExpressUseCase.addRequest(graphqlRequest)
            checkoutExpressUseCase.execute(RequestParams.create(), CheckoutExpressSubscriber(view, this))
        } else {
            checkoutOneClickShipment(fragmentViewModel)
        }
    }

    override fun checkoutOneClickShipment(fragmentViewModel: FragmentViewModel) {
        view?.getAddToCartObservable(getCheckoutOcsParams(fragmentViewModel))
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OneClickShipmentSubscriber(view, this));
    }

    private fun getCheckoutOcsParams(fragmentViewModel: FragmentViewModel): AddToCartRequest {
        return AddToCartRequest.Builder()
                .productId(getProductId(fragmentViewModel))
                .notes(fragmentViewModel.getNoteViewModel()?.note)
                .quantity(fragmentViewModel.getQuantityViewModel()?.orderQuantity
                        ?: fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productQuantity
                        ?: 0)
                .shopId(fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.shopId
                        ?: 0)
                .build()
    }

    private fun getProductId(fragmentViewModel: FragmentViewModel): Int {
        var productId = 0
        if (fragmentViewModel.getProductViewModel()?.productChildrenList?.isNotEmpty() == true) {
            val productChildrenList = fragmentViewModel.getProductViewModel()?.productChildrenList
            if (productChildrenList != null) {
                for (productChild: ProductChild in productChildrenList) {
                    if (productChild.isSelected) {
                        productId = productChild.productId
                        break
                    }
                }
            }
        } else {
            productId = fragmentViewModel.getProductViewModel()?.parentId ?: 0
        }
        return productId
    }

    private fun getCheckoutExpressParams(fragmentViewModel: FragmentViewModel): HashMap<String, Any?> {
        val cart = Cart()
        cart.setDefaultProfile = fragmentViewModel.getProfileViewModel()?.isDefaultProfileCheckboxChecked
        cart.promoCode = ""
        cart.isDonation = 0
        cart.data = arrayListOf(getDataCheckoutRequest(fragmentViewModel))

        val checkoutParam = CheckoutParam()
        //        checkoutParam.accountName =
        //        checkoutParam.accountNumber =
        //        checkoutParam.bankId =

        val profile = Profile()
        profile.addressId = fragmentViewModel.getProfileViewModel()?.addressId
        profile.description = ""
        profile.gatewayCode = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.paymentModel?.gatewayCode
        profile.status = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.status
        profile.profileId = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.id
        profile.checkoutParam = checkoutParam

        val checkoutRequestParam = CheckoutRequestParam()
        checkoutRequestParam.carts = cart
        checkoutRequestParam.profile = profile

        val variables = HashMap<String, Any?>()
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkoutRequestParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables.put("params", jsonObjectCheckoutRequest)
        return variables
    }

    private fun getDataCheckoutRequest(fragmentViewModel: FragmentViewModel): DataCheckoutRequest {
        val dropshipDataCheckoutRequest = DropshipDataCheckoutRequest()
        dropshipDataCheckoutRequest.name = ""
        dropshipDataCheckoutRequest.telpNo = ""

        val productDataCheckoutRequest = ProductDataCheckoutRequest()
        productDataCheckoutRequest.productId = getProductId(fragmentViewModel)
        productDataCheckoutRequest.productQuantity = fragmentViewModel.getQuantityViewModel()?.orderQuantity ?: fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productQuantity ?: 1
        productDataCheckoutRequest.productNotes = fragmentViewModel.getNoteViewModel()?.note
        productDataCheckoutRequest.isPurchaseProtection = false

        val shippingInfoCheckoutRequest = ShippingInfoCheckoutRequest()
        shippingInfoCheckoutRequest.ratesId = 0.toString()
        shippingInfoCheckoutRequest.shippingId = fragmentViewModel.getInsuranceViewModel()?.shippingId ?: 0
        shippingInfoCheckoutRequest.spId = fragmentViewModel.getInsuranceViewModel()?.spId ?: 0

        val shopProductCheckoutRequest = ShopProductCheckoutRequest()
        shopProductCheckoutRequest.isDropship = 0
        shopProductCheckoutRequest.finsurance = if (fragmentViewModel.getInsuranceViewModel()?.isChecked == true) 1 else 0
        shopProductCheckoutRequest.isPreorder = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productIsPreorder ?: 0
        shopProductCheckoutRequest.shopId = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.shopId ?: 0
        shopProductCheckoutRequest.dropshipData = dropshipDataCheckoutRequest
        shopProductCheckoutRequest.productData = arrayListOf(productDataCheckoutRequest)
        shopProductCheckoutRequest.shippingInfo = shippingInfoCheckoutRequest

        val dataCheckoutRequest = DataCheckoutRequest()
        dataCheckoutRequest.addressId = fragmentViewModel.getProfileViewModel()?.addressId ?: 0
        dataCheckoutRequest.shopProducts = arrayListOf(shopProductCheckoutRequest)

        return dataCheckoutRequest
    }

    override fun hitOldCheckout(fragmentViewModel: FragmentViewModel) {
        view?.showLoadingDialog()
        view?.getCheckoutObservable(getOldCheckoutParams(fragmentViewModel))
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(CheckoutSubscriber(view, this));
    }

    private fun getOldCheckoutParams(fragmentViewModel: FragmentViewModel): CheckoutRequest {
        val checkoutRequest = CheckoutRequest.Builder()
                .isDonation(0)
                .promoCode("")
                .data(arrayListOf(getDataCheckoutRequest(fragmentViewModel)))

        return checkoutRequest.build()
    }
}
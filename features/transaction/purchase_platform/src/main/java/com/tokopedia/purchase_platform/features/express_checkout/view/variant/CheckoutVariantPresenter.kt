package com.tokopedia.purchase_platform.features.express_checkout.view.variant

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.purchase_platform.common.data.model.param.EditAddressParam
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
import com.tokopedia.purchase_platform.common.data.model.request.checkout.*
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.CheckoutUseCase
import com.tokopedia.purchase_platform.features.checkout.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.features.express_checkout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.purchase_platform.features.express_checkout.domain.mapper.checkout.CheckoutDomainModelMapper
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.AtcResponseModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.usecase.DoAtcExpressUseCase
import com.tokopedia.purchase_platform.features.express_checkout.domain.usecase.DoCheckoutExpressUseCase
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.mapper.ViewModelMapper
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.subscriber.*
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.FragmentUiModel
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.ProductChild
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter @Inject constructor(private val doAtcExpressUseCase: DoAtcExpressUseCase,
                                                   private val doCheckoutExpressUseCase: DoCheckoutExpressUseCase,
                                                   private val ratesUseCase: GetRatesUseCase,
                                                   private val addToCartOcsUseCase: AddToCartOcsUseCase,
                                                   private val stateConverter: RatesResponseStateConverter,
                                                   private val atcDomainModelMapper: AtcDomainModelMapper,
                                                   private val checkoutDomainModelMapper: CheckoutDomainModelMapper,
                                                   private var viewModelMapper: ViewModelMapper,
                                                   private val userSessionInterface: UserSessionInterface,
                                                   private val checkoutUseCase: CheckoutUseCase,
                                                   private val editAddressUseCase: EditAddressUseCase) :
        BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    private lateinit var atcResponseModel: AtcResponseModel

    override fun detachView() {
        doAtcExpressUseCase.unsubscribe()
        ratesUseCase.unsubscribe()
        doCheckoutExpressUseCase.unsubscribe()
        addToCartOcsUseCase.unsubscribe()
        super.detachView()
    }

    override fun setAtcResponseModel(atcResponseModel: AtcResponseModel) {
        this.atcResponseModel = atcResponseModel
    }

    override fun prepareViewModel(productData: ProductData?) {
        view?.updateFragmentViewModel(atcResponseModel)
        view?.showData(viewModelMapper.convertToViewModels(atcResponseModel, productData))
    }

    override fun loadExpressCheckoutData(atcRequestParam: AtcRequestParam) {
        view?.showLoading()

        doAtcExpressUseCase.setParams(atcRequestParam)
        doAtcExpressUseCase.execute(RequestParams.create(), DoAtcExpressSubscriber(view, this, atcDomainModelMapper))
    }

    override fun loadShippingRates(price: Long, quantity: Int, selectedServiceId: Int, selectedSpId: Int) {
        val shippingParam = getShippingParam(quantity, price)

        val shopShipmentModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels
                ?: arrayListOf()

        view?.showLoading()

        val param = RatesParam.Builder(shopShipmentModels, shippingParam).build()
        ratesUseCase.execute(param)
                .map { shippingRecommendationData ->
                    stateConverter.fillState(shippingRecommendationData, shopShipmentModels,
                            selectedSpId, selectedServiceId)
                }
                .subscribe(
                        GetRatesSubscriber(view, this, selectedServiceId, selectedSpId)
                )
    }

    override fun getShippingParam(quantity: Int, price: Long): ShippingParam {
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
        shippingParam.productInsurance = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productFinsurance
                ?: 0
        shippingParam.orderValue = price * quantity
        return shippingParam
    }

    override fun checkoutExpress(fragmentUiModel: FragmentUiModel, trackerAttribution: String?,
                                 trackerListName: String?) {
        view?.showLoadingDialog()
        view?.generateFingerprintPublicKey()
        if (fragmentUiModel.getProfileViewModel()?.isStateHasRemovedProfile == false) {
            doCheckoutExpressUseCase.setParams(fragmentUiModel, getDataCheckoutRequest(fragmentUiModel))
            doCheckoutExpressUseCase.execute(RequestParams.create(), DoCheckoutExpressSubscriber(view, this, checkoutDomainModelMapper))
        } else {
            checkoutOneClickShipment(fragmentUiModel, trackerAttribution, trackerListName)
        }
    }

    override fun checkoutOneClickShipment(fragmentUiModel: FragmentUiModel, trackerAttribution: String?,
                                          trackerListName: String?) {
        val requestParams = RequestParams.create()
        val addToCartOcsRequestParams = AddToCartOcsRequestParams()
        addToCartOcsRequestParams.productId = getProductId(fragmentUiModel).toLong()
        addToCartOcsRequestParams.shopId = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.shopId
                ?: 0
        addToCartOcsRequestParams.quantity = fragmentUiModel.getQuantityViewModel()?.orderQuantity
                ?: fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productQuantity
                        ?: 0
        addToCartOcsRequestParams.notes = fragmentUiModel.getNoteViewModel()?.note ?: ""
        addToCartOcsRequestParams.warehouseId = 0
        addToCartOcsRequestParams.trackerAttribution = trackerAttribution ?: ""
        addToCartOcsRequestParams.trackerListName = trackerListName ?: ""
        addToCartOcsRequestParams.isTradeIn = false

        requestParams.putObject(AddToCartOcsUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartOcsRequestParams)
        addToCartOcsUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DoOneClickShipmentAtcSubscriber(view, this))
    }

    override fun updateAddress(fragmentUiModel: FragmentUiModel, latitude: String, longitude: String) {
        val addressModel = fragmentUiModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.addressModel
        val requestParamsMap = AuthHelper.generateParamsNetwork(
                userSessionInterface.userId, userSessionInterface.deviceId, TKPDMapParam<String, String>())
        requestParamsMap.put(EditAddressParam.ADDRESS_ID, addressModel?.addressId?.toString() ?: "")
        requestParamsMap.put(EditAddressParam.ADDRESS_NAME, addressModel?.addressName ?: "")
        requestParamsMap.put(EditAddressParam.ADDRESS_STREET, addressModel?.addressStreet ?: "")
        requestParamsMap.put(EditAddressParam.POSTAL_CODE, addressModel?.postalCode ?: "")
        requestParamsMap.put(EditAddressParam.DISTRICT_ID, addressModel?.districtId?.toString()
                ?: "")
        requestParamsMap.put(EditAddressParam.CITY_ID, addressModel?.cityId?.toString() ?: "")
        requestParamsMap.put(EditAddressParam.PROVINCE_ID, addressModel?.provinceId?.toString()
                ?: "")
        requestParamsMap.put(EditAddressParam.RECEIVER_NAME, addressModel?.receiverName ?: "")
        requestParamsMap.put(EditAddressParam.RECEIVER_PHONE, addressModel?.phone ?: "")
        requestParamsMap.put(EditAddressParam.LATITUDE, latitude)
        requestParamsMap.put(EditAddressParam.LONGITUDE, longitude)
        val requestParams = RequestParams.create()
        requestParams.putAllString(requestParamsMap)
        editAddressUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DoEditAddressSubscriber(view, this, latitude, longitude))
    }

    private fun getProductId(fragmentUiModel: FragmentUiModel): Int {
        var productId = 0
        if (fragmentUiModel.getProductViewModel()?.productChildrenList?.isNotEmpty() == true) {
            val productChildrenList = fragmentUiModel.getProductViewModel()?.productChildrenList
            if (productChildrenList != null) {
                for (productChild: ProductChild in productChildrenList) {
                    if (productChild.isSelected) {
                        productId = productChild.productId
                        break
                    }
                }
            }
        } else {
            productId = fragmentUiModel.getProductViewModel()?.parentId ?: 0
        }
        return productId
    }

    private fun getDataCheckoutRequest(fragmentUiModel: FragmentUiModel): DataCheckoutRequest {
        val dropshipDataCheckoutRequest = DropshipDataCheckoutRequest()
        dropshipDataCheckoutRequest.name = ""
        dropshipDataCheckoutRequest.telpNo = ""

        val productDataCheckoutRequest = ProductDataCheckoutRequest()
        productDataCheckoutRequest.productId = getProductId(fragmentUiModel)
        productDataCheckoutRequest.productQuantity = fragmentUiModel.getQuantityViewModel()?.orderQuantity
                ?: fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productQuantity
                        ?: 1
        productDataCheckoutRequest.productNotes = fragmentUiModel.getNoteViewModel()?.note
        productDataCheckoutRequest.isPurchaseProtection = false

        val shippingInfoCheckoutRequest = ShippingInfoCheckoutRequest()
        shippingInfoCheckoutRequest.ratesId = 0.toString()
        shippingInfoCheckoutRequest.shippingId = fragmentUiModel.getInsuranceViewModel()?.shippingId
                ?: 0
        shippingInfoCheckoutRequest.spId = fragmentUiModel.getInsuranceViewModel()?.spId ?: 0

        val shopProductCheckoutRequest = ShopProductCheckoutRequest()
        shopProductCheckoutRequest.isDropship = 0
        shopProductCheckoutRequest.finsurance = if (fragmentUiModel.getInsuranceViewModel()?.isChecked == true) 1 else 0
        shopProductCheckoutRequest.isPreorder = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productIsPreorder
                ?: 0
        shopProductCheckoutRequest.shopId = fragmentUiModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopModel?.shopId
                ?: 0
        shopProductCheckoutRequest.dropshipData = dropshipDataCheckoutRequest
        shopProductCheckoutRequest.productData = arrayListOf(productDataCheckoutRequest)
        shopProductCheckoutRequest.shippingInfo = shippingInfoCheckoutRequest

        val dataCheckoutRequest = DataCheckoutRequest()
        dataCheckoutRequest.addressId = fragmentUiModel.getProfileViewModel()?.addressId ?: 0
        dataCheckoutRequest.shopProducts = arrayListOf(shopProductCheckoutRequest)

        return dataCheckoutRequest
    }

    override fun hitOldCheckout(fragmentUiModel: FragmentUiModel) {
        view?.showLoadingDialog()
        val requestParams = RequestParams.create()
        requestParams.putObject(CheckoutUseCase.PARAM_CARTS, getOldCheckoutParams(fragmentUiModel))
        requestParams.putBoolean(CheckoutUseCase.PARAM_ONE_CLICK_SHIPMENT, true)
        requestParams.putBoolean(CheckoutUseCase.PARAM_IS_EXPRESS, true)

        checkoutUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DoCheckoutSubscriber(view, this))
    }

    private fun getOldCheckoutParams(fragmentUiModel: FragmentUiModel): CheckoutRequest {
        val checkoutRequest = CheckoutRequest.Builder()
                .isDonation(0)
                .promoCode("")
                .data(arrayListOf(getDataCheckoutRequest(fragmentUiModel)))

        return checkoutRequest.build()
    }
}
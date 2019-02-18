package com.tokopedia.expresscheckout.view.variant

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.expresscheckout.domain.mapper.checkout.CheckoutDomainModelMapper
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.domain.usecase.DoAtcExpressUseCase
import com.tokopedia.expresscheckout.domain.usecase.DoCheckoutExpressUseCase
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.expresscheckout.view.variant.subscriber.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.FragmentViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProductChild
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.shipping_recommendation.domain.ShippingParam
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.EditAddressParam
import com.tokopedia.transactiondata.entity.request.*
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
                                                   private val getCourierRecommendationUseCase: GetCourierRecommendationUseCase,
                                                   private val atcDomainModelMapper: AtcDomainModelMapper,
                                                   private val checkoutDomainModelMapper: CheckoutDomainModelMapper,
                                                   private var viewModelMapper: ViewModelMapper,
                                                   private val userSessionInterface: UserSessionInterface) :
        BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    private lateinit var atcResponseModel: AtcResponseModel

    override fun detachView() {
        doAtcExpressUseCase.unsubscribe()
        getCourierRecommendationUseCase.unsubscribe()
        doCheckoutExpressUseCase.unsubscribe()
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
        val query = GraphqlHelper.loadRawString(view.getActivityContext()?.resources, R.raw.rates_v3_query)
        val shippingParam = getShippingParam(quantity, price)

        val shopShipmentModels = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.shopShipmentModels

        view?.showLoading()
        getCourierRecommendationUseCase.execute(
                query, -1, shippingParam, 0, 0, shopShipmentModels,
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
        shippingParam.productInsurance = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productFinsurance ?: 0
        shippingParam.orderValue = price * quantity
        return shippingParam
    }

    override fun checkoutExpress(fragmentViewModel: FragmentViewModel) {
        view?.showLoadingDialog()
        view?.generateFingerprintPublicKey()
        if (fragmentViewModel.getProfileViewModel()?.isStateHasRemovedProfile == false) {
            doCheckoutExpressUseCase.setParams(fragmentViewModel, getDataCheckoutRequest(fragmentViewModel))
            doCheckoutExpressUseCase.execute(RequestParams.create(), DoCheckoutExpressSubscriber(view, this, checkoutDomainModelMapper))
        } else {
            checkoutOneClickShipment(fragmentViewModel)
        }
    }

    override fun checkoutOneClickShipment(fragmentViewModel: FragmentViewModel) {
        view?.getAddToCartObservable(getCheckoutOcsParams(fragmentViewModel))
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(DoOneClickShipmentAtcSubscriber(view, this))
    }

    override fun updateAddress(fragmentViewModel: FragmentViewModel, latitude: String, longitude: String) {
        val addressModel = fragmentViewModel.atcResponseModel?.atcDataModel?.userProfileModelDefaultModel?.addressModel
        val requestParamsMap = AuthUtil.generateParamsNetwork(
                userSessionInterface.userId, userSessionInterface.deviceId, TKPDMapParam<String, String>())
        requestParamsMap.put(EditAddressParam.ADDRESS_ID, addressModel?.addressId?.toString())
        requestParamsMap.put(EditAddressParam.ADDRESS_NAME, addressModel?.addressName)
        requestParamsMap.put(EditAddressParam.ADDRESS_STREET, addressModel?.addressStreet)
        requestParamsMap.put(EditAddressParam.POSTAL_CODE, addressModel?.postalCode)
        requestParamsMap.put(EditAddressParam.DISTRICT_ID, addressModel?.districtId?.toString())
        requestParamsMap.put(EditAddressParam.CITY_ID, addressModel?.cityId?.toString())
        requestParamsMap.put(EditAddressParam.PROVINCE_ID, addressModel?.provinceId?.toString())
        requestParamsMap.put(EditAddressParam.RECEIVER_NAME, addressModel?.receiverName)
        requestParamsMap.put(EditAddressParam.RECEIVER_PHONE, addressModel?.phone)
        requestParamsMap.put(EditAddressParam.LATITUDE, latitude)
        requestParamsMap.put(EditAddressParam.LONGITUDE, longitude)
        val requestParams = RequestParams.create()
        requestParams.putAllString(requestParamsMap)
        view?.getEditAddressObservable(requestParams)
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(DoEditAddressSubscriber(view, this, latitude, longitude))
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
                ?.subscribe(DoCheckoutSubscriber(view, this))
    }

    private fun getOldCheckoutParams(fragmentViewModel: FragmentViewModel): CheckoutRequest {
        val checkoutRequest = CheckoutRequest.Builder()
                .isDonation(0)
                .promoCode("")
                .data(arrayListOf(getDataCheckoutRequest(fragmentViewModel)))

        return checkoutRequest.build()
    }
}
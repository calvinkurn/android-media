package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.utils.ErrorHandler.Companion.getErrorMessage
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingDurationPresenter @Inject constructor(private val ratesUseCase: GetRatesUseCase,
                                                    private val ratesApiUseCase: GetRatesApiUseCase,
                                                    private val stateConverter: RatesResponseStateConverter,
                                                    private val shippingCourierConverter: ShippingCourierConverter) : BaseDaggerPresenter<ShippingDurationContract.View>(), ShippingDurationContract.Presenter {

    private var view: ShippingDurationContract.View? = null

    override fun attachView(view: ShippingDurationContract.View) {
        super.attachView(view)
        this.view = view
    }

    override fun detachView() {
        super.detachView()
        ratesUseCase.unsubscribe()
        ratesApiUseCase.unsubscribe()
    }

    /**
     * Calls rates
     */
    override fun loadCourierRecommendation(shipmentDetailData: ShipmentDetailData,
                                           selectedServiceId: Int,
                                           shopShipmentList: List<ShopShipment>,
                                           codHistory: Int, isCorner: Boolean,
                                           isLeasing: Boolean, pslCode: String,
                                           products: List<Product>, cartString: String,
                                           isTradeInDropOff: Boolean,
                                           recipientAddressModel: RecipientAddressModel,
                                           isFulfillment: Boolean, preOrderTime: Int,
                                           mvc: String) {
        if (view != null) {
            view!!.showLoading()
            val shippingParam = getShippingParam(shipmentDetailData, products, cartString,
                    isTradeInDropOff, recipientAddressModel)
            var selectedSpId = 0
            if (shipmentDetailData.selectedCourier != null) {
                selectedSpId = shipmentDetailData.selectedCourier!!.shipperProductId
            }
            loadDuration(selectedSpId, selectedServiceId, codHistory, isCorner, isLeasing,
                    shopShipmentList, isTradeInDropOff, shippingParam, pslCode, mvc)
        }
    }

    private fun loadDuration(selectedSpId: Int, selectedServiceId: Int, codHistory: Int,
                             isCorner: Boolean, isLeasing: Boolean,
                             shopShipmentList: List<ShopShipment>, isRatesTradeInApi: Boolean,
                             shippingParam: ShippingParam, pslCode: String,
                             mvc: String) {
        val param = RatesParam.Builder(shopShipmentList, shippingParam)
                .isCorner(isCorner)
                .codHistory(codHistory)
                .isLeasing(isLeasing)
                .promoCode(pslCode)
                .mvc(mvc)
                .build()
        val observable: Observable<ShippingRecommendationData> = if (isRatesTradeInApi) {
            ratesApiUseCase.execute(param)
        } else {
            ratesUseCase.execute(param)
        }
        observable
                .map { shippingRecommendationData: ShippingRecommendationData ->
                    stateConverter.fillState(shippingRecommendationData, shopShipmentList,
                            selectedSpId, selectedServiceId)
                }
                .subscribe(
                        object : Subscriber<ShippingRecommendationData>() {
                            override fun onCompleted() {
                                //no-op
                            }

                            override fun onError(e: Throwable) {
                                if (view != null) {
                                    view!!.showErrorPage(getErrorMessage(view!!.getActivity(), e))
                                    view!!.stopTrace()
                                }
                            }

                            override fun onNext(shippingRecommendationData: ShippingRecommendationData) {
                                if (view != null) {
                                    view!!.hideLoading()
                                    if (shippingRecommendationData.errorId != null && shippingRecommendationData.errorId == ErrorProductData.ERROR_RATES_NOT_AVAILABLE) {
                                        view!!.showNoCourierAvailable(shippingRecommendationData.errorMessage)
                                        view!!.stopTrace()
                                    } else if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                                        if (view!!.isDisableCourierPromo()) {
                                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                                shippingDurationUiModel.serviceData.isPromo = 0
                                                for (productData in shippingDurationUiModel.serviceData.products) {
                                                    productData.promoCode = ""
                                                }
                                            }
                                        }
                                        view!!.showData(shippingRecommendationData.shippingDurationUiModels, shippingRecommendationData.listLogisticPromo, shippingRecommendationData.preOrderModel)
                                        view!!.stopTrace()
                                    } else {
                                        view!!.showNoCourierAvailable(view!!.getActivity().getString(R.string.label_no_courier_bottomsheet_message))
                                        view!!.stopTrace()
                                    }
                                }
                            }
                        }
                )
    }

    private fun getShippingParam(shipmentDetailData: ShipmentDetailData,
                                 products: List<Product>,
                                 cartString: String,
                                 isTradeInDropOff: Boolean,
                                 recipientAddressModel: RecipientAddressModel): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = shipmentDetailData.shipmentCartData!!.originDistrictId
        shippingParam.originPostalCode = shipmentDetailData.shipmentCartData!!.originPostalCode
        shippingParam.originLatitude = shipmentDetailData.shipmentCartData!!.originLatitude
        shippingParam.originLongitude = shipmentDetailData.shipmentCartData!!.originLongitude
        shippingParam.weightInKilograms = shipmentDetailData.shipmentCartData!!.weight / 1000
        shippingParam.weightActualInKilograms = shipmentDetailData.shipmentCartData!!.weightActual / 1000
        shippingParam.shopId = shipmentDetailData.shopId
        shippingParam.shopTier = shipmentDetailData.shipmentCartData!!.shopTier
        shippingParam.token = shipmentDetailData.shipmentCartData!!.token
        shippingParam.ut = shipmentDetailData.shipmentCartData!!.ut
        shippingParam.insurance = shipmentDetailData.shipmentCartData!!.insurance
        shippingParam.productInsurance = shipmentDetailData.shipmentCartData!!.productInsurance
        shippingParam.orderValue = shipmentDetailData.shipmentCartData!!.orderValue
        shippingParam.categoryIds = shipmentDetailData.shipmentCartData!!.categoryIds
        shippingParam.isBlackbox = shipmentDetailData.isBlackbox
        shippingParam.isPreorder = shipmentDetailData.preorder
        shippingParam.addressId = shipmentDetailData.addressId
        shippingParam.isTradein = shipmentDetailData.isTradein
        shippingParam.products = products
        shippingParam.uniqueId = cartString
        shippingParam.isTradeInDropOff = isTradeInDropOff
        shippingParam.preOrderDuration = shipmentDetailData.shipmentCartData!!.preOrderDuration
        shippingParam.isFulfillment = shipmentDetailData.shipmentCartData!!.isFulfillment
        shippingParam.boMetadata = shipmentDetailData.shipmentCartData!!.boMetadata
        if (isTradeInDropOff && recipientAddressModel.locationDataModel != null) {
            shippingParam.destinationDistrictId = recipientAddressModel.locationDataModel.district
            shippingParam.destinationPostalCode = recipientAddressModel.locationDataModel.postalCode
            shippingParam.destinationLatitude = recipientAddressModel.locationDataModel.latitude
            shippingParam.destinationLongitude = recipientAddressModel.locationDataModel.longitude
        } else {
            shippingParam.destinationDistrictId = shipmentDetailData.shipmentCartData!!.destinationDistrictId
            shippingParam.destinationPostalCode = shipmentDetailData.shipmentCartData!!.destinationPostalCode
            shippingParam.destinationLatitude = shipmentDetailData.shipmentCartData!!.destinationLatitude
            shippingParam.destinationLongitude = shipmentDetailData.shipmentCartData!!.destinationLongitude
        }
        return shippingParam
    }

    override fun getCourierItemData(shippingCourierUiModels: List<ShippingCourierUiModel>): CourierItemData? {
        for (shippingCourierUiModel in shippingCourierUiModels) {
            if (shippingCourierUiModel.productData.isRecommend) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel)
            }
        }
        return null
    }

    override fun getCourierItemDataById(spId: Int, shippingCourierUiModels: List<ShippingCourierUiModel>): CourierItemData? {
        for (shippingCourierUiModel in shippingCourierUiModels) {
            if (shippingCourierUiModel.productData.shipperProductId == spId) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel)
            }
        }
        return null
    }
}
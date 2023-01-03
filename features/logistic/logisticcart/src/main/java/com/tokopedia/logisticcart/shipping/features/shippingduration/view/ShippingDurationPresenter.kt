package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.DividerModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.utils.ErrorHandler.Companion.getErrorMessage
import rx.Observable
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingDurationPresenter @Inject constructor(
    private val ratesUseCase: GetRatesUseCase,
    private val ratesApiUseCase: GetRatesApiUseCase,
    private val stateConverter: RatesResponseStateConverter
) : BaseDaggerPresenter<ShippingDurationContract.View>(), ShippingDurationContract.Presenter {

    private var view: ShippingDurationContract.View? = null
    var shippingData: ShippingRecommendationData? = null

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
    override fun loadCourierRecommendation(
        shipmentDetailData: ShipmentDetailData,
        selectedServiceId: Int,
        shopShipmentList: List<ShopShipment>,
        codHistory: Int,
        isCorner: Boolean,
        isLeasing: Boolean,
        pslCode: String,
        products: List<Product>,
        cartString: String,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?,
        isFulfillment: Boolean,
        preOrderTime: Int,
        mvc: String,
        cartData: String,
        isOcc: Boolean,
        isDisableCourierPromo: Boolean
    ) {
        view?.let {
            it.showLoading()
            val shippingParam = getShippingParam(
                shipmentDetailData,
                products,
                cartString,
                isTradeInDropOff,
                recipientAddressModel
            )
            var selectedSpId = 0
            shipmentDetailData.selectedCourier?.let { selectedCourier ->
                selectedSpId = selectedCourier.shipperProductId
            }
            loadDuration(
                selectedSpId = selectedSpId,
                selectedServiceId = selectedServiceId,
                codHistory = codHistory,
                isCorner = isCorner,
                isLeasing = isLeasing,
                shopShipmentList = shopShipmentList,
                isRatesTradeInApi = isTradeInDropOff,
                shippingParam = shippingParam,
                pslCode = pslCode,
                mvc = mvc,
                cartData = cartData,
                isOcc = isOcc,
                disableCourierPromo = isDisableCourierPromo
            )
        }
    }

    private fun loadDuration(
        selectedSpId: Int,
        selectedServiceId: Int,
        codHistory: Int,
        isCorner: Boolean,
        isLeasing: Boolean,
        shopShipmentList: List<ShopShipment>,
        isRatesTradeInApi: Boolean,
        shippingParam: ShippingParam,
        pslCode: String,
        mvc: String,
        cartData: String,
        isOcc: Boolean,
        disableCourierPromo: Boolean
    ) {
        val param = RatesParam.Builder(shopShipmentList, shippingParam)
            .isCorner(isCorner)
            .codHistory(codHistory)
            .isLeasing(isLeasing)
            .promoCode(pslCode)
            .mvc(mvc)
            .isOcc(isOcc)
            .cartData(cartData)
            .build()
        val observable: Observable<ShippingRecommendationData> = if (isRatesTradeInApi) {
            ratesApiUseCase.execute(param)
        } else {
            ratesUseCase.execute(param)
        }
        observable
            .map { shippingRecommendationData: ShippingRecommendationData ->
                stateConverter.fillState(
                    shippingRecommendationData,
                    shopShipmentList,
                    selectedSpId,
                    selectedServiceId
                )
            }
            .subscribe(
                object : Subscriber<ShippingRecommendationData>() {
                    override fun onCompleted() {
                        // no-op
                    }

                    override fun onError(e: Throwable) {
                        view?.let {
                            it.showErrorPage(getErrorMessage(it.getActivity(), e))
                            it.stopTrace()
                        }
                    }

                    override fun onNext(shippingRecommendationData: ShippingRecommendationData) {
                        view?.let {
                            it.hideLoading()
                            if (shippingRecommendationData.errorId != null && shippingRecommendationData.errorId == ErrorProductData.ERROR_RATES_NOT_AVAILABLE) {
                                it.showNoCourierAvailable(shippingRecommendationData.errorMessage)
                                it.stopTrace()
                            } else if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                                if (disableCourierPromo) {
                                    for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                        shippingDurationUiModel.serviceData.isPromo = 0
                                        for (productData in shippingDurationUiModel.serviceData.products) {
                                            productData.promoCode = ""
                                        }
                                    }
                                }
                                shippingData = shippingRecommendationData
                                it.showData(
                                    convertServiceListToUiModel(
                                        shippingRecommendationData.shippingDurationUiModels,
                                        shippingRecommendationData.listLogisticPromo,
                                        shippingRecommendationData.preOrderModel,
                                        isOcc
                                    )
                                )

                                // tracker
                                val hasCourierPromo = checkHasCourierPromo(shippingRecommendationData.shippingDurationUiModels)
                                if (hasCourierPromo) {
                                    it.sendAnalyticCourierPromo(shippingRecommendationData.shippingDurationUiModels)
                                }
                                it.sendAnalyticPromoLogistic(shippingRecommendationData.listLogisticPromo)

                                it.stopTrace()
                            } else {
                                it.showNoCourierAvailable(
                                    it.getActivity()
                                        .getString(R.string.label_no_courier_bottomsheet_message)
                                )
                                it.stopTrace()
                            }
                        }
                    }
                }
            )
    }

    private fun getShippingParam(
        shipmentDetailData: ShipmentDetailData,
        products: List<Product>,
        cartString: String,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?
    ): ShippingParam {
        val shippingParam = ShippingParam()
        shippingParam.originDistrictId = shipmentDetailData.shipmentCartData!!.originDistrictId
        shippingParam.originPostalCode = shipmentDetailData.shipmentCartData!!.originPostalCode
        shippingParam.originLatitude = shipmentDetailData.shipmentCartData!!.originLatitude
        shippingParam.originLongitude = shipmentDetailData.shipmentCartData!!.originLongitude
        shippingParam.weightInKilograms = shipmentDetailData.shipmentCartData!!.weight / 1000
        shippingParam.weightActualInKilograms =
            shipmentDetailData.shipmentCartData!!.weightActual / 1000
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
        if (isTradeInDropOff && recipientAddressModel?.locationDataModel != null) {
            shippingParam.destinationDistrictId = recipientAddressModel.locationDataModel.district
            shippingParam.destinationPostalCode = recipientAddressModel.locationDataModel.postalCode
            shippingParam.destinationLatitude = recipientAddressModel.locationDataModel.latitude
            shippingParam.destinationLongitude = recipientAddressModel.locationDataModel.longitude
        } else {
            shippingParam.destinationDistrictId =
                shipmentDetailData.shipmentCartData!!.destinationDistrictId
            shippingParam.destinationPostalCode =
                shipmentDetailData.shipmentCartData!!.destinationPostalCode
            shippingParam.destinationLatitude =
                shipmentDetailData.shipmentCartData!!.destinationLatitude
            shippingParam.destinationLongitude =
                shipmentDetailData.shipmentCartData!!.destinationLongitude
        }
        return shippingParam
    }

    private fun findRecommendedCourier(
        serviceData: ServiceData,
        shippingCourierUiModelList: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel? {
        return shippingCourierUiModelList.takeIf { serviceData.selectedShipperProductId > 0 }
            ?.find { shippingCourierUiModel -> shippingCourierUiModel.productData.shipperProductId == serviceData.selectedShipperProductId }
            ?: shippingCourierUiModelList.firstOrNull { it.productData.isRecommend && !it.productData.isUiRatesHidden && it.productData.error?.errorMessage?.isEmpty() != false }
    }

    private fun findAutoSelectedCourier(
        serviceData: ServiceData,
        shippingCourierUiModelList: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel {
        return findRecommendedCourier(serviceData, shippingCourierUiModelList)
            ?: shippingCourierUiModelList.firstOrNull { !it.productData.isUiRatesHidden && (it.productData.error?.errorMessage?.isEmpty() != false) }
            ?: shippingCourierUiModelList.first()
    }

    override fun getCourierItemDataById(
        spId: Int,
        shippingCourierUiModels: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel? {
        for (shippingCourierUiModel in shippingCourierUiModels) {
            if (shippingCourierUiModel.productData.shipperProductId == spId) {
                return shippingCourierUiModel
            }
        }
        return null
    }

    override fun convertServiceListToUiModel(
        shippingDurationUiModels: List<ShippingDurationUiModel>,
        promoUiModel: List<LogisticPromoUiModel>,
        preOrderModel: PreOrderModel?,
        isOcc: Boolean
    ): MutableList<RatesViewModelType> {
        val eligibleServices = shippingDurationUiModels.filter { !it.serviceData.isUiRatesHidden }
        if (!isOcc && promoUiModel.any { it.etaData.textEta.isEmpty() && it.etaData.errorCode == 1 }) {
            initiateShowcase(eligibleServices)
        }
        val uiModelList: MutableList<RatesViewModelType> = mutableListOf<RatesViewModelType>().apply {
            addAll(eligibleServices)
        }

        if (promoUiModel.isNotEmpty()) {
            uiModelList.addAll(0, promoUiModel + listOf<RatesViewModelType>(DividerModel()))
        }

        preOrderModel?.let {
            if (it.display) {
                uiModelList.add(0, it)
            }
        }

        if (!isOcc && eligibleServices.getOrNull(0)?.etaErrorCode == 1) {
            uiModelList.add(0, NotifierModel(NotifierModel.TYPE_DEFAULT))
        }

        return uiModelList
    }

    private fun initiateShowcase(services: List<ShippingDurationUiModel>) {
        services.firstOrNull()?.isShowShowCase = true
    }

    private fun getRatesDataFromLogisticPromo(serId: Int): ShippingDurationUiModel? {
        shippingData?.shippingDurationUiModels?.firstOrNull { it.serviceData.serviceId == serId }
            ?.let {
                return it
            }
        return null
    }

    fun checkHasCourierPromo(services: List<ShippingDurationUiModel>): Boolean {
        return services.any { it.serviceData.isPromo == 1 }
    }

    override fun onChooseDuration(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        cartPosition: Int,
        serviceData: ServiceData,
        isOcc: Boolean
    ) {
        var flagNeedToSetPinpoint = false
        var selectedServiceId = 0
        val selectedShippingCourierUiModel = if (isOcc) {
            findAutoSelectedCourier(
                serviceData,
                shippingCourierUiModelList
            )
        } else {
            findRecommendedCourier(serviceData, shippingCourierUiModelList)
        }
        if (view?.isToogleYearEndPromotionOn() == true && !isOcc) {
            if (serviceData.error != null && serviceData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED &&
                serviceData.error.errorMessage.isNotEmpty()
            ) {
                flagNeedToSetPinpoint = true
                selectedServiceId = serviceData.serviceId
            }
        } else {
            shippingCourierUiModelList.forEach { shippingCourierUiModel ->
                shippingCourierUiModel.isSelected =
                    shippingCourierUiModel == selectedShippingCourierUiModel
                if (shippingCourierUiModel.productData.error != null &&
                    shippingCourierUiModel.productData.error.errorMessage != null &&
                    shippingCourierUiModel.productData.error.errorId != null &&
                    shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED
                ) {
                    selectedServiceId = shippingCourierUiModel.serviceData.serviceId
                    flagNeedToSetPinpoint = true
                    if (!isOcc) {
                        shippingCourierUiModel.serviceData.texts.textRangePrice =
                            shippingCourierUiModel.productData.error.errorMessage
                    }
                }
            }
        }
        view?.onShippingDurationAndRecommendCourierChosen(
            shippingCourierUiModelList,
            selectedShippingCourierUiModel,
            cartPosition,
            selectedServiceId,
            serviceData,
            flagNeedToSetPinpoint
        )
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        // Project Army
        val shippingDurationUiModel = getRatesDataFromLogisticPromo(data.serviceId)
        if (shippingDurationUiModel == null) {
            view?.showPromoCourierNotAvailable()
            return
        }
        val courierData = getCourierItemDataById(
            data.shipperProductId,
            shippingDurationUiModel.shippingCourierViewModelList
        )
        if (courierData == null) {
            view?.showPromoCourierNotAvailable()
            return
        }
        view?.onLogisticPromoChosen(
            shippingDurationUiModel.shippingCourierViewModelList,
            courierData,
            shippingDurationUiModel.serviceData,
            false,
            data.promoCode,
            data.serviceId,
            data
        )
    }
}

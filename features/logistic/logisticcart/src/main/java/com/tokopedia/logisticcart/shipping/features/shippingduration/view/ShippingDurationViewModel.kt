package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.ChooseShippingDurationState
import com.tokopedia.logisticcart.shipping.model.DividerModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.PaidSectionInfoUiModel
import com.tokopedia.logisticcart.shipping.model.ProductShipmentDetailModel
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationAnalyticState
import com.tokopedia.logisticcart.shipping.model.ShippingDurationListState
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShippingDurationViewModel @Inject constructor(
    private val ratesUseCase: GetRatesCoroutineUseCase,
    private val ratesApiUseCase: GetRatesApiCoroutineUseCase,
    private val stateConverter: RatesResponseStateConverter
) : BaseViewModel(Dispatchers.Main) {
    companion object {
        private const val DEFAULT_ERROR_NO_SHIPMENT_AVAILABLE =
            "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda."
    }

    var shippingData: ShippingRecommendationData? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _chosenDuration = SingleLiveEvent<ChooseShippingDurationState>()
    val chosenDuration: LiveData<ChooseShippingDurationState>
        get() = _chosenDuration

    private val _shipmentData = MutableLiveData<ShippingDurationListState>()
    val shipmentData: LiveData<ShippingDurationListState>
        get() = _shipmentData

    private val _shipmentAnalytic = SingleLiveEvent<ShippingDurationAnalyticState>()
    val shipmentAnalytic: LiveData<ShippingDurationAnalyticState>
        get() = _shipmentAnalytic

    fun loadDuration(
        selectedSpId: Int,
        selectedServiceId: Int,
        ratesParam: RatesParam,
        isRatesTradeInApi: Boolean,
        isOcc: Boolean
    ) {
        launch {
            try {
                _loading.value = true
                val response =
                    if (isRatesTradeInApi) ratesApiUseCase(ratesParam) else ratesUseCase(ratesParam)
                val model = stateConverter.fillState(
                    response,
                    ratesParam.shopShipments,
                    selectedSpId,
                    selectedServiceId
                )
                _loading.value = false
                if (model.errorId != null && model.errorId == ErrorProductData.ERROR_RATES_NOT_AVAILABLE) {
                    _shipmentData.value =
                        ShippingDurationListState.NoShipmentAvailable(model.errorMessage.orEmpty())
                } else if (model.shippingDurationUiModels.isNotEmpty()) {
                    shippingData = model
                    _shipmentData.value = ShippingDurationListState.ShowList(
                        convertServiceListToUiModel(
                            model.shippingDurationUiModels,
                            model.listLogisticPromo,
                            model.productShipmentDetailModel,
                            model.paidSectionInfoUiModel,
                            isOcc
                        )
                    )
                    _shipmentAnalytic.value =
                        ShippingDurationAnalyticState.AnalyticShippingDuration(model.shippingDurationUiModels)
                    val hasCourierPromo = checkHasCourierPromo(model.shippingDurationUiModels)
                    if (hasCourierPromo) {
                        _shipmentAnalytic.value =
                            ShippingDurationAnalyticState.AnalyticCourierPromo(model.shippingDurationUiModels)
                    }
                    _shipmentAnalytic.value =
                        ShippingDurationAnalyticState.AnalyticPromoLogistic(model.listLogisticPromo)
                } else {
                    _shipmentData.value =
                        ShippingDurationListState.NoShipmentAvailable(
                            DEFAULT_ERROR_NO_SHIPMENT_AVAILABLE
                        )
                }
            } catch (e: Exception) {
                _loading.value = false
                _shipmentData.value = ShippingDurationListState.RatesError(e)
            }
        }
    }

    fun onChooseDuration(
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
        if (!isOcc) {
            if (serviceData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED && serviceData.error.errorMessage.isNotEmpty()) {
                flagNeedToSetPinpoint = true
                selectedServiceId = serviceData.serviceId
            }
        } else {
            shippingCourierUiModelList.forEach { shippingCourierUiModel ->
                shippingCourierUiModel.isSelected =
                    shippingCourierUiModel == selectedShippingCourierUiModel
                if (shippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED
                ) {
                    selectedServiceId = shippingCourierUiModel.serviceData.serviceId
                    flagNeedToSetPinpoint = true
                }
            }
        }
        _chosenDuration.value = ChooseShippingDurationState.NormalShipping(
            shippingCourierUiModelList,
            selectedShippingCourierUiModel,
            cartPosition,
            selectedServiceId,
            serviceData,
            flagNeedToSetPinpoint
        )
    }

    fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        // Project Army
        val shippingDurationUiModel = getRatesDataFromLogisticPromo(data.serviceId)
        if (shippingDurationUiModel == null) {
            _chosenDuration.value = ChooseShippingDurationState.CourierNotAvailable
            return
        }
        val courierData = getCourierItemDataById(
            data.shipperProductId,
            shippingDurationUiModel.shippingCourierViewModelList
        )
        if (courierData == null) {
            _chosenDuration.value = ChooseShippingDurationState.CourierNotAvailable
            return
        }
        _chosenDuration.value = ChooseShippingDurationState.FreeShipping(
            shippingDurationUiModel.shippingCourierViewModelList,
            courierData,
            shippingDurationUiModel.serviceData,
            false,
            data.promoCode,
            data.serviceId,
            data
        )
    }

    private fun checkHasCourierPromo(services: List<ShippingDurationUiModel>): Boolean {
        return services.any { it.serviceData.isPromo == 1 }
    }

    private fun getCourierItemDataById(
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

    private fun getRatesDataFromLogisticPromo(serId: Int): ShippingDurationUiModel? {
        shippingData?.shippingDurationUiModels?.firstOrNull { it.serviceData.serviceId == serId }
            ?.let {
                return it
            }
        return null
    }

    private fun findRecommendedCourier(
        serviceData: ServiceData,
        shippingCourierUiModelList: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel? {
        return shippingCourierUiModelList.takeIf { serviceData.selectedShipperProductId > 0 }
            ?.find { shippingCourierUiModel -> shippingCourierUiModel.productData.shipperProductId == serviceData.selectedShipperProductId }
            ?: shippingCourierUiModelList.firstOrNull { it.productData.isRecommend && !it.productData.isUiRatesHidden && it.productData.error.errorMessage.isEmpty() }
    }

    private fun findAutoSelectedCourier(
        serviceData: ServiceData,
        shippingCourierUiModelList: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel {
        return findRecommendedCourier(serviceData, shippingCourierUiModelList)
            ?: shippingCourierUiModelList.firstOrNull { !it.productData.isUiRatesHidden && it.productData.error.errorMessage.isEmpty() }
            ?: shippingCourierUiModelList.first()
    }

    private fun convertServiceListToUiModel(
        shippingDurationUiModels: List<ShippingDurationUiModel>,
        promoUiModel: List<LogisticPromoUiModel>,
        productShipmentDetailModel: ProductShipmentDetailModel?,
        paidSectionInfoUiModel: PaidSectionInfoUiModel,
        isOcc: Boolean
    ): MutableList<RatesViewModelType> {
        val eligibleServices = shippingDurationUiModels.filter { !it.serviceData.isUiRatesHidden }
        val uiModelList: MutableList<RatesViewModelType> = mutableListOf()

        // paid shipping
        if (paidSectionInfoUiModel.title.isNotEmpty()) {
            if (paidSectionInfoUiModel.isCollapsed) {
                uiModelList.addAll(eligibleServices)
            }
            uiModelList.add(0, paidSectionInfoUiModel)
        } else {
            uiModelList.addAll(eligibleServices)
        }

        if (promoUiModel.isNotEmpty()) {
            uiModelList.addAll(0, promoUiModel + listOf<RatesViewModelType>(DividerModel()))
        }

        // notifier
        if (!isOcc && !eligibleServices.any { it.etaErrorCode == 0 }) {
            uiModelList.add(0, NotifierModel())
        }

        // shipment info
        productShipmentDetailModel?.let {
            uiModelList.add(0, it)
        }

        return uiModelList
    }

    fun onCollapseClicked(collapsed: Boolean, isOcc: Boolean) {
        shippingData?.let { model ->
            model.paidSectionInfoUiModel.isCollapsed = collapsed
            _shipmentData.value = ShippingDurationListState.ShowList(
                convertServiceListToUiModel(
                    model.shippingDurationUiModels,
                    model.listLogisticPromo,
                    model.productShipmentDetailModel,
                    model.paidSectionInfoUiModel,
                    isOcc
                )
            )
        }
    }
}

package com.tokopedia.tokofood.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.response.ChosenAddressDataResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel

fun createLoadingState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val loadingStateUiModel = TokoFoodHomeLoadingStateUiModel(id = LOADING_STATE)
    mutableList.add(loadingStateUiModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.LOADING
    )
}

fun createNoPinPoinState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressModel = TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
    val noPinPoinUiModel = TokoFoodHomeEmptyStateLocationUiModel(id = EMPTY_STATE_NO_PIN_POINT)
    mutableList.add(chooseAddressModel)
    mutableList.add(noPinPoinUiModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.HIDE
    )
}

fun createNoAddressState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressModel = TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
    val noAddressModel = TokoFoodHomeEmptyStateLocationUiModel(id = EMPTY_STATE_NO_ADDRESS)
    mutableList.add(chooseAddressModel)
    mutableList.add(noAddressModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.HIDE
    )
}

fun createChooseAddress(): GetStateChosenAddressQglResponse {
    return GetStateChosenAddressQglResponse(
        response = GetStateChosenAddressResponse(
            data = ChosenAddressDataResponse(
                districtId = 1,
                cityId = 1,
                addressId = 1
            )
        )
    )
}

fun createKeroAddrIsEligibleForAddressFeature(): KeroAddrIsEligibleForAddressFeatureResponse {
    return KeroAddrIsEligibleForAddressFeatureResponse(
        data = KeroAddrIsEligibleForAddressFeatureData (
            eligibleForRevampAna = EligibleForAddressFeature (
                eligible = true
                    )
                )
    )
}
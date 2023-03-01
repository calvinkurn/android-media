package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.State
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressDataView
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.utils.mvvm.SearchUiState
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel as ChooseAddressModel

data class MPSState(
    val parameter: Map<String, String> = mapOf(),
    val result: State<List<Visitable<*>>> = State.Loading(),
    val chooseAddressModel: ChooseAddressModel? = null,
): SearchUiState {

    fun success(mpsModel: MPSModel) = copy(
        result = State.Success(
            data = successData(mpsModel),
        )
    )

    private fun successData(mpsModel: MPSModel) =
        listOf(ChooseAddressDataView) +
            mpsShopWidgetList(mpsModel)

    private fun mpsShopWidgetList(mpsModel: MPSModel) =
        mpsModel.shopList.map(MPSShopWidgetDataView::create)

    fun error(throwable: Throwable) = copy(
        result = State.Error(
            message = "",
            data = null,
            throwable = throwable,
        )
    )

    fun chooseAddress(chooseAddressModel: ChooseAddressModel) = copy(
        chooseAddressModel = chooseAddressModel
    )
}

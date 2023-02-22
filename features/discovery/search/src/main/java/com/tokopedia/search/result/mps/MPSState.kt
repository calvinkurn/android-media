package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.State
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.utils.mvvm.SearchUiState
import javax.inject.Inject

data class MPSState(
    val parameter: Map<String, String> = mapOf(),
    val result: State<List<Visitable<*>>> = State.Loading(),
): SearchUiState {

    fun success(mpsModel: MPSModel) = copy(
        result = State.Success(
            data = mpsModel.shopList.map(MPSShopWidgetDataView::create),
        )
    )

    fun error(throwable: Throwable) = copy(
        result = State.Error(
            message = "",
            data = null,
            throwable = throwable,
        )
    )
}

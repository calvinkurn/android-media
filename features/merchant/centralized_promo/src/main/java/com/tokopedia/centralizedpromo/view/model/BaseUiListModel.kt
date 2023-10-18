package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface BaseUiListModel<V: Visitable<*>>: BaseUiModel {
    val items: List<V>
    val errorMessage: String
}

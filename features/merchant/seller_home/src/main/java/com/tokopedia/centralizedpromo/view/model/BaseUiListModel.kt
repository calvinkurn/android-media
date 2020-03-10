package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface BaseUiListModel: BaseUiModel {
    val items: List<Visitable<*>>
    val errorMessage: String
}
package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

interface BaseUiListItemModel<T: BaseAdapterTypeFactory> : Visitable<T> {
    val impressHolder: ImpressHolder
}

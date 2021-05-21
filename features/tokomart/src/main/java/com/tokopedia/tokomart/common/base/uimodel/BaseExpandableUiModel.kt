package com.tokopedia.tokomart.common.base.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class BaseExpandableUiModel<T>: Visitable<T> {
    var isExpanded = false
}
package com.tokopedia.mvc.presentation.bottomsheet.adapter.factory

import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel

interface MenuAdapterFactory {

    fun type(model: MoreMenuUiModel): Int
}

package com.tokopedia.vouchercreation.voucherlist.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.FilterAdapterFactory

/**
 * Created By @ilhamsuaib on 22/04/20
 */

sealed class BaseFilterUiModel : Visitable<FilterAdapterFactory> {

    override fun type(typeFactory: FilterAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class FilterItem(
            val label: String,
            val key: String,
            var isSelected: Boolean = false
    ) : BaseFilterUiModel()

    data class FilterGroup(val title: String) : BaseFilterUiModel()

    object FilterDivider : BaseFilterUiModel()
}
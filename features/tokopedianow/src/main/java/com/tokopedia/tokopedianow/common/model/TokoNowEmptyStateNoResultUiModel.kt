package com.tokopedia.tokopedianow.common.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory

class TokoNowEmptyStateNoResultUiModel(
    val id: String = "",
    val activeFilterList: List<Option>? = null,
    val defaultTitle: String = "",
    val defaultDescription: String = "",
    @StringRes val defaultTitleResId: Int? = null,
    @StringRes val defaultDescriptionResId: Int? = null,
    @StringRes val globalSearchBtnTextResId: Int? = null
): Visitable<TokoNowEmptyStateNoResultTypeFactory> {

    override fun type(typeFactory: TokoNowEmptyStateNoResultTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
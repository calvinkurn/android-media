package com.tokopedia.tokopedianow.common.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateNoResultTypeFactory

class TokoNowEmptyStateNoResultUiModel(
    val id: String = "",
    val activeFilterList: List<Option>? = null,
    val defaultTitle: String = "",
    val defaultDescription: String = "",
    val defaultImage: String = "",
    val defaultTextPrimaryButton: String = "",
    val defaultUrlPrimaryButton: String = "",
    val excludeFilter: Option? = null,
    @StringRes val defaultTitleResId: Int? = null,
    @StringRes val defaultDescriptionResId: Int? = null,
    @StringRes val globalSearchBtnTextResId: Int? = null,
    val enablePrimaryButton: Boolean = true,
    val enableSecondaryButton: Boolean = true
): Visitable<TokoNowEmptyStateNoResultTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: TokoNowEmptyStateNoResultTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}

package com.tokopedia.product.manage.feature.list.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductFilterAdapterFactory

sealed class FilterViewModel(
    @StringRes val titleId: Int,
    open val count: Int = 0,
    @DrawableRes val icon: Int? = null
) : Visitable<ProductFilterAdapterFactory> {

    data class Active(override val count: Int): FilterViewModel(R.string.product_manage_filter_active, count)
    data class InActive(override val count: Int): FilterViewModel(R.string.product_manage_filter_inactive, count)
    data class Banned(override val count: Int): FilterViewModel(R.string.product_manage_filter_banned, count)

    object Default: FilterViewModel(R.string.product_manage_filter, icon = R.drawable.unify_filter_ic)

    override fun type(typeFactory: ProductFilterAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
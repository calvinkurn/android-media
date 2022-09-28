package com.tokopedia.product.manage.feature.list.view.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductFilterAdapterFactory
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

sealed class FilterTabUiModel(
    @StringRes val titleId: Int,
    open val count: Int = 0,
    val status: ProductStatus? = null,
    open var isSelected: Boolean
) : Visitable<ProductFilterAdapterFactory> {

    data class Active(
        override val count: Int,
        override var isSelected: Boolean = false
    ) : FilterTabUiModel(
        R.string.product_manage_filter_active,
        count,
        ProductStatus.ACTIVE,
        isSelected
    )

    data class InActive(
        override val count: Int,
        override var isSelected: Boolean = false
    ) : FilterTabUiModel(
        R.string.product_manage_filter_inactive,
        count,
        ProductStatus.INACTIVE,
        isSelected
    )

    data class Violation(
        override val count: Int,
        override var isSelected: Boolean = false
    ) : FilterTabUiModel(
        R.string.product_manage_filter_banned,
        count,
        ProductStatus.VIOLATION,
        isSelected
    )

    override fun type(typeFactory: ProductFilterAdapterFactory): Int {
        return typeFactory.type(this)
    }

    enum class FilterId {
        ACTIVE,
        INACTIVE,
        VIOLATION
    }
}
package com.tokopedia.product.manage.feature.list.view.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductMenuAdapterFactory

sealed class ProductMenuViewModel(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    open val product: ProductViewModel
): Visitable<ProductMenuAdapterFactory> {

    data class Preview(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_preview_menu,
        com.tokopedia.unifycomponents.R.drawable.unify_password_show,
        product
    )

    data class Duplicate(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_duplicate_product_menu,
        R.drawable.ic_product_manage_duplicate,
        product
    )

    data class StockReminder(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_stock_reminder_menu,
        com.tokopedia.resources.common.R.drawable.ic_system_action_notification_normal_24,
        product
    )

    data class Delete(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_menu_delete_product,
        R.drawable.ic_product_manage_delete,
        product
    )

    data class SetTopAds(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_set_promo_ads_menu,
        R.drawable.ic_manage_product_topads,
        product
    )

    data class SeeTopAds(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_see_promo_ads_menu,
        R.drawable.ic_manage_product_topads,
        product
    )

    data class SetCashBack(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_menu_set_cashback,
        R.drawable.ic_product_manage_cashback,
        product
    )

    data class SetFeaturedProduct(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_set_featured_menu,
        R.drawable.ic_product_manage_featured,
        product
    )

    data class RemoveFeaturedProduct(override val product: ProductViewModel): ProductMenuViewModel(
        R.string.product_manage_remove_featured_menu,
        R.drawable.ic_product_manage_featured,
        product
    )

    override fun type(typeFactory: ProductMenuAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
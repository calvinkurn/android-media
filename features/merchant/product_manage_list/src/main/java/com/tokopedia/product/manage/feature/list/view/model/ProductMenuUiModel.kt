package com.tokopedia.product.manage.feature.list.view.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductMenuAdapterFactory

sealed class ProductMenuUiModel(
    @StringRes val title: Int,
    val icon: Int,
    open val product: ProductUiModel
): Visitable<ProductMenuAdapterFactory> {

    data class Preview(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_preview_menu,
        IconUnify.VISIBILITY,
        product
    )

    data class Duplicate(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_duplicate_product_menu,
        IconUnify.COPY,
        product
    )

    data class StockReminder(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_stock_reminder_menu,
        IconUnify.BELL,
        product
    )

    data class Delete(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_menu_delete_product,
        IconUnify.DELETE,
        product
    )

    data class SetTopAds(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_set_promo_ads_menu,
        IconUnify.SPEAKER,
        product
    )

    data class SeeTopAds(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_see_promo_ads_menu,
        IconUnify.SPEAKER,
        product
    )

    data class CreateBroadcastChat(override val product: ProductUiModel): ProductMenuUiModel(
            R.string.product_manage_create_broadcast_chat,
            R.drawable.ic_bc_chat,
            product
    )

    data class SetCashBack(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_menu_set_cashback,
        IconUnify.DISCOUNT,
        product
    )

    data class SetFeaturedProduct(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_set_featured_menu,
        IconUnify.STAR_CIRCLE,
        product
    )

    data class RemoveFeaturedProduct(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_remove_featured_menu,
        IconUnify.STAR_CIRCLE,
        product
    )

    override fun type(typeFactory: ProductMenuAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
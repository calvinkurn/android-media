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
    val newTagEndMillis: Long?,
    open val product: ProductUiModel
): Visitable<ProductMenuAdapterFactory> {

    companion object {
        // Wed Mar 30 2022 00:00:00
        private const val PRODUCT_COUPON_END_DATE = 1648573200000
    }

    data class Preview(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_preview_menu,
        IconUnify.VISIBILITY,
        null,
        product
    )

    data class Duplicate(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_duplicate_product_menu,
        IconUnify.COPY,
        null,
        product
    )

    data class StockReminder(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_stock_reminder_menu,
        IconUnify.BELL,
        null,
        product
    )

    data class Delete(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_menu_delete_product,
        IconUnify.DELETE,
        null,
        product
    )

    data class SetTopAds(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_set_promo_ads_menu,
        IconUnify.SPEAKER,
        null,
        product
    )

    data class SeeTopAds(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_see_promo_ads_menu,
        IconUnify.SPEAKER,
        null,
        product
    )

    data class CreateBroadcastChat(override val product: ProductUiModel): ProductMenuUiModel(
            R.string.product_manage_create_broadcast_chat,
            R.drawable.ic_bc_chat,
        null,
            product
    )

    data class SetCashBack(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_menu_set_cashback,
        IconUnify.DISCOUNT,
        null,
        product
    )

    data class SetFeaturedProduct(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_set_featured_menu,
        IconUnify.STAR_CIRCLE,
        null,
        product
    )

    data class RemoveFeaturedProduct(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_remove_featured_menu,
        IconUnify.STAR_CIRCLE,
        null,
        product
    )

    data class CreateProductCoupon(override val product: ProductUiModel): ProductMenuUiModel(
        R.string.product_manage_create_product_coupon,
        IconUnify.DISCOUNT,
        PRODUCT_COUPON_END_DATE,
        product
    )

    override fun type(typeFactory: ProductMenuAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
package com.tokopedia.seller_migration_common.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureAdapterTypeFactory

sealed class SellerFeatureUiModel(
        @DrawableRes
        val imageId: Int,
        @StringRes
        val titleId: Int,
        @StringRes
        val descriptionId: Int,
        var data: Any? = null
) : Visitable<SellerFeatureAdapterTypeFactory> {

    // product features
    class FeaturedProductFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_featured_product,
            titleId = R.string.seller_migration_fragment_product_tab_featured_product_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_featured_product_description)

    class ImportInstagramFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_import_instagram,
            titleId = R.string.seller_migration_fragment_product_tab_instagram_import_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_instagram_import_description)

    class MultiEditFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_multi_edit,
            titleId = R.string.seller_migration_fragment_product_tab_multi_edit_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_multi_edit_description)

    class SetCashbackFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_voucher,
            titleId = R.string.seller_migration_fragment_product_tab_set_cashback_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_set_cashback_description)

    class SetVariantFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_set_variant,
            titleId = R.string.seller_migration_fragment_product_tab_set_variant_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_set_variant_description)

    class AttachVoucherFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_attach_voucher,
            titleId = R.string.seller_migration_fragment_chat_tab_attach_voucher_title,
            descriptionId = R.string.seller_migration_fragment_chat_tab_attach_voucher_description)

    class MarketInsightFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_insight,
            titleId = R.string.seller_migration_fragment_statistic_tab_market_insight_title,
            descriptionId = R.string.seller_migration_fragment_statistic_tab_market_insight_description)

    class ReviewStatisticsFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_review_statistic,
            titleId = R.string.seller_migration_fragment_review_tab_review_statistic_title,
            descriptionId = R.string.seller_migration_fragment_review_tab_review_statistic_description)

    class ShopInsightFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_insight,
            titleId = R.string.seller_migration_fragment_statistic_tab_shop_insight_title,
            descriptionId = R.string.seller_migration_fragment_statistic_tab_shop_insight_description)

    class TemplateChatFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_template_chat,
            titleId = R.string.seller_migration_fragment_chat_tab_template_chat_title,
            descriptionId = R.string.seller_migration_fragment_chat_tab_template_chat_description)

    class TemplateReviewFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_template_review,
            titleId = R.string.seller_migration_fragment_review_tab_template_review_title,
            descriptionId = R.string.seller_migration_fragment_review_tab_template_review_description)

    class TopAdsFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_topads,
            titleId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_title,
            descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_description)

    class VoucherCashbackFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_voucher,
            titleId = R.string.seller_migration_fragment_ads_and_promo_tab_shop_voucher_title,
            descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_shop_voucher_description)

    class MultiEditFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_multi_edit,
            titleId = R.string.seller_migration_fragment_product_tab_multi_edit_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_multi_edit_description,
            data = data)

    class TopAdsFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_topads,
            titleId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_title,
            descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_description,
            data = data)

    class SetCashbackFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_voucher,
            titleId = R.string.seller_migration_fragment_product_tab_set_cashback_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_set_cashback_description,
            data = data)

    class FeaturedProductFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_featured_product,
            titleId = R.string.seller_migration_fragment_product_tab_featured_product_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_featured_product_description,
            data = data)

    class StockReminderFeatureUiModel : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_stock_reminder,
            titleId = R.string.seller_migration_fragment_product_tab_stock_reminder_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_stock_reminder_description)

    class StockReminderFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_stock_reminder,
            titleId = R.string.seller_migration_fragment_product_tab_stock_reminder_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_stock_reminder_description,
            data = data)

    class AddEditSetVariantFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_set_variant,
            titleId = R.string.seller_migration_add_edit_product_set_variant_title,
            descriptionId = R.string.seller_migration_add_edit_product_set_variant_description,
            data = data)

    class ProductManageSetVariantFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_set_variant,
            titleId = R.string.seller_migration_product_manage_set_variant_title,
            descriptionId = R.string.seller_migration_product_manage_set_variant_description,
            data = data)

    class ImportInstagramFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_import_instagram,
            titleId = R.string.seller_migration_fragment_product_tab_instagram_import_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_instagram_import_description,
            data = data)

    class PostFeedDataUiModel: SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_feed,
            titleId = R.string.seller_migration_fragment_feed_play_tab_post_feed_title,
            descriptionId = R.string.seller_migration_fragment_feed_play_tab_post_feed_description
    )

    class LiveVideoDataUiModel: SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_tokopedia_play,
            titleId = R.string.seller_migration_fragment_feed_play_tab_live_video_title,
            descriptionId = R.string.seller_migration_fragment_feed_play_tab_live_video_description
    )

    class ShopCapitalDataUiModel: SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_modal_toko,
            titleId = R.string.seller_migration_fragment_financial_service_tab_shop_capital_title,
            descriptionId = R.string.seller_migration_fragment_financial_service_tab_shop_capital_description
    )

    class PriorityBalanceDataUiModel: SellerFeatureUiModel(
            imageId = R.drawable.ic_seller_migration_saldo_prio,
            titleId = R.string.seller_migration_fragment_financial_service_tab_priority_balance_title,
            descriptionId = R.string.seller_migration_fragment_financial_service_tab_priority_balance_description
    )

    override fun type(typeFactory: SellerFeatureAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
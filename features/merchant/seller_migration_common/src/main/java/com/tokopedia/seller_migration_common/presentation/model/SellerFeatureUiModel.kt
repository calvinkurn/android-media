package com.tokopedia.seller_migration_common.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_ATTACH_VOUCHER_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_FEATURED_PRODUCT_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_CASHBACK_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_FEED_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_FINANCE_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_MULTI_EDIT_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_PLAY_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_SELLER_INSIGHT_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_SHOP_FUND_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_SMART_REPLY_DISCUSSION_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_STATISTICS_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_STOCK_REMINDER_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_TEMPLATE_CHAT_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_TEMPLATE_REVIEW_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_TOPADS_ICON
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.URL_VARIANT_ICON
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureAdapterTypeFactory

sealed class SellerFeatureUiModel(
        val imageUrl: String,
        @StringRes
        val titleId: Int,
        @StringRes
        val descriptionId: Int,
        var data: Any? = null
) : Visitable<SellerFeatureAdapterTypeFactory> {

    // product features
    class FeaturedProductFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_FEATURED_PRODUCT_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_featured_product_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_featured_product_description)

    class MultiEditFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_MULTI_EDIT_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_multi_edit_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_multi_edit_description)

    class SetCashbackFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_CASHBACK_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_set_cashback_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_set_cashback_description)

    class SetVariantFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_VARIANT_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_set_variant_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_set_variant_description)

    class AttachVoucherFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_ATTACH_VOUCHER_ICON,
            titleId = R.string.seller_migration_fragment_chat_tab_attach_voucher_title,
            descriptionId = R.string.seller_migration_fragment_chat_tab_attach_voucher_description)

    class AutoReplyFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_TEMPLATE_CHAT_ICON,
            titleId = R.string.seller_migration_fragment_chat_tab_auto_reply_title,
            descriptionId = R.string.seller_migration_fragment_chat_tab_auto_reply_description)

    class MarketInsightFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_SELLER_INSIGHT_ICON,
            titleId = R.string.seller_migration_fragment_statistic_tab_market_insight_title,
            descriptionId = R.string.seller_migration_fragment_statistic_tab_market_insight_description)

    class ReviewStatisticsFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_STATISTICS_ICON,
            titleId = R.string.seller_migration_fragment_review_tab_review_statistic_title,
            descriptionId = R.string.seller_migration_fragment_review_tab_review_statistic_description)

    class ShopInsightFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_SELLER_INSIGHT_ICON,
            titleId = R.string.seller_migration_fragment_statistic_tab_shop_insight_title,
            descriptionId = R.string.seller_migration_fragment_statistic_tab_shop_insight_description)

    class TemplateChatFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_TEMPLATE_CHAT_ICON,
            titleId = R.string.seller_migration_fragment_chat_tab_template_chat_title,
            descriptionId = R.string.seller_migration_fragment_chat_tab_template_chat_description)

    class TemplateReviewFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_TEMPLATE_REVIEW_ICON,
            titleId = R.string.seller_migration_fragment_review_tab_template_review_title,
            descriptionId = R.string.seller_migration_fragment_review_tab_template_review_description)

    class TopAdsFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_TOPADS_ICON,
            titleId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_title,
            descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_description)

    class VoucherCashbackFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_CASHBACK_ICON,
            titleId = R.string.seller_migration_fragment_ads_and_promo_tab_shop_voucher_title,
            descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_shop_voucher_description)

    class MultiEditFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_MULTI_EDIT_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_multi_edit_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_multi_edit_description,
            data = data)

    class TopAdsFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_TOPADS_ICON,
            titleId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_title,
            descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_description,
            data = data)

    class SetCashbackFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_CASHBACK_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_set_cashback_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_set_cashback_description,
            data = data)

    class FeaturedProductFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_FEATURED_PRODUCT_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_featured_product_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_featured_product_description,
            data = data)

    class StockReminderFeatureUiModel : SellerFeatureUiModel(
            imageUrl = URL_STOCK_REMINDER_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_stock_reminder_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_stock_reminder_description)

    class StockReminderFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_STOCK_REMINDER_ICON,
            titleId = R.string.seller_migration_fragment_product_tab_stock_reminder_title,
            descriptionId = R.string.seller_migration_fragment_product_tab_stock_reminder_description,
            data = data)

    class AddEditSetVariantFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_VARIANT_ICON,
            titleId = R.string.seller_migration_add_edit_product_set_variant_title,
            descriptionId = R.string.seller_migration_add_edit_product_set_variant_description,
            data = data)

    class ProductManageSetVariantFeatureWithDataUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_VARIANT_ICON,
            titleId = R.string.seller_migration_product_manage_set_variant_title,
            descriptionId = R.string.seller_migration_product_manage_set_variant_description,
            data = data)

    class PostFeedDataUiModel : SellerFeatureUiModel(
            imageUrl = URL_FEED_ICON,
            titleId = R.string.seller_migration_fragment_feed_play_tab_post_feed_title,
            descriptionId = R.string.seller_migration_fragment_feed_play_tab_post_feed_description
    )

    class LiveVideoDataUiModel : SellerFeatureUiModel(
            imageUrl = URL_PLAY_ICON,
            titleId = R.string.seller_migration_fragment_feed_play_tab_live_video_title,
            descriptionId = R.string.seller_migration_fragment_feed_play_tab_live_video_description
    )

    class ShopCapitalDataUiModel : SellerFeatureUiModel(
            imageUrl = URL_SHOP_FUND_ICON,
            titleId = R.string.seller_migration_fragment_financial_service_tab_shop_capital_title,
            descriptionId = R.string.seller_migration_fragment_financial_service_tab_shop_capital_description
    )

    class PriorityBalanceDataUiModel : SellerFeatureUiModel(
            imageUrl = URL_FINANCE_ICON,
            titleId = R.string.seller_migration_fragment_financial_service_tab_priority_balance_title,
            descriptionId = R.string.seller_migration_fragment_financial_service_tab_priority_balance_description
    )

    class BroadcastChatUiModel : SellerFeatureUiModel(
            imageUrl = URL_TEMPLATE_CHAT_ICON,
            titleId = R.string.seller_migration_tab_ads_broadcast_chat_title,
            descriptionId = R.string.seller_migration_tab_ads_broadcast_chat_description
    )

    class BroadcastChatProductManageUiModel(data: Any) : SellerFeatureUiModel(
            imageUrl = URL_TEMPLATE_CHAT_ICON,
            titleId = R.string.seller_migration_tab_ads_broadcast_chat_title,
            descriptionId = R.string.seller_migration_entry_point_broadcast_chat_description,
            data = data
    )

    class DiscussionTemplateUiModel: SellerFeatureUiModel(
            imageUrl = URL_TEMPLATE_CHAT_ICON,
            titleId = R.string.seller_migration_template_title,
            descriptionId = R.string.seller_migration_template_subtitle
    )

    class DiscussionSmartReplyUiModel: SellerFeatureUiModel(
            imageUrl = URL_SMART_REPLY_DISCUSSION_ICON,
            titleId = R.string.seller_migration_smart_reply_title,
            descriptionId = R.string.seller_migration_smart_reply_subtitle
    )

    override fun type(typeFactory: SellerFeatureAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
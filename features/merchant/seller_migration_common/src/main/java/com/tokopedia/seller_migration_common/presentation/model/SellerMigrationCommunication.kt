package com.tokopedia.seller_migration_common.presentation.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import kotlinx.android.parcel.Parcelize

interface SellerMigrationCommunication: Parcelable {
    val trackingEventClick: String
}

sealed class CommunicationInfo(@StringRes val titleRes: Int,
                               val imageUrl: String,
                               @StringRes val descRes: Int,
                               val benefitPointResList: ArrayList<Int> = arrayListOf(),
                               @StringRes val tickerMessagePrefixRes: Int,
                               override val trackingEventClick: String): SellerMigrationCommunication {

    @Parcelize
    object TopAds: CommunicationInfo(
            R.string.seller_migration_topads_bottom_sheet_title,
            CommunicationImageUrl.TOPADS,
            R.string.seller_migration_topads_bottom_sheet_desc,
            arrayListOf(
                    R.string.seller_migration_topads_bottom_sheet_item_product,
                    R.string.seller_migration_topads_bottom_sheet_item_strenghten,
                    R.string.seller_migration_topads_bottom_sheet_item_advertise
            ),
            R.string.seller_migration_topads_ticker_desc_prefix,
            SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_TOPADS
    )

    @Parcelize
    object BroadcastChat: CommunicationInfo(
            R.string.seller_migration_broadcast_chat_bottom_sheet_title,
            CommunicationImageUrl.BROADCAST_CHAT,
            R.string.seller_migration_broadcast_chat_bottom_sheet_desc,
            arrayListOf(
                    R.string.seller_migration_broadcast_chat_bottom_sheet_item_send,
                    R.string.seller_migration_broadcast_chat_bottom_sheet_item_match,
                    R.string.seller_migration_broadcast_chat_bottom_sheet_item_access
            ),
            R.string.seller_migration_broadcast_chat_ticker_desc_prefix,
            SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_CHAT
    )

    @Parcelize
    object PostFeed: CommunicationInfo(
            R.string.seller_migration_feed_bottom_sheet_title,
            CommunicationImageUrl.POST_FEED,
            R.string.seller_migration_feed_bottom_sheet_desc,
            tickerMessagePrefixRes = R.string.seller_migration_feed_ticker_desc_prefix,
            trackingEventClick = SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_POST_FEED
    )

    @Parcelize
    object ShopCapital: CommunicationInfo(
            R.string.seller_migration_modal_toko_bottom_sheet_title,
            CommunicationImageUrl.SHOP_CAPITAL,
            R.string.seller_migration_modal_toko_bottom_sheet_desc,
            arrayListOf(
                    R.string.seller_migration_modal_toko_bottom_sheet_item_active,
                    R.string.seller_migration_modal_toko_bottom_sheet_item_withdraw,
                    R.string.seller_migration_modal_toko_bottom_sheet_item_process
            ),
            R.string.seller_migration_modal_toko_ticker_desc_prefix,
            SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_BALANCE
    )

    @Parcelize
    object PriorityBalance: CommunicationInfo(
            R.string.seller_migration_saldo_prioritas_bottom_sheet_title,
            CommunicationImageUrl.PRIORITY_BALANCE,
            R.string.seller_migration_saldo_prioritas_bottom_sheet_desc,
            arrayListOf(
                    R.string.seller_migration_saldo_prioritas_bottom_sheet_item_process,
                    R.string.seller_migration_saldo_prioritas_bottom_sheet_item_result,
                    R.string.seller_migration_saldo_prioritas_bottom_sheet_item_return
            ),
            R.string.seller_migration_modal_toko_ticker_desc_prefix,
            SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_BALANCE
    )
}


/**
 * This object reserved for dynamic Modal Toko and Saldo Prioritas information (eligible for both), so we will provide both infos in a list
 */
@Parcelize
open class DynamicCommunicationInfo(val communicationInfoList: List<CommunicationInfo>,
                                    override val trackingEventClick: String): SellerMigrationCommunication

@Parcelize
object DynamicCommunication: DynamicCommunicationInfo(listOf(CommunicationInfo.ShopCapital, CommunicationInfo.PriorityBalance), SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_BALANCE)

object CommunicationImageUrl {
    const val BROADCAST_CHAT = "https://ecs7.tokopedia.net/android/merchant/seller_migration/seller_migration_broadcast_chat.png"
    const val POST_FEED = "https://ecs7.tokopedia.net/android/merchant/seller_migration/seller_migration_post_feed.png"
    const val PRIORITY_BALANCE = "https://ecs7.tokopedia.net/android/merchant/seller_migration/seller_migration_priority_balance.png"
    const val SHOP_CAPITAL = "https://ecs7.tokopedia.net/android/merchant/seller_migration/seller_migration_shop_capital.png"
    const val TOPADS = "https://ecs7.tokopedia.net/android/merchant/seller_migration/seller_migration_topads.png"
}
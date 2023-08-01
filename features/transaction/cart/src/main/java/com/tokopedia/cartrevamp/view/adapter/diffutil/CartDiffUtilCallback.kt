package com.tokopedia.cartrevamp.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.cartrevamp.view.uimodel.CartChooseAddressHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartEmptyHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemTickerErrorHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartLoadingHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartSelectedAmountHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cartrevamp.view.uimodel.CartWishlistHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartrevamp.view.uimodel.DisabledReasonHolderData
import com.tokopedia.purchase_platform.common.feature.sellercashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData

class CartDiffUtilCallback(
    private val oldList: List<Any>,
    private val newList: List<Any>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is CartSelectedAmountHolderData && newItem is CartSelectedAmountHolderData -> oldItem == newItem
            oldItem is CartChooseAddressHolderData && newItem is CartChooseAddressHolderData -> oldItem == newItem
            oldItem is CartGroupHolderData && newItem is CartGroupHolderData -> oldItem == newItem
            oldItem is CartShopBottomHolderData && newItem is CartShopBottomHolderData -> oldItem == newItem
            oldItem is CartItemHolderData && newItem is CartItemHolderData -> oldItem == newItem
            oldItem is CartItemTickerErrorHolderData && newItem is CartItemTickerErrorHolderData -> oldItem == newItem
            oldItem is ShipmentSellerCashbackModel && newItem is ShipmentSellerCashbackModel -> oldItem == newItem
            oldItem is CartEmptyHolderData && newItem is CartEmptyHolderData -> oldItem == newItem
            oldItem is CartRecentViewHolderData && newItem is CartRecentViewHolderData -> oldItem == newItem
            oldItem is CartWishlistHolderData && newItem is CartWishlistHolderData -> oldItem == newItem
            oldItem is CartSectionHeaderHolderData && newItem is CartSectionHeaderHolderData -> oldItem == newItem
            oldItem is CartTopAdsHeadlineData && newItem is CartTopAdsHeadlineData -> oldItem == newItem
            oldItem is CartRecommendationItemHolderData && newItem is CartRecommendationItemHolderData -> oldItem == newItem
            oldItem is CartLoadingHolderData && newItem is CartLoadingHolderData -> oldItem == newItem
            oldItem is TickerAnnouncementHolderData && newItem is TickerAnnouncementHolderData -> oldItem == newItem
            oldItem is DisabledItemHeaderHolderData && newItem is DisabledItemHeaderHolderData -> oldItem == newItem
            oldItem is DisabledReasonHolderData && newItem is DisabledReasonHolderData -> oldItem == newItem
            oldItem is DisabledAccordionHolderData && newItem is DisabledAccordionHolderData -> oldItem == newItem
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}

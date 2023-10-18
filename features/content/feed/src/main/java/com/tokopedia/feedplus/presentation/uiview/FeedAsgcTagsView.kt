package com.tokopedia.feedplus.presentation.uiview

import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_NEW_PRODUCTS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_RESTOCK
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SHOP_DISCOUNT
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SHOP_FLASH_SALE
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SPECIAL_RELEASE
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_TOP_ADS
import com.tokopedia.feedplus.presentation.adapter.FeedAsgcTagAdapter
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedAsgcTagsView(private val recyclerView: RecyclerView) {
    val adapter = FeedAsgcTagAdapter(mutableListOf())
    fun bindData(type: String, campaign: FeedCardCampaignModel) {
        val asgcList = mutableListOf<String>()

        val asgcTag = mapTypeToTag(type)
        if (asgcTag.isNotEmpty()) {
            asgcList.add(asgcTag)
        }

        if (campaign.isExclusiveForMember) {
            asgcList.add(getString(R.string.feed_asgc_exclusive_follower))
        }

        if (asgcList.isNotEmpty()) {
            adapter.setItems(asgcList)
            val layoutManager =
                LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            recyclerView.show()
        } else {
            recyclerView.hide()
        }
    }

    private fun mapTypeToTag(type: String) = when (type) {
        TYPE_FEED_ASGC_NEW_PRODUCTS -> getString(com.tokopedia.content.common.R.string.feeds_asgc_new_product_text)
        TYPE_FEED_ASGC_RESTOCK -> getString(com.tokopedia.content.common.R.string.feeds_asgc_restock_text)
        TYPE_FEED_ASGC_SHOP_DISCOUNT -> getString(com.tokopedia.content.common.R.string.feed_asgc_diskon_toko)
        TYPE_FEED_ASGC_SHOP_FLASH_SALE -> getString(com.tokopedia.content.common.R.string.feed_asgc_flash_sale_toko)
        TYPE_FEED_ASGC_SPECIAL_RELEASE -> getString(com.tokopedia.content.common.R.string.feed_asgc_rilisan_special)
        TYPE_FEED_TOP_ADS -> getString(com.tokopedia.content.common.R.string.feeds_ads_text)
        else -> ""
    }

    private fun getString(@StringRes id: Int) = recyclerView.context.getString(id)
}

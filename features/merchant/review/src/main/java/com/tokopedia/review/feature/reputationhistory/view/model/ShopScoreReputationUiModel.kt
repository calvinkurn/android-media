package com.tokopedia.review.feature.reputationhistory.view.model

import com.tokopedia.base.list.seller.common.util.ItemType
import com.tokopedia.review.R

class ShopScoreReputationUiModel : ItemType {

    companion object {
        const val TYPE = 134574635
        val LAYOUT = R.layout.item_card_reputation_join_shop_score
    }

    override fun getType(): Int {
        return TYPE
    }
}
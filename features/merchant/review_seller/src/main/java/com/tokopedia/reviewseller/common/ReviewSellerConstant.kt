package com.tokopedia.reviewseller.common

import com.tokopedia.reviewseller.feature.reviewdetail.view.model.HeaderModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ItemRatingBarModel

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewSellerConstant {
    val headerRatingData = HeaderModel("4.6", "1.234 Ulasan")
    val listRatingBarData = mutableListOf<ItemRatingBarModel>().apply {
        add(ItemRatingBarModel("5", "1.200"))
        add(ItemRatingBarModel("4", "24"))
        add(ItemRatingBarModel("3", "6"))
        add(ItemRatingBarModel("2", "0"))
        add(ItemRatingBarModel("1", "4"))
    }
}
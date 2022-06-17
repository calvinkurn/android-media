package com.tokopedia.homenav.component

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.homenav.R
import com.tokopedia.test.application.espresso_component.CommonActions

/**
 * Created by dhaba
 */
const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_FAVORITE_SHOP = "tracker/home_nav/favorite_shop.json"

fun clickOnEachShop(viewHolder: RecyclerView.ViewHolder) {
    CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.favorite_shop_rv, 0)
}
package com.tokopedia.recentview.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory
import java.util.*

/**
 * Created by Lukas on 13/11/20.
 */
data class RecentViewProductDataModel (
        val id: String,
        val name: String,
        val price: String,
        val shop: String,
        val imgUri: String,
        val shopId: Int = 0,
        val preorder: String,
        val wholesale: String,
        val badges: List<BadgeDataModel> = ArrayList(),
        val free_return: String,
        var isWishlist: Boolean = false,
        val isGold: String = ""
)
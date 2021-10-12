package com.tokopedia.feed_shop.shop.view

import android.view.View
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify

data class ShopPageFabConfig(
    val type: Int = FloatingButtonUnify.BASIC,
    val color: Int = FloatingButtonUnify.COLOR_GREEN,
    val items: ArrayList<FloatingButtonItem> = arrayListOf(),
    val onMainCircleButtonClicked: View.OnClickListener? = null
)

package com.tokopedia.tokopedianow.category.presentation.callback

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryTitleViewHolder

class CategoryTitleCallback(
    private val context: Context?,
    private val warehouseId: String
): CategoryTitleViewHolder.CategoryTitleListener {
    override fun onClickAnotherCategory() {
        RouteManager.route(
            context,
            ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
            warehouseId
        )
    }
}

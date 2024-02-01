package com.tokopedia.deals.ui.search.listener

import android.view.View
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.data.entity.Category
import com.tokopedia.deals.ui.search.model.response.Item
import com.tokopedia.deals.ui.search.model.visitor.MoreBrandModel
import com.tokopedia.deals.ui.search.model.visitor.VoucherModel

interface DealsSearchListener {
    fun onVoucherClicked(itemView: View, voucher: VoucherModel)
    fun onBrandClicked(itemView: View, brand: Brand, position: Int)
    fun onMoreBrandClicked(itemView: View, moreBrandModel: MoreBrandModel)
    fun onCuratedChipClicked(itemView: View, curated: Category)
    fun onLastSeenClicked(itemView: View, lastSeen: Item)
}

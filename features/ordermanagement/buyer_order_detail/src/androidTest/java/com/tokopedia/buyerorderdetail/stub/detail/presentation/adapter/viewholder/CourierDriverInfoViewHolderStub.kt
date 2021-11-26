package com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierDriverInfoViewHolder

class CourierDriverInfoViewHolderStub(
    itemView: View?,
    navigator: BuyerOrderDetailNavigator
) : CourierDriverInfoViewHolder(itemView, navigator) {
    // noop since image loading placeholder causing the main thread busy
    override fun setupDriverPhoto(photoUrl: String) {
//        ivBuyerOrderDetailCourierDriverPhoto?.setImageUrl(photoUrl)
    }
}
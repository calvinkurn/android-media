package com.tokopedia.shop.product.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.model.MembershipStampProgressViewModel

class MembershipRegisterViewHolder(itemView: View): AbstractViewHolder<MembershipStampProgressViewModel>(itemView) {

    companion object{
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_membership_register
    }

    override fun bind(element: MembershipStampProgressViewModel?) {

    }
}
package com.tokopedia.gm.subscribe.membership.view.adapter

import android.view.View
import com.tokopedia.gm.subscribe.view.recyclerview.GmProductViewHolder

class GmMembershipProductViewHolder(itemView : View) : GmProductViewHolder(itemView) {

    override fun setSelected() {
        iconCheck.visibility = View.VISIBLE
    }

    override fun setUnselected() {
        iconCheck.visibility = View.GONE
    }

}

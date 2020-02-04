package com.tokopedia.sellerhomedrawer.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerGroupListener
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerGroup
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem
import kotlinx.android.synthetic.main.sh_drawer_group.view.*

class SellerDrawerGroupViewHolder(itemView: View,
                                  val listener: SellerDrawerGroupListener)
    : AbstractViewHolder<SellerDrawerGroup>(itemView) {

    private var sellerDrawerItems: List<SellerDrawerItem>? = null

    companion object {
        val LAYOUT_RES = R.layout.sh_drawer_group
    }

    override fun bind(sellerDrawerGroup: SellerDrawerGroup?) {
        if (sellerDrawerGroup != null) {
            with(itemView) {
                setArrowPosition(sellerDrawerGroup.isExpanded)
                setNotification(sellerDrawerGroup.notif)
                label.text = sellerDrawerGroup.label
                setGroupIcon(sellerDrawerGroup.iconId)
                drawer_item.setOnClickListener {
                    listener.onGroupClicked(sellerDrawerGroup, adapterPosition)
                }

            }
        }
    }



    private fun setArrowPosition(isExpanded: Boolean) {
        with(itemView) {
            if (isExpanded)
                arrow.setImageResource(R.drawable.sh_arrow_up)
            else arrow.setImageResource(R.drawable.sh_arrow_drop_down)
        }
    }

    private fun setNotification(notifCount: Int) {
        with(itemView) {
            notif.visibility = View.GONE
            if (notifCount > 0)
                toggle_notif.visibility = View.VISIBLE
            else toggle_notif.visibility = View.GONE
        }
    }

    private fun setGroupIcon(iconId: Int) {
        if (iconId != 0) itemView.icon.setImageResource(iconId)
    }
}
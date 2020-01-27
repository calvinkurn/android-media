package com.tokopedia.sellerhomedrawer.presentation.view.viewholder

import android.graphics.Typeface
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerItemListener
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem
import kotlinx.android.synthetic.main.sh_drawer_item.view.*

class SellerDrawerItemViewHolder (itemView: View,
                                  val listener: SellerDrawerItemListener)
    : AbstractViewHolder<SellerDrawerItem>(itemView){

    companion object {
        val LAYOUT_RES = R.layout.sh_drawer_item
        const val MAX_PLACEHOLDER = "999+"
    }

    private var selectedItem : Int = 0
        set(value) {
            field = value
            listener.notifyDataSetChanged()
        }

    override fun bind(sellerDrawerItem: SellerDrawerItem) {
        with(itemView) {
            label.text = sellerDrawerItem.label
            icon.setImageResource(sellerDrawerItem.iconId)
            setNotificationCounter(sellerDrawerItem.notif)
            setSelectedBackground(sellerDrawerItem.id)
            setDrawerHighlightVisibility(sellerDrawerItem.isNew)
            drawer_item.setOnClickListener {
                listener.onItemClicked(sellerDrawerItem)
            }
        }
    }

    private fun setNotificationCounter(notificationCount: Int) {
        val maximumCounter = 999
        val notif = itemView.notif
        notif.visibility = View.VISIBLE
        when {
            notificationCount > 0 ->
                notif.text = notificationCount.toString()
            notificationCount > maximumCounter ->
                notif.text = MAX_PLACEHOLDER
            else -> notif.visibility = View.GONE
        }
    }

    private fun setSelectedBackground(sellerDrawerItemId: Int) {
        if (sellerDrawerItemId == selectedItem)
            itemView.label.setTypeface(null, Typeface.BOLD)
        else itemView.label.setTypeface(null, Typeface.NORMAL)
    }

    private fun setDrawerHighlightVisibility(isItemNew: Boolean) {
        itemView.new_drawer_menu.visibility.apply {
            if (isItemNew) View.VISIBLE
            else View.GONE
        }
    }

}
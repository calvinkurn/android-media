package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberCardBgAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberCardBg
import kotlinx.android.synthetic.main.tm_dash_colorbg_item.view.*

var lastItemSelectedPosBg = -1
var selectedItemPosBg = -1

class TmDashCardBgVh(val itemView: View, private val listener: TokomemberCardBgAdapterListener) :
    AbstractViewHolder<TokomemberCardBg>(itemView) {

    private val tmCardColorBg = itemView.colorBg
    private val tmCardColorBgSelector = itemView.viewBgSelector

    override fun bind(element: TokomemberCardBg?) {
        element?.apply {
            tmCardColorBg.loadImage(imageUrl)
        }
        if (adapterPosition == selectedItemPosBg) {
            tmCardColorBgSelector.show()
        } else {
            tmCardColorBgSelector.hide()
        }
        itemView.setOnClickListener {
            selectedItemPosBg = adapterPosition
            lastItemSelectedPosBg = if (lastItemSelectedPosBg == -1)
                selectedItemPosBg
            else {
                listener.onItemClickCardCBg(
                    tokoCardItem = element,
                    position = lastItemSelectedPosBg
                )
                selectedItemPosBg
            }
            listener.onItemClickCardCBg(tokoCardItem = element, position = selectedItemPosBg)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_colorbg_item
    }
}
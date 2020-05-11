package com.tokopedia.feedplus.view.adapter.viewholder.productcard

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.Button

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R

/**
 * @author by nisie on 5/15/17.
 */

class EmptyFeedViewHolder(itemView: View, listener: EmptyFeedListener) : AbstractViewHolder<EmptyModel>(itemView) {

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.list_feed_product_empty
    }

    init {
        val searchShopButton = itemView.findViewById<Button>(R.id.search_shop_button)

        searchShopButton.setOnClickListener { listener.onSearchShopButtonClicked() }
    }

    override fun bind(emptyModel: EmptyModel) {

    }

    interface EmptyFeedListener {
        fun onSearchShopButtonClicked()
    }

}

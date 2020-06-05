package com.tokopedia.talk_old.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.producttalk.view.viewmodel.LoadProductTalkThreadViewModel

class LoadProductTalkThreadViewHolder(val v: View,
                                      val listener: LoadTalkListener) :
        AbstractViewHolder<LoadProductTalkThreadViewModel>(v) {

    interface LoadTalkListener {
        fun onLoadClicked()
    }

    companion object {
        val LAYOUT = R.layout.product_talk_load
    }

    val loadMore:View = itemView.findViewById(R.id.loadMore)


    override fun bind(element: LoadProductTalkThreadViewModel) {
        element?.run {

            loadMore.setOnClickListener {
                listener.onLoadClicked()
            }
        }
    }

}

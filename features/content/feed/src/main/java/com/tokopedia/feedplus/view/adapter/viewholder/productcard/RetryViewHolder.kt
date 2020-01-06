package com.tokopedia.feedplus.view.adapter.viewholder.productcard

import androidx.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.viewmodel.RetryModel

/**
 * Created by stevenfredian on 5/31/17.
 */

class RetryViewHolder(itemView: View, val listener: RetryViewHolderListener) : AbstractViewHolder<RetryModel>(itemView) {
    private val button: View

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.retry_layout
    }

    init {
        button = itemView.findViewById(R.id.retry)
    }

    override fun bind(element: RetryModel) {
        button.setOnClickListener { listener.onRetryClicked() }
    }

    interface RetryViewHolderListener {
        fun onRetryClicked()
    }

}
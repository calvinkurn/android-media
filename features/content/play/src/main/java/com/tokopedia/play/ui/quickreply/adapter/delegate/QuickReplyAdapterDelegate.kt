package com.tokopedia.play.ui.quickreply.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.quickreply.viewholder.QuickReplyViewHolder

/**
 * Created by jegul on 13/12/19
 */
class QuickReplyAdapterDelegate(
        private val onQuickReplyClicked: (String) -> Unit
) : TypedAdapterDelegate<String, String, QuickReplyViewHolder>(QuickReplyViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: String, holder: QuickReplyViewHolder) {
        holder.bind(item, onQuickReplyClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): QuickReplyViewHolder {
        return QuickReplyViewHolder(basicView)
    }
}
package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.LoaderUnify

class TopchatLoadingMoreViewHolder(itemView: View?) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    private var loader: LoaderUnify? = itemView?.findViewById(R.id.loader_more_chat)

    override fun bind(element: LoadingMoreModel?) {
        playLoader()
    }

    private fun playLoader() {
        loader?.avd?.start()
    }

    override fun onViewRecycled() {
        loader?.avd?.stop()
        super.onViewRecycled()
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_load_more
    }

}
package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifycomponents.LoaderUnify

class TopchatLoadingMoreViewHolder(itemView: View?) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    private var loader: LoaderUnify? = itemView?.findViewById(R.id.loader_more_chat)
    private var loaderNoAnimation: ProgressBar? = itemView?.findViewById(R.id.pb_no_animation)

    override fun bind(element: LoadingMoreModel?) {
        playLoader()
    }

    private fun playLoader() {
        if (ViewUtil.areSystemAnimationsEnabled(itemView.context)) {
            loaderNoAnimation?.hide()
            loader?.show()
            loader?.avd?.start()
        } else {
            loaderNoAnimation?.show()
            loader?.hide()
        }
    }

    override fun onViewRecycled() {
        loader?.avd?.stop()
        super.onViewRecycled()
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_load_more
    }

}
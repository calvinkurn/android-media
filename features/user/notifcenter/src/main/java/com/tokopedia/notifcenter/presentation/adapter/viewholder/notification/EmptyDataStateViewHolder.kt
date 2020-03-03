package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.state.EmptySource
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean

typealias LayoutParam = ViewGroup.LayoutParams

class EmptyDataStateViewHolder(view: View): AbstractViewHolder<NotificationEmptyStateViewBean>(view) {

    private val container = view.findViewById<RelativeLayout>(R.id.empty_container)
    private val txtMessage = view.findViewById<TextView>(R.id.txt_message)
    private val imgState = view.findViewById<ImageView>(R.id.img_state)

    private val _matchParent by lazy { LayoutParam.MATCH_PARENT }
    private val _wrapContent by lazy { LayoutParam.WRAP_CONTENT }

    override fun bind(element: NotificationEmptyStateViewBean) {
        txtMessage.text = if (element.title != 0) getString(element.title) else ""
        imgState.setImageResource(element.icon)

        when (element.type) {
            is EmptySource.Normal -> {
                setLayoutParamsContainer()
            }
            is EmptySource.Transaction -> {
                setLayoutParamsContainer(
                        height = _wrapContent
                )
            }
        }
    }

    private fun setLayoutParamsContainer(width: Int = _matchParent, height: Int = _matchParent) {
        container.layoutParams = ViewGroup.LayoutParams(width, height)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_empty_state
    }

}
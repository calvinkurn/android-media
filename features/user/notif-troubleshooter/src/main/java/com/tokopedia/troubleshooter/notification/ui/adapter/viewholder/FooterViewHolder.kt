package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.util.ClearCacheUtil
import com.tokopedia.troubleshooter.notification.util.ClearCacheUtil.showClearCache
import com.tokopedia.unifycomponents.UnifyButton

class FooterViewHolder(
        private val listener: FooterListener,
        view: View
): AbstractViewHolder<FooterUIView>(view) {

    private val btnAction = view.findViewById<UnifyButton>(R.id.btnAction)
    private val btnInfo = view.findViewById<ImageView>(R.id.btnInfo)
    private val context by lazy { itemView.context }

    override fun bind(element: FooterUIView?) {
        btnAction?.setOnClickListener {
            showClearCache(context)
        }

        btnInfo?.setOnClickListener {
            listener.onInfoClicked()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_footer_message
    }

}
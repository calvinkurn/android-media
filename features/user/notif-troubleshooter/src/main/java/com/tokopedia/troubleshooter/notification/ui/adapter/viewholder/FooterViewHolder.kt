package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.util.ClearCacheUtil
import com.tokopedia.troubleshooter.notification.util.ClearCacheUtil.showClearCache
import com.tokopedia.troubleshooter.notification.util.gotoAudioSetting
import com.tokopedia.unifycomponents.UnifyButton

class FooterViewHolder(
        private val listener: FooterListener,
        view: View
): AbstractViewHolder<FooterUIView>(view) {

    private val btnAction = view.findViewById<UnifyButton>(R.id.btnAction)
    private val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
    private val txtStatus = view.findViewById<TextView>(R.id.txtStatus)
    private val btnInfo = view.findViewById<ImageView>(R.id.btnInfo)

    private val context by lazy { itemView.context }

    override fun bind(element: FooterUIView?) {
        if (element == null) return

        if (element.isDndMode) {
            txtStatus?.show()
            btnAction?.text = context?.getString(R.string.btn_notif_turnoff_dnd)
            txtMessage?.text = context?.getString(R.string.notif_footer_dnd_message)
        } else {
            txtStatus?.hide()
            btnAction?.text = context?.getString(R.string.btn_notif_clear_cache)
            txtMessage?.text = context?.getString(R.string.notif_footer_message)
        }

        btnAction?.setOnClickListener { onActionClicked(element.isDndMode) }
        btnInfo?.setOnClickListener { listener.onInfoClicked() }
    }

    private fun onActionClicked(isDndMode: Boolean) {
        if (!isDndMode) showClearCache(context) else dndMode()
    }

    private fun dndMode() {
        context?.gotoAudioSetting()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_footer_message
    }

}
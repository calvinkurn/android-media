package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.util.ClearCacheUtil.showClearCache
import com.tokopedia.troubleshooter.notification.util.gotoDeviceSettings
import com.tokopedia.unifycomponents.UnifyButton

class FooterViewHolder(
        private val listener: FooterListener,
        view: View
): AbstractViewHolder<FooterUIView>(view) {

    private val btnAction = view.findViewById<UnifyButton>(R.id.btnAction)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
    private val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
    private val txtStatus = view.findViewById<TextView>(R.id.txtStatus)

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
        txtTitle?.setOnClickListener { listener.onInfoClicked() }

        txtTitle?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                EMPTY_DRAWABLE,
                EMPTY_DRAWABLE,
                R.drawable.ic_ts_notif_info,
                EMPTY_DRAWABLE
        )
    }

    private fun onActionClicked(isDndMode: Boolean) {
        if (!isDndMode) showClearCache(context) else dndMode()
    }

    private fun dndMode() {
        context?.gotoDeviceSettings()
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_footer_message

        private const val EMPTY_DRAWABLE = 0
    }

}
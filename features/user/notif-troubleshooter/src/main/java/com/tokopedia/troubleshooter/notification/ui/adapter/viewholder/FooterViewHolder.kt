package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.util.gotoDeviceSettings
import com.tokopedia.troubleshooter.notification.util.setCustomSpan
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.R.color.Unify_G500 as Unify_G500

class FooterViewHolder(
        private val listener: FooterListener,
        view: View
): AbstractViewHolder<FooterUIView>(view) {

    private val btnAction = view.findViewById<UnifyButton>(R.id.btnAction)
    private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
    private val txtMessage = view.findViewById<TextView>(R.id.txtMessage)
    private val txtStatus = view.findViewById<TextView>(R.id.txtStatus)
    private val txtHelpCare = view.findViewById<TextView>(R.id.txtHelpCare)

    private val context by lazy { itemView.context }

    override fun bind(element: FooterUIView?) {
        if (element == null) return
        helpCareLabel()
        footerVisibilityType(element.isDndMode)

        btnAction?.setOnClickListener { onActionClicked(element.isDndMode) }
        txtTitle?.setOnClickListener { listener.onInfoClicked() }

        txtTitle?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                EMPTY_DRAWABLE,
                EMPTY_DRAWABLE,
                R.drawable.ic_ts_notif_info,
                EMPTY_DRAWABLE
        )
    }

    private fun footerVisibilityType(isDndMode: Boolean) {
        if (isDndMode) {
            txtStatus?.show()
            btnAction?.text = context?.getString(R.string.btn_notif_turnoff_dnd)
            txtMessage?.text = context?.getString(R.string.notif_footer_dnd_message)
        } else {
            txtStatus?.hide()
            btnAction?.text = context?.getString(R.string.btn_notif_clear_cache)
            txtMessage?.text = context?.getString(R.string.notif_footer_message)
        }
    }

    private fun helpCareLabel() {
        val visitCareText = context.getString(R.string.notif_footer_visit_care)
        val unifyGreenColor = ContextCompat.getColor(context, Unify_G500)

        txtHelpCare?.setCustomSpan(visitCareText, unifyGreenColor)

        txtHelpCare?.setOnClickListener {
            context.startActivity(RouteManager.getIntent(context, URL_TOKOPEDIA_CARE))
        }
    }

    private fun onActionClicked(isDndMode: Boolean) {
        if (!isDndMode) {
            listener.onClearCacheClicked()
        } else {
            context?.gotoDeviceSettings()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_footer_message

        private const val URL_TOKOPEDIA_CARE = "https://www.tokopedia.com/help/article/apa-itu-push-notification"
        private const val EMPTY_DRAWABLE = 0
    }

}
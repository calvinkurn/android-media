package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.databinding.ItemFooterMessageBinding
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.util.gotoDeviceSettings
import com.tokopedia.troubleshooter.notification.util.setCustomSpan
import com.tokopedia.unifyprinciples.R.color.Unify_G500
import com.tokopedia.utils.view.binding.viewBinding

class FooterViewHolder(
        private val listener: FooterListener,
        view: View
): AbstractViewHolder<FooterUIView>(view) {

    private val binding: ItemFooterMessageBinding? by viewBinding()
    private val context by lazy { itemView.context }

    override fun bind(element: FooterUIView?) {
        if (element == null) return
        helpCareLabel()
        footerVisibilityType(element.isDndMode)

        binding?.btnAction?.setOnClickListener { onActionClicked(element.isDndMode) }
        binding?.txtTitle?.setOnClickListener { listener.onInfoClicked() }
        binding?.imgStatus?.loadImage(FOOTER_IMG)

        binding?.txtTitle?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                EMPTY_DRAWABLE,
                EMPTY_DRAWABLE,
                R.drawable.ic_ts_notif_info,
                EMPTY_DRAWABLE
        )
    }

    private fun footerVisibilityType(isDndMode: Boolean) {
        if (isDndMode) {
            binding?.txtStatus?.show()
            binding?.btnAction?.text = context.getString(R.string.btn_notif_turnoff_dnd)
            binding?.txtMessage?.text = context.getString(R.string.notif_footer_dnd_message)
        } else {
            binding?.txtStatus?.hide()
            binding?.btnAction?.text = context.getString(R.string.btn_notif_clear_cache)
            binding?.txtMessage?.text = context.getString(R.string.notif_footer_message)
        }
    }

    private fun helpCareLabel() {
        val visitCareText = context.getString(R.string.notif_footer_visit_care)
        val unifyGreenColor = ContextCompat.getColor(context, Unify_G500)

        binding?.txtHelpCare?.setCustomSpan(visitCareText, unifyGreenColor)

        binding?.txtHelpCare?.setOnClickListener {
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

        private const val FOOTER_IMG = TokopediaImageUrl.FOOTER_IMG
        private const val URL_TOKOPEDIA_CARE = "tokopedia://webview?url=https://www.tokopedia.com/help/article/apa-itu-push-notification"
        private const val EMPTY_DRAWABLE = 0
    }

}
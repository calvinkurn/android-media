package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.content.Intent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.data.service.googleplay.PlayServicesManager
import com.tokopedia.troubleshooter.notification.databinding.ItemFooterMessageBinding
import com.tokopedia.troubleshooter.notification.ui.listener.FooterListener
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView
import com.tokopedia.troubleshooter.notification.util.gotoDeviceSettings
import com.tokopedia.troubleshooter.notification.util.setCustomSpan
import com.tokopedia.utils.view.binding.viewBinding

class FooterViewHolder(
        private val listener: FooterListener,
        view: View,
        private val playServicesManager: PlayServicesManager
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
        if (playServicesManager.isPlayServiceExist()) {
            if (isDndMode) {
                binding?.txtStatus?.show()
                binding?.btnAction?.text = context.getString(R.string.btn_notif_turnoff_dnd)
                binding?.txtMessage?.text = context.getString(R.string.notif_footer_dnd_message)
            } else {
                binding?.txtStatus?.hide()
                binding?.btnAction?.text = context.getString(R.string.btn_notif_clear_cache)
                binding?.txtMessage?.text = context.getString(R.string.notif_footer_message)
            }
        } else {
            handleUnavailablePlayServices()
        }
    }

    private fun helpCareLabel() {
        val visitCareText = context.getString(R.string.notif_footer_visit_care)
        val unifyGreenColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)

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

    private fun handleUnavailablePlayServices() {
        binding?.imgStatusPlay?.show()
        adjustingTitleConstraints()
        adjustingHelpCareConstraints()
        binding?.txtMessage?.text = context.getString(R.string.notif_footer_message_play_services)
        binding?.txtTitle?.text = context.getString(R.string.notif_title_play_services)
        binding?.btnActionHome?.show()
        binding?.btnActionHome?.setOnClickListener { jumpToHomePage() }
        hideViews()
    }

    private fun adjustingTitleConstraints() {
        binding?.txtTitle?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToEnd = R.id.img_status_play
            topToTop = R.id.img_status_play
        }
    }

    private fun adjustingHelpCareConstraints() {
        binding?.txtHelpCare?.updateLayoutParams<ConstraintLayout.LayoutParams> {
            topToBottom = R.id.btn_action_home
        }
    }

    private fun hideViews() {
        binding?.txtStatus?.hide()
        binding?.btnAction?.hide()
        binding?.imgStatus?.hide()
    }

    private fun jumpToHomePage() {
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, HOME)
        appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.applicationContext.startActivity(appLinkIntent)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_footer_message

        private const val FOOTER_IMG = TokopediaImageUrl.FOOTER_IMG
        private const val URL_TOKOPEDIA_CARE = "tokopedia://webview?url=https://www.tokopedia.com/help/article/apa-itu-push-notification"
        private const val EMPTY_DRAWABLE = 0
        private const val HOME = "tokopedia://home"
    }

}

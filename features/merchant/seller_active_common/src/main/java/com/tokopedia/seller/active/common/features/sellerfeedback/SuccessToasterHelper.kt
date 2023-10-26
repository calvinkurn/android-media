package com.tokopedia.seller.active.common.features.sellerfeedback

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerfeedback.DeeplinkMapperSellerFeedback
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.seller.active.common.R
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by @ilhamsuaib on 21/03/23.
 */

object SuccessToasterHelper {

    const val SHOW_SETTING_BOTTOM_SHEET = "extra_show_settings"
    private const val TOASTER_HEIGHT = 104
    private const val TOASTER_CTA_WIDTH = 120
    private const val TOASTER_DURATION = 5000
    private const val TOASTER_DELAY = 1000L
    private var isToasterShown = false

    fun showToaster(context: Context, view: View, isScreenShootTriggerEnabled: Boolean) {
        if (isToasterShown) return

        Handler(Looper.getMainLooper()).postDelayed({
            Toaster.toasterCustomBottomHeight = context.dpToPx(TOASTER_HEIGHT).toInt()
            val toaster = if (isScreenShootTriggerEnabled) {
                Toaster.build(
                    view,
                    text = getToastMessage(context, isScreenShootTriggerEnabled),
                    duration = Toaster.LENGTH_LONG
                )
            } else {
                Toaster.toasterCustomCtaWidth = context.dpToPx(TOASTER_CTA_WIDTH).toInt()
                Toaster.build(view,
                    text = getToastMessage(context, isScreenShootTriggerEnabled),
                    actionText = context.getString(R.string.feedback_form_settings),
                    duration = TOASTER_DURATION,
                    clickListener = {
                        openFeedbackForm(context)
                    }
                )
            }
            toaster.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    isToasterShown = false
                }
            }).show()
        }, TOASTER_DELAY)
    }

    fun getToastMessage(
        context: Context,
        isScreenShootTriggerEnabled: Boolean
    ): String {
        return if (isScreenShootTriggerEnabled) {
            context.getString(R.string.feedback_form_screenshoot_tigger_enabled_success_message)
        } else {
            context.getString(R.string.feedback_form_screenshoot_tigger_disabled_success_message)
        }
    }

    private fun openFeedbackForm(context: Context) {
        val intent = RouteManager.getIntent(
            context,
            DeeplinkMapperSellerFeedback.getSellerFeedbackInternalAppLink(context)
        )
        intent.putExtra(SHOW_SETTING_BOTTOM_SHEET, true)
        context.startActivity(intent)
    }
}

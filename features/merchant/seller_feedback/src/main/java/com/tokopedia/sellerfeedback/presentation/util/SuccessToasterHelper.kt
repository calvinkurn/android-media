package com.tokopedia.sellerfeedback.presentation.util

import android.content.Context
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerfeedback.R
import com.tokopedia.unifycomponents.Toaster

/**
 * Created By @ilhamsuaib on 08/10/21
 */

object SuccessToasterHelper {

    const val SHOW_SETTING_BOTTOM_SHEET = "extra_show_settings"
    private const val TOASTER_HEIGHT = 104
    private const val TOASTER_DURATION = 5000
    private const val TOASTER_DELAY = 1000L

    fun showToaster(context: Context, view: View, isScreenShootTriggerEnabled: Boolean) {
        view.postDelayed({
            Toaster.toasterCustomBottomHeight = context.dpToPx(TOASTER_HEIGHT).toInt()
            if (isScreenShootTriggerEnabled) {
                Toaster.build(
                    view,
                    text = getToastMessage(context, isScreenShootTriggerEnabled),
                    duration = Toaster.LENGTH_LONG
                ).show()
            } else {
                Toaster.build(view,
                    text = getToastMessage(context, isScreenShootTriggerEnabled),
                    actionText = context.getString(R.string.feedback_form_settings),
                    duration = TOASTER_DURATION,
                    clickListener = {
                        openFeedbackForm(context)
                    }
                ).show()
            }
        }, TOASTER_DELAY)
    }

    fun getToastMessage(
        context: Context,
        isScreenShootTriggerEnabled: Boolean
    ): String {
        return if (isScreenShootTriggerEnabled) {
            context.getString(R.string.feedback_from_screenshoot_tigger_enabled_success_message)
        } else {
            context.getString(R.string.feedback_from_screenshoot_tigger_disabled_success_message)
        }
    }

    private fun openFeedbackForm(context: Context) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalSellerapp.SELLER_FEEDBACK
        )
        intent.putExtra(SHOW_SETTING_BOTTOM_SHEET, true)
        context.startActivity(intent)
    }
}
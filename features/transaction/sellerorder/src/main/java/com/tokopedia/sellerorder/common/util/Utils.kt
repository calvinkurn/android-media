package com.tokopedia.sellerorder.common.util

import android.content.Context
import android.content.Intent
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import java.util.*

/**
 * Created by fwidjaja on 2019-11-21.
 */
object Utils {
    @JvmStatic
    fun showToasterError(message: String, view: View?) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, SomConsts.ACTION_OK)
        }
    }

    @JvmStatic
    fun createUserNotAllowedDialog(context: Context): DialogUnify {
        context.run {
            return DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.dialog_title_cannot_access_page))
                setDescription(getString(R.string.dialog_description_cannot_access_page))
                setPrimaryCTAText(getString(R.string.button_understand))
                setPrimaryCTAClickListener {
                    goToHome(this@run)
                    dismiss()
                }

                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
    }

    private fun goToHome(context: Context) {
        context.run {
            RouteManager.getIntent(context, ApplinkConst.HOME).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(this)
            }
        }
    }

    @JvmStatic
    fun mapStringTickerTypeToUnifyTickerType(typeString: String): Int {
        return when (typeString) {
            "announcement" -> Ticker.TYPE_ANNOUNCEMENT
            "info" -> Ticker.TYPE_INFORMATION
            "warning" -> Ticker.TYPE_WARNING
            "error" -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    @JvmStatic
    fun getL2CancellationReason(text: String, textToAppend: String = ""): String {
        return if (text.contains('-')) {
            "$textToAppend ${text.split(" - ").last().decapitalize()}"
        } else {
            text
        }
    }
}
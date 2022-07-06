package com.tokopedia.ovop2p.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import java.util.*
import java.util.regex.Pattern


object OvoP2pUtil {

    fun getOvoUserTransferConfirmSubmitAlertDialog(context: Activity?, onClickListener: View.OnClickListener, dataMap: HashMap<String, Any>): AlertDialog.Builder {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.ovo_p2p_confirmation_dlg, null)
        if (dialogView != null) {
            var rcvrName = dataMap[Constants.Keys.NAME].toString()
            if (!TextUtils.isEmpty(rcvrName)) {
                dialogView.findViewById<TextView>(R.id.rcvr_name).text = rcvrName
                dialogView.findViewById<TextView>(R.id.rcvr_name).visibility = View.VISIBLE
            }
            dialogView.findViewById<TextView>(R.id.rcvr_no).text =
                dataMap[Constants.Keys.TO_PHN_NO].toString()
            dialogView.findViewById<TextView>(R.id.trnsfr_amt).text =
                "Rp" + dataMap[Constants.Keys.FORMATTED_AMOUNT].toString()
            dialogView.findViewById<TextView>(R.id.msg).text =
                dataMap[Constants.Keys.MESSAGE].toString()
        }
        dialogView?.findViewById<View>(R.id.proceed_dlg)?.setOnClickListener(onClickListener)
        dialogView?.findViewById<View>(R.id.cancel)?.setOnClickListener(onClickListener)
        dialogBuilder.setView(dialogView)
        return dialogBuilder
    }


    fun extractNumbersFromString(srcStr: String): String {
        var numStr = srcStr.split("-").toString()
        val p = Pattern.compile("\\d+")
        val m = p.matcher(numStr)
        var result = ""
        while (m.find()) {
            result += m.group()
        }
        return result
    }

    fun getNonOvoUserConfirmationDailog(context: Activity?, onClickListener: View.OnClickListener): AlertDialog.Builder {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.non_ovo_user_confirmation_dialog, null)
        dialogView?.findViewById<View>(R.id.proceed_dlg)?.setOnClickListener(onClickListener)
        dialogView?.findViewById<View>(R.id.close)?.setOnClickListener(onClickListener)
        dialogBuilder.setView(dialogView)
        return dialogBuilder
    }

    fun checkValidRcvrPhoneEntry(svQuery: String, rcvrPhnNo: String): String {
        return if (!TextUtils.isEmpty(rcvrPhnNo) && svQuery.contains(rcvrPhnNo)) {
            rcvrPhnNo
        } else {
            svQuery
        }
    }

    fun createErrorSnackBar(
        activity: Activity,
        errorMsg: String,
        onClickListener: View.OnClickListener
    ): Snackbar {
        return if (!TextUtils.isEmpty(errorMsg))
            Toaster.build( activity.findViewById(android.R.id.content), errorMsg, Snackbar.LENGTH_LONG, type =  Toaster.TYPE_ERROR,  activity.getString(
                            R.string.ovop2p_oke),onClickListener)
        else
            Toaster.build( activity.findViewById(android.R.id.content), activity.getString(R.string.ovop2p_tryagain_later), Snackbar.LENGTH_LONG, type =  Toaster.TYPE_ERROR,  activity.getString(R.string.ovop2p_oke),onClickListener)

    }

    fun getUserId(context: Context): String{
        var userSession = UserSession(context)
        return userSession.userId
    }

    fun getUserName(context: Context): String{
        var userSession = UserSession(context)
        return userSession.name
    }

    fun getDeviceId(context: Context): String{
        var userSession = UserSession(context)
        return userSession.deviceId
    }

    fun getDiscoveryPageIntent(context: Context): Intent{
        val intent = RouteManager.getIntent(
                context,
                ApplinkConst.DISCOVERY_PAGE.replace(Constants.DeepLinkConstants.PAGE_ID, Constants.DeepLinkConstants.DISCOVERY_PAGE)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        return intent
    }
}

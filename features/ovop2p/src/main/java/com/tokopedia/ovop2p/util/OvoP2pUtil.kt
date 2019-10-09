package com.tokopedia.ovop2p.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.ovop2p.Constants

import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.model.WalletDataBase
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import java.util.HashMap
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
            dialogView.findViewById<TextView>(R.id.rcvr_no).text = dataMap[Constants.Keys.TO_PHN_NO].toString()
            dialogView.findViewById<TextView>(R.id.trnsfr_amt).text = "Rp" + dataMap[Constants.Keys.FORMATTED_AMOUNT].toString()
            dialogView.findViewById<TextView>(com.tokopedia.abstraction.R.id.msg).text = dataMap[Constants.Keys.MESSAGE].toString()
        }
        dialogView?.findViewById<View>(R.id.proceed_dlg)?.setOnClickListener(onClickListener)
        dialogView?.findViewById<View>(R.id.cancel)?.setOnClickListener(onClickListener)
        dialogBuilder.setView(dialogView)
        return dialogBuilder
    }

    fun executeOvoP2pTransferRequest(context: Context, subscriber: Subscriber<GraphqlResponse>, gqlMutationDataMap: HashMap<String, Any>) {
        val ovoP2pTransferRequestUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.ovo_p2p_transfer_request),
                OvoP2pTransferRequestBase::class.java, gqlMutationDataMap)
        ovoP2pTransferRequestUseCase.addRequest(graphqlRequest)
        ovoP2pTransferRequestUseCase.execute(subscriber)
    }

    fun executeOvoP2pTransferConfirm(context: Context, subscriber: Subscriber<GraphqlResponse>, gqlMutationDataMap: HashMap<String, Any>) {
        val ovoP2pTransferConfirmUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.ovo_p2p_transfer_confirm),
                OvoP2pTransferConfirmBase::class.java, gqlMutationDataMap)
        ovoP2pTransferConfirmUseCase.addRequest(graphqlRequest)
        ovoP2pTransferConfirmUseCase.execute(subscriber)
    }

    fun executeOvoP2pTransferThankyou(context: Context, subscriber: Subscriber<GraphqlResponse>, gqlMutationDataMap: HashMap<String, Any>) {
        val ovoP2pTransferThankyouUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.ovo_p2p_thank_you_page),
                OvoP2pTransferThankyouBase::class.java, gqlMutationDataMap)
        ovoP2pTransferThankyouUseCase.addRequest(graphqlRequest)
        ovoP2pTransferThankyouUseCase.execute(subscriber)
    }

    fun executeOvoGetWalletData(context: Context, subscriber: Subscriber<GraphqlResponse>) {
        var ovoWalletDataUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.ovop2p_wallet_detail),
                WalletDataBase::class.java)
        ovoWalletDataUseCase.addRequest(graphqlRequest)
        ovoWalletDataUseCase.execute(subscriber)

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

    fun createErrorSnackBar(activity: Activity,
                            onClickListener: View.OnClickListener,
                            errorMsg: String): Snackbar {
        val snackbar = Snackbar.make(activity.findViewById(
                android.R.id.content), "", Snackbar.LENGTH_LONG)
        val layout = snackbar.view as Snackbar.SnackbarLayout
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val snackView = inflater.inflate(com.tokopedia.design.R.layout.error_snackbar_layout, null)
        snackView.findViewById<View>(com.tokopedia.design.R.id.btn_ok).setOnClickListener(onClickListener)
        if (!TextUtils.isEmpty(errorMsg)) (snackView.findViewById<View>(com.tokopedia.design.R.id.error_msg) as TextView).text = errorMsg
        layout.addView(snackView, 0)
        return snackbar
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

package com.tokopedia.ovop2p.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.ovop2p.Constants

import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.model.OvoP2pTransferConfirmBase
import com.tokopedia.ovop2p.model.OvoP2pTransferRequestBase
import com.tokopedia.ovop2p.model.OvoP2pTransferThankyouBase
import rx.Subscriber
import java.util.HashMap
import java.util.regex.Pattern


object OvoP2pUtil {

    fun getOvoUserTransferConfirmSubmitAlertDialog(context: Activity?, onClickListener: View.OnClickListener, dataMap: HashMap<String, Any>): AlertDialog.Builder {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.ovo_p2p_confirmation_dlg, null)
        if(dialogView != null) {
            dialogView.findViewById<TextView>(R.id.rcvr_name).text = dataMap[Constants.Keys.NAME].toString()
            dialogView.findViewById<TextView>(R.id.rcvr_no).text = dataMap[Constants.Keys.TO_PHN_NO].toString()
            dialogView.findViewById<TextView>(R.id.trnsfr_amt).text = dataMap[Constants.Keys.FORMATTED_AMOUNT].toString()
            dialogView.findViewById<TextView>(R.id.msg).text = dataMap[Constants.Keys.MESSAGE].toString()
        }
        dialogView?.findViewById<View>(R.id.proceed)?.setOnClickListener(onClickListener)
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
        val ovoP2pTransferRequestUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.ovo_p2p_transfer_confirm),
                OvoP2pTransferConfirmBase::class.java, gqlMutationDataMap)
        ovoP2pTransferRequestUseCase.addRequest(graphqlRequest)
        ovoP2pTransferRequestUseCase.execute(subscriber)
    }

    fun executeOvoP2pTransferThankyou(context: Context, subscriber: Subscriber<GraphqlResponse>, gqlMutationDataMap: HashMap<String, Any>) {
        val ovoP2pTransferRequestUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.ovo_p2p_thank_you_page),
                OvoP2pTransferThankyouBase::class.java, gqlMutationDataMap)
        ovoP2pTransferRequestUseCase.addRequest(graphqlRequest)
        ovoP2pTransferRequestUseCase.execute(subscriber)
    }

    fun extractNumbersFromString(srcStr: String): String{
        val p = Pattern.compile("\\d+")
        val m = p.matcher(srcStr)
        var result = ""
        while (m.find()) {
            result += m.group()
        }
        return result
    }

    fun getNonOvoUserConfirmationDailog(context: Activity?, onClickListener: View.OnClickListener): AlertDialog.Builder{
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.non_ovo_user_confirmation_dialog, null)
        dialogView?.findViewById<View>(R.id.proceed_dlg)?.setOnClickListener(onClickListener)
        dialogView?.findViewById<View>(R.id.cancel)?.setOnClickListener(onClickListener)
        dialogBuilder.setView(dialogView)
        return dialogBuilder
    }
}

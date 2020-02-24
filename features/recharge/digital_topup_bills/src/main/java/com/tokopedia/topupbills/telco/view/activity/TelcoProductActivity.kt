package com.tokopedia.topupbills.telco.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.gms.actions.SearchIntents
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.AssistActionBuilder
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType

/**
 * Created by nabillasabbaha on 11/04/19.
 *
 * applink
 * tokopedia-android-internal://digital/telco
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.TELCO_DIGITAL)
 */

class TelcoProductActivity : BaseTelcoActivity() {
    private val actionTokenExtra = "actions.fulfillment.extra.ACTION_TOKEN"

    override fun getNewFragment(): Fragment {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(PARAM_MENU_ID)
                ?: TelcoComponentType.TELCO_PREPAID.toString()
        digitalTelcoExtraParam.categoryId = bundle?.getString(PARAM_CATEGORY_ID) ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(PARAM_PRODUCT_ID) ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER) ?: ""
        return DigitalTelcoFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.handleIntent()
        updateTitle(getString(R.string.digital_title_telco_page))
    }

    companion object {

        val PARAM_MENU_ID = "menu_id"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_CLIENT_NUMBER = "client_number"
        val PARAM_CATEGORY_ID = "category_id"

        fun newInstance(context: Context, menuId: String, categoryId: String, productId: String = "", clientNumber: String = ""): Intent {
            val intent = Intent(context, TelcoProductActivity::class.java)
            intent.putExtra(PARAM_MENU_ID, menuId)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            intent.putExtra(PARAM_CLIENT_NUMBER, clientNumber)
            return intent
        }
    }

    override fun onBackPressed() {
        (fragment as DigitalTelcoFragment).onBackPressed()
        super.onBackPressed()
    }

    private fun Intent.handleIntent() {
        when (action) {
            Intent.ACTION_VIEW -> handleDeepLink(data)
            SearchIntents.ACTION_SEARCH -> {
            }
            else -> {
            }
        }
    }

    private fun handleDeepLink(data: Uri?) {
        var actionHandled = true
        when (data?.path) {
            "/telco" -> actionHandled = true
            else -> actionHandled = false
        }


        notifyActionSuccess(actionHandled)
    }

    private fun notifyActionSuccess(succeed: Boolean) {
        intent.getStringExtra(actionTokenExtra)?.let { actionToken ->
            val actionStatus = if (succeed) {
                Action.Builder.STATUS_TYPE_COMPLETED
            } else {
                Action.Builder.STATUS_TYPE_FAILED
            }
            val action = AssistActionBuilder()
                    .setActionToken(actionToken)
                    .setActionStatus(actionStatus)
                    .build()

            FirebaseUserActions.getInstance().end(action)
        }
    }
}
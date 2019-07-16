package com.tokopedia.instantdebitbca.data.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

import com.bca.xco.widget.BCAEditXCOWidget
import com.bca.xco.widget.XCOEnum
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase
import com.tokopedia.instantdebitbca.data.view.activity.BcaEditLimitActivity
import com.tokopedia.instantdebitbca.data.view.activity.InstantDebitBcaActivity
import com.tokopedia.network.utils.AuthUtil

import java.util.HashMap
import javax.inject.Inject

class EditLimitFragment @Inject constructor(): InstantDebitBcaFragment() {

    private var xcoid: String? = null
    private var widgetBca: BCAEditXCOWidget? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        xcoid = arguments!!.getString(BcaEditLimitActivity.XCOID)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun createAndSetBcaWidget() {
        widgetBca = BCAEditXCOWidget(activity, XCOEnum.ENVIRONMENT.DEV)
        widgetBca!!.setListener(this)
        layoutWidget.addView(widgetBca)
    }

    override fun openWidgetBca(accessToken: String) {
        widgetBca!!.openWidget(accessToken, AuthUtil.KEY.API_KEY_INSTANT_DEBIT_BCA, AuthUtil.KEY.API_SEED_INSTANT_DEBIT_BCA,
                userSession!!.userId, AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_ID, xcoid)
    }

    override fun onBCASuccess(xcoID: String, credentialType: String, credentialNo: String, maxLimit: String) {
        val mapCardData = HashMap<String, String>()
        mapCardData[NotifyDebitRegisterBcaUseCase.XCOID] = xcoID
        mapCardData[NotifyDebitRegisterBcaUseCase.MAX_LIMIT] = maxLimit
        val debitData = convertObjToJsonString(mapCardData)
        presenter!!.notifyDebitRegisterEditLimit(debitData, "")
    }

    companion object {

        fun newInstance(context: Context, callbackUrl: String, xcoid: String): Fragment {
            val fragment = EditLimitFragment()
            val bundle = Bundle()
            bundle.putString(InstantDebitBcaActivity.CALLBACK_URL, callbackUrl)
            bundle.putString(BcaEditLimitActivity.XCOID, xcoid)
            fragment.arguments = bundle
            return fragment
        }
    }

}

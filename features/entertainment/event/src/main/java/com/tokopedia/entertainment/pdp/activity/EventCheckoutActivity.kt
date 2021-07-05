package com.tokopedia.entertainment.pdp.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment
import com.tokopedia.oms.scrooge.ScroogePGUtil

class EventCheckoutActivity : BaseSimpleActivity(), HasComponent<EventPDPComponent>{

    override fun getComponent(): EventPDPComponent =
            DaggerEventPDPComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getNewFragment(): Fragment? = EventCheckoutFragment.newInstance(
            intent.getStringExtra(EXTRA_URL_PDP),
            intent.getParcelableExtra(EXTRA_META_DATA),
            intent.getStringExtra(EXTRA_PACKAGE_ID),
            intent.getStringExtra(EXTRA_GATEWAY_CODE)
    )

    companion object{
        const val EXTRA_URL_PDP= "EXTRA_URL_PDP"
        const val EXTRA_META_DATA = "EXTRA_META_DATA"
        const val EXTRA_PACKAGE_ID = "EXTRA_PACKAGE_ID"
        const val EXTRA_GATEWAY_CODE = "EXTRA_GATEWAY_CODE"

        fun createIntent(context: Context, urlPDP : String, metaDataResponse: MetaDataResponse, packageID : String,
                         gatewayCode : String
        ): Intent = Intent(context,EventCheckoutActivity::class.java)
                .putExtra(EXTRA_META_DATA, metaDataResponse)
                .putExtra(EXTRA_URL_PDP,urlPDP)
                .putExtra(EXTRA_PACKAGE_ID, packageID)
                .putExtra(EXTRA_GATEWAY_CODE, gatewayCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScroogePGUtil.REQUEST_CODE_OPEN_SCROOGE_PAGE) {
            if (data != null) {
                this.let {
                    val url = getString(R.string.ent_checkout_to_order_detail,data.getStringExtra(ScroogePGUtil.SUCCESS_MSG_URL))
                    RouteManager.route(it, url)
                    finish()
                }
            }
        }
    }

}
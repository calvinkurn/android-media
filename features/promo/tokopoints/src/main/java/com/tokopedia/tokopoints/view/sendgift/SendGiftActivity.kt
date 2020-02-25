package com.tokopedia.tokopoints.view.sendgift

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent
import com.tokopedia.tokopoints.di.TokoPointComponent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant

class SendGiftActivity : BaseSimpleActivity(), HasComponent<TokoPointComponent?> {
    private var tokoPointComponent: TokoPointComponent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.tp_title_send_coupon))
        toolbar.setNavigationIcon(com.tokopedia.design.R.drawable.ic_close)
        toolbar.setNavigationOnClickListener {
            AnalyticsTrackerUtil.sendEvent(this@SendGiftActivity,
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KIRIM_KUPON,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
                    couponTitle)
            onBackPressed()
        }
    }

    override fun getNewFragment(): Fragment {
        return SendGiftFragment.newInstance(intent.extras)
    }

    private val couponTitle: String
        private get() {
            if (intent != null) {
                val couponTitle = intent.getStringExtra(CommonConstant.EXTRA_COUPON_TITLE)
                if (couponTitle != null) return couponTitle
            }
            return ""
        }

    override fun getComponent(): TokoPointComponent {
        if (tokoPointComponent == null) initInjector()
        return tokoPointComponent!!
    }

    private fun initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        fun getCallingIntent(context: Context?, extras: Bundle?): Intent {
            val intent = Intent(context, SendGiftActivity::class.java)
            intent.putExtras(extras)
            return intent
        }
    }
}
package com.tokopedia.pms.howtopay_native.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.howtopay.ReactNativeHowToPayActivity
import com.tokopedia.pms.howtopay_native.di.HowToPayComponent
import com.tokopedia.pms.howtopay_native.ui.fragment.HowToPayFragment
import com.tokopedia.tkpdreactnative.react.ReactConst

open class HowToPayActivity : BaseSimpleActivity(), HasComponent<HowToPayComponent> {

    override fun getNewFragment(): Fragment? = HowToPayFragment.getInstance(intent)

    override fun getComponent(): HowToPayComponent {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = getReactNativeProps()
        val reactIntent = Intent(this, ReactNativeHowToPayActivity::class.java)
        reactIntent.data = intent.data
        startActivity(intent)
    }

    private fun getReactNativeProps(): Bundle? {
        var bundle = intent.extras
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.HOW_TO_PAY)
        bundle.putString(ReactNativeHowToPayActivity.EXTRA_TITLE, "Cara Pembayaran")
        val newBundle = Bundle()
        for (key in bundle.keySet()) {
            if (!key.equals(ReactNativeHowToPayActivity.IS_DEEP_LINK_FLAG, ignoreCase = true) &&
                    !key.equals(ReactNativeHowToPayActivity.ANDROID_INTENT_EXTRA_REFERRER, ignoreCase = true) &&
                    !key.equals(ReactNativeHowToPayActivity.DEEP_LINK_URI, ignoreCase = true)) {
                newBundle.putString(key, bundle.getString(key))
            }
        }
        return newBundle
    }

}
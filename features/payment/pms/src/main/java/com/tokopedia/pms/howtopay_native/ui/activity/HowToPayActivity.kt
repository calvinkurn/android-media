package com.tokopedia.pms.howtopay_native.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.howtopay.ReactNativeHowToPayActivity
import com.tokopedia.pms.howtopay_native.di.DaggerHowToPayComponent
import com.tokopedia.pms.howtopay_native.di.HowToPayComponent
import com.tokopedia.pms.howtopay_native.di.HowToPayModule
import com.tokopedia.pms.howtopay_native.ui.fragment.HowToPayFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class HowToPayActivity : BaseSimpleActivity(), HasComponent<HowToPayComponent> {

    private val KEY_IS_NEW_HOW_TO_PAY_PAGE = "app_flag_is_native_howtopay"

    override fun getNewFragment(): Fragment? = HowToPayFragment.getInstance(this, intent.extras)

    override fun getComponent(): HowToPayComponent {
        return DaggerHowToPayComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .howToPayModule(HowToPayModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isNewHowToPayPageEnabled())
            startRectHowToPayActivity()
    }

    private fun startRectHowToPayActivity() {
        var bundle = intent.extras
        if (bundle == null) {
            bundle = Bundle()
        }

        val newBundle = Bundle()
        for (key in bundle.keySet()) {
            newBundle.putString(key, bundle.getString(key))
        }
        val reactIntent = Intent(this, ReactNativeHowToPayActivity::class.java)
        reactIntent.putExtras(newBundle)
        startActivity(reactIntent)
        finish()
    }

    private fun isNewHowToPayPageEnabled(): Boolean {
        return try {
            val remoteConfig = FirebaseRemoteConfigImpl(this)
            return remoteConfig.getBoolean(KEY_IS_NEW_HOW_TO_PAY_PAGE, true)
        } catch (e: Exception) {
            true
        }
    }

}

package com.tokopedia.sellerorder.detail.presentation.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.detail.di.DaggerSomDetailComponent
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailActivity: BaseSimpleActivity(), HasComponent<SomDetailComponent> {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setWhiteStatusBarBackground()
    }

    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
        }
        return SomDetailFragment.newInstance(bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.som_action_chat -> {
                onChatClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onChatClicked() {
        (fragment as? SomDetailFragment)?.doClickChat()
    }

    override fun getComponent(): SomDetailComponent =
        DaggerSomDetailComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()

    private fun setWhiteStatusBarBackground() {
        if (GlobalConfig.isSellerApp() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        // Do not put super, avoid crash "Can not perform this action after onSaveInstanceState"
    }
}
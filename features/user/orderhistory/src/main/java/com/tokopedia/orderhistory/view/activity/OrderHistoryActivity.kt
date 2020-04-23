package com.tokopedia.orderhistory.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.di.DaggerOrderHistoryComponent
import com.tokopedia.orderhistory.view.fragment.OrderHistoryFragment

class OrderHistoryActivity : BaseSimpleActivity(), HasComponent<OrderHistoryComponent>,
        OrderHistoryFragment.Listener {

    override fun getNewFragment(): Fragment? {
        return OrderHistoryFragment.createInstance(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightNotificationBar()
        initWindowBackground()
        setupToolbar()
    }

    override fun getComponent(): OrderHistoryComponent {
        return DaggerOrderHistoryComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }

    override fun onClickOrderHistory(intent: Intent) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

package com.tokopedia.orderhistory.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.di.DaggerOrderHistoryComponent
import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.di.OrderHistoryContextModule
import com.tokopedia.orderhistory.view.fragment.OrderHistoryFragment

open class OrderHistoryActivity : BaseSimpleActivity(), HasComponent<OrderHistoryComponent> {

    private var orderHistoryComponent: OrderHistoryComponent? = null

    override fun getNewFragment(): Fragment? {
        return OrderHistoryFragment.createInstance(getArgument())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightNotificationBar()
        initWindowBackground()
        setupToolbar()
    }

    override fun getComponent(): OrderHistoryComponent {
        return orderHistoryComponent ?: initializeOrderHistoryComponent()
    }

    protected open fun initializeOrderHistoryComponent(): OrderHistoryComponent {
        return DaggerOrderHistoryComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .orderHistoryContextModule(OrderHistoryContextModule(this))
                .build().also {
                    orderHistoryComponent = it
                }
    }

    private fun getArgument(): Bundle? {
        val bundle = Bundle()
        val shopId = intent.data?.lastPathSegment ?: return bundle
        return bundle.apply {
            putString(ApplinkConst.OrderHistory.PARAM_SHOP_ID, shopId)
        }
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        supportActionBar?.setTitle(R.string.title_orderhistory)
        toolbar.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

}

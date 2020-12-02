package com.tokopedia.paylater.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent.builder
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.DaggerPayLaterComponent
import com.tokopedia.paylater.di.component.DaggerPayLaterComponent.builder
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.presentation.fragment.PayLaterFragment

class PayLaterActivity : BaseSimpleActivity(), HasComponent<PayLaterComponent> {

    private lateinit var payLaterComponent: PayLaterComponent
    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(SCREEN_NAME)
    }

    override fun getLayoutRes() = R.layout.activity_paylater

    override fun getToolbarResourceID() = R.id.paylater_header

    override fun getParentViewResourceID(): Int = R.id.paylater_parent_view

    override fun getNewFragment(): Fragment? {
        return PayLaterFragment.newInstance()
    }

    companion object {
        const val SCREEN_NAME = "PayLater & Cicilan"
    }

    override fun getComponent(): PayLaterComponent {
        if (!::payLaterComponent.isInitialized)
            payLaterComponent = DaggerPayLaterComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication)
                            .baseAppComponent).build()
        return payLaterComponent
    }
}
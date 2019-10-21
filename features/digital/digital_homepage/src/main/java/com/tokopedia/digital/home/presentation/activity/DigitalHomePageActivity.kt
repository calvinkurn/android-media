package com.tokopedia.digital.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.di.DigitalHomePageComponent
import com.tokopedia.digital.home.di.DigitalHomePageComponentInstance
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageFragment
import com.tokopedia.graphql.data.GraphqlClient

class DigitalHomePageActivity : BaseSimpleActivity(), HasComponent<DigitalHomePageComponent> {

    private lateinit var travelHomepageComponent: DigitalHomePageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        GraphqlClient.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    override fun getNewFragment(): Fragment = DigitalHomePageFragment.getInstance()

    override fun getComponent(): DigitalHomePageComponent {
        if (!::travelHomepageComponent.isInitialized) {
            travelHomepageComponent = DigitalHomePageComponentInstance.getTravelHomepageComponent(application)
        }
        return travelHomepageComponent
    }

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, DigitalHomePageActivity::class.java)
    }
}
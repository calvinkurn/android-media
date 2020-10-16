package com.tokopedia.digital.home.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponentInstance
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageSearchFragment
import com.tokopedia.graphql.data.GraphqlClient

class DigitalHomePageSearchActivity : BaseSimpleActivity(), HasComponent<RechargeHomepageComponent> {

    private lateinit var rechargeHomepageComponent: RechargeHomepageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        GraphqlClient.init(this)
    }

    override fun getNewFragment(): Fragment = DigitalHomePageSearchFragment.getInstance()

    override fun getComponent(): RechargeHomepageComponent {
        if (!::rechargeHomepageComponent.isInitialized) {
            rechargeHomepageComponent = RechargeHomepageComponentInstance.getRechargeHomepageComponent(application)
        }
        return rechargeHomepageComponent
    }

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, DigitalHomePageSearchActivity::class.java)
    }
}
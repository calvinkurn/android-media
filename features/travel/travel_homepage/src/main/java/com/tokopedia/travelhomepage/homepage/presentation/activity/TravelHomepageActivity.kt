package com.tokopedia.travelhomepage.homepage.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travelhomepage.homepage.TravelHomepageComponentInstance
import com.tokopedia.travelhomepage.homepage.di.TravelHomepageComponent
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment

class TravelHomepageActivity : BaseSimpleActivity(), HasComponent<TravelHomepageComponent> {

    private lateinit var travelHomepageComponent: TravelHomepageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        initInjector()
        GraphqlClient.init(this)
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getNewFragment(): Fragment = TravelHomepageFragment.getInstance()

    override fun getComponent(): TravelHomepageComponent {
        if (!::travelHomepageComponent.isInitialized) {
            travelHomepageComponent = TravelHomepageComponentInstance.getTravelHomepageComponent(application)
        }
        return travelHomepageComponent
    }

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, TravelHomepageActivity::class.java)
    }
}

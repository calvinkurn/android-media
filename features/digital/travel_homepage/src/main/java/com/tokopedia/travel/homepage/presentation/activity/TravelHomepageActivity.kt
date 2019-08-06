package com.tokopedia.travel.homepage.presentation.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travel.homepage.TravelHomepageComponentInstance
import com.tokopedia.travel.homepage.di.TravelHomepageComponent

class TravelHomepageActivity : BaseSimpleActivity() {

    private lateinit var travelHomepageComponent: TravelHomepageComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        GraphqlClient.init(this)
    }

    private fun  initInjector() {
        getComponent().inject(this)
    }

    override fun getNewFragment(): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getComponent(): TravelHomepageComponent {
        if (!::travelHomepageComponent.isInitialized) {
            travelHomepageComponent = TravelHomepageComponentInstance.getTravelHomepageComponent(application)
        }
        return travelHomepageComponent
    }

}

package com.tokopedia.travelhomepage.destination.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travelhomepage.destination.TravelDestinationComponentInstance
import com.tokopedia.travelhomepage.destination.di.TravelDestinationComponent
import com.tokopedia.travelhomepage.destination.presentation.fragment.TravelDestinationFragment

class TravelDestinationActivity : BaseSimpleActivity(), HasComponent<TravelDestinationComponent> {

    private lateinit var travelDestinationComponent: TravelDestinationComponent

    override fun getComponent(): TravelDestinationComponent {
        if (!::travelDestinationComponent.isInitialized) {
            travelDestinationComponent = TravelDestinationComponentInstance.getTravelDestinationComponent(application)
        }
        return travelDestinationComponent
    }

    override fun getNewFragment(): Fragment = TravelDestinationFragment.getInstance("https://m.tokopedia.com/travel-entertainment/bandung/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        initInjector()
        GraphqlClient.init(this)
    }

    private fun initInjector() {
        component.inject(this)
    }

    companion object {
        const val EXTRA_DESTINATION_WEB_URL = "EXTRA_DESTINATION_WEB_URL"
    }
}

package com.tokopedia.paylater.common.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.paylater.R
import com.tokopedia.paylater.common.di.component.DaggerPdpSimulationComponent
import com.tokopedia.paylater.common.di.component.PdpSimulationComponent
import com.tokopedia.paylater.common.presentation.fragment.PdpSimulationFragment

class PdpSimulationActivity : BaseSimpleActivity(), HasComponent<PdpSimulationComponent> {

    private lateinit var pdpSimulationComponent: PdpSimulationComponent

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.pdp_simulation_header_title))
    }

    override fun getLayoutRes() = R.layout.activity_pdp_simulation

    override fun getToolbarResourceID() = R.id.pdpSimulationHeader

    override fun getParentViewResourceID(): Int = R.id.pdpSimulationParentView

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null)
            bundle.putAll(intent.extras)
        return PdpSimulationFragment.newInstance(bundle)
    }

    companion object {
        const val SCREEN_NAME = "PayLater & Cicilan"
    }

    override fun getComponent(): PdpSimulationComponent {
        if (!::pdpSimulationComponent.isInitialized)
            pdpSimulationComponent = DaggerPdpSimulationComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication)
                            .baseAppComponent).build()
        return pdpSimulationComponent
    }
}
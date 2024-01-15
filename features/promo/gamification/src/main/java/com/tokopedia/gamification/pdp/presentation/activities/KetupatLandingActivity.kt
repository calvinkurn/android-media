package com.tokopedia.gamification.pdp.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.data.di.components.PdpComponent
import com.tokopedia.gamification.pdp.presentation.fragments.KetupatLandingFragment
import timber.log.Timber
import com.tokopedia.gamification.R as gamificationR

class KetupatLandingActivity :
    BaseSimpleActivity(),
    HasComponent<PdpComponent> {

    private lateinit var fm: FrameLayout

    private val pdpComponent: PdpComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

    fun getLayout() = gamificationR.layout.activity_ketupat_landing

    open fun getDestinationFragment(): Fragment = KetupatLandingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        pdpComponent.injectKetupatLandingActivity(this)
        super.onCreate(savedInstanceState)
        try {
            setContentView(getLayout())
            fm = findViewById(gamificationR.id.fmLandingPage)
        } catch (th: Throwable) {
            Timber.e(th)
            finish()
            return
        }

        supportFragmentManager
            .beginTransaction()
            .add(fm.id, getDestinationFragment())
            .commit()
    }

    override fun onStart() {
        try {
            super.onStart()
        } catch (ex: Exception) {
            Timber.e(ex)
            finish()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun initInject() = DaggerPdpComponent.builder()
        .activityContextModule(ActivityContextModule(this))
        .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun getComponent(): PdpComponent = pdpComponent
}

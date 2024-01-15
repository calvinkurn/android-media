package com.tokopedia.gamification.pdp.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.data.di.components.PdpComponent
import com.tokopedia.gamification.pdp.presentation.fragments.KetupatLandingFragment
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.gamification.R as gamificationR

open class KetupatLandingActivity :
    BaseActivity(),
    HasComponent<PdpComponent> {

    @Inject
    lateinit var userSession: UserSession
    private lateinit var fm: FrameLayout

    private val pdpComponent: PdpComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

    fun getLayout() = gamificationR.layout.activity_ketupat_landing

    open fun getDestinationFragment(): Fragment = KetupatLandingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdpComponent.injectKetupatLandingActivity(this)
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

//    fun checkLoggedIn() {

//    fun afterLoginAttempt() {
//        if (userSession.isLoggedIn) {
//            showGiftBoxFragment()
//        } else {
//            finish()
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initInject() = DaggerPdpComponent.builder()
        .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun getComponent(): PdpComponent = pdpComponent

}

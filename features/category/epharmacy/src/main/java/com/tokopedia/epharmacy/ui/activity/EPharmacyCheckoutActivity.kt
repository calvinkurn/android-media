package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyCheckoutFragment
import com.tokopedia.epharmacy.utils.EPHARMACY_CONSULTATION_SOURCE_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyCheckoutActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var consultationId = ""
    private var enablerId = ""
    private var groupId = ""

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setPageTitle()
        initArguments()
    }

    private fun setPageTitle() {
        updateTitle(getString(R.string.epharmacy_checkout_page_title))
    }

    private fun initArguments() {
        groupId = intent?.extras?.getString(EPHARMACY_GROUP_ID, "") ?: ""
        enablerId = intent?.extras?.getString(EPHARMACY_ENABLER_ID, "") ?: ""
        consultationId = intent?.extras?.getString(EPHARMACY_CONSULTATION_SOURCE_ID, "") ?: ""
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        return EPharmacyCheckoutFragment.newInstance(Bundle().apply {
            putString(EPHARMACY_GROUP_ID,groupId)
            putString(EPHARMACY_ENABLER_ID,enablerId)
            putString(EPHARMACY_CONSULTATION_SOURCE_ID,consultationId)
        })
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}

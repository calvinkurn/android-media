package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyPrescriptionAttachmentPageFragment
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyAttachPrescriptionActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        if (!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_MINI_CONSULTATION_PAGE, true)) {
            this.finish()
        }
        super.onCreate(savedInstanceState)
        setPageTitle()
    }

    private fun setPageTitle() {
        updateTitle(getString(R.string.epharmacy_prescription_attachment_page_title))
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        return EPharmacyPrescriptionAttachmentPageFragment.newInstance()
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}

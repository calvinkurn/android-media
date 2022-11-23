package com.tokopedia.epharmacy.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyPrescriptionAttachmentPageFragment
import com.tokopedia.epharmacy.ui.fragment.UploadPrescriptionFragment
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.utils.TrackerId.Companion.OPEN_SCREEN_ID
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import java.lang.NumberFormatException
import javax.inject.Inject

class EPharmacyAttachPrescriptionActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
    
}

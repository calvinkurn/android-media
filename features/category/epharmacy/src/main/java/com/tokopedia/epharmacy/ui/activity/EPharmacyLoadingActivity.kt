package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyLoadingFragment
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID

class EPharmacyLoadingActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var tConsultationId: String = ""

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        extractArguments()
        return EPharmacyLoadingFragment.newInstance(
            Bundle().apply {
                putString(EPHARMACY_TOKO_CONSULTATION_ID, tConsultationId)
            }
        )
    }

    private fun extractArguments() {
        intent?.data?.let {
            tConsultationId = it.getQueryParameter(EPHARMACY_TOKO_CONSULTATION_ID) ?: ""
        }
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}

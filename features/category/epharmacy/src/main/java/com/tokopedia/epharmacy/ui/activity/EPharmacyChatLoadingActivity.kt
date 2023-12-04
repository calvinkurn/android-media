package com.tokopedia.epharmacy.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyChatLoadingFragment
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.IS_SINGLE_CONSUL_FLOW
import com.tokopedia.kotlin.extensions.view.toLongOrZero

class EPharmacyChatLoadingActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var tConsultationId = 0L
    private var singleConsulFlow = false

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
        initView()
        return EPharmacyChatLoadingFragment.newInstance(
            Bundle().apply {
                putLong(EPHARMACY_TOKO_CONSULTATION_ID, tConsultationId)
                putBoolean(IS_SINGLE_CONSUL_FLOW, singleConsulFlow)
            }
        )
    }

    private fun initView() {
        updateTitle(" ")
    }

    private fun extractArguments() {
        val pathSegments = Uri.parse(intent.data?.path.orEmpty()).pathSegments
        tConsultationId = if (pathSegments.size > 1) pathSegments[1].toLongOrZero() else 0L
        intent?.data?.let { uri ->
            singleConsulFlow = uri.getBooleanQueryParameter(IS_SINGLE_CONSUL_FLOW, false)
        }
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}

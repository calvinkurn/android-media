package com.tokopedia.epharmacy.ui.activity

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyComponentBottomSheet
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero

class EPharmacyComponentActivity : BaseActivity(), HasComponent<EPharmacyComponent> {

    private var componentName = String.EMPTY
    private var tConsultationId = 0L

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    private val binding: EpharmacyMiniConsultationTransparentActivityBinding by lazy {
        EpharmacyMiniConsultationTransparentActivityBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adjustOrientation()
        extractParameters()
        openBottomSheet()
    }

    private fun openBottomSheet() {
        EPharmacyComponentBottomSheet.newInstance(
            Bundle().apply {
                    putString(EPharmacyComponentBottomSheet.COMPONENT_NAME, componentName)
                    putLong(EPHARMACY_TOKO_CONSULTATION_ID, tConsultationId)
            }
        ).show(supportFragmentManager, EPharmacyComponentBottomSheet::class.simpleName)
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path.orEmpty()).pathSegments
        componentName = if (pathSegments.size > 1) pathSegments[1].orEmpty() else String.EMPTY
        intent?.data?.let { uri ->
            tConsultationId = uri.getQueryParameter(EPHARMACY_TOKO_CONSULTATION_ID).toLongOrZero()
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()

}

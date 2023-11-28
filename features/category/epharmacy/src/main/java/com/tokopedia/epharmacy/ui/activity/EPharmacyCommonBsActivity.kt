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
import com.tokopedia.epharmacy.ui.bottomsheet.EPharmacyCommonBottomSheet
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_IDS
import com.tokopedia.kotlin.extensions.view.EMPTY

class EPharmacyCommonBsActivity : BaseActivity(), HasComponent<EPharmacyComponent> {

    private var componentName = String.EMPTY
    private var tConsultationIds = listOf<String>()

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
        EPharmacyCommonBottomSheet.newInstance(
            Bundle().apply {
                    putString(EPharmacyCommonBottomSheet.COMPONENT_NAME, componentName)
                    putStringArrayList(EPHARMACY_TOKO_CONSULTATION_IDS, ArrayList(tConsultationIds))
            }
        ).show(supportFragmentManager, EPharmacyCommonBottomSheet::class.simpleName)
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path.orEmpty()).pathSegments
        componentName = if (pathSegments.size > 1) pathSegments[1].orEmpty() else String.EMPTY
        intent?.data?.let { uri ->
            tConsultationIds = uri.getQueryParameters(EPHARMACY_TOKO_CONSULTATION_IDS)
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

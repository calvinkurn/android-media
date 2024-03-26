package com.tokopedia.epharmacy.ui.activity

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.databinding.EpharmacyMiniConsultationTransparentActivityBinding
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.utils.openDocument
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class EPharmacyViewPrescriptionActivity : BaseActivity(), HasComponent<EPharmacyComponent> {

    private var tConsultationId = String.EMPTY

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    private val binding: EpharmacyMiniConsultationTransparentActivityBinding by lazy {
        EpharmacyMiniConsultationTransparentActivityBinding.inflate(LayoutInflater.from(this))
    }

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val ePharmacyPrescriptionAttachmentViewModel: EPharmacyPrescriptionAttachmentViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[EPharmacyPrescriptionAttachmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adjustOrientation()
        extractParameters()
        setUpObservers()
        getData()
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path.orEmpty()).pathSegments
        tConsultationId = if (pathSegments.size > 1) pathSegments[1].orEmpty() else String.EMPTY
    }

    private fun setUpObservers() {
        observerConsultationDetails()
    }

    private fun getData() {
        binding.epLoader.show()
        ePharmacyPrescriptionAttachmentViewModel.getConsultationDetails(tConsultationId)
    }

    private fun observerConsultationDetails() {
        ePharmacyPrescriptionAttachmentViewModel.consultationDetails.observe(this) { consultationDetails ->
            when (consultationDetails) {
                is Success -> {
                    consultationDetails.data.epharmacyConsultationDetailsData?.consultationData?.prescription?.firstOrNull()?.documentUrl?.let { url ->
                        openDocument(url)
                    }
                    removeShimmerAndExit()
                }
                is Fail -> {
                    removeShimmerAndExit()
                }
            }
        }
    }

    private fun removeShimmerAndExit() {
        binding.epLoader.show()
        finish()
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

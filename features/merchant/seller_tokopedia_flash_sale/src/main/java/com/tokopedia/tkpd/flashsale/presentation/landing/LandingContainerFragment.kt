package com.tokopedia.tkpd.flashsale.presentation.landing

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentLandingContainerBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.detail.bottomsheet.CampaignDetailBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.TimelineStepModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class LandingContainerFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = LandingContainerFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<StfsFragmentLandingContainerBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(LandingContainerViewModel::class.java) }

    override fun getScreenName(): String = LandingContainerFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentLandingContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        val timelineSteps = listOf(
            TimelineStepModel("Periode Pendaftaran", "7 - 16 Jul 2022", true, true),
            TimelineStepModel("Tambah Produk", "7 - 16 Jul 2022", true, true),
            TimelineStepModel("Proses Seleksi", "7 - 16 Jul 2022", false, true),
            TimelineStepModel("Promosi Aktif", "7 - 16 Jul 2022"),
            TimelineStepModel("Selesai")
        )
        val productCriterias = listOf(
            ProductCriteriaModel("Olahraga", "Belum ada produkmu yang sesuai"),
            ProductCriteriaModel("Suplemen Diet, + 10 lainnya", "Belum ada produkmu yang sesuai", categories = listOf("Robot", "Mesin", "Alien", "Tengkorak")),
            ProductCriteriaModel("Olahraga, Games", "15 produkmu sesuai")
        )
        val model = CampaignDetailBottomSheetModel(timelineSteps, productCriterias)
        val bottomSheet = CampaignDetailBottomSheet.newInstance(model)
        bottomSheet.show(childFragmentManager, "")
    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
        }
    }
}
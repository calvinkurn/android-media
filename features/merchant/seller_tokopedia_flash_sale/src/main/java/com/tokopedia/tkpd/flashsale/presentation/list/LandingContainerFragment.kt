package com.tokopedia.tkpd.flashsale.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.entity.SingleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.entity.MultipleSelectionItem
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentLandingContainerBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
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

        observeUiEvent()
        observeUiState()

        viewModel.getTabsMetaData()
        viewModel.submitProduct()
        viewModel.deleteProduct()
        viewModel.getReservedProduct()
        viewModel.getFlashSaleList()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event -> handleEvent(event) }
        }
    }

    private fun handleEvent(event: LandingContainerViewModel.UiEvent) {
        when (event) {
            is LandingContainerViewModel.UiEvent.FetchTabMetaError -> {

            }
        }
    }

    private fun handleUiState(uiState: LandingContainerViewModel.UiState) {
        val tabsMeta = uiState.tabsMetadata
        //displaySingleSelectionBottomSheet()
        displayMultipleSelectionBottomSheet()
    }

    private fun displaySingleSelectionBottomSheet() {

        val selectedItemId = "all-product"
        val singleSelectionItems = arrayListOf(
            SingleSelectionItem("criteria", "Memenuhi kriteria"),
            SingleSelectionItem("all-product", "Semua Produk"),
        )
        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedItemId, singleSelectionItems)
        bottomSheet.apply {
            setBottomSheetTitle("Mau cari produk apa?")
            setBottomSheetButtonTitle("Terapkan")
            setOnApplyButtonClick { selectedItem ->
                val id = selectedItem.id
            }
        }
        val selectedItem = bottomSheet.getSelectedItem()
        bottomSheet.show(childFragmentManager, tag)
    }

    private fun displayMultipleSelectionBottomSheet() {
        val selectedItemIds = arrayListOf("clothes", "toys")
        val multipleSelectionItems = arrayListOf(
            MultipleSelectionItem("clothes", "Baju"),
            MultipleSelectionItem("pants", "Celana"),
            MultipleSelectionItem("toys", "Mainan Anak"),
            MultipleSelectionItem("food", "Makanan"),
        )
        val bottomSheet = MultipleSelectionBottomSheet.newInstance(selectedItemIds, multipleSelectionItems)
        bottomSheet.apply {
            setBottomSheetTitle("Mau cari kategori apa?")
            setBottomSheetButtonTitle("Terapkan")
            setOnApplyButtonClick { selectedItems ->
                val items = selectedItems
            }
        }
        val selectedItems = bottomSheet.getAllSelectedItems()
        bottomSheet.show(childFragmentManager, tag)
    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
        }
    }
}
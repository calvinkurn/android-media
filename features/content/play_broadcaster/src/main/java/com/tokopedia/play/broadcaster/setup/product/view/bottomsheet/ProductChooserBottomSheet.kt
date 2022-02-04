package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductChooserBinding
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserAction
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseProductListMap
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.EtalaseChipsViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductListViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SortChipsViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductChooserBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dialogCustomizer: PlayBroadcastDialogCustomizer,
) : BottomSheetUnify(), ProductSortBottomSheet.Listener {

    private lateinit var viewModel: PlayBroProductSetupViewModel

    private var _binding: BottomSheetPlayBroProductChooserBinding? = null
    private val binding: BottomSheetPlayBroProductChooserBinding
        get() = _binding!!

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Any>() },
    )

    private val productListView by viewComponent { ProductListViewComponent(binding.rvProducts) }
    private val sortChipsView by viewComponent(isEagerInit = true) {
        SortChipsViewComponent(binding.chipsSort, eventBus)
    }
    private val etalaseChipsView by viewComponent {
        EtalaseChipsViewComponent(binding.chipsEtalase, eventBus)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory)
            .get(PlayBroProductSetupViewModel::class.java)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductSortBottomSheet -> childFragment.setListener(this)
        }
    }

    override fun onSortChosen(bottomSheet: ProductSortBottomSheet, item: SortUiModel) {
        viewModel.submitAction(PlayBroProductChooserAction.SetSort(item))
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroProductChooserBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.85f).toInt()
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderProductList(prevState?.focusedProductList, state.focusedProductList)
                renderSortChips(prevState?.sort, state.sort, state.campaignAndEtalase)
                renderEtalaseChips(prevState?.campaignAndEtalase, state.campaignAndEtalase)
                renderBottomSheetTitle(state.selectedProductList)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.subscribe().collect {
                when (it) {
                    is SortChipsViewComponent.Event -> handleSortChipsEvent(it)
                    is EtalaseChipsViewComponent.Event -> handleEtalaseChipsEvent(it)
                }
            }
        }
    }

    private fun renderProductList(
        prevProductList: List<ProductUiModel>?,
        productList: List<ProductUiModel>,
    ) {
        if (prevProductList == productList) return

        productListView.setProductList(productList)
    }

    private fun renderSortChips(
        prevSort: SortUiModel?,
        sort: SortUiModel?,
        campaignAndEtalase: CampaignAndEtalaseUiModel,
    ) {
        if (prevSort?.id != sort?.id) {
            sortChipsView.setText(sort?.text)
        }

        if (campaignAndEtalase.selected !is SelectedEtalaseModel.Campaign) sortChipsView.show()
        else sortChipsView.hide()
    }

    private fun renderEtalaseChips(
        prevModel: CampaignAndEtalaseUiModel?,
        model: CampaignAndEtalaseUiModel
    ) {
        if (prevModel == model) return

        val selectedTitle = when (model.selected) {
            is SelectedEtalaseModel.Campaign -> model.selected.campaign.title
            is SelectedEtalaseModel.Etalase -> model.selected.etalase.title
            SelectedEtalaseModel.None ->
                if (model.campaignList.isNotEmpty() && model.etalaseList.isNotEmpty()) {
                    getString(R.string.play_bro_campaign_and_etalase)
                } else if (model.campaignList.isNotEmpty()) {
                    getString(R.string.play_bro_campaign)
                } else {
                    getString(R.string.play_bro_etalase)
                }
        }

        etalaseChipsView.setState(selectedTitle, model.selected != SelectedEtalaseModel.None)
    }

    private fun renderBottomSheetTitle(
        selectedProductList: EtalaseProductListMap,
    ) {
        setTitle(
            getString(
                R.string.play_bro_selected_product_title,
                selectedProductList.values.sumOf { it.size },
                30
            )
        )
    }

    /**
     * View Event
     */
    private fun handleSortChipsEvent(event: SortChipsViewComponent.Event) {
        when (event) {
            SortChipsViewComponent.Event.OnClicked -> {
                ProductSortBottomSheet.getFragment(
                    childFragmentManager,
                    requireActivity().classLoader,
                    selectedId = viewModel.uiState.value.sort?.id
                ).show(childFragmentManager)
            }
        }
    }

    private fun handleEtalaseChipsEvent(event: EtalaseChipsViewComponent.Event) {
        when (event) {
            EtalaseChipsViewComponent.Event.OnClicked -> {
                (parentFragment as? ProductSetupFragment)
                    ?.openCampaignAndEtalaseList()
            }
        }
    }

    companion object {
        private const val TAG = "PlayBroProductChooserBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ProductChooserBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductChooserBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    ProductChooserBottomSheet::class.java.name
                ) as ProductChooserBottomSheet
            }
        }
    }
}
package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductChooserBinding
import com.tokopedia.play.broadcaster.setup.product.model.CampaignAndEtalaseUiModel
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSaveStateUiModel
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupConfig
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.EtalaseChipsViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductErrorViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductListViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SaveButtonViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SearchBarViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SortChipsViewComponent
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductChooserBottomSheet @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val dialogCustomizer: PlayBroadcastDialogCustomizer,
) : BaseProductSetupBottomSheet(), ProductSortBottomSheet.Listener {

    private var _binding: BottomSheetPlayBroProductChooserBinding? = null
    private val binding: BottomSheetPlayBroProductChooserBinding
        get() = _binding!!

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Any>() },
    )

    private val productListView by viewComponent {
        ProductListViewComponent(binding.rvProducts, eventBus)
    }
    private val sortChipsView by viewComponent(isEagerInit = true) {
        SortChipsViewComponent(binding.chipsSort, eventBus)
    }
    private val etalaseChipsView by viewComponent {
        EtalaseChipsViewComponent(binding.chipsEtalase, eventBus)
    }
    private val searchBarView by viewComponent {
        SearchBarViewComponent(binding.searchBar, eventBus)
    }
    private val saveButtonView by viewComponent {
        SaveButtonViewComponent(binding.btnNext, eventBus)
    }
    private val productErrorView by viewComponent {
        ProductErrorViewComponent(binding.errorProduct, eventBus)
    }
    private val exitConfirmationDialog by lifecycleBound(
        creator = {
            DialogUnify(
                it.requireContext(),
                DialogUnify.HORIZONTAL_ACTION,
                DialogUnify.NO_IMAGE,
            ).apply {
                setTitle(it.getString(R.string.play_bro_product_chooser_exit_dialog_title))
                setDescription(it.getString(R.string.play_bro_product_chooser_exit_dialog_desc))
                setPrimaryCTAText(it.getString(R.string.play_batal))
                setSecondaryCTAText(it.getString(R.string.play_broadcast_exit))

                setPrimaryCTAClickListener {
                    this@apply.dismiss()
                }

                setSecondaryCTAClickListener {
                    this@apply.dismiss()
                    mListener?.onSetupCancelled(this@ProductChooserBottomSheet)
                }
            }
        },
        onLifecycle = whenLifecycle {
            onDestroy { it.dismiss() }
        }
    )

    private var mListener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun cancel() {
                closeBottomSheet()
            }
        }.apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        mListener = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductSortBottomSheet -> childFragment.setListener(this)
        }
    }

    override fun onSortChosen(bottomSheet: ProductSortBottomSheet, item: SortUiModel) {
        viewModel.submitAction(ProductSetupAction.SetSort(item))
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroProductChooserBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        clearContentPadding = true
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.85f).toInt()
        }

        setCloseClickListener {
            closeBottomSheet()
        }
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderProductList(
                    prevState?.focusedProductList,
                    state.focusedProductList,
                    prevState?.selectedProductSectionList,
                    state.selectedProductSectionList,
                )
                renderSortChips(prevState?.loadParam, state.loadParam, state.campaignAndEtalase)
                renderEtalaseChips(prevState?.loadParam, state.loadParam, state.campaignAndEtalase)
                renderSearchBar(
                    state.loadParam,
                    state.focusedProductList,
                    state.campaignAndEtalase,
                    prevState?.config,
                    state.config
                )
                renderBottomSheetTitle(
                    prevState?.selectedProductSectionList,
                    state.selectedProductSectionList,
                    state.config
                )
                renderSaveButton(
                    state.selectedProductSectionList,
                    state.saveState
                )
                renderProductError(state.campaignAndEtalase, state.focusedProductList)
                renderChipsContainer(state.campaignAndEtalase, state.focusedProductList)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.subscribe().collect {
                when (it) {
                    is SortChipsViewComponent.Event -> handleSortChipsEvent(it)
                    is EtalaseChipsViewComponent.Event -> handleEtalaseChipsEvent(it)
                    is ProductListViewComponent.Event -> handleProductListEvent(it)
                    is SearchBarViewComponent.Event -> handleSearchBarEvent(it)
                    is SaveButtonViewComponent.Event -> handleSaveButtonEvent(it)
                    is ProductErrorViewComponent.Event -> handleProductErrorEvent(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    PlayBroProductChooserEvent.SaveProductSuccess -> {
                        mListener?.onSetupSuccess(this@ProductChooserBottomSheet)
                    }
                    is PlayBroProductChooserEvent.ShowError -> {
                        //TODO("Show Error")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderProductList(
        prevProductListPaging: ProductListPaging?,
        productListPaging: ProductListPaging,
        prevSelectedProducts: List<ProductTagSectionUiModel>?,
        selectedProducts: List<ProductTagSectionUiModel>,
    ) {
        if (prevProductListPaging == productListPaging && prevSelectedProducts == selectedProducts) return

        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            productListView.setProductList(
                productList = productListPaging.productList,
                selectedList = selectedProducts.flatMap { it.products },
                showLoading = productListPaging.resultState !is PageResultState.Success ||
                        productListPaging.resultState.hasNextPage,
            )
        }
    }

    private fun renderSortChips(
        prevParam: ProductListPaging.Param?,
        param: ProductListPaging.Param,
        campaignAndEtalase: CampaignAndEtalaseUiModel,
    ) {
        if (prevParam?.sort?.id != param.sort?.id) {
            sortChipsView.setText(param.sort?.text)
        }

        if (param.etalase !is SelectedEtalaseModel.Campaign) sortChipsView.show()
        else sortChipsView.hide()
    }

    private fun renderEtalaseChips(
        prevParam: ProductListPaging.Param?,
        param: ProductListPaging.Param,
        campaignAndEtalase: CampaignAndEtalaseUiModel,
    ) {
        if (prevParam == param) return

        val selectedTitle = when (param.etalase) {
            is SelectedEtalaseModel.Campaign -> param.etalase.campaign.title
            is SelectedEtalaseModel.Etalase -> param.etalase.etalase.title
            SelectedEtalaseModel.None ->
                if (campaignAndEtalase.campaignList.isNotEmpty() &&
                    campaignAndEtalase.etalaseList.isNotEmpty()) {
                    getString(R.string.play_bro_campaign_and_etalase)
                } else if (campaignAndEtalase.campaignList.isNotEmpty()) {
                    getString(R.string.play_bro_campaign)
                } else {
                    getString(R.string.play_bro_etalase)
                }
        }

        etalaseChipsView.setState(selectedTitle, param.etalase != SelectedEtalaseModel.None)
    }

    private fun renderSearchBar(
        param: ProductListPaging.Param,
        productList: ProductListPaging,
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        prevConfig: ProductSetupConfig?,
        config: ProductSetupConfig,
    ) {
        if (param.etalase is SelectedEtalaseModel.Campaign ||
            hasNoProducts(campaignAndEtalase, productList)) searchBarView.hide()
        else searchBarView.show()

        if (prevConfig?.shopName != config.shopName) {
            searchBarView.setPlaceholder(
                getString(R.string.play_etalase_search_hint, config.shopName)
            )
        }
    }

    private fun renderBottomSheetTitle(
        prevSelectedProducts: List<ProductTagSectionUiModel>?,
        selectedProducts: List<ProductTagSectionUiModel>,
        config: ProductSetupConfig,
    ) {
        if (prevSelectedProducts == selectedProducts) return

        setTitle(
            getString(
                R.string.play_bro_selected_product_title,
                selectedProducts.sumOf { it.products.size },
                config.maxProduct,
            )
        )
    }

    private fun renderSaveButton(
        selectedProducts: List<ProductTagSectionUiModel>,
        saveState: ProductSaveStateUiModel
    ) {
        val isProductChanged = selectedProducts != viewModel.initialProductSectionList
        saveButtonView.setState(
            isLoading = saveState.isLoading,
            isEnabled = saveState.canSave && isProductChanged
        )
    }

    private fun renderProductError(
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        productList: ProductListPaging,
    ) {
        if (isProductEmpty(productList)) {
            if (isEtalaseEmpty(campaignAndEtalase)) productErrorView.setHasNoProduct()
            else productErrorView.setProductNotFound()

            productErrorView.show()
        } else {
            productErrorView.hide()
        }
    }

    private fun renderChipsContainer(
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        productList: ProductListPaging,
    ) {
        binding.containerChips.visibility =
            if (hasNoProducts(campaignAndEtalase, productList)) View.GONE
            else View.VISIBLE
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
                    selectedId = viewModel.uiState.value.loadParam.sort?.id
                ).show(childFragmentManager)
            }
        }
    }

    private fun handleEtalaseChipsEvent(event: EtalaseChipsViewComponent.Event) {
        when (event) {
            EtalaseChipsViewComponent.Event.OnClicked -> {
                mListener?.openCampaignAndEtalaseList(this)
            }
        }
    }

    private fun handleProductListEvent(event: ProductListViewComponent.Event) {
        when (event) {
            is ProductListViewComponent.Event.OnSelected -> {
                viewModel.submitAction(ProductSetupAction.SelectProduct(event.product))
            }
            is ProductListViewComponent.Event.OnLoadMore -> {
                viewModel.submitAction(
                    ProductSetupAction.LoadProductList(keyword = "")
                )
            }
        }
    }

    private fun handleSearchBarEvent(event: SearchBarViewComponent.Event) {
        when (event) {
            is SearchBarViewComponent.Event.OnSearched -> {
                viewModel.submitAction(ProductSetupAction.SearchProduct(event.keyword))
            }
        }
    }

    private fun handleSaveButtonEvent(event: SaveButtonViewComponent.Event) {
        when (event) {
            SaveButtonViewComponent.Event.OnClicked -> {
                viewModel.submitAction(ProductSetupAction.SaveProducts)
            }
        }
    }

    private fun handleProductErrorEvent(event: ProductErrorViewComponent.Event) {
        when (event) {
            ProductErrorViewComponent.Event.AddProductClicked -> {
                RouteManager.route(context, ApplinkConst.PRODUCT_ADD)
            }
        }
    }

    private fun closeBottomSheet() {
        if (saveButtonView.isEnabled()) exitConfirmationDialog.show()
        else mListener?.onSetupCancelled(this@ProductChooserBottomSheet)
    }

    /**
     * Util
     */
    private fun isEtalaseEmpty(campaignAndEtalase: CampaignAndEtalaseUiModel): Boolean {
        return campaignAndEtalase.state == NetworkState.Success &&
                campaignAndEtalase.campaignList.isEmpty() &&
                campaignAndEtalase.etalaseList.isEmpty()
    }

    private fun isProductEmpty(productList: ProductListPaging): Boolean {
        return productList.resultState is PageResultState.Success &&
                productList.productList.isEmpty()
    }

    private fun hasNoProducts(
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        productList: ProductListPaging,
    ): Boolean {
        return isEtalaseEmpty(campaignAndEtalase) && isProductEmpty(productList)
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

    interface Listener {
        fun onSetupCancelled(bottomSheet: ProductChooserBottomSheet)
        fun onSetupSuccess(bottomSheet: ProductChooserBottomSheet)

        fun openCampaignAndEtalaseList(bottomSheet: ProductChooserBottomSheet)
    }
}
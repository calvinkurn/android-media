package com.tokopedia.content.product.picker.seller.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.content.common.util.throwable.isNetworkError
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.databinding.BottomSheetSellerProductChooserBinding
import com.tokopedia.content.product.picker.seller.analytic.manager.ProductChooserAnalyticManager
import com.tokopedia.content.product.picker.seller.model.ProductListPaging
import com.tokopedia.content.product.picker.seller.model.etalase.SelectedEtalaseModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.result.ContentProductPickerNetworkResult
import com.tokopedia.content.product.picker.seller.model.result.PageResultState
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel
import com.tokopedia.content.product.picker.seller.model.uimodel.CampaignAndEtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductChooserEvent
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSaveStateUiModel
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupConfig
import com.tokopedia.content.product.picker.seller.util.getCustomErrorMessage
import com.tokopedia.content.product.picker.seller.view.viewcomponent.EtalaseChipsViewComponent
import com.tokopedia.content.product.picker.seller.view.viewcomponent.ProductErrorViewComponent
import com.tokopedia.content.product.picker.seller.view.viewcomponent.ProductListViewComponent
import com.tokopedia.content.product.picker.seller.view.viewcomponent.SaveButtonViewComponent
import com.tokopedia.content.product.picker.seller.view.viewcomponent.SearchBarViewComponent
import com.tokopedia.content.product.picker.seller.view.viewcomponent.SortChipsViewComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductChooserBottomSheet @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val dialogCustomizer: ContentDialogCustomizer,
    private val analyticManager: ProductChooserAnalyticManager,
    private val router: Router
) : BaseProductSetupBottomSheet(), ProductSortBottomSheet.Listener {

    private var _binding: BottomSheetSellerProductChooserBinding? = null
    private val binding: BottomSheetSellerProductChooserBinding
        get() = _binding!!

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Any>() }
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
                DialogUnify.NO_IMAGE
            ).apply {
                setTitle(it.getString(R.string.product_chooser_exit_dialog_title))
                setDescription(it.getString(R.string.product_chooser_exit_dialog_desc))
                setPrimaryCTAText(it.getString(R.string.content_product_picker_cancel))
                setSecondaryCTAText(it.getString(R.string.content_product_picker_exit))

                setPrimaryCTAClickListener {
                    eventBus.emit(Event.ExitDialogCancel)
                }

                setSecondaryCTAClickListener {
                    eventBus.emit(Event.ExitDialogConfirm)
                }
            }
        },
        onLifecycle = whenLifecycle {
            onDestroy { it.dismiss() }
        }
    )
    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    // TODO("Need to find a better way to handle this")
    private var isSelectedProductsChanged = false

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

    override fun onStart() {
        super.onStart()
        eventBus.emit(Event.ViewBottomSheet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductSortBottomSheet -> {
                childFragment.setListener(this)
            }
        }
    }

    override fun onSortChosen(bottomSheet: ProductSortBottomSheet, item: SortUiModel) {
        eventBus.emit(Event.SortChosen(item))
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetSellerProductChooserBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        clearContentPadding = true
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * SHEET_HEIGHT_PERCENT).toInt()
        }

        setCloseClickListener {
            closeBottomSheet()
        }

        searchBarView.setKeyword(viewModel.searchKeyword)

        viewModel.submitAction(ProductSetupAction.SyncSelectedProduct)
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderProductList(
                    prevState?.focusedProductList,
                    state.focusedProductList,
                    prevState?.selectedProductList,
                    state.selectedProductList
                )
                renderSortChips(prevState?.loadParam, state.loadParam)
                renderEtalaseChips(
                    prevState?.loadParam,
                    state.loadParam,
                    prevState?.campaignAndEtalase,
                    state.campaignAndEtalase
                )
                renderSearchBar(
                    state.loadParam,
                    state.focusedProductList,
                    state.campaignAndEtalase,
                    prevState?.config,
                    state.config
                )
                renderBottomSheetTitle(
                    prevState?.selectedProductList,
                    state.selectedProductList,
                    prevState?.config,
                    state.config
                )
                renderSaveButton(state.saveState)
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
                    is Event -> handleBottomSheetEvent(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    ProductChooserEvent.SaveProductSuccess -> {
                        mListener?.onSetupSuccess(this@ProductChooserBottomSheet)
                    }
                    is ProductChooserEvent.ShowError -> {
                        toaster.showError(
                            err = it.error,
                            customErrMessage = requireContext().getCustomErrorMessage(
                                throwable = it.error,
                                defaultMessage = getString(R.string.product_chooser_error_save)
                            )
                        )
                    }
                    else -> {}
                }
            }
        }

        analyticManager.observe(
            viewLifecycleOwner.lifecycleScope,
            eventBus,
            viewModel.uiState
        )
    }

    private fun renderProductList(
        prevProductListPaging: ProductListPaging?,
        productListPaging: ProductListPaging,
        prevSelectedProducts: List<ProductUiModel>?,
        selectedProducts: List<ProductUiModel>
    ) {
        if (prevProductListPaging == productListPaging &&
            prevSelectedProducts == selectedProducts
        ) {
            return
        }

        when {
            productListPaging.resultState is PageResultState.Fail ||
                isProductEmpty(productListPaging) -> {
                productListView.hide()
            }
            else -> productListView.show()
        }

        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            productListView.setProductList(
                productList = productListPaging.productList,
                selectedList = selectedProducts,
                showLoading = productListPaging.resultState == PageResultState.Loading ||
                    (
                        productListPaging.resultState is PageResultState.Success &&
                            productListPaging.resultState.hasNextPage
                        )
            )
        }
    }

    private fun renderSortChips(
        prevParam: ProductListPaging.Param?,
        param: ProductListPaging.Param
    ) {
        if (prevParam?.sort != param.sort) {
            sortChipsView.setText(param.sort?.text)
        }

        if (param.etalase !is SelectedEtalaseModel.Campaign) {
            sortChipsView.show()
        } else {
            sortChipsView.hide()
        }
    }

    private fun renderEtalaseChips(
        prevParam: ProductListPaging.Param?,
        param: ProductListPaging.Param,
        prevCampaignAndEtalase: CampaignAndEtalaseUiModel?,
        campaignAndEtalase: CampaignAndEtalaseUiModel
    ) {
        if (prevParam == param && prevCampaignAndEtalase == campaignAndEtalase) return

        val selectedTitle = when (param.etalase) {
            is SelectedEtalaseModel.Campaign -> param.etalase.campaign.title
            is SelectedEtalaseModel.Etalase -> param.etalase.etalase.title
            SelectedEtalaseModel.None ->
                if (campaignAndEtalase.campaignList.isNotEmpty() &&
                    campaignAndEtalase.etalaseList.isNotEmpty()
                ) {
                    getString(R.string.campaign_and_etalase)
                } else if (campaignAndEtalase.campaignList.isNotEmpty()) {
                    getString(R.string.campaign)
                } else {
                    getString(R.string.etalase)
                }
        }

        etalaseChipsView.setState(selectedTitle, param.etalase != SelectedEtalaseModel.None)
    }

    private fun renderSearchBar(
        param: ProductListPaging.Param,
        productList: ProductListPaging,
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        prevConfig: ProductSetupConfig?,
        config: ProductSetupConfig
    ) {
        if (param.etalase is SelectedEtalaseModel.Campaign ||
            hasNoProducts(campaignAndEtalase, productList)
        ) {
            searchBarView.hide()
        } else {
            searchBarView.show()
        }

        if (prevConfig?.shopName != config.shopName) {
            searchBarView.setPlaceholder(
                getString(R.string.product_etalase_search_hint, config.shopName)
            )
        }
    }

    private fun renderBottomSheetTitle(
        prevSelectedProducts: List<ProductUiModel>?,
        selectedProducts: List<ProductUiModel>,
        prevConfig: ProductSetupConfig?,
        config: ProductSetupConfig
    ) {
        if (prevSelectedProducts == selectedProducts &&
            prevConfig?.maxProduct == config.maxProduct
        ) {
            return
        }

        setTitle(
            getString(
                R.string.selected_product_title,
                selectedProducts.size,
                config.maxProduct
            )
        )
    }

    private fun renderSaveButton(
        saveState: ProductSaveStateUiModel
    ) {
        saveButtonView.setState(
            isLoading = saveState.isLoading,
            isEnabled = saveState.canSave && isSelectedProductsChanged
        )
    }

    private fun renderProductError(
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        productList: ProductListPaging
    ) {
        when {
            productList.resultState is PageResultState.Fail -> {
                val error = productList.resultState.error
                if (error.isNetworkError) {
                    productErrorView.setConnectionError()
                } else {
                    productErrorView.setServerError()
                }

                productErrorView.show()
            }
            isProductEmpty(productList) -> {
                if (isEtalaseEmpty(campaignAndEtalase)) {
                    productErrorView.setHasNoProduct()
                } else {
                    productErrorView.setProductNotFound()
                }

                productErrorView.show()
            }
            else -> {
                productErrorView.hide()
            }
        }
    }

    private fun renderChipsContainer(
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        productList: ProductListPaging
    ) {
        binding.containerChips.visibility =
            if (hasNoProducts(campaignAndEtalase, productList)) {
                View.GONE
            } else {
                View.VISIBLE
            }
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
                    selected = viewModel.uiState.value.loadParam.sort
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
                isSelectedProductsChanged = true
                viewModel.submitAction(ProductSetupAction.ToggleSelectProduct(event.product))
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
            else -> {
                // no-op
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
                router.route(context, ApplinkConst.PRODUCT_ADD)
            }
            ProductErrorViewComponent.Event.RetryClicked -> {
                viewModel.submitAction(ProductSetupAction.RetryFetchProducts)
            }
        }
    }

    private fun handleBottomSheetEvent(event: Event) {
        when (event) {
            Event.ExitDialogCancel -> exitConfirmationDialog.dismiss()
            Event.ExitDialogConfirm -> {
                exitConfirmationDialog.dismiss()
                mListener?.onSetupCancelled(this@ProductChooserBottomSheet)
            }
            Event.CloseClicked -> {
                if (saveButtonView.isEnabled()) {
                    exitConfirmationDialog.show()
                } else {
                    mListener?.onSetupCancelled(this@ProductChooserBottomSheet)
                }
            }
            is Event.SortChosen -> {
                viewModel.submitAction(ProductSetupAction.SetSort(event.sort))
            }
            else -> {
                // no-op
            }
        }
    }

    private fun closeBottomSheet() {
        if (viewModel.uiState.value.saveState.isLoading) return
        eventBus.emit(Event.CloseClicked)
    }

    /**
     * Util
     */
    private fun isEtalaseEmpty(campaignAndEtalase: CampaignAndEtalaseUiModel): Boolean {
        return campaignAndEtalase.state == ContentProductPickerNetworkResult.Success &&
            campaignAndEtalase.campaignList.isEmpty() &&
            campaignAndEtalase.etalaseList.isEmpty()
    }

    private fun isProductEmpty(productList: ProductListPaging): Boolean {
        return productList.resultState is PageResultState.Success &&
            productList.productList.isEmpty()
    }

    private fun hasNoProducts(
        campaignAndEtalase: CampaignAndEtalaseUiModel,
        productList: ProductListPaging
    ): Boolean {
        return isEtalaseEmpty(campaignAndEtalase) && isProductEmpty(productList)
    }

    companion object {
        private const val TAG = "PlayBroProductChooserBottomSheet"
        private const val SHEET_HEIGHT_PERCENT = 0.85f

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ProductChooserBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductChooserBottomSheet
            return if (oldInstance != null) {
                oldInstance
            } else {
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

    sealed class Event {
        object ViewBottomSheet : Event()
        object ExitDialogConfirm : Event()
        object ExitDialogCancel : Event()
        object CloseClicked : Event()
        data class SortChosen(val sort: SortUiModel) : Event()
    }
}

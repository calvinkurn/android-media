package com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowShoppingListAnotherOptionBinding
import com.tokopedia.tokopedianow.shoppinglist.analytic.ShoppingListAnalytic
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet.ShoppingListAnotherOptionBottomSheetAdapterTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.shoppinglist.helper.AbstractShoppingListHorizontalProductCardItemListener
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet.ShoppingListAnotherOptionBottomSheetAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListAnotherOptionBottomSheetDecoration
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListAnotherOptionBottomSheetViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowShoppingListAnotherOptionBottomSheet : BottomSheetUnify() {

    companion object {
        private const val KEY_PRODUCT_ID = "productId"

        private val TAG: String = TokoNowShoppingListAnotherOptionBottomSheet::class.java.simpleName

        fun newInstance(
            productId: String
        ): TokoNowShoppingListAnotherOptionBottomSheet {
            val bottomSheet = TokoNowShoppingListAnotherOptionBottomSheet()
            val bundle = Bundle()
            bundle.putString(KEY_PRODUCT_ID, productId)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }

    /**
     * -- lateinit variable section --
     */

    @Inject
    lateinit var viewModel: TokoNowShoppingListAnotherOptionBottomSheetViewModel

    @Inject
    lateinit var analytic: ShoppingListAnalytic

    /**
     * -- private variable section --
     */

    private val adapter by lazy {
        ShoppingListAnotherOptionBottomSheetAdapter(
            ShoppingListAnotherOptionBottomSheetAdapterTypeFactory(
                errorListener = createErrorCallback(),
                productListener = createHorizontalProductCardItemCallback()
            )
        )
    }

    private val productId
        get() = arguments?.getString(KEY_PRODUCT_ID, String.EMPTY)

    private var binding
        by autoClearedNullable<BottomsheetTokopedianowShoppingListAnotherOptionBinding>()

    private val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    /**
     * -- override function section --
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectStateFlow()
        setupRecyclerView()

        viewModel.availableProducts.clear()
        viewModel.availableProducts.addAll(availableProducts)
        viewModel.loadLayout(productId.orEmpty())
    }

    /**
     * -- private function section --
     */

    private suspend fun collectProductRecommendation() {
        viewModel.layoutState.collect { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    if (!uiState.data.isNullOrEmpty()) {
                        adapter.submitList(uiState.data)
                    }
                }
                is UiState.Error -> {
                    if (!uiState.data.isNullOrEmpty()) {
                        adapter.submitList(uiState.data)
                    }
                }
                is UiState.Success -> {
                    adapter.submitList(uiState.data)
                }
            }
        }
    }

    private suspend fun collectToasterErrorShown() {
        viewModel.toasterData.collect { data ->
            if (data?.any != null && data.any is ShoppingListHorizontalProductCardItemUiModel && data.event == ToasterModel.Event.ADD_WISHLIST) {
                showToaster(data) {
                    viewModel.addToWishlist(data.any)
                }
            }
        }
    }

    private fun initInjector() {
        DaggerShoppingListComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .shoppingListModule(ShoppingListModule(requireContext()))
            .build()
            .inject(this)
    }


    private fun initLayout() {
        binding = BottomsheetTokopedianowShoppingListAnotherOptionBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        configureBottomSheet()
    }

    private fun collectStateFlow() {
        lifecycleScope.launch {
            withStarted {
                launch { collectProductRecommendation() }
                launch { collectToasterErrorShown() }
            }
        }
    }

    private fun showToaster(
        model: ToasterModel,
        clickListener: View.OnClickListener
    ) {
        binding?.let {
            Toaster.build(
                view = it.root,
                text = model.text,
                duration = model.duration,
                type = model.type,
                actionText = model.actionText,
                clickListener = if (model.type == Toaster.TYPE_ERROR) clickListener else View.OnClickListener {  }
            ).show()
        }
    }

    private fun configureBottomSheet() {
        setTitle(getString(R.string.tokopedianow_shopping_list_another_option))
        clearContentPadding = true
        showCloseIcon = true
        isHideable = true
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@TokoNowShoppingListAnotherOptionBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ShoppingListAnotherOptionBottomSheetDecoration())
            itemAnimator = null
        }
    }

    /**
     * -- callback function section --
     */

    private fun createErrorCallback() = object : TokoNowErrorViewHolder.TokoNowErrorListener {
        override fun onClickRefresh() {
            viewModel.loadLoadingState()
            viewModel.loadLayout(productId.orEmpty())
        }
    }

    private fun createHorizontalProductCardItemCallback() = object : AbstractShoppingListHorizontalProductCardItemListener() {
        override fun onClickAddToShoppingList(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.shoppingListHorizontalProductCardAnalytic.trackClickAddToShoppingListOnProduct(product)
            viewModel.addToWishlist(product)
        }

        override fun onClickProduct(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.shoppingListHorizontalProductCardAnalytic.trackClickProduct(product)
        }

        override fun onImpressProduct(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            analytic.shoppingListHorizontalProductCardAnalytic.trackImpressProduct(product)
        }
    }

    fun show(
        fm: FragmentManager,
        availableProducts: List<ShoppingListHorizontalProductCardItemUiModel>,
        onClickCloseListener: () -> Unit
    ) {
        setOnDismissListener {
            analytic.trackClickCloseButtonOnAnotherOptionBottomSheet()
            val isDataUpdated = viewModel.availableProducts != availableProducts
            if (isDataUpdated) onClickCloseListener.invoke()
            dismiss()
        }
        show(fm, TAG)
        this.availableProducts.clear()
        this.availableProducts.addAll(availableProducts)
    }
}

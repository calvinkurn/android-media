package com.tokopedia.tokopedianow.shoppinglist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.viewholder.TokoNowErrorViewHolder
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowShoppingListAnotherOptionBinding
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet.ShoppingListAnotherOptionBottomSheetAdapterTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.tokopedianow.shoppinglist.di.component.DaggerShoppingListComponent
import com.tokopedia.tokopedianow.shoppinglist.di.module.ShoppingListModule
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet.ShoppingListAnotherOptionBottomSheetAdapter
import com.tokopedia.tokopedianow.shoppinglist.presentation.decoration.ShoppingListAnotherOptionBottomSheetDecoration
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListAnotherOptionBottomSheetViewModel
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
        collectProductRecommendation()

        setupRecyclerView()

        viewModel.availableProducts.clear()
        viewModel.availableProducts.addAll(availableProducts)
        viewModel.loadLayout(String.EMPTY)
    }

    /**
     * -- private function section --
     */

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

    private fun collectProductRecommendation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
        }
    }

    private fun configureBottomSheet() {
        setTitle(getString(R.string.tokopedianow_shopping_list_another_option))
        clearContentPadding = true
        showCloseIcon = true
        isHideable = true
        setCloseClickListener {
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = this@TokoNowShoppingListAnotherOptionBottomSheet.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ShoppingListAnotherOptionBottomSheetDecoration())
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

    private fun createHorizontalProductCardItemCallback() = object : ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener {
        override fun onSelectCheckbox(
            productId: String,
            isSelected: Boolean
        ) {
        }

        override fun onClickOtherOptions(
            productId: String
        ) {

        }

        override fun onClickDeleteIcon(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {

        }

        override fun onClickAddToShoppingList(
            product: ShoppingListHorizontalProductCardItemUiModel
        ) {
            viewModel.addToWishlist(product)
        }
    }

    fun show(
        fm: FragmentManager,
        availableProducts: List<ShoppingListHorizontalProductCardItemUiModel>
    ) {
        show(fm, TAG)
        this.availableProducts.clear()
        this.availableProducts.addAll(availableProducts)
    }
}

package com.tokopedia.tokopedianow.category.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryNavigationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryShowcaseItemCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryTitleCallback
import com.tokopedia.tokopedianow.category.presentation.callback.CategoryProductRecommendationCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowCategoryMenuCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowChooseAddressWidgetCallback
import com.tokopedia.tokopedianow.category.presentation.callback.TokoNowViewCallback
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowCategoryMainFragment: TokoNowCategoryBaseFragment() {

    companion object {
        fun newInstance(): TokoNowCategoryMainFragment {
            return TokoNowCategoryMainFragment()
        }
    }

    @Inject
    lateinit var viewModel: TokoNowCategoryMainViewModel

    @Inject
    lateinit var productRecommendationViewModel: TokoNowProductRecommendationViewModel

    override val userId: String
        get() = viewModel.getUserId()

    override val categoryIdL1: String
        get() = viewModel.categoryIdL1

    override val categoryIdL2: String
        get() = String.EMPTY

    override val categoryIdL3: String
        get() = String.EMPTY

    override val shopId: String
        get() = viewModel.getShopId().toString()

    override val currentCategoryId: String
        get() = viewModel.categoryIdL1

    override val addressData: LocalCacheModel
        get() = viewModel.getAddressData()

    override val adapter: CategoryAdapter by lazy {
        CategoryAdapter(
            typeFactory = CategoryAdapterTypeFactory(
                categoryTitleListener = createTitleCallback(),
                categoryNavigationListener = createCategoryNavigationCallback(),
                categoryShowcaseItemListener = createCategoryShowcaseItemCallback(),
                tokoNowView = createTokoNowViewCallback(),
                tokoNowChooseAddressWidgetListener = createTokoNowChooseAddressWidgetCallback(),
                tokoNowCategoryMenuListener = createTokoNowCategoryMenuCallback(),
                tokoNowProductRecommendationListener = createProductRecommendationCallback()
            ),
            differ = CategoryDiffer()
        )
    }

    override fun initInjector() = getComponent(CategoryComponent::class.java).inject(this)

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.getMiniCart()
        productRecommendationViewModel.updateMiniCartSimplified(miniCartSimplifiedData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCategoryHeader(
            navToolbarHeight = navToolbarHeight
        )

        observer()
    }

    override fun loadMore(isAtTheBottomOfThePage: Boolean) {
        viewModel.loadMore(isAtTheBottomOfThePage)
    }

    override fun refreshLayout() {
        viewModel.refreshLayout(
            navToolbarHeight = navToolbarHeight
        )
    }

    override fun getMiniCart() {
        viewModel.getMiniCart()
    }

    private fun observer() {
        observeCategoryHeader()
        observeCategoryPage()
        observeIsScrollNotNeeded()
        observeMiniCart()
        observeAddToCart()
        observeUpdateCartItem()
        observeRemoveCartItem()
        observeProductRecomAddToCart()
        observeProductRecomRemoveCartItem()
        observeProductRecomUpdateCartItem()
        observeToolbarNotification()
    }

    private fun observeCategoryHeader() {
        viewModel.categoryHeader.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    adapter.submitList(it.data)

                    viewModel.getFirstPage()

                    binding?.categoryShimmering?.root?.hide()
                }
                is Fail -> {}
            }
        }
    }

    private fun observeCategoryPage() {
        viewModel.categoryPage.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    adapter.submitList(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun observeIsScrollNotNeeded() {
        viewModel.isOnScrollNotNeeded.observe(viewLifecycleOwner) { isNotNeeded ->
            if (isNotNeeded) {
                binding?.rvCategory?.removeOnScrollListener(onScrollListener)
            } else {
                binding?.rvCategory?.addOnScrollListener(onScrollListener)
            }
        }
    }

    private fun observeMiniCart() {
        viewModel.miniCart.observe(viewLifecycleOwner) {
            when(it) {
                is Success -> {
                    val data = it.data
                    showMiniCart(data)
                    setupPadding(data)
                    productRecommendationViewModel.updateMiniCartSimplified(it.data)
                }
                is Fail -> {
                    hideMiniCart()
                    resetPadding()
                }
            }
        }
    }

    private fun observeAddToCart() {
        viewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessAddItemToCart(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeUpdateCartItem() {
        viewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success ->  onSuccessUpdateCartItem()
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeRemoveCartItem() {
        viewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessRemoveCartItem(result.data)
                is Fail -> showErrorToaster(result)
            }
        }
    }

    private fun observeProductRecomAddToCart() {
        productRecommendationViewModel.addItemToCart.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessAddItemToCart(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeProductRecomUpdateCartItem() {
        productRecommendationViewModel.updateCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    onSuccessUpdateCartItem()
                }
                is Fail -> {
                    showErrorToaster(
                        error = result
                    )
                }
            }
        }
    }

    private fun observeProductRecomRemoveCartItem() {
        productRecommendationViewModel.removeCartItem.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessRemoveCartItem(
                    data = result.data
                )
                is Fail -> showErrorToaster(
                    error = result
                )
            }
        }
    }

    private fun observeToolbarNotification() {
        viewModel.updateToolbarNotification.observe(viewLifecycleOwner) { needToUpdate ->
            if (needToUpdate) updateToolbarNotification()
        }
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second)
        getMiniCart()
    }

    /**
     * Callback Sections
     */

    private fun createTitleCallback() = CategoryTitleCallback(
        context = context,
        warehouseId = viewModel.getWarehouseId()
    )

    private fun createCategoryNavigationCallback() = CategoryNavigationCallback()

    private fun createCategoryShowcaseItemCallback() = CategoryShowcaseItemCallback(
        shopId = shopId,
        categoryIdL1 = categoryIdL1,
        startActivityForResult = ::startActivityForResult,
        onCartQuantityChangedListener = { position, product, quantity ->
            viewModel.onCartQuantityChanged(
                productId = product.productCardModel.productId,
                quantity = quantity,
                shopId = shopId,
                layoutType = CategoryLayoutType.CATEGORY_SHOWCASE
            )
        }
    )

    private fun createTokoNowViewCallback() = TokoNowViewCallback(
        fragment = this@TokoNowCategoryMainFragment
    ) {
        viewModel.refreshLayout(navToolbarHeight)
    }

    private fun createTokoNowCategoryMenuCallback() = TokoNowCategoryMenuCallback()

    private fun createTokoNowChooseAddressWidgetCallback() = TokoNowChooseAddressWidgetCallback()

    private fun createProductRecommendationCallback() = CategoryProductRecommendationCallback(
        productRecommendationViewModel = productRecommendationViewModel,
        activity = activity,
        categoryIdL1 = categoryIdL1,
        startActivityForResult = ::startActivityForResult,
        hideProductRecommendationWidgetListener = {
        }
    )
}

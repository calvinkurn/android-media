package com.tokopedia.tokopedianow.category.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView.ProductCardCompactListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryL2TabComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.presentation.adapter.CategoryL2TabAdapter
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2TabDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2TabAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2TabViewModel
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.util.RecyclerViewGridUtil.addProductItemDecoration
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowL2TabBinding
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokopedianow.similarproduct.presentation.activity.TokoNowSimilarProductBottomSheetActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


open class TokoNowCategoryL2TabFragment : Fragment() {

    companion object {
        fun newInstance(
            categoryIdL2: String = "",
            components: List<Component>
        ): TokoNowCategoryL2TabFragment {
            return TokoNowCategoryL2TabFragment().apply {
                this.categoryIdL2 = categoryIdL2
                this.components = components
            }
        }

        private const val SPAN_COUNT = 3
        private const val SPAN_FULL_SPACE = 1
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapterTypeFactory by lazy {
        CategoryL2TabAdapterTypeFactory(
            adsCarouselListener = createProductAdsCarouselListener(),
            quickFilterListener = createQuickFilterListener(),
            productItemListener = createProductItemListener(),
            productCardCompactListener = createProductCardCompactListener(),
            similarProductTrackerListener = createSimilarProductTrackerListener()
        )
    }

    private val categoryAdapter by lazy {
        CategoryL2TabAdapter(adapterTypeFactory, CategoryL2TabDiffer())
    }

    private val viewModel: TokoNowCategoryL2TabViewModel by viewModels {
        viewModelFactory
    }

    private var binding by autoClearedNullable<FragmentTokopedianowL2TabBinding>()

    private var loginActivityResult: ActivityResultLauncher<Intent>? = null
    private var addToCartVariantResult: ActivityResultLauncher<Intent>? = null

    private var categoryIdL2: String = ""
    private var components = listOf<Component>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResults()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokopedianowL2TabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeLiveData()
        onViewCreated()
    }

    override fun onDestroy() {
        loginActivityResult = null
        addToCartVariantResult = null
        adapterTypeFactory.onDestroy()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(components)
    }

    fun loadMore() {
        viewModel.loadMore()
    }

    private fun observeLiveData() {
        observe(viewModel.visitableListLiveData) {
            categoryAdapter.submitList(it)
        }

        observe(viewModel.addItemToCart) {
            when (it) {
                is Success -> onSuccessAddToCart(it)
                is Fail -> showErrorToaster(it.throwable)
            }
        }

        observe(viewModel.updateCartItem) {
            when (it) {
                is Success -> {}
                is Fail -> showErrorToaster(it.throwable)
            }
        }

        observe(viewModel.removeCartItem) {
            when (it) {
                is Success -> onSuccessRemoveCartItem(it)
                is Fail -> showErrorToaster(it.throwable)
            }
        }

        observe(viewModel.routeAppLink) {
            RouteManager.route(context, it)
        }

        observe(viewModel.openLoginPage) {
            openLoginPage()
        }
    }

    private fun onViewCreated() {
        viewModel.onViewCreated(categoryIdL2, components)
    }

    private fun registerActivityResults() {
        loginActivityResult = registerActivityResult {
            viewModel.refreshPage(components)
        }
        addToCartVariantResult = registerActivityResult {
            getMiniCart()
        }
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
                addOnScrollListener(createEndlessScrollListener(this))
                spanSizeLookup = createSpanSizeLookup()
            }
            adapter = categoryAdapter
            addProductItemDecoration()
        }
    }

    private fun createSpanSizeLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (categoryAdapter.getItemViewType(position)) {
                ProductItemViewHolder.LAYOUT -> SPAN_FULL_SPACE
                else -> SPAN_COUNT
            }
        }
    }

    private fun openVariantBottomSheet(productId: String, shopId: String) {
        AtcVariantHelper.goToAtcVariant(
            context = requireActivity(),
            productId = productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = shopId,
            trackerCdListName = String.format(TOKONOW_CATEGORY_ORGANIC, categoryIdL2),
            startActivitResult = { intent, _ ->
                addToCartVariantResult?.launch(intent)
            }
        )
    }

    private fun openLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        loginActivityResult?.launch(intent)
    }

    private fun registerActivityResult(
        onActivityResult: () -> Unit
    ) = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            onActivityResult.invoke()
        }
    }

    private fun getMiniCart() {
        viewModel.getMiniCart()
    }

    private fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        duration: Int = Toaster.LENGTH_SHORT,
        onClickActionBtn: View.OnClickListener = View.OnClickListener { }
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickActionBtn
                )
                toaster.show()
            }
        }
    }

    private fun showErrorToaster(throwable: Throwable) {
        val message = throwable.message.orEmpty()
        showToaster(message = message, type = Toaster.TYPE_ERROR)
    }

    private fun showAddToCartBlockedToaster() {
        val message = getString(
            R.string.tokopedianow_home_toaster_description_you_are_not_be_able_to_shop)
        showToaster(message = message)
    }

    private fun onSuccessAddToCart(result: Success<AddToCartDataModel>) {
        val message = result.data.errorMessage
            .joinToString(separator = ", ")
        showToaster(message)
        getMiniCart()
    }

    private fun onSuccessRemoveCartItem(result: Success<Pair<String, String>>) {
        val message = result.data.second
        val actionText = getString(R.string.tokopedianow_toaster_ok)
        showToaster(message = message, actionText = actionText)
        getMiniCart()
    }

    private fun injectDependencies() {
        DaggerCategoryL2TabComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .categoryContextModule(CategoryContextModule(requireContext()))
            .build()
            .inject(this)
    }

    private fun createProductAdsCarouselListener(): ProductAdsCarouselListener {
        return object : ProductAdsCarouselListener {
            override fun onProductCardClicked(
                position: Int,
                title: String,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                viewModel.createProductDetailAppLink(
                    product.getProductId(),
                    product.appLink
                )
            }

            override fun onProductCardImpressed(
                position: Int,
                title: String,
                product: ProductCardCompactCarouselItemUiModel
            ) {
            }

            override fun onProductCardQuantityChanged(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel,
                quantity: Int
            ) {
                val productCardModel = product.productCardModel
                viewModel.onCartQuantityChanged(
                    productId = product.getProductId(),
                    shopId = product.shopId,
                    quantity = quantity,
                    stock = productCardModel.availableStock,
                    isVariant = productCardModel.isVariant
                )
            }

            override fun onProductCardAddVariantClicked(
                position: Int,
                product: ProductCardCompactCarouselItemUiModel
            ) {
                openVariantBottomSheet(product.getProductId(), product.shopId)
            }
        }
    }

    private fun createProductItemListener(): ProductItemListener {
        return object : ProductItemListener {
            override fun onProductImpressed(productItemDataView: ProductItemDataView) {

            }

            override fun onProductClick(productItemDataView: ProductItemDataView) {
                val productId = productItemDataView.productCardModel.productId
                viewModel.createProductDetailAppLink(productId)
            }

            override fun onProductNonVariantQuantityChanged(
                productItemDataView: ProductItemDataView,
                quantity: Int
            ) {
                val productCardModel = productItemDataView.productCardModel
                viewModel.onCartQuantityChanged(
                    productId = productCardModel.productId,
                    shopId = productItemDataView.shop.id,
                    quantity = quantity,
                    stock = productCardModel.availableStock,
                    isVariant = productCardModel.isVariant
                )
            }

            override fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView) {
                val productCardModel = productItemDataView.productCardModel
                openVariantBottomSheet(productCardModel.productId, productItemDataView.shopId)
            }

            override fun onWishlistButtonClicked(
                productId: String,
                isWishlistSelected: Boolean,
                descriptionToaster: String,
                ctaToaster: String,
                type: Int,
                ctaClickListener: (() -> Unit)?
            ) {
                viewModel.updateWishlistStatus(productId, isWishlistSelected)
                showToaster(descriptionToaster, type, ctaToaster) {
                    ctaClickListener?.invoke()
                }
            }

            override fun onProductCardAddToCartBlocked() {
                showAddToCartBlockedToaster()
            }
        }
    }

    private fun createProductCardCompactListener(): ProductCardCompactListener {
        return object : ProductCardCompactListener {
            override fun onClickSimilarProduct(
                productId: String,
                similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?
            ) {
                val intent = TokoNowSimilarProductBottomSheetActivity.createNewIntent(
                    requireContext(),
                    productId,
                    similarProductTrackerListener
                )
                startActivity(intent)
            }
        }
    }

    private fun createSimilarProductTrackerListener(): ProductCardCompactSimilarProductTrackerListener {
        return object : ProductCardCompactSimilarProductTrackerListener {
            override fun trackImpressionBottomSheet(
                userId: String,
                warehouseId: String,
                similarProduct: ProductCardCompactSimilarProductUiModel,
                productIdTriggered: String
            ) {

            }

            override fun trackClickProduct(
                userId: String,
                warehouseId: String,
                similarProduct: ProductCardCompactSimilarProductUiModel,
                productIdTriggered: String
            ) {

            }

            override fun trackClickAddToCart(
                userId: String,
                warehouseId: String,
                similarProduct: ProductCardCompactSimilarProductUiModel,
                productIdTriggered: String,
                newQuantity: Int
            ) {

            }

            override fun trackClickCloseBottomsheet(
                userId: String,
                warehouseId: String,
                productIdTriggered: String
            ) {

            }

            override fun trackClickSimilarProductBtn(
                userId: String,
                warehouseId: String,
                productIdTriggered: String
            ) {

            }

            override fun trackImpressionEmptyState(
                userId: String,
                warehouseId: String,
                productIdTriggered: String
            ) {

            }
        }
    }

    private fun createQuickFilterListener(): QuickFilterListener {
        return object : QuickFilterListener {
            override fun openFilterPage() {
            }
        }
    }

    private fun createEndlessScrollListener(
        layoutManager: GridLayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
            }
        }
    }
}

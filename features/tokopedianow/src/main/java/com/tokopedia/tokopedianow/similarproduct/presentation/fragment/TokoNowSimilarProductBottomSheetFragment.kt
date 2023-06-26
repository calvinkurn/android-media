package com.tokopedia.tokopedianow.similarproduct.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.tokopedianow.similarproduct.domain.mapper.ProductRecommendationResponseMapper
import com.tokopedia.productcard.compact.similarproduct.presentation.bottomsheet.ProductCardCompactSimilarProductBottomSheet
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.productcard.compact.similarproduct.presentation.viewholder.ProductCardCompactSimilarProductViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.similarproduct.di.component.DaggerSimilarProductComponent
import com.tokopedia.tokopedianow.similarproduct.presentation.activity.TokoNowSimilarProductBottomSheetActivity.Companion.EXTRA_SIMILAR_PRODUCT_ID
import com.tokopedia.tokopedianow.similarproduct.presentation.viewmodel.TokoNowSimilarProductBottomSheetViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowSimilarProductBottomSheetFragment : Fragment(),
    ProductCardCompactSimilarProductViewHolder.SimilarProductListener,
    MiniCartWidgetListener {

    companion object {
        private const val REQUEST_CODE_LOGIN = 101
        private const val QUANTITY_ZERO = 0

        fun newInstance(productId: String?): TokoNowSimilarProductBottomSheetFragment {
            return TokoNowSimilarProductBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SIMILAR_PRODUCT_ID, productId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel : TokoNowSimilarProductBottomSheetViewModel

    private val productList = ArrayList<ProductCardCompactSimilarProductUiModel>()

    private var listener: ProductCardCompactSimilarProductTrackerListener? = null
    private var bottomSheet: ProductCardCompactSimilarProductBottomSheet? = null

    private val productIdTriggered: String
        get() = arguments?.getString(EXTRA_SIMILAR_PRODUCT_ID, "").orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLiveData()
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()

        viewModel.getSimilarProductList(productIdTriggered)
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return

        when(requestCode) {
            REQUEST_CODE_LOGIN -> activity?.finish()
        }
    }

    override fun onCartQuantityChanged(
        productId: String,
        shopId: String,
        quantity: Int,
        stock: Int,
        isVariant: Boolean
    ) {
        if(viewModel.isLoggedIn()) {
            viewModel.onCartQuantityChanged(productId, shopId, quantity, stock, isVariant)
        } else {
            goToLoginPage()
        }
    }

    override fun onProductClicked(product: ProductCardCompactSimilarProductUiModel) {
        goToProductDetailPage(product)
        listener?.trackClickProduct(
            userId = viewModel.getUserId(),
            warehouseId = viewModel.getWarehouseId(),
            similarProduct = product,
            productIdTriggered = productIdTriggered
        )
    }

    override fun onProductImpressed(product: ProductCardCompactSimilarProductUiModel) {
        listener?.trackImpressionBottomSheet(
            userId = viewModel.getUserId(),
            warehouseId = viewModel.getWarehouseId(),
            similarProduct = product,
            productIdTriggered = productIdTriggered
        )
    }

    override fun onResume() {
        super.onResume()
        getMiniCart()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.getMiniCart()
    }

    private fun goToProductDetailPage(item: ProductCardCompactSimilarProductUiModel) {
        val uri = UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.id)
        val appLink = viewModel.createAffiliateLink(uri)
        RouteManager.route(context, appLink)
    }

    private fun setupBottomSheet() {
        val title = getString(R.string.tokopedianow_similar_product_bottom_sheet_title)

        bottomSheet = ProductCardCompactSimilarProductBottomSheet.newInstance().apply {
            productListener = this@TokoNowSimilarProductBottomSheetFragment
            items = emptyList()
            setTitle(title)
            triggerProductId = arguments?.getString(EXTRA_SIMILAR_PRODUCT_ID, "").toString()
        }

        bottomSheet?.setOnDismissListener {
            listener?.trackClickCloseBottomsheet(
                userId = viewModel.getUserId(),
                warehouseId = viewModel.getWarehouseId(),
                productIdTriggered = productIdTriggered
            )
        }
        bottomSheet?.show(childFragmentManager)
        bottomSheet?.setListener(listener)
    }

    private fun observeLiveData() {
        viewModel.similarProductList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                //map this list to similar ui model list
                list?.forEachIndexed { index, recommendationItem ->
                    run {
                        recommendationItem?.let { recommendationItem ->
                            ProductRecommendationResponseMapper.mapToProductUiModel(
                                index,
                                recommendationItem
                            )?.let { mappedProduct -> productList.add(mappedProduct) }
                        }
                    }
                }
                viewModel.onViewCreated(productList)
            } else {
                // show no products ui
                bottomSheet?.showEmptyProductListUi()
            }
            trackAction()
        }

        observe(viewModel.visitableItems) {
            bottomSheet?.items = it
        }

        observe(viewModel.addItemToCart) {
            when (it) {
                is Success -> onSuccessAddItemToCart(it.data)
                is Fail -> showErrorToaster(it)
            }
        }

        observe(viewModel.removeCartItem) {
            when (it) {
                is Success -> onSuccessRemoveCartItem(it.data)
                is Fail -> showErrorToaster(it)
            }
        }

        observe(viewModel.updateCartItem) {
            when (it) {
                is Success -> onSuccessUpdateCartItem(it.data)
                is Fail -> showErrorToaster(it)
            }
        }

        observe(viewModel.miniCart) {
            when(it) {
                is Success -> {
                    bottomSheet?.setMiniCartData(it.data, viewModel.getShopId(), this)
                }
                is Fail -> { /* nothing to do */ }
            }
        }
    }

    private fun onSuccessAddItemToCart(data: AddToCartDataModel) {
        val message = data.errorMessage.joinToString(separator = ", ")
        showToaster(message = message, actionText = getString(R.string.tokopedianow_similar_product_bottom_sheet_see), onClickAction = {bottomSheet?.openMiniCartBottomsheet(this)})
        val position = productList.indexOfFirst {
            it.id == data.data.productId
        }
        bottomSheet?.changeQuantity(data.data.quantity, position)

        listener?.trackClickAddToCart(
            userId = viewModel.getUserId(),
            warehouseId = viewModel.getWarehouseId(),
            similarProduct = productList[position],
            productIdTriggered = productIdTriggered,
            newQuantity = data.data.quantity
        )

        getMiniCart()
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second, actionText = getString(R.string.tokopedianow_similar_product_bottom_sheet_ok), onClickAction = {})
        val position = productList.indexOfFirst {
            it.id == data.first
        }
        bottomSheet?.changeQuantity(QUANTITY_ZERO, position)
        getMiniCart()
    }

    private fun onSuccessUpdateCartItem(data : Triple<String, UpdateCartV2Data, Int>) {
        val shopId = viewModel.getShopId().toString()
        bottomSheet?.updateMiniCart(shopId)

        val productId = data.first
        val quantity = data.third
        val position = productList.indexOfFirst {
            it.id == productId
        }
        bottomSheet?.changeQuantity(quantity, position)
    }

    private fun showErrorToaster(error: Fail) {
        showToaster(
            message = error.throwable.message.orEmpty(),
            type = Toaster.TYPE_ERROR
        )
    }

    private fun getMiniCart() {
        viewModel.getMiniCart()
    }

    private fun showToaster(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickAction: View.OnClickListener = View.OnClickListener { }
    ) {
        bottomSheet?.showToaster(message, duration, type, actionText, onClickAction)
    }

    private fun goToLoginPage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun trackAction() {
        listener?.trackClickSimilarProductBtn(
            userId = viewModel.getUserId(),
            warehouseId = viewModel.getWarehouseId(),
            productIdTriggered = productIdTriggered
        )

        if(productList.isEmpty()) {
            listener?.trackImpressionEmptyState(
                userId = viewModel.getUserId(),
                warehouseId = viewModel.getWarehouseId(),
                productIdTriggered = productIdTriggered
            )
        }
    }

    private fun injectDependencies() {
        DaggerSimilarProductComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    fun setListener(listener: ProductCardCompactSimilarProductTrackerListener?){
        this.listener = listener
    }
}

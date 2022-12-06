package com.tokopedia.tokopedianow.similarproduct.fragment

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
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SimilarProductListener
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.similarproduct.activity.TokoNowSimilarProductActivity.Companion.EXTRA_SIMILAR_PRODUCT_ID
import com.tokopedia.tokopedianow.similarproduct.bottomsheet.TokoNowSimilarProductBottomSheet
import com.tokopedia.tokopedianow.similarproduct.di.component.DaggerSimilarProductComponent
import com.tokopedia.tokopedianow.similarproduct.domain.SimilarProductMapper
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.tokopedianow.similarproduct.viewholder.SimilarProductViewHolder
import com.tokopedia.tokopedianow.similarproduct.viewmodel.TokoNowSimilarProductViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowSimilarProductFragment : Fragment(), SimilarProductViewHolder.SimilarProductListener,
    MiniCartWidgetListener {

    companion object {
        private const val REQUEST_CODE_LOGIN = 101

        fun newInstance(products: String?): TokoNowSimilarProductFragment {
            return TokoNowSimilarProductFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SIMILAR_PRODUCT_ID, products)
                }
            }
        }
    }

    private val QUANTITY_ZERO = 0
    private val EMPTY_LIST_INDEX = -1
    private var listener: SimilarProductListener? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel : TokoNowSimilarProductViewModel

    @Inject
    lateinit var chooseAddressWrapper: ChooseAddressWrapper

    private val productList = ArrayList<SimilarProductUiModel>()

    private var bottomSheet: TokoNowSimilarProductBottomSheet? = null

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

        arguments?.getString(EXTRA_SIMILAR_PRODUCT_ID, "")?.let {
            viewModel.getSimilarProductList(userSession.userId.toIntOrZero(),
                it
            )
        }
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

    override fun deleteCartItem(productId: String) {
        val miniCartItem = viewModel.getMiniCartItem(productId)
        val cartId = miniCartItem?.cartId.orEmpty()
        viewModel.deleteCartItem(productId, cartId)
    }

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        if(userSession.isLoggedIn) {
            viewModel.onQuantityChanged(productId, shopId, quantity)
        } else {
            goToLoginPage()
        }
    }

    override fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        if(userSession.isLoggedIn) {
            viewModel.addItemToCart(productId, shopId, quantity)
        } else {
            goToLoginPage()
        }
    }

    override fun onResume() {
        super.onResume()
        getMiniCart()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        viewModel.getMiniCart()
    }

    private fun setupBottomSheet() {
        val title = getString(R.string.tokopedianow_recipe_similar_product_title)

        bottomSheet = TokoNowSimilarProductBottomSheet.newInstance().apply {
            productListener = this@TokoNowSimilarProductFragment
            items = emptyList()
            setTitle(title)
            triggerProductId = arguments?.getString(EXTRA_SIMILAR_PRODUCT_ID, "").toString()
        }

        bottomSheet?.show(childFragmentManager)
        bottomSheet?.setListener(listener)
    }

    private fun observeLiveData() {
        viewModel.similarProductList.observe(viewLifecycleOwner, { list ->
            if(list.isNotEmpty()) {
                //map this list to similar ui model list
                list?.forEachIndexed { index, recommendationItem ->
                    run {
                        recommendationItem?.let { recommendationItem ->
                            SimilarProductMapper.mapToProductUiModel(
                                index,
                                recommendationItem
                            )?.let { mappedProduct -> productList.add(mappedProduct) }
                        }
                    }
                }
                viewModel.onViewCreated(productList)
            }
            else{
                // show no products ui
                bottomSheet?.showEmptyProductListUi()
            }
            trackImpression()

        })
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
            if (it is Fail) {
                showErrorToaster(it)
            }
            else{
                val shopId = viewModel.getShopId().toString()
                bottomSheet?.updateMiniCart(shopId)
            }
        }

        observe(viewModel.miniCart) {
            when(it) {
                is Success -> {
                    val data = it.data
                    val indexList = arrayListOf<Int>()
                    data.miniCartItems.values.forEach {
                        if(it is MiniCartItem.MiniCartItemProduct){
                            val cartProduct = it
                            val index = productList.indexOfFirst { it.id == cartProduct.productId }
                            if(index != EMPTY_LIST_INDEX){
                                indexList.add(index)
                                productList[index].quantity = cartProduct.quantity
                            }
                        }
                    }
                    bottomSheet?.updateList(indexList)
                    bottomSheet?.showMiniCart(data, viewModel.getShopId(), this)
                }
                is Fail -> {
                    bottomSheet?.hideMiniCart()
                }
            }
        }
    }

    private fun onSuccessAddItemToCart(data: AddToCartDataModel) {
        val message = data.errorMessage.joinToString(separator = ", ")
        showToaster(message = message, actionText = "Lihat", onClickAction = {bottomSheet?.openMiniCartBottomsheet(this)})
        val position = productList.indexOfFirst {
            it.id == data.data.productId.toString()
        }
        bottomSheet?.changeQuantity(data.data.quantity, position)
        getMiniCart()
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second, actionText = "Oke", onClickAction = {})
        val position = productList.indexOfFirst {
            it.id == data.first
        }
        bottomSheet?.changeQuantity(QUANTITY_ZERO, position)
        getMiniCart()
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

    private fun trackImpression() {
            if(productList.isNotEmpty()) {
                listener?.trackImpressionBottomSheet(
                    userSession.userId,
                    warehouseId = chooseAddressWrapper.getChooseAddressData().warehouse_id,
                    productId = arguments?.getString(EXTRA_SIMILAR_PRODUCT_ID, "")
                        .toString(),
                    similarProducts = productList,
                )
            }
            else{
                listener?.trackImpressionEmptyState(
                    chooseAddressWrapper.getChooseAddressData().warehouse_id,
                    arguments?.getString(EXTRA_SIMILAR_PRODUCT_ID, "").toString()
                )
            }
    }

    private fun injectDependencies() {
        DaggerSimilarProductComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    fun setListener(listener: SimilarProductListener?){
        this.listener = listener
    }
}

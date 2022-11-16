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
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.similarproduct.activity.TokoNowSimilarProductActivity.Companion.EXTRA_SIMILAR_PRODUCT_ID
import com.tokopedia.tokopedianow.similarproduct.analytic.SimilarProductAnalytics
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

class TokoNowSimilarProductFragment : Fragment(), SimilarProductViewHolder.SimilarProductListener {

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

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
     lateinit var viewModel : TokoNowSimilarProductViewModel

    private val analytics by lazy { SimilarProductAnalytics(userSession) }

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
//        detailViewModel.checkAddressData()
        setupBottomSheet()
        trackImpression()

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

    private fun setupBottomSheet() {
        val title = getString(R.string.tokopedianow_recipe_similar_product_title)

        bottomSheet = TokoNowSimilarProductBottomSheet.newInstance().apply {
            productListener = this@TokoNowSimilarProductFragment
            productAnalytics = analytics
            items = emptyList()
            setTitle(title)
        }

        bottomSheet?.show(childFragmentManager)
    }

    private fun observeLiveData() {
        viewModel.similarProductList.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                //map this list to similar ui model list
                it?.forEachIndexed { index, recommendationItem ->
                    run {
                        recommendationItem?.let { it1 ->
                            SimilarProductMapper.mapToProductUiModel(
                                index,
                                it1
                            )?.let { it2 ->
                                productList.add(
                                    it2
                                )
                            }
                        }
                    }
                }
                viewModel.onViewCreated(productList)
            }
            else{
                // show no products ui
                bottomSheet?.showEmptyProductListUi()
            }
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
        }
    }

    private fun onSuccessAddItemToCart(data: AddToCartDataModel) {
        val message = data.errorMessage.joinToString(separator = ", ")
        showToaster(message = message)
        getMiniCart()
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second)
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
        analytics.trackImpressionBottomSheet()
    }

    private fun injectDependencies() {
        DaggerSimilarProductComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}

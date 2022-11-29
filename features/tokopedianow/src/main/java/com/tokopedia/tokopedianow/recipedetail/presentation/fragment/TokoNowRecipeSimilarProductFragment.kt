package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipecommon.bottomsheet.TokoNowRecipeProductBottomSheet
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeSimilarProductAnalytics
import com.tokopedia.tokopedianow.recipedetail.presentation.activity.TokoNowRecipeSimilarProductActivity.Companion.EXTRA_SIMILAR_PRODUCT_LIST
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener
import com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel.TokoNowRecipeSimilarProductViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowRecipeSimilarProductFragment : Fragment(), RecipeProductListener {

    companion object {
        private const val REQUEST_CODE_LOGIN = 101

        fun newInstance(products: List<RecipeProductUiModel>): TokoNowRecipeSimilarProductFragment {
            return TokoNowRecipeSimilarProductFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_SIMILAR_PRODUCT_LIST, ArrayList(products))
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)
            .get(TokoNowRecipeSimilarProductViewModel::class.java)
    }

    private val analytics by lazy { RecipeSimilarProductAnalytics(userSession) }

    private var bottomSheet: TokoNowRecipeProductBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productList = arguments
            ?.getParcelableArrayList<RecipeProductUiModel>(EXTRA_SIMILAR_PRODUCT_LIST).orEmpty()

        observeLiveData()
        setupBottomSheet()
        trackImpression()

        viewModel.onViewCreated(productList)
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

        bottomSheet = TokoNowRecipeProductBottomSheet.newInstance().apply {
            productListener = this@TokoNowRecipeSimilarProductFragment
            productAnalytics = analytics
            items = emptyList()
            setTitle(title)
        }

        bottomSheet?.show(childFragmentManager)
    }

    private fun observeLiveData() {
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
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}
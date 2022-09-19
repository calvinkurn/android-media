package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        val title = getString(R.string.tokopedianow_recipe_similar_product_title)
        val productList = arguments
            ?.getParcelableArrayList<RecipeProductUiModel>(EXTRA_SIMILAR_PRODUCT_LIST).orEmpty()

        val bottomSheet = TokoNowRecipeProductBottomSheet.newInstance().apply {
            productListener = this@TokoNowRecipeSimilarProductFragment
            productAnalytics = analytics
            items = productList
            setTitle(title)
        }
        bottomSheet.show(childFragmentManager)

        trackBottomSheetImpression()
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun deleteCartItem(productId: String) {
        val miniCartItem = viewModel.getMiniCartItem(productId)
        val cartId = miniCartItem?.cartId.orEmpty()
        viewModel.deleteCartItem(productId, cartId)
    }

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        viewModel.onQuantityChanged(productId, shopId, quantity)
    }

    override fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        viewModel.addItemToCart(productId, shopId, quantity)
    }

    private fun observeLiveData() {
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
    }

    private fun onSuccessRemoveCartItem(data: Pair<String, String>) {
        showToaster(message = data.second)
    }

    private fun showErrorToaster(error: Fail) {
        showToaster(
            message = error.throwable.message.orEmpty(),
            type = Toaster.TYPE_ERROR
        )
    }

    private fun showToaster(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickAction: View.OnClickListener = View.OnClickListener { }
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickAction
                )
                toaster.show()
            }
        }
    }

    private fun trackBottomSheetImpression() {
        analytics.trackImpressionBottomSheet()
    }

    private fun injectDependencies() {
        DaggerRecipeDetailComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}
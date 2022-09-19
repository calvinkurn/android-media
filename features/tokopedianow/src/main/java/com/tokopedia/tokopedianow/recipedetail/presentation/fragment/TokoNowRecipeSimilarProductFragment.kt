package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipecommon.bottomsheet.TokoNowRecipeProductBottomSheet
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeSimilarProductAnalytics
import com.tokopedia.tokopedianow.recipedetail.di.component.DaggerRecipeDetailComponent
import com.tokopedia.tokopedianow.recipedetail.presentation.activity.TokoNowRecipeSimilarProductActivity.Companion.EXTRA_SIMILAR_PRODUCT_LIST
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener
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
    }

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
    }

    override fun addItemToCart(productId: String, shopId: String, quantity: Int) {
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
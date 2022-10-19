package com.tokopedia.tokopedianow.recipedetail.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow.RECIPE_SIMILAR_PRODUCT_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.recipedetail.presentation.fragment.TokoNowRecipeSimilarProductFragment
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel

class TokoNowRecipeSimilarProductActivity: BaseTokoNowActivity() {

    companion object {
        const val EXTRA_SIMILAR_PRODUCT_LIST = "extra_similar_product_list"

        fun createNewIntent(context: Context, products: List<RecipeProductUiModel>): Intent {
            return RouteManager.getIntent(context, RECIPE_SIMILAR_PRODUCT_BOTTOM_SHEET).apply {
                putParcelableArrayListExtra(EXTRA_SIMILAR_PRODUCT_LIST, ArrayList(products))
            }
        }
    }

    override fun getFragment(): Fragment {
        val products = intent
            ?.getParcelableArrayListExtra<RecipeProductUiModel>(EXTRA_SIMILAR_PRODUCT_LIST).orEmpty()
        return TokoNowRecipeSimilarProductFragment.newInstance(products)
    }
}
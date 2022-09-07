package com.tokopedia.tokopedianow.recipedetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recipecommon.bottomsheet.TokoNowRecipeProductBottomSheet
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.viewholders.RecipeProductViewHolder.RecipeProductListener

class TokoNowRecipeSimilarProductFragment : Fragment(), RecipeProductListener {

    companion object {

        fun newInstance(): TokoNowRecipeSimilarProductFragment {
            return TokoNowRecipeSimilarProductFragment()
        }
    }

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
        val bottomSheet = TokoNowRecipeProductBottomSheet.newInstance().apply {
            val productList = mutableListOf<RecipeProductUiModel>()

            for (i in 0..12) {
                val position = i + 1

                productList.add(
                    RecipeProductUiModel(
                        id = position.toString(),
                        shopId = (i * 1).toString(),
                        name = "Product Name $position",
                        quantity = i * 2,
                        stock = i * 3,
                        minOrder = 0,
                        maxOrder = i * 3,
                        priceFmt = "Rp 50.000",
                        weight = "500g",
                        imageUrl = "",
                        slashedPrice = "",
                        discountPercentage = "",
                    )
                )
            }

            productListener = this@TokoNowRecipeSimilarProductFragment
            items = productList
            setTitle(title)
        }
        bottomSheet.show(childFragmentManager)
    }

    override fun deleteCartItem(productId: String) {
    }

    override fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
    }

    override fun addItemToCart(productId: String, shopId: String, quantity: Int) {
    }
}
package com.tokopedia.tokopedianow.test.common.productcard.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowWishlistButtonView.TokoNowWishlistButtonListener
import com.tokopedia.tokopedianow.similarproduct.listener.TokoNowSimilarProductTrackerListener
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.tokopedianow.test.R

internal class TokoNowProductCardViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView),
    TokoNowWishlistButtonListener,
    TokoNowSimilarProductTrackerListener{

    private val productCard: TokoNowProductCardView? by lazy {
        itemView.findViewById(R.id.product_card)
    }

    fun bind(model: TokoNowProductCardViewUiModel) {
        productCard?.apply {
            setData(model)
            setOnClickListener { showToaster("Click Product Card") }
            setOnClickQuantityEditorListener { showToaster("Click Quantity Editor") }
            setOnClickQuantityEditorVariantListener { showToaster("Click Quantity Editor Variant") }
            setSimilarProductListener(this@TokoNowProductCardViewHolder)
            setWishlistButtonListener(this@TokoNowProductCardViewHolder)
        }
    }

    override fun onWishlistButtonClicked(
        productId: String,
        isWishlistSelected: Boolean,
        descriptionToaster: String,
        ctaToaster: String,
        type: Int,
        ctaClickListener: (() -> Unit)?
    ) {
        showToaster("Click Wishlist Button")
    }

    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
        productIdTriggered: String
    ) {
        showToaster("Track BottomSheet Impression")
    }

    override fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
        productIdTriggered: String
    ) {
        showToaster("Track Click Product on BottomSheet")
    }

    override fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
        productIdTriggered: String,
        newQuantity: Int
    ) {
        showToaster("Track Click ATC Product on BottomSheet")
    }

    override fun trackClickCloseBottomsheet(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        showToaster("Track Click Close on BottomSheet")
    }

    override fun trackClickSimilarProductBtn(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        showToaster("Track Click Similar Product Button on BottomSheet")
    }

    override fun trackImpressionEmptyState(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        showToaster("Track Product on BottomSheet Impression")
    }

    private fun showToaster(message: String) {
        val toastMessage = "Position ${layoutPosition}n, $message"
        Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
    }
}

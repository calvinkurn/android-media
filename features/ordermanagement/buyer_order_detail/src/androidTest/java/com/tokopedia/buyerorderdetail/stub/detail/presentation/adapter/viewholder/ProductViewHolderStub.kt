package com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PartialProductItemViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel

class ProductViewHolderStub(
    itemView: View?,
    listener: PartialProductItemViewHolder.ProductViewListener,
    navigator: BuyerOrderDetailNavigator
) : ProductViewHolder(itemView, listener, navigator) {

    override fun setupButton(showBuyAgainButton: ActionButtonsUiModel.ActionButton, processing: Boolean) {
        btnBuyerOrderDetailBuyProductAgain?.apply {
//            isLoading = processing -> there's an issue when the button is showing loader
            text = showBuyAgainButton.label
            buttonVariant = Utils.mapButtonVariant(showBuyAgainButton.variant)
            buttonType = Utils.mapButtonType(showBuyAgainButton.type)
            showWithCondition(showBuyAgainButton.label.isNotBlank())
        }
    }

    // noop since image loading placeholder causing the main thread busy
    override fun setupProductThumbnail(productThumbnailUrl: String) {}
}

package com.tokopedia.buyerorder.detail.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.view.adapter.BuyerBundlingProductItemAdapter
import com.tokopedia.buyerorder.detail.view.adapter.divider.BuyerBundlingProductItemDivider
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerBundlingProductUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class BuyerBundlingProductViewHolder(itemView: View?): AbstractViewHolder<BuyerBundlingProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.bottomsheet_cancel_bundle_item

        private const val PRODUCT_BUNDLING_ICON_URL = "https://images.tokopedia.net/img/android/others/ic_product_bundling.png"
    }

    private val bundleNameText: Typography? = itemView?.findViewById(R.id.tv_buyer_order_bundling_header)
    private val bundleIconImage: ImageUnify? = itemView?.findViewById(R.id.iv_buyer_order_bundling_header)
    private val bundleItemsRecyclerView: RecyclerView? = itemView?.findViewById(R.id.rv_buyer_order_bundling)

    override fun bind(element: BuyerBundlingProductUiModel) {
        bundleNameText?.text = element.bundleName
        bundleIconImage?.setImageUrl(PRODUCT_BUNDLING_ICON_URL)
        bundleItemsRecyclerView?.run {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = BuyerBundlingProductItemAdapter(element.productList)
            addItemDecoration(BuyerBundlingProductItemDivider(itemView.context))
        }
    }
}
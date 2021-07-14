package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.ProductBundlingItemAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.itemdecoration.ProductBundlingItemDecoration
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductBundlingViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
): AbstractViewHolder<ProductListUiModel.ProductBundlingUiModel>(itemView), View.OnClickListener, ProductBundlingItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_bundling

        private const val PRODUCT_BUNDLING_IMAGE_ICON_URL = "https://images.tokopedia.net/img/android/others/ic_product_bundling.png"
    }

    private val bundleItemDecoration by lazy {
        itemView?.context?.let {
            ProductBundlingItemDecoration(it)
        }
    }

    private var containerLayout: ConstraintLayout? = null
    private var bundlingNameText: Typography? = null
    private var bundlingIconImage: ImageUnify? = null
    private var bundlingItemRecyclerView: RecyclerView? = null
    private var bundlingPriceText: Typography? = null
    private var bundlingPurchaseAgainButton: UnifyButton? = null

    init {
        itemView?.run {
            containerLayout = findViewById(R.id.container_bom_detail_bundling)
            bundlingNameText = findViewById(R.id.tv_bom_detail_bundling_name)
            bundlingIconImage = findViewById(R.id.iv_bom_detail_bundling_icon)
            bundlingItemRecyclerView = findViewById(R.id.rv_bom_detail_bundling)
            bundlingPriceText = findViewById(R.id.tv_bom_detail_bundling_price_value)
            bundlingPurchaseAgainButton = findViewById(R.id.btn_bom_detail_bundling_purchase_again)
        }

        setupClickListener()
    }

    override fun bind(element: ProductListUiModel.ProductBundlingUiModel) {
        setupBundleHeader(element.bundleName)
        setupBundleAdapter(element.bundleItemList)
        setupBundleTotalPrice(element.totalPriceText)
    }

    override fun onClick(p0: View?) {

    }

    override fun onBundleItemClicked(orderId: String, orderDetailId: String) {
//        TODO("Not yet implemented")
    }

    private fun setupClickListener() {

    }

    private fun setupBundleHeader(bundleName: String) {
        bundlingNameText?.text = bundleName
        bundlingIconImage?.setImageUrl(PRODUCT_BUNDLING_IMAGE_ICON_URL)
    }

    private fun setupBundleAdapter(bundleItemList: List<ProductListUiModel.ProductBundlingItemUiModel>) {
        bundlingItemRecyclerView?.run {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = ProductBundlingItemAdapter(this@ProductBundlingViewHolder, bundleItemList)
            bundleItemDecoration?.let {
                addItemDecoration(it)
            }
        }
    }

    private fun setupBundleTotalPrice(price: String) {
        bundlingPriceText?.text = price
    }

    interface Listener {
        fun onPurchaseAgainButtonClicked()
    }

}
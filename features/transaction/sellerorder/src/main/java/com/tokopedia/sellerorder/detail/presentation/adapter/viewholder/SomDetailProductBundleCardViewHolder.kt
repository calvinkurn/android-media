package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.RecyclerViewItemDivider
import com.tokopedia.sellerorder.databinding.ItemSomProductBundlingBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailProductBundlingAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created By @ilhamsuaib on 05/07/21
 */

class SomDetailProductBundleCardViewHolder(
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?,
    private val addOnListener: AddOnViewHolder.Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool,
    itemView: View?
) : AbstractViewHolder<ProductBundleUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_som_product_bundling
    }

    private val binding by viewBinding<ItemSomProductBundlingBinding>()

    private val productAdapter by lazy {
        SomDetailProductBundlingAdapter(actionListener, addOnListener, recyclerViewSharedPool)
    }

    override fun bind(element: ProductBundleUiModel) {
        binding?.run {
            icSomProductBundling.loadImage(element.bundleIcon)
            tvSomBundleName.text = element.bundleName
            tvSomTotalPrice.text = element.bundleSubTotal

            setupProductList(element.products)
            containerSomProductBundling.setupCardDarkMode()
        }
    }

    private fun setupProductList(products: List<ProductBundleUiModel.ProductUiModel>) {
        binding?.rvSomProductBundling?.run {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
            setRecycledViewPool(recyclerViewSharedPool)
            adapter = productAdapter
            if (itemDecorationCount == 0) {
                val margins = context.resources.getDimension(unifycomponentsR.dimen.spacing_lvl4).toInt()
                addItemDecoration(
                    RecyclerViewItemDivider(
                        divider = MethodChecker.getDrawable(context, R.drawable.som_detail_product_bundling_product_divider),
                        topMargin = margins,
                        bottomMargin = margins,
                        applyMarginAfterLastItem = true
                    )
                )
            }

            productAdapter.products = products
            post {
                productAdapter.notifyDataSetChanged()
            }
        }
    }
}

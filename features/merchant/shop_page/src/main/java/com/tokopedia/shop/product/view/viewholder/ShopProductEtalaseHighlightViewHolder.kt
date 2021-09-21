package com.tokopedia.shop.product.view.viewholder

import android.view.View

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapter
import com.tokopedia.shop.product.view.adapter.EtalaseHighlightAdapterTypeFactory
import com.tokopedia.shop.product.view.datamodel.EtalaseHighlightCarouselUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseHighlightUiModel
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener

import java.util.ArrayList

class ShopProductEtalaseHighlightViewHolder(itemView: View, deviceWidth: Int,
                                            shopProductClickedListener: ShopProductClickedListener?,
                                            shopProductImpressionListener: ShopProductImpressionListener?,
                                            shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?) : AbstractViewHolder<ShopProductEtalaseHighlightUiModel>(itemView) {

    private var recyclerView: RecyclerView? = null
    private val etalaseHighlightAdapter: EtalaseHighlightAdapter

    init {
        etalaseHighlightAdapter = EtalaseHighlightAdapter(
                EtalaseHighlightAdapterTypeFactory(shopProductClickedListener,
                        shopProductImpressionListener,
                        shopCarouselSeeAllClickedListener,
                        deviceWidth))
        findViews(itemView)
    }

    override fun bind(shopProductEtalaseHighlightUiModel: ShopProductEtalaseHighlightUiModel) {
        var etalaseHighlightCarouselUiModelList: List<EtalaseHighlightCarouselUiModel>? = shopProductEtalaseHighlightUiModel.getEtalaseHighlightCarouselUiModelList()
        if (etalaseHighlightCarouselUiModelList == null) {
            etalaseHighlightCarouselUiModelList = ArrayList()
        }
        etalaseHighlightAdapter.softClear()
        etalaseHighlightAdapter.setElement(etalaseHighlightCarouselUiModelList)
        etalaseHighlightAdapter.notifyDataSetChanged()
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewEtalaseHighLight)
        val layoutManager = LinearLayoutManager(view.context,
                LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        val animator = recyclerView!!.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        recyclerView!!.isNestedScrollingEnabled = false
        recyclerView!!.adapter = etalaseHighlightAdapter
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_etalase_highlight
    }

}

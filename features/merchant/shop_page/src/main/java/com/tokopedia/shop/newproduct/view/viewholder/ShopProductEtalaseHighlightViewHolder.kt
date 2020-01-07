package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.adapter.EtalaseHighlightAdapter
import com.tokopedia.shop.newproduct.view.adapter.EtalaseHighlightAdapterTypeFactory
import com.tokopedia.shop.newproduct.view.datamodel.EtalaseHighlightCarouselViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseHighlightViewModel
import com.tokopedia.shop.newproduct.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductClickedListener

import java.util.ArrayList

class ShopProductEtalaseHighlightViewHolder(itemView: View, deviceWidth: Int,
                                            shopProductClickedListener: ShopProductClickedListener?,
                                            shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?) : AbstractViewHolder<ShopProductEtalaseHighlightViewModel>(itemView) {

    private var recyclerView: RecyclerView? = null
    private val etalaseHighlightAdapter: EtalaseHighlightAdapter

    init {
        etalaseHighlightAdapter = EtalaseHighlightAdapter(
                EtalaseHighlightAdapterTypeFactory(shopProductClickedListener,
                        shopCarouselSeeAllClickedListener,
                        deviceWidth))
        findViews(itemView)
    }

    override fun bind(shopProductEtalaseHighlightViewModel: ShopProductEtalaseHighlightViewModel) {
        var etalaseHighlightCarouselViewModelList: List<EtalaseHighlightCarouselViewModel>? = shopProductEtalaseHighlightViewModel.etalaseHighlightCarouselViewModelList
        if (etalaseHighlightCarouselViewModelList == null) {
            etalaseHighlightCarouselViewModelList = ArrayList()
        }
        etalaseHighlightAdapter.softClear()
        etalaseHighlightAdapter.setElement(etalaseHighlightCarouselViewModelList)
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
        val LAYOUT = R.layout.item_shop_product_etalase_highlight
    }

}

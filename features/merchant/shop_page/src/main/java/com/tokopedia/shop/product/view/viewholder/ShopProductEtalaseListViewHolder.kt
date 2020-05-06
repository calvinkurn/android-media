package com.tokopedia.shop.product.view.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.adapter.ShopProductEtalaseAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.ShopProductEtalaseAdapter
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseChipItemViewModel
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseListViewModel

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductEtalaseListViewHolder(
        itemView: View,
        private val shopProductEtalaseChipListViewHolderListener: ShopProductEtalaseChipListViewHolderListener?
) : AbstractViewHolder<ShopProductEtalaseListViewModel>(itemView) {

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_new_shop_product_etalase_chip_list
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonEtalaseMore: View
    private lateinit var shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel
    private val shopEtalaseAdapter: ShopProductEtalaseAdapter = ShopProductEtalaseAdapter(ShopProductEtalaseAdapterTypeFactory(
            shopProductEtalaseChipListViewHolderListener
    ))

    interface ShopProductEtalaseChipListViewHolderListener {
        fun onEtalaseChipClicked(shopProductEtalaseChipItemViewModel: ShopProductEtalaseChipItemViewModel)
        fun onEtalaseMoreListClicked()
        fun onAddEtalaseChipClicked()
    }

    init {
        initLayout(itemView)
    }

    private fun initLayout(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val animator = recyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        recyclerView.adapter = shopEtalaseAdapter
        buttonEtalaseMore = view.findViewById(R.id.v_etalase_more)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                shopProductEtalaseListViewModel.recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            }
        })
    }

    override fun bind(shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel) {
        this.shopProductEtalaseListViewModel = shopProductEtalaseListViewModel
        shopProductEtalaseListViewModel.recyclerViewState?.let {
            recyclerView.layoutManager?.onRestoreInstanceState(it)
        }
        shopEtalaseAdapter.setElements(shopProductEtalaseListViewModel.etalaseModelList)
        val selectedEtalaseId = shopProductEtalaseListViewModel.selectedEtalaseId
        shopEtalaseAdapter.selectedEtalaseId = selectedEtalaseId
        shopEtalaseAdapter.notifyDataSetChanged()
        buttonEtalaseMore.setOnClickListener {
            shopProductEtalaseChipListViewHolderListener?.onEtalaseMoreListClicked()
        }
    }
}
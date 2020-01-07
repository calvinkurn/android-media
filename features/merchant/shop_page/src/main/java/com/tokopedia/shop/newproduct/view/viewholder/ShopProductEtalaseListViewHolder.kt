package com.tokopedia.shop.newproduct.view.viewholder

import android.os.Parcelable
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.view.View
import android.view.animation.TranslateAnimation

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.newproduct.view.adapter.ShopProductEtalaseAdapterTypeFactory
import com.tokopedia.shop.newproduct.view.adapter.ShopProductEtalaseAdapter
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseChipItemViewModel
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductEtalaseListViewModel

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
        val LAYOUT = R.layout.item_shop_new_product_etalase_chip_list
    }

    private lateinit var recyclerView: RecyclerView
    private var recyclerViewState: Parcelable? = null
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
                if (dx > 15) {
                    if (!buttonEtalaseMore.isVisible) {
                        val anim = TranslateAnimation(
                                buttonEtalaseMore.height.toFloat(), 0f, 0f, 0f
                        )
                        anim.duration = 100
                        anim.fillAfter = true
                        buttonEtalaseMore.startAnimation(anim)
                        buttonEtalaseMore.show()
                        shopProductEtalaseListViewModel.isButtonEtalaseMoreShown = true
                    }
                } else if (dx <= -15) {
                    if (buttonEtalaseMore.isVisible) {
                        val anim = TranslateAnimation(
                                0f, buttonEtalaseMore.height.toFloat(), 0f, 0f
                        )
                        anim.duration = 100
                        anim.fillAfter = true
                        buttonEtalaseMore.startAnimation(anim)
                        buttonEtalaseMore.hide()
                        shopProductEtalaseListViewModel.isButtonEtalaseMoreShown = false
                    }
                }
                shopProductEtalaseListViewModel.recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            }
        })
    }

    override fun bind(shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel) {
        this.shopProductEtalaseListViewModel = shopProductEtalaseListViewModel
        shopProductEtalaseListViewModel.recyclerViewState?.let{
            recyclerView.layoutManager?.onRestoreInstanceState(it)
        }
        if (this.shopProductEtalaseListViewModel.isButtonEtalaseMoreShown) {
            buttonEtalaseMore.show()
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
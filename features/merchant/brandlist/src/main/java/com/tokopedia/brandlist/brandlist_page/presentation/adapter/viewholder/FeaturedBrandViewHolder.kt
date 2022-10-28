package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.data.model.Shop
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandUiModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.FeaturedBrandAdapter
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography

class FeaturedBrandViewHolder(itemView: View?, val listener: BrandlistPageTrackingListener) :
        AbstractViewHolder<FeaturedBrandUiModel>(itemView) {

    private var featuredBrandList: List<Shop> = listOf()
    private var incrementalBrandList: List<Shop> = listOf()
    private var context: Context? = null
    private var headerView: Typography? = null
    private var adapter: FeaturedBrandAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var expandButtonView: AppCompatTextView? = null

    companion object {
        const val INITIAL_AMOUNT = 4
        const val INCREMENTAL_AMOUNT = 6
        const val START_INDEX_IMPRESSION = 0
        const val INCREMENTAL_INDEX_IMPRESSION = 3

        @LayoutRes
        val LAYOUT = R.layout.brandlist_featured_brand_layout
    }

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        recyclerView = itemView?.findViewById(R.id.rv_featured_brand)
        expandButtonView = itemView?.findViewById(R.id.tv_expand_button)

        itemView?.context?.let {
            context = it
            adapter = FeaturedBrandAdapter(it, listener)
            recyclerView?.layoutManager = GridLayoutManager(it, 2)
            recyclerView?.adapter = adapter
            expandButtonView?.setOnClickListener(createExpandButtonOnClickListener())
        }
    }

    override fun bind(element: FeaturedBrandUiModel?) {
        headerView?.text = element?.header?.title

        element?.featuredBrands?.let {
            setFeaturedBrandList(it)
            setIncrementalBrandList(getInitialBrandList(it))
            setImpressionDataTracking(getInitialBrandList(incrementalBrandList), START_INDEX_IMPRESSION)
            if (recyclerView?.isComputingLayout == false) {
                adapter?.setFeaturedBrands(getInitialBrandList(incrementalBrandList))
            }
        }

        if (featuredBrandList.size <= INITIAL_AMOUNT) {
            expandButtonView?.hide()
        } else {
            expandButtonView?.text = getString(R.string.brandlist_action_expand_all)
            expandButtonView?.visible()
        }
    }

    private fun setImpressionDataTracking(shops: List<Shop>, position: Int) {
        shops.forEachIndexed { index, shop ->
            listener.impressionBrandPilihan(
                    (shop.id).toString(),
                    (position + index + 1).toString(),
                    shop.imageUrl, shop.name)
        }
    }

    private fun getInitialBrandList(featuredBrandList: List<Shop>): List<Shop> {
        return featuredBrandList.take(INITIAL_AMOUNT).toMutableList()
    }

    private fun createExpandButtonOnClickListener(): View.OnClickListener? {
        return View.OnClickListener {
            if (featuredBrandList.size != incrementalBrandList.size) {
                val incrementalAmount = getIncrementalAmount(featuredBrandList, incrementalBrandList)
                val incrementedBrandList = getIncrementedBrandList(featuredBrandList, incrementalAmount)
                setIncrementalBrandList(incrementedBrandList)
                setImpressionDataTracking(getInitialBrandList(incrementalBrandList), INCREMENTAL_INDEX_IMPRESSION)
                if (recyclerView?.isComputingLayout == false) {
                    adapter?.setFeaturedBrands(incrementedBrandList)
                    expandButtonView?.let { button -> hideExpandButton(featuredBrandList, incrementedBrandList, button) }
                    listener.clickLihatSemua()
                }
            }
        }
    }

    private fun setFeaturedBrandList(featuredBrandList: List<Shop>) {
        this.featuredBrandList = featuredBrandList
    }

    private fun setIncrementalBrandList(renderedBrandList: List<Shop>) {
        this.incrementalBrandList = renderedBrandList
    }

    private fun getIncrementedBrandList(featuredBrandList: List<Shop>,
                                        incrementalAmount: Int): MutableList<Shop> {
        return featuredBrandList.take(incrementalAmount).toMutableList()
    }

    private fun getIncrementalAmount(featuredBrandList: List<Shop>,
                                     incrementalBrandList: List<Shop>): Int {
        val totalSize = featuredBrandList.size
        val renderedBrands = incrementalBrandList.size
        val incrementalAmount = renderedBrands + INCREMENTAL_AMOUNT
        return if (incrementalAmount <= totalSize) incrementalAmount
        else totalSize
    }

    private fun hideExpandButton(featuredBrandList: List<Shop>,
                                 incrementedBrandList: List<Shop>,
                                 expandButton: AppCompatTextView) {
        if (featuredBrandList.size == incrementedBrandList.size) {
            expandButton.hide()
        }
    }
}
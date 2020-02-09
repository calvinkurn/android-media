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
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.FeaturedBrandAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifyprinciples.Typography

class FeaturedBrandViewHolder(itemView: View?) : AbstractViewHolder<FeaturedBrandViewModel>(itemView) {

    private var featuredBrandList: List<Shop> = listOf()
    private var incrementalBrandList: List<Shop> = listOf()

    private var context: Context? = null
    private var headerView: Typography? = null
    private var adapter: FeaturedBrandAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var expandButtonView: AppCompatTextView? = null

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        recyclerView = itemView?.findViewById(R.id.rv_featured_brand)
        expandButtonView = itemView?.findViewById(R.id.tv_expand_button)

        itemView?.context?.let {
            context = it
            adapter = FeaturedBrandAdapter(it)
            recyclerView?.layoutManager = GridLayoutManager(it, 2)
            recyclerView?.adapter = adapter
        }
    }

    override fun bind(element: FeaturedBrandViewModel?) {
        headerView?.text = element?.header?.title

        element?.featuredBrands?.let {

            setFeaturedBrandList(it)
            setIncrementalBrandList(getInitialBrandList(it))

            adapter?.setFeaturedBrands(getInitialBrandList(incrementalBrandList))
        }

        if (featuredBrandList.size <= INITIAL_AMOUNT) {
            expandButtonView?.hide()
        } else {
            expandButtonView?.text = element?.header?.ctaText
            expandButtonView?.setOnClickListener(createExpandButtonOnClickListener())
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
                adapter?.setFeaturedBrands(incrementedBrandList)
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

    companion object {

        const val INITIAL_AMOUNT = 4
        const val INCREMENTAL_AMOUNT = 6

        @LayoutRes
        val LAYOUT = R.layout.brandlist_featured_brand_layout
    }
}
package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * author by Rafli Syam on 03/08/2021
 */
class ShopHomeShowcaseListSliderMediumViewHolder (
        itemView: View
) : AbstractViewHolder<ShopHomeShowcaseListSliderUiModel>(itemView) {

    companion object {
        /**
         * Base slider layout
         */
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_base_etalase_list_slider

        /**
         * Slider layout types
         */
        @LayoutRes
        private val ITEM_SLIDER_MEDIUM_LAYOUT = R.layout.item_shop_home_etalase_list_slider_medium
    }

    private var tvCarouselTitle : TextView? = null
    private var recyclerView : RecyclerView? = null

    init {
        initView()
    }

    override fun bind(element: ShopHomeShowcaseListSliderUiModel) {
        tvCarouselTitle?.text = element.header.title
        initRecyclerView(element.showcaseListItem)
    }

    private fun initView() {
        tvCarouselTitle = itemView.findViewById(R.id.tvShowcaseSectionTitle)
        recyclerView = itemView.findViewById(R.id.rvShowcaseListWidget)
    }

    private fun initRecyclerView(showcaseListItemData: List<ShopHomeShowcaseListItemUiModel>) {
        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = SliderMediumAdapter(showcaseListItemData)
        }
    }

    inner class SliderMediumAdapter(showcaseListItemData: List<ShopHomeShowcaseListItemUiModel>): RecyclerView.Adapter<SliderMediumViewHolder>() {

        private var showcaseListItem: List<ShopHomeShowcaseListItemUiModel> = showcaseListItemData

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderMediumViewHolder {
            return SliderMediumViewHolder(View.inflate(parent.context, ITEM_SLIDER_MEDIUM_LAYOUT, null))
        }

        override fun onBindViewHolder(holder: SliderMediumViewHolder, position: Int) {
            holder.bind(showcaseListItem[position])
        }

        override fun getItemCount(): Int {
            return showcaseListItem.size
        }
    }

    inner class SliderMediumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var showcaseItemImage: ImageUnify? = null
        private var showcaseItemName: Typography? = null

        init {
            showcaseItemImage = itemView.findViewById(R.id.img_showcase_item_slider_medium)
            showcaseItemName = itemView.findViewById(R.id.tv_showcase_name_item_slider_medium)
        }

        fun bind(element: ShopHomeShowcaseListItemUiModel) {
            // try catch to avoid crash ImageUnify on loading image with Glide
            try {
                if (showcaseItemImage?.context?.isValidGlideContext() == true) {
                    element.imageUrl.let { showcaseItemImage?.setImageUrl(it) }
                }
            } catch (e: Throwable) {
            }

            showcaseItemName?.text = element.name
        }
    }
}
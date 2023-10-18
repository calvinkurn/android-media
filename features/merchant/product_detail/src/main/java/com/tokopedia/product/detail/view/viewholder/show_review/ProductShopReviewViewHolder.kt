package com.tokopedia.product.detail.view.viewholder.show_review

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewUiModel
import com.tokopedia.product.detail.databinding.ItemDynamicShopReviewBinding
import com.tokopedia.product.detail.databinding.ShopReviewContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.isInflated
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.show_review.adapter.ShopReviewListItemAdapter
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductShopReviewViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductShopReviewDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_shop_review

        private val SPACE_BETWEEN_ITEM = 8.toPx()
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicShopReviewBinding.bind(view)
    }

    private val content by lazyThreadSafetyNone {
        val view = binding.stubShopReviewContent.inflate()
        ShopReviewContentBinding.bind(view)
    }

    private val adapter by lazyThreadSafetyNone {
        ShopReviewListItemAdapter(listener = listener)
    }

    init {
        initRecyclerView()
    }

    override fun bind(element: ProductShopReviewDataModel) {
        if (!element.shouldRender) {
            binding.showLoading()
        } else {
            binding.hideLoading()
            content.renderUI(element = element)
        }
    }

    private fun ItemDynamicShopReviewBinding.showLoading() {
        shopReviewHeaderShimmer.container.show()

        if (binding.stubShopReviewContent.isInflated()) {
            content.container.hide()
        }
    }

    private fun ItemDynamicShopReviewBinding.hideLoading() {
        shopReviewHeaderShimmer.container.hide()

        if (binding.stubShopReviewContent.isInflated()) {
            content.container.show()
        }
    }

    private fun ShopReviewContentBinding.renderUI(element: ProductShopReviewDataModel) {
        renderHeader(uiModel = element.data)
        renderItems(uiModel = element.data, trackDataModel = getComponentTrackData(element))
        setEventClick(uiModel = element.data, trackDataModel = getComponentTrackData(element))
        setImpression(element = element)
    }

    private fun ShopReviewContentBinding.renderHeader(uiModel: ProductShopReviewUiModel) {
        shopReviewListTitle.text = uiModel.title
        shopReviewListSeeMore.text = uiModel.appLinkTitle
    }

    private fun renderItems(uiModel: ProductShopReviewUiModel, trackDataModel: ComponentTrackDataModel) {
        adapter.submitList(uiModel.reviews, trackDataModel = trackDataModel)
    }

    private fun ShopReviewContentBinding.setEventClick(
        uiModel: ProductShopReviewUiModel,
        trackDataModel: ComponentTrackDataModel
    ) {
        if (uiModel.appLink.isNotBlank()) {
            shopReviewListSeeMore.setOnClickDebounceListener {
                listener.onShopReviewSeeMore(
                    appLink = uiModel.appLink,
                    eventLabel = "",
                    trackData = trackDataModel
                )
            }
        }
    }

    private fun ShopReviewContentBinding.setImpression(element: ProductShopReviewDataModel) {
        root.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun initRecyclerView() = with(content.shopReviewList) {
        adapter = this@ProductShopReviewViewHolder.adapter
        layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
        addItemDecoration(createSpaceBetweenItem())
    }

    private fun createSpaceBetweenItem() = object : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val marginEnd = if (parent.getChildAdapterPosition(view) < state.itemCount - Int.ONE) {
                SPACE_BETWEEN_ITEM
            } else {
                Int.ZERO
            }
            outRect.set(Int.ZERO, Int.ZERO, marginEnd, Int.ZERO)
        }
    }
}

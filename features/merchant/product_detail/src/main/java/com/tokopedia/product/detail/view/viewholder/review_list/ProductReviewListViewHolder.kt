package com.tokopedia.product.detail.view.viewholder.review_list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductReviewListDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductReviewListUiModel
import com.tokopedia.product.detail.databinding.ItemDynamicReviewListBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.review_list.adapter.ReviewListItemAdapter
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductReviewListViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductReviewListDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_review_list

        private val SPACE_BETWEEN_ITEM = 8.toPx()
    }

    private val binding by lazyThreadSafetyNone {
        ItemDynamicReviewListBinding.bind(view)
    }

    private val adapter by lazyThreadSafetyNone {
        ReviewListItemAdapter(listener = listener)
    }

    init {
        initRecyclerView()
    }

    override fun bind(element: ProductReviewListDataModel) = with(binding) {
        if (!element.shouldRender) {
            showLoading()
        } else {
            renderUI(element = element)
        }
    }

    private fun showLoading() {
    }

    private fun ItemDynamicReviewListBinding.renderUI(element: ProductReviewListDataModel) {
        renderHeader(uiModel = element.data)
        renderItems(uiModel = element.data, trackDataModel = getComponentTrackData(element))
        setEventClick(uiModel = element.data)
        setImpression(element = element)
    }

    private fun ItemDynamicReviewListBinding.renderHeader(uiModel: ProductReviewListUiModel) {
        reviewListTitle.text = uiModel.title
        reviewListSeeMore.text = uiModel.appLinkTitle
    }

    private fun renderItems(uiModel: ProductReviewListUiModel, trackDataModel: ComponentTrackDataModel) {
        adapter.submitList(uiModel.reviews, trackDataModel = trackDataModel)
    }

    private fun ItemDynamicReviewListBinding.setEventClick(uiModel: ProductReviewListUiModel) {
        if (uiModel.appLink.isNotBlank()) {
            reviewListSeeMore.setOnClickListener {
                listener.goToApplink(url = uiModel.appLink)
            }
        }
    }

    private fun ItemDynamicReviewListBinding.setImpression(element: ProductReviewListDataModel) {
        root.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun initRecyclerView() = with(binding.reviewList) {
        adapter = this@ProductReviewListViewHolder.adapter
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

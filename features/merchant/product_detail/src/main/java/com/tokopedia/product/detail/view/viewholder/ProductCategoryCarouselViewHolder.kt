package com.tokopedia.product.detail.view.viewholder

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CategoryCarousel
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCategoryCarouselDataModel
import com.tokopedia.product.detail.view.adapter.ProductCategoryCarouselAdapter
import com.tokopedia.product.detail.view.fragment.partialview.PartialContentView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 13/07/21
 */
class ProductCategoryCarouselViewHolder(view: View,
                                        private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductCategoryCarouselDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_category_carousel_view_holder
    }

    private var header: PartialContentView? = null
    private var categoryCarouselTitle: Typography? = itemView.findViewById(R.id.product_category_title)
    private var categoryCarouselLink: Typography? = itemView.findViewById(R.id.product_category_link)
    private var categoryCarouselRv: RecyclerView? = itemView.findViewById(R.id.product_category_rv)

    private val adapter = ProductCategoryCarouselAdapter(listener)
    private var componentTrackDataModel: ComponentTrackDataModel? = null

    init {
        header = PartialContentView(view, listener)
    }

    override fun bind(element: ProductCategoryCarouselDataModel) {
        if (componentTrackDataModel == null) {
            componentTrackDataModel = getComponentTrackData(element)
        }

        renderInitialView(element)
        renderCarousel(element.categoryList)
    }

    private fun renderCarousel(categoryList: List<CategoryCarousel>) {
        categoryCarouselRv?.let {
            adapter.setData(categoryList, componentTrackDataModel)
            if (it.itemDecorationCount == 0) {
                it.addItemDecoration(MarginLeftItemDecorator(it.context))
            }
            it.adapter = adapter
        }
    }

    private fun renderInitialView(element: ProductCategoryCarouselDataModel) {
        categoryCarouselTitle?.text = element.titleCarousel
        categoryCarouselLink?.shouldShowWithAction(element.linkText.isNotEmpty()) {
            categoryCarouselLink?.text = element.linkText
            categoryCarouselLink?.setOnClickListener {
                listener.onCategoryCarouselSeeAllClicked(element.applink, componentTrackDataModel)
            }
        }
    }


    private fun getComponentTrackData(element: ProductCategoryCarouselDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}

class MarginLeftItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val leftMargin: Int = context.resources.getDimension(R.dimen.pdp_dp_16).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (position == 0) {
            outRect.left = leftMargin
        }
    }
}
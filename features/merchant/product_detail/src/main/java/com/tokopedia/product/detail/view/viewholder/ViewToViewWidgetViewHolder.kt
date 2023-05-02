package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ViewToViewWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewItemData
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewWidgetView

class ViewToViewWidgetViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener,
) : AbstractViewHolder<ViewToViewWidgetDataModel>(view),
    ViewToViewWidgetBasicListener {

    private var viewToView: ViewToViewWidgetDataModel? = null

    private val viewToViewWidget: ViewToViewWidgetView =
        itemView.findViewById(R.id.widget_view_to_view)

    private fun ViewToViewWidgetDataModel.shouldShowLoading() : Boolean {
        return recomWidgetData == null || recomWidgetData?.recommendationItemList?.isEmpty() == true || isLoading
    }

    override fun bind(element: ViewToViewWidgetDataModel) {
        viewToView = element
        itemView.visible()
        when {
            element.isFailed -> {
                viewToViewWidget.setBasicListener(this)
                viewToViewWidget.setPageName(element.name)
                viewToViewWidget.showFailedState(itemView.context.getString(R.string.title_other_toppers))
            }
            element.shouldShowLoading() -> {
                viewToViewWidget.showLoading()
            }
            else -> {
                element.recomWidgetData?.let {
                    viewToViewWidget.bind(
                        carouselData = RecommendationCarouselData(
                            recommendationData = it,
                            state = RecommendationCarouselData.STATE_READY
                        ),
                        adapterPosition = bindingAdapterPosition,
                        basicListener = this,
                    )
                }
            }
        }
    }

    override fun onViewToViewItemImpressed(
        data: ViewToViewItemData,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        val title = viewToView?.recomWidgetData?.title.orEmpty()
        listener.onViewToViewImpressed(data, title, itemPosition, adapterPosition)
    }

    override fun onViewToViewItemClicked(
        data: ViewToViewItemData,
        itemPosition: Int,
        adapterPosition: Int,
    ) {
        val title = viewToView?.recomWidgetData?.title.orEmpty()
        listener.onViewToViewClicked(data, title, itemPosition, adapterPosition)
    }

    override fun onViewToViewReload(pageName: String) {
        viewToView?.let {
            it.state = RecommendationCarouselData.STATE_LOADING
            bind(it)
        }
        listener.onViewToViewReload(pageName)
    }

    override fun onWidgetFail(pageName: String, e: Throwable) {
    }

    override fun onShowError(pageName: String, e: Throwable) {
    }

    companion object {
        val LAYOUT = R.layout.item_dynamic_widget_view_to_view
    }
}

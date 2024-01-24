package com.tokopedia.product.detail.view.adapter.dynamicadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomWidgetDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationVerticalPlaceholderDataModel
import com.tokopedia.product.detail.data.model.datamodel.ViewToViewWidgetDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ContentWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductMediaViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecomWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationVerticalPlaceholderViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationVerticalViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationViewHolder
import com.tokopedia.product.detail.view.viewholder.ViewToViewWidgetViewHolder
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageUiModel

/**
 * Created by Yehezkiel on 04/01/21
 */
class ProductDetailAdapter(
    asyncDifferConfig: AsyncDifferConfig<DynamicPdpDataModel>,
    private val listener: DynamicProductDetailListener?,
    private val adapterTypeFactory: DynamicProductDetailAdapterFactory
) :
    ListAdapter<DynamicPdpDataModel, AbstractViewHolder<*>>(asyncDifferConfig) {

    var shouldRedrawLayout: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        determineFullSpan(holder)
        bind(holder as AbstractViewHolder<DynamicPdpDataModel>, getItem(position))
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            bind(holder as AbstractViewHolder<DynamicPdpDataModel>, getItem(position), payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    private fun determineFullSpan(holder: AbstractViewHolder<*>) {
        val isFullSpan = holder !is ProductRecommendationVerticalViewHolder
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)
            ?.isFullSpan = isFullSpan
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else {
            currentList[position]?.type(adapterTypeFactory) ?: HideViewHolder.LAYOUT
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)

        val currentPosition = holder.bindingAdapterPosition
        val dataModel = currentList[currentPosition]

        if (holder is ProductRecommendationViewHolder &&
            currentPosition < currentList.size &&
            (dataModel as? ProductRecommendationDataModel)?.recomWidgetData == null
        ) {
            val recommData = dataModel as? ProductRecommendationDataModel
            listener?.loadTopads(
                pageName = recommData?.name.orEmpty(),
                queryParam = recommData?.queryParam.orEmpty(),
                thematicId = recommData?.thematicId.orEmpty()
            )
        }

        if (holder is ProductRecomWidgetViewHolder &&
            currentPosition < currentList.size &&
            (dataModel as? ProductRecomWidgetDataModel)?.recomWidgetData == null
        ) {
            val recommData = dataModel as? ProductRecomWidgetDataModel
            listener?.loadTopads(
                pageName = recommData?.name.orEmpty(),
                queryParam = recommData?.queryParam.orEmpty(),
                thematicId = recommData?.thematicId.orEmpty()
            )
        }

        if (holder is ViewToViewWidgetViewHolder &&
            currentPosition < currentList.size &&
            (dataModel as? ViewToViewWidgetDataModel)?.recomWidgetData == null
        ) {
            val recommData = dataModel as? ViewToViewWidgetDataModel
            listener?.loadViewToView(
                pageName = recommData?.name.orEmpty(),
                queryParam = recommData?.queryParam.orEmpty(),
                thematicId = recommData?.thematicId.orEmpty()
            )
        }

        if (holder is ContentWidgetViewHolder &&
            currentPosition < currentList.size &&
            (dataModel as? ContentWidgetDataModel)?.playWidgetState?.isLoading == true
        ) {
            listener?.loadPlayWidget()
        }

        if (holder is ProductRecommendationVerticalPlaceholderViewHolder) {
            if (currentPosition < currentList.size &&
                (dataModel as? ProductRecommendationVerticalPlaceholderDataModel)?.recomWidgetData == null
            ) {
                val recommData = dataModel as? ProductRecommendationVerticalPlaceholderDataModel
                listener?.startVerticalRecommendation(
                    pageName = recommData?.name().orEmpty(),
                    queryParam = recommData?.queryParam.orEmpty(),
                    thematicId = recommData?.thematicId.orEmpty()
                )
            }
            shouldRedrawLayout = true
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<*>) {
        super.onViewDetachedFromWindow(holder)
        when (holder) {
            is ProductMediaViewHolder -> holder.detachView()
            is ProductRecommendationVerticalPlaceholderViewHolder -> shouldRedrawLayout = false
        }
    }

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun bind(holder: AbstractViewHolder<DynamicPdpDataModel>, item: DynamicPdpDataModel) {
        holder.bind(item)
    }

    fun bind(holder: AbstractViewHolder<DynamicPdpDataModel>, item: DynamicPdpDataModel, payloads: MutableList<Any>) {
        val payloadInt = (payloads.firstOrNull() as? Bundle)?.getInt(ProductDetailConstant.DIFFUTIL_PAYLOAD)
        if (payloads.isNotEmpty() && payloads.firstOrNull() != null && payloadInt != null) {
            holder.bind(item, listOf(payloadInt))
        } else {
            holder.bind(item)
        }
    }

    fun showLoading() {
        if (!isLoading()) {
            submitList(listOf(ProductLoadingDataModel()))
        }
    }

    fun showError(data: PageErrorDataModel) {
        submitList(listOf(data))
    }

    fun getComponentPositionByName(componentName: String): Int {
        return currentList.indexOfFirst { it.name() == componentName }
    }

    private fun isLoading(): Boolean {
        val lastIndex = if (currentList.size == 0) -1 else currentList.size - 1
        return if (lastIndex > -1) {
            currentList[lastIndex] is LoadingModel ||
                currentList[lastIndex] is LoadingMoreModel ||
                currentList[lastIndex] is ProductLoadingDataModel
        } else {
            false
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<DynamicPdpDataModel>,
        currentList: MutableList<DynamicPdpDataModel>
    ) {
        listener?.updateNavigationTabPosition()
        super.onCurrentListChanged(previousList, currentList)
    }

    fun getSeeMoreAPlusTogglePosition(): Int {
        return currentList.indexOfFirst {
            it is APlusImageUiModel && it.showOnCollapsed && it.ctaText.isNotBlank()
        }
    }
}

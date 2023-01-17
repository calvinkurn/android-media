package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoadMore
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoading
import com.tokopedia.dilayanitokopedia.home.presentation.factory.HomeRecommendationTypeFactory
import com.tokopedia.dilayanitokopedia.home.presentation.factory.HomeRecommendationTypeFactoryImpl
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeRecommendationVisitable
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.smart_recycler_helper.SmartRecyclerAdapter
import com.tokopedia.smart_recycler_helper.SmartVisitable

class HomeRecommendationForYouAdapter(
    smartExecutors: SmartExecutors,
    private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl,
    private val listener: HomeRecommendationListener
) : SmartRecyclerAdapter<HomeRecommendationVisitable, HomeRecommendationTypeFactory>(
    appExecutors = smartExecutors,
    adapterTypeFactory = adapterTypeFactory,
    diffCallback = object : DiffUtil.ItemCallback<HomeRecommendationVisitable>() {

        override fun getChangePayload(oldItem: HomeRecommendationVisitable, newItem: HomeRecommendationVisitable): Any? {
            return oldItem.getChangePayloadFrom(newItem)
        }

        override fun areItemsTheSame(oldItem: HomeRecommendationVisitable, newItem: HomeRecommendationVisitable): Boolean {
            return oldItem.getUniqueIdentity() == newItem.getUniqueIdentity()
        }

        override fun areContentsTheSame(oldItem: HomeRecommendationVisitable, newItem: HomeRecommendationVisitable): Boolean {
            return oldItem.equalsDataModel(newItem)
        }
    }
) {

    /**
     * This override void from [BaseListAdapter]
     * It handling binding viewHolder
     * @param holder the viewHolder on bind
     * @param item item visitable
     */
    override fun bind(holder: SmartAbstractViewHolder<SmartVisitable<*>>, item: HomeRecommendationVisitable) {
        adjustWithWidgets(holder, item)
        holder.bind(item, listener)
    }

    /*
    provide full width for necessary widget
     */
    private fun adjustWithWidgets(holder: SmartAbstractViewHolder<SmartVisitable<*>>, item: HomeRecommendationVisitable) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layout.isFullSpan = true
        when (item) {
            is HomeRecommendationLoading -> layout.isFullSpan = true
            is HomeRecommendationError -> layout.isFullSpan = true
            is HomeRecommendationLoadMore -> layout.isFullSpan = true
            is HomeRecommendationEmpty -> layout.isFullSpan = true

            else -> layout.isFullSpan = false
        }
    }

    override fun bind(
        holder: SmartAbstractViewHolder<SmartVisitable<*>>,
        item: HomeRecommendationVisitable,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(item, listener, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}

interface HomeRecommendationListener : SmartListener {
    fun onProductImpression(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
    fun onProductClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
    fun onProductThreeDotsClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
    fun onBannerImpression(bannerRecommendationDataModel: BannerRecommendationDataModel)
    fun onBannerTopAdsClick(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        position: Int
    )

    fun onBannerTopAdsImpress(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        position: Int
    )

    fun onRetryGetProductRecommendationData()
}

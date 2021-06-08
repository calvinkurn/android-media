package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.smart_recycler_helper.*

class HomeRecommendationAdapter(
        smartExecutors: SmartExecutors,
        private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl,
        private val listener: HomeRecommendationListener
) : SmartRecyclerAdapter<HomeRecommendationVisitable, HomeRecommendationTypeFactory>(
        appExecutors = smartExecutors,
        adapterTypeFactory = adapterTypeFactory,
        diffCallback = object : DiffUtil.ItemCallback<HomeRecommendationVisitable>(){
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
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when(item){
            is HomeRecommendationLoading,
            is HomeRecommendationError,
            is HomeRecommendationEmpty,
            is HomeRecommendationLoadMore,
            is HomeRecommendationBannerTopAdsDataModel -> layout.isFullSpan = true
        }
        holder.bind(item, listener)
    }

    override fun bind(holder: SmartAbstractViewHolder<SmartVisitable<*>>, item: HomeRecommendationVisitable, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.bind(item, listener, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}

interface HomeRecommendationListener: SmartListener {
    fun onProductImpression(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
    fun onProductClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
    fun onProductThreeDotsClick(homeRecommendationItemDataModel: HomeRecommendationItemDataModel, position: Int)
    fun onBannerImpression(bannerRecommendationDataModel: BannerRecommendationDataModel)
    fun onBannerTopAdsClick(homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel, position: Int)
    fun onBannerTopAdsImpress(homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel, position: Int)
    fun onRetryGetProductRecommendationData()
    fun onProductWithPmProImpressed(pmProView: View?, position: Int)
}
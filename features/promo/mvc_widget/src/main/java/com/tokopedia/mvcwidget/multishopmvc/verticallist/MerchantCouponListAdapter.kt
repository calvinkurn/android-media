package com.tokopedia.mvcwidget.multishopmvc.verticallist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.multishopmvc.MvcMultiShopView
import com.tokopedia.mvcwidget.multishopmvc.data.*
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class MerchantCouponListAdapter(
    val viewmodel: MerchantCouponViewModel,
    callback: AdapterCallback,
    var adImpression: HashSet<String?>,
    var source: Int
) : BaseAdapter<CatalogMVCWithProductsListItem>(callback) {
    private var mRecyclerView: RecyclerView? = null
    private val eventSet = HashSet<String?>()

    inner class CouponListViewHolder(view: View) : BaseVH(view) {
        var multiShopView: MvcMultiShopView = view.findViewById(R.id.mvc_multishop)

        override fun bindView(item: CatalogMVCWithProductsListItem?, position: Int) {
            setData(this, DataMapperMultiShopView.map(item))
        }
    }

    override fun getItemViewHolder(
        parent: ViewGroup?,
        inflater: LayoutInflater?,
        viewType: Int
    ): BaseVH {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.mvc_verticallist_item, parent, false)

        return CouponListViewHolder(itemView)
    }

    private fun setData(vh: CouponListViewHolder, item: MultiShopModel) {
        vh.multiShopView.setMultiShopModel(item,source)
    }

    override fun loadData(currentPageIndex: Int,vararg args: String?) {
        super.loadData(currentPageIndex, *args)
        viewmodel.merchantCouponData(currentPageIndex)
    }

    fun onSuccess(data: Productlist) {
        data.catalogMVCWithProductsList?.let { loadCompleted(it, data) }
        isLastPage = !data.tokopointsPaging?.hasNext!!
    }

    fun onError() {
        loadCompletedWithError()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    private fun sendTopadsImpression(context: Context, adInfo: AdInfo?) {
        if (!eventSet.contains(adInfo?.AdID) && !adImpression.contains(adInfo?.AdID)) {
            eventSet.add(adInfo?.AdID)
            TopAdsUrlHitter(context).hitImpressionUrl(
                className,
                adInfo?.AdViewUrl,
                "",
                "",
                ""
            )
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is CouponListViewHolder) {
            val data = items[holder.getAdapterPosition()] ?: return

            val item: MutableMap<String, Any?> = HashMap()
            val (shopInfo, _, title, _, _) = data
            val eventLabel = "mvc - {${holder.adapterPosition + 1}} - ${shopInfo?.name}"
            item["item_name"] = shopInfo?.name
            item["position"] = (holder.adapterPosition + 1).toString()
            item["creative_name"] = title
            item["item_id"] = shopInfo?.id
            val promotions = HashMap<String, Any>()
            promotions["promotions"] = listOf<Map<String, Any?>>(item)
            sendTopadsImpression(holder.itemView.context, items[holder.adapterPosition].AdInfo)
            MvcTracker().viewMVCCoupon(eventLabel, promotions ,source)
        }
    }

    companion object {
        const val className = "com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponListAdapter"
    }
}

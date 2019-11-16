package com.tokopedia.home_wishlist.view.viewholder

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.util.GravitySnapHelper
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.view.adapter.RecommendationCarouselAdapter
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import androidx.recyclerview.widget.DefaultItemAnimator
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.smart_recycler_helper.SmartListener


class RecommendationCarouselViewHolder(view: View, private val appExecutors: SmartExecutors) : SmartAbstractViewHolder<RecommendationCarouselDataModel>(view) {
    private val viewPool = RecyclerView.RecycledViewPool()
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }
    private val disabledView: View by lazy { view.findViewById<View>(R.id.disabled_view) }
    private var clickedItem: Int = -1

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener) {
        title.text = element.title
        disabledView.visibility = if(element.isOnBulkRemoveProgress) View.VISIBLE else View.GONE
        seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener { RouteManager.route(itemView.context, element.seeMoreAppLink) }
        setupRecyclerView(element, listener)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel, listener: SmartListener){
        recyclerView.apply {
            if(adapter == null) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                itemAnimator = DefaultItemAnimator()
                GravitySnapHelper(Gravity.START).attachToRecyclerView(this)
//                setRecycledViewPool(viewPool)
//                isNestedScrollingEnabled = false
//                setHasFixedSize(true)
                adapter = RecommendationCarouselAdapter(listener as WishlistListener, appExecutors)
            }
            (recyclerView.adapter as RecommendationCarouselAdapter).updateList(dataModel.list)
//            if(clickedItem != -1) {
//                recyclerView.layoutManager?.scrollToPosition(clickedItem - 1)
//                clickedItem = -1
//            }
        }

    }

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener, payloads: List<Any>) {
        if(payloads.isNotEmpty()){
            val bundle = payloads[0] as Bundle
            if(bundle.containsKey("isOnBulkRemoveProgress")){
                disabledView.visibility = if(bundle.getBoolean("isOnBulkRemoveProgress")) View.VISIBLE else View.GONE
            }

            if(bundle.containsKey("updateList")){
                val index = bundle.getInt("updateList")
                val isWishlist = bundle.getBoolean("updateIsWishlist")
                if(recyclerView.adapter != null){
                    (recyclerView.adapter as RecommendationCarouselAdapter).updateWishlist(index, isWishlist)
                }
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel
    }
}
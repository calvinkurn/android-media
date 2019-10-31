package com.tokopedia.home_wishlist.view.viewholder

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartExecutors
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.WishlistDataModel
import com.tokopedia.home_wishlist.view.adapter.RecommendationCarouselAdapter
import com.tokopedia.home_wishlist.view.listener.WishlistListener


class RecommendationCarouselViewHolder(view: View, private val appExecutors: SmartExecutors) : SmartAbstractViewHolder<RecommendationCarouselDataModel>(view) {
    private val viewPool = RecyclerView.RecycledViewPool()
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }
    private val disabledView: View by lazy { view.findViewById<View>(R.id.disabled_view) }

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener) {
        title.text = element.title
        disabledView.visibility = if(element.isBulkMode) View.VISIBLE else View.GONE
        seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener { RouteManager.route(itemView.context, element.seeMoreAppLink) }
        setupRecyclerView(element, listener)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel, listener: SmartListener){
        recyclerView.apply {
            if(adapter == null) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecommendationCarouselAdapter(
                        appExecutors,
                        object : WishlistListener{
                            override fun onProductImpression(dataModel: WishlistDataModel) {}

                            override fun onProductClick(dataModel: WishlistDataModel, position: Int) {
                                (listener as WishlistListener).onProductClick(dataModel, position)
                            }

                            override fun onDeleteClick(dataModel: WishlistDataModel, adapterPosition: Int) {}

                            override fun onAddToCartClick(dataModel: WishlistDataModel, adapterPosition: Int) {}

                            override fun onWishlistClick(parentPosition: Int, childPosition: Int) {
                                val recyclerViewState = layoutManager?.onSaveInstanceState()
                                (listener as WishlistListener).onWishlistClick(parentPosition, childPosition)
                                layoutManager?.onRestoreInstanceState(recyclerViewState)
                            }

                            override fun onClickCheckboxDeleteWishlist(position: Int, isChecked: Boolean) {}

                            override fun onTryAgainClick() {}
                        })
                GravitySnapHelper(Gravity.START).attachToRecyclerView(this)
                setRecycledViewPool(viewPool)
            }

            (recyclerView.adapter as RecommendationCarouselAdapter).submitList(dataModel.list)

        }

    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel
    }
}
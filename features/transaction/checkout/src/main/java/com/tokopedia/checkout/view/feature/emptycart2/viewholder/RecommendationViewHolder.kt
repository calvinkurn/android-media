package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.adapter.RecommendationAdapter
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecommendationUiModel
import kotlinx.android.synthetic.main.item_checkout_product_recommendation.view.*


/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecommendationViewHolder(val view: View, val listener: ActionListener, val itemWidth: Int) : AbstractViewHolder<RecommendationUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_checkout_product_recommendation
    }

    override fun bind(element: RecommendationUiModel) {
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(element: RecommendationUiModel) {
        val recommendationAdapter = RecommendationAdapter(listener, itemWidth)
        recommendationAdapter.setData(element.recommendationItems)
        val gridLayoutManager = GridLayoutManager(itemView.context, 2)
        itemView.rv_recommendation.layoutManager = gridLayoutManager
        itemView.rv_recommendation.adapter = recommendationAdapter

        itemView.rv_recommendation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

//                if (!recyclerView.canScrollVertically(1)) {
//                    Toast.makeText(this@YourActivity, "Last", Toast.LENGTH_LONG).show()
                    Log.e("InnerOnScrollStateChd", newState.toString());
//                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                if (!recyclerView.canScrollVertically(1)) {
//                    Toast.makeText(this@YourActivity, "Last", Toast.LENGTH_LONG).show()
                    Log.e("InnerOnScrolled", "Dx:$dx | Dy:$dy");
//                }

            }
        })
    }

}
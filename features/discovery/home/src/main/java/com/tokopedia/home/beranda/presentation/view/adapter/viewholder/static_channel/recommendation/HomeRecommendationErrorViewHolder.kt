package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationError
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationErrorViewHolder (view: View) : SmartAbstractViewHolder<HomeRecommendationError>(view){
    private val globalError: GlobalError? = itemView.findViewById(R.id.global_unify_error)

    override fun bind(element: HomeRecommendationError, listener: SmartListener) {
        globalError?.setType(GlobalError.NO_CONNECTION)
        globalError?.setActionClickListener { (listener as HomeRecommendationListener).onRetryGetProductRecommendationData() }
    }

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.item_home_recommendation_error_layout
    }
}

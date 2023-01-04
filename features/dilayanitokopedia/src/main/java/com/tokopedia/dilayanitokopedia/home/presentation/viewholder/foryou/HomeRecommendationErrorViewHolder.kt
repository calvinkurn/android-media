package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationErrorViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationError>(view) {
//    private val globalError: GlobalError? = itemView.findViewById(R.id.global_unify_error)

    override fun bind(element: HomeRecommendationError, listener: SmartListener) {
//        globalError?.setType(GlobalError.NO_CONNECTION)
//        globalError?.setActionClickListener { (listener as HomeRecommendationListener).onRetryGetProductRecommendationData() }
    }

    companion object {
        //        val LAYOUT = R.layout.item_home_recommendation_error_layout
        @LayoutRes
        val LAYOUT = R.layout.loading_layout
    }
}

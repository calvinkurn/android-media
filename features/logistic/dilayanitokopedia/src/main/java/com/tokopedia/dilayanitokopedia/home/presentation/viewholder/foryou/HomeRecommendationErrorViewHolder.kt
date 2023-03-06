package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationErrorViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationError>(view) {
    private val globalError: GlobalError? = itemView.findViewById(R.id.global_unify_error)

    override fun bind(element: HomeRecommendationError, listener: SmartListener) {
        globalError?.setType(GlobalError.NO_CONNECTION)
        globalError?.setActionClickListener { (listener as HomeRecommendationListener).onRetryGetProductRecommendationData() }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_dt_home_recommendation_error_layout
    }
}

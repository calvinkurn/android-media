package com.tokopedia.search.result.presentation.view.adapter.viewholder.profile

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProfileRecommendationTitleViewModel

class ProfileRecommendationTitleViewHolder(view: View): AbstractViewHolder<ProfileRecommendationTitleViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_profile_empty_recommendation_title_layout
    }

    override fun bind(element: ProfileRecommendationTitleViewModel?) {

    }
}
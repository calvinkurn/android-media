package com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify

class EmptyStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val image: ImageUnify = view.findViewById(R.id.emptyStateImage)

    fun bind(imgUrl: String) {
        image.urlSrc = imgUrl
    }
}

package com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton


class FailedStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val image: ImageUnify = view.findViewById(R.id.emptyStateImage)
    private val reloadCta: UnifyButton = view.findViewById(R.id.emptyStateCta)

    fun bind(imgUrl: String, reloadPage: (() -> Unit)?) {
        image.urlSrc = imgUrl
        reloadCta.setOnClickListener {
            reloadPage?.invoke()
        }
    }
}

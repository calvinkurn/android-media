package com.tokopedia.home_recom.view.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by Lukas on 31/08/19
 */

class SimilarProductLoadMoreViewHolder(itemView: View) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    override fun bind(element: LoadingMoreModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.loading_layout
    }

}

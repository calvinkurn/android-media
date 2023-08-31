package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyModel
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by Pika on 7/6/20.
 */
class MovetoGroupEmptyViewHolder(val view: View) :
    MovetoGroupViewHolder<MovetoGroupEmptyModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_no_search_result
    }

    override fun bind(
        item: MovetoGroupEmptyModel, lastSelected: Int, countList: MutableList<CountDataItem>,
    ) {
        view.findViewById<ImageUnify>(R.id.image_empty)
            .setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
    }

}

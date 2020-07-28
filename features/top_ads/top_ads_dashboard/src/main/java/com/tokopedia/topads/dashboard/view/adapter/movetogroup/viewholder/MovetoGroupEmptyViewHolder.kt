package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewmodel.MovetoGroupEmptyViewModel

/**
 * Created by Pika on 7/6/20.
 */
class MovetoGroupEmptyViewHolder(view: View) : MovetoGroupViewHolder<MovetoGroupEmptyViewModel>(view){

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_no_search_result
    }

}
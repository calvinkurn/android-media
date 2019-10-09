package com.tokopedia.feedcomponent.helper

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jegul on 2019-10-09.
 */
abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onViewRecycled()
}
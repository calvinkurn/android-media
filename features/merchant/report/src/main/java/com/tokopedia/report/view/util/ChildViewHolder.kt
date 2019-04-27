package com.tokopedia.report.view.util

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class ChildViewHolder<C: Any>(val view: View): RecyclerView.ViewHolder(view){
    var recyclerAdapter: ExpandableRecyclerViewAdapter<*, C, *, *>? = null
    var child: C? = null

    fun getParentAdapterPosition(): Int{
        val flatPosition = adapterPosition
        return  if (flatPosition == RecyclerView.NO_POSITION) RecyclerView.NO_POSITION
                else recyclerAdapter?.getNearestParentPosition(flatPosition) ?: RecyclerView.NO_POSITION
    }

    fun getChildAdapterPosition(): Int {
        val flatPosition = adapterPosition

        return  if (flatPosition == RecyclerView.NO_POSITION) RecyclerView.NO_POSITION
                else recyclerAdapter?.getChildPosition(flatPosition) ?: RecyclerView.NO_POSITION
    }
}
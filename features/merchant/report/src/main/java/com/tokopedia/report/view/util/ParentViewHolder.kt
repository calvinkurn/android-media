package com.tokopedia.report.view.util

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class ParentViewHolder<P : ParentItem<C>, C: Any>(view: View): RecyclerView.ViewHolder(view),
    View.OnClickListener {

    var isExpanded = false
    var parent: P? = null
    var onExpandCollapseListener: ParentOnExpandCollapseListener? = null
    var recyclerAdapter: ExpandableRecyclerViewAdapter<P, C, *, *>? = null


    interface ParentOnExpandCollapseListener {
        fun onParentExpanded(flatPos : Int)
        fun onParentCollapsed(flatPos: Int)
    }

    init {
        itemView.setOnClickListener(this)
    }

    fun getParentAdapterPosition(): Int {
        val flatPosition = adapterPosition
        return if (flatPosition == RecyclerView.NO_POSITION) flatPosition
        else recyclerAdapter?.getNearestParentPosition(flatPosition) ?: flatPosition
    }

    fun expandView(){
        isExpanded = true
        onExpandCollapseListener?.onParentExpanded(adapterPosition)
    }

    fun collapseView(){
        isExpanded = false
        onExpandCollapseListener?.onParentCollapsed(adapterPosition)
    }

    fun shouldAbleToClick(): Boolean = true

    override fun onClick(p0: View?) {
        if (shouldAbleToClick()){
            if (isExpanded){
                collapseView()
            } else {
                expandView()
            }
        }
    }
}
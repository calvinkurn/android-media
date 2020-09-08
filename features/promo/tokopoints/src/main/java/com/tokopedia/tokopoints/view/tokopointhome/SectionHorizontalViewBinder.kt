package com.tokopedia.tokopoints.view.tokopointhome

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent

class SectionHorizontalViewBinder( var recyclerViewManagerState: Parcelable? = null)
    : SectionItemViewBinder<SectionContent, SectionHorizontalViewHolder>(
        SectionContent::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionHorizontalViewHolder {
        return SectionHorizontalViewHolder(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionHorizontalViewHolder) {
        viewHolder.bind(model,recyclerViewManagerState)
    }

    override fun getSectionItemType() : Int= R.layout.tp_layout_generic_carousal

    override fun onViewRecycled(viewHolder: SectionHorizontalViewHolder) {
        saveInstanceState(viewHolder)
    }

    override fun onViewDetachedFromWindow(viewHolder: SectionHorizontalViewHolder) {
        saveInstanceState(viewHolder)
    }

    // Saving the present instance of the recyclerview every time before the nested recyclerview is detached or the view get recycled
    fun saveInstanceState(viewHolder: SectionHorizontalViewHolder) {
        if (viewHolder.adapterPosition == RecyclerView.NO_POSITION) {
            return
        }
        recyclerViewManagerState = viewHolder.getLayoutManagerState()
    }

}

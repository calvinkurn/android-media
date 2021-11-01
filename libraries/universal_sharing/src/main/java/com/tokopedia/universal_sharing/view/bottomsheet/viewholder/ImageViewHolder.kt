package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.universal_sharing.R

class ImageViewHolder(itemView: View, updateSelectedPosition: (m: Int, view:View) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.item_view
    }

    var imageView : View = itemView
    var selectedViewGroup : Group
    var image : ImageUnify
    var updatePosFunction : (m: Int, view: View) -> Unit = updateSelectedPosition

    init {
        imageView.setOnClickListener(this)
        selectedViewGroup = imageView.findViewById(R.id.selected_view_group)
        image = imageView.findViewById(R.id.image)
    }

    override fun onClick(v: View?) {
        v?.let { updatePosFunction(layoutPosition, it.findViewById(R.id.image)) }
    }
}
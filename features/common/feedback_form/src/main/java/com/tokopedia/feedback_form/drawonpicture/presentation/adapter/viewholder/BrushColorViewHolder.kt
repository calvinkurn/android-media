package com.tokopedia.feedback_form.drawonpicture.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedback_form.R
import com.tokopedia.feedback_form.drawonpicture.presentation.Utils
import kotlinx.android.synthetic.main.item_brush_color.view.*

/**
 * @author by furqan on 01/10/2020
 */
class BrushColorViewHolder(
        private val listener: Listener,
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(color: String, selectedColor: String) {
        Utils.changeShapeColor(itemView.brushColorOption.background, color)

        if (color == selectedColor) {
            itemView.ivBrushColorCheck.visibility = View.VISIBLE
        } else {
            itemView.ivBrushColorCheck.visibility = View.GONE
        }

        itemView.setOnClickListener {
            listener.onItemClicked(color)
        }
    }

    interface Listener {
        fun onItemClicked(color: String)
    }

    companion object {
        val LAYOUT = R.layout.item_brush_color
    }

}
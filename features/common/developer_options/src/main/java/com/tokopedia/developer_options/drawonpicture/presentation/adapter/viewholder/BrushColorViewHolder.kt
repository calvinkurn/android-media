package com.tokopedia.developer_options.drawonpicture.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import kotlinx.android.synthetic.main.item_brush_color.view.*

/**
 * @author by furqan on 01/10/2020
 */
class BrushColorViewHolder(
        private val listener: Listener,
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(color: String, selectedColor: String) {
        changeShapeColor(itemView.brushColorOption.background, color)

        if (color == selectedColor) {
            itemView.ivBrushColorCheck.visibility = View.VISIBLE
        } else {
            itemView.ivBrushColorCheck.visibility = View.GONE
        }

        itemView.setOnClickListener {
            listener.onItemClicked(color)
        }
    }

    private fun changeShapeColor(background: Drawable, color: String) {
        when (background) {
            is ShapeDrawable -> {
                background.paint.color = Color.parseColor(color)
            }
            is GradientDrawable -> {
                background.setColor(Color.parseColor(color))
            }
            is ColorDrawable -> {
                background.color = Color.parseColor(color)
            }
        }
    }

    interface Listener {
        fun onItemClicked(color: String)
    }

    companion object {
        val LAYOUT = R.layout.item_brush_color
    }

}
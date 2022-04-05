package com.tokopedia.home_component.viewholders.adapter

import android.content.Context
import android.graphics.Outline
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutCueCategoryBinding
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.*
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CueWidgetCategoryAdapter(channels: ChannelModel ) :
    RecyclerView.Adapter<CueWidgetCategoryAdapter.CueWidgetCategoryItemViewHolder>() {
    private var grids: List<ChannelGrid> = channels.channelGrids



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CueWidgetCategoryItemViewHolder {
        val layout = R.layout.layout_cue_category
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CueWidgetCategoryItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (grids.size < 6) 4 else 6
    }

    override fun onBindViewHolder(holder: CueWidgetCategoryItemViewHolder, position: Int) {
        val grid = grids[position]
        holder.binding?.run {
            imageCategory.loadImageNormal(
                grid.imageUrl,
                com.tokopedia.home_component.R.drawable.placeholder_grey
            )
            val curveRadius = 20f
            imageCategory.outlineProvider = object : ViewOutlineProvider() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View, outline: Outline?) {
                    outline?.setRoundRect(
                        0,
                        0,
                        view.width,
                        (view.height + curveRadius).toInt(),
                        curveRadius
                    )
                }
            }
            imageCategory.clipToOutline = true
            categoryName.text = grid.name
        }
    }

    class CueWidgetCategoryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: LayoutCueCategoryBinding? by viewBinding()
        val context: Context
            get() = itemView.context
    }
}


package com.tokopedia.home_component.viewholders.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalDcCueCategoryBinding
import com.tokopedia.home_component.databinding.LayoutCueCategoryBinding
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.home_component.visitable.CueCategoryDataModel
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.Lego4AutoItem
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.HashMap

/**
 * Created by dhaba
 */
class CueWidgetCategoryAdapter(private val channels: ChannelModel ) :
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
        return grids.size
    }

    override fun onBindViewHolder(holder: CueWidgetCategoryItemViewHolder, position: Int) {
        val grid = grids[position]
        holder.binding?.run {
            imageCategory.loadImageWithoutPlaceholder(grid.imageUrl)
            categoryName.text = grid.name
        }
    }

    class CueWidgetCategoryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: LayoutCueCategoryBinding? by viewBinding()
        val context: Context
            get() = itemView.context
    }
}


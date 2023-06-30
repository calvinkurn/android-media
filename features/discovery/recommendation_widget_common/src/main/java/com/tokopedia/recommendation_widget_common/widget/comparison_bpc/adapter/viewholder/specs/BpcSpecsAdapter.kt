package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder.specs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemBpcSpecBinding
import kotlin.math.abs

/**
 * Created by Frenzel
 */
class BpcSpecsAdapter(var listModel: BpcSpecsListModel) : RecyclerView.Adapter<BpcSpecsItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BpcSpecsItemViewHolder {
        val view = ItemBpcSpecBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        setupBpcSpecsLayout(parent, viewType, view)
        return BpcSpecsItemViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return listModel.specs.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BpcSpecsItemViewHolder, position: Int) {
        if (position < listModel.specs.size) {
            holder.bind(listModel.specs[position])
        }
    }

    private fun setupBpcSpecsLayout(parent: ViewGroup, viewType: Int, view: ItemBpcSpecBinding) {
        if (viewType != -1 && viewType < listModel.specs.size) {
            val layoutParams = view.root.layoutParams
            layoutParams.height = listModel.specsConfig.heightPositionMap[viewType] ?: 0
            view.root.layoutParams = layoutParams

            view.viewDivider.visibility = if (viewType == 0) View.INVISIBLE else View.VISIBLE
            val firstSpecTranslationY = abs(parent.context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_start))
            val firstSpecExtraMarginTop = abs(parent.context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_first_specs_extra_margin_top))
            val defaultTopPadding = parent.context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_top)
            val topPadding = if (viewType == 0) abs(firstSpecTranslationY) + firstSpecExtraMarginTop + defaultTopPadding else defaultTopPadding
            view.specLayout.setPadding(
                parent.context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_start),
                topPadding,
                parent.context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_end),
                parent.context.resources.getDimensionPixelSize(R.dimen.comparison_bpc_specs_margin_bottom)
            )
        }
    }
}

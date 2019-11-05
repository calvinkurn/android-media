package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible

/**
 * Created by jegul on 2019-09-09.
 */
class CuratedProductSortViewHolder(
        itemView: View,
        val onItemClicked: (id: Int) -> Unit
) : AbstractViewHolder<CuratedProductSortViewModel>(itemView) {

    private val tvOption = itemView.findViewById<TextView>(R.id.tv_option)
    private val ivCheck = itemView.findViewById<AppCompatImageView>(R.id.iv_check)

    override fun bind(element: CuratedProductSortViewModel) {
        tvOption.text = element.text
        if (element.isChecked) ivCheck.visible() else ivCheck.gone()

        itemView.setOnClickListener { onItemClicked(element.id) }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_af_curated_product_sort
    }
}
package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.adapter.ItemTrustMakerAdapter
import com.tokopedia.catalogcommon.databinding.WidgetTrustmakerBinding
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel
import com.tokopedia.catalogcommon.util.decoration.DividerItemDecoration
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.utils.view.binding.viewBinding

class TrustmakerViewHolder(itemView: View) :
    AbstractViewHolder<TrustMakerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_trustmaker
    }

    private val binding by viewBinding<WidgetTrustmakerBinding>()

    private val displayMetrics = itemView.resources.displayMetrics

    override fun bind(element: TrustMakerUiModel) {
        binding?.rvItems?.apply {
            adapter = ItemTrustMakerAdapter(element.items)
            layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    itemView.context,
                    R.drawable.divider_trustmaker,
                    16.dpToPx(displayMetrics)
                )
            )
        }
    }
}

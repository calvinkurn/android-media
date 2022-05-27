package com.tokopedia.shop.flash_sale.presentation.draft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemDraftBinding
import com.tokopedia.shop.flash_sale.common.extension.formatTo
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel
import com.tokopedia.utils.view.binding.viewBinding

class DraftListViewHolder(
    itemView: View,
    private val deleteIconClickListener: (DraftItemModel) -> Unit = {}
): RecyclerView.ViewHolder(itemView) {

    companion object {
        fun createRootView(parent: ViewGroup): View = LayoutInflater.from(parent.context)
                .inflate(R.layout.ssfs_item_draft, parent, false)
    }

    private val binding: SsfsItemDraftBinding? by viewBinding()

    fun bind(item: DraftItemModel) {
        val startDate = item.startDate.formatTo("dd/MM/yyyy")
        val startHour = item.startDate.formatTo("hh:mm")
        val endDate = item.endDate.formatTo("dd/MM/yyyy")
        val endHour = item.endDate.formatTo("hh:mm")

        binding?.apply {
            typographyDraftTitle.text = item.title
            typographyDraftDesc.text = item.description
            typographyDraftStart.text = MethodChecker.fromHtml("<b>$startDate</b><br/>$startHour")
            typographyDraftEnd.text = MethodChecker.fromHtml("<b>$endDate</b><br/>$endHour")
            iconDelete.setOnClickListener {
                deleteIconClickListener.invoke(item)
            }
        }
    }

}
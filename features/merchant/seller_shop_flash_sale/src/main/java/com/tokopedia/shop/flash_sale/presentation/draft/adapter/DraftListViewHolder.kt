package com.tokopedia.shop.flash_sale.presentation.draft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemDraftBinding
import com.tokopedia.shop.flash_sale.common.constant.DraftConstant.DRAFT_DATE
import com.tokopedia.shop.flash_sale.common.constant.DraftConstant.DRAFT_TIME_WIB
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
        val startDate = item.startDate.formatTo(DRAFT_DATE)
        val startHour = item.startDate.formatTo(DRAFT_TIME_WIB)
        val endDate = item.endDate.formatTo(DRAFT_DATE)
        val endHour = item.endDate.formatTo(DRAFT_TIME_WIB)

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
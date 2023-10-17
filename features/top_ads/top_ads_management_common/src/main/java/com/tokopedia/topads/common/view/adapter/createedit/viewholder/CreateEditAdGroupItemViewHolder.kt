package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.databinding.TopadsCreateEditItemEditAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemUiModel
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemTag
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

class CreateEditAdGroupItemViewHolder(private val viewBinding: TopadsCreateEditItemEditAdGroupBinding) : AbstractViewHolder<CreateEditAdGroupItemUiModel>(viewBinding.root) {

    override fun bind(element: CreateEditAdGroupItemUiModel) {
        viewBinding.editAdItemTitle.text = element.title
        if (element.subtitle.isEmpty()) {
            viewBinding.editAdItemShimmer.show()
            viewBinding.editAdItemSubtitle.invisible()
        } else {
            setSubtitle(element)
            viewBinding.editAdItemSubtitle.show()
            viewBinding.editAdItemShimmer.hide()
        }
        viewBinding.root.setOnClickListener {
            element.clickListener()
        }
    }

    private fun setSubtitle(element: CreateEditAdGroupItemUiModel) {
        if (element.tag == CreateEditAdGroupItemTag.DAILY_BUDGET) {
            if (element.subtitle.toDoubleOrZero() <= Int.ZERO) {
                viewBinding.editAdItemSubtitle.text = "Tidak dibatasi"
            } else {
                viewBinding.editAdItemSubtitle.text = String.format("Rp. %s", CurrencyFormatHelper.convertToRupiah(element.subtitle.toFloatOrZero().toInt().toString()))
            }
        } else if (element.tag == CreateEditAdGroupItemTag.ADS_RECOMMENDATION) {
            viewBinding.editAdItemSubtitle.text = String.format("Biaya iklan Rp.%s", CurrencyFormatHelper.convertToRupiah(element.subtitle.removeCommaRawString().toIntOrZero().toString()))
        } else {
            viewBinding.editAdItemSubtitle.text = element.subtitle
        }
    }

    companion object {
        val LAYOUT = R.layout.topads_create_edit_item_edit_ad_group
    }

}

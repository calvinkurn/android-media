package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography

const val LOW = "low"
const val HIGH = "high"
const val MEDIUM = "mid"
const val KALI = " kali"

class BidInfoItemViewHolder(val view: View, private var actionDelete: (pos: Int) -> Unit, var editBudget: ((pos: Int) -> Unit)?, var editType: ((pos: Int) -> Unit)?) : BidInfoViewHolder<BidInfoItemViewModel>(view) {

    var btnDelete = view.findViewById<ImageUnify>(R.id.btnDelete)
    var btnEditBudget = view.findViewById<ImageUnify>(R.id.editBudget)
    var editTypeBtn = view.findViewById<ImageUnify>(R.id.editType)
    var keywordData = view.findViewById<Typography>(R.id.keywordData)
    var keywordName = view.findViewById<Typography>(R.id.keywordName)
    var keywordType = view.findViewById<Typography>(R.id.typeKeyword)
    var keywordBudget = view.findViewById<Typography>(R.id.keywordBudget)
    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_budget_list_item
    }

    override fun bind(item: BidInfoItemViewModel, minBid: String) {
        item.let {

            btnDelete.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.DELETE))
            btnEditBudget.setImageDrawable(view.context.getResDrawable(com.tokopedia.iconunify.R.drawable.iconunify_edit))
            editTypeBtn.setImageDrawable(view.context.getResDrawable(com.tokopedia.iconunify.R.drawable.iconunify_edit))
            btnDelete.setOnClickListener {
                view.context?.let { context ->
                    val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                    dialog.setTitle(String.format(view.resources.getString(com.tokopedia.topads.common.R.string.topads_create_del_key_conf_dialog_title), item.data.keyword))
                    dialog.setDescription(context.getString(com.tokopedia.topads.common.R.string.topads_create_del_key_conf_dialog_desc))
                    dialog.setPrimaryCTAText(context.getString(com.tokopedia.topads.common.R.string.topads_common_cancel_btn))
                    dialog.setSecondaryCTAText(context.getString(com.tokopedia.topads.common.R.string.topads_create_ya_hapus))
                    dialog.setPrimaryCTAClickListener {
                        dialog.dismiss()
                    }
                    dialog.setSecondaryCTAClickListener {
                        actionDelete.invoke(adapterPosition)
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }
            btnEditBudget.setOnClickListener {
                editBudget?.invoke(adapterPosition)
            }
            editTypeBtn.setOnClickListener {
                editType?.invoke(adapterPosition)
            }
            val competition = when (item.data.competition) {
                LOW -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_low)
                MEDIUM -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_moderation)
                HIGH -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_high)
                else -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_low)
            }
            keywordData?.text = MethodChecker.fromHtml(String.format(view.context.getString(com.tokopedia.topads.common.R.string.topads_create_keyword_data), competition, item.data.totalSearch + KALI))
            keywordName.text = item.data.keyword
            keywordType.text = item.data.keywordType
            if (item.data.bidSuggest != "0")
                keywordBudget.text = "Rp " + item.data.bidSuggest
            else
                keywordBudget.text = "Rp $minBid"
        }
    }
}
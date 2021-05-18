package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_budget_list_item.view.*

const val LOW = "low"
const val HIGH = "high"
const val MEDIUM = "mid"
const val UNKNOWN_SEARCH = "belum ada data"
const val KALI = " kali"

class BidInfoItemViewHolder(val view: View, private var actionDelete: (pos: Int) -> Unit, var editBudget: ((pos: Int) -> Unit)?, var editType: ((pos: Int) -> Unit)?) : BidInfoViewHolder<BidInfoItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_budget_list_item
    }

    override fun bind(item: BidInfoItemViewModel, minBid: String) {
        item.let {
            view.btnDelete.setOnClickListener {
                view.context?.let { context ->
                    val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                    dialog.setTitle(String.format(view.resources.getString(R.string.topads_create_del_key_conf_dialog_title), item.data.keyword))
                    dialog.setDescription(context.getString(R.string.topads_create_del_key_conf_dialog_desc))
                    dialog.setPrimaryCTAText(context.getString(R.string.cancel_btn))
                    dialog.setSecondaryCTAText(context.getString(R.string.topads_create_ya_hapus))
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
            view.editBudget.setOnClickListener {
                editBudget?.invoke(adapterPosition)
            }
            view.editType.setOnClickListener {
                editType?.invoke(adapterPosition)
            }
            val competition = when (item.data.competition) {
                LOW -> view.resources.getString(R.string.topads_common_keyword_competition_low)
                MEDIUM -> view.resources.getString(R.string.topads_common_keyword_competition_moderation)
                HIGH -> view.resources.getString(R.string.topads_common_keyword_competition_high)
                else -> view.resources.getString(R.string.topads_common_keyword_competition_low)
            }
            view.keywordData?.text = MethodChecker.fromHtml(String.format(view.context.getString(R.string.topads_create_keyword_data), competition, item.data.totalSearch + KALI))
            view.keywordName.text = item.data.keyword
            view.typeKeyword.text = item.data.keywordType
            if (item.data.bidSuggest != "0")
                view.keywordBudget.text = "Rp " + item.data.bidSuggest
            else
                view.keywordBudget.text = "Rp $minBid"
        }
    }
}
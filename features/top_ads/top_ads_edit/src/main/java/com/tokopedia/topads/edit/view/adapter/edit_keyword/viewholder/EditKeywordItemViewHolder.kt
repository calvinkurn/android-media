package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.common.data.util.Utils.KALI
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder.Companion.HIGH
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder.Companion.LOW
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder.Companion.MEDIUM
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordItemViewHolder(val view: View,
                                var actionDelete: (pos: Int) -> Unit, var editBudget: ((pos: Int) -> Unit)?, var editType: ((pos: Int) -> Unit)?) : EditKeywordViewHolder<EditKeywordItemViewModel>(view) {


    var btnDelete = view.findViewById<UnifyImageButton>(com.tokopedia.topads.common.R.id.btnDelete)
    var budgetEdit = view.findViewById<UnifyImageButton>(com.tokopedia.topads.common.R.id.editBudget)
    var typeEdit = view.findViewById<UnifyImageButton>(com.tokopedia.topads.common.R.id.editType)
    var keywordData = view.findViewById<Typography>(com.tokopedia.topads.common.R.id.keywordData)
    var keywordName = view.findViewById<Typography>(com.tokopedia.topads.common.R.id.keywordName)
    var typeKeyword = view.findViewById<Typography>(com.tokopedia.topads.common.R.id.typeKeyword)
    var keywordBudget = view.findViewById<Typography>(com.tokopedia.topads.common.R.id.keywordBudget)
    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_budget_list_item

        private const val SPECIFIC_TYPE = "Spesifik"
        private const val BROAD_TYPE = "Luas"
        private const val EXACT_POSITIVE = 21
    }

    override fun bind(item: EditKeywordItemViewModel, added: MutableList<Boolean>, minBid: String) {

        item.let {
            btnDelete.setOnClickListener {
                view.context?.let { context ->
                    val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                    dialog.setTitle(String.format(view.resources.getString(com.tokopedia.topads.common.R.string.topads_create_del_key_conf_dialog_title), item.data.name))
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
            budgetEdit.setOnClickListener {
                editBudget?.invoke(adapterPosition)
            }
            keywordBudget.setOnClickListener {
                editType?.invoke(adapterPosition)
            }
            val competition = when (item.data.competition) {
                LOW -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_low)
                MEDIUM -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_moderation)
                HIGH -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_high)
                else -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_low)
            }
            keywordData.text = MethodChecker.fromHtml(String.format(view.context.getString(com.tokopedia.topads.common.R.string.topads_create_keyword_data), competition, item.data.totalSearch + KALI))
            keywordName.text = item.data.name
            typeKeyword.text = item.data.type
            if (item.data.suggestedBid != "0")
                keywordBudget.text = "Rp " + item.data.suggestedBid
            else
                keywordBudget.text = "Rp $minBid"
        }
    }
}
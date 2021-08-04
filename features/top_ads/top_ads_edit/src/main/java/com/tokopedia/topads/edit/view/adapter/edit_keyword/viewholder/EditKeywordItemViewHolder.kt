package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder.Companion.HIGH
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder.Companion.LOW
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder.Companion.MEDIUM
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordItemViewHolder(
    val view: View,
    var actionDelete: (pos: Int) -> Unit,
    var editBudget: ((pos: Int) -> Unit)?,
    var editType: ((pos: Int) -> Unit)?
) : EditKeywordViewHolder<EditKeywordItemViewModel>(view) {


    private var btnDelete = view.findViewById<IconUnify>(com.tokopedia.topads.common.R.id.btnDelete)
    private var budgetEdit =
        view.findViewById<IconUnify>(com.tokopedia.topads.common.R.id.editBudget)
    private var typeEdit = view.findViewById<IconUnify>(com.tokopedia.topads.common.R.id.editType)
    private var keywordData =
        view.findViewById<Typography>(com.tokopedia.topads.common.R.id.keywordData)
    private var keywordName =
        view.findViewById<Typography>(com.tokopedia.topads.common.R.id.keywordName)
    private var typeKeyword =
        view.findViewById<Typography>(com.tokopedia.topads.common.R.id.typeKeyword)
    private var keywordBudget =
        view.findViewById<Typography>(com.tokopedia.topads.common.R.id.keywordBudget)

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
                if (adapterPosition != RecyclerView.NO_POSITION)
                    actionDelete.invoke(adapterPosition)
            }
            budgetEdit.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    editBudget?.invoke(adapterPosition)
            }
            typeEdit.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    editType?.invoke(adapterPosition)
            }
            val competition = when (item.data.competition) {
                LOW -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_low)
                MEDIUM -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_moderation)
                HIGH -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_high)
                else -> view.resources.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_competition_unknown)
            }

            val search = if (it.data.totalSearch == "-1")
                "-"
            else
                it.data.totalSearch
            keywordData.text = MethodChecker.fromHtml(
                String.format(
                    view.context.getString(com.tokopedia.topads.common.R.string.topads_create_keyword_data),
                    competition,
                    search
                )
            )
            keywordName.text = item.data.name
            if (item.data.typeInt == EXACT_POSITIVE)
                typeKeyword.text = SPECIFIC_TYPE
            else
                typeKeyword.text = BROAD_TYPE
            if (item.data.priceBid != "0")
                keywordBudget.text = "Rp " + item.data.priceBid
            else
                keywordBudget.text = "Rp $minBid"
        }
    }
}
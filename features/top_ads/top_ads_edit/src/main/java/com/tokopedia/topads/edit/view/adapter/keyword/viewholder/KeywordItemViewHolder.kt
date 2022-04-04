package com.tokopedia.topads.edit.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import java.lang.Exception

class KeywordItemViewHolder(val view: View, private var actionSelected: ((pos: Int) -> Unit)?) :
    KeywordViewHolder<KeywordItemViewModel>(view) {

    private val keywordName: Typography = view.findViewById(R.id.keyword_name)
    private val keywordCount: Typography = view.findViewById(R.id.keyword_count)
    private val keywordCompetition: Label = view.findViewById(R.id.keywordCompetition)
    private val checkBox: CheckboxUnify = view.findViewById(R.id.checkBox)

    companion object {
        const val LOW = "low"
        const val HIGH = "high"
        const val MEDIUM = "medium"

        @LayoutRes
        var LAYOUT = R.layout.topads_edit_layout_keyword_list_item
    }

    init {
        view.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
            actionSelected?.invoke(adapterPosition)
        }
    }

    override fun bind(item: KeywordItemViewModel) {
        item.data.let {
            keywordName.text = it.keyword
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = item.isChecked
            try {
                keywordCount.text = Utils.convertToCurrencyString(it.totalSearch.toLong())
            } catch (e: Exception) {
                keywordCount.text = it.totalSearch
            }
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionSelected?.invoke(adapterPosition)
            }
            when (it.competition) {
                LOW -> {
                    keywordCompetition.setLabelType(Label.GENERAL_DARK_GREEN)
                    keywordCompetition.setLabel(view.resources.getString(R.string.topads_common_keyword_competition_low))
                }

                MEDIUM -> {
                    keywordCompetition.setLabelType(Label.GENERAL_DARK_ORANGE)
                    keywordCompetition.setLabel(view.resources.getString(R.string.topads_common_keyword_competition_moderation))
                }

                HIGH -> {
                    keywordCompetition.setLabelType(Label.GENERAL_DARK_RED)
                    keywordCompetition.setLabel(view.resources.getString(R.string.topads_common_keyword_competition_high))
                }

            }

        }
    }

}
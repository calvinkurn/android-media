package com.tokopedia.topads.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_item.view.*
import java.lang.Exception

/**
 * Author errysuprayogi on 11,November,2019
 */
class KeywordItemViewHolder(val view: View, private var actionSelected: ((pos: Int) -> Unit)?) : KeywordViewHolder<KeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_keyword_list_item
        const val LOW = "low"
        const val HIGH = "high"
        const val MEDIUM = "mid"
    }

    init {
        view.setOnClickListener {
            it.checkBox.isChecked = !it.checkBox.isChecked
            actionSelected?.invoke(adapterPosition)
        }
    }

    override fun bind(item: KeywordItemViewModel) {
        item.data.let {
            view.keyword_name.text = it.keyword
            view.checkBox.setOnCheckedChangeListener(null)
            view.checkBox.isChecked = item.isChecked
            try {
                view.keyword_count.text = Utils.convertToCurrencyString(it.totalSearch.toLong())
            } catch (e: Exception) {
                view.keyword_count.text = it.totalSearch
            }
            when (it.competition) {
                LOW -> {
                    view.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREEN)
                    view.keywordCompetition.setLabel(view.resources.getString(R.string.topads_common_keyword_competition_low))
                }

                MEDIUM -> {
                    view.keywordCompetition.setLabelType(Label.GENERAL_DARK_ORANGE)
                    view.keywordCompetition.setLabel(view.resources.getString(R.string.topads_common_keyword_competition_moderation))
                }

                HIGH -> {
                    view.keywordCompetition.setLabelType(Label.GENERAL_DARK_RED)
                    view.keywordCompetition.setLabel(view.resources.getString(R.string.topads_common_keyword_competition_high))
                }

            }
            view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionSelected?.invoke(adapterPosition)
            }
        }
    }

}
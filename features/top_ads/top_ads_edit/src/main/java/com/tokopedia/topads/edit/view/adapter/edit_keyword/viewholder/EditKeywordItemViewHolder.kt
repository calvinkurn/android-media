package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_EXACT
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_PHRASE
import com.tokopedia.topads.edit.utils.Constants.MAX
import com.tokopedia.topads.edit.utils.Constants.MIN
import com.tokopedia.topads.edit.utils.Constants.TITLE_1
import com.tokopedia.topads.edit.utils.Constants.TITLE_2
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.sheet.EditKeywordSortSheet
import kotlinx.android.synthetic.main.topads_edit_keyword_edit_item_layout.view.*

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordItemViewHolder(val view: View,
                                private var actionDelete: ((pos: Int) -> Unit)?,
                                private val actionClick: (() -> MutableMap<String, Int>),
                                private var actionEnable: (() -> Unit),
                                private var actionStatusChange: ((pos: Int) -> Unit)) : EditKeywordViewHolder<EditKeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_keyword_edit_item_layout
        private var bidMap = mutableMapOf<String, Int>()

    }

    private lateinit var sortKeywordList: EditKeywordSortSheet

    override fun bind(item: EditKeywordItemViewModel, data: MutableList<Int>, error: MutableList<Boolean>) {

        item.data.let {
            view.keyword_name.text = it.tag
            if (item.data.type == KEYWORD_TYPE_PHRASE)
                view.sort.text = TITLE_1
            else
                view.sort.text = TITLE_2
            if (adapterPosition < data.size)
                view.budget.textFieldInput.setText(data[adapterPosition].toString())
            bidMap = actionClick.invoke()
            if (data[adapterPosition] < bidMap[MIN]!! && bidMap[MIN] != 0) {
                setError(true, view.resources.getString(R.string.min_bid_error), bidMap[MIN]!!)
                error[adapterPosition] = true

            } else if (data[adapterPosition] > bidMap[MAX]!! && bidMap[MAX] != 0) {
                error[adapterPosition] = true
                setError(true, view.resources.getString(R.string.max_bid_error), bidMap[MAX]!!)
            }
            view.delete.setOnClickListener {
                actionDelete?.invoke(adapterPosition)
            }
            actionEnable.invoke()
            view.sort.setOnClickListener {
                sortKeywordList = EditKeywordSortSheet.newInstance(view.context)
                sortKeywordList.setChecked(view.sort.text.toString())
                sortKeywordList.show()
                sortKeywordList.onItemClick = {
                    val prev = view.sort.text
                    view.sort.text = sortKeywordList.getSelectedSortId()
                    if (sortKeywordList.getSelectedSortId() == TITLE_1) {
                        item.data.type = KEYWORD_TYPE_PHRASE
                    } else {
                        item.data.type = KEYWORD_TYPE_EXACT
                    }
                    if (prev != view.sort.text)
                        actionStatusChange.invoke(adapterPosition)
                }
            }
            view.budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(view.budget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    val result = number.toInt()
                    if (bidMap[MIN] == 0)
                        bidMap = actionClick.invoke()

                    if (adapterPosition < data.size)
                        data[adapterPosition] = result
                    when {
                        result < bidMap[MIN]!! -> {
                            error[adapterPosition] = true
                            setError(true, view.resources.getString(R.string.min_bid_error), bidMap[MIN]!!)
                        }
                        result > bidMap[MAX]!! -> {
                            error[adapterPosition] = true
                            setError(true, view.resources.getString(R.string.max_bid_error), bidMap["max"]!!)
                        }
                        else -> {
                            error[adapterPosition] = false
                            view.budget.setError(false)
                            view.budget.setMessage("")
                        }
                    }
                    actionEnable.invoke()

                }
            })
        }
    }

    fun setError(isError: Boolean, error: String, bid: Int) {
        view.budget.setError(isError)
        view.budget.setMessage(String.format(error, bid))
    }
}
package com.tokopedia.sellerfeedback.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.uimodel.FeedbackPageUiModel
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class FeedbackPageViewHolder(
        itemView: View,
        private val listener: FeedbackPageListener
) : AbstractViewHolder<FeedbackPageUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feedback_page
    }

    private var searchBar: SearchBarUnify? = null
    private var listPage: ListUnify? = null
    private var stringPages: List<String>? = null
    private var itemPages: ArrayList<ListItemUnify>? = null

    override fun bind(data: FeedbackPageUiModel) {
        itemView.run {
            searchBar = findViewById(R.id.search_bar)
            listPage = findViewById(R.id.list_page)

            resources.getStringArray(R.array.feedback_pages).toList().let {
                stringPages = it
                itemPages = mapToListItemUnify(it)
            }

            initList(data)
            setupInteraction()
        }
    }

    private fun initList(data: FeedbackPageUiModel) {
        val listPage = listPage
        val itemPages = itemPages
        val stringPages = stringPages

        if (listPage == null || itemPages == null || stringPages == null) return

        listPage.setData(itemPages)
        listPage.onLoadFinish {
            val selectedPosition = stringPages.indexOf(data.title)
            listPage.setSelection(selectedPosition)

            listPage.onItemClickListener = generateOnItemClickListener(itemPages)
            itemPages.forEachIndexed { index, item ->
                item.listRightRadiobtn?.apply {
                    if (index == selectedPosition) isChecked = true
                    setOnClickListener {
                        listener.onItemClicked(item.listTitleText)
                    }
                }
            }
        }
    }

    private fun mapToListItemUnify(list: List<String>): ArrayList<ListItemUnify> {
        return list.map {
            val item = ListItemUnify(it, "")
            item.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            item
        } as ArrayList<ListItemUnify>
    }

    private fun setupInteraction() {
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val listPage = listPage
                val itemPages = itemPages
                if (listPage != null && itemPages != null) {
                    val text = p0?.toString() ?: ""
                    val filteredPages = itemPages.filter { it.listTitleText.contains(text, true) } as ArrayList<ListItemUnify>
                    listPage.setData(filteredPages)
                    listPage.onLoadFinish { listPage.onItemClickListener = generateOnItemClickListener(filteredPages) }
                }
            }
        }
        searchBar?.searchBarTextField?.addTextChangedListener(searchTextWatcher)
    }

    private fun generateOnItemClickListener(currentPages: List<ListItemUnify>): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, _, index, _ -> listener.onItemClicked(currentPages[index].listTitleText) }
    }

    interface FeedbackPageListener {
        fun onItemClicked(title: String)
    }

}
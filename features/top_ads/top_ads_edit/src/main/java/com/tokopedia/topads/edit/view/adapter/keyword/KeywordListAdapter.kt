package com.tokopedia.topads.edit.view.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel

class KeywordListAdapter(private val typeFactory: KeywordListAdapterTypeFactory, var items: MutableList<KeywordViewModel>) : RecyclerView.Adapter<KeywordViewHolder<KeywordViewModel>>() {

    private var remains: MutableList<KeywordViewModel> = mutableListOf()
    var favoured: MutableList<KeywordItemViewModel> = mutableListOf() /*chosen keywords */
    var manualKeywords: MutableList<KeywordItemViewModel> = mutableListOf() /*manually added keywords*/
    private var SELECTED_KEYWORD = " Kata Kunci Pilihan"
    private var RECOMMENDED = "Rekomendasi"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder<KeywordViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as KeywordViewHolder<KeywordViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: KeywordViewHolder<KeywordViewModel>, position: Int) {
        holder.bind(items[position])
    }

    fun getSelectedItems(): List<KeywordItemViewModel> {
        val selected = mutableListOf<KeywordItemViewModel>()
        items.forEachIndexed { _, model ->
            if ((model is KeywordItemViewModel) && model.isChecked) {
                selected.add(model)
            }
        }
        return selected
    }

    fun addNewKeyword(it: KeywordItemViewModel, originalList: ArrayList<String>): Boolean {

        originalList.forEach { name ->
            if (name == it.data.keyword) {
                return true
            }
        }
        favoured.forEachIndexed lit@{ index, _ ->
            if (favoured[index].data.keyword == it.data.keyword) {
                return true
            }
        }
        if (items.size == 0) {
            items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        } else if (items[0] is KeywordGroupViewModel && (items[0] as KeywordGroupViewModel).title != SELECTED_KEYWORD) {
            items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        }

        var index = 0
        items.forEachIndexed { ind, item ->
            if (item is KeywordItemViewModel) {
                if (item.data.keyword == it.data.keyword) {
                    index = ind
                }
            }
        }
        if (index != 0)
            items.removeAt(index)
        else
            manualKeywords.add(it)
        items.removeAll(favoured)
        favoured.add(it)
        items.addAll(1, favoured)
        it.isChecked = true
        notifyDataSetChanged()
        return false
    }

    fun setSelectedKeywords(it: List<KeywordItemViewModel>) {
        favoured.clear()
        remains.clear()
        favoured.addAll(it)
        val listSelected: MutableList<String> = mutableListOf()
        getSelectedItems().forEach {
            listSelected.add(it.data.keyword)
        }
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            val temp = iterator.next()
            if (temp is KeywordItemViewModel) {
                if (favoured.find { fav -> fav.data.keyword == temp.data.keyword } != null) {
                    iterator.remove()
                }
            }
        }
        items.forEach { index ->
            if (index is KeywordItemViewModel)
                remains.add(index)
        }
        items.clear()
        items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        items.addAll(favoured)
        if (remains.size != 0) {
            items.add(KeywordGroupViewModel(RECOMMENDED))
            items.addAll(remains)
        }
        setSelectedList(listSelected)
    }


    fun setSelectedList(selectedKeywords: MutableList<String>) {
        items.forEachIndexed { _, key ->
            if (key is KeywordItemViewModel) {
                key.isChecked = false
                selectedKeywords.forEach {
                    if (key.data.keyword == it) {
                        key.isChecked = true
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun setList(items: MutableList<KeywordViewModel>) {
        this.items = items
        notifyDataSetChanged()

    }

    fun addRestoredData(favoured: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>?, selected: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>?, manual: ArrayList<KeywordSuggestionResponse.Result.TopAdsGetKeywordSuggestionV3.DataItem.KeywordDataItem>?) {
        val toBeAdded: MutableList<KeywordViewModel> = mutableListOf()
        toBeAdded.addAll(items)
        manual?.forEach {
            manualKeywords.add(KeywordItemViewModel(it))
        }
        toBeAdded.removeAt(0)

        val iterator = toBeAdded.iterator()
        while (iterator.hasNext()) {
            val temp = iterator.next()
            if (favoured?.find { it -> it.keyword == (temp as KeywordItemViewModel).data.keyword } != null) {
                iterator.remove()
            }
        }
        items.clear()
        items.add(0, KeywordGroupViewModel(SELECTED_KEYWORD))
        val list: MutableList<KeywordItemViewModel> = mutableListOf()
        val listSelected: MutableList<String> = mutableListOf()
        favoured?.forEach {
            list.add(KeywordItemViewModel(it))
            if (selected?.find { selected -> it.keyword == selected.keyword } != null) {
                listSelected.add(it.keyword)
            }
        }
        items.addAll(list)
        items.add(KeywordGroupViewModel(RECOMMENDED))
        items.addAll(toBeAdded)
        setSelectedList(listSelected)
    }

    companion object {
        fun createInstance(typeFactory: KeywordListAdapterTypeFactory): KeywordListAdapter {
            return KeywordListAdapter(typeFactory, mutableListOf())
        }
    }
}

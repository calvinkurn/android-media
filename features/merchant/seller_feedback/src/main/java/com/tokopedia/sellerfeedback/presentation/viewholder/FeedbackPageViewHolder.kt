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

    private val sampleList = listOf(
            "Home",
            "Chat",
            "Diskusi",
            "Tambah Produk",
            "Ubah Produk",
            "Produk Toko",
            "Penjualan",
            "Statistik",
            "Iklan dan Promosi",
            "Dekorasi Toko",
            "Ulasan",
            "Pesanan Dikomplain",
            "Pengaturan Toko",
            "Pengaturan Admin"
    )

    private var searchBar: SearchBarUnify? = null
    private var listPage: ListUnify? = null

    private val pages = mapToListItemUnify(sampleList)

    override fun bind(data: FeedbackPageUiModel) {
        itemView.run {
            searchBar = findViewById(R.id.search_bar)
            listPage = findViewById(R.id.list_page)

            initList(data)
            setupInteraction()

        }
    }

    private fun initList(data: FeedbackPageUiModel) {
        listPage?.apply {

            setData(pages)

            onLoadFinish {
                val position = sampleList.indexOf(data.title)
                setSelection(position)
                onItemClickListener = generateOnItemClickListener(pages)

                pages.forEachIndexed { index, item ->
                    item.listRightRadiobtn?.apply {
                        if (index == position) isChecked = true
                        setOnClickListener {
                            listener.onItemClicked(item.listTitleText)
                        }
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
                listPage?.apply {
                    val text = p0?.toString() ?: ""
                    val filteredPages = pages.filter { it.listTitleText.contains(text, true) } as ArrayList<ListItemUnify>
                    setData(filteredPages)
                    onLoadFinish { onItemClickListener = generateOnItemClickListener(filteredPages) }
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
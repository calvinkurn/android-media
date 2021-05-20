package com.tokopedia.sellerfeedback.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.uimodel.FeedbackPageUiModel
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.util.*

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

    override fun bind(data: FeedbackPageUiModel) {
        itemView.run {
            searchBar = findViewById(R.id.search_bar)
            listPage = findViewById(R.id.list_page)

            listPage?.apply {
                val newList = sampleList.map {
                    val item = ListItemUnify(it, "")
                    item.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
                    item
                } as ArrayList<ListItemUnify>

                setData(newList)
                onLoadFinish {
                    val position = sampleList.indexOf(data.title)
                    setOnItemClickListener { _, _, position, _ ->
                        setSelection(position)
                        listener.onItemClicked(sampleList[position])
                    }
                    newList.forEachIndexed { index, item ->
                        item.listRightRadiobtn?.apply {
                            isChecked = index == position
                            setOnClickListener {
                                listener.onItemClicked(sampleList[index])
                            }
                        }
                    }
                }
            }
        }
    }

    interface FeedbackPageListener {
        fun onItemClicked(title: String)
    }

}
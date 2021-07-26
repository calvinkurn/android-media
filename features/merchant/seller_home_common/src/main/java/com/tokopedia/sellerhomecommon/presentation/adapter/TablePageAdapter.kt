package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableItemDivider
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlViewHolder
import kotlinx.android.synthetic.main.shc_item_table_page.view.*

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class TablePageAdapter : RecyclerView.Adapter<TablePageAdapter.TablePageViewHolder>() {

    private var itemImpressionListener: ((position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit)? = null
    private var itemClickHtmlListener: ((url: String, isEmpty: Boolean) -> Unit)? = null
    private var items: List<TablePageUiModel> = emptyList()

    fun setItems(items: List<TablePageUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun addOnImpressionListener(onView: (position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit) {
        this.itemImpressionListener = onView
    }

    fun addOnClickHtmlListener(onClick: (url: String, isEmpty: Boolean) -> Unit) {
        this.itemClickHtmlListener = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TablePageViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return TablePageViewHolder(inflater.inflate(R.layout.shc_item_table_page, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TablePageViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onView = {
            itemImpressionListener?.invoke(position, items.size.orZero(), item.rows.isNullOrEmpty())
        }, onClickHtml = { url ->
            itemClickHtmlListener?.invoke(url, item.rows.isNullOrEmpty())
        })
    }

    inner class TablePageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), TableColumnHtmlViewHolder.Listener {

        private val tableAdapter = TableItemAdapter(this)
        private var onHtmlClicked: (String) -> Unit = {}

        fun bind(item: TablePageUiModel,
                 onView: () -> Unit,
                 onClickHtml: (String) -> Unit) = with(itemView) {
            val mSpanCount = item.headers.sumBy { it.width }
            val mLayoutManager = object : GridLayoutManager(context, mSpanCount) {
                override fun canScrollVertically(): Boolean = false
            }

            with(rvTableContent) {
                adapter = tableAdapter
                layoutManager = mLayoutManager
            }

            mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return try {
                        when (val mItem: Visitable<*> = tableAdapter.items[position]) {
                            is TableHeaderUiModel -> mItem.width
                            is TableRowsUiModel -> mItem.width
                            else -> mLayoutManager.spanCount //is TableItemDivider
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        mLayoutManager.spanCount
                    }
                }
            }

            setTableData(item)

            addOnImpressionListener(item.impressHolder, onView)
            onHtmlClicked = onClickHtml
        }

        private fun setTableData(item: TablePageUiModel) {
            tableAdapter.items.clear()
            tableAdapter.items.addAll(item.headers)
            tableAdapter.items.add(TableItemDivider)
            val headerCount = item.headers.size
            val rowCount = item.rows.size
            item.rows.forEachIndexed { i, row ->
                tableAdapter.items.add(row)
                if (i.plus(1).rem(headerCount) == 0 && i != rowCount.minus(1)) {
                    tableAdapter.items.add(TableItemDivider)
                }
            }
            tableAdapter.notifyDataSetChanged()
        }

        override fun onHyperlinkClicked(url: String) {
            onHtmlClicked(url)
        }
    }
}
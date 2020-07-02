package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableItemDivider
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import kotlinx.android.synthetic.main.shc_item_table_page.view.*

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class TablePageAdapter : RecyclerView.Adapter<TablePageAdapter.TablePageViewHolder>() {

    private var items: List<TablePageUiModel> = emptyList()

    fun setItems(items: List<TablePageUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TablePageViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return TablePageViewHolder(inflater.inflate(R.layout.shc_item_table_page, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TablePageViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class TablePageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tableAdapter = TableItemAdapter()

        fun bind(item: TablePageUiModel) = with(itemView) {
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
        }

        private fun setTableData(item: TablePageUiModel) {
            tableAdapter.items.clear()
            tableAdapter.items.addAll(item.headers)
            tableAdapter.items.add(TableItemDivider)
            val headerCount = item.headers.size
            val rowCount = item.rows.size
            var counter = 0
            item.rows.forEachIndexed { i, row ->
                tableAdapter.items.add(row)
                if (counter == headerCount.minus(1) && i != rowCount.minus(1)) {
                    tableAdapter.items.add(TableItemDivider)
                    counter = 0
                } else {
                    counter++
                }
            }
            tableAdapter.notifyDataSetChanged()
        }
    }
}
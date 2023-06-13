package com.tokopedia.sellerhomecommon.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.databinding.ShcItemTablePageBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableHeaderUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableItemDivider
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.TableColumnHtmlViewHolder

/**
 * Created By @ilhamsuaib on 30/06/20
 */

class TablePageAdapter : RecyclerView.Adapter<TablePageAdapter.TablePageViewHolder>() {

    private var itemImpressionListener: (
        (position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit
    )? = null
    private var itemClickHtmlListener: ((url: String, text: String, meta: TableRowsUiModel.Meta, isEmpty: Boolean) -> Unit)? = null
    private var items: List<TablePageUiModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TablePageViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ShcItemTablePageBinding.inflate(inflater, parent, false)
        return TablePageViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TablePageViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onView = {
            itemImpressionListener?.invoke(position, items.size.orZero(), item.rows.isEmpty())
        }, onClickHtml = { url, text, meta ->
            itemClickHtmlListener?.invoke(url, text, meta, item.rows.isEmpty())
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<TablePageUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnImpressionListener(onView: (position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit) {
        this.itemImpressionListener = onView
    }

    fun addOnClickHtmlListener(onClick: (url: String, text: String, meta: TableRowsUiModel.Meta, isEmpty: Boolean) -> Unit) {
        this.itemClickHtmlListener = onClick
    }

    inner class TablePageViewHolder(
        private val binding: ShcItemTablePageBinding
    ) : RecyclerView.ViewHolder(binding.root), TableColumnHtmlViewHolder.Listener {

        private val tableAdapter = TableItemAdapter(this)
        private var onHtmlClicked: (String, String, TableRowsUiModel.Meta) -> Unit = { _, _, _ -> }

        fun bind(
            item: TablePageUiModel,
            onView: () -> Unit,
            onClickHtml: (String, String, TableRowsUiModel.Meta) -> Unit
        ) = with(binding) {
            val mSpanCount = item.headers.sumOf { it.width }
            val mLayoutManager = object : GridLayoutManager(root.context, mSpanCount) {
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

            root.addOnImpressionListener(item.impressHolder, onView)
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

        override fun onHyperlinkClicked(url: String, text: String, meta: TableRowsUiModel.Meta) {
            onHtmlClicked(url, text, meta)
        }

    }
}

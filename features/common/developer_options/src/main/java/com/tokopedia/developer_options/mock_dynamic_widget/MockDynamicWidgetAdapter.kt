package com.tokopedia.developer_options.mock_dynamic_widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class MockDynamicWidgetAdapter(
    private val listener: MockDynamicWidgetPageViewHolder.Listener? = null
) : RecyclerView.Adapter<MockDynamicWidgetAdapter.MockDynamicWidgetPageViewHolder>() {

    private var listMockDynamicWidgetPage: MutableList<MockDynamicWidgetModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MockDynamicWidgetPageViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_mock_dynamic_widget_page_item, parent, false)
        return MockDynamicWidgetPageViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: MockDynamicWidgetPageViewHolder, position: Int) {
        listMockDynamicWidgetPage.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return listMockDynamicWidgetPage.size
    }

    fun addEntryPage(model: MockDynamicWidgetModel) {
        submitList(listMockDynamicWidgetPage.toMutableList().apply {
            add(model)
        })
    }

    private fun submitList(items: List<MockDynamicWidgetModel>) {
        val diffUtilCallback = RvDiffUtilCallback(listMockDynamicWidgetPage, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        listMockDynamicWidgetPage.clear()
        listMockDynamicWidgetPage.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    class RvDiffUtilCallback(
        private val oldItems: List<MockDynamicWidgetModel>,
        private val newItems: List<MockDynamicWidgetModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems.getOrNull(oldItemPosition)
            val newItem = newItems.getOrNull(newItemPosition)
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems.getOrNull(oldItemPosition)
            val newItem = newItems.getOrNull(newItemPosition)
            return oldItem == newItem
        }
    }

    class MockDynamicWidgetPageViewHolder(itemView: View, private val listener: Listener? = null) : RecyclerView.ViewHolder(itemView) {
        interface Listener {
            fun onItemClick(model: MockDynamicWidgetModel)
        }

        fun bind(model: MockDynamicWidgetModel) {
            configShopWidgetName(model)
        }

        private fun configShopWidgetName(model: MockDynamicWidgetModel) {
            itemView.findViewById<UnifyButton>(R.id.button_mock_dynamic_widget_page).apply {
                text = model.pageName
                listener?.let {
                    setOnClickListener { listener.onItemClick(model) }
                }
            }
        }
    }
}

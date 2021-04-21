package com.tokopedia.autocomplete.initialstate.dynamic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.layout_dynamic_item_initial_state.view.*
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class DynamicInitialStateViewHolder(
        itemView: View,
        private val listener: InitialStateItemClickListener
) : AbstractViewHolder<DynamicInitialStateSearchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_dynamic_initial_state
    }

    override fun bind(element: DynamicInitialStateSearchDataView) {
        bindContent(element)
    }

    private fun bindContent(element: DynamicInitialStateSearchDataView) {
        itemView.recyclerView?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(element.list)
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
    }

    private fun createAdapter(
            list: List<BaseItemInitialStateSearch>
    ): RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
        val adapter = ItemAdapter(listener)
        adapter.setData(list)
        return adapter
    }

    private inner class ItemAdapter(private val clickListener: InitialStateItemClickListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
        private var data: List<BaseItemInitialStateSearch> = ArrayList()

        fun setData(data: List<BaseItemInitialStateSearch>) {
            this.data = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_dynamic_item_initial_state, parent, false)
            return ItemViewHolder(itemView, clickListener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : RecyclerView.ViewHolder(itemView) {

            fun bind(item: BaseItemInitialStateSearch) {
                bindIcon(item)
                bindTitle(item)
                bindSubtitle(item)
                bindListener((item))
            }

            private fun bindIcon(item: BaseItemInitialStateSearch) {
                itemView.initialStateDynamicIcon?.shouldShowWithAction(item.imageUrl.isNotEmpty()) {
                    ImageHandler.loadImageRounded(itemView.context, itemView.initialStateDynamicIcon, item.imageUrl, 6.toDp().toFloat())
                }
            }

            private fun bindTitle(item: BaseItemInitialStateSearch) {
                itemView.initialStateDynamicItemTitle?.shouldShowWithAction(item.title.isNotEmpty()) {
                    itemView.initialStateDynamicItemTitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.title).toString())
                }
            }

            private fun bindSubtitle(item: BaseItemInitialStateSearch) {
                itemView.initialStateDynamicItemSubtitle?.shouldShowWithAction(item.subtitle.isNotEmpty()) {
                    itemView.initialStateDynamicItemSubtitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.subtitle).toString())
                }
            }

            private fun bindListener(item: BaseItemInitialStateSearch) {
                itemView.initialStateDynamicItem?.setOnClickListener {
                    clickListener.onDynamicSectionItemClicked(item, adapterPosition)
                }
            }
        }
    }
}
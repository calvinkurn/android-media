package com.tokopedia.autocompletecomponent.initialstate.dynamic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutDynamicInitialStateBinding
import com.tokopedia.autocompletecomponent.databinding.LayoutDynamicItemInitialStateBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateLayoutStrategyFactory
import com.tokopedia.autocompletecomponent.initialstate.InitialStateLayoutStrategy
import com.tokopedia.autocompletecomponent.util.loadImageRounded
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class DynamicInitialStateViewHolder(
    itemView: View,
    private val listener: DynamicInitialStateListener,
    reimagineVariant: Search1InstAuto
) : AbstractViewHolder<DynamicInitialStateSearchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_dynamic_initial_state
    }

    private var binding: LayoutDynamicInitialStateBinding? by viewBinding()

    private val layoutStrategy: InitialStateLayoutStrategy = InitialStateLayoutStrategyFactory.create(reimagineVariant)
    override fun bind(element: DynamicInitialStateSearchDataView) {
        bindContent(element)
    }

    private fun bindContent(element: DynamicInitialStateSearchDataView) {
        binding?.recyclerViewDynamicInitialState?.let {
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

    private inner class ItemAdapter(
        private val listener: DynamicInitialStateListener,
    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
        private var data: List<BaseItemInitialStateSearch> = ArrayList()

        fun setData(data: List<BaseItemInitialStateSearch>) {
            this.data = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.layout_dynamic_item_initial_state,
                    parent,
                    false
                )
            return ItemViewHolder(itemView, listener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(
            itemView: View,
            private val listener: DynamicInitialStateListener,
        ) : RecyclerView.ViewHolder(itemView) {

            private var binding: LayoutDynamicItemInitialStateBinding? by viewBinding()

            fun bind(item: BaseItemInitialStateSearch) {
                bindIcon(item)
                bindTitle(item)
                bindSubtitle(item)
                bindListener((item))
            }

            private fun bindIcon(item: BaseItemInitialStateSearch) {
                val icon = binding?.initialStateDynamicIcon ?: return

                icon.shouldShowWithAction(item.imageUrl.isNotEmpty()) {
                    icon.loadImageRounded(item.imageUrl)
                }
            }

            private fun bindTitle(item: BaseItemInitialStateSearch) {
                val title = binding?.initialStateDynamicItemTitle ?: return
                layoutStrategy.bindTitle(title, item)
            }

            private fun bindSubtitle(item: BaseItemInitialStateSearch) {
                val subtitle = binding?.initialStateDynamicItemSubtitle ?: return

                subtitle.shouldShowWithAction(item.subtitle.isNotEmpty()) {
                    subtitle.text = MethodChecker.fromHtml(item.subtitle).toString()
                }
            }

            private fun bindListener(item: BaseItemInitialStateSearch) {
                binding?.initialStateDynamicItem?.setOnClickListener {
                    listener.onDynamicSectionItemClicked(item)
                }
            }
        }
    }
}

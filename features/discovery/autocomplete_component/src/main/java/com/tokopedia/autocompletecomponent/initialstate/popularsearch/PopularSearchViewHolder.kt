package com.tokopedia.autocompletecomponent.initialstate.popularsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutDynamicItemInitialStateBinding
import com.tokopedia.autocompletecomponent.databinding.LayoutPopularAutocompleteBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.view.binding.viewBinding

class PopularSearchViewHolder(
    itemView: View,
    private val listener: PopularSearchListener
) : AbstractViewHolder<PopularSearchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_popular_autocomplete
    }

    private var binding: LayoutPopularAutocompleteBinding? by viewBinding()

    override fun bind(element: PopularSearchDataView) {
        bindContent(element)
    }

    private fun bindContent(element: PopularSearchDataView) {
        binding?.recyclerViewPopularSearch?.let {
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
        private val clickListener: PopularSearchListener,
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
            return ItemViewHolder(itemView, clickListener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(
            itemView: View,
            private val listener: PopularSearchListener,
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
                    ImageHandler.loadImageRounded(
                        itemView.context,
                        icon,
                        item.imageUrl,
                        6.toDp().toFloat()
                    )
                }
            }

            private fun bindTitle(item: BaseItemInitialStateSearch) {
                val title = binding?.initialStateDynamicItemTitle ?: return

                title.shouldShowWithAction(item.title.isNotEmpty()) {
                    title.text = MethodChecker.fromHtml(item.title).toString()
                }
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
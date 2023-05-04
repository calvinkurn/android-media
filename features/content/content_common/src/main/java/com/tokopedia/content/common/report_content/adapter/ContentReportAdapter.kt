package com.tokopedia.content.common.report_content.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.databinding.ItemFeedThreeDotsMenuBinding
import com.tokopedia.content.common.report_content.model.ContentReportItemUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * @author by astidhiyaa on 27/03/23
 */
internal class ContentReportAdapter(private val listener: ContentReportViewHolder.Listener) :
    RecyclerView.Adapter<ContentReportViewHolder>() {
    private val items = mutableListOf<ContentReportItemUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentReportViewHolder =
        ContentReportViewHolder.create(parent, listener)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ContentReportViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateList(list: List<ContentReportItemUiModel>) {
        items.clear()
        items.addAll(list)
    }
}

internal class ContentReportViewHolder(
    private val binding: ItemFeedThreeDotsMenuBinding,
    private val listener: ContentReportViewHolder.Listener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ContentReportItemUiModel) {
        binding.tvName.text = itemView.resources.getString(item.title)
        binding.ivIcon.showWithCondition(item.icon != null)
        binding.ivIcon.setImageDrawable(item.icon)
        binding.root.setOnClickListener {
            listener.onClicked(item)
        }
    }

    interface Listener {
        fun onClicked(item: ContentReportItemUiModel)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ): ContentReportViewHolder {
            return ContentReportViewHolder(
                ItemFeedThreeDotsMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }
    }
}

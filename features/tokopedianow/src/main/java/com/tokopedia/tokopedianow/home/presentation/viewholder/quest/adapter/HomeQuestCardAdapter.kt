package com.tokopedia.tokopedianow.home.presentation.viewholder.quest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.quest.HomeQuestCardItemViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.quest.adapter.diffutil.HomeQuestCardDiffCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.quest.HomeQuestCardItemViewHolder.HomeQuestCardItemListener

class HomeQuestCardAdapter(
    private val questCardItemListener: HomeQuestCardItemListener? = null
) : ListAdapter<HomeQuestCardItemUiModel, HomeQuestCardItemViewHolder>(HomeQuestCardDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeQuestCardItemViewHolder {
        val binding = ItemTokopedianowQuestCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeQuestCardItemViewHolder(
            binding = binding,
            listener = questCardItemListener
        )
    }

    override fun onBindViewHolder(holder: HomeQuestCardItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLongOrZero()
    }
}


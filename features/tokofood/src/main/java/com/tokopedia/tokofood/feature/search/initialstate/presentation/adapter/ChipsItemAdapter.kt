package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.tokofood.databinding.ChipsItemInitialStatePopularSearchBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.diffutil.ChipsHighlightDiffUtil
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsPopularSearch

class ChipsItemAdapter(private val chipsItemListener: ChipsItemListener) :
    RecyclerView.Adapter<ChipsItemAdapter.ChipsItemViewHolder>() {

    private val popularSearchList = mutableListOf<ChipsPopularSearch>()

    fun setChipsPopularSearch(newItemList: List<ChipsPopularSearch>) {
        val callBack = ChipsHighlightDiffUtil(popularSearchList, newItemList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        popularSearchList.clear()
        popularSearchList.addAll(newItemList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipsItemViewHolder {
        val binding = ChipsItemInitialStatePopularSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChipsItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return popularSearchList.size
    }

    override fun onBindViewHolder(holder: ChipsItemViewHolder, position: Int) {
        val data = popularSearchList[position]
        holder.bind(data)
    }

    inner class ChipsItemViewHolder(
        private val binding: ChipsItemInitialStatePopularSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChipsPopularSearch) {
            with(binding.chipsPopularSearch) {
                val searchColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN400)
                chipImageResource = getIconUnifyDrawable(context, IconUnify.SEARCH, searchColor)
                centerText = true
                chipText = data.title
                setOnClickListener {
                    chipsItemListener.onChipsClicked(data)
                }
            }
        }
    }

    interface ChipsItemListener {
        fun onChipsClicked(data: ChipsPopularSearch)
    }
}
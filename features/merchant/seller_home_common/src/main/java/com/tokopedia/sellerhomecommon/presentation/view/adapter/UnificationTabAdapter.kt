package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemUnificationTabBinding
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel

/**
 * Created by @ilhamsuaib on 20/07/22.
 */

class UnificationTabAdapter(
    private val items: List<UnificationTabUiModel>
) : RecyclerView.Adapter<UnificationTabAdapter.ViewHolder>() {

    companion object {
        private const val TITLE_FORMAT = "%s (%s)"
    }

    private var onItemClicked: (UnificationTabUiModel) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShcItemUnificationTabBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClicked)
    }

    override fun getItemCount(): Int = items.size

    fun setOnItemSelectedListener(action: (UnificationTabUiModel) -> Unit) {
        this.onItemClicked = action
    }

    inner class ViewHolder(
        private val binding: ShcItemUnificationTabBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: UnificationTabUiModel,
            onItemClicked: (UnificationTabUiModel) -> Unit
        ) {
            with(binding) {
                tvShcUnificationTabTitle.text = String.format(
                    TITLE_FORMAT, item.title, item.itemCount.toString()
                )
                tvShcUnificationTabDescription.text = item.tooltip
                icShcUnificationTabStatus.isVisible = item.isSelected

                if (item.isVisited) {
                    imgShcUnificationTabDot.gone()
                } else {
                    imgShcUnificationTabDot.visible()
                    imgShcUnificationTabDot.loadImage(R.drawable.ic_shc_tooltip_chart_dot)
                }

                val isLastItem = adapterPosition == itemCount.minus(Int.ONE)
                if (isLastItem) {
                    dividerShcUnificationTab.invisible()
                } else {
                    dividerShcUnificationTab.visible()
                }

                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }
}
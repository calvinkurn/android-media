package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemUnificationTabBinding
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.unifycomponents.NotificationUnify

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
                tvShcUnificationTabTitle.text = if (item.isUnauthorized) {
                    item.title
                } else {
                    String.format(TITLE_FORMAT, item.title, item.itemCount.toString())
                }
                tvShcUnificationTabDescription.text = item.tooltip
                icShcUnificationTabStatus.isVisible = item.isSelected

                val isLastItem = adapterPosition == itemCount.minus(Int.ONE)
                if (isLastItem) {
                    dividerShcUnificationTab.invisible()
                } else {
                    dividerShcUnificationTab.visible()
                }

                showTag(item.isNew, item.isUnauthorized)

                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }

        private fun showTag(isNew: Boolean, isUnauthorized: Boolean) {
            with(binding) {
                when {
                    isNew && !isUnauthorized -> {
                        shcUnificationTabTag.visible()
                        val newTag = root.context.getString(R.string.shc_new)
                        shcUnificationTabTag.setNotification(
                            newTag,
                            NotificationUnify.TEXT_TYPE,
                            NotificationUnify.COLOR_TEXT_TYPE
                        )
                    }
                    isUnauthorized -> {
                        shcUnificationTabTag.visible()
                        val newTag = root.context.getString(R.string.shc_no_access)
                        shcUnificationTabTag.setNotification(
                            newTag,
                            NotificationUnify.TEXT_TYPE,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN100
                        )
                        shcUnificationTabTag.setTextColor(
                            root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                        )
                    }
                    else -> shcUnificationTabTag.gone()
                }
            }
        }
    }
}
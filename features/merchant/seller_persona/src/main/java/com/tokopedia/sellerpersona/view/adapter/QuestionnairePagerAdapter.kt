package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerpersona.databinding.ItemQuestionnairePagerBinding
import com.tokopedia.sellerpersona.view.adapter.viewholder.BaseOptionViewHolder
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class QuestionnairePagerAdapter : Adapter<QuestionnairePagerAdapter.PagerViewHolder>() {

    private val pages: MutableList<QuestionnairePagerUiModel> = mutableListOf()
    private var onOptionItemSelected: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuestionnairePagerBinding.inflate(inflater, parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = pages.size

    fun getPages(): List<QuestionnairePagerUiModel> {
        return pages
    }

    fun setPages(pages: List<QuestionnairePagerUiModel>) {
        this.pages.clear()
        this.pages.addAll(pages)

        notifyItemRangeChanged(Int.ZERO, pages.size.minus(Int.ONE))
    }

    fun onOptionItemSelectedListener(onOptionItemSelected: () -> Unit) {
        this.onOptionItemSelected = onOptionItemSelected
    }

    inner class PagerViewHolder(
        private val binding: ItemQuestionnairePagerBinding
    ) : ViewHolder(binding.root), BaseOptionViewHolder.Listener {

        private val optionAdapter by lazy {
            OptionsAdapter(this)
        }

        override fun onOptionItemSelectedListener(option: BaseOptionUiModel) {
            when (option) {
                is BaseOptionUiModel.QuestionOptionSingleUiModel -> {
                    handleSingleOptionSelected(option)
                }
                is BaseOptionUiModel.QuestionOptionMultipleUiModel -> {
                    handleMultipleOptionSelected(option)
                }
            }
            onOptionItemSelected?.invoke()
        }

        fun bind() {
            val page = pages[absoluteAdapterPosition]
            showTitle(page)
            showOptions(page)
        }

        private fun showTitle(page: QuestionnairePagerUiModel) {
            with(binding) {
                tvSpQuestionTitle.text = page.questionTitle
                tvSpQuestionSubtitle.text = page.questionSubtitle
            }
        }

        private fun showOptions(page: QuestionnairePagerUiModel) {
            with(binding.rvSpQuestionOptions) {
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollHorizontally(): Boolean = false
                }
                adapter = optionAdapter

                post {
                    val options = page.options.orEmpty()
                    val isItemEqual = options == optionAdapter.data
                    if (!isItemEqual) {
                        optionAdapter.clearAllElements()
                        optionAdapter.addElement(options)
                    }
                }
            }
        }

        private fun handleMultipleOptionSelected(option: BaseOptionUiModel.QuestionOptionMultipleUiModel) {
            pages[absoluteAdapterPosition].options?.forEach {
                if (it.value == option.value) {
                    it.isSelected = option.isSelected
                }
            }
        }

        private fun handleSingleOptionSelected(option: BaseOptionUiModel.QuestionOptionSingleUiModel) {
            pages[absoluteAdapterPosition].options?.forEachIndexed { i, it ->
                if (option.isSelected) {
                    if (it.value == option.value) {
                        it.isSelected = true
                    } else if (it.isSelected) {
                        it.isSelected = false
                        optionAdapter.notifyItemChanged(i)
                    }
                }
            }
        }
    }
}
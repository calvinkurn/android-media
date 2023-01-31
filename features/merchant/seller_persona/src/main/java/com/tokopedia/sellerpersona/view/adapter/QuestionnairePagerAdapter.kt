package com.tokopedia.sellerpersona.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerpersona.databinding.ItemQuestionnairePagerBinding
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class QuestionnairePagerAdapter : Adapter<QuestionnairePagerAdapter.PagerViewHolder>() {

    private val pages: MutableList<QuestionnairePagerUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQuestionnairePagerBinding.inflate(inflater, parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = pages.size

    fun setPages(pages: List<QuestionnairePagerUiModel>) {
        this.pages.clear()
        this.pages.addAll(pages)

        notifyItemRangeChanged(Int.ZERO, pages.size.minus(Int.ONE))
    }

    inner class PagerViewHolder(
        private val binding: ItemQuestionnairePagerBinding
    ) : ViewHolder(binding.root) {

        private val optionAdapter by lazy {
            OptionsAdapter()
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
                    optionAdapter.setItems(options)
                    optionAdapter.notifyItemRangeChanged(Int.ZERO,options.size.minus(Int.ONE))
                }
            }
        }
    }
}
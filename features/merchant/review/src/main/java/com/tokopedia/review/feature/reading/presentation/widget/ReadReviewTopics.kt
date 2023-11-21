package com.tokopedia.review.feature.reading.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.children
import com.google.android.material.chip.ChipGroup
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetReadReviewExtractedTopicBinding
import com.tokopedia.review.feature.reading.data.Keyword
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify

class ReadReviewTopics @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    var keywords: List<Keyword> = emptyList()
        set(value) = apply(value)

    var listener: ReadReviewFilterChipsListener? = null

    var preselectKeyword: String? = null

    private var preselectChip: Chip? = null
    private var selectedChip: Chip? = null

    private val binding = WidgetReadReviewExtractedTopicBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private fun apply(keywords: List<Keyword>) = with(binding) {
        if (keywords.isEmpty()) return
        root.show()

        keywords.forEachIndexed { index, keyword ->
            val chip = keyword.toChip()
            if (keyword.text == preselectKeyword) preselectChip = chip
            extractedTopicGroup.addView(chip.view)
            listener?.onImpressTopicChip(keyword, index)
        }

        registerBehavior()
    }

    @SuppressLint("RestrictedApi")
    private fun registerBehavior() {
        val chipGroup = binding.extractedTopicGroup
        chipGroup.addOneTimeGlobalLayoutListener {
            val preselectChip = preselectChip
            if (preselectChip == null) {
                determineExpandButton(chipGroup)
            } else {
                val preselectChipRowIndex = chipGroup.getRowIndex(preselectChip.view)
                if (preselectChipRowIndex > 0) {
                    chipGroup.expand()
                } else {
                    determineExpandButton(chipGroup)
                }
                preselectChip.select()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun determineExpandButton(chipGroup: ChipGroup) {
        val lastChip = chipGroup.children.last()
        val lastChipRowIndex = chipGroup.getRowIndex(lastChip)
        if (lastChipRowIndex > 0) showExpandButton()
    }

    private fun showExpandButton() {
        val expandButton = binding.extractedTopicExpand
        expandButton.show()
        expandButton.setOnClickListener {
            binding.extractedTopicGroup.expand()
            listener?.onClickLihatSemua()
        }
    }

    private fun ChipGroup.expand() {
        setLayoutHeight(WRAP_CONTENT)
        binding.extractedTopicExpand.hide()
    }

    private fun Keyword.toChip(): Chip {
        val chip = Chip(ChipsUnify(context), this).apply {
            view.chipText = "%s (%s)".format(text, count)
            view.setOnClickListener { onClickChip(this) }
        }
        return chip
    }

    private fun onClickChip(chip: Chip) = with(chip) {
        if (isSelected) {
            unselect()
        } else {
            select()
            listener?.onFilterTopic(chip.keyword.text)
        }
    }

    private fun Chip.select() = with(view) {
        chipType = ChipsUnify.TYPE_SELECTED
        isSelected = true

        selectedChip?.deselect()
        selectedChip = this@select
    }

    private fun Chip.unselect() = with(view) {
        deselect()

        if (selectedChip == this@unselect) {
            selectedChip = null
            listener?.onFilterTopic("")
        }
    }

    private fun Chip.deselect() = with(view) {
        chipType = ChipsUnify.TYPE_NORMAL
        isSelected = false
    }

    private data class Chip(
        val view: ChipsUnify,
        val keyword: Keyword
    )
}

package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmQuestionnaireMultipleOptionBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.QuestionnaireChipOptionAdapter
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireMultipleViewHolder(
        itemView: View,
        private val onAnswerSelected: () -> Unit
) : BaseViewHolder<QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_pm_questionnaire_multiple_option

        const val MARGIN_RIGHT_ITEM = 8
        const val MARGIN_TOP_ITEM = 12
    }

    private val binding: ItemPmQuestionnaireMultipleOptionBinding? by viewBinding()

    private var optionChipAdapter: QuestionnaireChipOptionAdapter? = null

    override fun bind(element: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel) {
        binding?.run {
            setupMultipleOptionsList(element)
            tvPmQuestionnaireTitle.text = element.question
        }
    }

    private fun ItemPmQuestionnaireMultipleOptionBinding.setupMultipleOptionsList(element: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel) {

        val layoutManagerChips = ChipsLayoutManager.newBuilder(root.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

        optionChipAdapter = QuestionnaireChipOptionAdapter(element.options, onAnswerSelected)

        rvPmQuestionnaireOptions.run {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(object: RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.right = MARGIN_RIGHT_ITEM.toPx()
                        outRect.top = MARGIN_TOP_ITEM.toPx()
                    }
                })
            }
            layoutManager = layoutManagerChips
            ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR)
            adapter = optionChipAdapter
        }
    }
}

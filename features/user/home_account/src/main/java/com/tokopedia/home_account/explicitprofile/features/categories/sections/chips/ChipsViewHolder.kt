package com.tokopedia.home_account.explicitprofile.features.categories.sections.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.ViewItemExplicitProfileCategoriesQuestionChipsBinding
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.ANSWER_NO
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.ANSWER_YES
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class ChipsViewHolder(
    itemView: View,
    private val chipsListener: QuestionChipsListener
) : BaseViewHolder(itemView) {

    private var itemViewBinding: ViewItemExplicitProfileCategoriesQuestionChipsBinding? by viewBinding()

    fun onBind(questionDataModel: QuestionDataModel) {
        itemViewBinding?.chipsCategories?.apply {
            setSelectionByResponse(questionDataModel)

            chipText = questionDataModel.property.name
            if (questionDataModel.property.image.isNotEmpty()) {
                chip_image_icon.apply {
                    loadImageWithoutPlaceholder(questionDataModel.property.image) {
                        useCache(true)
                    }
                }.show()
            } else {
                chip_image_icon.hide()
            }

            setOnClickListener {
                chipsListener.onItemClick(questionDataModel, this.isSelected) { isSelected ->

                    this.isSelected = isSelected
                    this.chipType = if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

                    questionDataModel.answerValue = if (isSelected) ANSWER_YES else ANSWER_NO
                }
            }
        }
    }

    /**
     * set default selection answers from BE
     */
    private fun setSelectionByResponse(questionDataModel: QuestionDataModel) {
        itemViewBinding?.chipsCategories?.apply {
            if (questionDataModel.answerValue == ANSWER_YES) {
                isSelected = true
                chipType = ChipsUnify.TYPE_SELECTED
            } else if (questionDataModel.answerValue == ANSWER_NO) {
                isSelected = false
                chipType = ChipsUnify.TYPE_NORMAL
            }
        }
    }

    interface QuestionChipsListener {
        fun onItemClick(questionDataModel: QuestionDataModel, isChipSelected: Boolean, updateChipsSelection: (isSelected: Boolean) -> Unit)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_item_explicit_profile_categories_question_chips
    }
}

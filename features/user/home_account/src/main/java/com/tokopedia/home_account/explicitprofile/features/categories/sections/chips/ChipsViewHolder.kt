package com.tokopedia.home_account.explicitprofile.features.categories.sections.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.ViewItemExplicitProfileCategoriesQuestionChipsBinding
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.ANSWER_NO
import com.tokopedia.home_account.explicitprofile.ExplicitProfileConstant.ANSWER_YES
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
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
//            chip_image_icon.apply {
//                loadImage(questionDataModel.property.image) {
//                    useCache(true)
//                }
//            }.show()

            itemView.setOnClickListener {
                this.isSelected = !this.isSelected
                this.chipType = if (this.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

                questionDataModel.answerValue = if (this.isSelected) ANSWER_YES else ANSWER_NO
                chipsListener.onItemClick(questionDataModel, this.isSelected)
            }
        }
    }

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
        fun onItemClick(questionDataModel: QuestionDataModel, isActive: Boolean)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_item_explicit_profile_categories_question_chips
    }
}
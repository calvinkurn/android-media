package com.tokopedia.home_account.explicitprofile.features.categories.sections

import android.view.View
import androidx.annotation.LayoutRes
import com.google.android.flexbox.*
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.LayoutExplicitProfileQuestionSectionBinding
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.SectionsDataModel
import com.tokopedia.home_account.explicitprofile.features.categories.sections.chips.ChipsAdapter
import com.tokopedia.home_account.explicitprofile.features.categories.sections.chips.ChipsViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding

class SectionViewHolder(
    itemView: View,
    private val sectionListener: SectionListener
) : BaseViewHolder(itemView), ChipsViewHolder.QuestionChipsListener {

    private val itemViewBinding: LayoutExplicitProfileQuestionSectionBinding? by viewBinding()
    private val chipsAdapter = ChipsAdapter(this)
    private val flexboxLayoutManager = FlexboxLayoutManager(itemView.context)

    fun onBind(sectionsDataModel: SectionsDataModel) {
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START

        itemViewBinding?.apply {
            sectionTitle.text = sectionsDataModel.property.title
            sectionQuestionList.apply {
                layoutManager = flexboxLayoutManager
                adapter = chipsAdapter
            }

            chipsAdapter.clearAllItems()
            chipsAdapter.addItems(sectionsDataModel.questions)

            if (sectionsDataModel.property.infoHeader.isEmpty()) {
                sectionInfoIcon.hide()
            } else {
                sectionInfoIcon.setOnClickListener { sectionListener.onClickInfo(sectionsDataModel) }
            }
        }
    }

    override fun onItemClick(questionDataModel: QuestionDataModel, isActive: Boolean) {
        sectionListener.onQuestionSelected(questionDataModel, isActive)
    }

    interface SectionListener {
        fun onClickInfo(sectionsDataModel: SectionsDataModel)
        fun onQuestionSelected(questionDataModel: QuestionDataModel, isSelected: Boolean)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_explicit_profile_question_section
    }
}
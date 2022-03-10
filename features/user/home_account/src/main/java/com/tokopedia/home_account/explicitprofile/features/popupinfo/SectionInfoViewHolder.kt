package com.tokopedia.home_account.explicitprofile.features.popupinfo

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.ViewItemExplicitProfileSectionInfoBinding
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.utils.view.binding.viewBinding

class SectionInfoViewHolder(
    itemView: View
) : BaseViewHolder(itemView) {

    private val itemViewBinding: ViewItemExplicitProfileSectionInfoBinding? by viewBinding()

    fun onBind(questionDataModel: QuestionDataModel) {
        itemViewBinding?.apply {
            sectionInfoTitle.text = questionDataModel.property.name
            sectionInfoDescription.text = questionDataModel.property.infoContent
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_item_explicit_profile_section_info
    }
}
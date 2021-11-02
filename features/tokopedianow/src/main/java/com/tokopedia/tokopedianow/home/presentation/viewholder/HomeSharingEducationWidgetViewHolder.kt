package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeSharingEducationWidgetBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingEducationWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeSharingEducationWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingEducationListener? = null
) : AbstractViewHolder<HomeSharingEducationWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_sharing_education_widget
        const val IMG_SHARING_EDUCATION = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_education.png"
    }

    private var binding: ItemTokopedianowHomeSharingEducationWidgetBinding? by viewBinding()

    override fun bind(element: HomeSharingEducationWidgetUiModel) {
        binding?.apply {
            if (element.state == HomeLayoutItemState.LOADED) {
                cvSharingEducation.show()
                setupUi()
                btnSharingEducation.setOnClickListener {
                    listener?.onShareBtnSharingEducationClicked()
                }
                iCloseSharingEducation.setOnClickListener {
                    listener?.onCloseBtnSharingEducationClicked(element.id)
                }
            }
        }
    }

    private fun setupUi() {
        binding?.apply {
            iuSharingEducation.setImageUrl(IMG_SHARING_EDUCATION)
            tpSharingEducation.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_sharing_education_title))
            iCloseSharingEducation.setImage(IconUnify.CLOSE)
            iCloseSharingEducation.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
        }
    }

    interface HomeSharingEducationListener {
        fun onShareBtnSharingEducationClicked()
        fun onCloseBtnSharingEducationClicked(id: String)
    }
}
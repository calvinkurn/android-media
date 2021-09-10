package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingEducationWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class HomeSharingEducationWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingEducationListener? = null
) : AbstractViewHolder<HomeSharingEducationWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_sharing_education_widget
        const val IMG_SHARING_EDUCATION = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_education.png"
    }

    private var iuSharingEducation: ImageUnify? = null
    private var tpSharingEducation: Typography? = null
    private var btnSharingEducation: UnifyButton? = null
    private var iCloseSharingEducation: IconUnify? = null

    init {
        iuSharingEducation = itemView.findViewById(R.id.iu_sharing_education)
        tpSharingEducation = itemView.findViewById(R.id.tp_sharing_education)
        btnSharingEducation = itemView.findViewById(R.id.btn_sharing_education)
        iCloseSharingEducation = itemView.findViewById(R.id.i_close_sharing_education)
    }

    override fun bind(element: HomeSharingEducationWidgetUiModel) {
        setupUi()
        btnSharingEducation?.setOnClickListener {
            listener?.onShareBtnSharingEducationClicked()
        }
        iCloseSharingEducation?.setOnClickListener {
            listener?.onCloseBtnSharingEducationClicked(element.id)
        }
    }

    private fun setupUi() {
        iuSharingEducation?.setImageUrl(IMG_SHARING_EDUCATION)
        tpSharingEducation?.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_home_sharing_education_title))
        iCloseSharingEducation?.setImage(IconUnify.CLOSE)
        iCloseSharingEducation?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
    }

    interface HomeSharingEducationListener {
        fun onShareBtnSharingEducationClicked()
        fun onCloseBtnSharingEducationClicked(id: String)
    }
}
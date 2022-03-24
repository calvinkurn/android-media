package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.SHARING_WIDGET_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeFormattedCopy
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeSharingWidgetBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeSharingWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingEducationListener? = null
) : AbstractViewHolder<HomeSharingWidgetUiModel>(itemView) {

    companion object {
        private const val IMG_SHARING_EDUCATION = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_education.png"
        private const val DEFAULT_DESC_RES = -1

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_sharing_widget
    }

    private var binding: ItemTokopedianowHomeSharingWidgetBinding? by viewBinding()

    override fun bind(element: HomeSharingWidgetUiModel) {
        binding?.apply {
            if (element.state == HomeLayoutItemState.LOADED) {
                cvSharingEducation.show()
                when(element) {
                    is HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel -> {
                        setupUi(
                            descRes = element.descRes,
                            btnTextRes = element.btnTextRes
                        )
                    }
                    is HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel -> {
                        setupUi(
                            serviceType = element.serviceType,
                            btnTextRes = element.btnTextRes
                        )
                    }
                }
                btnSharingEducation.setOnClickListener {
                    listener?.onShareBtnSharingEducationClicked()
                }
                iCloseSharingEducation.setOnClickListener {
                    listener?.onCloseBtnSharingEducationClicked(element.id)
                }
            }
        }
    }

    private fun setupUi(serviceType: String = "", descRes: Int = DEFAULT_DESC_RES, btnTextRes: Int) {
        binding?.apply {
            tpSharingEducation.text = if (descRes == DEFAULT_DESC_RES) {
                MethodChecker.fromHtml(
                    getServiceTypeFormattedCopy(
                        context = root.context,
                        key = SHARING_WIDGET_RESOURCE_ID,
                        serviceType = serviceType
                    )
                )
            } else {
                itemView.resources.getString(descRes)
            }

            btnSharingEducation.text = itemView.resources.getString(btnTextRes)
            iuSharingEducation.setImageUrl(IMG_SHARING_EDUCATION)
            iCloseSharingEducation.setImage(IconUnify.CLOSE)
            iCloseSharingEducation.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
        }
    }

    interface HomeSharingEducationListener {
        fun onShareBtnSharingEducationClicked()
        fun onCloseBtnSharingEducationClicked(id: String)
    }
}
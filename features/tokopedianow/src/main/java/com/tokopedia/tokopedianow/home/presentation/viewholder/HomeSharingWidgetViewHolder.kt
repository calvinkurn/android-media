package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.SHARING_WIDGET_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeFormattedCopy
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeSharingWidgetBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.REFERRAL_PAGE_URL
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class HomeSharingWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingListener? = null
) : AbstractViewHolder<HomeSharingWidgetUiModel>(itemView) {

    companion object {
        private const val IMG_SHARING_EDUCATION = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_education.png"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_sharing_widget
    }

    private var binding: ItemTokopedianowHomeSharingWidgetBinding? by viewBinding()

    override fun bind(element: HomeSharingWidgetUiModel) {
        if (element.state == HomeLayoutItemState.LOADED) {
            checkUiModel(element)
        }
    }

    private fun checkUiModel(element: HomeSharingWidgetUiModel) {
        binding?.apply {
            cvSharingEducation.show()
            when(element) {
                is HomeSharingReferralWidgetUiModel -> {
                    setReferralData(element)
                    setReferralListener(element)
                }
                is HomeSharingEducationWidgetUiModel -> {
                    setEducationalInfoData(element)
                    setEducationalInfoListener(element)
                }
            }
        }
    }

    private fun setReferralData(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            iCloseSharingEducation.hide()
            if (element.isSender) {
                convertStringToLink(tpSharingEducation, itemView.context, R.string.tokopedianow_home_referral_widget_desc_sender, element.slug)
                btnSharingEducation.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_sender)
            } else {
                tpSharingEducation.text = MethodChecker.fromHtml(itemView.resources.getString(R.string.tokopedianow_home_referral_widget_desc_receiver))
                btnSharingEducation.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_receiver)
            }
            btnSharingEducation.isLoading = element.isButtonLoading
            iuSharingEducation.setImageUrl(IMG_SHARING_EDUCATION)
        }
    }

    private fun setReferralListener(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            btnSharingEducation.setOnClickListener {
                if (element.isSender) {
                    listener?.onShareBtnSharingReferralClicked(element.slug, element.isSender)
                } else {
                    goToInformationPage(REFERRAL_PAGE_URL+element.slug)
                }
            }
        }
    }

    private fun setEducationalInfoData(element: HomeSharingEducationWidgetUiModel) {
        binding?.apply {
            iCloseSharingEducation.setImage(IconUnify.CLOSE)
            iCloseSharingEducation.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
            tpSharingEducation.text = MethodChecker.fromHtml(
                getServiceTypeFormattedCopy(
                    context = root.context,
                    key = SHARING_WIDGET_RESOURCE_ID,
                    serviceType = element.serviceType
                )
            )
            btnSharingEducation.text = itemView.resources.getString(element.btnTextRes)
            iuSharingEducation.setImageUrl(IMG_SHARING_EDUCATION)
        }
    }

    private fun setEducationalInfoListener(element: HomeSharingEducationWidgetUiModel) {
        binding?.apply {
            btnSharingEducation.setOnClickListener {
                listener?.onShareBtnSharingEducationalInfoClicked()
            }

            iCloseSharingEducation.setOnClickListener {
                listener?.onCloseBtnSharingEducationalInfoClicked(element.id)
            }
        }
    }

    private fun convertStringToLink(typography: Typography, context: Context, stringRes: Int, slug: String) {
        val greenColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500).toString()
        val linkHelper = HtmlLinkHelper(context, context.getString(stringRes, greenColor, REFERRAL_PAGE_URL+slug))
        typography.text = linkHelper.spannedString
        typography.movementMethod = LinkMovementMethod.getInstance()
        linkHelper.urlList[0].let { link ->
            link.onClick = {
                goToInformationPage(link.linkUrl)
            }
        }
    }

    private fun goToInformationPage(linkUrl: String) {
        RouteManager.route(itemView.context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
    }

    interface HomeSharingListener {
        fun onShareBtnSharingReferralClicked(slug: String, isSender: Boolean)
        fun onShareBtnSharingEducationalInfoClicked()
        fun onCloseBtnSharingEducationalInfoClicked(id: String)
    }
}
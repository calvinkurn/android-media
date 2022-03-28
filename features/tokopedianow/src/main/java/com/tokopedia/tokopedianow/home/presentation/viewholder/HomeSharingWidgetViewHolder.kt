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
                is HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel -> {
                    setReferralData(
                        isSender = element.isSender,
                        slug = element.slug
                    )
                    setReferralListener(
                        slug = element.slug,
                        isSender = element.isSender
                    )
                }
                is HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel -> {
                    setEducationalInfoData(
                        serviceType = element.serviceType,
                        btnTextRes = element.btnTextRes
                    )
                    setEducationalInfoListener(
                        elementId = element.id
                    )
                }
            }
        }
    }

    private fun setReferralListener(slug: String, isSender: Boolean) {
        binding?.apply {
            btnSharingEducation.setOnClickListener {
                if (isSender) {
                    listener?.onShareBtnSharingReferralClicked(slug)
                } else {
                    goToInformationPage(REFERRAL_PAGE_URL+slug)
                }
            }
        }
    }

    private fun setEducationalInfoListener(elementId: String) {
        binding?.apply {
            btnSharingEducation.setOnClickListener {
                listener?.onShareBtnSharingEducationalInfoClicked()
            }

            iCloseSharingEducation.setOnClickListener {
                listener?.onCloseBtnSharingEducationalInfoClicked(elementId)
            }
        }
    }

    private fun setReferralData(isSender: Boolean, slug: String) {
        binding?.apply {
            iCloseSharingEducation.hide()
            if (isSender) {
                convertStringToLink(tpSharingEducation, itemView.context, R.string.tokopedianow_home_referral_widget_desc_sender, slug)
                btnSharingEducation.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_sender)
            } else {
                tpSharingEducation.text = MethodChecker.fromHtml(itemView.resources.getString(R.string.tokopedianow_home_referral_widget_desc_receiver))
                btnSharingEducation.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_receiver)
            }
            iuSharingEducation.setImageUrl(IMG_SHARING_EDUCATION)
        }
    }

    private fun setEducationalInfoData(serviceType: String, btnTextRes: Int) {
        binding?.apply {
            iCloseSharingEducation.setImage(IconUnify.CLOSE)
            iCloseSharingEducation.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
            tpSharingEducation.text = MethodChecker.fromHtml(
                getServiceTypeFormattedCopy(
                    context = root.context,
                    key = SHARING_WIDGET_RESOURCE_ID,
                    serviceType = serviceType
                )
            )
            btnSharingEducation.text = itemView.resources.getString(btnTextRes)
            iuSharingEducation.setImageUrl(IMG_SHARING_EDUCATION)
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
        fun onShareBtnSharingReferralClicked(slug: String)
        fun onShareBtnSharingEducationalInfoClicked()
        fun onCloseBtnSharingEducationalInfoClicked(id: String)
    }
}
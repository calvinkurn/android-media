package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.SHARING_WIDGET_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeFormattedCopy
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeSharingWidgetBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.REFERRAL_PAGE_URL
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingEducationWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class HomeSharingWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingListener? = null
) : AbstractViewHolder<HomeSharingWidgetUiModel>(itemView) {

    companion object {
        private const val IMG_SHARING_EDUCATION = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_education.png"
        private const val IMG_SHARING_REFERRAL_RECEIVER = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_referral_receiver.png"
        private const val IMG_SHARING_REFERRAL_SENDER = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_referral_sender.png"

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
            iuCloseSharing.hide()
        }

        if (element.isSender) {
            setSenderData(element)
        } else {
            setReceiverData(element)
        }
        shareReferralWidgetImpressed(element)
    }

    private fun setSenderData(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            convertStringToLink(
                typography = tpSharing,
                stringRes = R.string.tokopedianow_home_referral_widget_desc_sender,
                referral = element
            )
            btnSharing.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_sender)
            iuSharing.loadImage(IMG_SHARING_REFERRAL_SENDER)
            ivBgImageLeft.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_sender_supergraphic_left))
            ivBgImageRight.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_sender_supergraphic_right))
        }
    }

    private fun setReceiverData(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            tpSharing.text = MethodChecker.fromHtml(itemView.resources.getString(
                R.string.tokopedianow_home_referral_widget_desc_receiver, element.maxReward))
            btnSharing.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_receiver)
            iuSharing.loadImage(IMG_SHARING_REFERRAL_RECEIVER)
            ivBgImageLeft.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_receiver_supergraphic_left))
            ivBgImageRight.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_receiver_supergraphic_right))
        }
    }

    private fun shareReferralWidgetImpressed(element: HomeSharingReferralWidgetUiModel) {
        if (!element.isDisplayed && element.userStatus.isNotBlank()) {
            if (element.isSender) {
                listener?.onShareReferralSenderWidgetImpressed(element)
            } else {
                listener?.onShareReferralReceiverWidgetImpressed(element)
            }
        }
        element.display()
    }

    private fun createVectorDrawableCompat(resId: Int): VectorDrawableCompat? {
        return VectorDrawableCompat.create(itemView.resources, resId, itemView.context.theme)
    }

    private fun setReferralListener(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            btnSharing.setOnClickListener {
                if (element.isSender) {
                    listener?.onShareBtnReferralSenderClicked(element)
                } else {
                    listener?.onShareBtnReferralReceiverClicked(element)
                }
            }
        }
    }

    private fun setEducationalInfoData(element: HomeSharingEducationWidgetUiModel) {
        binding?.apply {
            iuCloseSharing.show()
            ivBgImageTop.show()
            ivBgImageLeft.hide()
            ivBgImageRight.hide()
            iuCloseSharing.setImage(IconUnify.CLOSE)
            iuCloseSharing.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
            tpSharing.text = MethodChecker.fromHtml(
                getServiceTypeFormattedCopy(
                    context = root.context,
                    key = SHARING_WIDGET_RESOURCE_ID,
                    serviceType = element.serviceType
                )
            )
            btnSharing.text = itemView.resources.getString(element.btnTextRes)
            iuSharing.loadImage(IMG_SHARING_EDUCATION)
        }
    }

    private fun setEducationalInfoListener(element: HomeSharingEducationWidgetUiModel) {
        binding?.apply {
            btnSharing.setOnClickListener {
                listener?.onShareBtnSharingEducationalInfoClicked()
            }

            iuCloseSharing.setOnClickListener {
                listener?.onCloseBtnSharingEducationalInfoClicked(element.id)
            }
        }
    }

    private fun convertStringToLink(
        typography: Typography,
        stringRes: Int,
        referral: HomeSharingReferralWidgetUiModel
    ) {
        val context = itemView.context
        val urlParam = REFERRAL_PAGE_URL + referral.slug
        val greenColor = ContextCompat.getColor(
            context, com.tokopedia.unifyprinciples.R.color.Unify_GN500).toString()
        val linkHelper = HtmlLinkHelper(context, context.getString(stringRes, greenColor, urlParam, referral.maxReward))
        typography.text = linkHelper.spannedString
        typography.movementMethod = LinkMovementMethod.getInstance()
        linkHelper.urlList[0].let { link ->
            link.onClick = {
                listener?.onMoreReferralClicked(referral, link.linkUrl)
            }
        }
    }

    interface HomeSharingListener {
        fun onMoreReferralClicked(referral: HomeSharingReferralWidgetUiModel, linkUrl: String)
        fun onShareBtnReferralSenderClicked(referral: HomeSharingReferralWidgetUiModel)
        fun onShareBtnReferralReceiverClicked(referral: HomeSharingReferralWidgetUiModel)
        fun onShareReferralSenderWidgetImpressed(referral: HomeSharingReferralWidgetUiModel)
        fun onShareReferralReceiverWidgetImpressed(referral: HomeSharingReferralWidgetUiModel)
        fun onShareBtnSharingEducationalInfoClicked()
        fun onCloseBtnSharingEducationalInfoClicked(id: String)
    }
}
package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
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
            btnSharing.isLoading = element.isButtonLoading
        }

        if (element.isSender) {
            setSenderData(element)
        } else {
            setReceiverData()
        }
        shareReferralWidgetImpressed(element)
    }

    private fun setSenderData(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            convertStringToLink(
                typography = tpSharing,
                context = itemView.context,
                stringRes = R.string.tokopedianow_home_referral_widget_desc_sender,
                slug = element.slug,
                isSender = element.isSender,
                campaignCode = element.campaignCode,
                warehouseId = element.warehouseId,
                userStatus = element.userStatus
            )
            btnSharing.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_sender)
            iuSharing.loadImage(IMG_SHARING_REFERRAL_SENDER)
            ivBgImageLeft.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_sender_supergraphic_left))
            ivBgImageRight.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_sender_supergraphic_right))
        }
    }

    private fun setReceiverData() {
        binding?.apply {
            tpSharing.text = MethodChecker.fromHtml(itemView.resources.getString(R.string.tokopedianow_home_referral_widget_desc_receiver))
            btnSharing.text = itemView.resources.getString(R.string.tokopedianow_home_referral_widget_button_text_receiver)
            iuSharing.loadImage(IMG_SHARING_REFERRAL_RECEIVER)
            ivBgImageLeft.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_receiver_supergraphic_left))
            ivBgImageRight.loadImage(createVectorDrawableCompat(R.drawable.tokopedianow_bg_receiver_supergraphic_right))
        }
    }

    private fun shareReferralWidgetImpressed(element: HomeSharingReferralWidgetUiModel) {
        if (!element.isDisplayed && element.userStatus.isNotBlank()) {
            if (element.isSender) {
                listener?.onShareReferralSenderWidgetImpressed(
                    element.slug,
                    element.isSender,
                    element.campaignCode,
                    element.warehouseId,
                    element.userStatus
                )
            } else {
                listener?.onShareReferralReceiverWidgetImpressed(
                    element.slug,
                    element.isSender,
                    element.campaignCode,
                    element.warehouseId,
                    element.userStatus
                )
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
                    listener?.onShareBtnReferralSenderClicked(
                        element.slug
                    )
                } else {
                    listener?.onShareBtnReferralReceiverClicked(
                        element.slug,
                        element.isSender,
                        element.campaignCode,
                        element.warehouseId,
                        element.userStatus
                    )
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
        context: Context,
        stringRes: Int,
        slug: String,
        isSender: Boolean,
        campaignCode: String,
        warehouseId: String,
        userStatus: String
    ) {
        val greenColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500).toString()
        val urlParam = REFERRAL_PAGE_URL+slug
        val linkHelper = HtmlLinkHelper(context, context.getString(stringRes, greenColor, urlParam.encodeToUtf8()))
        typography.text = linkHelper.spannedString
        typography.movementMethod = LinkMovementMethod.getInstance()
        linkHelper.urlList[0].let { link ->
            link.onClick = {
                listener?.onMoreReferralClicked(slug, isSender, campaignCode, warehouseId, link.linkUrl, userStatus)
            }
        }
    }

    interface HomeSharingListener {
        fun onMoreReferralClicked(slug: String, isSender: Boolean, campaignCode: String, warehouseId: String, linkUrl: String, userStatus: String)
        fun onShareBtnReferralSenderClicked(slug: String)
        fun onShareBtnReferralReceiverClicked(slug: String, isSender: Boolean, campaignCode: String, warehouseId: String, userStatus: String)
        fun onShareReferralSenderWidgetImpressed(slug: String, isSender: Boolean, campaignCode: String, warehouseId: String, userStatus: String)
        fun onShareReferralReceiverWidgetImpressed(slug: String, isSender: Boolean, campaignCode: String, warehouseId: String, userStatus: String)
        fun onShareBtnSharingEducationalInfoClicked()
        fun onCloseBtnSharingEducationalInfoClicked(id: String)
    }
}
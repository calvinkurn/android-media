package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
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
import com.tokopedia.utils.view.binding.viewBinding

class HomeSharingWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingListener? = null
) : AbstractViewHolder<HomeSharingWidgetUiModel>(itemView) {

    companion object {
        private const val IMG_SHARING_EDUCATION = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_sharing_education.png"
        private const val LOTTIE_REFERRAL = "https://assets.tokopedia.net/asts/lottie/android/tokonow/tokonow_animation_referral.json"
        private const val IMG_SHARING_REFERRAL_BG_BTM = "https://images.tokopedia.net/img/tokonow/tokonow/bg_referral_btm/Fill.png"
        private const val IMG_SHARING_REFERRAL_BG_TOP = "https://images.tokopedia.net/img/tokonow/tokonow_bg_referral_top/Line.png"
        private const val ANIMATION_REPEAT_COUNT = 1

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_sharing_widget
    }

    private var binding: ItemTokopedianowHomeSharingWidgetBinding? by viewBinding()

    override fun bind(element: HomeSharingWidgetUiModel) {
        if (element.state == HomeLayoutItemState.LOADED) {
            checkUiModel(element)
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.apply {
            lottieReferral.clearAnimation()
        }
    }

    private fun checkUiModel(element: HomeSharingWidgetUiModel) {
        binding?.apply {
            cvSharingEducation.show()
            when (element) {
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
            val context = itemView.context
            val greenColor = ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            ).toString()
            tpSharing.text = MethodChecker.fromHtml(context.getString(R.string.tokopedianow_home_referral_widget_desc_sender, greenColor))
            btnSharing.text = getString(R.string.tokopedianow_home_referral_widget_button_text_sender)
            if (element.titleSection.isNotEmpty()) {
                tpTitle.visible()
                tpTitle.text = element.titleSection
            }

            containerWidgetSharing.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50))
            iuSharing.hide()
            playAnimation()
            ivBgImageBtm.loadImage(IMG_SHARING_REFERRAL_BG_BTM)
            ivBgImageTopRight.loadImage(IMG_SHARING_REFERRAL_BG_TOP)
        }
    }

    private fun setReceiverData(element: HomeSharingReferralWidgetUiModel) {
        binding?.apply {
            tpSharing.text = MethodChecker.fromHtml(itemView.resources.getString(
                R.string.tokopedianow_home_referral_widget_desc_receiver, element.ogDescription))
            btnSharing.hide()
            containerWidgetSharing.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50))
            if (element.titleSection.isNotEmpty()) {
                tpTitle.visible()
                tpTitle.text = element.titleSection
            }
            iuSharing.hide()
            playAnimation()
            ivBgImageBtm.loadImage(IMG_SHARING_REFERRAL_BG_BTM)
            ivBgImageTopRight.loadImage(IMG_SHARING_REFERRAL_BG_TOP)
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

    private fun playAnimation() {
        binding?.apply {
            lottieReferral.setAnimationFromUrl(LOTTIE_REFERRAL)
            lottieReferral.playAnimation()
            lottieReferral.repeatCount = ANIMATION_REPEAT_COUNT
        }
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

            if (element.isSender) {
                containerWidgetSharing.setOnClickListener {
                    val url = REFERRAL_PAGE_URL + element.slug
                    listener?.onMoreReferralClicked(element, url)
                }
            }
        }
    }

    private fun setEducationalInfoData(element: HomeSharingEducationWidgetUiModel) {
        binding?.apply {
            iuCloseSharing.show()
            ivBgImageTop.show()
            ivBgImageBtm.hide()
            ivBgImageTopRight.hide()
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

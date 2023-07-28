package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.animation.ObjectAnimator
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_FREE_SHIPPING_DARK_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_FREE_SHIPPING_LIGHT_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_GUARANTEED_QUALITY_DARK_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_GUARANTEED_QUALITY_LIGHT_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_STOCK_AVAILABLE_DARK_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_STOCK_AVAILABLE_LIGHT_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_TIME_DARK_MODE
import com.tokopedia.imageassets.TokopediaImageUrl.IMG_USP_NOW_TIME_LIGHT_MODE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_WIDGET_DURATION_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_WIDGET_SELECTED_PRODUCT_FREE_SHIPPING_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeEducationalInformationWidgetBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class HomeEducationalInformationWidgetViewHolder(
    itemView: View,
    private val listener: HomeEducationalInformationListener? = null
) : AbstractViewHolder<HomeEducationalInformationWidgetUiModel>(itemView) {

    companion object {
        private const val CHEVRON_ANIMATION_PROPERTY_NAME_TARGETED = "translationY"
        private const val CHEVRON_ANIMATION_START_TRANSLATION_VALUE = -10F
        private const val CHEVRON_ANIMATION_END_TRANSLATION_VALUE = 10F
        private const val CHEVRON_ANIMATION_DEFAULT_TRANSLATION_VALUE = 0F
        private const val CHEVRON_ANIMATION_DURATION = 600L

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_educational_information_widget
    }

    private var binding: ItemTokopedianowHomeEducationalInformationWidgetBinding? by viewBinding()
    private var chevronAnimation: ObjectAnimator? = null

    override fun bind(element: HomeEducationalInformationWidgetUiModel) {
        binding?.apply {
            setupUi(element.serviceType)
            root.addOnImpressionListener(element) {
                listener?.onEducationInformationWidgetImpressed()
            }
        }
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setupUi(serviceType: String) {
        setupTexts(serviceType)
        setupImages(serviceType)
        setupChevron()
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setDurationText(serviceType: String) {
        getServiceTypeRes(
            key = EDU_WIDGET_DURATION_RESOURCE_ID,
            serviceType = serviceType
        )?.let {
            tpTime.text = getString(it)
        }
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setSelectedProductFreeShippingText(serviceType: String) {
        getServiceTypeRes(
            key = EDU_WIDGET_SELECTED_PRODUCT_FREE_SHIPPING_RESOURCE_ID,
            serviceType = serviceType
        )?.let {
            tpSelectedProductFreeShipping.text = getString(it)
        }
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setupTexts(
        serviceType: String
    ) {
        setDurationText(serviceType)
        setSelectedProductFreeShippingText(serviceType)
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setupImages(
        serviceType: String
    ) {
        if (root.context.isDarkMode()) {
            iuTime.setImageUrl(IMG_USP_NOW_TIME_DARK_MODE)
            iuSelectedProductFreeShipping.setImageUrl(if (serviceType == ServiceType.NOW_15M) IMG_USP_NOW_STOCK_AVAILABLE_DARK_MODE  else IMG_USP_NOW_FREE_SHIPPING_DARK_MODE)
            iuGuaranteedQuality.setImageUrl(IMG_USP_NOW_GUARANTEED_QUALITY_DARK_MODE)
        } else {
            iuTime.setImageUrl(IMG_USP_NOW_TIME_LIGHT_MODE)
            iuSelectedProductFreeShipping.setImageUrl(if (serviceType == ServiceType.NOW_15M) IMG_USP_NOW_STOCK_AVAILABLE_LIGHT_MODE  else IMG_USP_NOW_FREE_SHIPPING_LIGHT_MODE)
            iuGuaranteedQuality.setImageUrl(IMG_USP_NOW_GUARANTEED_QUALITY_LIGHT_MODE)
        }
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setupChevron() {
        if(listener?.isEducationInformationLottieStopped() == true) {
            setChevronDefault()
        } else {
            setChevronAnimation()
            setChevronDefault()
        }
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setChevronAnimation() {
        chevronAnimation = ObjectAnimator.ofFloat(
            sivChevronDown,
            CHEVRON_ANIMATION_PROPERTY_NAME_TARGETED,
            CHEVRON_ANIMATION_START_TRANSLATION_VALUE,
            CHEVRON_ANIMATION_END_TRANSLATION_VALUE
        ).apply {
            duration = CHEVRON_ANIMATION_DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun ItemTokopedianowHomeEducationalInformationWidgetBinding.setChevronDefault() {
        val unifyColor = ContextCompat.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )

        sivChevronDown.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            unifyColor,
            BlendModeCompat.SRC_ATOP
        )

        sivChevronDown.setOnClickListener {
            if (chevronAnimation?.isRunning == true) {
                chevronAnimation?.cancel()
                chevronAnimation = null
                sivChevronDown.translationY = CHEVRON_ANIMATION_DEFAULT_TRANSLATION_VALUE
            }

            RouteManager.route(itemView.context, EDUCATIONAL_INFO)
            listener?.onEducationInformationDropDownClicked()
        }
    }

    interface HomeEducationalInformationListener {
        fun isEducationInformationLottieStopped(): Boolean
        fun onEducationInformationLottieClicked()
        fun onEducationInformationWidgetImpressed()
        fun onEducationInformationDropDownClicked()
    }
}

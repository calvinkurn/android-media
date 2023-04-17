package com.tokopedia.tokopedianow.home.presentation.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_WIDGET_DURATION_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.EDU_WIDGET_SELECTED_PRODUCT_FREE_SHIPPING_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeEducationalInformationWidgetBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeEducationalInformationWidgetViewHolder(
    itemView: View,
    private val listener: HomeEducationalInformationListener? = null
) : AbstractViewHolder<HomeEducationalInformationWidgetUiModel>(itemView) {

    companion object {
        private const val LOTTIE = TokopediaImageUrl.LOTTIE
        private const val IMG_TIME = TokopediaImageUrl.IMG_TIME
        private const val IMG_STOCK_AVAILABLE = TokopediaImageUrl.IMG_STOCK_AVAILABLE
        private const val IMG_GUARANTEED_QUALITY = TokopediaImageUrl.IMG_GUARANTEED_QUALITY
        private const val IMG_FREE_SHIPPING = TokopediaImageUrl.IMG_FREE_SHIPPING

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_educational_information_widget
    }

    private var binding: ItemTokopedianowHomeEducationalInformationWidgetBinding? by viewBinding()

    override fun bind(element: HomeEducationalInformationWidgetUiModel) {
        if (element.state == HomeLayoutItemState.LOADED) {
            setupUi(element.serviceType)
            listener?.onEducationInformationWidgetImpressed()
        }
    }

    private fun setupUi(serviceType: String) {
        binding?.apply {
            cvEducationalInfo.show()

            setDurationText(serviceType)
            setSelectedProductFreeShippingText(serviceType)

            iuTime.setImageUrl(IMG_TIME)
            iuSelectedProductFreeShipping.setImageUrl(if (serviceType == ServiceType.NOW_15M) IMG_STOCK_AVAILABLE  else IMG_FREE_SHIPPING)
            iuGuaranteedQuality.setImageUrl(IMG_GUARANTEED_QUALITY)
        }

        if(listener?.isEducationInformationLottieStopped() == true) {
            setupBasicButton()
        } else {
            setupLottie()
        }
    }

    private fun setDurationText(serviceType: String) {
        getServiceTypeRes(
            key = EDU_WIDGET_DURATION_RESOURCE_ID,
            serviceType = serviceType
        )?.let {
            binding?.tpTime?.text = getString(it)
        }
    }

    private fun setSelectedProductFreeShippingText(serviceType: String) {
        getServiceTypeRes(
            key = EDU_WIDGET_SELECTED_PRODUCT_FREE_SHIPPING_RESOURCE_ID,
            serviceType = serviceType
        )?.let {
            binding?.tpSelectedProductFreeShipping?.text = getString(it)
        }
    }

    private fun setupLottie() {
        itemView.context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, LOTTIE)

            binding?.apply {
                lottieCompositionLottieTask.addListener { result ->
                    laChevron.setComposition(result)
                    laChevron.playAnimation()
                    laChevron.repeatCount = LottieDrawable.INFINITE
                }

                laChevron.setOnClickListener {
                    showBottomSheet()
                    listener?.onEducationInformationLottieClicked()
                }
            }
        }
    }

    private fun setupBasicButton() {
        binding?.apply {
            laChevron.gone()
            sivChevronDown.show()
            val unifyColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            sivChevronDown.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(unifyColor, BlendModeCompat.SRC_ATOP)
            sivChevronDown.setOnClickListener {
                showBottomSheet()
                listener?.onEducationInformationDropDownClicked()
            }
        }
    }

    private fun showBottomSheet() {
        setupBasicButton()
        RouteManager.route(itemView.context, EDUCATIONAL_INFO)
    }

    interface HomeEducationalInformationListener {
        fun isEducationInformationLottieStopped(): Boolean
        fun onEducationInformationLottieClicked()
        fun onEducationInformationWidgetImpressed()
        fun onEducationInformationDropDownClicked()
    }
}
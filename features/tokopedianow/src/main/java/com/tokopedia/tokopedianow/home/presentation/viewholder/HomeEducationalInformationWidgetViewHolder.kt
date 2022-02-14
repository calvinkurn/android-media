package com.tokopedia.tokopedianow.home.presentation.viewholder

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
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeEducationalInformationWidgetBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeEducationalInformationWidgetViewHolder(
    itemView: View,
    private val listener: HomeEducationalInformationListener? = null
) : AbstractViewHolder<HomeEducationalInformationWidgetUiModel>(itemView) {

    companion object {
        private const val LOTTIE = "https://assets.tokopedia.net/asts/android/tokonow/tokopedianow_educational_information_chevron_lottie.json"
        private const val IMG_TWO_HOURS = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_educational_information_two_hours.png"
        private const val IMG_STOCK_AVAILABLE = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_educational_information_stock_available.png"
        private const val IMG_GUARANTEED_QUALITY = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_educational_information_guaranteed_quality.png"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_educational_information_widget
    }

    private var binding: ItemTokopedianowHomeEducationalInformationWidgetBinding? by viewBinding()

    override fun bind(element: HomeEducationalInformationWidgetUiModel) {
        if (element.state == HomeLayoutItemState.LOADED) {
            setupUi()
            listener?.onEducationInformationWidgetImpressed()
        }
    }

    private fun setupUi() {
        binding?.apply {
            cvEducationalInfo.show()
            iuTwoHours.setImageUrl(IMG_TWO_HOURS)
            iuStockAvailable.setImageUrl(IMG_STOCK_AVAILABLE)
            iuGuaranteedQuality.setImageUrl(IMG_GUARANTEED_QUALITY)
        }

        if(listener?.isEducationInformationLottieStopped() == true) {
            setupBasicButton()
        } else {
            setupLottie()
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
    }
}
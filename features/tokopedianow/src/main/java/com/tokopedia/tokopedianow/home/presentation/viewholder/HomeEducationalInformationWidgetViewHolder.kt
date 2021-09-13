package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.bottomsheet.TokoNowHomeEducationalInformationBottomSheet
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify


class HomeEducationalInformationWidgetViewHolder(
    itemView: View,
    private val tokoNowListener: TokoNowView? = null
) : AbstractViewHolder<HomeEducationalInformationWidgetUiModel>(itemView) {

    companion object {
        private const val LOTTIE = "https://assets.tokopedia.net/asts/android/tokonow/tokopedianow_educational_information_chevron_lottie.json"
        private const val IMG_TWO_HOURS = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_educational_information_two_hours.png"
        private const val IMG_STOCK_AVAILABLE = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_educational_information_stock_available.png"
        private const val IMG_GUARANTEED_QUALITY = "https://images.tokopedia.net/img/android/tokonow/tokonow_ic_educational_information_guaranteed_quality.png"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_educational_information_widget
    }

    private var iuTwoHours: ImageUnify? = null
    private var iuStockAvailable: ImageUnify? = null
    private var iuGuaranteedQuality: ImageUnify? = null
    private var laChevron: LottieAnimationView? = null

    init {
        iuTwoHours = itemView.findViewById(R.id.iu_two_hours)
        iuStockAvailable = itemView.findViewById(R.id.iu_stock_available)
        iuGuaranteedQuality = itemView.findViewById(R.id.iu_guaranteed_quality)
        laChevron = itemView.findViewById(R.id.la_chevron)
    }

    override fun bind(element: HomeEducationalInformationWidgetUiModel) {
        iuTwoHours?.setImageUrl(IMG_TWO_HOURS)
        iuStockAvailable?.setImageUrl(IMG_STOCK_AVAILABLE)
        iuGuaranteedQuality?.setImageUrl(IMG_GUARANTEED_QUALITY)
        setupLottie()
    }

    private fun setupLottie() {
        itemView.context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, LOTTIE)

            lottieCompositionLottieTask.addListener { result ->
                laChevron?.setComposition(result)
                laChevron?.playAnimation()
                laChevron?.repeatCount = LottieDrawable.INFINITE
            }

            laChevron?.setOnClickListener {
                laChevron?.repeatCount = 0
                val bottomSheet = TokoNowHomeEducationalInformationBottomSheet.newInstance()
                tokoNowListener?.getFragmentManagerPage()?.let { fragmentManager ->
                    bottomSheet.show(fragmentManager)
                }
            }
        }
    }
}
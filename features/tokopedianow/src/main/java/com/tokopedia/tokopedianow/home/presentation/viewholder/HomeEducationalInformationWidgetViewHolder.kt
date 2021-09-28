package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.bottomsheet.TokoNowHomeEducationalInformationBottomSheet
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeEducationalInformationWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify

class HomeEducationalInformationWidgetViewHolder(
    itemView: View,
    private val tokoNowView: TokoNowView? = null,
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

    private var iuTwoHours: ImageUnify? = null
    private var iuStockAvailable: ImageUnify? = null
    private var iuGuaranteedQuality: ImageUnify? = null
    private var laChevron: LottieAnimationView? = null
    private var cvChevron: CardView? = null
    private var cvEducationalInfo: CardView? = null
    private var ivChevronDown: ImageView? = null

    init {
        iuTwoHours = itemView.findViewById(R.id.iu_two_hours)
        iuStockAvailable = itemView.findViewById(R.id.iu_stock_available)
        iuGuaranteedQuality = itemView.findViewById(R.id.iu_guaranteed_quality)
        ivChevronDown = itemView.findViewById(R.id.iv_chevron_down)
        laChevron = itemView.findViewById(R.id.la_chevron)
        cvChevron = itemView.findViewById(R.id.cv_chevron)
        cvEducationalInfo = itemView.findViewById(R.id.cv_educational_info)
    }

    override fun bind(element: HomeEducationalInformationWidgetUiModel) {
        if (element.state == HomeLayoutItemState.LOADED) {
            setupUi()
            listener?.onEducationInformationWidgetImpressed()
        }
    }

    private fun setupUi() {
        cvEducationalInfo?.show()
        iuTwoHours?.setImageUrl(IMG_TWO_HOURS)
        iuStockAvailable?.setImageUrl(IMG_STOCK_AVAILABLE)
        iuGuaranteedQuality?.setImageUrl(IMG_GUARANTEED_QUALITY)

        if(listener?.isEducationInformationLottieStopped() == true) {
            setupBasicButton()
        } else {
            setupLottie()
        }
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
                showBottomSheet()
                listener?.onEducationInformationLottieClicked()
            }
        }
    }

    private fun setupBasicButton() {
        cvChevron?.show()
        laChevron?.gone()
        val unifyColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        ivChevronDown?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(unifyColor, BlendModeCompat.SRC_ATOP)
        cvChevron?.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        setupBasicButton()
        val bottomSheet = TokoNowHomeEducationalInformationBottomSheet.newInstance()
        tokoNowView?.getFragmentManagerPage()?.let { fragmentManager ->
            bottomSheet.show(fragmentManager)
        }
    }

    interface HomeEducationalInformationListener {
        fun isEducationInformationLottieStopped(): Boolean
        fun onEducationInformationLottieClicked()
        fun onEducationInformationWidgetImpressed()
    }
}
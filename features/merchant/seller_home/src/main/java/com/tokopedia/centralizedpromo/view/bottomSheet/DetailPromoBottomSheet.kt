package com.tokopedia.centralizedpromo.view.bottomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhome.databinding.BottomSheetDetailPromoBinding
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.BaseBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.CalendarWidgetDateFilterBottomSheet

class DetailPromoBottomSheet :
    BaseBottomSheet<BottomSheetDetailPromoBinding>() {

    private var onCheckBoxListener: ((Boolean) -> Unit?)? = null
    private var onCreateCampaignTracking: (() -> Unit?)? = null
    private var onClickPaywallTracking: (() -> Unit?)? = null
    private var onImpressionPaywallracking: (() -> Unit?)? = null

    companion object {
        private const val TAG = "DetailPromoBottomSheet"
        private const val KEY_PROMO = "promo"

        fun createInstance(promoCreationUiModel: PromoCreationUiModel): DetailPromoBottomSheet {
            return DetailPromoBottomSheet().apply {
                showKnob = false
                showCloseIcon = true
                arguments = Bundle().apply {
                    putParcelable(KEY_PROMO, promoCreationUiModel)
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDetailPromoBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {
        val promoCreationUiModel: PromoCreationUiModel? = arguments?.getParcelable(KEY_PROMO)
        promoCreationUiModel?.let {
            setTitle(it.title)
            if (it.infoText.isNotEmpty()) {
                tickerInfoEligble.show()
                tickerInfoEligble.setTextDescription(it.infoText)
                cbDontShowInfo.gone()
                onImpressionPaywallracking?.invoke()
            } else {
                tickerInfoEligble.gone()
                cbDontShowInfo.show()
                cbDontShowInfo.setOnCheckedChangeListener { _, _ ->
                    onCheckBoxListener?.invoke(binding?.cbDontShowInfo?.isChecked.orFalse())
                }
            }
            tvHeaderText.text = it.headerText
            ivBannerImage.loadImage(it.banner)
            tvBottomText.text = it.bottomText
            btnCtaPromo.text = it.ctaText
            btnCtaPromo.setOnClickListener { _ ->
                dismiss()
                RouteManager.route(context, it.ctaLink)
                if (it.infoText.isEmpty()) {
                    onCreateCampaignTracking?.invoke()
                } else {
                    onClickPaywallTracking?.invoke()
                }
            }
        }
    }

    fun show(fm: FragmentManager) {
        if (!fm.isStateSaved) {
            show(fm, TAG)
        }
    }

    fun onCheckBoxListener(onCheckBoxListener: (Boolean) -> Unit) {
        this.onCheckBoxListener = onCheckBoxListener
    }

    fun onCreateCampaignTracking(onCreateCampaignTracking: () -> Unit) {
        this.onCreateCampaignTracking = onCreateCampaignTracking
    }

    fun onClickPaywallTracking(onClickPaywallTracking: () -> Unit) {
        this.onClickPaywallTracking = onClickPaywallTracking
    }

    fun onImpressionPaywallTracking(onImpressionPaywallracking: () -> Unit) {
        this.onImpressionPaywallracking = onImpressionPaywallracking
    }
}
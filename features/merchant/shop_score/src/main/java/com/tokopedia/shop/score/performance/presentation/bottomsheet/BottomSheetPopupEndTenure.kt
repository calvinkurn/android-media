package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PMTier
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.getNumberFormat
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomSheetNewSellerShopScoreBinding
import com.tokopedia.shop.score.performance.presentation.model.PopupEndTenureUiModel

class BottomSheetPopupEndTenure : BaseBottomSheetShopScore<BottomSheetNewSellerShopScoreBinding>() {

    override fun bind(view: View) = BottomSheetNewSellerShopScoreBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottom_sheet_new_seller_shop_score

    override fun getTitleBottomSheet(): String = ""

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private fun setupData() = binding?.run {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        val popupEndTenureUiModel = cacheManager?.get<PopupEndTenureUiModel>(
            KEY_ITEM_END_TENURE_POP_UP, PopupEndTenureUiModel::class.java
        )
        ivNewSellerIllustration.loadImage(ShopScoreConstant.IL_NEW_SELLER_SHOP_SCORE_URL)
        popupEndTenureUiModel?.let {
            tvShopLevel.text =
                getString(R.string.title_level_pop_up_end_tenure, it.shopLevel)
            setShopScore(it.shopScore)
            setupLevelBarNewSeller(it.shopLevel)
            toggleTipsNewSeller(it)
        }
        setupBtnUnderstand()
    }

    private fun setupBtnUnderstand() {
        binding?.btnUnderstand?.setOnClickListener {
            dismiss()
        }
    }

    private fun toggleTipsNewSeller(popupEndTenureUiModel: PopupEndTenureUiModel) = binding?.run {
        when {
            isPowerMerchant(popupEndTenureUiModel).value ||
                    isPowerMerchantPro(popupEndTenureUiModel).value -> {
                if (popupEndTenureUiModel.shopScore
                        .getNumberFormat(ShopScoreConstant.NULL_NUMBER) <
                    ShopScoreConstant.SHOP_AGE_SIXTY
                ) {
                    tvTipsIncreasePerformance.hide()
                    setDescTicker(popupEndTenureUiModel)
                    tickerTipsIncreasePerformance.show()
                } else {
                    tvTipsIncreasePerformance.show()
                    tickerTipsIncreasePerformance.hide()
                }
            }
            else -> { }
        }
    }

    private fun isPowerMerchant(popupEndTenureUiModel: PopupEndTenureUiModel?): Lazy<Boolean> {
        return lazy {
            popupEndTenureUiModel?.powerMerchantData?.powerMerchant?.pmTier == PMTier.REGULAR &&
                    (popupEndTenureUiModel.powerMerchantData.powerMerchant.status == PMStatusConst.ACTIVE ||
                            popupEndTenureUiModel.powerMerchantData.powerMerchant.status
                            == PMStatusConst.IDLE)
        }
    }

    private fun isPowerMerchantPro(popupEndTenureUiModel: PopupEndTenureUiModel?): Lazy<Boolean> {
        return lazy {
            popupEndTenureUiModel?.powerMerchantData?.powerMerchant?.pmTier == PMTier.PRO
        }
    }

    private fun setDescTicker(popupEndTenureUiModel: PopupEndTenureUiModel?) = binding?.run {
        when {
            isPowerMerchant(popupEndTenureUiModel).value -> {
                tickerTipsIncreasePerformance.tickerTitle =
                    getString(R.string.title_warning_new_seller_shop_score_pm)
                tickerTipsIncreasePerformance.setTextDescription(
                    getString(R.string.desc_warning_new_seller_shop_score_pm)
                )
            }
            isPowerMerchantPro(popupEndTenureUiModel).value -> {
                tickerTipsIncreasePerformance.tickerTitle =
                    getString(R.string.title_warning_new_seller_shop_score_pm_pro)
                tickerTipsIncreasePerformance.setTextDescription(
                    getString(R.string.desc_warning_new_seller_shop_score_pm_pro)
                )
            }
            else -> { }
        }
    }

    private fun setShopScore(shopScore: String) = binding?.run {
        val shopScoreNumber = shopScore.getNumberFormat(ShopScoreConstant.NULL_NUMBER)
        tvShopScoreValue.run {
            text = shopScore
            if (shopScoreNumber < ShopScoreConstant.SHOP_AGE_SIXTY) {
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R600
                    )
                )
            } else {
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                    )
                )
            }
        }
    }

    private fun setupLevelBarNewSeller(shopLevel: String) = binding?.run {
        ivLevelBarNewSeller.run {
            when (shopLevel.getNumberFormat(ShopScoreConstant.NULL_NUMBER)) {
                ShopScoreConstant.SHOP_SCORE_LEVEL_ONE ->
                    setImageResource(R.drawable.ic_one_level_green)
                ShopScoreConstant.SHOP_SCORE_LEVEL_TWO ->
                    setImageResource(R.drawable.ic_two_level_green)
                ShopScoreConstant.SHOP_SCORE_LEVEL_THREE ->
                    setImageResource(R.drawable.ic_three_level_green)
                ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR ->
                    setImageResource(R.drawable.ic_four_level_green)
            }
        }
    }

    companion object {
        const val TAG = "BottomSheetShopEndTenure"

        const val KEY_ITEM_END_TENURE_POP_UP = "key_item_end_tenure_pop_up"
        const val KEY_CACHE_MANAGER_ID = "extra_cache_manager_id"

        fun newInstance(cacheManagerId: String): BottomSheetPopupEndTenure {
            return BottomSheetPopupEndTenure().apply {
                showKnob = true
                val bundle = Bundle()
                bundle.putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
                arguments = bundle
            }
        }
    }
}
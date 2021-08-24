package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.getNumberFormat
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.performance.presentation.model.PopupEndTenureUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopType
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class BottomSheetPopupEndTenure : BaseBottomSheetShopScore() {

    private var ivNewSellerIllustration: ImageUnify? = null
    private var tvShopLevel: Typography? = null
    private var ivLevelBarNewSeller: ImageUnify? = null
    private var tvShopScoreValue: Typography? = null
    private var tvTipsIncreasePerformance: Typography? = null
    private var tickerTipsIncreasePerformance: Ticker? = null

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
        view.setupView()
        setupData()
    }

    private fun View.setupView() = this.run {
        ivNewSellerIllustration = findViewById(R.id.ivNewSellerIllustration)
        tvShopLevel = findViewById(R.id.tvShopLevel)
        ivLevelBarNewSeller = findViewById(R.id.ivLevelBarNewSeller)
        tvShopScoreValue = findViewById(R.id.tvShopScoreValue)
        tvTipsIncreasePerformance = findViewById(R.id.tvTipsIncreasePerformance)
        tickerTipsIncreasePerformance = findViewById(R.id.tickerTipsIncreasePerformance)
    }

    private fun setupData() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID)
            )
        }
        val popupEndTenureUiModel = cacheManager?.get<PopupEndTenureUiModel>(
            KEY_ITEM_END_TENURE_POP_UP, PopupEndTenureUiModel::class.java
        )
        ivNewSellerIllustration?.loadImage(ShopScoreConstant.IL_NEW_SELLER_SHOP_SCORE_URL)
        tvShopLevel?.text = popupEndTenureUiModel?.shopLevel
        setShopScore(popupEndTenureUiModel?.shopScore.orEmpty())
        setupLevelBarNewSeller(popupEndTenureUiModel?.shopLevel.orEmpty())
        toggleTipsNewSeller(popupEndTenureUiModel)
    }

    private fun toggleTipsNewSeller(popupEndTenureUiModel: PopupEndTenureUiModel?) {
        when (popupEndTenureUiModel?.shopType) {
            ShopType.POWER_MERCHANT, ShopType.POWER_MERCHANT_PRO -> {
                if (popupEndTenureUiModel.shopScore.getNumberFormat(ShopScoreConstant.NULL_NUMBER) <
                    ShopScoreConstant.SHOP_AGE_SIXTY
                ) {
                    tvTipsIncreasePerformance?.hide()
                    tickerTipsIncreasePerformance?.show()
                } else {
                    tvTipsIncreasePerformance?.show()
                    tickerTipsIncreasePerformance?.hide()
                }
            }
            else -> {
            }
        }
    }

    private fun setShopScore(shopScore: String) {
        val shopScoreNumber = shopScore.getNumberFormat(ShopScoreConstant.NULL_NUMBER)
        tvShopScoreValue?.apply {
            text = shopScore
            if (shopScoreNumber < ShopScoreConstant.SHOP_AGE_SIXTY) {

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

    private fun setupLevelBarNewSeller(shopLevel: String) {
        ivLevelBarNewSeller?.apply {
            when (shopLevel.getNumberFormat(ShopScoreConstant.NULL_NUMBER)) {
                ShopScoreConstant.SHOP_SCORE_LEVEL_ONE -> loadImage(R.drawable.ic_one_level_green)
                ShopScoreConstant.SHOP_SCORE_LEVEL_TWO -> loadImage(R.drawable.ic_two_level_green)
                ShopScoreConstant.SHOP_SCORE_LEVEL_THREE -> loadImage(R.drawable.ic_three_level_green)
                ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR -> loadImage(R.drawable.ic_four_level_green)
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
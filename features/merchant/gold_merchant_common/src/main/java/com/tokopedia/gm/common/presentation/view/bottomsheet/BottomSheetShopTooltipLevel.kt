package com.tokopedia.gm.common.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.databinding.GmcBottomsheetTooltipInformationLevelBinding
import com.tokopedia.gm.common.presentation.common.TooltipLevelItemDecoration
import com.tokopedia.gm.common.presentation.model.CardTooltipLevelUiModel
import com.tokopedia.gm.common.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.gm.common.presentation.view.adapter.CardTooltipLevelAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

class BottomSheetShopTooltipLevel : BottomSheetUnify() {

    companion object {
        const val SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG = "ShopTooltipLevelBottomSheetTag"
        private const val SHOP_PERFORMANCE_LEVEL_KEY = "shop_performance_level_key"
        private const val SHOP_INCOME_KEY = "shop_income_key"
        private const val PRODUCT_SOLD_KEY = "product_sold_key"
        private const val PERIOD_KEY = "period_key"
        private const val NEXT_UPDATE_KEY = "next_update_key"
        private const val DASH = "-"
        private const val SPAN_COUNT = 2

        fun createInstance(
            shopLevel: Long,
            shopIncome: String,
            productSold: String,
            period: String,
            nextUpdate: String
        ): BottomSheetShopTooltipLevel {
            return BottomSheetShopTooltipLevel().apply {
                val bundle = Bundle()
                bundle.putLong(SHOP_PERFORMANCE_LEVEL_KEY, shopLevel)
                bundle.putString(SHOP_INCOME_KEY, shopIncome)
                bundle.putString(PRODUCT_SOLD_KEY, productSold)
                bundle.putString(PERIOD_KEY, period)
                bundle.putString(NEXT_UPDATE_KEY, nextUpdate)
                arguments = bundle
            }
        }
    }

    private var shopLevel = 0L
    private var shopIncome = ""
    private var productSold = ""
    private var nextUpdate = ""
    private var period = ""

    private var binding: GmcBottomsheetTooltipInformationLevelBinding? = null
    private val cardTooltipLevelAdapter by lazy { CardTooltipLevelAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setChildView(inflater, container)
        val title = getString(R.string.gmc_title_shop_information_level)
        setTitle(title)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            bottomSheetWrapper.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
        }
        isFullpage = true
        setRecyclerView()
        observeShopInfoLevel()
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG)
            }
        }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(getLayoutResId(), container, false)
        binding = GmcBottomsheetTooltipInformationLevelBinding.bind(view)
        setChild(view)
    }

    private fun getLayoutResId(): Int = R.layout.gmc_bottomsheet_tooltip_information_level

    private fun getDataFromArguments() {
        shopLevel = arguments?.getLong(SHOP_PERFORMANCE_LEVEL_KEY, Int.ZERO.toLong()).orZero()
        shopIncome = arguments?.getString(SHOP_INCOME_KEY).orEmpty()
        productSold = arguments?.getString(PRODUCT_SOLD_KEY).orEmpty()
        nextUpdate = arguments?.getString(NEXT_UPDATE_KEY).orEmpty()
        period = arguments?.getString(PERIOD_KEY).orEmpty()
    }

    private fun setRecyclerView() {
        binding?.rvLevelCard?.run {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(TooltipLevelItemDecoration())
            }
            context?.let {
                layoutManager = GridLayoutManager(it, SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            }
            adapter = cardTooltipLevelAdapter
        }
    }

    private fun observeShopInfoLevel() {
        binding?.loaderTooltipLevel?.hide()
        val shopInfoLevelUiModel = ShopInfoLevelUiModel(
            shopIncome = shopIncome,
            productSold = productSold,
            periodDate = period,
            nextUpdate = nextUpdate,
            cardTooltipLevelList = getCardTooltipLevelList()
        )
        setShopLevelData(shopInfoLevelUiModel)
    }

    private fun getCardTooltipLevelList(): List<CardTooltipLevelUiModel> {
        return listOf(
            CardTooltipLevelUiModel(
                R.string.gmc_title_level_1,
                R.string.gmc_desc_level_1,
                PMConstant.ShopLevel.ONE == shopLevel.toInt()
            ),
            CardTooltipLevelUiModel(
                R.string.gmc_title_level_2,
                R.string.gmc_desc_level_2,
                PMConstant.ShopLevel.TWO == shopLevel.toInt()
            ),
            CardTooltipLevelUiModel(
                R.string.gmc_title_level_3,
                R.string.gmc_desc_level_3,
                PMConstant.ShopLevel.THREE == shopLevel.toInt()
            ),
            CardTooltipLevelUiModel(
                R.string.gmc_title_level_4,
                R.string.gmc_desc_level_4,
                PMConstant.ShopLevel.FOUR == shopLevel.toInt()
            )
        )
    }

    private fun setShopLevelData(data: ShopInfoLevelUiModel) = binding?.run {
        tvPeriodInformationLevel.text = data.periodDate
        tvValueIncomeTooltip.text = if (data.shopIncome.toDoubleOrZero() < Int.ZERO) {
            DASH
        } else {
            StringBuilder("Rp${data.shopIncome.toDoubleOrZero().getNumberFormatted()}")
        }
        tvValueProductSoldTooltip.text = if (data.productSold.toDoubleOrZero() < Int.ZERO) {
            DASH
        } else {
            data.productSold
        }
        val nextUpdate = if (data.nextUpdate.isBlank()) {
            DASH
        } else {
            data.nextUpdate
        }
        tvValueNextUpdate.text = getString(R.string.gmc_title_update_date, nextUpdate)
        cardTooltipLevelAdapter.setCardToolTipLevelList(data.cardTooltipLevelList)
    }
}
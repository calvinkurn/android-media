package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.TooltipLevelItemDecoration
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetTooltipInformationLevelBinding
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.adapter.CardTooltipLevelAdapter
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import javax.inject.Inject

class BottomSheetShopTooltipLevel :
    BaseBottomSheetShopScore<BottomsheetTooltipInformationLevelBinding>() {

    @Inject
    lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    private var shopLevel = 0L
    private var shopIncome = ""
    private var productSold = ""
    private var nextUpdate = ""
    private var period = ""

    private val cardTooltipLevelAdapter by lazy { CardTooltipLevelAdapter() }

    override fun bind(view: View) = BottomsheetTooltipInformationLevelBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottomsheet_tooltip_information_level

    override fun getTitleBottomSheet(): String = getString(R.string.title_shop_information_level)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    private fun getDataFromArguments() {
        shopLevel = arguments?.getLong(SHOP_PERFORMANCE_LEVEL_KEY, 0).orZero()
        shopIncome = arguments?.getString(SHOP_INCOME_KEY).orEmpty()
        productSold = arguments?.getString(PRODUCT_SOLD_KEY).orEmpty()
        nextUpdate = arguments?.getString(NEXT_UPDATE_KEY).orEmpty()
        period = arguments?.getString(PERIOD_KEY).orEmpty()
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

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java)?.inject(this)
    }

    private fun setRecyclerView() {
        binding?.rvLevelCard?.run {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(TooltipLevelItemDecoration())
            }
            layoutManager =
                context?.let { GridLayoutManager(it, 2, GridLayoutManager.VERTICAL, false) }
            adapter = cardTooltipLevelAdapter
        }
    }

    private fun observeShopInfoLevel() {
        observe(shopPerformanceViewModel.shopInfoLevel) {
            binding?.loaderTooltipLevel?.hide()
            val shopInfoLevelUiModel = ShopInfoLevelUiModel(
                shopIncome = shopIncome,
                productSold = productSold,
                periodDate = period,
                nextUpdate = nextUpdate,
                cardTooltipLevelList = it.cardTooltipLevelList
            )
            setShopLevelData(shopInfoLevelUiModel)
        }
        shopPerformanceViewModel.getShopInfoLevel(shopLevel)
    }

    private fun setShopLevelData(data: ShopInfoLevelUiModel) = binding?.run {
        tvPeriodInformationLevel.text = data.periodDate
        tvValueIncomeTooltip.text =
            if (data.shopIncome.toDoubleOrZero() < 0.0) {
                "-"
            } else {
                StringBuilder("Rp${data.shopIncome.toDoubleOrZero().getNumberFormatted()}")
            }
        tvValueProductSoldTooltip.text =
            if (data.productSold.toDoubleOrZero() < 0.0) {
                "-"
            } else {
                data.productSold
            }
        tvValueNextUpdate.text = getString(R.string.title_update_date, data.nextUpdate)
        cardTooltipLevelAdapter.setCardToolTipLevelList(data.cardTooltipLevelList)
        context?.let {
            containerCardIncomeInformation.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
        }
    }


    companion object {
        const val SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG = "ShopTooltipLevelBottomSheetTag"
        private const val SHOP_PERFORMANCE_LEVEL_KEY = "shop_performance_level_key"
        private const val SHOP_INCOME_KEY = "shop_income_key"
        private const val PRODUCT_SOLD_KEY = "product_sold_key"
        private const val PERIOD_KEY = "period_key"
        private const val NEXT_UPDATE_KEY = "next_update_key"

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
}
package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.TooltipLevelItemDecoration
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.adapter.CardTooltipLevelAdapter
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_tooltip_information_level.*
import javax.inject.Inject

class BottomSheetShopTooltipLevel : BaseBottomSheetShopScore() {

    @Inject
    lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    private var shopLevel = 0
    private var shopIncome = ""
    private var productSold = ""
    private var nextUpdate = ""
    private var period = ""
    private var tvPeriodInformationLevel: Typography? = null
    private var tvValueIncomeTooltip: Typography? = null
    private var tvValueProductSoldTooltip: Typography? = null
    private var rvLevelCard: RecyclerView? = null
    private var tvValueNextUpdate: Typography? = null

    private val cardTooltipLevelAdapter by lazy { CardTooltipLevelAdapter() }

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
        shopLevel = arguments?.getInt(SHOP_PERFORMANCE_LEVEL_KEY, 0).orZero()
        shopIncome = arguments?.getString(SHOP_INCOME_KEY).orEmpty()
        productSold = arguments?.getString(PRODUCT_SOLD_KEY).orEmpty()
        nextUpdate = arguments?.getString(NEXT_UPDATE_KEY).orEmpty()
        period = arguments?.getString(PERIOD_KEY).orEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFullpage = true
        initView(view)
        setRecyclerView()
        observeShopInfoLevel()
    }

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java)?.inject(this)
    }

    private fun initView(view: View) {
        tvPeriodInformationLevel = view.findViewById(R.id.tv_period_information_level)
        tvValueIncomeTooltip = view.findViewById(R.id.tv_value_income_tooltip)
        tvValueProductSoldTooltip = view.findViewById(R.id.tv_value_product_sold_tooltip)
        rvLevelCard = view.findViewById(R.id.rv_level_card)
        tvValueNextUpdate = view.findViewById(R.id.tv_value_next_update)
    }

    private fun setRecyclerView() {
        rvLevelCard?.apply {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(TooltipLevelItemDecoration())
            }
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = cardTooltipLevelAdapter
        }
    }

    private fun observeShopInfoLevel() {
        observe(shopPerformanceViewModel.shopInfoLevel) {
            loaderTooltipLevel?.hide()
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

    private fun setShopLevelData(data: ShopInfoLevelUiModel) {
        tvPeriodInformationLevel?.text = data.periodDate
        tvValueIncomeTooltip?.text = StringBuilder("Rp${data.shopIncome.toIntOrZero().getNumberFormatted()}")
        tvValueProductSoldTooltip?.text = data.productSold
        tvValueNextUpdate?.text = getString(R.string.title_update_date, data.nextUpdate)
        cardTooltipLevelAdapter.setCardToolTipLevelList(data.cardTooltipLevelList)
    }


    companion object {
        const val SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG = "ShopTooltipLevelBottomSheetTag"
        private const val SHOP_PERFORMANCE_LEVEL_KEY = "shop_performance_level_key"
        private const val SHOP_INCOME_KEY = "shop_income_key"
        private const val PRODUCT_SOLD_KEY = "product_sold_key"
        private const val PERIOD_KEY = "period_key"
        private const val NEXT_UPDATE_KEY = "next_update_key"

        fun createInstance(shopLevel: Int, shopIncome: String, productSold: String, period: String, nextUpdate: String): BottomSheetShopTooltipLevel {
            return BottomSheetShopTooltipLevel().apply {
                val bundle = Bundle()
                bundle.putInt(SHOP_PERFORMANCE_LEVEL_KEY, shopLevel)
                bundle.putString(SHOP_INCOME_KEY, shopIncome)
                bundle.putString(PRODUCT_SOLD_KEY, productSold)
                bundle.putString(PERIOD_KEY, period)
                bundle.putString(NEXT_UPDATE_KEY, nextUpdate)
                arguments = bundle
            }
        }
    }
}
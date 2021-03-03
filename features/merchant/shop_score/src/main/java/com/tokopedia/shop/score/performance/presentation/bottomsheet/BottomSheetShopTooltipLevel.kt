package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.performance.presentation.adapter.CardTooltipLevelAdapter
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_tooltip_information_level.*
import javax.inject.Inject

class BottomSheetShopTooltipLevel: BaseBottomSheetShopScore() {

    @Inject
    lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    private var shopLevel = 0
    private var tvPeriodInformationLevel: Typography? = null
    private var tvValueIncomeTooltip: Typography? = null
    private var tvValueProductSoldTooltip: Typography? = null
    private var rvLevelCard: RecyclerView? = null
    private var tvValueNextUpdate: Typography? = null

    private val cardTooltipLevelAdapter by lazy { CardTooltipLevelAdapter() }

    override fun getLayoutResId(): Int = R.layout.bottomsheet_tooltip_information_level

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setRecyclerView()
        observeShopInfoLevel()
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
            layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false)
            adapter = cardTooltipLevelAdapter
        }
    }

    private fun observeShopInfoLevel() {
        observe(shopPerformanceViewModel.shopInfoLevel) {
            when (it) {
                is Success -> {
                    initData(it.data)
                    loaderTooltipLevel?.hide()
                }
                is Fail -> {
                    loaderTooltipLevel?.hide()
                }
            }
        }
        shopPerformanceViewModel.getShopInfoLevel(shopLevel)
    }

    private fun initData(data: ShopInfoLevelUiModel) {
        tvValueIncomeTooltip?.text = data.shopIncome
        tvValueProductSoldTooltip?.text = data.productSold
        cardTooltipLevelAdapter.setCardToolTipLevelList(data.cardTooltipLevelList)
    }

    //temporary data
    private fun initialDummy() {

    }

    companion object {
        const val SHOP_TOOLTIP_LEVEL_BOTTOM_SHEET_TAG = "SomFilterBottomSheetTag"
    }
}
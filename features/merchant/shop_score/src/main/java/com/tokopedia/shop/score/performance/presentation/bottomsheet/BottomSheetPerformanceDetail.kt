package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class BottomSheetPerformanceDetail: BaseBottomSheetShopScore() {

    @Inject lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    private var titlePerformanceDetail = ""
    private var tvDescCalculationDetail: Typography? = null
    private var tvDescTipsDetail: Typography? = null
    private var tvMoreInfoPerformanceDetail: Typography? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titlePerformanceDetail = arguments?.getString(TITLE_PERFORMANCE_DETAIL_KEY).orEmpty()
    }

    override fun getLayoutResId(): Int = R.layout.bottomsheet_shop_performance_detail

    override fun getTitleBottomSheet(): String = titlePerformanceDetail

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PERFORMANCE_DETAIL_BOTTOM_SHEET_TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setup()
        observeShopPerformanceDetail()
    }

    private fun View.setup() {
        tvDescCalculationDetail = findViewById(R.id.tvDescCalculationDetail)
        tvDescTipsDetail = findViewById(R.id.tvDescTipsDetail)
        tvMoreInfoPerformanceDetail = findViewById(R.id.tvMoreInfoPerformanceDetail)
    }

    private fun observeShopPerformanceDetail() {
        observe(shopPerformanceViewModel.shopPerformanceDetail) {
            setupData(it)
        }
        shopPerformanceViewModel.getShopPerformanceDetail(titlePerformanceDetail)
    }

    private fun setupData(data: ShopPerformanceDetailUiModel) {
        with(data) {
            tvDescCalculationDetail?.text = MethodChecker.fromHtml(getString(descCalculation))
            tvDescTipsDetail?.text = MethodChecker.fromHtml(getString(descTips))
            tvMoreInfoPerformanceDetail?.showWithCondition(getString(moreInformation).isNotBlank())
            tvMoreInfoPerformanceDetail?.setClickableUrlHtml(getString(moreInformation)) {
                RouteManager.route(requireContext(), ApplinkConstInternalGlobal.WEBVIEW, urlLink)
            }
        }
    }

    companion object {
        const val PERFORMANCE_DETAIL_BOTTOM_SHEET_TAG = "PerformanceDetailBottomSheetTag"
        private const val TITLE_PERFORMANCE_DETAIL_KEY = "title_performance_detail_key"

        fun createInstance(titlePerformanceDetail: String): BottomSheetPerformanceDetail {
            val bottomSheetPerformanceDetail = BottomSheetPerformanceDetail()
            val args = Bundle()
            args.putString(TITLE_PERFORMANCE_DETAIL_KEY, titlePerformanceDetail)
            bottomSheetPerformanceDetail.arguments = args
            return bottomSheetPerformanceDetail
        }
    }
}
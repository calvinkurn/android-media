package com.tokopedia.shop.score.performance.presentation.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.presentation.BaseBottomSheetShopScore
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class BottomSheetPerformanceDetail: BaseBottomSheetShopScore() {

    @Inject lateinit var shopPerformanceViewModel: ShopPerformanceViewModel

    private var titlePerformanceDetail = ""
    private var identifierPerformanceDetail = ""
    private var tvDescCalculationDetail: Typography? = null
    private var tvDescTipsDetail: Typography? = null
    private var tvMoreInfoPerformanceDetail: Typography? = null
    private var separatorTips: DividerUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
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

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setup()
        observeShopPerformanceDetail()
    }

    private fun getDataFromArguments() {
        titlePerformanceDetail = arguments?.getString(TITLE_PERFORMANCE_DETAIL_KEY).orEmpty()
        identifierPerformanceDetail = arguments?.getString(IDENTIFIER_PERFORMANCE_DETAIL_KEY).orEmpty()
    }

    private fun View.setup() {
        tvDescCalculationDetail = findViewById(R.id.tvDescCalculationDetail)
        tvDescTipsDetail = findViewById(R.id.tvDescTipsDetail)
        tvMoreInfoPerformanceDetail = findViewById(R.id.tvMoreInfoPerformanceDetail)
        separatorTips = findViewById(R.id.separatorTips)
    }

    private fun observeShopPerformanceDetail() {
        observe(shopPerformanceViewModel.shopPerformanceDetail) {
            setupData(it)
        }
        shopPerformanceViewModel.getShopPerformanceDetail(identifierPerformanceDetail)
    }

    private fun setupData(data: ShopPerformanceDetailUiModel) {
        with(data) {
            tvDescCalculationDetail?.text = MethodChecker.fromHtml(descCalculation?.let { getString(it) })
            tvDescTipsDetail?.text = MethodChecker.fromHtml(descTips?.let { getString(it) })
            tvMoreInfoPerformanceDetail?.showWithCondition(moreInformation != null)
            separatorTips?.showWithCondition(moreInformation != null)
            moreInformation?.let {
                tvMoreInfoPerformanceDetail?.setTextMakeHyperlink(getString(it, urlLink)) {
                    if (urlLink.isNotBlank()) {
                        context?.let { context ->
                            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, urlLink)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val PERFORMANCE_DETAIL_BOTTOM_SHEET_TAG = "PerformanceDetailBottomSheetTag"
        private const val TITLE_PERFORMANCE_DETAIL_KEY = "title_performance_detail_key"
        private const val IDENTIFIER_PERFORMANCE_DETAIL_KEY = "identifier_performance_detail_key"

        fun createInstance(titlePerformanceDetail: String, identifier: String): BottomSheetPerformanceDetail {
            val bottomSheetPerformanceDetail = BottomSheetPerformanceDetail()
            val args = Bundle()
            args.putString(TITLE_PERFORMANCE_DETAIL_KEY, titlePerformanceDetail)
            args.putString(IDENTIFIER_PERFORMANCE_DETAIL_KEY, identifier)
            bottomSheetPerformanceDetail.arguments = args
            return bottomSheetPerformanceDetail
        }
    }
}
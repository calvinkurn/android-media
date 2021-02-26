package com.tokopedia.review.feature.reputationhistory.view.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShopScoreReputationBottomSheet: BottomSheetUnify() {

    companion object {
        val IMAGE_URL_1 = ""
        val IMAGE_URL_2 = ""
    }

    private var tvDatePerformancePage: Typography? = null
    private var tvMoreInterestBuyer: Typography? = null
    private var tvDescGetBenefitPerformance: Typography? = null
    private var btnShopPerformance: UnifyButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottomsheet_reputation_join_shop_score, container, false)
        tvDatePerformancePage = view.findViewById(R.id.tv_date_performance_page)
        tvMoreInterestBuyer = view.findViewById(R.id.tv_more_interest_buyer)
        tvDescGetBenefitPerformance = view.findViewById(R.id.tv_desc_get_benefit_performance)
        setChild(view)
        isFullpage = true
    }

    private fun setupView() {
        tvDatePerformancePage?.text = getString(R.string.desc_info_reputation_migrate_shop_score, "1 Juni 2021")
        tvMoreInterestBuyer?.text = MethodChecker.fromHtml(getString(R.string.more_interest_candidate_buyer))
        tvDescGetBenefitPerformance?.setClickableUrlHtml(getString(R.string.desc_get_benefit_performance)) { url ->
            //
        }

        btnShopPerformance?.setOnClickListener {
            //TODO
        }
    }
}
package com.tokopedia.review.feature.reputationhistory.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.PeriodType.COMMUNICATION_PERIOD
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.setTextMakeHyperlink
import com.tokopedia.review.feature.reputationhistory.di.SellerReputationComponent
import com.tokopedia.review.feature.reputationhistory.view.viewmodel.ShopScoreReputationViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_reputation_join_shop_score.*
import kotlinx.android.synthetic.main.fragment_seller_review_reply.*
import javax.inject.Inject

class ShopScoreReputationBottomSheet : BaseBottomSheetShopScoreReputation() {

    private var tvDatePerformancePage: Typography? = null
    private var tvMoreInterestBuyer: Typography? = null
    private var tvDescGetBenefitPerformance: Typography? = null
    private var btnShopPerformance: UnifyButton? = null
    private var ivMoreInterestBuyer: AppCompatImageView? = null
    private var ivMoreFocusService: AppCompatImageView? = null

    private var userSession: UserSessionInterface? = null

    @Inject lateinit var viewModel: ShopScoreReputationViewModel

    override fun getLayoutResId(): Int = R.layout.bottomsheet_reputation_join_shop_score

    override fun getTitleBottomSheet(): String = ""

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG_BOTTOM_SHEET_REPUTATION_HISTORY)
            }
        }
    }

    override fun initInjector() {
        getComponent(SellerReputationComponent::class.java)?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        isFullpage = true
        observePeriodType()
    }

    private fun initView(view: View?) = view?.run {
        tvDatePerformancePage = findViewById(R.id.tv_date_performance_page)
        tvMoreInterestBuyer = findViewById(R.id.tv_more_interest_buyer)
        tvDescGetBenefitPerformance = findViewById(R.id.tv_desc_get_benefit_performance)
        ivMoreFocusService = findViewById(R.id.iv_more_focus_shop_service)
        ivMoreInterestBuyer = findViewById(R.id.iv_more_interest_buyer)
    }

    private fun observePeriodType() {
        observe(viewModel.shopPeriod) {
            loaderReviewReply.hide()
            when (it) {
                is Success -> {
                    setupView(it.data)
                }
                is Fail -> {
                    setupView("")
                }
            }
        }
    }

    private fun setupView(periodType: String) {
        tvDatePerformancePage?.text = MethodChecker.fromHtml(getString(R.string.desc_info_reputation_migrate_shop_score, "1 Juni 2021"))
        tvMoreInterestBuyer?.text = MethodChecker.fromHtml(getString(R.string.more_interest_candidate_buyer))

        tvDescGetBenefitPerformance?.setTextMakeHyperlink(getString(R.string.desc_get_benefit_performance)) {
            val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
            RouteManager.route(requireContext(), appLink)
        }

        ivMoreInterestBuyer?.loadImage(ReviewConstants.IV_MORE_INTEREST_BUYER)

        if (userSession?.isGoldMerchant == true) {
            iv_more_focus_shop_service.loadImage(ReviewConstants.IV_MORE_FOCUS_SERVICE_PM)
        } else {
            iv_more_focus_shop_service.loadImage(ReviewConstants.IV_MORE_FOCUS_SERVICE_RM)
        }

        btnShopPerformance?.setOnClickListener {
            when (periodType) {
                COMMUNICATION_PERIOD -> {
                    //go to info page
                }
                else -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PERFORMANCE)
                }
            }
        }
    }

    companion object {
        const val TAG_BOTTOM_SHEET_REPUTATION_HISTORY = "TAG_BOTTOM_SHEET_REPUTATION_HISTORY"
    }
}
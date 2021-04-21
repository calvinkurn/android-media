package com.tokopedia.review.feature.reputationhistory.view.bottomsheet

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.constant.PeriodType.COMMUNICATION_PERIOD
import com.tokopedia.gm.common.constant.TRANSITION_PERIOD
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.setTextMakeHyperlink
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_reputation_join_shop_score.*

class ShopScoreReputationBottomSheet : BaseBottomSheetShopScoreReputation() {

    private var tvDatePerformancePage: Typography? = null
    private var tvMoreInterestBuyer: Typography? = null
    private var tvDescGetBenefitPerformance: Typography? = null
    private var btnShopPerformance: UnifyButton? = null
    private var ivMoreInterestBuyer: AppCompatImageView? = null
    private var ivMoreFocusService: AppCompatImageView? = null
    private var cardDateMoveReputation: FrameLayout? = null
    private var iconSpeakerReputation: IconUnify? = null

    private var userSession: UserSessionInterface? = null

    private var periodType = ""

    override fun getLayoutResId(): Int = R.layout.bottomsheet_reputation_join_shop_score

    override fun getTitleBottomSheet(): String = ""

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG_BOTTOM_SHEET_REPUTATION_HISTORY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(requireContext())
        periodType = arguments?.getString(PERIOD_TYPE_SHOP_SCORE) ?: ""
        clearContentPadding = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(periodType)
        isFullpage = true
    }

    private fun initView(view: View?) = view?.run {
        iconSpeakerReputation = findViewById(R.id.icon_speaker_reputation)
        cardDateMoveReputation = findViewById(R.id.card_date_move_reputation)
        tvDatePerformancePage = findViewById(R.id.tv_date_performance_page)
        tvMoreInterestBuyer = findViewById(R.id.tv_more_interest_buyer)
        tvDescGetBenefitPerformance = findViewById(R.id.tv_desc_get_benefit_performance)
        ivMoreFocusService = findViewById(R.id.iv_more_focus_shop_service)
        ivMoreInterestBuyer = findViewById(R.id.iv_more_interest_buyer)
        btnShopPerformance = findViewById(R.id.btnShopPerformance)
    }

    private fun setupView(periodType: String) {
        cardDateMoveReputation?.background = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_content_info_penalty) }
        tvDatePerformancePage?.text = MethodChecker.fromHtml(getString(R.string.desc_info_reputation_migrate_shop_score, getShopScoreDate(context)))
        tvMoreInterestBuyer?.text = MethodChecker.fromHtml(getString(R.string.more_interest_candidate_buyer))

        tvDescGetBenefitPerformance?.setTextMakeHyperlink(
                if (userSession?.isGoldMerchant == true) getString(R.string.desc_get_benefit_performance_pm)
                else getString(R.string.desc_get_benefit_performance_rm)) {
            val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
            RouteManager.route(context, appLink)
        }

        if (periodType == COMMUNICATION_PERIOD) {
            context?.let {
                iconSpeakerReputation?.setImageDrawable(getIconUnifyDrawable(
                        it,
                        IconUnify.SPEAKER,
                        ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ))
            }
        } else if (periodType == TRANSITION_PERIOD) {
            context?.let {
                iconSpeakerReputation?.setImageDrawable(getIconUnifyDrawable(
                        it,
                        IconUnify.ERROR,
                        ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Y300)
                ))
            }
        }

        ivMoreInterestBuyer?.loadImage(ReviewConstants.IV_MORE_INTEREST_BUYER)

        if (userSession?.isGoldMerchant == true) {
            iv_more_focus_shop_service.loadImage(ReviewConstants.IV_MORE_FOCUS_SERVICE_PM)
        } else {
            iv_more_focus_shop_service.loadImage(ReviewConstants.IV_MORE_FOCUS_SERVICE_RM)
        }

        btnShopPerformance?.setOnClickListener {
            if (periodType.isBlank()) return@setOnClickListener
            when (periodType) {
                COMMUNICATION_PERIOD -> {
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, GMCommonUrl.SHOP_INTERRUPT_PAGE)
                }
                else -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PERFORMANCE)
                }
            }
        }
    }

    companion object {
        const val TAG_BOTTOM_SHEET_REPUTATION_HISTORY = "TAG_BOTTOM_SHEET_REPUTATION_HISTORY"
        private const val PERIOD_TYPE_SHOP_SCORE = "PERIOD_TYPE_SHOP_SCORE"

        @JvmStatic
        fun newInstance(periodType: String): ShopScoreReputationBottomSheet {
            return ShopScoreReputationBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(PERIOD_TYPE_SHOP_SCORE, periodType)
                arguments = bundle
            }
        }
    }
}
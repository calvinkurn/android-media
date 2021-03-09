package com.tokopedia.review.feature.reputationhistory.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_reputation_join_shop_score.*

class ShopScoreReputationBottomSheet: BottomSheetUnify() {

    private var tvDatePerformancePage: Typography? = null
    private var tvMoreInterestBuyer: Typography? = null
    private var tvDescGetBenefitPerformance: Typography? = null
    private var btnShopPerformance: UnifyButton? = null
    private var ivMoreInterestBuyer: AppCompatImageView? = null
    private var ivMoreFocusService: AppCompatImageView? = null

    private var userSession: UserSessionInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(requireContext())
    }

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
        ivMoreFocusService = view.findViewById(R.id.iv_more_focus_shop_service)
        ivMoreInterestBuyer = view.findViewById(R.id.iv_more_interest_buyer)
        setChild(view)
        isFullpage = true
    }

    private fun setupView() {
        tvDatePerformancePage?.text = getString(R.string.desc_info_reputation_migrate_shop_score, "1 Juni 2021")
        tvMoreInterestBuyer?.text = MethodChecker.fromHtml(getString(R.string.more_interest_candidate_buyer))
        tvDescGetBenefitPerformance?.setClickableUrlHtml(getString(R.string.desc_get_benefit_performance)) { url ->
            if(url.isNotBlank()) {
                //TODO
            }
        }

        ivMoreInterestBuyer?.loadImage(ReviewConstants.IV_MORE_INTEREST_BUYER)

        if (userSession?.isGoldMerchant == true) {
            iv_more_focus_shop_service.loadImage(ReviewConstants.IV_MORE_FOCUS_SERVICE_PM)
        } else {
            iv_more_focus_shop_service.loadImage(ReviewConstants.IV_MORE_FOCUS_SERVICE_RM)
        }

        btnShopPerformance?.setOnClickListener {
            //TODO
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if(!isVisible) {
                show(it, TAG_BOTTOM_SHEET_REPUTATION_HISTORY)
            }
        }
    }

    companion object {
        const val TAG_BOTTOM_SHEET_REPUTATION_HISTORY = "TAG_BOTTOM_SHEET_REPUTATION_HISTORY"
    }
}
package com.tokopedia.shop.score.detail_old.view.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.gm.common.constant.COMMUNICATION_PERIOD
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.constant.GM_BADGE_TITLE
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.gm.common.utils.ShopScoreReputationErrorLogger
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_TYPE_OS
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_TYPE_PM
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_TYPE_RM
import com.tokopedia.shop.score.detail_old.analytics.ShopScoreDetailTracking
import com.tokopedia.shop.score.detail_old.di.component.DaggerShopScoreComponent
import com.tokopedia.shop.score.detail_old.view.model.ShopScoreDetailItem
import com.tokopedia.shop.score.detail_old.view.model.ShopScoreDetailSummary
import com.tokopedia.shop.score.detail_old.view.model.ShopType
import com.tokopedia.shop.score.detail_old.view.recyclerview.ShopScoreDetailAdapter
import com.tokopedia.shop.score.detail_old.view.util.formatShopScore
import com.tokopedia.shop.score.detail_old.view.viewmodel.ShopScoreDetailViewModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_score_detail.*
import javax.inject.Inject

class ShopScoreDetailFragment : Fragment() {

    @Inject
    lateinit var viewModel: ShopScoreDetailViewModel

    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    private var adapter: ShopScoreDetailAdapter? = null

    override fun onAttach(context: Context) {
        SplitCompat.install(context)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_score_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()

        setupRecyclerView()
        setDescriptionText()
        setupClickListeners()

        observeShopScoreDetail()
    }

    private fun initInjector() {
        DaggerShopScoreComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupTickerShopScore(shopInfoPeriodUiModel: ShopInfoPeriodUiModel) {
        ticker_info_shop_score?.apply {
            showWithCondition(shopInfoPeriodUiModel.periodType == COMMUNICATION_PERIOD)
            if (isVisible) {
                ShopScoreDetailTracking.impressHereTickerOldShopScoreDetail(viewModel.userSession.userId, getTypeShop())
            }

            setHtmlDescription(getString(R.string.ticker_info_shop_score, getShopScoreDate(context)))
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    when (shopInfoPeriodUiModel.periodType) {
                        COMMUNICATION_PERIOD -> {
                            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, GMCommonUrl.SHOP_INTERRUPT_PAGE)
                            shopScorePenaltyTracking.clickMoreInfoTickerOldShopScore()
                        }
                        else -> {
                        }
                    }
                    ShopScoreDetailTracking.clickHereTickerOldShopScoreDetail(viewModel.userSession.userId, getTypeShop())
                }

                override fun onDismiss() {}
            })
        }
    }

    private fun getTypeShop() = when {
        viewModel.userSession.isShopOfficialStore -> {
            SHOP_TYPE_OS
        }
        viewModel.userSession.isGoldMerchant -> {
            SHOP_TYPE_PM
        }
        else -> {
            SHOP_TYPE_RM
        }
    }

    private fun setupRecyclerView() {
        adapter = ShopScoreDetailAdapter()

        with(recycler_view_shop_score_detail) {
            adapter = this@ShopScoreDetailFragment.adapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    private fun setDescriptionText() {
        val description = getString(R.string.description_shop_score_gold_badge_state, GM_BADGE_TITLE)
        description_shop_score_detail_gold_badge_info.text = description
    }

    private fun setupClickListeners() {
        button_go_to_seller_center.setOnClickListener {
            goToSellerCenter()
        }
        button_go_to_complete_information.setOnClickListener {
            goToCompleteInformation()
        }
    }

    private fun renderShopScoreDetail(items: List<ShopScoreDetailItem>) {
        adapter?.updateData(items)
    }

    private fun renderShopScoreSummary(viewModel: ShopScoreDetailSummary?) {
        setNoGravity()
        val stringConcat = buildStringSummary(viewModel)
        text_view_shop_score_summary_detail_tittle?.text = MethodChecker.fromHtml(stringConcat)
    }

    private fun renderShopScoreState(type: ShopType) {
        val icon = when (type) {
            ShopType.POWER_MERCHANT -> com.tokopedia.gm.common.R.drawable.ic_power_merchant
            ShopType.REGULAR_MERCHANT, ShopType.OFFICIAL_STORE -> {
                com.tokopedia.gm.common.R.drawable.ic_pm_badge_shop_regular
            }
            else -> com.tokopedia.gm.common.R.drawable.ic_pm_badge_shop_regular
        }
        setShopScoreGoldBadgeState(MethodChecker.getDrawable(context, icon))
    }

    private fun emptyState() {
        context?.let {
            val color = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            scrollview?.setBackgroundColor(color)
        }

        container_view?.visibility = View.GONE
        setGravityCenter()

        NetworkErrorHelper
                .showEmptyState(
                        activity,
                        main_frame,
                        getString(R.string.error_title_shop_score_failed),
                        getString(R.string.error_subtitle_shop_score_failed),
                        getString(R.string.label_try_again),
                        com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
                ) { viewModel.getShopScoreDetail() }
    }

    private fun showLoading() {
        setGravityCenter()
        loading?.visibility = View.VISIBLE
        container_view?.visibility = View.GONE
    }

    private fun dismissLoading() {
        loading?.visibility = View.GONE
        container_view?.visibility = View.VISIBLE
    }

    private fun setGravityCenter() {
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        main_frame?.layoutParams = params
    }

    private fun setNoGravity() {
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        main_frame?.layoutParams = params
    }

    private fun setShopScoreGoldBadgeState(icon: Drawable) {
        image_view_gold_badge?.setImageDrawable(icon)
    }

    private fun buildStringSummary(summary: ShopScoreDetailSummary?): String {
        return (getString(R.string.subtitle_first_shop_score_detail_summary)
                + " "
                + "<font color=#"
                + summary?.color
                + "><strong>"
                + summary?.text
                + "</strong></font>"
                + " "
                + getString(R.string.subtitle_second_shop_score_detail_summary)
                + " "
                + "<strong>"
                + summary?.value?.formatShopScore()
                + "</strong>"
                + ".")
    }

    private fun observeShopScoreDetail() {
        observe(viewModel.shopScoreData) { result ->
            dismissLoading()

            when (result) {
                is Success -> {
                    setupTickerShopScore(result.data.second)
                    result.data.first.run {
                        renderShopScoreDetail(items)
                        renderShopScoreSummary(summary)
                        renderShopScoreState(shopType)
                    }
                }
                is Fail -> {
                    emptyState()
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                            ShopScoreReputationErrorLogger.OLD_SHOP_SCORE_ERROR, result.throwable)
                }
            }
        }
        viewModel.getShopScoreDetail()
    }

    private fun goToSellerCenter() {
        openUrlWebView(SELLER_CENTER_LINK)
    }

    private fun goToCompleteInformation() {
        openUrl(Uri.parse(SHOP_SCORE_INFORMATION))
    }

    private fun openUrl(parse: Uri) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, parse)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            NetworkErrorHelper.showSnackbar(activity, getString(R.string.error_no_browser))
        }
    }

    private fun openUrlWebView(urlString: String) {
        val webViewApplink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewApplink)
    }

    companion object {
        private const val SELLER_CENTER_LINK = "https://seller.tokopedia.com/edu/skor-toko"
        private const val SHOP_SCORE_INFORMATION = "https://help.tokopedia.com/hc/en-us/articles/115000854466-Performa-Toko"

        @JvmStatic
        fun createFragment(): Fragment {
            return ShopScoreDetailFragment()
        }
    }
}
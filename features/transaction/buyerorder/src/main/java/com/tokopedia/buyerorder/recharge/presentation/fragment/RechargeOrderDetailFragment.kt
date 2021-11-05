package com.tokopedia.buyerorder.recharge.presentation.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerUtils
import com.tokopedia.buyerorder.databinding.FragmentRechargeOrderDetailBinding
import com.tokopedia.buyerorder.detail.analytics.OrderListAnalyticsUtils
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.di.RechargeOrderDetailComponent
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailAdapter
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailTypeFactory
import com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder.*
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailActionButtonModel
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailStaticButtonModel
import com.tokopedia.buyerorder.recharge.presentation.utils.RechargeOrderDetailAnalytics
import com.tokopedia.buyerorder.recharge.presentation.viewmodel.RechargeOrderDetailViewModel
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailFragment : BaseDaggerFragment(),
        RechargeOrderDetailTopSectionViewHolder.ActionListener,
        RechargeOrderDetailProductViewHolder.ActionListener,
        RechargeOrderDetailDigitalRecommendationViewHolder.ActionListener,
        RechargeOrderDetailStaticButtonViewHolder.ActionListener,
        RechargeOrderDetailAboutOrderViewHolder.ActionListener,
        RechargeOrderDetailActionButtonSectionViewHolder.ActionListener,
        RecommendationWidgetListener {

    private lateinit var binding: FragmentRechargeOrderDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val rechargeViewModel: RechargeOrderDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(RechargeOrderDetailViewModel::class.java)
    }

    @Inject
    lateinit var rechargeOrderDetailAnalytics: RechargeOrderDetailAnalytics

    private val digitalRecommendationData: DigitalRecommendationData
        get() = DigitalRecommendationData(
                viewModelFactory,
                viewLifecycleOwner,
                DigitalRecommendationAdditionalTrackingData(
                        userType = "",
                        widgetPosition = rechargeViewModel.getDigitalRecommendationPosition().toString()
                ),
                DigitalRecommendationPage.DIGITAL_GOODS
        )
    private val typeFactory: RechargeOrderDetailTypeFactory by lazy {
        RechargeOrderDetailTypeFactory(digitalRecommendationData,
                this,
                this,
                this,
                this,
                this,
                this,
                this)
    }
    private val adapter: RechargeOrderDetailAdapter by lazy {
        RechargeOrderDetailAdapter(typeFactory)
    }

    private var orderId: String = ""
    private var orderCategory: String = ""
    private var isPrimaryButtonExists: Boolean = false

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(RechargeOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRechargeOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            orderId = it.getString(EXTRA_ORDER_ID) ?: ""
            orderCategory = it.getString(EXTRA_ORDER_CATEGORY) ?: ""
            isPrimaryButtonExists = it.getBoolean(EXTRA_IS_PRIMARY_BUTTON_EXISTS)
        }

        arguments?.let {
            if (it.containsKey(EXTRA_ORDER_ID))
                orderId = it.getString(EXTRA_ORDER_ID) ?: ""
            if (it.containsKey(EXTRA_ORDER_CATEGORY))
                orderCategory = it.getString(EXTRA_ORDER_CATEGORY) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        showLoading()
        observeData()

        rechargeViewModel.fetchData(
                RechargeOrderDetailRequest(
                        orderCategory = orderCategory,
                        orderId = orderId
                )
        )

        rechargeOrderDetailAnalytics.eventOpenScreen(RechargeOrderDetailAnalytics.DefaultValue.SCREEN_NAME_ORDER_DETAIL)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EXTRA_ORDER_ID, orderId)
        outState.putString(EXTRA_ORDER_CATEGORY, orderCategory)
        outState.putBoolean(EXTRA_IS_PRIMARY_BUTTON_EXISTS, isPrimaryButtonExists)
    }

    override fun onCopyInvoiceNumberClicked(invoiceRefNum: String) {
        copyToClipboard(INVOICE_NUMBER_LABEL, invoiceRefNum)
    }

    override fun onSeeInvoiceClicked(invoiceRefNum: String, invoiceUrl: String) {
        rechargeOrderDetailAnalytics.eventClickSeeInvoice(
                OrderListAnalyticsUtils.getCategoryName(rechargeViewModel.getOrderDetailResultData()),
                OrderListAnalyticsUtils.getProductName(rechargeViewModel.getOrderDetailResultData())
        )
        rechargeOrderDetailAnalytics.eventOpenScreen(RechargeOrderDetailAnalytics.DefaultValue.SCREEN_NAME_INVOICE)
    }

    override fun onCopyCodeClicked(label: String, value: String) {
        copyToClipboard(label, value)
    }

    override fun hideDigitalRecommendation() {
        adapter.removeDigitalRecommendation()
    }

    override fun onClickStaticButton(staticButtonModel: RechargeOrderDetailStaticButtonModel) {
        sendActionButtonClickedEvent(
                staticButtonModel.title,
                FEATURE_ACTION_BUTTON_TYPE
        )
    }

    override fun onClickHelp(helpUrl: String) {
        // no op for now
    }

    override fun onActionButtonClicked(actionButton: RechargeOrderDetailActionButtonModel) {
        sendActionButtonClickedEvent(
                actionButton.name,
                actionButton.buttonType
        )
    }

    override fun onBestSellerClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        rechargeOrderDetailAnalytics.eventTopAdsClick(recommendationItem)
        RouteManager.route(context, recommendationItem.appUrl)
    }

    override fun onBestSellerImpress(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        rechargeOrderDetailAnalytics.eventTopAdsImpression(recommendationItem)
    }

    override fun onBestSellerThreeDotsClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        // no op
    }

    override fun onBestSellerFilterClick(filter: RecommendationFilterChipsEntity.RecommendationFilterChip, bestSellerDataModel: BestSellerDataModel, widgetPosition: Int, chipsPosition: Int) {
        // no op
    }

    override fun onBestSellerSeeMoreTextClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int) {
        RouteManager.route(context, appLink)
    }

    override fun onBestSellerSeeAllCardClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int) {
        RouteManager.route(context, appLink)
    }

    private fun setupViews() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvRechargeOrderDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rvRechargeOrderDetail.adapter = adapter

            rvRechargeOrderDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = rvRechargeOrderDetail.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!adapter.lastVisibleIsActionButton(lastVisibleItemPosition) && isPrimaryButtonExists) {
                        binding.containerRechargeOrderDetailStickyButton.show()
                    } else {
                        binding.containerRechargeOrderDetailStickyButton.hide()
                    }
                }
            })
        }
    }

    private fun setupStickyButton(primaryActionButton: RechargeOrderDetailActionButtonModel?) {
        primaryActionButton?.let {
            with(binding) {
                btnRechargeOrderDetailSticky.text = primaryActionButton.label
                btnRechargeOrderDetailSticky.setOnClickListener {
                    context?.let { ctx ->
                        sendActionButtonClickedEvent(
                                primaryActionButton.name,
                                primaryActionButton.buttonType
                        )
                        onStickyActionButtonClicked(ctx, primaryActionButton.uri)
                    }
                }
            }
            isPrimaryButtonExists = true
        }
    }

    private fun observeData() {
        rechargeViewModel.orderDetailData.observe(viewLifecycleOwner, { result ->
            hideLoading()
            when (result) {
                is Success -> {
                    adapter.updateItems(result.data,
                            rechargeViewModel.getTopAdsData(),
                            rechargeViewModel.getRecommendationWidgetPositionData())
                    setupStickyButton(result.data.actionButtonList.actionButtons.firstOrNull {
                        it.buttonType.equals(PRIMARY_ACTION_BUTTON_TYPE, true)
                    })
                    showRecyclerView()
                    rechargeViewModel.fetchTopAdsData()
                }
                is Fail -> {

                }
            }
        })

        rechargeViewModel.topadsData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    adapter.updateItems(rechargeViewModel.getOrderDetailResultData(),
                            result.data,
                            rechargeViewModel.getRecommendationWidgetPositionData())
                }
                is Fail -> {
                    adapter.removeTopAds()
                }
            }
        })

        rechargeViewModel.recommendationPosition.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    adapter.updateItems(
                            rechargeViewModel.getOrderDetailResultData(),
                            rechargeViewModel.getTopAdsData(),
                            result.data
                    )
                }
                is Fail -> {

                }
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvRechargeOrderDetail.show()
    }

    private fun hideRecyclerView() {
        binding.rvRechargeOrderDetail.hide()
    }

    private fun showLoading() {
        binding.rechargeOrderDetailLoader.show()
    }

    private fun hideLoading() {
        binding.rechargeOrderDetailLoader.hide()
    }

    private fun onStickyActionButtonClicked(context: Context, uri: String) {
        if (uri.isNotEmpty() && uri.startsWith(TOKOPEDIA_PREFIX)) {
            var mUri = uri
            val url = Uri.parse(mUri)

            if (mUri.contains(IDEM_POTENCY_KEY)) {
                mUri = mUri.replace(url.getQueryParameter(IDEM_POTENCY_KEY)
                        ?: "", "")
                mUri = mUri.replace("${IDEM_POTENCY_KEY}=", "")
            }
            RouteManager.route(context, mUri)
        } else if (uri.isNotEmpty()) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, uri)
        }
    }

    private fun copyToClipboard(label: String, value: String) {
        rechargeOrderDetailAnalytics.eventClickCopyButton(
                OrderListAnalyticsUtils.getCategoryName(rechargeViewModel.getOrderDetailResultData()),
                OrderListAnalyticsUtils.getProductName(rechargeViewModel.getOrderDetailResultData())
        )
        context?.let {
            BuyerUtils.copyTextToClipBoard(label, value, it)
            BuyerUtils.vibrate(it)
            Toaster.build(binding.root,
                    String.format(getString(R.string.recharge_order_detail_copied_message), label),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL
            ).show()
        }
    }

    private fun sendActionButtonClickedEvent(buttonName: String, buttonType: String) {
        rechargeOrderDetailAnalytics.eventClickActionButton(
                OrderListAnalyticsUtils.getCategoryName(rechargeViewModel.getOrderDetailResultData()),
                OrderListAnalyticsUtils.getProductName(rechargeViewModel.getOrderDetailResultData()),
                buttonName,
                when (buttonType) {
                    PRIMARY_ACTION_BUTTON_TYPE -> RechargeOrderDetailAnalytics.EventAction.CLICK_PRIMARY_BUTTON
                    SECONDARY_ACTION_BUTTON_TYPE -> RechargeOrderDetailAnalytics.EventAction.CLICK_SECONDARY_BUTTON
                    else -> RechargeOrderDetailAnalytics.EventAction.CLICK_FEATURE_BUTTON
                }
        )
    }

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"
        private const val EXTRA_ORDER_CATEGORY = "EXTRA_ORDER_CATEGORY"
        private const val EXTRA_IS_PRIMARY_BUTTON_EXISTS = "EXTRA_IS_PRIMARY_BUTTON_EXISTS"

        private const val PRIMARY_ACTION_BUTTON_TYPE = "primary"
        private const val SECONDARY_ACTION_BUTTON_TYPE = "secondary"
        private const val FEATURE_ACTION_BUTTON_TYPE = "feature"

        private const val TOKOPEDIA_PREFIX = "tokopedia"
        private const val IDEM_POTENCY_KEY = "idem_potency_key"

        private const val INVOICE_NUMBER_LABEL = "Nomor Invoice"

        fun getInstance(orderId: String,
                        orderCategory: String): RechargeOrderDetailFragment =
                RechargeOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_ORDER_CATEGORY, orderCategory)
                        putString(EXTRA_ORDER_ID, orderId)
                    }
                }
    }

}
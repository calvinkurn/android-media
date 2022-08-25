package com.tokopedia.buyerorder.detail.revamp.fragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.common.util.BuyerUtils
import com.tokopedia.buyerorder.databinding.FragmentOmsListDetailBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.AdditionalInfo
import com.tokopedia.buyerorder.detail.data.ConditionalInfo
import com.tokopedia.buyerorder.detail.data.Detail
import com.tokopedia.buyerorder.detail.data.Invoice
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.PayMethod
import com.tokopedia.buyerorder.detail.data.PaymentData
import com.tokopedia.buyerorder.detail.data.Pricing
import com.tokopedia.buyerorder.detail.data.Status
import com.tokopedia.buyerorder.detail.data.Title
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.revamp.adapter.OrderDetailTypeFactoryImpl
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.EventsViewHolder
import com.tokopedia.buyerorder.detail.revamp.util.OrderCategory
import com.tokopedia.buyerorder.detail.revamp.util.VisitableMapper
import com.tokopedia.buyerorder.detail.revamp.viewModel.OrderDetailViewModel
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.detail.view.activity.OrderListwebViewActivity
import com.tokopedia.buyerorder.detail.view.customview.HorizontalCoupleTextView
import com.tokopedia.buyerorder.recharge.data.response.AdditionalTickerInfo
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.view.DoubleTextView
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * created by @bayazidnasir on 22/8/2022
 */

class OmsDetailFragment: BaseDaggerFragment(), EventDetailsListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var orderAnalytics: OrderListAnalytics

    @Inject
    lateinit var localCacheHandler: LocalCacheHandler

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: OrderDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, factory)[OrderDetailViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentOmsListDetailBinding>()
    private var isSingleButton = false
    private var isDownloadable = false
    private var upstream: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOmsListDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(OrderDetailsComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            upstream = it.get(KEY_UPSTREAM).toString()
            showProgressBar()
            viewModel.requestOmsDetail(
                it.get(KEY_ORDER_ID).toString(),
                it.get(KEY_ORDER_CATEGORY).toString(),
                it.get(KEY_UPSTREAM).toString()
            )
            viewModel.requestDigiPerso()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.omsDetail.observe(viewLifecycleOwner){
            when(it){
                is Success -> renderDetailsData(it.data.orderDetails)
                is Fail -> showError(it.throwable)
            }
        }

        viewModel.digiPerso.observe(viewLifecycleOwner){
            when(it){
                is Success -> renderRecommendation(it.data)
                is Fail -> showError(it.throwable)
            }
        }

        viewModel.actionButton.observe(viewLifecycleOwner){
            setTapAction(it.second)
        }
    }

    private fun renderDetailsData(details: OrderDetails){
        hideProgressBar()
        setStatus(details.status)

        if (details.conditionalInfo.text.isNotEmpty()){
            setConditionalInfo(details.conditionalInfo)
        }

        details.title.forEach { setTitle(it) }

        setInvoice(details.invoice)

        details.detail.forEach { setDetail(it) }

        if (details.items.isNotEmpty()){
            setItems(details.items, details.flags.isOrderTradeIn, details)
        }

        //TODO : not used
        if (details.additionalInfo.isNotEmpty()){
            setAdditionalInfoVisibility(View.VISIBLE)
        }

        details.additionalInfo.forEach { setAdditionalInfo(it) }

        val isCategoryEvent = details.items.isNotEmpty() && details.items.first().category.equals(OrderCategory.EVENT.category, true)

        details.payMethods.forEach {
            if (it.value.isNotEmpty())
                setPayMethodInfo(it, isCategoryEvent)
        }

        details.pricing.forEach { setPricing(it, isCategoryEvent) }

        setPaymentData(details.paymentData, isCategoryEvent)
        setContactUs(details.helpLink)
        setActionButtons(details)
        setTicker(details.additionalTickerInfo)

        binding?.mainView?.visible()
    }

    private fun setTicker(tickers: List<AdditionalTickerInfo>){
        binding?.let {
            if (tickers.isNotEmpty()) {
                it.tickerStatus.visible()
                it.tickerStatus.setTextDescription(tickers.first().notes)
                if (tickers.size > TOTAL_SIZE_1) {
                    it.tickerDetailOrder.visible()
                    it.tickerDetailOrder.setTextDescription(tickers[1].notes)
                } else {
                    it.tickerDetailOrder.gone()
                }
            } else {
                it.tickerStatus.gone()
                it.tickerDetailOrder.gone()
            }
        }
    }

    private fun setDetail(detail: Detail){

    }

    private fun renderRecommendation(recommendation: RecommendationDigiPersoResponse) {

    }

    private fun showError(throwable: Throwable?) {

    }

    private fun hideProgressBar() {

    }

    private fun showProgressBar() {

    }

    private fun setStatus(status: Status) {
        binding?.statusLabel?.text = status.statusLabel
        binding?.statusValue?.text = status.statusText
        if (status.textColor.isNotEmpty()){
            binding?.statusValue?.setTextColor(Color.parseColor(status.textColor))
        }
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = SHAPE_CORNER_RADIUS_4
        }
        if (status.backgroundColor.isNotEmpty()){
            shape.setColor(Color.parseColor(status.backgroundColor))
        }
        binding?.statusValue?.background = shape
    }

    private fun setConditionalInfo(conditional: ConditionalInfo) {
        binding?.conditionalInfo?.visible()
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = SHAPE_CORNER_RADIUS_9
        }

        if (conditional.color.background.isNotEmpty()){
            shape.setColor(Color.parseColor(conditional.color.background))
        }

        if (conditional.color.border.isNotEmpty()){
            shape.setStroke(
                getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_2),
                Color.parseColor(conditional.color.border)
            )
        }

        binding?.conditionalInfo?.let {
            it.background = shape
            it.setPadding(
                getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16),
                getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16),
                getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16),
                getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16)
            )
            it.text = conditional.text
            if (conditional.color.textColor.isNotEmpty()){
                it.setTextColor(Color.parseColor(conditional.color.textColor))
            }
        }
    }

    private fun setTitle(title: Title) {
        val coupleTextView = HorizontalCoupleTextView(requireContext()).apply {
            setLabel(title.label)
            setValue(title.value)
            isShowSeparator(true)
        }
        binding?.statusDetail?.addView(coupleTextView)
    }

    private fun setInvoice(invoice: Invoice) {
        binding?.invoice?.text = invoice.invoiceRefNum
        binding?.icCopy?.setOnClickListener {
            BuyerUtils.copyTextToClipBoard(KEY_TEXT, binding?.invoice?.text.toString(), requireContext())
        }
    }

    private fun setItems(items: List<Items>, isTradeIn: Boolean, orderDetails: OrderDetails) {
        var isMetadataEmpty = true

        val newItemList = items.filter { !CATEGORY_GIFT_CARD.equals(it.category, true) }

        newItemList.forEach {
            if (it.metaData.isNotEmpty()){
                isMetadataEmpty = false
                return@forEach
            }
        }

        if (newItemList.isNotEmpty() && !isMetadataEmpty){
            binding?.recyclerView?.adapter = BaseAdapter(
                OrderDetailTypeFactoryImpl(this),
                VisitableMapper.mappingVisitable(items, false, upstream ?: "", orderDetails)
            )
        } else {
            binding?.detailsSection?.gone()
            binding?.dividerAboveInfoLabel?.gone()
        }
    }

    private fun setAdditionalInfoVisibility(visibility: Int) {

    }

    private fun setAdditionalInfo(additionalInfo: AdditionalInfo) {

    }

    private fun setPayMethodInfo(payMethod: PayMethod, isCategoryEvent: Boolean) {
        val coupleTextView = HorizontalCoupleTextView(requireContext()).apply {
            setLabel(payMethod.label)
        }

        val value = payMethod.value.replaceIf(payMethod.value.equals(getString(R.string.zero_rupiah), true) && isCategoryEvent){
            getString(R.string.free_rupiah)
        }

        coupleTextView.setValue(value)
        binding?.infoPayment?.addView(coupleTextView)
    }

    private fun setPricing(pricing: Pricing, isCategoryEvent: Boolean) {
        val coupleTextView = HorizontalCoupleTextView(requireContext()).apply {
            setLabel(pricing.label)
        }
        val value = pricing.value.replaceIf(pricing.value.equals(getString(R.string.zero_rupiah), true) && isCategoryEvent){
            getString(R.string.free_rupiah)
        }
        coupleTextView.setValue(value)
        binding?.infoValue?.addView(coupleTextView)
    }

    private fun setPaymentData(paymentData: PaymentData, isCategoryEvent: Boolean) {
        val coupleTextView = HorizontalCoupleTextView(requireContext()).apply {
            setLabel(paymentData.label)
        }
        val value = paymentData.value.replaceIf(paymentData.value.equals(getString(R.string.zero_rupiah), true) && isCategoryEvent){
            getString(R.string.free_rupiah)
        }
        coupleTextView.setValue(value)
        if (paymentData.textColor.isNotEmpty()){
            coupleTextView.setValueColor(Color.parseColor(paymentData.textColor))
        }

        binding?.totalPrice?.addView(coupleTextView)
    }

    private fun setContactUs(helpLink: String) {
        val text = getString(R.string.contact_us_text)
        val clickableLink = getString(R.string.contact_us_clickable_text)
        val spannableString = SpannableString(text)
        val startIndexOfLink = text.indexOf(clickableLink)
        if (startIndexOfLink != -1){
            spannableString.setSpan(object : ClickableSpan(){
                override fun onClick(p0: View) {
                    if (helpLink.isNotEmpty()){
                        RouteManager.route(context, helpLink)
                    } else {
                        val url = try {
                            URLDecoder.decode(getString(R.string.contact_us_applink), ENCODER)
                        } catch (e : UnsupportedEncodingException){
                            ""
                        }
                        val intent = OrderListwebViewActivity.getWebViewIntent(context, url, WEB_VIEW_TITLE)
                        startActivity(intent)
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
                }
            }, startIndexOfLink, startIndexOfLink + clickableLink.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding?.helpLabel?.let {
                it.highlightColor = Color.TRANSPARENT
                it.movementMethod = LinkMovementMethod.getInstance()
                it.setText(spannableString, TextView.BufferType.SPANNABLE)
            }
        }
    }

    private fun setTapAction(actionButtons: List<ActionButton>){

        fun getLabelIfNotEmpty(actionButton: List<ActionButton>): String{
            return if (actionButton.isNotEmpty()){
                actionButton.first().label
            } else {
                ""
            }
        }

        binding?.let {
            it.actionButton.visible()
            it.actionButtonText.text = getLabelIfNotEmpty(actionButtons)
            it.actionButton.setOnClickListener {
                if (actionButtons.isEmpty()){
                    return@setOnClickListener
                }

                val button = actionButtons.first()

                if (button.control.equals(KEY_BUTTON, true)){
                    viewModel.requestActionButton(actionButtons, 0, false)
                } else if (button.control.equals(KEY_REDIRECT, true)){
                    RouteManager.route(context, button.body.appURL)
                }
            }
        }
    }

    private fun setActionButtons(details: OrderDetails) {
        if (details.items.isNotEmpty()
            && ( details.items.first().category.equals(OrderCategory.EVENT.category, true)
                    || details.items.first().category.equals(OrderCategory.DEALS.category, true))){
            setActionButtonsVisibility(View.GONE, View.GONE)
        } else {
            when(details.actionButtons.size){
                TOTAL_SIZE_2 -> {
                    setTopActionButton(details.actionButtons[0])
                    setBottomActionButton(details.actionButtons[1])
                }
                TOTAL_SIZE_1 -> {
                    val actionButton = details.actionButtons.first()
                    isSingleButton = true
                    if (actionButton.label == BuyerConsts.INVOICE){
                        setBottomActionButton(actionButton)
                        setActionButtonsVisibility(View.GONE, View.VISIBLE)
                    } else {
                        setTopActionButton(actionButton)
                        setActionButtonsVisibility(View.VISIBLE, View.GONE)
                    }
                }
                else -> {
                    setActionButtonsVisibility(View.GONE, View.GONE)
                }
            }
        }
    }

    private fun setTopActionButton(actionButton: ActionButton) {
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(
                getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0),
                getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0),
                getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0),
                getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_0),
            )
        }
        binding?.langannan?.text = actionButton.label
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = SHAPE_CORNER_RADIUS_4
            setColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            setStroke(
                SHAPE_STROKE_2,
                getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100)
            )
        }
        binding?.langannan?.background = shape
        if (isSingleButton){
            binding?.langannan?.layoutParams = params
        }
        if (actionButton.body.appURL.isNotEmpty()){
            binding?.langannan?.setOnClickListener {
                clickActionButton(actionButton.body.appURL)
            }
        }
    }

    private fun setBottomActionButton(actionButton: ActionButton) {
        binding?.beliLagi?.text = actionButton.label
        val shape = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = SHAPE_CORNER_RADIUS_4
            setColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_Y500))
        }
        binding?.beliLagi?.let {
            it.background = shape
            it.setTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            if (actionButton.body.appURL.isNotEmpty()){
                it.setOnClickListener { clickActionButton(actionButton.body.appURL) }
            }
        }
    }

    private fun clickActionButton(uri: String){
        var newUri = uri
        if (uri.startsWith(KEY_URI)){
            if (newUri.contains(KEY_URI_PARAMETER)){
                val url = Uri.parse(newUri)
                newUri = newUri.replace(url.getQueryParameter(KEY_URI_PARAMETER) ?: "", "")
                newUri = newUri.replace(KEY_URI_PARAMETER_EQUAL, "")
            }
            RouteManager.route(context, newUri)
        } else if (uri.isNotEmpty()) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, uri)
        }
    }

    private fun setActionButtonsVisibility(primaryBtnVisibility: Int, secondaryBtnVisibility: Int) {
        binding?.langannan?.visibility = primaryBtnVisibility
        binding?.beliLagi?.visibility = secondaryBtnVisibility
    }

    private inline fun String.replaceIf(isReplace: Boolean, replacement: () -> String) : String {
        return if (isReplace) replacement() else this
    }

    private inline fun Int.replaceIf(isReplace: Boolean, replacement: () -> Int) : Int {
        return if (isReplace) replacement() else this
    }

    private fun getDimensionPixelSize(@DimenRes id: Int): Int{
        return context?.resources?.getDimensionPixelSize(id) ?: 0
    }

    private fun getDimensionPixelOffset(@DimenRes id: Int): Int{
        return context?.resources?.getDimensionPixelOffset(id) ?: 0
    }

    private fun getColor(@ColorRes colorId: Int): Int{
        return MethodChecker.getColor(context, colorId)
    }

    override fun setEventDetails(actionButton: ActionButton, item: Items) {
        TODO("Not yet implemented")
    }

    override fun openQRFragment(actionButton: ActionButton, item: Items) {
        if (item.category.equals(VisitableMapper.CATEGORY_DEALS, true)
            || item.categoryID == VisitableMapper.DEALS_CATEGORY_ID ) {
            DealsQRBottomSheet().apply {
                renderView(actionButton)
                show(childFragmentManager)
            }
        } else {
            EventsQRBottomSheet().apply {
                renderView(actionButton)
                show(childFragmentManager)
            }
        }
    }

    override fun setDetailTitle(title: String) {
        TODO("Not yet implemented")
    }

    override fun setInsuranceDetail() {
        TODO("Not yet implemented")
    }

    override fun setPassengerEvent(item: Items, metadata: MetaDataInfo) {
        if (!item.category.equals(OrderCategory.EVENT.category, true)){
            return
        }

        binding?.let {
            if (metadata.passengerForms.isEmpty()){
                it.userLabel.gone()
                it.userInformationLayout.gone()
                it.dividerAboveUserInfo.gone()

                return@let
            }

            it.userLabel.visible()
            it.userInformationLayout.visible()
            it.dividerAboveUserInfo.visible()
            it.userInformationLayout.removeAllViews()

            metadata.entityPessengers.forEach { passenger ->
                val doubleTextView = DoubleTextView(context, LinearLayout.VERTICAL).apply {
                    setTopText(passenger.title)
                    setTopTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    setBottomText(passenger.value)
                    setBottomTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                    setBottomTextStyle(BOLD_TEXT_STYLE)
                }
                it.userInformationLayout.addView(doubleTextView)
            }
        }
    }

    override fun setActionButtonEvent(
        actionButton: ActionButton,
        item: Items,
        orderDetails: OrderDetails
    ) {
        binding?.let {
            if (orderDetails.actionButtons.isEmpty()) {
                it.actionButton.gone()
                it.dividerAboveActionButton.gone()
            } else {
                it.dividerAboveActionButton.visible()
                it.actionButton.visible()
                it.actionButtonText.text = actionButton.label
                it.actionButton.setOnClickListener {
                    if (actionButton.control.equals(KEY_BUTTON, true)
                        && actionButton.name.equals(KEY_CUSTOMER_NOTIFICATION, true)){
                        TODO("hit send email")
                        return@setOnClickListener
                    }

                    if (actionButton.control.equals(KEY_REDIRECT, true)) {
                        RouteManager.route(context, actionButton.body.appURL)
                    }
                }
            }
        }
    }

    override fun setDealsBanner(metadata: MetaDataInfo) {
        if (metadata.customLinkType.equals(KEY_REDIRECT, true)){
            val bannerView = binding?.bannerDealsOrderDetail?.root
            binding?.dividerAboveBannerDeals?.visible()
            bannerView?.visible()

            if (isCoachMarkAlreadyShowed()){
                bannerView?.post {
                    val scrollTo =  bannerView.top + bannerView.top
                    binding?.parentScroll?.smoothScrollTo(0, scrollTo)
                    addCoachMark()
                }
            }
            binding?.bannerDealsOrderDetail?.tgDealBannerTitle?.text =
                getString(R.string.banner_deals_main_title, userSession.name)

            if (metadata.customLinkLabel.isNotEmpty()) {
                binding?.bannerDealsOrderDetail?.tgDealBannerSubTitle?.text = metadata.customLinkLabel
            }

            binding?.bannerDealsOrderDetail?.root?.setOnClickListener {
                if (metadata.customLinkAppUrl.isNotEmpty()){
                    RouteManager.route(context, metadata.customLinkAppUrl)
                }
            }
        }
    }

    private fun isCoachMarkAlreadyShowed() = localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, true)

    private fun addCoachMark(){
        val coachMarkItem = CoachMarkItem(
            binding?.bannerDealsOrderDetail?.root,
            getString(R.string.banner_deals_coachmark_title),
            getString(R.string.banner_deals_coachmark_sub_title)
        )
        val listCoachMark = arrayListOf(coachMarkItem)

        val coachMark = CoachMarkBuilder().build()
        Handler(Looper.getMainLooper()).postDelayed({
            coachMark.show(activity, "", listCoachMark)
        }, DELAY_COACH_MARK_START)

        localCacheHandler.apply {
            putBoolean(SHOW_COACH_MARK_KEY, false)
            applyEditor()
        }
    }

    override fun askPermission(uri: String, isDownloadable: Boolean, downloadFileName: String) {
        val permissionChecker = PermissionCheckerHelper()
        val permissions = arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
            PermissionCheckerHelper.Companion.PERMISSION_READ_EXTERNAL_STORAGE,
        )
        permissionChecker.checkPermissions(this, permissions, object : PermissionCheckerHelper.PermissionCheckListener{
            override fun onPermissionDenied(permissionText: String) {}

            override fun onNeverAskAgain(permissionText: String) {}

            override fun onPermissionGranted() {
                if (isDownloadable && uri.isNotEmpty() && downloadFileName.isNotEmpty()){
                    permissionGrantedContinueDownload(uri, downloadFileName, isDownloadable)
                }
            }
        })
    }

    private fun permissionGrantedContinueDownload(
        uri: String,
        fileName: String,
        isDownloadable: Boolean
    ){
        this.isDownloadable = isDownloadable
        val downloadHelper = DownloadHelper(requireContext(), uri, fileName, null)
        downloadHelper.downloadFile { isUriDownloadable(uri) }
    }

    private fun isUriDownloadable(url: String): Boolean{
        val pattern = Pattern.compile("^.+\\.([pP][dD][fF])\$")
        val matcher = pattern.matcher(url)
        return matcher.find() || isDownloadable
    }

    override fun sendThankYouEvent(
        metadata: MetaDataInfo,
        categoryType: Int,
        orderDetails: OrderDetails
    ) {
        val fromPayment = arguments?.get(KEY_FROM_PAYMENT).toString()
        if (!fromPayment.equals(IS_TRUE, true)){
            return
        }

        val paymentStatus = orderDetails.status.statusText.ifEmpty { "" }
        val paymentMethod = orderDetails.payMethods.first().value.replaceIf(
            orderDetails.payMethods.isNotEmpty()
                    && orderDetails.payMethods.first().value.isNotEmpty()
        ) { "" }

        val isCategoryEvent = categoryType == EventsViewHolder.ITEM_EVENTS

        orderAnalytics.sendThankYouEvent(
            metadata.entityProductId,
            metadata.productName.replaceIf(isCategoryEvent) { metadata.entityProductName },
            metadata.totalPrice.replaceIf(isCategoryEvent) { metadata.totalTicketPrice },
            metadata.quantity.replaceIf(isCategoryEvent) { metadata.totalTicketCount },
            metadata.entityBrandName,
            arguments?.get(KEY_ORDER_ID).toString(),
            categoryType,
            paymentMethod,
            paymentStatus,
        )
    }

    override fun sendOpenScreenDeals(isOMP: Boolean) {
        orderAnalytics.sendOpenScreenDeals(isOMP)
    }

    override fun setActionButtonGql(tapAction: List<ActionButton>, position: Int, flag: Boolean) {
        viewModel.requestActionButton(tapAction, position, flag)
    }

    companion object{
        private const val KEY_URI = "tokopedia"
        private const val KEY_URI_PARAMETER = "idem_potency_key"
        private const val KEY_URI_PARAMETER_EQUAL = "idem_potency_key="
        private const val KEY_ORDER_ID = "OrderId"
        private const val KEY_ORDER_CATEGORY = "OrderCategory"
        private const val KEY_FROM_PAYMENT = "from_payment"
        private const val KEY_UPSTREAM = "upstream"
        private const val KEY_BUTTON = "button"
        private const val KEY_REDIRECT = "redirect"
        private const val KEY_CUSTOMER_NOTIFICATION = "customer_notification"
        private const val SHAPE_STROKE_2 = 2
        private const val SHAPE_CORNER_RADIUS_4 = 4f
        private const val SHAPE_CORNER_RADIUS_9 = 9f
        private const val TOTAL_SIZE_2 = 2
        private const val TOTAL_SIZE_1 = 1
        private const val DELAY_COACH_MARK_START = 500L
        private const val KEY_TEXT = "text"
        private const val ENCODER = "UTF-8"
        private const val WEB_VIEW_TITLE = "Help Centre"
        private const val CATEGORY_GIFT_CARD = "Gift-card"
        private const val IS_TRUE = "true"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key_deals_banner"
        private const val BOLD_TEXT_STYLE = "bold"
        const val PREFERENCES_NAME = "deals_banner_preferences"

        fun getInstance(
            orderId: String,
            orderCategory: String,
            fromPayment: String,
            upstream: String): Fragment {
            return OmsDetailFragment().also {
                val bundle = Bundle().apply {
                    putString(KEY_ORDER_ID, orderId)
                    putString(KEY_ORDER_CATEGORY, orderCategory)
                    putString(KEY_FROM_PAYMENT, fromPayment)
                    putString(KEY_UPSTREAM, upstream)
                }
                it.arguments = bundle
            }
        }
    }
}
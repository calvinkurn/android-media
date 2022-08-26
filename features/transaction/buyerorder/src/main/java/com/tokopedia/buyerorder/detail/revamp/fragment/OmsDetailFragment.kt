package com.tokopedia.buyerorder.detail.revamp.fragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
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
import com.tokopedia.buyerorder.databinding.LayoutScanQrCodeBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.ActionButtonEventWrapper
import com.tokopedia.buyerorder.detail.data.ConditionalInfo
import com.tokopedia.buyerorder.detail.data.Invoice
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.PayMethod
import com.tokopedia.buyerorder.detail.data.PaymentData
import com.tokopedia.buyerorder.detail.data.Pricing
import com.tokopedia.buyerorder.detail.data.RedeemVoucherModel
import com.tokopedia.buyerorder.detail.data.Status
import com.tokopedia.buyerorder.detail.data.Title
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.revamp.adapter.OrderDetailTypeFactoryImpl
import com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder.EventsViewHolder
import com.tokopedia.buyerorder.detail.revamp.util.OrderCategory
import com.tokopedia.buyerorder.detail.revamp.util.VisitableMapper
import com.tokopedia.buyerorder.detail.revamp.viewModel.OrderDetailViewModel
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.buyerorder.detail.view.activity.OrderListwebViewActivity
import com.tokopedia.buyerorder.detail.view.adapter.RedeemVoucherAdapter
import com.tokopedia.buyerorder.detail.view.customview.HorizontalCoupleTextView
import com.tokopedia.buyerorder.recharge.data.response.AdditionalTickerInfo
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
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

    private lateinit var typeFactory: OrderDetailTypeFactoryImpl
    private lateinit var adapter: BaseAdapter<OrderDetailTypeFactoryImpl>

    private val viewModel: OrderDetailViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, factory)[OrderDetailViewModel::class.java]
    }

    private val permissionChecker: PermissionCheckerHelper by lazy(LazyThreadSafetyMode.NONE) {
        PermissionCheckerHelper()
    }

    private var binding by autoClearedNullable<FragmentOmsListDetailBinding>()

    private var isSingleButton = false
    private var isDownloadable = false
    private var upstream: String? = null
    private var dealsBottomSheet: DealsQRBottomSheet? = null
    private var eventBottomSheet: BottomSheetUnify? = null
    private var listItems: List<Items>? = null

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

        typeFactory = OrderDetailTypeFactoryImpl(this)
        dealsBottomSheet = DealsQRBottomSheet()
        eventBottomSheet = BottomSheetUnify()

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

        viewModel.actionButton.observe(viewLifecycleOwner){
            when(it){
                is ActionButtonEventWrapper.TapActionButton -> {
                    setTapActionButton(it.position, it.list)
                }
                is ActionButtonEventWrapper.SetActionButton -> {
                    setActionButton(it.position, it.list)
                }
                is ActionButtonEventWrapper.RenderActionButton -> {
                    renderActionButton(it.list)
                }
            }
        }

        viewModel.eventEmail.observe(viewLifecycleOwner){
            when(it){
                is Success -> {
                    showToaster(getString(R.string.event_voucher_code_copied))
                    setActionButtonText(getString(R.string.event_voucher_code_success))
                }
                is Fail -> {
                    showToaster(it.throwable.message)
                    setActionButtonText(getString(R.string.event_voucher_code_fail))
                }
            }
        }

        viewModel.actionClickable.observe(viewLifecycleOwner){
            binding?.actionButton?.isClickable = it
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

        if (details.items.isNotEmpty()){
            listItems = details.items
            setItems(details.items, details)
        }

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

    private fun showError(throwable: Throwable?) {
        val errorMsg = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.build(it, errorMsg).show()
        }
    }

    private fun hideProgressBar() {
        binding?.progressBarLayout?.gone()
        binding?.mainView?.visible()
    }

    private fun showProgressBar() {
        binding?.progressBarLayout?.visible()
        binding?.mainView?.gone()
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
            showToaster(getString(R.string.deals_label_copied))
        }
    }

    private fun setItems(items: List<Items>, orderDetails: OrderDetails) {
        var isMetadataEmpty = true

        val newItemList = items.filter { !CATEGORY_GIFT_CARD.equals(it.category, true) }

        newItemList.forEach {
            if (it.metaData.isNotEmpty()){
                isMetadataEmpty = false
                return@forEach
            }
        }

        if (newItemList.isNotEmpty() && !isMetadataEmpty){
            adapter = BaseAdapter(
                typeFactory,
                VisitableMapper.mappingVisitable(items, false, upstream ?: "", orderDetails)
            )
            binding?.recyclerView?.adapter = adapter
        } else {
            binding?.detailsSection?.gone()
            binding?.dividerAboveInfoLabel?.gone()
        }
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
                    if (helpLink.isNotEmpty()) RouteManager.route(context, helpLink)
                    else redirectToWebView(getString(R.string.contact_us_applink), WEB_VIEW_TITLE_HELP)
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

    private fun setActionButton(position: Int, actionButtons: List<ActionButton>){
        if (!listItems.isNullOrEmpty()){
            listItems?.let {
                it[position].tapActions = actionButtons
                it[position].isTapActionsLoaded = true
            }
            adapter.notifyItemChanged(position)
        }
    }

    private fun setTapActionButton(position: Int, actionButtons: List<ActionButton>){
        if (!listItems.isNullOrEmpty()){
            listItems?.let {
                it[position].actionButtons = actionButtons
                it[position].isActionButtonLoaded = true
            }
            adapter.notifyItemChanged(position)
        }
    }

    private fun renderActionButton(actionButtons: List<ActionButton>){

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
                    viewModel.requestActionButton(actionButtons, 0, flag = false, false)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            permissionChecker.onRequestPermissionsResult(activity, requestCode, permissions, grantResults)
        }
    }

    override fun setEventDetails(actionButton: ActionButton, item: Items, metadata: MetaDataInfo) {
        binding?.let {
            if (item.actionButtons.isEmpty()) {
                it.actionButton.gone()
            } else {
                it.dividerAboveActionButton.visible()
                it.actionButton.visible()
                it.actionButtonText.text = actionButton.label
                it.actionButton.setOnClickListener {
                    if (actionButton.control.equals(KEY_BUTTON, true)) {
                        if (item.category.isNotEmpty() && item.category.equals(KEY_DEAL, true)){
                            view?.let { v -> Toaster.build(v, String.format(
                                TOASTER_FORMAT,
                                getString(R.string.deal_voucher_code_copied),
                                metadata.entityAddress.email
                            )).show() }
                        } else {
                            view?.let { v -> Toaster.build(v, String.format(
                                TOASTER_FORMAT,
                                getString(R.string.event_voucher_code_copied),
                                metadata.entityAddress.email
                            )).show() }
                        }

                        viewModel.requestActionButton(item.actionButtons, 0, flag = false, false)

                        return@setOnClickListener
                    }

                    if (actionButton.control.equals(KEY_REDIRECT, true )) {
                        RouteManager.route(context, actionButton.body.appURL)
                    }
                }
            }

            if (!item.category.equals(OrderCategory.EVENT.category, true)) {
                if (metadata.entityPackages.isNotEmpty()) {
                    it.userLabel.visible()
                    it.userInformationLayout.visible()
                    it.dividerAboveUserInfo.visible()
                    it.userInformationLayout.removeAllViews()

                    metadata.entityPessengers.forEach { entityPessenger ->
                        val doubleTextView = DoubleTextView(context, LinearLayout.VERTICAL).apply {
                            setTopText(entityPessenger.title)
                            setTopTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                            setBottomText(entityPessenger.value)
                            setBottomTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                            setBottomTextStyle(BOLD_TEXT_STYLE)
                        }
                        it.userInformationLayout.addView(doubleTextView)
                    }
                } else {
                    it.userLabel.gone()
                    it.userInformationLayout.gone()
                    it.dividerAboveUserInfo.gone()
                }
            }
        }
    }

    override fun openQRFragment(actionButton: ActionButton, item: Items) {
        if (item.category.equals(VisitableMapper.CATEGORY_DEALS, true)
            || item.categoryID == VisitableMapper.DEALS_CATEGORY_ID ) {
            dealsBottomSheet?.let {
                it.renderView(actionButton)
                it.show(childFragmentManager)
            }
        } else {
            //TODO : create bottom sheet separated
            showQREvent(actionButton)
        }
    }

    private fun showQREvent(actionButton: ActionButton){
        val bottomSheetView = LayoutScanQrCodeBinding.inflate(LayoutInflater.from(context), binding?.mainView, false)
        eventBottomSheet?.let {
            it.setTitle(getString(R.string.text_redeem_voucher))
            it.setCloseClickListener { _ ->
                it.dismiss()
            }
        }

        bottomSheetView.let {
            var totalItem = 0
            val voucherList = mutableListOf<RedeemVoucherModel>()
            val indicatorItems = mutableListOf<ImageView>()

            if (actionButton.body.body.isNotEmpty()){
                val voucherCodes = actionButton.body.body.split(",")
                voucherCodes.forEachIndexed { index, code ->
                    voucherList.add(
                        RedeemVoucherModel(
                            actionButton.headerObject.powered_by,
                            actionButton.body.appURL,
                            code,
                            actionButton.headerObject.statusLabel)
                    )

                    totalItem = voucherCodes.size

                    val indicator = ImageView(context).apply {
                        setPadding(
                            5,
                            0,
                            5,
                            0
                        )
                    }

                    if (index == 0) {
                        indicator.setImageResource(R.drawable.ic_indicator_selected)
                    } else {
                        indicator.setImageResource(R.drawable.ic_indicator_unselected)
                    }

                    indicatorItems.add(indicator)
                    it.itemIndicator.addView(indicator)
                }

                it.tvLabelCount.showWithCondition(voucherCodes.size > 1)
                it.itemIndicator.showWithCondition(voucherCodes.size > 1)
            }

            it.tvLabelCount.text = getString(R.string.deals_voucher_label_count, 1, totalItem)

            val redeemAdapter = RedeemVoucherAdapter(voucherList)
            with(it.rvVoucher){
                adapter = redeemAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }

            if (it.rvVoucher.onFlingListener == null){
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(it.rvVoucher)
            }

            redeemAdapter.setOnCopiedListener { voucherCode ->
                context?.let { ctx ->
                    BuyerUtils.copyTextToClipBoard("text", voucherCode, ctx)
                }
                view?.let { v ->
                    Toaster.build(v, getString(R.string.deals_msg_copy)).show()
                }
            }

            eventBottomSheet?.setOnDismissListener {
                it.rvVoucher.onFlingListener = null
            }
        }

        activity?.let {
            eventBottomSheet?.setChild(bottomSheetView.root)
            eventBottomSheet?.show(it.supportFragmentManager, "qr_event")
        }
    }

    override fun setDetailTitle(title: String) {
        if (title.isNotEmpty()) {
            binding?.detailLabel?.text = title
        }
    }

    override fun setInsuranceDetail() {
        binding?.let {
            it.policy.visible()
            it.claim.visible()
            it.dividerAboveInfoLabel.gone()
            it.claim.setOnClickListener {
                RouteManager.route(context, INSURANCE_CLAIM)
            }
        }
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

            metadata.passengerForms.forEach { passengerForm ->
                passengerForm.passengerInformations.forEach { passengerInformation ->
                    val doubleTextView = DoubleTextView(context, LinearLayout.VERTICAL).apply {
                        setTopText(passengerInformation.title)
                        setTopTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                        setBottomText(passengerInformation.value)
                        setBottomTextColor(getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                        setBottomTextStyle(BOLD_TEXT_STYLE)
                    }
                    it.userInformationLayout.addView(doubleTextView)
                }
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
                        viewModel.sendEventEmail(actionButton, orderDetails.metadata)
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
                    val scrollTo =  (bannerView.parent as View).top  + bannerView.top
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

    override fun setActionButtonGql(tapAction: List<ActionButton>, position: Int, flag: Boolean, isCalledFromAdapter: Boolean) {
        viewModel.requestActionButton(tapAction, position, flag, isCalledFromAdapter)
    }

    override fun showRetryButtonToaster(message: String) {
        showToaster(message)
    }

    override fun onTapActionDeals(
        view: TextView?,
        actionButton: ActionButton,
        item: Items,
        count: Int,
        position: Int
    ) {
        if (actionButton.control.equals(KEY_BUTTON, true)
            || actionButton.control.equals(KEY_REFRESH, true) ) {
            viewModel.requestActionButton(item.actionButtons, position, true, isCalledFromAdapter = true)
            return
        }

        when{
            actionButton.control.equals(KEY_REDIRECT, true) -> {
                if (actionButton.body.appURL.isEmpty()){
                    return
                }

                if (view == null) RouteManager.route(context, actionButton.body.appURL)
                else redirectToWebView(actionButton.body.appURL, WEB_VIEW_TITLE_REDEEM)
            }
            actionButton.control.equals(KEY_QRCODE, true) -> {
                openQRFragment(actionButton, item)
            }
            actionButton.control.equals(KEY_REDIRECT_EXTERNAL, true) -> {
                RouteManager.route(context, actionButton.body.appURL)
            }
        }
    }

    private fun redirectToWebView(appLink: String, title: String){
        val url = try {
            URLDecoder.decode(appLink, ENCODER)
        } catch (e : UnsupportedEncodingException){
            ""
        }
        val intent = OrderListwebViewActivity.getWebViewIntent(context, url, title)
        startActivity(intent)
    }

    private fun showToaster(message: String?){
        view?.let {
            Toaster.build(it, message ?: DEFAULT_MESSAGE_ERROR, Toaster.LENGTH_INDEFINITE, actionText = getString(R.string.review_oke)).show()
        }
    }

    private fun setActionButtonText(text: String){
        binding?.actionButtonText?.text = text
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
        private const val KEY_REDIRECT_EXTERNAL = "redirectexternal"
        private const val KEY_REFRESH = "refresh"
        private const val KEY_QRCODE = "qrcode"
        private const val KEY_CUSTOMER_NOTIFICATION = "customer_notification"
        private const val KEY_DEAL = "Deal"
        private const val SHAPE_STROKE_2 = 2
        private const val SHAPE_CORNER_RADIUS_4 = 4f
        private const val SHAPE_CORNER_RADIUS_9 = 9f
        private const val TOTAL_SIZE_2 = 2
        private const val TOTAL_SIZE_1 = 1
        private const val DELAY_COACH_MARK_START = 500L
        private const val KEY_TEXT = "text"
        private const val ENCODER = "UTF-8"
        private const val WEB_VIEW_TITLE_HELP = "Help Centre"
        private const val WEB_VIEW_TITLE_REDEEM = "Redeem Voucher"
        private const val CATEGORY_GIFT_CARD = "Gift-card"
        private const val IS_TRUE = "true"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key_deals_banner"
        private const val BOLD_TEXT_STYLE = "bold"
        private const val TOASTER_FORMAT = "%s %s"
        private const val INSURANCE_CLAIM = "tokopedia://webview?allow_override=false&url=https://www.tokopedia.com/asuransi/klaim"
        private const val DEFAULT_MESSAGE_ERROR = "Something Error"
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
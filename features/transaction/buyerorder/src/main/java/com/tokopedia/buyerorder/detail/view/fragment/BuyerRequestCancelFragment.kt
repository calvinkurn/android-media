package com.tokopedia.buyerorder.detail.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.common.util.BuyerConsts.BUTTON_INSTANT_CANCELATION
import com.tokopedia.buyerorder.common.util.BuyerConsts.BUTTON_REGULER_CANCELATION
import com.tokopedia.buyerorder.common.util.BuyerConsts.BUYER_CANCEL_REASON_SCREEN_NAME
import com.tokopedia.buyerorder.common.util.BuyerConsts.LAINNYA
import com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_CODE_INSTANT_CANCEL
import com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_MSG_INSTANT_CANCEL
import com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_POPUP_BODY_INSTANT_CANCEL
import com.tokopedia.buyerorder.common.util.BuyerConsts.RESULT_POPUP_TITLE_INSTANT_CANCEL
import com.tokopedia.buyerorder.common.util.BuyerConsts.TICKER_LABEL
import com.tokopedia.buyerorder.common.util.BuyerConsts.TICKER_URL
import com.tokopedia.buyerorder.common.util.BuyerUtils
import com.tokopedia.buyerorder.detail.analytics.BuyerAnalytics
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData.Data.GetCancellationReason.TickerInfo
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.activity.BuyerRequestCancelActivity
import com.tokopedia.buyerorder.detail.view.adapter.BuyerListOfProductsBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.GetCancelReasonBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.GetCancelSubReasonBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerCancellationViewModel
import com.tokopedia.buyerorder.list.common.OrderListContants
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.bottomsheet_buyer_request_cancel.view.*
import kotlinx.android.synthetic.main.fragment_buyer_request_cancel.*
import java.io.Serializable
import javax.inject.Inject


/**
 * Created by fwidjaja on 08/06/20.
 */
class BuyerRequestCancelFragment: BaseDaggerFragment(),
        GetCancelReasonBottomSheetAdapter.ActionListener, GetCancelSubReasonBottomSheetAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var reasonBottomSheetAdapter: GetCancelReasonBottomSheetAdapter
    private lateinit var subReasonBottomSheetAdapter: GetCancelSubReasonBottomSheetAdapter
    private lateinit var buyerListOfProductsBottomSheetAdapter: BuyerListOfProductsBottomSheetAdapter
    private var shopName = ""
    private var invoiceNum = ""
    private var orderId = ""
    private var uri = ""
    private var isCancelAlreadyRequested : Boolean = false
    private var isWaitToCancel : Boolean = false
    private var cancelRequestedTitle = ""
    private var cancelRequestedBody = ""
    private var waitMessage = ""
    private var shopId = -1
    private var boughtDate = ""
    private var invoiceUrl = ""
    private var statusId = ""
    private var statusInfo = ""
    private var listProductsSerializable : Serializable? = null
    private var listProduct = emptyList<Items>()
    private var cancelReasonResponse = BuyerGetCancellationReasonData.Data.GetCancellationReason()
    private var instantCancelResponse = BuyerInstantCancelData.Data.BuyerInstantCancel()
    private var buyerRequestCancelResponse = BuyerRequestCancelData.Data.BuyerRequestCancel()
    private var bottomSheet = BottomSheetUnify()
    private var reasonCancel = ""
    private var isCancelAlreadyClicked = false
    private var reasonCode = -1
    private var arrayListOfReason = arrayListOf<String>()
    private var listOfSubReason = listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem.SubReasonsItem>()
    private var currentReasonStr = ""
    private var userSession: UserSession? = null

    private val buyerCancellationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[BuyerCancellationViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BuyerRequestCancelFragment {
            return BuyerRequestCancelFragment().apply {
                arguments = Bundle().apply {
                    putString(BuyerConsts.PARAM_SHOP_NAME, bundle.getString(BuyerConsts.PARAM_SHOP_NAME))
                    putString(BuyerConsts.PARAM_INVOICE, bundle.getString(BuyerConsts.PARAM_INVOICE))
                    putSerializable(BuyerConsts.PARAM_LIST_PRODUCT, bundle.getSerializable(BuyerConsts.PARAM_LIST_PRODUCT))
                    putString(BuyerConsts.PARAM_ORDER_ID, bundle.getString(BuyerConsts.PARAM_ORDER_ID))
                    putString(BuyerConsts.PARAM_URI, bundle.getString(BuyerConsts.PARAM_URI))
                    putBoolean(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED, bundle.getBoolean(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED))
                    putString(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED, bundle.getString(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED))
                    putString(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED, bundle.getString(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED))
                    putInt(BuyerConsts.PARAM_SHOP_ID, bundle.getInt(BuyerConsts.PARAM_SHOP_ID))
                    putString(BuyerConsts.PARAM_BOUGHT_DATE, bundle.getString(BuyerConsts.PARAM_BOUGHT_DATE))
                    putString(BuyerConsts.PARAM_INVOICE_URL, bundle.getString(BuyerConsts.PARAM_INVOICE_URL))
                    putString(BuyerConsts.PARAM_STATUS_ID, bundle.getString(BuyerConsts.PARAM_STATUS_ID))
                    putString(BuyerConsts.PARAM_STATUS_INFO, bundle.getString(BuyerConsts.PARAM_STATUS_INFO))
                    putBoolean(BuyerConsts.PARAM_IS_WAIT_TO_CANCEL, bundle.getBoolean(BuyerConsts.PARAM_IS_WAIT_TO_CANCEL))
                    putString(BuyerConsts.PARAM_WAIT_MSG, bundle.getString(BuyerConsts.PARAM_WAIT_MSG))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            shopName = arguments?.getString(BuyerConsts.PARAM_SHOP_NAME).toString()
            invoiceNum = arguments?.getString(BuyerConsts.PARAM_INVOICE).toString()
            listProductsSerializable = arguments?.getSerializable(BuyerConsts.PARAM_LIST_PRODUCT)
            listProduct = listProductsSerializable as List<Items>
            orderId = arguments?.getString(BuyerConsts.PARAM_ORDER_ID).toString()
            uri = arguments?.getString(BuyerConsts.PARAM_URI).toString()
            isCancelAlreadyRequested = arguments?.getBoolean(BuyerConsts.PARAM_IS_CANCEL_ALREADY_REQUESTED) ?: false
            cancelRequestedTitle = arguments?.getString(BuyerConsts.PARAM_TITLE_CANCEL_REQUESTED).toString()
            cancelRequestedBody = arguments?.getString(BuyerConsts.PARAM_BODY_CANCEL_REQUESTED).toString()
            shopId = arguments?.getInt(BuyerConsts.PARAM_SHOP_ID) ?: -1
            boughtDate = arguments?.getString(BuyerConsts.PARAM_BOUGHT_DATE).toString()
            invoiceUrl = arguments?.getString(BuyerConsts.PARAM_INVOICE_URL).toString()
            statusId = arguments?.getString(BuyerConsts.PARAM_STATUS_ID).toString()
            statusInfo = arguments?.getString(BuyerConsts.PARAM_STATUS_INFO).toString()
            isWaitToCancel = arguments?.getBoolean(BuyerConsts.PARAM_IS_WAIT_TO_CANCEL) ?: false
            waitMessage = arguments?.getString(BuyerConsts.PARAM_WAIT_MSG).toString()
        }
        getCancelReasons()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buyer_request_cancel, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(OrderDetailsComponent::class.java).inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { BuyerAnalytics.sendScreenName(it, BUYER_CANCEL_REASON_SCREEN_NAME) }
        observingCancelReasons()
        observingInstantCancel()
        observingRequestCancel()

        btn_req_cancel?.isEnabled = false
        tf_choose_sub_reason?.textFieldInput?.isFocusable = false
        tf_choose_sub_reason?.textFieldInput?.isClickable = false
        tf_choose_sub_reason?.setOnClickListener {}

        reasonBottomSheetAdapter = GetCancelReasonBottomSheetAdapter(this)
        label_shop_name?.text = shopName
        label_invoice?.text = invoiceNum

        if (listProduct.isNotEmpty()) {
            label_product_name?.text = listProduct.first().title
            label_price?.text = listProduct.first().price
            iv_product?.loadImage(listProduct.first().imageUrl)

            if (listProduct.size > 1) {
                label_see_all_products?.visible()
                label_see_all_products?.text = "${getString(R.string.see_all_placeholder)} (${listProduct.size})"
                label_see_all_products?.setOnClickListener { showProductsBottomSheet() }
            } else {
                label_see_all_products?.gone()
            }
        }

        when {
            isCancelAlreadyRequested -> {
                setLayoutCancelAlreadyRequested()
            }
            isWaitToCancel -> {
                setLayoutWaitToCancel()
            }
            else -> {
                setLayoutCancelIsAvailable()
            }
        }

    }

    private fun setLayoutCancelAlreadyRequested() {
        tv_cancel_wait_desc?.gone()
        tv_cancel_wait_time?.gone()
        btn_req_cancel_wait?.gone()

        tf_choose_reason?.gone()
        tf_choose_sub_reason?.gone()
        btn_req_cancel?.gone()

        iv_check?.visible()
        tv_title_cancel_requested?.visible()
        tv_body_cancel_requested?.visible()
        btn_chat_penjual?.visible()

        tv_title_cancel_requested?.text = cancelRequestedTitle
        tv_body_cancel_requested?.text = cancelRequestedBody
        setListenerCancelRequestedAlready()
    }

    private fun setLayoutCancelIsAvailable() {
        tv_cancel_wait_desc?.gone()
        tv_cancel_wait_time?.gone()
        btn_req_cancel_wait?.gone()

        iv_check?.gone()
        tv_title_cancel_requested?.gone()
        tv_body_cancel_requested?.gone()
        btn_chat_penjual?.gone()

        tf_choose_sub_reason?.gone()
        btn_req_cancel?.visible()

        tf_choose_reason?.visible()
        tf_choose_reason?.textFieldInput?.isFocusable = false
        tf_choose_reason?.textFieldInput?.isClickable = true
        setListenersCancelIsAvailable()
    }

    @SuppressLint("SetTextI18n")
    private fun setLayoutWaitToCancel() {
        iv_check?.gone()
        tv_title_cancel_requested?.gone()
        tv_body_cancel_requested?.gone()
        btn_chat_penjual?.gone()

        tf_choose_reason?.gone()
        tf_choose_sub_reason?.gone()
        btn_req_cancel?.gone()

        tv_cancel_wait_desc?.visible()
        if (waitMessage.contains(BuyerConsts.KEY_SETELAH))  {
            tv_cancel_wait_desc?.text = waitMessage.substring(0, waitMessage.indexOf(BuyerConsts.KEY_SETELAH)+7) + BuyerConsts.KEY_HOUR_DIVIDER

            if (waitMessage.contains(BuyerConsts.KEY_LAGI)) {
                tv_cancel_wait_time?.visible()
                tv_cancel_wait_time?.text = waitMessage.substring(waitMessage.indexOf(BuyerConsts.KEY_SETELAH)+7, waitMessage.indexOf(BuyerConsts.KEY_LAGI))
            }
            // do not delete - plan B : manual splitting
            /*var hour = waitMessage.substring(waitMessage.indexOf(BuyerConsts.KEY_SETELAH)+8, waitMessage.indexOf(BuyerConsts.KEY_HOUR))
            var minute = waitMessage.substring(waitMessage.indexOf(BuyerConsts.KEY_HOUR_DIVIDER)+1, waitMessage.indexOf(BuyerConsts.KEY_MINUTE))
            tv_cancel_wait_time?.visible()
            hour = hour.replace(" ", "")
            minute = minute.replace(" ", "")
            if (hour.length == 1) hour = "0$hour"
            if (minute.length == 1) minute = "0$minute"
            tv_cancel_wait_time?.text = hour + " " + BuyerConsts.KEY_HOUR_DIVIDER + " " + minute*/
        } else {
            tv_cancel_wait_desc?.text = waitMessage
        }
        btn_req_cancel_wait?.visible()
    }

    private fun setListenersCancelIsAvailable() {
        tf_choose_reason?.setOnClickListener {
            showReasonBottomSheet()
        }
        tf_choose_reason?.textFieldInput?.setOnClickListener {
            showReasonBottomSheet()
        }
        tf_choose_reason?.textFieldIcon1?.setOnClickListener {
            showReasonBottomSheet()
        }
    }

    private fun setListenerCancelRequestedAlready() {
        btn_chat_penjual?.setOnClickListener {
            goToChatSeller()
        }
    }

    private fun showReasonBottomSheet() {
        reasonBottomSheetAdapter = GetCancelReasonBottomSheetAdapter(this).apply {
            listReason = arrayListOfReason
            currReason = currentReasonStr
            notifyDataSetChanged()
        }
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = reasonBottomSheetAdapter
            }
        }

        bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_CANCEL_REASON_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheet.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showSubReasonBottomSheet() {
        subReasonBottomSheetAdapter = GetCancelSubReasonBottomSheetAdapter(this).apply {
            listSubReason = listOfSubReason
            currReasonCode = reasonCode
            notifyDataSetChanged()
        }
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = subReasonBottomSheetAdapter
            }
        }

        bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_CANCEL_REASON_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheet.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun showProductsBottomSheet() {
        buyerListOfProductsBottomSheetAdapter = BuyerListOfProductsBottomSheetAdapter().apply {
            listProducts = listProduct
            notifyDataSetChanged()
        }

        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null).apply {
            rv_cancel?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = buyerListOfProductsBottomSheetAdapter
            }
        }

        val bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_LIST_OF_PRODUCT_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        fragmentManager?.let { bottomSheet.show(it, getString(R.string.show_bottomsheet)) }
    }

    private fun getCancelReasons() {
        userSession = UserSession(context)
        userSession?.let {
            buyerCancellationViewModel.getCancelReasons(
                    GraphqlHelper.loadRawString(resources, R.raw.get_cancel_reason), it.userId, orderId)
        }
    }

    private fun observingCancelReasons() {
        buyerCancellationViewModel.cancelReasonResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    empty_state_cancellation?.gone()
                    cl_cancellation_content?.visible()
                    cancelReasonResponse = it.data.getCancellationReason
                    renderPage()
                }
                is Fail -> {
                    cl_cancellation_content?.gone()
                    empty_state_cancellation?.visible()
                    empty_state_cancellation?.apply {
                        ContextCompat.getDrawable(context, R.drawable.buyer_cancellation_no_connection)?.let { it1 -> setImageDrawable(it1) }
                        setTitle(getString(R.string.cancellation_no_connection_title))
                        setDescription(getString(R.string.cancellation_no_connection_desc))
                        setPrimaryCTAText(getString(R.string.cancellation_no_connection_btn))
                        setPrimaryCTAClickListener {
                            getCancelReasons()
                        }
                    }
                }
            }
        })
    }

    private fun renderPage() {
        // page title
        if (cancelReasonResponse.isEligibleInstantCancel) {
            (activity as BuyerRequestCancelActivity).supportActionBar?.title = BUTTON_INSTANT_CANCELATION
        } else {
            (activity as BuyerRequestCancelActivity).supportActionBar?.title = BUTTON_REGULER_CANCELATION
        }

        // cancel reasons
        cancelReasonResponse.reasons.forEach { reasonItem ->
            arrayListOfReason.add(reasonItem.title)
        }

        // ticker
        if (cancelReasonResponse.isShowTicker) {
            renderTicker(cancelReasonResponse.tickerInfo)
        } else {
            buyer_ticker_info?.gone()
        }

        // button
        btn_req_cancel?.apply {
            text = when (cancelReasonResponse.isEligibleInstantCancel) {
                true -> {
                    BUTTON_INSTANT_CANCELATION
                }
                false -> {
                    BUTTON_REGULER_CANCELATION
                }
            }
            setOnClickListener { cancelBtnClickListener(cancelReasonResponse.isEligibleInstantCancel) }
        }
    }

    private fun cancelBtnClickListener(isEligibleInstantCancel: Boolean) {
        if (reasonCode == BuyerConsts.REASON_CODE_LAINNYA) {
            when {
                tf_choose_sub_reason_editable.textFieldInput.text.isEmpty() -> {
                    showToaster(getString(R.string.toaster_lainnya_empty), Toaster.TYPE_NORMAL)
                }
                tf_choose_sub_reason_editable.textFieldInput.text.length < 15 -> {
                    showToaster(getString(R.string.toaster_manual_min), Toaster.TYPE_ERROR)
                }
                tf_choose_sub_reason_editable.textFieldInput.text.length > 160 -> {
                    showToaster(getString(R.string.toaster_manual_max), Toaster.TYPE_ERROR)
                }
                else -> {
                    val subReasonLainnya = tf_choose_sub_reason_editable.textFieldInput.text.toString().trimStart()
                    if (subReasonLainnya.isNotEmpty() && !isCancelAlreadyClicked) {
                        reasonCancel = subReasonLainnya
                        isCancelAlreadyClicked = true
                    }
                    if (isEligibleInstantCancel) submitInstantCancel()
                    else {
                        submitRequestCancel()
                    }
                }
            }
        } else {
            if (reasonCode != -1) {
                if (isEligibleInstantCancel) submitInstantCancel()
                else {
                    submitRequestCancel()
                }
            }
        }
    }

    override fun onReasonClicked(reason: String) {
        bottomSheet.dismiss()
        btn_req_cancel?.isEnabled = false
        tf_choose_reason?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        tf_choose_reason?.textFieldInput?.isSingleLine = false
        tf_choose_reason?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        tf_choose_reason?.textFieldInput?.setText(reason)
        currentReasonStr = reason

        if (reason.equals(LAINNYA, true)) {
            reasonCode = BuyerConsts.REASON_CODE_LAINNYA
            tf_choose_sub_reason?.gone()
            tv_sub_reason?.visible()
            tv_sub_reason?.text = getString(R.string.ask_2_lainnya)
            tf_choose_sub_reason_editable?.visible()
            tf_choose_sub_reason_editable?.requestFocus()
            context?.let { showKeyboard(it) }
            tf_choose_sub_reason_editable?.setCounter(160)
            tf_choose_sub_reason_editable?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tf_choose_sub_reason_editable?.textFieldInput?.isSingleLine = false
            tf_choose_sub_reason_editable?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            tf_choose_sub_reason_editable?.textFieldInput?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    //Before user enters the text
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    //On user changes the text
                    btn_req_cancel?.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
                }

                override fun afterTextChanged(s: Editable) {
                    //After user is done entering the text
                    when {
                        s.length < 15 -> {
                            tf_choose_sub_reason_editable?.setError(true)
                            tf_choose_sub_reason_editable?.setMessage(getString(R.string.min_char_reason_lainnya))
                        }
                        s.length > 160 -> {
                            tf_choose_sub_reason_editable?.setError(true)
                            tf_choose_sub_reason_editable?.setMessage(getString(R.string.max_char_reason_lainnya))
                        }
                        else -> {
                            tf_choose_sub_reason_editable?.setError(false)
                            tf_choose_sub_reason_editable?.setMessage("")
                        }
                    }
                }
            })
        } else {
            context?.let { hideKeyboard(it) }
            if (cancelReasonResponse.reasons.isNotEmpty()) {
                tv_sub_reason?.visible()
                tv_sub_reason?.text = getString(R.string.ask_2_placeholder)

                tf_choose_sub_reason?.visible()
                tf_choose_sub_reason_editable?.gone()
                // tf_choose_sub_reason?.setPlaceholder(getString(R.string.reason_placeholder))
                tf_choose_sub_reason?.textFieldIcon1?.setImageResource(R.drawable.ic_chevron_down)
                tf_choose_sub_reason?.textFieldInput?.setText("")

                cancelReasonResponse.reasons.forEach {
                    if (it.title.equals(reason, true))  {
                        listOfSubReason = it.subReasons

                        tf_choose_sub_reason?.textFiedlLabelText?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                        tf_choose_sub_reason?.textFieldInput?.isSingleLine = false
                        tf_choose_sub_reason?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                        tf_choose_sub_reason?.textFieldInput?.isFocusable = false
                        tf_choose_sub_reason?.textFieldInput?.isClickable = true
                        tf_choose_sub_reason?.setOnClickListener {
                            showSubReasonBottomSheet()
                        }
                        tf_choose_sub_reason?.textFieldInput?.setOnClickListener {
                            showSubReasonBottomSheet()
                        }
                        tf_choose_sub_reason?.textFieldIcon1?.setOnClickListener {
                            showSubReasonBottomSheet()
                        }
                    }
                }
            }
        }
    }

    override fun onSubReasonClicked(rCode: Int, reason: String) {
        bottomSheet.dismiss()
        reasonCode = rCode
        tf_choose_sub_reason?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        tf_choose_sub_reason?.textFieldInput?.isSingleLine = false
        tf_choose_sub_reason?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        tf_choose_sub_reason?.textFieldInput?.setText(reason)
        reasonCancel = reason

        tf_choose_reason?.textFieldInput?.text?.let { inputReason ->
            if (inputReason.isNotEmpty()) {
                tf_choose_sub_reason?.textFieldInput?.text?.let { inputSubReason ->
                    if (inputSubReason.isNotEmpty()) {
                        btn_req_cancel?.isEnabled = true
                    }
                }
            }
        }
    }

    private fun submitRequestCancel() {
        userSession?.let {
            buyerCancellationViewModel.requestCancel(
                    GraphqlHelper.loadRawString(resources, R.raw.buyer_request_cancel), it.userId, orderId, "$reasonCode", reasonCancel)
        }
    }
    
    private fun observingRequestCancel() {
        buyerCancellationViewModel.requestCancelResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    buyerRequestCancelResponse = it.data.buyerRequestCancel
                    if (buyerRequestCancelResponse.success == 1 && buyerRequestCancelResponse.message.isNotEmpty()) {
                        backToDetailPage(1, buyerRequestCancelResponse.message.first(), "", "")
                    } else if (buyerRequestCancelResponse.success == 0) {
                        if (buyerRequestCancelResponse.popup.title.isNotEmpty() && buyerRequestCancelResponse.popup.body.isNotEmpty()) {
                            showPopup(buyerRequestCancelResponse.popup)
                        } else if (buyerRequestCancelResponse.message.isNotEmpty()) {
                            showToaster(buyerRequestCancelResponse.message.first(), Toaster.TYPE_ERROR)
                        }
                    }
                }
                is Fail -> {
                    showToaster(getString(R.string.fail_cancellation), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun submitInstantCancel() {
        buyerCancellationViewModel.instantCancellation(
                GraphqlHelper.loadRawString(resources, R.raw.buyer_instant_cancel), orderId, "$reasonCode", reasonCancel)
    }

    private fun observingInstantCancel() {
        buyerCancellationViewModel.buyerInstantCancelResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    instantCancelResponse = it.data.buyerInstantCancel
                    renderInstantCancellation()
                }
                is Fail -> {
                    showToaster(getString(R.string.fail_cancellation), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun renderInstantCancellation() {
        when (instantCancelResponse.success) {
            0 -> {
                showToaster(instantCancelResponse.message, Toaster.TYPE_ERROR)
            }
            1 -> {
                // showToaster(instantCancelResponse.message, Toaster.TYPE_NORMAL)
                backToDetailPage(1, instantCancelResponse.message, "", "")
            }
            2 -> {
                showPopupWithTwoButtons()
            }
            3 -> {
                // showPopupWithSingleButton()
                backToDetailPage(3, instantCancelResponse.message, instantCancelResponse.popup.title, instantCancelResponse.popup.body)
            }
        }
    }

    private fun backToDetailPage(resultCode: Int, resultMsg: String, popupTitle: String, popupBody: String) {
        val intent = Intent()
        intent.putExtra(RESULT_CODE_INSTANT_CANCEL, resultCode)
        intent.putExtra(RESULT_MSG_INSTANT_CANCEL, resultMsg)
        intent.putExtra(RESULT_POPUP_TITLE_INSTANT_CANCEL, popupTitle)
        intent.putExtra(RESULT_POPUP_BODY_INSTANT_CANCEL, popupBody)
        activity?.setResult(MarketPlaceDetailFragment.INSTANT_CANCEL_BUYER_REQUEST, intent)
        activity?.finish()
    }

    private fun showPopupWithTwoButtons() {
        val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.apply {
            setTitle(instantCancelResponse.popup.title)
            setDescription(instantCancelResponse.popup.body)
            setPrimaryCTAText(getString(R.string.button_order_detail_request_cancel))
            setPrimaryCTAClickListener {
                dismiss()
                submitRequestCancel()
            }
            setSecondaryCTAText(getString(R.string.popup_selesai_cancel_btn))
            setSecondaryCTAClickListener {
                dismiss()
                backToDetailPage(0, "", "", "")
            }
        }
        dialog?.show()
    }

    private fun showPopupWithSingleButton() {
        val dialog = context?.let { DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(instantCancelResponse.popup.title)
        dialog?.setDescription(instantCancelResponse.popup.body)
        dialog?.setPrimaryCTAText(getString(R.string.mengerti_button))
        dialog?.setPrimaryCTAClickListener { dialog.dismiss() }
    }

    private fun goToChatSeller() {
        if (shopId != -1) {
            val applink = "tokopedia://topchat/askseller/$shopId"
            val intent = RouteManager.getIntent(context, applink)
            intent.putExtra(ApplinkConst.Chat.INVOICE_ID, listProduct.first().invoiceId)
            intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, invoiceNum)
            intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, listProduct.first().title)
            intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, boughtDate)
            intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, listProduct.first().imageUrl)
            intent.putExtra(ApplinkConst.Chat.INVOICE_URL, invoiceUrl)
            intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, statusId)
            intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, statusInfo)
            intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, listProduct.first().totalPrice)
            intent.putExtra(ApplinkConst.Chat.SOURCE, MarketPlaceDetailFragment.TX_ASK_SELLER)
            startActivity(intent)
        }
    }

    private fun showToaster(msg: String, type: Int) {
        val toaster = Toaster
        view?.let { v ->
            toaster.build(v, msg, Toaster.LENGTH_SHORT, type, BuyerConsts.ACTION_OK).show()
        }
    }

    private fun showPopup(dataPopup: BuyerRequestCancelData.Data.BuyerRequestCancel.Popup) {
        val dialog = context?.let { DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION) }
        dialog?.setTitle(dataPopup.title)
        dialog?.setDescription(dataPopup.body)
        dialog?.setImageDrawable(R.drawable.ic_terkirim)
        dialog?.setPrimaryCTAText(getString(R.string.mengerti_button))
        dialog?.setPrimaryCTAClickListener {
                dialog.dismiss()
                activity?.setResult(MarketPlaceDetailFragment.CANCEL_ORDER_DISABLE)
                activity?.finish()
        }
        dialog?.show()
    }

    private fun renderTicker(tickerInfo: TickerInfo) {
        buyer_ticker_info?.apply {
            visible()
            tickerType = BuyerUtils.getTickerType(tickerInfo.type)
            tickerShape = Ticker.SHAPE_LOOSE
            setHtmlDescription(tickerInfo.text + " ${getString(R.string.buyer_ticker_info_selengkapnya)
                    .replace(TICKER_URL, tickerInfo.actionUrl)
                    .replace(TICKER_LABEL, tickerInfo.actionText)}")
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                }

                override fun onDismiss() {
                }

            })
        }
    }

    private fun hideKeyboard(context: Context) {
        try {
            (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            if (context.currentFocus != null && context.currentFocus?.windowToken != null) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showKeyboard(context: Context) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}
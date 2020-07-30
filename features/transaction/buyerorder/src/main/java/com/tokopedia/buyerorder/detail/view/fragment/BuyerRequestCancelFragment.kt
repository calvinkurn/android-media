package com.tokopedia.buyerorder.detail.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.di.OrderDetailsComponent
import com.tokopedia.buyerorder.detail.view.adapter.BuyerListOfProductsBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.GetCancelReasonBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.adapter.GetCancelSubReasonBottomSheetAdapter
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerGetCancellationReasonViewModel
import com.tokopedia.buyerorder.list.common.OrderListContants
import com.tokopedia.buyerorder.list.data.ticker.Input
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
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
    private var bottomSheet = BottomSheetUnify()
    private var reasonCancel = ""
    private var reasonCode = -1
    private var arrayListOfReason = arrayListOf<String>()
    private var listOfSubReason = listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.ReasonsItem.SubReasonsItem>()
    private var currentReasonStr = ""

    private val buyerGetCancellationReasonViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[BuyerGetCancellationReasonViewModel::class.java]
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observingCancelReasons()
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

        tf_choose_reason?.visible()
        tf_choose_sub_reason?.visible()
        btn_req_cancel?.visible()

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

        btn_req_cancel?.setOnClickListener {
            if (reasonCancel.isNotEmpty()) {
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
                            val subReasonLainnya = tf_choose_sub_reason_editable.textFieldInput.text
                            if (subReasonLainnya.isNotEmpty()) {
                                reasonCancel += " - $subReasonLainnya"
                            }
                            submitResultReason()
                        }
                    }
                } else {
                    if (reasonCode != -1) {
                        submitResultReason()
                    }
                }
            }
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
        val userSession = UserSession(context)
        buyerGetCancellationReasonViewModel.getCancelReasons(
                GraphqlHelper.loadRawString(resources, R.raw.get_cancel_reason), userSession.userId, orderId)
    }

    private fun observingCancelReasons() {
        buyerGetCancellationReasonViewModel.cancelReasonResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    cancelReasonResponse = it.data.getCancellationReason
                    cancelReasonResponse.reasons.forEach { reasonItem ->
                        arrayListOfReason.add(reasonItem.title)
                    }
                }
                is Fail -> {
                    val toasterFail = Toaster
                    view?.let { v ->
                        toasterFail.make(v, getString(R.string.fail_cancellation), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, BuyerConsts.ACTION_OK)
                    }
                }
            }
        })
    }

    override fun onReasonClicked(reason: String) {
        bottomSheet.dismiss()
        btn_req_cancel?.isEnabled = false
        tf_choose_reason?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        tf_choose_reason?.textFieldInput?.setSingleLine(false)
        tf_choose_reason?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        tf_choose_reason?.textFieldInput?.setText(reason)
        reasonCancel = reason
        currentReasonStr = reason

        if (cancelReasonResponse.reasons.isNotEmpty()) {
            tf_choose_sub_reason_editable?.gone()
            tf_choose_sub_reason?.visible()
            tf_choose_sub_reason?.setPlaceholder(getString(R.string.reason_placeholder))
            tf_choose_sub_reason?.textFieldIcon1?.setImageResource(R.drawable.ic_chevron_down)
            tf_choose_sub_reason?.textFieldInput?.setText("")
            tf_choose_sub_reason?.textFiedlLabelText?.setText(R.string.ask_2_placeholder)

            cancelReasonResponse.reasons.forEach {
                if (it.title.equals(reason, true))  {
                    listOfSubReason = it.subReasons

                    tf_choose_sub_reason?.textFiedlLabelText?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    tf_choose_sub_reason?.textFieldInput?.setSingleLine(false)
                    tf_choose_sub_reason?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                    tf_choose_sub_reason?.textFiedlLabelText?.text = it.question
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

    override fun onSubReasonClicked(rCode: Int, reason: String) {
        bottomSheet.dismiss()
        reasonCode = rCode
        if (rCode == BuyerConsts.REASON_CODE_LAINNYA) {
            tf_choose_sub_reason?.gone()
            tf_choose_sub_reason_editable?.visible()
            tf_choose_sub_reason_editable?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tf_choose_sub_reason_editable?.textFieldInput?.setSingleLine(false)
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
                }
            })
        } else {
            tf_choose_sub_reason?.textFieldInput?.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tf_choose_sub_reason?.textFieldInput?.setSingleLine(false)
            tf_choose_sub_reason?.textFieldInput?.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            tf_choose_sub_reason?.textFieldInput?.setText(reason)
            reasonCancel += " - $reason"

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
    }

    private fun submitResultReason() {
        val intent = Intent()
        intent.putExtra(OrderListContants.REASON, reasonCancel)
        intent.putExtra(OrderListContants.REASON_CODE, reasonCode)
        intent.putExtra(MarketPlaceDetailFragment.ACTION_BUTTON_URL, uri)
        activity?.setResult(MarketPlaceDetailFragment.CANCEL_BUYER_REQUEST, intent)
        activity?.finish()
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
            toaster.make(v, msg, Toaster.LENGTH_SHORT, type, BuyerConsts.ACTION_OK)
        }
    }
}
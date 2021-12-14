package com.tokopedia.ordermanagement.buyercancellationorder.presentation.fragment

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.ordermanagement.buyercancellationorder.R
import com.tokopedia.ordermanagement.buyercancellationorder.analytics.BuyerAnalytics
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.BUTTON_INSTANT_CANCELATION
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.BUTTON_REGULER_CANCELATION
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.BUYER_CANCEL_REASON_SCREEN_NAME
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.LAINNYA
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.RESULT_CODE_BACK
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.RESULT_CODE_INSTANT_CANCEL
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.RESULT_CODE_SUCCESS
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.RESULT_MSG_INSTANT_CANCEL
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.TICKER_LABEL
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerConsts.TICKER_URL
import com.tokopedia.ordermanagement.buyercancellationorder.common.constants.BuyerOrderIntentCode
import com.tokopedia.ordermanagement.buyercancellationorder.common.utils.BuyerUtils
import com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.ordermanagement.buyercancellationorder.databinding.FragmentBuyerRequestCancelBinding
import com.tokopedia.ordermanagement.buyercancellationorder.di.component.BuyerCancellationOrderComponent
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.activity.BuyerRequestCancelActivity
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.BuyerListOfProductsBottomSheetAdapter
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.BuyerProductBundlingBottomSheetAdapter
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.GetCancelReasonBottomSheetAdapter
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.GetCancelSubReasonBottomSheetAdapter
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.divider.BuyerBundlingProductItemDivider
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.typefactory.BuyerProductBundlingAdapterFactory
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerNormalProductUiModel
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.viewmodel.BuyerCancellationViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
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
    private var isFromUoh : Boolean = false
    private var helplinkUrl: String = ""
    private var listProduct = listOf<BuyerGetCancellationReasonData.Data.GetCancellationReason.OrderDetailsCancellation>()
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
    private var isBundlingProduct: Boolean = false

    private var binding by autoClearedNullable<FragmentBuyerRequestCancelBinding>()

    private val buyerCancellationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BuyerCancellationViewModel::class.java]
    }

    private val buyerProductBundlingAdapterFactory by lazy {
        BuyerProductBundlingAdapterFactory()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BuyerRequestCancelFragment {
            return BuyerRequestCancelFragment().apply {
                arguments = Bundle().apply {
                    putString(BuyerConsts.PARAM_SHOP_NAME, bundle.getString(BuyerConsts.PARAM_SHOP_NAME))
                    putString(BuyerConsts.PARAM_INVOICE, bundle.getString(BuyerConsts.PARAM_INVOICE))
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
                    putBoolean(BuyerConsts.PARAM_SOURCE_UOH, bundle.getBoolean(BuyerConsts.PARAM_SOURCE_UOH))
                    putString(BuyerConsts.PARAM_HELP_LINK_URL, bundle.getString(BuyerConsts.PARAM_HELP_LINK_URL))
                }
            }
        }

        const val SUCCESS_CODE_0 = 0
        const val SUCCESS_CODE_1 = 1
        const val SUCCESS_CODE_2 = 2
        const val SUCCESS_CODE_3 = 3
        const val COUNTER_160 = 160
        const val COUNTER_15 = 15
        const val COUNTER_7 = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            shopName = arguments?.getString(BuyerConsts.PARAM_SHOP_NAME).toString()
            invoiceNum = arguments?.getString(BuyerConsts.PARAM_INVOICE).toString()
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
            isFromUoh = arguments?.getBoolean(BuyerConsts.PARAM_SOURCE_UOH) ?: false
            helplinkUrl = arguments?.getString(BuyerConsts.PARAM_HELP_LINK_URL).toString()
        }
        getCancelReasons()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBuyerRequestCancelBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(BuyerCancellationOrderComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        activity?.let { BuyerAnalytics.sendScreenName(BUYER_CANCEL_REASON_SCREEN_NAME) }
        observingCancelReasons()
        observingInstantCancel()
        observingRequestCancel()
        observeBuyerRequestCancelReasonValidationResult()
        observeBuyerNormalProducts()
        setupViews()

        reasonBottomSheetAdapter = GetCancelReasonBottomSheetAdapter(this)

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

    private fun setupViews() {
        binding?.run {
            btnReqCancel.isEnabled = false
            tfChooseSubReason.textFieldInput.isFocusable = false
            tfChooseSubReason.textFieldInput.isClickable = false
            tfChooseSubReason.setOnClickListener {}
            labelShopName.text = MethodChecker.fromHtml(shopName)
            labelInvoice.text = invoiceNum
        }
    }

    private fun setupHeader() {
        view?.findViewById<HeaderUnify>(R.id.header_buyer_request_cancel)?.let { header ->
            activity.run {
                if (this is AppCompatActivity) {
                    supportActionBar?.hide()
                    setSupportActionBar(header)
                } else {
                    header.gone()
                }
            }
        }
    }

    private fun setLayoutCancelAlreadyRequested() {
        binding?.run {
            tvCancelWaitDesc.gone()
            tvCancelWaitTime.gone()
            btnReqCancelWait.gone()

            tfChooseReason.gone()
            tfChooseSubReason.gone()
            btnReqCancel.gone()

            ivCheck.visible()
            tvTitleCancelRequested.visible()
            tvBodyCancelRequested.visible()
            btnChatPenjual.visible()

            tvTitleCancelRequested.text = cancelRequestedTitle
            tvBodyCancelRequested.text = cancelRequestedBody
        }
        setListenerCancelRequestedAlready()
    }

    private fun setLayoutCancelIsAvailable() {
        binding?.run {
            tvCancelWaitDesc.gone()
            tvCancelWaitTime.gone()
            btnReqCancelWait.gone()

            ivCheck.gone()
            tvTitleCancelRequested.gone()
            tvBodyCancelRequested.gone()
            btnChatPenjual.gone()

            tfChooseSubReason.gone()
            btnReqCancel.visible()

            tfChooseReason.visible()
            tfChooseReason.textFieldInput.isFocusable = false
            tfChooseReason.textFieldInput.isClickable = true

        }
        setListenersCancelIsAvailable()
    }

    @SuppressLint("SetTextI18n")
    private fun setLayoutWaitToCancel() {
        binding?.run {
            ivCheck.gone()
            tvTitleCancelRequested.gone()
            tvBodyCancelRequested.gone()
            btnChatPenjual.gone()

            tfChooseReason.gone()
            tfChooseSubReason.gone()
            btnReqCancel.gone()

            tvCancelWaitDesc.visible()
            if (waitMessage.contains(BuyerConsts.KEY_SETELAH)) {
                tvCancelWaitDesc.text = waitMessage.substring(
                    0,
                    waitMessage.indexOf(BuyerConsts.KEY_SETELAH) + COUNTER_7
                ) + BuyerConsts.KEY_HOUR_DIVIDER

                if (waitMessage.contains(BuyerConsts.KEY_LAGI)) {
                    tvCancelWaitTime.visible()
                    tvCancelWaitTime.text = waitMessage.substring(
                        waitMessage.indexOf(BuyerConsts.KEY_SETELAH) + COUNTER_7,
                        waitMessage.indexOf(BuyerConsts.KEY_LAGI)
                    )
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
                tvCancelWaitDesc.text = waitMessage
            }
            btnReqCancelWait.visible()
        }
    }

    private fun setListenersCancelIsAvailable() {
        binding?.run {
            tfChooseReason.setOnClickListener {
                showReasonBottomSheet()
            }
            tfChooseReason.textFieldInput.setOnClickListener {
                showReasonBottomSheet()
            }
            tfChooseReason.textFieldIcon1.setOnClickListener {
                showReasonBottomSheet()
            }
        }
    }

    private fun setListenerCancelRequestedAlready() {
        binding?.btnChatPenjual?.setOnClickListener {
            goToChatSeller()
        }
    }

    private fun showReasonBottomSheet() {
        reasonBottomSheetAdapter = GetCancelReasonBottomSheetAdapter(this).apply {
            listReason = arrayListOfReason
            currReason = currentReasonStr
            notifyDataSetChanged()
        }
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null)
        val rvCancel = viewBottomSheet.findViewById<RecyclerView>(R.id.rv_cancel)
        rvCancel?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = reasonBottomSheetAdapter
        }

        bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_CANCEL_REASON_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        bottomSheet.show(childFragmentManager, getString(R.string.show_bottomsheet))
    }

    private fun showSubReasonBottomSheet() {
        subReasonBottomSheetAdapter = GetCancelSubReasonBottomSheetAdapter(this).apply {
            listSubReason = listOfSubReason
            currReasonCode = reasonCode
            notifyDataSetChanged()
        }
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null)

        val rvCancel = viewBottomSheet.findViewById<RecyclerView>(R.id.rv_cancel)
        rvCancel?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = subReasonBottomSheetAdapter
        }

        bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_CANCEL_REASON_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        bottomSheet.show(childFragmentManager, getString(R.string.show_bottomsheet))
    }

    private fun showProductsBottomSheet() {
        buyerListOfProductsBottomSheetAdapter = BuyerListOfProductsBottomSheetAdapter().apply {
            listProducts = this@BuyerRequestCancelFragment.listProduct
            notifyDataSetChanged()
        }

        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null)
        val rvCancel = viewBottomSheet.findViewById<RecyclerView>(R.id.rv_cancel)
        rvCancel?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = buyerListOfProductsBottomSheetAdapter
        }

        val bottomSheet = BottomSheetUnify().apply {
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_LIST_OF_PRODUCT_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        bottomSheet.show(childFragmentManager, getString(R.string.show_bottomsheet))
    }

    private fun showProductBundleBottomSheet(normalProductList: List<BuyerNormalProductUiModel>) {
        val buyerProductBundlingAdapter = BuyerProductBundlingBottomSheetAdapter(
                normalProductItems = normalProductList,
                adapterTypeFactory = buyerProductBundlingAdapterFactory
        )
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_buyer_request_cancel, null)

        val rvCancel = viewBottomSheet.findViewById<RecyclerView>(R.id.rv_cancel)
        rvCancel?.run {
            context?.let {
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                adapter = buyerProductBundlingAdapter
                addItemDecoration(BuyerBundlingProductItemDivider(it))
            }
        }

        val bottomSheet = BottomSheetUnify().apply {
            clearContentPadding = true
            setChild(viewBottomSheet)
            setTitle(BuyerConsts.TITLE_LIST_OF_PRODUCT_BOTTOMSHEET)
            showCloseIcon = true
            setCloseClickListener { dismiss() }
        }

        bottomSheet.show(childFragmentManager, getString(R.string.show_bottomsheet))
    }

    private fun getCancelReasons() {
        userSession = UserSession(context)
        userSession?.let {
            buyerCancellationViewModel.getCancelReasons(it.userId, orderId)
        }
    }

    private fun observingCancelReasons() {
        buyerCancellationViewModel.cancelReasonResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    binding?.run {
                        emptyStateCancellation.gone()
                        clCancellationContent.visible()
                    }
                    cancelReasonResponse = it.data.getCancellationReason
                    listProduct = it.data.getCancellationReason.orderDetails
                    renderPage()
                }
                is Fail -> {
                    binding?.run {
                        clCancellationContent.gone()
                        emptyStateCancellation.visible()
                        emptyStateCancellation.apply {
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.buyer_cancellation_no_connection
                            )?.let { it1 -> setImageDrawable(it1) }
                            setTitle(getString(R.string.cancellation_no_connection_title))
                            setDescription(getString(R.string.cancellation_no_connection_desc))
                            setPrimaryCTAText(getString(R.string.cancellation_no_connection_btn))
                            setPrimaryCTAClickListener {
                                getCancelReasons()
                            }
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun renderPage() {
        // page title
        if (cancelReasonResponse.isEligibleInstantCancel) {
            (activity as? BuyerRequestCancelActivity)?.supportActionBar?.title = BUTTON_INSTANT_CANCELATION
        } else {
            (activity as? BuyerRequestCancelActivity)?.supportActionBar?.title = BUTTON_REGULER_CANCELATION
        }

        binding?.run {
            // list product
            if (listProduct.isNotEmpty() && !isBundlingProduct) {
                labelProductName.text = MethodChecker.fromHtml(listProduct.firstOrNull()?.productName.orEmpty())
                labelPrice.text = listProduct.firstOrNull()?.productPrice.orEmpty()
                ivProduct.loadImage(listProduct.firstOrNull()?.picture.orEmpty())

                if (listProduct.size > 1) {
                    labelSeeAllProducts.visible()
                    labelSeeAllProducts.text =
                        "${getString(R.string.see_all_placeholder)} (${listProduct.size})"
                    labelSeeAllProducts.setOnClickListener { showProductsBottomSheet() }
                } else {
                    labelSeeAllProducts.gone()
                }
            }

            // cancel reasons
            cancelReasonResponse.reasons.forEach { reasonItem ->
                arrayListOfReason.add(reasonItem.title)
            }

            // ticker
            if (cancelReasonResponse.isShowTicker) {
                renderTicker(cancelReasonResponse.tickerInfo)
            } else {
                buyerTickerInfo.gone()
            }

            // button
            btnReqCancel.apply {
                text = when (cancelReasonResponse.isEligibleInstantCancel) {
                    true -> {
                        BUTTON_INSTANT_CANCELATION
                    }
                    false -> {
                        BUTTON_REGULER_CANCELATION
                    }
                }
                this.setOnClickListener { cancelBtnClickListener(cancelReasonResponse.isEligibleInstantCancel) }
            }
        }
    }

    private fun cancelBtnClickListener(isEligibleInstantCancel: Boolean) {
        binding?.run {
            if (reasonCode == BuyerConsts.REASON_CODE_LAINNYA) {
                when {
                    tfChooseSubReasonEditable.textFieldInput.text.isEmpty() -> {
                        showToaster(getString(R.string.toaster_lainnya_empty), Toaster.TYPE_NORMAL)
                    }
                    tfChooseSubReasonEditable.textFieldInput.text.length < COUNTER_15 -> {
                        showToaster(getString(R.string.toaster_manual_min), Toaster.TYPE_ERROR)
                    }
                    tfChooseSubReasonEditable.textFieldInput.text.length > COUNTER_160 -> {
                        showToaster(getString(R.string.toaster_manual_max), Toaster.TYPE_ERROR)
                    }
                    else -> {
                        val subReasonLainnya =
                            tfChooseSubReasonEditable.textFieldInput.text.toString().trimStart()
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
    }

    override fun onReasonClicked(reason: String) {
        bottomSheet.dismiss()
        binding?.run {
            btnReqCancel.isEnabled = false
            tfChooseReason.textFieldInput.inputType =
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tfChooseReason.textFieldInput.isSingleLine = false
            tfChooseReason.textFieldInput.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            tfChooseReason.textFieldInput.setText(reason)
            currentReasonStr = reason

            if (reason.equals(LAINNYA, true)) {
                reasonCode = BuyerConsts.REASON_CODE_LAINNYA
                tfChooseSubReason.gone()
                tvSubReason.visible()
                tvSubReason.text = getString(R.string.ask_2_lainnya)
                tfChooseSubReasonEditable.visible()
                tfChooseSubReasonEditable.requestFocus()
                context?.let { showKeyboard(it) }
                tfChooseSubReasonEditable.setCounter(COUNTER_160)
                tfChooseSubReasonEditable.textFieldInput.inputType =
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                tfChooseSubReasonEditable.textFieldInput.isSingleLine = false
                tfChooseSubReasonEditable.textFieldInput.imeOptions =
                    EditorInfo.IME_FLAG_NO_ENTER_ACTION
                tfChooseSubReasonEditable.textFieldInput.addTextChangedListener(object :
                    TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        buyerCancellationViewModel.validateBuyerRequestCancelReason(s.toString())
                    }
                })
            } else {
                context?.let { hideKeyboard(it) }
                if (cancelReasonResponse.reasons.isNotEmpty()) {
                    tvSubReason.visible()
                    tvSubReason.text = getString(R.string.ask_2_placeholder)

                    tfChooseSubReason.visible()
                    tfChooseSubReasonEditable.gone()
                    tfChooseSubReason.textFieldIcon1.setImageResource(R.drawable.ic_chevron_down)
                    tfChooseSubReason.textFieldInput.setText("")

                    cancelReasonResponse.reasons.forEach {
                        if (it.title.equals(reason, true)) {
                            listOfSubReason = it.subReasons

                            tfChooseSubReason.textFiedlLabelText.inputType =
                                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            tfChooseSubReason.textFieldInput.isSingleLine = false
                            tfChooseSubReason.textFieldInput.imeOptions =
                                EditorInfo.IME_FLAG_NO_ENTER_ACTION
                            tfChooseSubReason.textFieldInput.isFocusable = false
                            tfChooseSubReason.textFieldInput.isClickable = true
                            tfChooseSubReason.setOnClickListener {
                                showSubReasonBottomSheet()
                            }
                            tfChooseSubReason.textFieldInput.setOnClickListener {
                                showSubReasonBottomSheet()
                            }
                            tfChooseSubReason.textFieldIcon1.setOnClickListener {
                                showSubReasonBottomSheet()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSubReasonClicked(rCode: Int, reason: String) {
        bottomSheet.dismiss()
        reasonCode = rCode
        binding?.run {
            tfChooseSubReason.textFieldInput.inputType =
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            tfChooseSubReason.textFieldInput.isSingleLine = false
            tfChooseSubReason.textFieldInput.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            tfChooseSubReason.textFieldInput.setText(reason)
            reasonCancel = reason

            tfChooseReason.textFieldInput.text?.let { inputReason ->
                if (inputReason.isNotEmpty()) {
                    tfChooseSubReason.textFieldInput.text?.let { inputSubReason ->
                        if (inputSubReason.isNotEmpty()) {
                            btnReqCancel.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun submitRequestCancel() {
        userSession?.let {
            buyerCancellationViewModel.requestCancel(it.userId, orderId, "$reasonCode", reasonCancel)
        }
    }

    private fun observingRequestCancel() {
        buyerCancellationViewModel.requestCancelResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    buyerRequestCancelResponse = it.data.buyerRequestCancel
                    if (buyerRequestCancelResponse.success == RESULT_CODE_SUCCESS && buyerRequestCancelResponse.message.isNotEmpty()) {
                        backToEntryPoint(RESULT_CODE_SUCCESS, buyerRequestCancelResponse.message.first())
                    } else if (buyerRequestCancelResponse.success == 0) {
                        if (buyerRequestCancelResponse.popup.title.isNotEmpty() && buyerRequestCancelResponse.popup.body.isNotEmpty()) {
                            showPopup(buyerRequestCancelResponse.popup)
                        } else if (buyerRequestCancelResponse.message.isNotEmpty()) {
                            showToaster(buyerRequestCancelResponse.message.first(), Toaster.TYPE_ERROR)
                        }
                    }
                }
                is Fail -> {
                    showToaster(getString(R.string.buyer_fail_cancellation), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun observeBuyerRequestCancelReasonValidationResult() {
        buyerCancellationViewModel.buyerRequestCancelReasonValidationResult.observe(viewLifecycleOwner, {
            binding?.run {
                tfChooseSubReasonEditable.setMessage(it.inputFieldMessage)
                tfChooseSubReasonEditable.setError(it.isError)
                btnReqCancel.isEnabled = it.isButtonEnable
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun observeBuyerNormalProducts() {
        buyerCancellationViewModel.buyerNormalProductUiModelListLiveData.observe(viewLifecycleOwner) { normalProductList ->
            isBundlingProduct = normalProductList != null
            normalProductList?.let { products ->
                binding?.run {
                    if (products.isNotEmpty()) {
                        labelProductName.text =
                            MethodChecker.fromHtml(products.firstOrNull()?.productName.orEmpty())
                        labelPrice.text = products.firstOrNull()?.productPrice.orEmpty()
                        ivProduct.loadImage(products.firstOrNull()?.productThumbnailUrl.orEmpty())
                    }
                    labelSeeAllProducts.run {
                        val totalItems = products.count().orZero()
                        if (totalItems > 1) {
                            text = "${getString(R.string.see_all_placeholder)} ($totalItems)"
                            setOnClickListener {
                                showProductBundleBottomSheet(products)
                            }
                            show()
                        } else {
                            gone()
                        }
                    }
                }
            }
        }
    }

    private fun submitInstantCancel() {
        buyerCancellationViewModel.instantCancellation(orderId, "$reasonCode", reasonCancel)
    }

    private fun observingInstantCancel() {
        buyerCancellationViewModel.buyerInstantCancelResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    instantCancelResponse = it.data.buyerInstantCancel
                    renderInstantCancellation()
                }
                is Fail -> {
                    showToaster(getString(R.string.buyer_fail_cancellation), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun renderInstantCancellation() {
        when (instantCancelResponse.success) {
            SUCCESS_CODE_0 -> {
                showToaster(instantCancelResponse.message, Toaster.TYPE_ERROR)
            }
            SUCCESS_CODE_1 -> {
                // showToaster(instantCancelResponse.message, Toaster.TYPE_NORMAL)
                backToEntryPoint(RESULT_CODE_SUCCESS, instantCancelResponse.message)
            }
            SUCCESS_CODE_2 -> {
                showPopupWithTwoButtons()
            }
            SUCCESS_CODE_3 -> {
                showPopupWithHelpButton(instantCancelResponse.popup.title, instantCancelResponse.popup.body)
            }
        }
    }

    private fun backToEntryPoint(resultCode: Int, resultMsg: String) {
        val intent = Intent()
        intent.putExtra(RESULT_CODE_INSTANT_CANCEL, resultCode)
        intent.putExtra(RESULT_MSG_INSTANT_CANCEL, resultMsg)
        activity?.setResult(BuyerOrderIntentCode.RESULT_CODE_INSTANT_CANCEL_BUYER, intent)
        activity?.finish()
    }

    private fun showPopupWithHelpButton(popupTitle: String, popupBody: String) {
        val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.apply {
            setTitle(popupTitle)
            setDescription(popupBody)
            setPrimaryCTAText(getString(R.string.mengerti_button))
            setPrimaryCTAClickListener { dismiss() }
            setSecondaryCTAText(getString(R.string.pusat_bantuan_button))
            setSecondaryCTAClickListener {
                dismiss()
                if (helplinkUrl.isNotEmpty()) {
                    RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, helplinkUrl)
                }
            }
        }
        dialog?.show()
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
                backToEntryPoint(RESULT_CODE_BACK, "")
            }
        }
        dialog?.show()
    }

    private fun goToChatSeller() {
        if (shopId != -1) {
            val applink = "tokopedia://topchat/askseller/$shopId"
            val intent = RouteManager.getIntent(context, applink)
            intent.putExtra(ApplinkConst.Chat.INVOICE_ID, orderId)
            intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, invoiceNum)
            intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, listProduct.first().productName)
            intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, boughtDate)
            intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, listProduct.first().picture)
            intent.putExtra(ApplinkConst.Chat.INVOICE_URL, invoiceUrl)
            intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, statusId)
            intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, statusInfo)
            intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, listProduct.first().productPrice)
            intent.putExtra(ApplinkConst.Chat.SOURCE, ApplinkConst.Chat.SOURCE_ASK_SELLER)
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
                activity?.setResult(BuyerOrderIntentCode.RESULT_CODE_CANCEL_ORDER_DISABLE)
                activity?.finish()
        }
        dialog?.show()
    }

    private fun renderTicker(tickerInfo: BuyerGetCancellationReasonData.Data.GetCancellationReason.TickerInfo) {
        binding?.buyerTickerInfo?.apply {
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

    /**
     * Filter product ui model to have unique id + price combination.
     * This means we can show the same product, only if the price are different
     *
     * @return  filtered list
     */
    private fun List<BuyerNormalProductUiModel>.filterSameIdAndPrice(): List<BuyerNormalProductUiModel> {
        return distinctBy { it.productId to it.productPrice }
    }

}
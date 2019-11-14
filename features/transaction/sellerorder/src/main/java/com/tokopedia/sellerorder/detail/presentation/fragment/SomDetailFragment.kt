package com.tokopedia.sellerorder.detail.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.convertMonth
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.convertStrObjToHashMap
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.ACTION_OK
import com.tokopedia.sellerorder.common.util.SomConsts.BOTTOMSHEET_TEXT_RADIO_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.BOTTOMSHEET_TEXT_RADIO_WITH_REASON_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_HEADER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PAYMENT_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PRODUCTS_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_SHIPPING_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_COURIER_PROBLEM_OFFICE_CLOSED
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_COURIER_PROBLEM_UNMATCHED_COST
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REASON_BUYER_NO_RESPONSE
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REASON_COURIER_PROBLEM
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REASON_EMPTY_STOCK
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REASON_OTHER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SHOP_ID
import com.tokopedia.sellerorder.common.util.SomConsts.QUERY_INVOICE_URL_UPLOAD_AWB
import com.tokopedia.sellerorder.common.util.SomConsts.RECEIVER_NOTES_COLON
import com.tokopedia.sellerorder.common.util.SomConsts.RECEIVER_NOTES_END
import com.tokopedia.sellerorder.common.util.SomConsts.RECEIVER_NOTES_START
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_REJECT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_ATUR_TOKO_TUTUP
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_COURIER_PROBLEM
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_PILIH_PENOLAKAN
import com.tokopedia.sellerorder.common.util.SomConsts.TITLE_PILIH_PRODUK_KOSONG
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_COURIER_PROBLEM_OFFICE_CLOSED
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_COURIER_PROBLEM_OTHERS
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_REASON_BUYER_NO_RESPONSE
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_REASON_COURIER_PROBLEM
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_REASON_OTHER
import com.tokopedia.sellerorder.common.util.SomConsts.VALUE_REASON_SHOP_CLOSED
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetCourierProblemsAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectReasonsAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectOrderAdapter
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetStockEmptyAdapter
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_secondary.*
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*
import kotlinx.android.synthetic.main.bottomsheet_shop_closed.view.*
import kotlinx.android.synthetic.main.fragment_som_detail.*
import kotlinx.android.synthetic.main.fragment_som_detail.btn_primary
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.net.Uri
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_TRACK_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.presentation.activity.SomConfirmReqPickupActivity


/**x
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailFragment : BaseDaggerFragment(), SomBottomSheetRejectOrderAdapter.ActionListener, SomDetailAdapter.ActionListener, SomBottomSheetRejectReasonsAdapter.ActionListener, SomBottomSheetCourierProblemsAdapter.ActionListener  {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var orderId = ""
    private var detailResponse = SomDetailOrder.Data.GetSomDetail()
    private var acceptOrderResponse = SomAcceptOrder.Data.AcceptOrder()
    private var rejectOrderResponse = SomRejectOrder.Data.RejectOrder()
    private var rejectReasonResponse = listOf<SomReasonRejectData.Data.SomRejectReason>()
    private var listDetailData: ArrayList<SomDetailData> = arrayListOf()
    private var listRejectTypeData: ArrayList<SomRejectTypeData> = arrayListOf()
    private lateinit var somDetailAdapter: SomDetailAdapter
    private lateinit var somBottomSheetRejectOrderAdapter:  SomBottomSheetRejectOrderAdapter
    private lateinit var somBottomSheetRejectReasonsAdapter:  SomBottomSheetRejectReasonsAdapter
    private lateinit var somBottomSheetStockEmptyAdapter: SomBottomSheetStockEmptyAdapter
    private lateinit var somBottomSheetCourierProblemsAdapter: SomBottomSheetCourierProblemsAdapter
    private lateinit var dialogUnify: DialogUnify
    private lateinit var bottomSheetUnify: BottomSheetUnify
    private val FLAG_CONFIRM_REQ_PICKUP = 3535

    private val somDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomDetailViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomDetailFragment {
            return SomDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, bundle.getString(PARAM_ORDER_ID))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            orderId = arguments?.getString(PARAM_ORDER_ID).toString()
        }
        loadDetail()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingDetail()
        observingAcceptOrder()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.chat_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.som_action_chat -> {
                onChatClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onChatClicked() {
        RouteManager.route(activity, ApplinkConst.TOPCHAT_IDLESS)
    }

    private fun prepareLayout() {
        somDetailAdapter = SomDetailAdapter().apply {
            setActionListener(this@SomDetailFragment)
        }
        // somDetailAdapter.setActionListener(this)
        rv_detail?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = somDetailAdapter
        }
    }

    private fun loadDetail() {
        somDetailViewModel.loadDetailOrder(
                GraphqlHelper.loadRawString(resources, R.raw.gql_som_detail), orderId)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomDetailComponent::class.java).inject(this)
    }

    private fun observingDetail() {
        somDetailViewModel.orderDetailResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    detailResponse = it.data
                    renderDetail()
                }
                is Fail -> {
                    // quick_filter?.visibility = View.GONE
                }
            }
        })
    }

    private fun observingAcceptOrder() {
        somDetailViewModel.acceptOrderResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    acceptOrderResponse = it.data.acceptOrder
                    if (acceptOrderResponse.success == 1) {
                        // if success = 1 : finishActivity, then show toaster
                        activity?.setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(RESULT_ACCEPT_ORDER, acceptOrderResponse)
                        })
                        activity?.finish()

                    } else {
                        showToasterError(acceptOrderResponse.listMessage.first())
                    }
                }
                is Fail -> {
                    dialogUnify.dismiss()
                }
            }
        })
    }

    private fun observingRejectReasons() {
        somDetailViewModel.rejectReasonResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    rejectReasonResponse = it.data.listSomRejectReason
                    somBottomSheetRejectReasonsAdapter = SomBottomSheetRejectReasonsAdapter(this)
                    bottomSheetUnify.dismiss()
                    bottomSheetUnify = BottomSheetUnify()
                    // if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
                    val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null)
                    viewBottomSheet.rv_bottomsheet_secondary?.apply {
                        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                        adapter = somBottomSheetRejectReasonsAdapter
                    }
                    viewBottomSheet.tf_extra_notes?.visibility = View.GONE

                    bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
                    bottomSheetUnify.setChild(viewBottomSheet)
                    bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
                    bottomSheetUnify.setTitle(TITLE_PILIH_PENOLAKAN)

                    somBottomSheetRejectReasonsAdapter.listRejectReasons = rejectReasonResponse.toMutableList()
                    somBottomSheetRejectReasonsAdapter.notifyDataSetChanged()
                }
                is Fail -> {
                    bottomSheetUnify.dismiss()
                    showToasterError(getString(R.string.global_error))
                }
            }
        })
    }

    private fun renderDetail() {
        // header
        val dataHeader = SomDetailHeader(
                detailResponse.statusText,
                detailResponse.invoice,
                detailResponse.invoiceUrl,
                detailResponse.paymentDate,
                detailResponse.customer.name,
                detailResponse.deadline.text,
                detailResponse.deadline.color,
                detailResponse.listLabelInfo,
                detailResponse.orderId.toString(),
                detailResponse.shipment.awb,
                detailResponse.shipment.awbUploadProofText)

        // products
        val dataProducts = SomDetailProducts(detailResponse.listProduct)

        // shipping
        val receiverStreet = detailResponse.receiver.street
        var notesValue = ""
        if (receiverStreet.contains(RECEIVER_NOTES_START)) {
            val indexStart = receiverStreet.indexOf(RECEIVER_NOTES_START)
            val indexEnd = receiverStreet.indexOf(RECEIVER_NOTES_END)
            val getAllNotes = receiverStreet.substring(indexStart, indexEnd+1)
            val indexValueStart = getAllNotes.indexOf(RECEIVER_NOTES_COLON)
            val indexValueEnd = getAllNotes.indexOf(RECEIVER_NOTES_END)
            notesValue = getAllNotes.substring(indexValueStart+1, indexValueEnd-1)
        }

        val dataShipping = SomDetailShipping(
                detailResponse.shipment.name + " - " + detailResponse.shipment.productName,
                detailResponse.paymentSummary.shippingPriceText,
                detailResponse.receiver.name,
                detailResponse.receiver.phone,
                detailResponse.receiver.street,
                detailResponse.receiver.district + ", " + detailResponse.receiver.city + " " + detailResponse.receiver.postal,
                notesValue,
                detailResponse.flagOrderMeta.flagFreeShipping,
                detailResponse.bookingInfo.driver.photo,
                detailResponse.bookingInfo.driver.name,
                detailResponse.bookingInfo.driver.phone,
                detailResponse.bookingInfo.driver.licenseNumber)

        val dataPayments = SomDetailPayments(
                detailResponse.paymentSummary.productsPriceText,
                detailResponse.paymentSummary.totalItem,
                detailResponse.paymentSummary.totalWeightText,
                detailResponse.paymentSummary.shippingPriceText,
                detailResponse.paymentSummary.insurancePrice,
                detailResponse.paymentSummary.insurancePriceText,
                detailResponse.paymentSummary.additionalPrice,
                detailResponse.paymentSummary.additionalPriceText,
                detailResponse.paymentSummary.totalPriceText)

        listDetailData.add(SomDetailData(dataHeader, DETAIL_HEADER_TYPE))
        listDetailData.add(SomDetailData(dataProducts, DETAIL_PRODUCTS_TYPE))
        listDetailData.add(SomDetailData(dataShipping, DETAIL_SHIPPING_TYPE))
        listDetailData.add(SomDetailData(dataPayments, DETAIL_PAYMENT_TYPE))

        somDetailAdapter.listDataDetail = listDetailData.toMutableList()
        somDetailAdapter.notifyDataSetChanged()

        // buttons
        if (detailResponse.button.isNotEmpty()) {
            rl_btn_detail?.visibility = View.VISIBLE
            detailResponse.button.first().let { buttonResp ->
                btn_primary?.text = buttonResp.displayName

                if (buttonResp.key.equals(KEY_ACCEPT_ORDER, true)) {
                    setActionAcceptOrder(buttonResp)

                } else if (buttonResp.key.equals(KEY_TRACK_SELLER, true)) {
                    setActionGoToTrackingPage(buttonResp)

                } else if (buttonResp.key.equals(KEY_REQUEST_PICKUP, true)) {
                    setActionRequestPickup()
                }
            }

            if (detailResponse.button.size > 1) {
                btn_secondary?.visibility = View.VISIBLE
                btn_secondary?.setOnClickListener {
                    somBottomSheetRejectOrderAdapter = SomBottomSheetRejectOrderAdapter(this, hasRadioBtn = false)
                    showTextOnlyBottomSheet()
                    bottomSheetUnify.clearHeader(true)
                    bottomSheetUnify.clearClose(true)
                    val mapKey = HashMap<String, String>()
                    detailResponse.button.filterIndexed { index, _ -> (index != 0) }.forEach { btn ->
                        mapKey[btn.key] = btn.displayName

                    }
                    somBottomSheetRejectOrderAdapter.mapKey = mapKey
                    somBottomSheetRejectOrderAdapter.notifyDataSetChanged()
                }
            } else {
                btn_secondary?.visibility = View.GONE
            }

        } else {
            rl_btn_detail?.visibility = View.GONE
        }
    }

    private fun setActionAcceptOrder(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        btn_primary?.setOnClickListener { v ->
            dialogUnify = DialogUnify(v.context, HORIZONTAL_ACTION, NO_IMAGE).apply {
                setTitle(buttonResp.title)
                setDescription(buttonResp.content)
                setPrimaryCTAText(getString(R.string.terima_pesanan))
                setPrimaryCTAClickListener {
                    val mapParam = buttonResp.param.convertStrObjToHashMap()
                    if (mapParam.containsKey(PARAM_ORDER_ID) && mapParam.containsKey(PARAM_SHOP_ID)) {
                        somDetailViewModel.acceptOrder(GraphqlHelper.loadRawString(resources, R.raw.gql_som_accept_order),
                                mapParam[PARAM_ORDER_ID].toString(), mapParam[PARAM_SHOP_ID].toString())
                        dialogUnify.dismiss()
                    }
                }
                setSecondaryCTAText(getString(R.string.kembali))
                setSecondaryCTAClickListener {
                    dialogUnify.dismiss()
                }
            }
            dialogUnify.show()
        }
    }

    private fun setActionGoToTrackingPage(buttonResp: SomDetailOrder.Data.GetSomDetail.Button) {
        btn_primary?.setOnClickListener {
            var routingAppLink: String = ApplinkConst.ORDER_TRACKING.replace("{order_id}", detailResponse.orderId.toString())

            val trackingUrl: String?
            val uri = Uri.parse(buttonResp.url)
            trackingUrl = uri.getQueryParameter("url")

            val uriBuilder = Uri.Builder()
            uriBuilder.appendQueryParameter(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING, trackingUrl)
            routingAppLink += uriBuilder.toString()
            RouteManager.route(context, routingAppLink)
        }
    }

    private fun setActionRequestPickup() {
        btn_primary?.setOnClickListener {
            Intent(activity, SomConfirmReqPickupActivity::class.java).apply {
                putExtra(PARAM_ORDER_ID, orderId)
                startActivityForResult(this, FLAG_CONFIRM_REQ_PICKUP)
            }
        }
    }

    private fun showTextOnlyBottomSheet() {
        bottomSheetUnify = BottomSheetUnify()
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null)
        viewBottomSheet.rv_bottomsheet_secondary?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = somBottomSheetRejectOrderAdapter
        }
        viewBottomSheet.fl_btn_primary?.visibility = View.GONE
        viewBottomSheet.tf_extra_notes?.visibility = View.GONE
        bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
        bottomSheetUnify.setChild(viewBottomSheet)
        bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
    }

    override fun onBottomSheetItemClick(key: String) {
        bottomSheetUnify.dismiss()
        println("++ KEY = $key")
        if (key.equals(KEY_REJECT_ORDER, true)) {
            setActionRejectOrder()

        } else if (key.equals(KEY_REASON_EMPTY_STOCK, true)) {
            // besok lanjut bikin adapter untuk beberapa layout khusus untuk reject order

        } else if (key.equals(KEY_REASON_COURIER_PROBLEM, true)) {
            showTextOnlyBottomSheet()
            bottomSheetUnify.clearHeader(false)
            bottomSheetUnify.clearClose(false)
            bottomSheetUnify.setTitle(TITLE_COURIER_PROBLEM)
            listRejectTypeData = arrayListOf()
            val mapRadio = HashMap<String, String>()
            mapRadio[KEY_REASON_OTHER] = VALUE_REASON_OTHER
            listRejectTypeData.add(SomRejectTypeData(createOptionsCourierProblems(), BOTTOMSHEET_TEXT_RADIO_TYPE))
            listRejectTypeData.add(SomRejectTypeData(mapRadio, BOTTOMSHEET_TEXT_RADIO_WITH_REASON_TYPE))
            // somBottomSheetRejectOrderAdapter.listRejectTypeData = listRejectTypeData
            // somBottomSheetRejectOrderAdapter.notifyDataSetChanged()
        }
    }

    private fun setActionRejectOrder() {
        somDetailViewModel.getRejectReasons(GraphqlHelper.loadRawString(resources, R.raw.gql_som_reject_reason))
        observingRejectReasons()
    }

    private fun createOptionsCourierProblems(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map[KEY_COURIER_PROBLEM_OFFICE_CLOSED] = VALUE_COURIER_PROBLEM_OFFICE_CLOSED
        map[KEY_COURIER_PROBLEM_UNMATCHED_COST] = VALUE_REASON_SHOP_CLOSED
        map[KEY_REASON_COURIER_PROBLEM] = VALUE_REASON_COURIER_PROBLEM
        map[KEY_REASON_BUYER_NO_RESPONSE] = VALUE_REASON_BUYER_NO_RESPONSE
        map[KEY_REASON_OTHER] = VALUE_REASON_OTHER
        return map
    }

    override fun onShowBottomSheetInfo(title: String, resIdDesc: Int) {
        val bottomSheetUnify = BottomSheetUnify()
        val childView = View.inflate(context, R.layout.bottomsheet_som_info, null)

        val bottomSheetDesc: Typography = childView.findViewById(R.id.bottomsheet_desc)
        bottomSheetDesc.setText(resIdDesc)

        val childBtn: UnifyButton = childView.findViewById(R.id.btn_mengerti)
        childBtn.setOnClickListener { bottomSheetUnify.dismiss() }

        bottomSheetUnify.apply {
            clearClose(false)
            clearHeader(false)
            setTitle(title)
            setOnDismissListener { this.dismiss() }
            setChild(childView)
        }
        bottomSheetUnify.show(fragmentManager, "")
    }

    override fun onRejectReasonItemClick(rejectReason: SomReasonRejectData.Data.SomRejectReason) {
        println("++ REASON CODE = ${rejectReason.reasonCode}")
        /* 1 = Stok Produk Kosong
        *  4 = Toko Sedang Tutup
        *  7 = Kendala Kurir
        *  15 = Pembeli Tidak Respons
        *  14 = Lainnya */
        when (rejectReason.reasonCode) {
            1 -> setProductEmpty(rejectReason.reasonCode.toString())
            4 -> setShopClosed(rejectReason.reasonCode.toString())
            7 -> setCourierProblems(rejectReason.reasonCode.toString(), rejectReason.listChild)
            15 -> setBuyerNoResponse(rejectReason.reasonCode.toString())
            14 -> setOtherReason(rejectReason.reasonCode.toString())
        }
    }

    override fun onTextCopied(label: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.primaryClip = ClipData.newPlainText(label, str)
        showCommonToaster(str)
    }

    override fun onInvalidResiUpload() {
        // TODO: need backend provide shop_id to make url for upload_proof_awb
        /*val awlUploadProofUrl = BASE_URL_UPLOAD_PROOF_AWB + detailResponse.orderId + "/" + QUERY_INVOICE_URL_UPLOAD_AWB + detailResponse.invoice
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.UPLOAD_AWB)
        intent.putExtra(EXTRA_URL_UPLOAD, data.getAwbUploadProofUrl())
        startActivity(intent)*/
    }

    private fun setProductEmpty(rCode: String) {
        // ini penentu previous bottomsheetnya dismissed apa nggak?
        bottomSheetUnify.dismiss()
        somBottomSheetStockEmptyAdapter = SomBottomSheetStockEmptyAdapter()
        bottomSheetUnify = BottomSheetUnify()
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null)
        viewBottomSheet.rv_bottomsheet_secondary?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = somBottomSheetStockEmptyAdapter }

        viewBottomSheet.tf_extra_notes?.setLabelStatic(true)
        viewBottomSheet.tf_extra_notes?.textFiedlLabelText?.text = getString(R.string.empty_stock_extra_note)
        viewBottomSheet.tf_extra_notes?.setPlaceholder(getString(R.string.empty_stock_extra_placeholder))

        viewBottomSheet.fl_btn_primary?.visibility = View.VISIBLE
        viewBottomSheet.fl_btn_primary?.setOnClickListener {
            bottomSheetUnify.dismiss()
            val orderRejectRequest = SomRejectRequest()
            orderRejectRequest.orderId = detailResponse.orderId.toString()
            orderRejectRequest.rCode = rCode
            var strListPrd = ""
            var indexPrd = 0
            somBottomSheetStockEmptyAdapter.getListProductEmptied().forEach {
                if (indexPrd > 0) strListPrd += "~"
                strListPrd += it.id
                indexPrd++
            }
            orderRejectRequest.listPrd = strListPrd
            orderRejectRequest.reason = viewBottomSheet.tf_extra_notes?.textFieldInput?.text.toString()
            doRejectOrder(orderRejectRequest)
        }

        bottomSheetUnify.setFullPage(true)
        bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
        bottomSheetUnify.setChild(viewBottomSheet)
        bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
        bottomSheetUnify.setTitle(TITLE_PILIH_PRODUK_KOSONG)
        somBottomSheetStockEmptyAdapter.listProduct = detailResponse.listProduct.toMutableList()
        somBottomSheetStockEmptyAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setShopClosed(rCode: String) {
        bottomSheetUnify.dismiss()
        bottomSheetUnify = BottomSheetUnify().apply {
            if (isAdded) this.dismiss()

        }
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_shop_closed, null)

        val defaultDateNow = Date().toFormattedString("dd/MM/yyyy")
        // setup closing start date
        viewBottomSheet.tf_start_shop_closed?.textFieldWrapper?.hint = getString(R.string.start_shop_closed_label)
        viewBottomSheet.tf_start_shop_closed?.textFieldInput?.setText(defaultDateNow)
        viewBottomSheet.tf_start_shop_closed?.textFieldInput?.isEnabled = false

        // setup closing end date
        viewBottomSheet.tf_end_shop_closed?.textFieldWrapper?.hint = getString(R.string.end_shop_closed_label)
        viewBottomSheet.tf_end_shop_closed?.textFieldInput?.apply {
            setText(defaultDateNow)
        }
        viewBottomSheet.tf_end_shop_closed?.textFieldInput?.isEnabled = false
        viewBottomSheet.tf_end_shop_closed?.setFirstIcon(R.drawable.ic_som_filter_calendar)
        viewBottomSheet.tf_end_shop_closed?.textFieldIcon1?.setOnClickListener {
            showDatePicker(viewBottomSheet.tf_end_shop_closed, viewBottomSheet)
        }
        updateClosingEndDate(convertFormatDate(defaultDateNow, "dd/MM/yyyy", "dd MMM yyyy"), viewBottomSheet)

        // setup closing additional notes
        viewBottomSheet.tf_shop_closed_notes?.setLabelStatic(true)
        viewBottomSheet.tf_shop_closed_notes?.textFiedlLabelText?.text = getString(R.string.shop_closed_note_label)
        viewBottomSheet.tf_shop_closed_notes?.textFieldInput?.hint = getString(R.string.shop_closed_note_placeholder)

        bottomSheetUnify.setFullPage(true)
        bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
        bottomSheetUnify.setChild(viewBottomSheet)
        bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
        bottomSheetUnify.setTitle(TITLE_ATUR_TOKO_TUTUP)

        viewBottomSheet.btn_reject_shop_closed?.setOnClickListener {
            bottomSheetUnify.dismiss()
            val orderRejectRequest = SomRejectRequest(
                    orderId = detailResponse.orderId.toString(),
                    rCode = rCode,
                    closedNote = viewBottomSheet.tf_shop_closed_notes?.textFieldInput?.text.toString(),
                    closeEnd = viewBottomSheet.tf_end_shop_closed?.textFieldInput?.text.toString()
            )
            doRejectOrder(orderRejectRequest)
        }
    }

    private fun updateClosingEndDate(endDate: String, viewBottomSheet: View) {
        val endNote1 = getString(R.string.shop_closed_endnote1)
        val endNote2 = getString(R.string.shop_closed_endnote2)
        val endNote3 = getString(R.string.shop_closed_endnote3)

        context?.let {
            val customString = HtmlLinkHelper(it, "$endNote1 <b>$endNote2</b>$endNote3 $endDate")
            viewBottomSheet.shop_closed_endnotes?.text = customString.spannedString
        }
    }

    private fun setCourierProblems(rCode: String, listChild: List<SomReasonRejectData.Data.SomRejectReason.Child>) {
        bottomSheetUnify.dismiss()
        somBottomSheetCourierProblemsAdapter = SomBottomSheetCourierProblemsAdapter(this)
        somBottomSheetCourierProblemsAdapter.reasonCode = rCode
        bottomSheetUnify = BottomSheetUnify()
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null)
        viewBottomSheet.rv_bottomsheet_secondary?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = somBottomSheetCourierProblemsAdapter }

        viewBottomSheet.fl_btn_primary?.visibility = View.VISIBLE
        viewBottomSheet.fl_btn_primary?.setOnClickListener {
            bottomSheetUnify.dismiss()
            val orderRejectRequest = SomRejectRequest()
            orderRejectRequest.orderId = detailResponse.orderId.toString()
            orderRejectRequest.rCode = rCode
            orderRejectRequest.reason = viewBottomSheet.tf_extra_notes?.textFieldInput?.text.toString()
            doRejectOrder(orderRejectRequest)
        }

        bottomSheetUnify.setFullPage(true)
        bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
        bottomSheetUnify.setChild(viewBottomSheet)
        bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
        bottomSheetUnify.setTitle(TITLE_COURIER_PROBLEM)
        somBottomSheetCourierProblemsAdapter.listChildCourierProblems = listChild.toMutableList()
        somBottomSheetCourierProblemsAdapter.notifyDataSetChanged()
    }

    private fun setBuyerNoResponse(reasonCode: String) {
        bottomSheetUnify.dismiss()

        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            rv_bottomsheet_secondary?.visibility = View.GONE
            tf_extra_notes?.visibility = View.VISIBLE
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = getString(R.string.buyer_no_resp_label)
            tf_extra_notes?.setPlaceholder(getString(R.string.buyer_no_resp_placeholder))
            fl_btn_primary?.visibility = View.VISIBLE
            fl_btn_primary?.setOnClickListener {
                bottomSheetUnify.dismiss()

                val orderRejectRequest = SomRejectRequest().apply {
                    orderId = detailResponse.orderId.toString()
                    rCode = reasonCode
                    reason = tf_extra_notes?.textFieldInput?.text.toString()
                }
                doRejectOrder(orderRejectRequest)
            }
        }

        bottomSheetUnify = BottomSheetUnify().apply {
            if (this.isAdded) this.dismiss()
            setFullPage(true)
            setOnDismissListener { this.dismiss() }
            setCloseClickListener { this.dismiss() }
            setChild(viewBottomSheet)
            setTitle(VALUE_REASON_BUYER_NO_RESPONSE)
        }
        bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
    }

    private fun setOtherReason(reasonCode: String) {
        bottomSheetUnify.dismiss()

        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null).apply {
            rv_bottomsheet_secondary?.visibility = View.GONE
            tf_extra_notes?.visibility = View.VISIBLE
            tf_extra_notes?.setLabelStatic(true)
            tf_extra_notes?.textFiedlLabelText?.text = getString(R.string.other_reason_resp_label)
            tf_extra_notes?.setPlaceholder(getString(R.string.other_reason_resp_placeholder))
            fl_btn_primary?.visibility = View.VISIBLE
            fl_btn_primary?.setOnClickListener {
                bottomSheetUnify.dismiss()

                val orderRejectRequest = SomRejectRequest().apply {
                    orderId = detailResponse.orderId.toString()
                    rCode = reasonCode
                    reason = tf_extra_notes?.textFieldInput?.text.toString()
                }
                doRejectOrder(orderRejectRequest)
            }
        }

        bottomSheetUnify = BottomSheetUnify().apply {
            if (this.isAdded) this.dismiss()
            setFullPage(true)
            setOnDismissListener { this.dismiss() }
            setCloseClickListener { this.dismiss() }
            setChild(viewBottomSheet)
            setTitle(VALUE_REASON_OTHER)
        }
        bottomSheetUnify.show(fragmentManager, getString(R.string.show_bottomsheet))
    }

    override fun onChooseOptionCourierProblem(optionCourierProblem: SomReasonRejectData.Data.SomRejectReason.Child) {
        if (optionCourierProblem.reasonText.equals(VALUE_COURIER_PROBLEM_OTHERS, ignoreCase = true)) {
            bottomSheetUnify.tf_extra_notes?.visibility = View.VISIBLE
            bottomSheetUnify.tf_extra_notes?.setLabelStatic(true)
            bottomSheetUnify.tf_extra_notes?.setPlaceholder(getString(R.string.placeholder_reject_reason))
        } else {
            bottomSheetUnify.tf_extra_notes?.visibility = View.GONE
        }
    }

    private fun doRejectOrder(orderRejectRequest: SomRejectRequest) {
        somDetailViewModel.rejectOrder(GraphqlHelper.loadRawString(resources, R.raw.gql_som_reject_order), orderRejectRequest)
        observingRejectOrder()
    }

    private fun observingRejectOrder() {
        somDetailViewModel.rejectOrderResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    rejectOrderResponse = it.data.rejectOrder
                    if (rejectOrderResponse.success == 1) {
                        // if success = 1 : finishActivity, then show toaster
                        activity?.setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(RESULT_REJECT_ORDER, rejectOrderResponse)
                        })
                        activity?.finish()

                    } else {
                        showToasterError(rejectOrderResponse.message.first())
                    }
                }
                is Fail -> {
                    showToasterError(getString(R.string.global_error))
                }
            }
        })
    }

    private fun showToasterError(message: String) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, LENGTH_SHORT, TYPE_ERROR, ACTION_OK)
        }
    }

    private fun showCommonToaster(message: String) {
        val toasterCommon = Toaster
        view?.let { v ->
            toasterCommon.make(v, message, LENGTH_SHORT, TYPE_NORMAL)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(tfEndShopClosed: TextFieldUnify, viewBottomSheet: View) {
        context?.let {  context ->
            val dateNow = GregorianCalendar(LocaleUtils.getCurrentLocale(context))
            val dateMax = GregorianCalendar(2100, 0, 1)

            val datePicker = DatePickerUnify(context, dateNow, dateNow, dateMax)
            datePicker.setTitle(getString(R.string.end_shop_closed_label))
            datePicker.show(fragmentManager, "")
            datePicker.datePickerButton.setOnClickListener {
                val resultDate = datePicker.getDate()
                tfEndShopClosed.textFieldInput.setText("${resultDate[0]}/${resultDate[1]+1}/${resultDate[2]}")
                updateClosingEndDate("${resultDate[0]} ${convertMonth(resultDate[1])} ${resultDate[2]}", viewBottomSheet)
                datePicker.dismiss()
            }
            datePicker.setCloseClickListener { datePicker.dismiss() }
        }
    }

    override fun onDialPhone(strPhoneNo: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val phone = "tel:$strPhoneNo"
        intent.data = Uri.parse(phone)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FLAG_CONFIRM_REQ_PICKUP && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(RESULT_PROCESS_REQ_PICKUP)) {
                    val resultProcessReqPickup = data.getParcelableExtra<SomProcessReqPickup.Data.MpLogisticRequestPickup>(RESULT_PROCESS_REQ_PICKUP)
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_PROCESS_REQ_PICKUP, resultProcessReqPickup)
                    })
                    activity?.finish()
                }
            }
        }
    }
}
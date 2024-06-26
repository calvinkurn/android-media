package com.tokopedia.logisticseller.ui.requestpickup.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_ORDER_ID
import com.tokopedia.logisticseller.common.LogisticSellerConst.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.logisticseller.common.Utils
import com.tokopedia.logisticseller.common.Utils.updateShopActive
import com.tokopedia.logisticseller.common.errorhandler.LogisticSellerErrorHandler
import com.tokopedia.logisticseller.databinding.FragmentRequestPickupBinding
import com.tokopedia.logisticseller.ui.requestpickup.data.RequestPickupAnalytics
import com.tokopedia.logisticseller.ui.requestpickup.data.mapper.SchedulePickupMapper
import com.tokopedia.logisticseller.ui.requestpickup.data.model.ScheduleTime
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.di.DaggerSomConfirmReqPickupComponent
import com.tokopedia.logisticseller.ui.requestpickup.di.SomConfirmReqPickupComponent
import com.tokopedia.logisticseller.ui.requestpickup.presentation.adapter.SchedulePickupAdapter
import com.tokopedia.logisticseller.ui.requestpickup.presentation.adapter.SomConfirmReqPickupCourierNotesAdapter
import com.tokopedia.logisticseller.ui.requestpickup.presentation.viewmodel.RequestPickupViewModel
import com.tokopedia.logisticseller.ui.requestpickup.util.DateMapper
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class RequestPickupFragment :
    BaseDaggerFragment(),
    SchedulePickupAdapter.SomConfirmSchedulePickupAdapterListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val somConfirmRequestPickupViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RequestPickupViewModel::class.java]
    }

    private val binding by viewBinding(FragmentRequestPickupBinding::bind)

    private var currOrderId = ""
    private var currSchedulePickupKey = ""
    private var currSchedulePickupTime = ""
    private var confirmReqPickupResponse = SomConfirmReqPickup.Data.MpLogisticPreShipInfo()
    private var processReqPickupResponse = SomProcessReqPickup.Data.MpLogisticRequestPickup()
    private lateinit var confirmReqPickupCourierNotesAdapter: SomConfirmReqPickupCourierNotesAdapter

    private var bottomSheetSchedulePickup: BottomSheetUnify? = null
    private var bottomSheetSchedulePickupTodayAdapter = SchedulePickupAdapter(this)
    private var bottomSheetSchedulePickupTomorrowAdapter = SchedulePickupAdapter(this)
    private var rvSchedulePickupToday: RecyclerView? = null
    private var rvSchedulePickupTomorrow: RecyclerView? = null
    private var tvSchedulePickupToday: Typography? = null
    private var tvSchedulePickupTomorrow: Typography? = null
    private var dividerSchedulePickup: View? = null
    private var isFirstVisit: Boolean? = true
    private val today: String = "Hari ini"
    private val tomorrow: String = "Besok"

    companion object {
        private const val ERROR_GET_CONFIRM_REQUEST_PICKUP_DATA = "Error when get confirm request pickup layout data."
        private const val ERROR_PROCESSING_REQUEST_PICKUP = "Error when processing request pickup."

        @JvmStatic
        fun newInstance(bundle: Bundle): RequestPickupFragment {
            return RequestPickupFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, bundle.getString(PARAM_ORDER_ID))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            currOrderId = arguments?.getString(PARAM_ORDER_ID).toString()
        }
        loadConfirmRequestPickup()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request_pickup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        observingConfirmReqPickup()
        loadingFullPage(true)
    }

    override fun onResume() {
        super.onResume()
        updateShopActive()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: SomConfirmReqPickupComponent = DaggerSomConfirmReqPickupComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    private fun setupHeader() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(binding?.headerSomConfirmRequestPickup)
        }
    }

    private fun loadConfirmRequestPickup() {
        val order = SomConfirmReqPickupParam.Order(orderId = currOrderId)
        val reqPickupParam = SomConfirmReqPickupParam().apply { orderList.add(order) }
        somConfirmRequestPickupViewModel.loadConfirmRequestPickup(reqPickupParam)
    }

    private fun processReqPickup() {
        val processReqPickupParam = SomProcessReqPickupParam(orderId = currOrderId, schedulePickupTime = currSchedulePickupKey)
        somConfirmRequestPickupViewModel.processRequestPickup(processReqPickupParam)
    }

    private fun observingConfirmReqPickup() {
        somConfirmRequestPickupViewModel.confirmReqPickupResult.observe(viewLifecycleOwner) {
            loadingFullPage(false)
            when (it) {
                is Success -> {
                    confirmReqPickupResponse = it.data.mpLogisticPreShipInfo
                    renderConfirmReqPickup()
                    renderTicker()
                }

                is Fail -> {
                    LogisticSellerErrorHandler.logExceptionToCrashlytics(
                        it.throwable,
                        ERROR_GET_CONFIRM_REQUEST_PICKUP_DATA
                    )
                    LogisticSellerErrorHandler.logExceptionToServer(
                        errorTag = LogisticSellerErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        LogisticSellerErrorHandler.SomMessage.GET_REQUEST_PICKUP_DATA_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )

                    when (it.throwable.localizedMessage?.toIntOrNull()) {
                        ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                        ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                        ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                        else -> {
                            view?.let { view ->
                                showGlobalError(GlobalError.SERVER_ERROR)
                                Utils.showToasterError(
                                    LogisticSellerErrorHandler.getErrorMessage(it.throwable, view.context),
                                    view
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderTicker() {
        binding?.tickerInfoCourier?.apply { ->
            setTickerShape(Ticker.SHAPE_LOOSE)

            val tickerParam = confirmReqPickupResponse.dataSuccess.tickerUnificationParams

            val template = TargetedTickerParamModel.Template().copy(
                contents = tickerParam.template.contents.map {
                    TargetedTickerParamModel.Template.Content(it.key, it.value)
                }
            )
            val target = tickerParam.target.map {
                TargetedTickerParamModel.Target(it.type, it.values)
            }

            val param = TargetedTickerParamModel(
                page = tickerParam.page,
                target = target,
                template = template
            )

            loadAndShow(param)
        }
    }

    private fun showGlobalError(type: Int) {
        binding?.apply {
            globalError.setType(type)
            globalError.setActionClickListener {
                loadConfirmRequestPickup()
                binding?.globalError?.gone()
            }
            globalError.visible()
            viewgroupParentFullView.gone()
            llBtn.gone()
        }
    }

    private fun observingProcessReqPickup() {
        somConfirmRequestPickupViewModel.processReqPickupResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    processReqPickupResponse = it.data.mpLogisticRequestPickup
                    activity?.setResult(
                        Activity.RESULT_OK,
                        Intent().apply {
                            putExtra(RESULT_PROCESS_REQ_PICKUP, processReqPickupResponse.listMessage.firstOrNull())
                        }
                    )
                    activity?.finish()
                }

                is Fail -> {
                    LogisticSellerErrorHandler.logExceptionToCrashlytics(
                        it.throwable,
                        ERROR_PROCESSING_REQUEST_PICKUP
                    )
                    LogisticSellerErrorHandler.logExceptionToServer(
                        errorTag = LogisticSellerErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        LogisticSellerErrorHandler.SomMessage.REQUEST_PICKUP_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    context?.run {
                        Utils.showToasterError(LogisticSellerErrorHandler.getErrorMessage(it.throwable, this), view)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderConfirmReqPickup() {
        binding?.run {
            shopAddress.text = confirmReqPickupResponse.dataSuccess.pickupLocation.address
            shopPhone.text = confirmReqPickupResponse.dataSuccess.pickupLocation.phone
            setInvoiceNumber(confirmReqPickupResponse.dataSuccess.detail.invoice)

            if (confirmReqPickupResponse.dataSuccess.detail.listShippers.isNotEmpty()) {
                val shipper = confirmReqPickupResponse.dataSuccess.detail.listShippers[0]
                ivCourier.loadImageWithoutPlaceholder(shipper.courierImg)

                context?.let {
                    val htmlCourierNameService = HtmlLinkHelper(it, "<b>${shipper.name}</b> ${shipper.service}")
                    tvCourierNameService.text = htmlCourierNameService.spannedString

                    val htmlCourierCountService = HtmlLinkHelper(it, "${shipper.countText} <b>${shipper.count}</b>")
                    tvCourierCount.text = htmlCourierCountService.spannedString
                }

                if (confirmReqPickupResponse.dataSuccess.detail.orchestraPartner.isEmpty()) {
                    tvCourierNotes.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
                } else {
                    tvCourierNotes.text =
                        getString(R.string.courier_option_schedule, confirmReqPickupResponse.dataSuccess.detail.orchestraPartner)
                }
            }

            if (confirmReqPickupResponse.dataSuccess.notes.listNotes.isNotEmpty()) {
                confirmReqPickupCourierNotesAdapter = SomConfirmReqPickupCourierNotesAdapter()
                labelPastikan.visibility = View.VISIBLE
                rvCourierNotes.visibility = View.VISIBLE
                rvCourierNotes.run {
                    layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                    adapter = confirmReqPickupCourierNotesAdapter
                }

                confirmReqPickupCourierNotesAdapter.listCourierNotes =
                    confirmReqPickupResponse.dataSuccess.notes.listNotes.toMutableList()
                confirmReqPickupCourierNotesAdapter.notifyDataSetChanged()
            } else {
                labelPastikan.visibility = View.GONE
                rvCourierNotes.visibility = View.GONE
            }

            val dropOffNotes = confirmReqPickupResponse.dataSuccess.dropOffLocation.dropOffNotes
            showDropOffInfo(
                titleView = dropoffInfoTitle,
                descriptionView = dropoffInfoText,
                button = dropoffInfoButton,
                cardView = cardDropOff,
                title = dropOffNotes.title,
                description = dropOffNotes.text,
                urlDetail = dropOffNotes.urlDetail,
                urlText = dropOffNotes.urlText
            )

            if (confirmReqPickupResponse.dataSuccess.schedule_time.today.isNotEmpty() || confirmReqPickupResponse.dataSuccess.schedule_time.tomorrow.isNotEmpty()) {
                val schedulePickupMapper = SchedulePickupMapper()

                rlSchedulePickup.visibility = View.VISIBLE
                btnArrow.visibility = View.GONE
                pickupNow.centerText = true
                pickupSchedule.centerText = true
                tvSchedule.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
                setActiveChips(pickupNow, pickupSchedule)
                bottomSheetSchedulePickupTodayAdapter.setData(
                    schedulePickupMapper.mapSchedulePickup(
                        confirmReqPickupResponse.dataSuccess.schedule_time.today,
                        today
                    ),
                    currSchedulePickupKey
                )
                bottomSheetSchedulePickupTomorrowAdapter.setData(
                    schedulePickupMapper.mapSchedulePickup(
                        confirmReqPickupResponse.dataSuccess.schedule_time.tomorrow,
                        tomorrow
                    ),
                    currSchedulePickupKey
                )

                pickupNow.setOnClickListener {
                    setActiveChips(pickupNow, pickupSchedule)
                    btnArrow.visibility = View.GONE
                    dividerSchedule.visibility = View.GONE
                    tvSchedule.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
                    currSchedulePickupKey = ""
                }

                pickupSchedule.setOnClickListener {
                    if (isFirstVisit == true) {
                        val startTime: String
                        val endTime: String
                        val formattedTime: String
                        if (confirmReqPickupResponse.dataSuccess.schedule_time.today.isNotEmpty()) {
                            startTime = DateMapper.formatDate(bottomSheetSchedulePickupTodayAdapter.scheduleTime[0].start)
                            endTime = DateMapper.formatDate(bottomSheetSchedulePickupTodayAdapter.scheduleTime[0].end)
                            formattedTime = "$startTime - $endTime WIB"
                            tvSchedule.text = "${bottomSheetSchedulePickupTodayAdapter.scheduleTime[0].day}, $formattedTime"
                        } else {
                            startTime = DateMapper.formatDate(bottomSheetSchedulePickupTomorrowAdapter.scheduleTime[0].start)
                            endTime = DateMapper.formatDate(bottomSheetSchedulePickupTomorrowAdapter.scheduleTime[0].end)
                            formattedTime = "$startTime - $endTime WIB"
                            tvSchedule.text = "${bottomSheetSchedulePickupTomorrowAdapter.scheduleTime[0].day}, $formattedTime"
                        }
                        isFirstVisit = false
                    } else {
                        tvSchedule.text = currSchedulePickupTime
                    }
                    setActiveChips(pickupSchedule, pickupNow)
                    btnArrow.visibility = View.VISIBLE
                    dividerSchedule.visibility = View.VISIBLE
                    btnArrow.setOnClickListener {
                        openBottomSheetSchedulePickup()
                    }
                }
            } else {
                rlSchedulePickup.visibility = View.GONE
            }

            btnReqPickup.setOnClickListener {
                RequestPickupAnalytics.eventClickRequestPickupPopup()
                processReqPickup()
                observingProcessReqPickup()
            }
        }
    }

    fun showDropOffInfo(
        titleView: Typography,
        descriptionView: Typography,
        button: Typography,
        cardView: CardUnify2,
        title: String,
        description: String,
        urlDetail: String,
        urlText: String
    ) {
        if (title.isNotEmpty() && description.isNotEmpty() && urlText.isNotEmpty()) {
            titleView.text = title
            descriptionView.text = description
            button.apply {
                text = urlText
                setOnClickListener {
                    openWebView(urlDetail)
                }
            }
            cardView.visible()
        } else {
            cardView.gone()
        }
    }

    private fun openBottomSheetSchedulePickup() {
        bottomSheetSchedulePickup = BottomSheetUnify()
        bottomSheetSchedulePickup?.setTitle(getString(R.string.som_request_pickup_bottomsheet_title))
        val viewBottomSheetSchedulePickup = View.inflate(context, R.layout.bottomsheet_schedule_pickup, null)
        setupChild(viewBottomSheetSchedulePickup)

        bottomSheetSchedulePickup?.apply {
            clearContentPadding = true
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetSchedulePickup)
            setOnDismissListener { dismiss() }
        }

        childFragmentManager.let {
            bottomSheetSchedulePickup?.show(it, "show")
        }
    }

    private fun setupChild(child: View) {
        rvSchedulePickupToday = child.findViewById(R.id.rv_today)
        rvSchedulePickupTomorrow = child.findViewById(R.id.rv_tomorrow)
        tvSchedulePickupToday = child.findViewById(R.id.tv_today)
        tvSchedulePickupTomorrow = child.findViewById(R.id.tv_tomorrow)
        dividerSchedulePickup = child.findViewById(R.id.divider_schedule)

        rvSchedulePickupToday?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetSchedulePickupTodayAdapter
        }

        rvSchedulePickupTomorrow?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetSchedulePickupTomorrowAdapter
        }

        if (confirmReqPickupResponse.dataSuccess.schedule_time.today.isEmpty()) {
            rvSchedulePickupToday?.gone()
            tvSchedulePickupToday?.gone()
            dividerSchedulePickup?.gone()
        } else if (confirmReqPickupResponse.dataSuccess.schedule_time.tomorrow.isEmpty()) {
            rvSchedulePickupTomorrow?.gone()
            tvSchedulePickupTomorrow?.gone()
            dividerSchedulePickup?.gone()
        }
    }

    private fun setActiveChips(selected: ChipsUnify?, deselected: ChipsUnify?) {
        selected?.chipType = ChipsUnify.TYPE_SELECTED
        deselected?.chipType = ChipsUnify.TYPE_NORMAL
    }

    @SuppressLint("SetTextI18n")
    override fun onSchedulePickupClicked(scheduleTime: ScheduleTime, formattedTime: String) {
        bottomSheetSchedulePickup?.dismiss()
        currSchedulePickupKey = scheduleTime.key
        binding?.tvSchedule?.text = "${scheduleTime.day}, $formattedTime"
        currSchedulePickupTime = "${scheduleTime.day}, $formattedTime"
    }

    private fun setInvoiceNumber(invoiceNumber: String) {
        if (invoiceNumber.isNotEmpty()) {
            binding?.tvInvoiceNumber?.text = invoiceNumber
            binding?.maskTriggerInvoiceNumber?.setOnClickListener {
                onTextCopied(getString(R.string.invoice_label), invoiceNumber)
            }
        } else {
            binding?.btnCopyInvoiceNumber?.visibility = View.GONE
        }
    }

    private fun onTextCopied(label: String, str: String) {
        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, str))
        Toaster.build(requireView(), getString(R.string.success_invoice_copied), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
    }

    private fun openWebView(url: String) {
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    private fun loadingFullPage(isLoading: Boolean) {
        if (isLoading) {
            binding?.viewgroupParentFullView?.gone()
            binding?.llBtn?.gone()
            binding?.loadingRequestPickupLoading?.visible()
        } else {
            binding?.viewgroupParentFullView?.visible()
            binding?.llBtn?.visible()
            binding?.loadingRequestPickupLoading?.gone()
        }
    }
}

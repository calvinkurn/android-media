package com.tokopedia.sellerorder.requestpickup.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.databinding.FragmentSomConfirmReqPickupBinding
import com.tokopedia.sellerorder.requestpickup.data.mapper.SchedulePickupMapper
import com.tokopedia.sellerorder.requestpickup.data.model.ScheduleTime
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.sellerorder.requestpickup.di.SomConfirmReqPickupComponent
import com.tokopedia.sellerorder.requestpickup.presentation.adapter.SomConfirmReqPickupCourierNotesAdapter
import com.tokopedia.sellerorder.requestpickup.presentation.adapter.SomConfirmSchedulePickupAdapter
import com.tokopedia.sellerorder.requestpickup.presentation.viewmodel.SomConfirmReqPickupViewModel
import com.tokopedia.sellerorder.requestpickup.util.DateMapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupFragment : BaseDaggerFragment(), SomConfirmSchedulePickupAdapter.SomConfirmSchedulePickupAdapterListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var currOrderId = ""
    private var currSchedulePickupKey = ""
    private var currSchedulePickupTime = ""
    private var confirmReqPickupResponse = SomConfirmReqPickup.Data.MpLogisticPreShipInfo()
    private var processReqPickupResponse = SomProcessReqPickup.Data.MpLogisticRequestPickup()
    private lateinit var confirmReqPickupCourierNotesAdapter: SomConfirmReqPickupCourierNotesAdapter

    private var bottomSheetSchedulePickup: BottomSheetUnify? = null
    private var bottomSheetSchedulePickupTodayAdapter = SomConfirmSchedulePickupAdapter(this)
    private var bottomSheetSchedulePickupTomorrowAdapter = SomConfirmSchedulePickupAdapter(this)
    private var rvSchedulePickupToday: RecyclerView? = null
    private var rvSchedulePickupTomorrow: RecyclerView? = null
    private var tvSchedulePickupToday: Typography? = null
    private var tvSchedulePickupTomorrow: Typography? = null
    private var dividerSchedulePickup: View? = null
    private var isFirstVisit: Boolean? = true
    private val today: String = "Hari ini"
    private val tomorrow: String = "Besok"

    private val somConfirmRequestPickupViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomConfirmReqPickupViewModel::class.java]
    }

    private val binding by viewBinding(FragmentSomConfirmReqPickupBinding::bind)

    companion object {
        private const val ERROR_GET_CONFIRM_REQUEST_PICKUP_DATA = "Error when get confirm request pickup layout data."
        private const val ERROR_PROCESSING_REQUEST_PICKUP = "Error when processing request pickup."

        @JvmStatic
        fun newInstance(bundle: Bundle): SomConfirmReqPickupFragment {
            return SomConfirmReqPickupFragment().apply {
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
        return inflater.inflate(R.layout.fragment_som_confirm_req_pickup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        observingConfirmReqPickup()
    }

    override fun onResume() {
        super.onResume()
        updateShopActive()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomConfirmReqPickupComponent::class.java).inject(this)
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
        somConfirmRequestPickupViewModel.loadConfirmRequestPickup(GraphqlHelper.loadRawString(resources, R.raw.gql_som_confirm_request_pickup), reqPickupParam)
    }

    private fun processReqPickup() {
        val processReqPickupParam = SomProcessReqPickupParam(orderId = currOrderId, schedulePickupTime = currSchedulePickupKey)
        somConfirmRequestPickupViewModel.processRequestPickup(GraphqlHelper.loadRawString(resources, R.raw.gql_som_process_req_pickup), processReqPickupParam)
    }

    private fun observingConfirmReqPickup() {
        somConfirmRequestPickupViewModel.confirmReqPickupResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    confirmReqPickupResponse = it.data.mpLogisticPreShipInfo
                    renderConfirmReqPickup()
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(
                        it.throwable,
                        ERROR_GET_CONFIRM_REQUEST_PICKUP_DATA
                    )
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_REQUEST_PICKUP_DATA_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    context?.run {
                        Utils.showToasterError(SomErrorHandler.getErrorMessage(it.throwable, this), view)
                    }
                }
            }
        }
    }

    private fun observingProcessReqPickup() {
        somConfirmRequestPickupViewModel.processReqPickupResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    processReqPickupResponse = it.data.mpLogisticRequestPickup
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_PROCESS_REQ_PICKUP, processReqPickupResponse)
                    })
                    activity?.finish()

                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(
                        it.throwable,
                        ERROR_PROCESSING_REQUEST_PICKUP
                    )
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = it.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.REQUEST_PICKUP_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    context?.run {
                        Utils.showToasterError(SomErrorHandler.getErrorMessage(it.throwable, this), view)
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
            if (confirmReqPickupResponse.dataSuccess.detail.listShippers.isNotEmpty()) {
                val shipper = confirmReqPickupResponse.dataSuccess.detail.listShippers[0]
                ivCourier.loadImageWithoutPlaceholder(shipper.courierImg)

                context?.let {
                    val htmlCourierNameService = HtmlLinkHelper(it, "<b>${shipper.name}</b> ${shipper.service}")
                    tvCourierNameService.text = htmlCourierNameService.spannedString

                    val htmlCourierCountService = HtmlLinkHelper(it, "${shipper.countText} <b>${shipper.count}</b>")
                    tvCourierCount.text = htmlCourierCountService.spannedString
                }

                if(confirmReqPickupResponse.dataSuccess.detail.orchestraPartner.isEmpty()) {
                    tvCourierNotes.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
                } else {
                    tvCourierNotes.text = getString(R.string.courier_option_schedule, confirmReqPickupResponse.dataSuccess.detail.orchestraPartner)
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

                confirmReqPickupCourierNotesAdapter.listCourierNotes = confirmReqPickupResponse.dataSuccess.notes.listNotes.toMutableList()
                confirmReqPickupCourierNotesAdapter.notifyDataSetChanged()

            } else {
                labelPastikan.visibility = View.GONE
                rvCourierNotes.visibility = View.GONE
            }

            if (confirmReqPickupResponse.dataSuccess.schedule_time.today.isNotEmpty() || confirmReqPickupResponse.dataSuccess.schedule_time.tomorrow.isNotEmpty()) {
                val schedulePickupMapper = SchedulePickupMapper()

                rlSchedulePickup.visibility = View.VISIBLE
                btnArrow.visibility = View.GONE
                pickupNow.centerText = true
                pickupSchedule.centerText = true
                tvSchedule.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
                setActiveChips(pickupNow, pickupSchedule)
                bottomSheetSchedulePickupTodayAdapter.setData(schedulePickupMapper.mapSchedulePickup(confirmReqPickupResponse.dataSuccess.schedule_time.today, today), currSchedulePickupKey)
                bottomSheetSchedulePickupTomorrowAdapter.setData(schedulePickupMapper.mapSchedulePickup(confirmReqPickupResponse.dataSuccess.schedule_time.tomorrow, tomorrow), currSchedulePickupKey)

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
                            tvSchedule.text =  "${bottomSheetSchedulePickupTodayAdapter.scheduleTime[0].day}, $formattedTime"
                        } else {
                            startTime = DateMapper.formatDate(bottomSheetSchedulePickupTomorrowAdapter.scheduleTime[0].start)
                            endTime = DateMapper.formatDate(bottomSheetSchedulePickupTomorrowAdapter.scheduleTime[0].end)
                            formattedTime = "$startTime - $endTime WIB"
                            tvSchedule.text =  "${bottomSheetSchedulePickupTomorrowAdapter.scheduleTime[0].day}, $formattedTime"
                        }
                        isFirstVisit = false
                    } else {
                        tvSchedule.text =  currSchedulePickupTime
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
                SomAnalytics.eventClickRequestPickupPopup()
                processReqPickup()
                observingProcessReqPickup()
            }
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

}
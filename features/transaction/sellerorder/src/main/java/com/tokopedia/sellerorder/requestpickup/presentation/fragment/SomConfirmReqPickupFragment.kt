package com.tokopedia.sellerorder.requestpickup.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.requestpickup.data.mapper.SchedulePickupMapper
import com.tokopedia.sellerorder.requestpickup.data.model.*
import com.tokopedia.sellerorder.requestpickup.di.SomConfirmReqPickupComponent
import com.tokopedia.sellerorder.requestpickup.presentation.adapter.SomConfirmReqPickupCourierNotesAdapter
import com.tokopedia.sellerorder.requestpickup.presentation.adapter.SomConfirmSchedulePickupAdapter
import com.tokopedia.sellerorder.requestpickup.presentation.viewmodel.SomConfirmReqPickupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_confirm_req_pickup.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupFragment : BaseDaggerFragment(), SomConfirmSchedulePickupAdapter.SomConfirmSchedulePickupAdapterListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var currOrderId = ""
    private var currSchedulePickupTime = ""
    private var confirmReqPickupResponse = SomConfirmReqPickup.Data.MpLogisticPreShipInfo()
    private var processReqPickupResponse = SomProcessReqPickup.Data.MpLogisticRequestPickup()
    private lateinit var confirmReqPickupCourierNotesAdapter: SomConfirmReqPickupCourierNotesAdapter

    private var bottomSheetSchedulePickup: BottomSheetUnify? = null
    private var bottomSheetSchedulePickupAdapter = SomConfirmSchedulePickupAdapter(this)
    private var rvSchedulePickup: RecyclerView? = null

    private val somConfirmRequestPickupViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomConfirmReqPickupViewModel::class.java]
    }

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
        observingConfirmReqPickup()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomConfirmReqPickupComponent::class.java).inject(this)
    }

    private fun loadConfirmRequestPickup() {
        val order = SomConfirmReqPickupParam.Order(orderId = currOrderId)
        val reqPickupParam = SomConfirmReqPickupParam().apply { orderList.add(order) }
        somConfirmRequestPickupViewModel.loadConfirmRequestPickup(GraphqlHelper.loadRawString(resources, R.raw.gql_som_confirm_request_pickup), reqPickupParam)
    }

    private fun processReqPickup() {
        val processReqPickupParam = SomProcessReqPickupParam(orderId = currOrderId, schedulePickupTime = currSchedulePickupTime)
        somConfirmRequestPickupViewModel.processRequestPickup(GraphqlHelper.loadRawString(resources, R.raw.gql_som_process_req_pickup), processReqPickupParam)
    }

    private fun observingConfirmReqPickup() {
        somConfirmRequestPickupViewModel.confirmReqPickupResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    confirmReqPickupResponse = it.data.mpLogisticPreShipInfo
                    renderConfirmReqPickup()
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_CONFIRM_REQUEST_PICKUP_DATA)
                    Utils.showToasterError(it.throwable.localizedMessage, view)
                }
            }
        })
    }

    private fun observingProcessReqPickup() {
        somConfirmRequestPickupViewModel.processReqPickupResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    processReqPickupResponse = it.data.mpLogisticRequestPickup
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_PROCESS_REQ_PICKUP, processReqPickupResponse)
                    })
                    activity?.finish()

                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_PROCESSING_REQUEST_PICKUP)
                    Utils.showToasterError(it.throwable.localizedMessage, view)
                }
            }
        })
    }

    private fun renderConfirmReqPickup() {
        shop_address?.text = confirmReqPickupResponse.dataSuccess.pickupLocation.address
        shop_phone?.text = confirmReqPickupResponse.dataSuccess.pickupLocation.phone
        if (confirmReqPickupResponse.dataSuccess.detail.listShippers.isNotEmpty()) {
            val shipper = confirmReqPickupResponse.dataSuccess.detail.listShippers[0]
            iv_courier?.loadImageWithoutPlaceholder(shipper.courierImg)

            context?.let {
                val htmlCourierNameService = HtmlLinkHelper(it, "<b>${shipper.name}</b> ${shipper.service}")
                tv_courier_name_service?.text = htmlCourierNameService.spannedString

                val htmlCourierCountService = HtmlLinkHelper(it, "${shipper.countText} <b>${shipper.count}</b>")
                tv_courier_count?.text = htmlCourierCountService.spannedString
            }
            tv_courier_notes?.text = confirmReqPickupResponse.dataSuccess.detail.orchestraPartner
        }

        if (confirmReqPickupResponse.dataSuccess.notes.listNotes.isNotEmpty()) {
            confirmReqPickupCourierNotesAdapter = SomConfirmReqPickupCourierNotesAdapter()
            label_pastikan?.visibility = View.VISIBLE
            rv_courier_notes?.visibility = View.VISIBLE
            rv_courier_notes?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = confirmReqPickupCourierNotesAdapter
            }

            confirmReqPickupCourierNotesAdapter.listCourierNotes = confirmReqPickupResponse.dataSuccess.notes.listNotes.toMutableList()
            confirmReqPickupCourierNotesAdapter.notifyDataSetChanged()

        } else {
            label_pastikan?.visibility = View.GONE
            rv_courier_notes?.visibility = View.GONE
        }

        if (confirmReqPickupResponse.dataSuccess.schedule_time.today.isNotEmpty() || confirmReqPickupResponse.dataSuccess.schedule_time.tomorrow.isNotEmpty()) {
            rl_schedule_pickup?.visibility = View.VISIBLE
            pickup_now?.centerText = true
            pickup_schedule?.centerText = true
            tv_schedule?.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
            setActiveChips(pickup_now, pickup_schedule)

            pickup_now?.setOnClickListener {
                setActiveChips(pickup_now, pickup_schedule)
                btn_arrow?.visibility = View.GONE
                divider_schedule?.visibility = View.GONE
                tv_schedule?.text = confirmReqPickupResponse.dataSuccess.detail.listShippers[0].note
                currSchedulePickupTime = ""
            }

            pickup_schedule?.setOnClickListener {
                setActiveChips(pickup_schedule, pickup_now)
                btn_arrow?.visibility = View.VISIBLE
                divider_schedule?.visibility = View.VISIBLE
                currSchedulePickupTime = confirmReqPickupResponse.dataSuccess.schedule_time.today[0].keyToday
                btn_arrow.setOnClickListener {
                    val schedulePickupMapper = SchedulePickupMapper()
                    bottomSheetSchedulePickupAdapter.setData(schedulePickupMapper.mapSchedulePickup(confirmReqPickupResponse.dataSuccess.schedule_time))
                    openBottomSheetSchedulePickup()
                }
            }

        } else {
            rl_schedule_pickup?.visibility = View.GONE
        }

        btn_req_pickup?.setOnClickListener {
            SomAnalytics.eventClickRequestPickupPopup()
            processReqPickup()
            observingProcessReqPickup()
        }
    }

    private fun openBottomSheetSchedulePickup() {
        bottomSheetSchedulePickup = BottomSheetUnify()
        bottomSheetSchedulePickup?.setTitle(getString(R.string.som_request_pickup_bottomsheet_title))
        val viewBottomSheetSchedulePickup = View.inflate(context, R.layout.bottomsheet_schedule_pickup, null)
        setupChild(viewBottomSheetSchedulePickup)

        bottomSheetSchedulePickup?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetSchedulePickup)
            setOnDismissListener { dismiss() }
        }

        childFragmentManager.let {
            bottomSheetSchedulePickup?.show(it, "show")
        }
    }

    private fun setupChild(child: View) {
        rvSchedulePickup = child.findViewById(R.id.rv_schedule_pickup)

        rvSchedulePickup?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = bottomSheetSchedulePickupAdapter
        }
    }


    private fun setActiveChips(selected: ChipsUnify?, deselected: ChipsUnify?) {
        selected?.chipType = ChipsUnify.TYPE_SELECTED
        deselected?.chipType = ChipsUnify.TYPE_NORMAL
    }

    override fun onSchedulePickupTodayClicked(today: Today) {
        bottomSheetSchedulePickup?.dismiss()
        currSchedulePickupTime = today.keyToday
    }

    override fun onSchedulePickupTomorrowClicked(tomorrow: Tomorrow) {
        bottomSheetSchedulePickup?.dismiss()
        currSchedulePickupTime = tomorrow.keyTomorrow
    }
}
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_PROCESS_REQ_PICKUP
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.sellerorder.requestpickup.di.SomConfirmReqPickupComponent
import com.tokopedia.sellerorder.requestpickup.presentation.adapter.SomConfirmReqPickupCourierNotesAdapter
import com.tokopedia.sellerorder.requestpickup.presentation.viewmodel.SomConfirmReqPickupViewModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_confirm_req_pickup.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var currOrderId = ""
    private var confirmReqPickupResponse = SomConfirmReqPickup.Data.MpLogisticPreShipInfo()
    private var processReqPickupResponse = SomProcessReqPickup.Data.MpLogisticRequestPickup()
    private lateinit var confirmReqPickupCourierNotesAdapter: SomConfirmReqPickupCourierNotesAdapter

    private val somConfirmRequestPickupViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomConfirmReqPickupViewModel::class.java]
    }

    companion object {
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
        val processReqPickupParam = SomProcessReqPickupParam(orderId = currOrderId)
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
                    // quick_filter?.visibility = View.GONE
                }
            }
        })
    }

    private fun observingProcessReqPickup() {
        somConfirmRequestPickupViewModel.processReqPickupResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    processReqPickupResponse = it.data.data.mpLogisticRequestPickup
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_PROCESS_REQ_PICKUP, processReqPickupResponse)
                    })
                    activity?.finish()

                }
                is Fail -> {
                    showToasterError(it.throwable.localizedMessage)
                }
            }
        })
    }

    private fun renderConfirmReqPickup() {
        shop_address?.text = confirmReqPickupResponse.dataSuccess.pickupLocation.address
        shop_phone?.text = confirmReqPickupResponse.dataSuccess.pickupLocation.phone
        val shipper = confirmReqPickupResponse.dataSuccess.detail.listShippers[0]
        iv_courier?.loadImageWithoutPlaceholder(shipper.courierImg)

        context?.let {
            val htmlCourierNameService = HtmlLinkHelper(it, "<b>${shipper.name}</b> ${shipper.service}")
            tv_courier_name_service?.text = htmlCourierNameService.spannedString

            val htmlCourierCountService = HtmlLinkHelper(it, "${shipper.countText} <b>${shipper.count}</b>")
            tv_courier_count?.text = htmlCourierCountService.spannedString
        }
        tv_courier_notes?.text = shipper.note

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

        btn_req_pickup?.setOnClickListener {
            processReqPickup()
            observingProcessReqPickup()
        }
    }

    private fun showToasterError(message: String) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, SomConsts.ACTION_OK)
        }
    }
}
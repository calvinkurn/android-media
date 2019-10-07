package com.tokopedia.sellerorder.detail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_HEADER_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PAYMENT_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_PRODUCTS_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.DETAIL_SHIPPING_TYPE
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RECEIVER_NOTES_COLON
import com.tokopedia.sellerorder.common.util.SomConsts.RECEIVER_NOTES_END
import com.tokopedia.sellerorder.common.util.SomConsts.RECEIVER_NOTES_START
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.di.SomDetailComponent
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.viewmodel.SomDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_detail.*
import java.util.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var orderId = ""
    private var detailResponse = SomDetailOrder.Data.GetSomDetail()
    private var listDetailData: ArrayList<SomDetailData> = arrayListOf()
    private lateinit var somDetailAdapter: SomDetailAdapter

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
    }

    private fun prepareLayout() {
        somDetailAdapter = SomDetailAdapter()
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
                detailResponse.listLabelInfo)

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
                notesValue)

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
    }
}
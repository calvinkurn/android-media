package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.data.model.TitleText
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent
import com.tokopedia.hotel.orderdetail.presentation.adapter.TitleTextAdapter
import com.tokopedia.hotel.orderdetail.presentation.viewmodel.HotelOrderDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.layout_order_detail_transaction_detail.*
import javax.inject.Inject

/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var orderDetailViewModel: HotelOrderDetailViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(HotelOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            orderDetailViewModel = viewModelProvider.get(HotelOrderDetailViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderDetailViewModel.orderDetailData.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderTransactionDetail(it.data)
                }
                is Fail -> {
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderDetailViewModel.getOrderDetail("")
    }

    fun renderTransactionDetail(orderDetail: HotelOrderDetail) {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        transaction_status.text = orderDetail.status.statusText

        var transactionDetailAdapter = TitleTextAdapter()
        transaction_detail_title_recycler_view.layoutManager = layoutManager
        transaction_detail_title_recycler_view.adapter = transactionDetailAdapter
        for (transactionDetails in orderDetail.title) {
            transactionDetailAdapter.addData(TitleText(transactionDetails.label, transactionDetails.value))
            transactionDetailAdapter.notifyDataSetChanged()
        }

        invoice_number.text = orderDetail.invoice.invoiceRefNum

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_order_detail, container, false)


    companion object {
        fun getInstance(): HotelOrderDetailFragment = HotelOrderDetailFragment()
    }
}
package com.tokopedia.salam.umrah.orderdetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.adapter.UmrahSimpleAdapter
import com.tokopedia.salam.umrah.common.presentation.adapter.UmrahSimpleDetailAdapter
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsMetaDataEntity
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_umrah_order_detail.*
import kotlinx.android.synthetic.main.partial_umrah_order_detail_content.*
import kotlinx.android.synthetic.main.partial_umrah_order_detail_header.*
import javax.inject.Inject

/**
 * @author by furqan on 08/10/2019
 */
class UmrahOrderDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var umrahOrderDetailViewModel: UmrahOrderDetailViewModel

    lateinit var orderId: String

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = savedInstanceState?.getString(EXTRA_ORDER_ID)
                ?: arguments?.getString(EXTRA_ORDER_ID) ?: ""

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            umrahOrderDetailViewModel = viewModelProvider.get(UmrahOrderDetailViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_order_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        umrahOrderDetailViewModel.orderDetailData.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderData(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        umrahOrderDetailViewModel.getOrderDetail(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_order_detail),
                orderId,
                GraphqlHelper.loadRawString(resources, R.raw.dummy_order_detail)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (::orderId.isInitialized) {
            outState.putString(EXTRA_ORDER_ID, orderId)
        }
    }

    fun showLoading() {
        umrah_order_detail_content_container.visibility = View.GONE
        pb_umrah_order_detail.visibility = View.VISIBLE
    }

    fun hideLoading() {
        umrah_order_detail_content_container.visibility = View.VISIBLE
        pb_umrah_order_detail.visibility = View.GONE
    }

    private fun renderData(data: UmrahOrderDetailsEntity) {
        // header section
        tg_status_label.text = data.status.statusLabel
        tg_status_detail.text = data.status.statusText
        if (data.status.textColor.isNotEmpty()) {
            tg_status_detail.setTextColor(Color.parseColor(data.status.textColor))
        }
        tg_invoice_id.text = data.invoice.invoiceRefNum
        tg_invoice_button.setOnClickListener {
            RouteManager.route(context, data.invoice.invoiceUrl)
        }
        val titleLayoutManager = LinearLayoutManager(context)
        val titleAdapter = UmrahSimpleAdapter()
        titleAdapter.setData(umrahOrderDetailViewModel.transformToSimpleModel(data.title))
        rv_header_detail.layoutManager = titleLayoutManager
        rv_header_detail.adapter = titleAdapter

        // content section
        val detailLayoutManager = LinearLayoutManager(context)
        val detailAdapter = UmrahSimpleDetailAdapter()
        detailAdapter.setData(umrahOrderDetailViewModel.transformToSimpleDetailModel(data.details))
        rv_umrah_details.layoutManager = detailLayoutManager
        rv_umrah_details.adapter = detailAdapter

        if (data.items.isNotEmpty()) {
            val itemData = data.items[0]
            val metaData = Gson().fromJson(itemData.metadata, UmrahOrderDetailsMetaDataEntity::class.java)

            tg_umrah_package.text = itemData.title
            tg_umrah_travel.text = metaData.travelAgent
            tg_booking_code.text = metaData.bookingCode
        }

        // footer section

    }

    companion object {

        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"

        fun getInstance(orderId: String): UmrahOrderDetailFragment = UmrahOrderDetailFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_ORDER_ID, orderId)
            }
        }

    }

}
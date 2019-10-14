package com.tokopedia.salam.umrah.orderdetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsMetaDataEntity
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        // content section
        if (data.items.isNotEmpty()) {
            val itemData = data.items[0]
            val metaData = Gson().fromJson(itemData.metadata, UmrahOrderDetailsMetaDataEntity::class.java)

            tg_umrah_package.text = itemData.title
            tg_umrah_travel.text = metaData.travelAgent
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
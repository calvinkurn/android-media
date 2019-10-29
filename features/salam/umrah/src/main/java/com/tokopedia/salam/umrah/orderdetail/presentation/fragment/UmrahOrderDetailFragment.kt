package com.tokopedia.salam.umrah.orderdetail.presentation.fragment


import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.adapter.UmrahSimpleAdapter
import com.tokopedia.salam.umrah.common.presentation.adapter.UmrahSimpleDetailAdapter
import com.tokopedia.salam.umrah.common.presentation.model.MyUmrahWidgetModel
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsMetaDataEntity
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailComponent
import com.tokopedia.salam.umrah.orderdetail.presentation.adapter.UmrahOrderDetailButtonAdapter
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailButtonViewModel
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_umrah_order_detail.*
import kotlinx.android.synthetic.main.partial_umrah_order_detail_content.*
import kotlinx.android.synthetic.main.partial_umrah_order_detail_footer.*
import kotlinx.android.synthetic.main.partial_umrah_order_detail_header.*
import javax.inject.Inject

/**
 * @author by furqan on 08/10/2019
 */
class UmrahOrderDetailFragment : BaseDaggerFragment(), UmrahOrderDetailButtonAdapter.Listener {

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
                    hideLoading()
                    renderData(it.data)
                }
                is Fail -> {
                    val data = it.throwable
                    view?.let {
                        pb_umrah_order_detail.visibility = View.GONE
                        Toaster.showErrorWithAction(it, data.message
                                ?: "", Snackbar.LENGTH_LONG, "OK", View.OnClickListener { /* do nothing */ })
                    }
                }
            }
        })

        umrahOrderDetailViewModel.myWidgetData.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderMyUmrahWidget(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        umrahOrderDetailViewModel.getOrderDetail(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_order_detail),
                orderId,
                GraphqlHelper.loadRawString(resources, R.raw.dummy_order_detail)
        )
        umrahOrderDetailViewModel.getMyUmrahWidget(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_umrah_saya_by_order_id),
                orderId,
                GraphqlHelper.loadRawString(resources, R.raw.dummy_my_umrah_by_order_id)
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
        titleAdapter.setData(umrahOrderDetailViewModel.transformToSimpleModel(detailsData = data.title))
        rv_header_detail.layoutManager = titleLayoutManager
        rv_header_detail.adapter = titleAdapter

        // content section
        val detailLayoutManager = LinearLayoutManager(context)
        val detailAdapter = UmrahSimpleDetailAdapter()
        detailAdapter.setData(umrahOrderDetailViewModel.transformToSimpleDetailModel(data.details))
        rv_umrah_order_details.layoutManager = detailLayoutManager
        rv_umrah_order_details.adapter = detailAdapter

        if (data.items.isNotEmpty()) {
            val metaData = Gson().fromJson(data.items[0].metadata, UmrahOrderDetailsMetaDataEntity::class.java)
            tg_umrah_order_package.text = data.items[0].title
            tg_umrah_order_travel.text = metaData.travelAgent
            tg_booking_code.text = metaData.bookingCode
            btn_show_detail_booking.setOnClickListener {
                RouteManager.route(context, data.items[0].productUrl)
            }
        }

        val jamaahLayoutManager = LinearLayoutManager(context)
        val jamaahAdapter = UmrahSimpleAdapter()
        jamaahAdapter.isTitleBold = true
        jamaahAdapter.setData(umrahOrderDetailViewModel.transformToSimpleModel(passengersData = data.passenger))
        rv_jamaah.layoutManager = jamaahLayoutManager
        rv_jamaah.adapter = jamaahAdapter

        // footer section
        val payMethodLayoutManager = LinearLayoutManager(context)
        val payMethodAdapter = UmrahSimpleAdapter()
        payMethodAdapter.setData(umrahOrderDetailViewModel.transformToSimpleModel(valueLabelData = data.payMethod))
        rv_payment_method.layoutManager = payMethodLayoutManager
        rv_payment_method.adapter = payMethodAdapter

        val pricingLayoutManager = LinearLayoutManager(context)
        val pricingAdapter = UmrahSimpleAdapter()
        pricingAdapter.setData(umrahOrderDetailViewModel.transformToSimpleModel(detailsData = data.pricing))
        rv_pricing.layoutManager = pricingLayoutManager
        rv_pricing.adapter = pricingAdapter

        val paymentLayoutManager = LinearLayoutManager(context)
        val paymentAdapter = UmrahSimpleAdapter()
        paymentAdapter.setData(umrahOrderDetailViewModel.transformToSimpleModel(detailsData = data.paymentsData))
        rv_payment_data.layoutManager = paymentLayoutManager
        rv_payment_data.adapter = paymentAdapter

        val buttonLayoutManager = LinearLayoutManager(context)
        val buttonAdapter = UmrahOrderDetailButtonAdapter(this)
        buttonAdapter.setData(umrahOrderDetailViewModel.transformToButtonModel(data.actionButtons))
        rv_action_button.layoutManager = buttonLayoutManager
        rv_action_button.adapter = buttonAdapter

        tg_umrah_contact_us.text = getTextFromHtml(data.contactUs.helpText)
        tg_umrah_contact_us.setOnClickListener {
            RouteManager.route(context, data.contactUs.helpUrl)
        }

    }

    private fun renderMyUmrahWidget(data: MyUmrahWidgetModel) {
        if (data.header.isNotEmpty()) {
            my_umrah_widget.myUmrahModel = data
            my_umrah_widget.buildView()
        } else {
            my_umrah_widget.visibility = View.GONE
        }
    }

    override fun onItemClicked(buttonViewModel: UmrahOrderDetailButtonViewModel, position: Int) {
        RouteManager.route(context, buttonViewModel.buttonLink)
    }

    private fun getTextFromHtml(htmlText: String): CharSequence =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(htmlText)
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
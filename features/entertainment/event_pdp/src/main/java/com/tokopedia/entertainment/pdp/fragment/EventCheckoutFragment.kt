package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_META_DATA
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_PACKAGE_ID
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.adapter.EventCheckoutPriceAdapter
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil.getDateString
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventMetaDataMapper.getCheckoutParam
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventMetaDataMapper.getPassengerMetaData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getItemMap
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventCheckoutViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oms.scrooge.ScroogePGUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_event_checkout.view.*
import kotlinx.android.synthetic.main.fragment_event_checkout.*
import kotlinx.android.synthetic.main.partial_event_checkout_desc.*
import kotlinx.android.synthetic.main.partial_event_checkout_footer.*
import kotlinx.android.synthetic.main.partial_event_checkout_passenger.*
import kotlinx.android.synthetic.main.partial_event_checkout_summary.*
import kotlinx.android.synthetic.main.widget_event_checkout_passenger.*
import java.io.Serializable
import javax.inject.Inject


class EventCheckoutFragment : BaseDaggerFragment() {

    private var urlPDP: String = ""
    private var metadata = MetaDataResponse()
    private var amount: Int = 0
    private var packageID : String = ""

    private var name: String = ""
    private var email: String = ""
    private var promoCode: String = ""

    private var forms: List<Form> = emptyList()

    lateinit var performanceMonitoring: PerformanceMonitoring

    @Inject
    lateinit var eventCheckoutViewModel: EventCheckoutViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    lateinit var progressDialog: ProgressDialog


    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(ENT_CHECKOUT_PERFORMANCE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        arguments?.let {
            urlPDP = it.getString(EXTRA_URL_PDP, "")
            metadata = it.getParcelable(EXTRA_META_DATA)
            packageID = it.getString(EXTRA_PACKAGE_ID, "")
        }
    }

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_event_checkout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eventCheckoutViewModel.eventProductDetail.observe(this, Observer {
            it.run {
                renderLayout(it)
                performanceMonitoring.stopTrace()
            }
        })

        eventCheckoutViewModel.isError.observe(this, Observer {
            it?.let {
                if (it.error) {
                    progressDialog.dismiss()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView) {
                        requestData()
                    }
                }
            }
        })

        eventCheckoutViewModel.errorValue.observe(this, Observer {
            it?.let {
                val error = it
                view?.let {
                    progressDialog.dismiss()
                    Toaster.make(it, error, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.context.getString(R.string.ent_checkout_error))
                }
            }
        })

        eventCheckoutViewModel.errorGeneralValue.observe(this, Observer {
            it?.let {
                val error = it
                view?.let {
                    progressDialog.dismiss()
                    Toaster.make(it, ErrorHandler.getErrorMessage(context, error), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                            it.context.getString(R.string.ent_checkout_error))
                }
            }
        })

        eventCheckoutViewModel.eventCheckoutResponse.observe(this, Observer {
            it?.let {
                val data = it
                context?.let {
                    progressDialog.dismiss()

                    val paymentData = data.checkout.data.data.queryString
                    val paymentURL: String = data.checkout.data.data.redirectUrl
                    ScroogePGUtil.openScroogePage(activity, paymentURL, true, paymentData, it.resources.getString(R.string.pembayaran))
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        requestData()
    }

    private fun requestData() {
        urlPDP.let {
            eventCheckoutViewModel.getDataProductDetail(GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_product_detail_v3),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_content_by_id), it,GraphqlHelper.loadRawString(resources, R.raw.dummy_response))
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.ent_checkout_payment))
        progressDialog.setCancelable(false)
    }

    private fun renderLayout(eventProductDetailEntity: EventProductDetailEntity) {
        pg_event_checkout.gone()
        container_checkout.show()

        renderDesc(eventProductDetailEntity.eventProductDetail.productDetailData)
        renderPassenger()
        renderSummary(eventProductDetailEntity.eventProductDetail.productDetailData)
        renderFooter()

    }

    private fun renderDesc(pdp: ProductDetailData) {
        tg_event_checkout_date.text = getDateString(DATE_FORMAT, getItemMap(metadata).scheduleTimestamp.toInt())
        tg_event_checkout_name.text = pdp.displayName
        tg_event_checkout_packet.text = getPackage(pdp, packageID).name
        iv_event_checkout_image.loadImageRounded(pdp.imageApp, 25f)
    }

    private fun renderPassenger() {
        btn_event_checkout_passenger.setOnClickListener {
            context?.run {
                val intent = RouteManager.getIntent(this, "${ApplinkConstInternalEntertainment.EVENT_FORM}/$urlPDP")
                intent.putExtra(EXTRA_DATA_PESSANGER, forms as Serializable)
                startActivityForResult(intent, REQUEST_CODE_FORM)
            }
        }
    }

    private fun renderSummary(pdp: ProductDetailData) {
        val adapterItemPrice = EventCheckoutPriceAdapter()
        adapterItemPrice.setList(metadata.itemMap)

        rv_event_checkout_price.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterItemPrice
        }


        tg_event_checkout_summary_price_price.text = getRupiahFormat(metadata.totalPrice)

        context?.let {
            tg_event_checkout_tnc.makeLinks(
                    Pair(getString(R.string.ent_event_checkout_summary_tnc_click), View.OnClickListener {
                        showBottomSheetTnc(it.context, pdp.tnc)
                    })
            )
        }

//        eventPDPTracking.onViewCheckoutPage(getPackage(scheduleID, groupID, packetID, pdp), pdp, amount)
    }

    private fun renderFooter() {
        cb_event_checkout.setOnCheckedChangeListener { _, isChecked ->
            btn_event_checkout.isEnabled = isChecked
        }

        btn_event_checkout.setOnClickListener {

            view?.let {
                val view = it
                context?.let {
                    if (!userSessionInterface.isLoggedIn) {
                        Toaster.make(view, it.getString(R.string.ent_event_checkout_submit_login), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error))
                    } else if (forms.isEmpty()) {
                        Toaster.make(view, it.getString(R.string.ent_event_checkout_submit_name), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error))
                    } else {
                        progressDialog.show()
                        // eventPDPTracking.onClickCheckoutButton(getPackage(scheduleID, groupID, packetID, productDetailData), productDetailData, amount)
                        if (name.isEmpty()) name = userSessionInterface.name
                        if (email.isEmpty()) email = userSessionInterface.email
                        metadata = getPassengerMetaData(metadata, forms)
                        eventCheckoutViewModel.checkoutEvent(GraphqlHelper.loadRawString(resources, R.raw.gql_mutation_event_checkout_v2),
                                getCheckoutParam(metadata))
                    }
                }
            }
        }
    }

    private fun showBottomSheetTnc(context: Context, tnc:String) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_checkout, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(context.getString(R.string.ent_event_checkout_summary_tnc_bottom_sheet))
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.tg_event_checkout_tnc_bottom_sheet.apply {
            text = tnc
        }
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FORM) {
                data?.let {
                    forms = data.getSerializableExtra(EXTRA_DATA_PESSANGER) as List<Form>
                    setPassengerData(forms)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setPassengerData(list: List<Form>) {
        widget_event_checkout_pessanger.renderRecycleView(list)

        for (item in list) {
            when (item.title) {
                PASSENGER_NAME -> {
                    item.value.let {
                        name = it
                    }
                }

                PASSENGER_EMAIL -> {
                    item.value.let {
                        email = it
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    companion object {
        const val DATE_FORMAT = "EEE, d MMM yyyy"
        const val REQUEST_CODE_FORM = 100

        const val EXTRA_DATA_PESSANGER = "EXTRA_DATA_PESSANGER"

        const val PASSENGER_NAME = "fullname"
        const val PASSENGER_EMAIL = "email"

        const val ENT_CHECKOUT_PERFORMANCE = "et_event_checkout"

        fun newInstance(urlPDP: String, metadata: MetaDataResponse, packageID: String) = EventCheckoutFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, urlPDP)
                putParcelable(EXTRA_META_DATA, metadata)
                putString(EXTRA_PACKAGE_ID, packageID)
            }
        }
    }
}
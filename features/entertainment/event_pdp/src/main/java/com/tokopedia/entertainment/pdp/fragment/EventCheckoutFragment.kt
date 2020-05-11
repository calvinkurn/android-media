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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_AMOUNT
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_GROUP_ID
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_PACKET_ID
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_SCHEDULE_ID
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil.getDateString
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.Schedule
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getSchedule
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPaymentMapper.getJsonMapper
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventVerifyMapper.getEntityPessangerVerify
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventVerifyMapper.getVerifyBody
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventCheckoutViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oms.scrooge.ScroogePGUtil
import com.tokopedia.oms.view.utils.Utils
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
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
    private var scheduleID: String = ""
    private var groupID: String = ""
    private var packetID: String = ""
    private var amount: Int = 0


    private var name: String = ""
    private var email: String = ""
    private var promoCode: String = ""

    private var forms: List<Form> = emptyList()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            urlPDP = it.getString(EXTRA_URL_PDP, "")
            scheduleID = it.getString(EXTRA_SCHEDULE_ID, "")
            groupID = it.getString(EXTRA_GROUP_ID, "")
            packetID = it.getString(EXTRA_PACKET_ID, "")
            amount = it.getInt(EXTRA_AMOUNT, 0)
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

                    val paymentData = Utils.transform(getJsonMapper(data))
                    val paymentURL: String = data.data.url
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
            eventCheckoutViewModel.getDataProductDetail(GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_product_detail),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_content_by_id), it)
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
        renderFooter(eventProductDetailEntity.eventProductDetail.productDetailData)
    }

    private fun renderDesc(pdp: ProductDetailData) {
        pdp.schedules.firstOrNull()?.let {
            tg_event_checkout_date.text = getDateString(DATE_FORMAT, it.schedule.startDate.toInt())
        }
        tg_event_checkout_name.text = pdp.displayName
        tg_event_checkout_packet.text = getPackage(scheduleID, groupID, packetID, pdp).displayName
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
        val schedule: Schedule = getSchedule(scheduleID, pdp)
        tg_event_checkout_summary_packet.text = getString(R.string.ent_checkout_summary_packet,
                getPackage(scheduleID, groupID, packetID, pdp).displayName,amount)
        tg_event_checkout_summary_price.text = getRupiahFormat(getPackage(scheduleID, groupID, packetID, pdp).salesPrice.toInt() * amount)
        tg_event_checkout_summary_price_price.text = getRupiahFormat(getPackage(scheduleID, groupID, packetID, pdp).salesPrice.toInt() * amount)

        context?.let {
            tg_event_checkout_tnc.makeLinks(
                    Pair(getString(R.string.ent_event_checkout_summary_tnc_click), View.OnClickListener {
                        showBottomSheetTnc(it.context, schedule)
                    })
            )
        }

        eventPDPTracking.onViewCheckoutPage(getPackage(scheduleID, groupID, packetID, pdp), pdp, amount)
    }

    private fun renderFooter(productDetailData: ProductDetailData) {
        renderPromo(productDetailData)
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
                        eventPDPTracking.onClickCheckoutButton(getPackage(scheduleID, groupID, packetID, productDetailData), productDetailData, amount)
                        if (name.isEmpty()) name = userSessionInterface.name
                        if (email.isEmpty()) email = userSessionInterface.email
                        eventCheckoutViewModel.checkVerify(true, getVerifyMapped(productDetailData))
                    }
                }
            }
        }
    }

    private fun renderPromo(productDetailData: ProductDetailData) {

        ticker_event_checkout_promo.state = TickerPromoStackingCheckoutView.State.EMPTY
        ticker_event_checkout_promo.actionListener = object : TickerPromoStackingCheckoutView.ActionListener {
            override fun onClickDetailPromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_EVENT)
                intent.putExtra(EXTRA_IS_USE, true)
                intent.putExtra(EXTRA_KUPON_CODE, promoCode)
                intent.putExtra(EXTRA_EVENT_VERIFY, getVerifyMapped(productDetailData))

                startActivityForResult(intent, PROMO_EXTRA_LIST_ACTIVITY_RESULT)
            }

            override fun onClickUsePromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_EVENT)
                intent.putExtra(EXTRA_COUPON_ACTIVE, true)
                intent.putExtra(PAGE_TRACKING, 1)
                intent.putExtra(EXTRA_EVENT_CATEGORY_ID, productDetailData.catalog.digitalCategoryId.toInt())
                intent.putExtra(EXTRA_EVENT_VERIFY, getVerifyMapped(productDetailData))
                startActivityForResult(intent, PROMO_EXTRA_LIST_ACTIVITY_RESULT)
            }

            override fun onDisablePromoDiscount() {

            }

            override fun onResetPromoDiscount() {
                promoCode = ""
                setupPromoTicker(TickerCheckoutView.State.EMPTY,
                        "",
                        "")
            }
        }
    }

    private fun showBottomSheetTnc(context: Context, schedule: Schedule) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_checkout, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(context.getString(R.string.ent_event_checkout_summary_tnc_bottom_sheet))
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.tg_event_checkout_tnc_bottom_sheet.apply {
            text = schedule.tnc
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
            } else if (requestCode == PROMO_EXTRA_LIST_ACTIVITY_RESULT) {
                data?.let {
                    if (it.hasExtra(EXTRA_PROMO_DATA)) {
                        val itemPromoData = it.getParcelableExtra<PromoData>(EXTRA_PROMO_DATA)
                        promoCode = itemPromoData.promoCode

                        when (itemPromoData.state) {
                            TickerCheckoutView.State.EMPTY -> {
                                promoCode = ""
                                setupPromoTicker(TickerCheckoutView.State.EMPTY,
                                        "",
                                        "")
                            }
                            TickerCheckoutView.State.FAILED -> {
                                promoCode = ""
                                setupPromoTicker(TickerCheckoutView.State.FAILED,
                                        itemPromoData?.title.toEmptyStringIfNull(),
                                        itemPromoData?.description.toEmptyStringIfNull())

                            }
                            TickerCheckoutView.State.ACTIVE -> {
                                setupPromoTicker(TickerCheckoutView.State.ACTIVE,
                                        itemPromoData?.title.toEmptyStringIfNull(),
                                        itemPromoData?.description.toEmptyStringIfNull())
                            }
                            TickerCheckoutView.State.INACTIVE -> {
                                setupPromoTicker(TickerCheckoutView.State.INACTIVE,
                                        itemPromoData?.title.toEmptyStringIfNull(),
                                        itemPromoData?.description.toEmptyStringIfNull())
                            }
                            else -> {
                                promoCode = ""
                                setupPromoTicker(TickerCheckoutView.State.EMPTY,
                                        "",
                                        "")
                            }
                        }
                    }
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

    private fun setupPromoTicker(state: TickerCheckoutView.State,
                                 title: String,
                                 description: String) {
        if (state == TickerCheckoutView.State.EMPTY) {
            ticker_event_checkout_promo.title = title
            ticker_event_checkout_promo.state = TickerPromoStackingCheckoutView.State.EMPTY
        } else if (state == TickerCheckoutView.State.ACTIVE) {
            ticker_event_checkout_promo.title = title
            ticker_event_checkout_promo.desc = description
            ticker_event_checkout_promo.state = TickerPromoStackingCheckoutView.State.ACTIVE
        } else if (state == TickerCheckoutView.State.INACTIVE) {
            ticker_event_checkout_promo.title = title
            ticker_event_checkout_promo.desc = description
            ticker_event_checkout_promo.state = TickerPromoStackingCheckoutView.State.INACTIVE
        }
    }


    fun getVerifyMapped(productDetailData: ProductDetailData): EventVerifyBody {
        if (name.isEmpty()) name = userSessionInterface.name
        if (email.isEmpty()) email = userSessionInterface.email

        return getVerifyBody(name, email, groupID.toInt(), packetID.toInt(), scheduleID.toInt(), productDetailData.id.toInt(),
                productDetailData.categoryId.toInt(), productDetailData.providerId.toInt(),
                amount, getPackage(scheduleID, groupID, packetID, productDetailData).salesPrice.toInt(),
                getPackage(scheduleID, groupID, packetID, productDetailData).salesPrice.toInt() * amount,
                productDetailData.catalog.digitalProductId.toInt(),
                getEntityPessangerVerify(forms), promoCode)
    }

    companion object {
        const val DATE_FORMAT = "EEE, d MMM yyyy"
        const val REQUEST_CODE_FORM = 100
        const val PROMO_EXTRA_LIST_ACTIVITY_RESULT = 123

        const val EXTRA_DATA_PESSANGER = "EXTRA_DATA_PESSANGER"
        const val EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val PAGE_TRACKING = "PAGE_TRACKING"
        const val EXTRA_EVENT_CATEGORY_ID = "EXTRA_EVENT_CATEGORY_ID"
        const val EXTRA_EVENT_VERIFY = "EXTRA_EVENT_VERIFY"

        const val PASSENGER_NAME = "fullname"
        const val PASSENGER_EMAIL = "email"

        fun newInstance(urlPDP: String, scheduleID: String, groupID: String,
                        packetID: String, amount: Int) = EventCheckoutFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, urlPDP)
                putString(EXTRA_SCHEDULE_ID, scheduleID)
                putString(EXTRA_GROUP_ID, groupID)
                putString(EXTRA_PACKET_ID, packetID)
                putInt(EXTRA_AMOUNT, amount)
            }
        }
    }
}
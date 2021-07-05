package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.payment.PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA
import com.tokopedia.common.payment.PaymentConstant.PAYMENT_SUCCESS
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.common.util.EventQuery.eventPDPV3
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_GATEWAY_CODE
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_META_DATA
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_PACKAGE_ID
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.adapter.EventCheckoutAdditionalAdapter
import com.tokopedia.entertainment.pdp.adapter.EventCheckoutPassengerDataAdapter
import com.tokopedia.entertainment.pdp.adapter.EventCheckoutPriceAdapter
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.entertainment.pdp.common.util.EventDateUtil.getDateString
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.initialListForm
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.isEmptyForms
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.mapFormToString
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventMetaDataMapper.getCheckoutParam
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventMetaDataMapper.getCheckoutParamInstant
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventMetaDataMapper.getPassengerMetaData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getAdditionalList
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getAdditionalPackage
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getItemMap
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.listener.OnAdditionalListener
import com.tokopedia.entertainment.pdp.viewmodel.EventCheckoutViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_event_checkout.view.*
import kotlinx.android.synthetic.main.fragment_event_checkout.*
import kotlinx.android.synthetic.main.item_checkout_event_data_tambahan_package_filled.*
import kotlinx.android.synthetic.main.partial_event_checkout_additional_item.*
import kotlinx.android.synthetic.main.partial_event_checkout_additional_package.*
import kotlinx.android.synthetic.main.partial_event_checkout_desc.*
import kotlinx.android.synthetic.main.partial_event_checkout_footer.*
import kotlinx.android.synthetic.main.partial_event_checkout_passenger.*
import kotlinx.android.synthetic.main.partial_event_checkout_summary.*
import kotlinx.android.synthetic.main.widget_event_checkout_passenger.*
import java.io.Serializable
import javax.inject.Inject


class EventCheckoutFragment : BaseDaggerFragment(), OnAdditionalListener {

    private var urlPDP: String = ""
    private var metadata = MetaDataResponse()
    private var amount: Int = 0
    private var packageID: String = ""
    private var gatewayCode: String = ""

    private var name: String = ""
    private var email: String = ""
    private var promoCode: String = ""

    private var forms: List<Form> = emptyList()
    private var listAdditionalItem: MutableList<EventCheckoutAdditionalData> = mutableListOf()
    private var eventCheckoutAdditionalDataPackage: EventCheckoutAdditionalData = EventCheckoutAdditionalData()
    private val adapterAdditional = EventCheckoutAdditionalAdapter(this)
    private var isPackageFormActive = false
    private var isItemFormActive = false

    lateinit var performanceMonitoring: PerformanceMonitoring

    @Inject
    lateinit var eventCheckoutViewModel: EventCheckoutViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    lateinit var progressDialog: ProgressDialog

    private lateinit var saveInstanceManager: SaveInstanceCacheManager


    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(ENT_CHECKOUT_PERFORMANCE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        activity?.let{
            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
            if(saveInstanceManager!=null){
                saveInstanceManager.get(EXTRA_SAVED_DATA_ADDITIONAL_PACKAGE,EventCheckoutAdditionalData::class.java, EventCheckoutAdditionalData())?.let {
                    if(it.listForm.isNotEmpty()){
                        eventCheckoutAdditionalDataPackage = it
                    }
                }

                saveInstanceManager.get(EXTRA_SAVED_DATA_ADDITIONAL_ITEM, object : TypeToken<MutableList<EventCheckoutAdditionalData>>() {}.type, mutableListOf<EventCheckoutAdditionalData>())?.let {
                    if(it.isNotEmpty()) {
                        listAdditionalItem = it
                    }
                }

                saveInstanceManager.get(EXTRA_SAVED_DATA_FORM, object : TypeToken<List<Form>>() {}.type, emptyList<Form>())?.let {
                    if (it.isNotEmpty()) {
                        forms = it
                    }
                }
            }
        }
        arguments?.let {
            urlPDP = it.getString(EXTRA_URL_PDP, "")
            metadata = it.getParcelable(EXTRA_META_DATA) ?: MetaDataResponse()
            packageID = it.getString(EXTRA_PACKAGE_ID, "")
            gatewayCode = it.getString(EXTRA_GATEWAY_CODE, "")
        }
    }

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_event_checkout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eventCheckoutViewModel.eventProductDetail.observe(viewLifecycleOwner, Observer {
            it.run {
                renderLayout(it)
                performanceMonitoring.stopTrace()
            }
        })

        eventCheckoutViewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.error) {
                    progressDialog.dismiss()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView) {
                        requestData()
                    }
                }
            }
        })

        eventCheckoutViewModel.errorValue.observe(viewLifecycleOwner, Observer {
            it?.let {
                val error = it
                view?.let {
                    progressDialog.dismiss()
                    Toaster.build(it, error, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.context.getString(R.string.ent_checkout_error)).show()
                }
            }
        })

        eventCheckoutViewModel.errorGeneralValue.observe(viewLifecycleOwner, Observer {
            it?.let {
                val error = it
                view?.let {
                    progressDialog.dismiss()
                    Toaster.build(it, ErrorHandler.getErrorMessage(context, error), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                            it.context.getString(R.string.ent_checkout_error)).show()
                }
            }
        })

        eventCheckoutViewModel.eventCheckoutResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                val data = it
                context?.let {
                    progressDialog.dismiss()
                    val context = it
                    if (data.checkout.data.success == 0) {
                        view?.let {
                            Toaster.build(it, data.checkout.data.message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                                    context.getString(R.string.ent_checkout_error)).show()
                        }
                    } else {
                        val paymentData = data.checkout.data.data.queryString
                        val paymentURL: String = data.checkout.data.data.redirectUrl

                        if (!paymentData.isNullOrEmpty() || !paymentURL.isNullOrEmpty()) {

                            val checkoutResultData = PaymentPassData()
                            checkoutResultData.queryString = paymentData
                            checkoutResultData.redirectUrl = paymentURL
                            checkoutResultData.callbackSuccessUrl = ORDER_LIST_EVENT

                            val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                            val intent = RouteManager.getIntent(context, paymentCheckoutString)
                            intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, checkoutResultData)
                            startActivityForResult(intent, PAYMENT_SUCCESS)

                        } else {
                            view?.let {
                                Toaster.build(it, data.checkout.data.error, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                                        context.getString(R.string.ent_checkout_error)).show()
                            }
                        }
                    }
                }
            }
        })


        eventCheckoutViewModel.eventCheckoutInstantResponse.observe(viewLifecycleOwner, Observer {
            val data = it
            context?.let {
                progressDialog.dismiss()
                val context = it
                if (data.checkout.data.success == 0) {
                    view?.let {
                        Toaster.build(it, data.checkout.header.messages.first(), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                                context.getString(R.string.ent_checkout_error)).show()
                    }
                } else {
                    RouteManager.route(context, data.checkout.data.data.redirectUrl)
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
            eventCheckoutViewModel.getDataProductDetail(eventPDPV3(),
                    eventContentById(), it)
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
        eventProductDetailEntity.eventProductDetail.productDetailData.apply {
            renderDesc(this)
            renderPassenger(this)
            renderSummary(this)
            renderAdditionalItem(this)
            renderAdditionalPackage(this)
            renderFooter(this)
        }

    }

    private fun renderDesc(pdp: ProductDetailData) {
        tg_event_checkout_date.text = getDateString(DATE_FORMAT, getItemMap(metadata).scheduleTimestamp.toInt())
        tg_event_checkout_name.text = pdp.displayName
        tg_event_checkout_packet.text = getPackage(pdp, packageID).name
        iv_event_checkout_image.loadImageRounded(pdp.imageApp, 25f)
    }

    private fun renderPassenger(pdp: ProductDetailData) {
        if(forms.isNullOrEmpty()){
            forms = initialListForm(pdp.forms, userSessionInterface, getString(R.string.ent_checkout_data_nullable_form))
        }
        if (!forms.isNullOrEmpty()) {
            setPassengerData(forms)
        }
        ticker_event_checkout.setTextDescription(resources.getString(R.string.ent_event_checkout_pessanger_ticker))
        btn_event_checkout_passenger.setOnClickListener {
            goToPageForm()
        }
        widget_event_checkout_pessangers.setOnClickListener {
            goToPageForm()
        }
    }

    private fun goToPageForm() {
        context?.run {
            val intent = RouteManager.getIntent(this, "${ApplinkConstInternalEntertainment.EVENT_FORM}/$urlPDP")
            intent.putExtra(EXTRA_DATA_PESSANGER, forms as Serializable)
            startActivityForResult(intent, REQUEST_CODE_FORM)
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

        eventPDPTracking.onViewCheckoutPage(pdp, metadata.itemMap, userSessionInterface.userId)
    }

    private fun renderAdditionalItem(pdp: ProductDetailData) {
        if(listAdditionalItem.isEmpty()) {
            listAdditionalItem = getAdditionalList(metadata.itemMap, pdp, packageID)
        }
        if (!listAdditionalItem.isNullOrEmpty()) {
            isItemFormActive = true
            adapterAdditional.setList(listAdditionalItem)
            rv_event_checkout_additional.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = adapterAdditional
            }
        } else {
            partial_event_checkout_additional_item.gone()
        }
    }

    private fun renderAdditionalPackage(pdp: ProductDetailData) {
        if(eventCheckoutAdditionalDataPackage.listForm.isNullOrEmpty()){
            eventCheckoutAdditionalDataPackage = getAdditionalPackage(pdp, packageID)
        }
        if (eventCheckoutAdditionalDataPackage.additionalType.equals(AdditionalType.NULL_DATA)) {
            partial_event_checkout_additional_package.gone()
        } else {
            isPackageFormActive = true
            updateAdditionalPackage()
            item_checkout_event_data_tambahan_package.setOnClickListener {
                clickAdditional(eventCheckoutAdditionalDataPackage, REQUEST_CODE_ADDITIONAL_PACKAGE)
            }
            img_event_package_additional.setOnClickListener {
                clickAdditional(eventCheckoutAdditionalDataPackage, REQUEST_CODE_ADDITIONAL_PACKAGE)
            }
        }
    }

    private fun updateAdditionalPackage() {
        if (eventCheckoutAdditionalDataPackage.additionalType.equals(AdditionalType.PACKAGE_FILLED)
                && eventCheckoutAdditionalDataPackage.listForm.isNotEmpty()) {
            item_checkout_event_data_tambahan_package.gone()
            item_checkout_event_data_tambahan_package_filled.show()
            val adapter = EventCheckoutPassengerDataAdapter()
            adapter.setList(mapFormToString(eventCheckoutAdditionalDataPackage.listForm))
            rv_event_checkout_additional_package_filled.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                this.adapter = adapter
            }
            item_checkout_event_additional_package_filled.setOnClickListener {
                clickAdditional(eventCheckoutAdditionalDataPackage, REQUEST_CODE_ADDITIONAL_PACKAGE)
            }
        }

    }

    private fun renderFooter(productDetailData: ProductDetailData) {
        cb_event_checkout.setOnCheckedChangeListener { _, isChecked ->
            btn_event_checkout.isEnabled = isChecked
        }

        btn_event_checkout.setOnClickListener {

            view?.let {
                val view = it
                context?.let {
                    when{
                        !userSessionInterface.isLoggedIn -> {
                            Toaster.build(view, it.getString(R.string.ent_event_checkout_submit_login), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                        }
                        forms.isEmpty() -> {
                            Toaster.build(view, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_event_checkout_passenger_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            scroll_view_event_checkout.focusOnView(partial_event_checkout_passenger)
                            widget_event_checkout_pessangers.startAnimationWiggle()
                        }
                        !forms.isNullOrEmpty() && isEmptyForms(forms, getString(R.string.ent_checkout_data_nullable_form)) -> {
                            Toaster.build(view, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_event_checkout_passenger_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            scroll_view_event_checkout.focusOnView(partial_event_checkout_passenger)
                            widget_event_checkout_pessangers.startAnimationWiggle()
                        }
                        isAdditionalItemFormNull() && isItemFormActive -> {
                            Toaster.build(view, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_checkout_data_pengunjung_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            scroll_view_event_checkout.focusOnView(partial_event_checkout_additional_item)
                            getRecycleViewWidgetAnimator()
                        }
                        eventCheckoutAdditionalDataPackage.listForm.isEmpty() && isPackageFormActive -> {
                            Toaster.build(view, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_checkout_data_tambahan_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            scroll_view_event_checkout.focusOnView(partial_event_checkout_additional_package)
                            item_checkout_event_data_tambahan_package.startAnimationWiggle()
                        }
                        else -> {
                            progressDialog.show()
                            eventPDPTracking.onClickCheckoutButton(productDetailData, metadata.itemMap, userSessionInterface.userId)
                            metadata = getPassengerMetaData(metadata, forms, listAdditionalItem, eventCheckoutAdditionalDataPackage,
                                    it.getString(R.string.ent_checkout_data_nullable_form))
                            if(gatewayCode.isNullOrEmpty()) {
                                eventCheckoutViewModel.checkoutEvent(EventQuery.mutationEventCheckoutV2(),
                                        getCheckoutParam(metadata, productDetailData, getPackage(productDetailData, packageID)))
                            } else {
                                eventCheckoutViewModel.checkoutEventInstant(
                                        getCheckoutParamInstant(gatewayCode, metadata, productDetailData, getPackage(productDetailData, packageID)))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showBottomSheetTnc(context: Context, tnc: String) {
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
            data?.let {
                when (requestCode) {
                    REQUEST_CODE_FORM -> {
                        forms = data.getSerializableExtra(EXTRA_DATA_PESSANGER) as List<Form>
                        setPassengerData(forms)
                    }
                    REQUEST_CODE_ADDITIONAL_ITEM -> {
                        val additionalData = data.getParcelableExtra<EventCheckoutAdditionalData>(EXTRA_DATA_PESSANGER)
                        listAdditionalItem[additionalData.position] = additionalData
                        adapterAdditional.notifyDataSetChanged()
                    }
                    REQUEST_CODE_ADDITIONAL_PACKAGE -> {
                        val additionalData = data.getParcelableExtra<EventCheckoutAdditionalData>(EXTRA_DATA_PESSANGER)
                        eventCheckoutAdditionalDataPackage = additionalData
                        updateAdditionalPackage()
                    }
                }
            }
        } else if (resultCode == PAYMENT_SUCCESS) {
            val taskStackBuilder = TaskStackBuilder.create(context)
            val intentHomeEvent = RouteManager.getIntent(context, ApplinkConstInternalEntertainment.EVENT_HOME)
            taskStackBuilder.addNextIntent(intentHomeEvent)
            taskStackBuilder.startActivities()

            val intent = RouteManager.getIntent(context, ApplinkConst.EVENTS_ORDER)
            intent?.run {
                taskStackBuilder.addNextIntent(this)
                taskStackBuilder.startActivities()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setPassengerData(list: List<Form>) {
        widget_event_checkout_pessanger.renderRecycleView(list)
    }

    override fun onClickAdditional(additonal: EventCheckoutAdditionalData) {
        clickAdditional(additonal, REQUEST_CODE_ADDITIONAL_ITEM)
    }

    private fun clickAdditional(additonal: EventCheckoutAdditionalData, codeAdditional: Int) {
        context?.run {
            val intent = RouteManager.getIntent(this, "${ApplinkConstInternalEntertainment.EVENT_FORM}/$urlPDP")
            intent.putExtra(EXTRA_ADDITIONAL_DATA, additonal)
            startActivityForResult(intent, codeAdditional)
        }
    }

    private fun getRecycleViewWidgetAnimator() {
        val itemView = rv_event_checkout_additional.layoutManager?.findViewByPosition(positionAdditionalItemFormNull())
        itemView?.startAnimationWiggle()
    }

    private fun isAdditionalItemFormNull(): Boolean {
        var status = false
        loop@ for (i in 0..listAdditionalItem.size - 1) {
            if (listAdditionalItem.get(i).listForm.isNullOrEmpty()) {
                status = true
                break@loop
            }
        }
        return status
    }

    private fun positionAdditionalItemFormNull(): Int {
        var position = 0
        loop@ for (i in 0..listAdditionalItem.size - 1) {
            if (listAdditionalItem.get(i).listForm.isNullOrEmpty()) {
                position = i
                break@loop
            }
        }
        return position
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    private fun View.startAnimationWiggle() {
        this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_event_checkout_wiggle))
    }

    private fun NestedScrollView.focusOnView(toView: View) {
        Handler().post(Runnable {
            this.smoothScrollTo(0, toView.y.toInt())
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceManager?.apply {
            put(EXTRA_SAVED_DATA_ADDITIONAL_PACKAGE, eventCheckoutAdditionalDataPackage)
            put(EXTRA_SAVED_DATA_FORM, forms)
            put(EXTRA_SAVED_DATA_ADDITIONAL_ITEM, listAdditionalItem)
        }
    }


    companion object {
        const val DATE_FORMAT = "EEE, d MMM yyyy"
        const val REQUEST_CODE_FORM = 100
        const val REQUEST_CODE_ADDITIONAL_ITEM = 101
        const val REQUEST_CODE_ADDITIONAL_PACKAGE = 102

        const val EXTRA_DATA_PESSANGER = "EXTRA_DATA_PESSANGER"
        const val EXTRA_ADDITIONAL_DATA = "EXTRA_ADDITIONAL_DATA"

        const val EXTRA_SAVED_DATA_ADDITIONAL_PACKAGE = "EXTRA_SAVED_DATA_ADDITIONAL_PACKAGE"
        const val EXTRA_SAVED_DATA_ADDITIONAL_ITEM = "EXTRA_SAVED_DATA_ADDITIONAL_ITEM"
        const val EXTRA_SAVED_DATA_FORM = "EXTRA_SAVED_DATA_FORM"

        const val ENT_CHECKOUT_PERFORMANCE = "et_event_checkout"

        const val ORDER_LIST_EVENT = "/order-list"

        fun newInstance(urlPDP: String, metadata: MetaDataResponse, packageID: String, gatewayCode: String) = EventCheckoutFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, urlPDP)
                putParcelable(EXTRA_META_DATA, metadata)
                putString(EXTRA_PACKAGE_ID, packageID)
                putString(EXTRA_GATEWAY_CODE, gatewayCode)
            }
        }
    }
}
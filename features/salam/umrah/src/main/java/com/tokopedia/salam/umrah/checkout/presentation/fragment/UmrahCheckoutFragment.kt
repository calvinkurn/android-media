package com.tokopedia.salam.umrah.checkout.presentation.fragment

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
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalSalam
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.*
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_DEPART_DATE
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_DOWN_PAYMENT_PRICE
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_PRICE
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_SLUG_NAME
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_TOTAL_PASSENGER
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_VARIANT
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutContactDataActivity
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutInstallmentActivity
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutPilgrimsActivity
import com.tokopedia.salam.umrah.checkout.presentation.adapter.UmrahCheckoutMandatoryDocumentAdapter
import com.tokopedia.salam.umrah.checkout.presentation.adapter.UmrahCheckoutPilgrimsListAdapter
import com.tokopedia.salam.umrah.checkout.presentation.adapter.UmrahCheckoutSummaryListAdapter
import com.tokopedia.salam.umrah.checkout.presentation.adapter.UmrahCheckoutTermConditionAdapter
import com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder.UmrahPilgrimsEmptyViewHolder
import com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder.UmrahPilgrimsFilledViewHolder
import com.tokopedia.salam.umrah.checkout.presentation.viewmodel.UmrahCheckoutViewModel
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.*
import com.tokopedia.salam.umrah.common.util.*
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getTime
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpAirlineModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_error.view.*
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_term_condition.view.*
import kotlinx.android.synthetic.main.bottom_sheet_umrah_mandatory_document.view.*
import kotlinx.android.synthetic.main.fragment_umrah_checkout.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_content_order.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_content_summary.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_footer.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_header.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_installment_list.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_promo.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by firman on 4/11/2019
 */

class UmrahCheckoutFragment : BaseDaggerFragment(), UmrahPilgrimsEmptyViewHolder.UmrahCheckoutPilgrimsListListener,
        UmrahPilgrimsFilledViewHolder.UmrahCheckoutPilgrimsListListener, UmrahCheckoutActivity.OnBackListener {

    @Inject
    lateinit var umrahCheckoutViewModel: UmrahCheckoutViewModel

    @Inject
    lateinit var trackingUmrahUtil: UmrahTrackingAnalytics

    lateinit var progressDialog: ProgressDialog
    var slugName: String = ""
    var variantId: String = ""
    var departDate: String = ""
    var promoCode : String = ""
    lateinit var performanceMonitoring: PerformanceMonitoring


    private val umrahCheckoutPilgrimsListAdapter by lazy { UmrahCheckoutPilgrimsListAdapter(this, this) }
    private val umrahCheckoutSummaryAdapter by lazy { UmrahCheckoutSummaryListAdapter() }
    private val umrahCheckoutTermConditionAdapter by lazy { UmrahCheckoutTermConditionAdapter() }
    private val umrahCheckoutMandatoryDocumentAdapter by lazy { UmrahCheckoutMandatoryDocumentAdapter() }

    override fun getScreenName(): String = ""


    override fun initInjector() = getComponent(UmrahCheckoutComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_checkout, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        arguments?.let{
            slugName = it.getString(EXTRA_SLUG_NAME, "")
            variantId = it.getString(EXTRA_VARIANT, "")
            pilgrimCount = it.getInt(EXTRA_TOTAL_PASSENGER, 0)
            price = it.getInt(EXTRA_PRICE, 0)
            departDate = it.getString(EXTRA_DEPART_DATE, "")
            totalPrice = it.getInt(EXTRA_TOTAL_PRICE, 0)
            downPaymentPrice = it.getInt(EXTRA_DOWN_PAYMENT_PRICE, 0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahCheckoutViewModel.checkoutMapped.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderView(it.data)
                    performanceMonitoring.stopTrace()
                }
                is Fail -> {
                    performanceMonitoring.stopTrace()
                    val data = it.throwable
                    view?.let {
                        Toaster.build(it, data.message ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.umrah_checkout_error_confirmation),
                                View.OnClickListener { })
                    }
                    hideLoadingBar()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView,null,null,null,R.drawable.umrah_img_empty_search_png){
                    requestData()
                        showLoadingBar()
                    }
                }
            }

        })

        umrahCheckoutViewModel.checkoutResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.checkoutGeneral.data.success == 0) {
                        progressDialog.dismiss()
                        showBottomSheetCheckoutError(it.data.checkoutGeneral.data.error)
                    } else {
                        context?.run {
                            val taskStackBuilder = TaskStackBuilder.create(this)
                            val intentHomeUmrah = RouteManager.getIntent(this, ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE)
                            taskStackBuilder.addNextIntent(intentHomeUmrah)

                            val checkoutResultData = PaymentPassData()
                            checkoutResultData.queryString = it.data.checkoutGeneral.data.data.queryString
                            checkoutResultData.redirectUrl = it.data.checkoutGeneral.data.data.redirectUrl
                            checkoutResultData.transactionId = it.data.checkoutGeneral.data.data.parameter.transactionId
                            checkoutResultData.paymentId = it.data.checkoutGeneral.data.data.parameter.pid

                            val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                            val intent = RouteManager.getIntent(context, paymentCheckoutString)
                            resetVariable()
                            progressDialog.dismiss()
                            intent?.run {
                                putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, checkoutResultData)
                                taskStackBuilder.addNextIntent(this)
                                taskStackBuilder.startActivities()
                            }
                        }
                    }
                }
                is Fail -> {
                    progressDialog.dismiss()
                    hideLoadingBar()
                    val data = it.throwable
                    view?.let {
                        Toaster.build(it, data.message ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.umrah_checkout_error_confirmation),
                                View.OnClickListener { })
                    }
                }
            }

        })

    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_CHECKOUT_PAGE_PERFORMANCE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        showLoadingBar()
        requestData()
    }

    private fun requestData(){
        umrahCheckoutViewModel.getDataCheckout(
                UmrahQuery.UMRAH_PDP_QUERY,
                UmrahQuery.UMRAH_CHECKOUT_SUMMARY_QUERY,
                UmrahQuery.UMRAH_CHECKOUT_PAYMENT_OPTION_QUERY,
                UmrahQuery.UMRAH_CHECKOUT_TNC_QUERY,
                slugName,
                variantId,
                pilgrimCount,
                price,
                departDate,
                ID_TERM_CONDITION,
                downPaymentPrice
        )
    }

    fun renderView(data: UmrahCheckoutMapperEntity) {
        hideLoadingBar()

        data.apply {
            productModel = checkoutPDP
            renderHeader(checkoutPDP)

            if(userData.email.isEmpty() || userData.name.isEmpty() || userData.phoneNumber.isEmpty())
                userData = user

            listPaymentOptions = paymentOptions.umrahPaymentOptions

            if (checkInstallment() && listSchemes.isEmpty())
                listSchemes = paymentOptions.umrahPaymentOptions.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].schemes

            renderContentSummary(summaryPayment.umrahCheckoutSummary, paymentOptions.umrahPaymentOptions, optionSchemes)
            renderContentOrder()
            renderFooter(totalPrice)
            renderButtonCheckout()
            renderPromo()
        }

        context?.let {
            tg_umrah_checkout_agreement_condition.makeLinks(
                    Pair(getString(R.string.umrah_checkout_agreement_condition_click), View.OnClickListener {
                        showBottomSheetTermCondition(it.context, data.termCondition.umrahTermsConditions)
                    })
            )
            tg_umrah_checkout_order_description_pilgrims.makeLinks(
                    Pair(getString(R.string.umrah_checkout_order_description_pilgrims_click), View.OnClickListener {
                        trackingUmrahUtil.getListDetailPilgrimsCheckoutTracker()
                        val mandatoryDocuments = mappingMandatoryDocument()
                        showMandatoryDocument(it.context, mandatoryDocuments)
                    })
            )
        }


        container_widget_umrah_checkout_rect.setOnClickListener {
            trackingUmrahUtil.getContactCustomerCheckoutTracker()
            context?.run {
                startActivityForResult(UmrahCheckoutContactDataActivity.getCallingIntent(this, userData),
                        REQUEST_CODE_CONTACT_DATA)
            }
        }


        partial_umrah_checkout_installment_list.setOnClickListener {
            startActivityForResult(UmrahCheckoutInstallmentActivity.createIntent(it.context,
                    data.paymentOptions.umrahPaymentOptions.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT]
                            .schemes, optionSchemes), REQUEST_CODE_PAYMENT)
        }

        btn_umrah_checkout.setOnClickListener {
            onButtonCheckoutClicked()
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.umrah_checkout_progress_dialog))
        progressDialog.setCancelable(false)
    }

    private fun onButtonCheckoutClicked() {
        progressDialog.show()

        if (validateData()) {
            trackingUmrahUtil.getCheckoutTracker(DEFAULT_OPTION_CHECKOUT_STEP, productModel, pilgrimCount)
            val checkoutResultParams = UmrahCheckoutResultParams(
                    carts = Carts(
                            meta_data = mapToCheckoutMetaData()
                    )
            )

            umrahCheckoutViewModel.executeCheckout(UmrahQuery.UMRAH_CHECKOUT_GENERAL_QUERY, checkoutResultParams)
        } else {
            progressDialog.dismiss()
            view?.let {
                Toaster.build(it, getString(R.string.umrah_checkout_validation_error) ?: "", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.umrah_checkout_error_confirmation),
                        View.OnClickListener { })
            }
        }

    }

    private fun validateData(): Boolean {
        var isValid = false
        if (cb_umrah_checkout.isChecked && userData.id.isNotEmpty() && userData.name.isNotEmpty() && userData.email.isNotEmpty() &&
                userData.phoneNumber.isNotEmpty() && productModel.id.isNotEmpty() && price > 0 && variantId.isNotEmpty() &&
                pilgrimsValidation())
            isValid = true

        return isValid
    }

    private fun mapToCheckoutMetaData(): String {

        val listPaymentTerms = getListTerms()
        val listPilgrims = mapPilgrimstoMetaData()
        val contact = Contact(
                name = userData.name,
                email = userData.email,
                phone = getString(R.string.umrah_checkout_contact_phone_code_and_phone_number,
                        userData.phoneCode.toString(), userData.phoneNumber)
        )
        var mappedCheckoutMetaData = UmrahCheckoutMetaDataParams(
                user_id = userData.id.toInt(),
                product_id = productModel.id.toInt(),
                price = totalPrice,
                product_variant_id = variantId.toInt(),
                promo_code = promoCode,
                contact = contact,
                pilgrims = listPilgrims,
                payment_terms = listPaymentTerms

        )

        return convertToJsonMetaData(mappedCheckoutMetaData)
    }

    private fun getListTerms(): List<TermParam> {
        val termParam = if (rb_umrah_checkout_full_paid.isChecked) {
            listPaymentOptions.paymentOptions[DEFAULT_OPTION_FULL_PAYMENT].schemes[DEFAULT_OPTION_FULL_PAYMENT].terms
        } else {
            listPaymentOptions.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].schemes[optionSchemes].terms
        }

        return termParam.map {
            with(it) {
                return@map TermParam(
                        type, price, dueDate
                )
            }
        }
    }

    private fun pilgrimsValidation(): Boolean {
        var isValid = false
        if (listDataPilgrims.isNotEmpty()) {
            for (i in 0 until listDataPilgrims.size) {
                isValid = (listDataPilgrims[i].firstName.isNotEmpty() && listDataPilgrims[i].lastName.isNotEmpty()
                        && listDataPilgrims[i].dateBirth.isNotEmpty() && listDataPilgrims[i].title.isNotEmpty())
            }
        }
        return isValid
    }

    private fun convertToJsonMetaData(umrahCheckoutMetaDataParams: UmrahCheckoutMetaDataParams): String {
        val gson = Gson()
        return gson.toJson(umrahCheckoutMetaDataParams)
    }

    private fun mapPilgrimstoMetaData(): List<Pilgrims> {
        return listDataPilgrims.map {
            with(it) {
                return@map Pilgrims(
                        title, firstName, lastName, dateBirth
                )
            }
        }
    }

    private fun renderHeader(data: UmrahProductModel.UmrahProduct) {
        data.apply {
            renderTitle(title)
            renderTravelAgent(travelAgent)
            renderPrice(price)
            renderPeriod(departureDate, returningDate, durationDays)
            renderHotel(hotels, variants)
            renderAirlines(airlines, transitCity)
            renderPilgrims()
        }

    }

    private fun updateContactData(){
        userData.apply {
            tg_umrah_checkout_order_customer_title.text = name
            tg_umrah_checkout_order_customer_email.text = email
            if(phoneNumber.isNotEmpty())
                tg_umrah_checkout_order_customer_phone.text = getString(R.string.umrah_checkout_contact_phone_code_and_phone_number, phoneCode.toString(),phoneNumber)
        }
    }

    private fun renderContentOrder() {
        updateContactData()
        renderPilgrimsList()
    }

    private fun renderContentSummary(summary: UmrahCheckoutSummary, paymentOption: UmrahPaymentOptions, optionSchemes:Int) {
        renderSummary(summary)
        renderPaymentOption(paymentOption, optionSchemes)
    }

    private fun renderFooter(price: Int) {
        tg_umrah_total_price.text = getRupiahFormat(price)
        cb_umrah_checkout.setOnCheckedChangeListener { _, _ ->
            renderButtonCheckout()
        }
    }

    private fun renderButtonCheckout() {
        btn_umrah_checkout.isEnabled = validateData()
    }

    private fun renderSummary(summary: UmrahCheckoutSummary) {
        rv_umrah_checkout_list_summary.apply {
            adapter = umrahCheckoutSummaryAdapter
            layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL, false
            )
            umrahCheckoutSummaryAdapter.setList(summary.checkoutDetails)
        }

        tg_umrah_checkout_summary_total_price.text = getRupiahFormat(summary.total)
    }

    private fun renderPaymentOption(paymentOption: UmrahPaymentOptions, optionSchemes: Int) {
        if (paymentOption.paymentOptions.isNotEmpty()) {
            tg_umrah_checkout_full_payment_price.text = getRupiahFormat(
                    paymentOption.paymentOptions[DEFAULT_OPTION_FULL_PAYMENT].price)

            if (checkInstallment()) {
                tg_umrah_checkout_installment_price.text = getRupiahFormat(
                        paymentOption.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].price)

                val schemes = paymentOption.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].schemes
                setPaymentTypeChecked(DEFAULT_OPTION_INSTALLMENT_PAYMENT, schemes, optionSchemes)
            } else {
                view_umrah_checkout_separator.visibility = View.GONE
                rl_umrah_checkout_down_payment.visibility = View.GONE
                partial_umrah_checkout_installment_list.visibility = View.GONE
                val schemes = paymentOption.paymentOptions[DEFAULT_OPTION_FULL_PAYMENT].schemes
                val defaultOptionSchemes = paymentOption.paymentOptions[DEFAULT_OPTION_FULL_PAYMENT].defaultOption
                setPaymentTypeChecked(DEFAULT_OPTION_FULL_PAYMENT, schemes, defaultOptionSchemes)
            }
        }

    }


    private fun renderPilgrimsList() {
        rv_umrah_checkout_pilgrims_list.apply {
            adapter = umrahCheckoutPilgrimsListAdapter
            layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL, false
            )
        }
        if (pilgrimCount > 0 && listDataPilgrims.size == 0) {
            listDataPilgrims = getPilgrimsLists(pilgrimCount)
        }
        umrahCheckoutPilgrimsListAdapter.setList(listDataPilgrims)
    }

    private fun renderTitle(title: String) {
        tg_umrah_checkout_title.text = title
    }

    private fun renderTravelAgent(data: TravelAgent) {
        val umrahItemWidgetModel: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
            title = data.name
            imageUri = data.imageUrl
            desc = data.permissionOfUmrah
        }
        iw_umrah_checkout_travel.umrahItemWidgetModel = umrahItemWidgetModel
        iw_umrah_checkout_travel.buildView()
    }

    private fun renderPrice(data: Int) {
        tg_umrah_checkout_price.text = getRupiahFormat(data)
    }

    private fun renderPeriod(departureDate: String, returningDate: String, durationDays: Int) {
        val departureDateFormatted = getTime(UmrahDateUtil.DATE_WITHOUT_YEAR_FORMAT, departureDate)
        val returningDateFormatted = getTime(UmrahDateUtil.DATE_WITH_YEAR_FORMAT, returningDate)
        val umrahItemWidgetModel = UmrahItemWidgetModel()
        umrahItemWidgetModel.apply {
            imageDrawable = R.drawable.umrah_ic_calendar
            title = getString(R.string.umrah_pdp_calendar, departureDateFormatted, returningDateFormatted)
            desc = getString(R.string.umrah_pdp_duration, durationDays, durationDays - 1)
        }
        iw_umrah_checkout_period.umrahItemWidgetModel = umrahItemWidgetModel
        iw_umrah_checkout_period.buildView()
    }

    private fun renderHotel(hotels: List<UmrahHotel>, variants: List<UmrahVariant>) {
        val hotelsRating = UmrahHotelRating.getAllHotelRatings(hotels)
        val hotelsVariant = UmrahHotelVariant.getHotelVariantbyID(variants, variantId)
        val umrahItemWidgetModel = UmrahItemWidgetModel()
        umrahItemWidgetModel.apply {
            imageDrawable = R.drawable.umrah_ic_hotel
            title = getString(R.string.umrah_search_hotel_rating_x, hotelsRating)
            desc = hotelsVariant
        }
        iw_umrah_checkout_hotel.umrahItemWidgetModel = umrahItemWidgetModel
        iw_umrah_checkout_hotel.buildView()
    }

    private fun renderAirlines(data: List<UmrahPdpAirlineModel>, transitCity: UmrahCity) {
        if (data.isNotEmpty()) {
            val umrahItemWidgetModel: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
                title = data[0].name
                imageDrawable = R.drawable.umrah_ic_plane
                desc = try {
                    transitCity.name
                } catch (e: NullPointerException) {
                    getString(R.string.umrah_pdp_direct_flight)
                }
            }
            iw_umrah_checkout_flight.umrahItemWidgetModel = umrahItemWidgetModel
            iw_umrah_checkout_flight.buildView()
        }
    }

    private fun renderPilgrims() {
        val umrahitemWidget: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
            title = getString(R.string.umrah_checkout_order_pilgrims)
            imageDrawable = R.drawable.umrah_ic_pdp_person
            desc = getString(R.string.umrah_checkout_order_pilgrims_count, pilgrimCount)
        }
        iw_umrah_checkout_pilgrims.umrahItemWidgetModel = umrahitemWidget
        iw_umrah_checkout_pilgrims.buildView()
    }


    private fun setPaymentTypeChecked(defaultOption: Int, schemes: List<Schemes>, defaultOptionSchemes: Int) {
        val defaultSchemes = schemes[defaultOptionSchemes]
        tg_umrah_checkout_installment_list.text = getDefaultInstallmentTitle(defaultSchemes)
        tg_umrah_checkout_installment_first_due_date.text = getDefaultFirstDueDate(defaultSchemes)
        when (defaultOption) {
            DEFAULT_OPTION_FULL_PAYMENT -> rb_umrah_checkout_full_paid.isChecked = true
            DEFAULT_OPTION_INSTALLMENT_PAYMENT -> rb_umrah_checkout_down_payment.isChecked = true
        }

        rl_umrah_checkout_down_payment.setOnClickListener {
            trackingUmrahUtil.getPaymentTypeCheckoutTracker(CHECKOUT_INSTALLMENT)
            if (rb_umrah_checkout_full_paid.isChecked) {
                rb_umrah_checkout_down_payment.isChecked = true
                rb_umrah_checkout_full_paid.isChecked = false
                partial_umrah_checkout_installment_list.visibility = View.VISIBLE
            }
        }

        rl_umrah_checkout_full_paid.setOnClickListener {
            trackingUmrahUtil.getPaymentTypeCheckoutTracker(CHECKOUT_FULL_PAID)
            if (rb_umrah_checkout_down_payment.isChecked) {
                rb_umrah_checkout_full_paid.isChecked = true
                rb_umrah_checkout_down_payment.isChecked = false
                partial_umrah_checkout_installment_list.visibility = View.GONE

            }
        }
    }


    private fun showBottomSheetCheckoutError(error: String) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_umrah_checkout_error, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            context?.let{
                setTitle(it.getString(R.string.umrah_checkout_mandatory_document_bottom_sheet_title))
            }
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.es_checkout.apply {
            ContextCompat.getDrawable(context,R.drawable.umrah_img_error_checkout)?.let { setImageDrawable(it) }
            setTitle(getString(R.string.umrah_checkout_error_title))
            setDescription(error)
            setPrimaryCTAText(getString(R.string.umrah_checkout_error_btn))
            setPrimaryCTAClickListener {
                activity?.finish()
            }
        }
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    fun showMandatoryDocument(context: Context, listBottomSheet: List<UmrahCheckoutMandatoryDocument>) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_umrah_mandatory_document, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(context.getString(R.string.umrah_checkout_mandatory_document_bottom_sheet_title))
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_umrah_checkout_mandatory_document.apply {
            umrahCheckoutMandatoryDocumentAdapter.setList(listBottomSheet)
            this.adapter = umrahCheckoutMandatoryDocumentAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        }
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }

    }

    fun showBottomSheetTermCondition(context: Context, listBottomSheet: List<UmrahTermCondition>) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_umrah_checkout_term_condition, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(context.getString(R.string.umrah_checkout_term_condition_bottom_sheet_title))
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_umrah_checkout_term_condition.apply {
            umrahCheckoutTermConditionAdapter.setList(listBottomSheet)
            this.adapter = umrahCheckoutTermConditionAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
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

    private fun mappingMandatoryDocument(): List<UmrahCheckoutMandatoryDocument> {
        val umrahMandatoryDocumentTitle = resources.getStringArray(R.array.umrah_checkout_mandatory_document_title)
        val umrahMandatoryDocumentDesc = resources.getStringArray(R.array.umrah_checkout_mandatory_document)
        return umrahMandatoryDocumentTitle.zip(umrahMandatoryDocumentDesc)
                .map { UmrahCheckoutMandatoryDocument(it.first, it.second) }
    }

    private fun getDefaultInstallmentTitle(scheme: Schemes): String {
        return "${scheme.title} - ${getRupiahFormat(scheme.price)}"
    }

    private fun getDefaultFirstDueDate(scheme: Schemes): String {
        val firstDueDate = getTime(UmrahDateUtil.DATE_WITH_YEAR_FULL_MONTH_FORMAT, scheme.firstDueDate)
        return getString(R.string.umrah_checkout_summary_installment_next, firstDueDate)
    }

    override fun onPilgrimsClick(position: Int) {
        trackingUmrahUtil.getListPilgrimsCheckoutTracker()
        context?.let {
            startActivityForResult(UmrahCheckoutPilgrimsActivity.createIntent(it, listDataPilgrims.get(position))
                    , REQUEST_CODE_PILGRIMS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PILGRIMS && data != null && data.hasExtra(CommonParam.ARG_CHECKOUT_ID)) {

                val cacheId = data.getStringExtra(CommonParam.ARG_CHECKOUT_ID)
                val cacheManager = context?.let { SaveInstanceCacheManager(it, cacheId) } ?: return
                val pilgrimsData = cacheManager.get(CommonParam.ARG_CHECKOUT, UmrahCheckoutPilgrims::class.java)
                        ?: UmrahCheckoutPilgrims()

                listDataPilgrims.set(pilgrimsData.pilgrimsNumber-1, pilgrimsData)

                renderPilgrimsList()
            } else if (requestCode == REQUEST_CODE_PAYMENT && data != null && data.hasExtra(CommonParam.ARG_CHECKOUT_PAYMENT_OPTION)) {

                val choosenPaymentTypeSchemes = data.getIntExtra(CommonParam.ARG_CHECKOUT_PAYMENT_OPTION, 0)
                optionSchemes = choosenPaymentTypeSchemes
                setPaymentTypeChecked(DEFAULT_OPTION_INSTALLMENT_PAYMENT, listSchemes, optionSchemes)

            } else if (requestCode == REQUEST_CODE_CONTACT_DATA && data != null) {
                data.run {
                    userData = this.getParcelableExtra(UmrahCheckoutContactDataFragment.EXTRA_CONTACT_DATA)
                    updateContactData()
                }
            } else if (requestCode == PROMO_EXTRA_LIST_ACTIVITY_RESULT && data !=null){
                data.let {
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
            renderButtonCheckout()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showLoadingBar() {
        container_umrah_checkout.visibility = View.INVISIBLE
        pg_umrah_checkout.visibility = View.VISIBLE
    }

    private fun hideLoadingBar() {
        container_umrah_checkout.visibility = View.VISIBLE
        pg_umrah_checkout.visibility = View.GONE
    }

    private fun checkInstallment(): Boolean {
        return listPaymentOptions.paymentOptions.size > 1
    }

    private fun renderPromo(){
        ticker_promo_umrah.actionListener = object : TickerPromoStackingCheckoutView.ActionListener {
            override fun onClickDetailPromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_UMROH)
                intent.putExtra(EXTRA_TOTAL_PRICE,totalPrice)
                intent.putExtra(EXTRA_PROMO_CODE,promoCode)
                startActivityForResult(intent, PROMO_EXTRA_LIST_ACTIVITY_RESULT)
            }

            override fun onClickUsePromo() {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_UMROH)
                intent.putExtra(EXTRA_TOTAL_PRICE,totalPrice)
                intent.putExtra(EXTRA_PROMO_CODE,promoCode)
                startActivityForResult(intent, PROMO_EXTRA_LIST_ACTIVITY_RESULT)
            }

            override fun onDisablePromoDiscount() {

            }

            override fun onResetPromoDiscount() {
                setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                promoCode = ""
            }
        }
        setupPromoTicker(TickerCheckoutView.State.EMPTY,"","")
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State,
                                 title: String,
                                 description: String) {
        if (state == TickerCheckoutView.State.EMPTY) {
            tickerStaticHide()
            ticker_promo_umrah.title = title
            ticker_promo_umrah.state = TickerPromoStackingCheckoutView.State.EMPTY
        } else if (state == TickerCheckoutView.State.ACTIVE) {
            tickerStaticShow()
            ticker_promo_umrah.title = title
            ticker_promo_umrah.desc = description
            ticker_promo_umrah.state = TickerPromoStackingCheckoutView.State.ACTIVE
        } else if (state == TickerCheckoutView.State.INACTIVE) {
            tickerStaticShow()
            ticker_promo_umrah.title = title
            ticker_promo_umrah.desc = description
            ticker_promo_umrah.state = TickerPromoStackingCheckoutView.State.INACTIVE
        }
    }

    private fun tickerStaticShow(){
        ticker_announc_umroh_checkout_promo.show()
        ticker_announc_umroh_checkout_promo.setTextDescription(getString(R.string.umrah_ticker_static_promo))
    }

    private fun tickerStaticHide(){
        ticker_announc_umroh_checkout_promo.gone()
    }

    companion object {

        private var pilgrimCount = 0
        private var price = 0
        private var totalPrice = 0
        private var optionSchemes = 0
        private var downPaymentPrice = 0
        private var userData = ContactUser()
        private var listSchemes: List<Schemes> = arrayListOf()
        private var listDataPilgrims: MutableList<UmrahCheckoutPilgrims> = mutableListOf()
        private var productModel = UmrahProductModel.UmrahProduct()
        private var listPaymentOptions = UmrahPaymentOptions()

        const val DEFAULT_OPTION_FULL_PAYMENT = 0
        const val DEFAULT_OPTION_INSTALLMENT_PAYMENT = 1

        const val EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA"
        const val ID_TERM_CONDITION = "TNC_CHECKOUT"

        const val REQUEST_CODE_CHECKOUT = 105
        const val REQUEST_CODE_CONTACT_DATA = 104
        const val REQUEST_CODE_PILGRIMS = 106
        const val REQUEST_CODE_PAYMENT = 107

        const val CHECKOUT_FULL_PAID = "Bayar Penuh"
        const val CHECKOUT_INSTALLMENT = "Uang Muka"

        const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val EXTRA_TOTAL_PRICE = "EXTRA_TOTAL_PRICE"
        const val EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"

        const val UMRAH_CHECKOUT_PAGE_PERFORMANCE = "sl_umrah_checkout"


        const val PROMO_EXTRA_LIST_ACTIVITY_RESULT = 123

        const val DEFAULT_OPTION_CHECKOUT_STEP = 1


        fun getInstance(slugName: String, variant: String, price: Int,
                        totalPrice: Int, totalPassenger: Int, departDate: String, downPaymentPrice:Int
        ): UmrahCheckoutFragment = UmrahCheckoutFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_SLUG_NAME, slugName)
                putString(EXTRA_VARIANT, variant)
                putInt(EXTRA_PRICE, price)
                putInt(EXTRA_TOTAL_PRICE, totalPrice)
                putInt(EXTRA_TOTAL_PASSENGER, totalPassenger)
                putString(EXTRA_DEPART_DATE, departDate)
                putInt(EXTRA_DOWN_PAYMENT_PRICE, downPaymentPrice)
            }
        }

        private fun getPilgrimsLists(pilgrimCount: Int): MutableList<UmrahCheckoutPilgrims> {
            val pilgrimsCountList = mutableListOf<UmrahCheckoutPilgrims>()
            for (n in 1..pilgrimCount) {
                pilgrimsCountList.add(UmrahCheckoutPilgrims(n))
            }
            return pilgrimsCountList
        }
    }

    private fun resetVariable(){
        pilgrimCount = 0
        price = 0
        totalPrice = 0
        optionSchemes = 0
        downPaymentPrice = 0
        userData = ContactUser()
        listSchemes= arrayListOf()
        listDataPilgrims = mutableListOf()
        productModel = UmrahProductModel.UmrahProduct()
        listPaymentOptions = UmrahPaymentOptions()
    }

    override fun onBackPress() {
        resetVariable()
        trackingUmrahUtil.getClickBackCheckoutTracker()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

}
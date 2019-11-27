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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.*
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.common.data.UmrahProductModel.UmrahProduct.TravelAgent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_DEPART_DATE
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_PRICE
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_SLUG_NAME
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_TOTAL_PASSENGER
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_TOTAL_PRICE
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity.Companion.EXTRA_VARIANT
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutInstallmentActivity
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutPilgrimsActivity
import com.tokopedia.salam.umrah.checkout.presentation.adapter.*
import com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder.UmrahPilgrimsEmptyViewHolder
import com.tokopedia.salam.umrah.checkout.presentation.adapter.viewholder.UmrahPilgrimsFilledViewHolder
import com.tokopedia.salam.umrah.checkout.presentation.viewmodel.UmrahCheckoutViewModel
import com.tokopedia.salam.umrah.common.data.*
import com.tokopedia.salam.umrah.common.util.CommonParam
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDay
import com.tokopedia.salam.umrah.common.util.UmrahHotelRating
import com.tokopedia.salam.umrah.common.util.UmrahHotelVariant
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpAirlineModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_contact.*
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_contact.view.*
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_installment.view.*
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_installment.view.tv_umrah_checkout_bottom_sheet_title
import kotlinx.android.synthetic.main.bottom_sheet_umrah_checkout_term_condition.view.*
import kotlinx.android.synthetic.main.bottom_sheet_umrah_mandatory_document.view.*
import kotlinx.android.synthetic.main.fragment_umrah_checkout.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_content_order.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_content_summary.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_footer.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_header.*
import kotlinx.android.synthetic.main.partial_umrah_checkout_installment_list.*
import javax.inject.Inject

/**
 * @author by firman on 4/11/2019
 */

class UmrahCheckoutFragment : BaseDaggerFragment(), UmrahPilgrimsEmptyViewHolder.UmrahCheckoutPilgrimsListListener,
        UmrahPilgrimsFilledViewHolder.UmrahCheckoutPilgrimsListListener {

    @Inject
    lateinit var umrahCheckoutViewModel: UmrahCheckoutViewModel

    lateinit var progressDialog: ProgressDialog
    lateinit var slugName: String
    lateinit var variantId: String
    lateinit var departDate: String

    val idTermCondition = "TNC_CHECKOUT"
    val REQUEST_FILTER_PILGRIMS = 0x10
    val REQUEST_FILTER_PAYMENT = 0x11

    var pilgrimCount = 0
    var price = 0
    var totalPrice = 0


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
        slugName = arguments!!.getString(EXTRA_SLUG_NAME, "")
        variantId = arguments!!.getString(EXTRA_VARIANT, "")
        pilgrimCount = arguments!!.getInt(EXTRA_TOTAL_PASSENGER, 0)
        price = arguments!!.getInt(EXTRA_PRICE, 0)
        departDate = arguments!!.getString(EXTRA_DEPART_DATE, "")
        totalPrice = arguments!!.getInt(EXTRA_TOTAL_PRICE,0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahCheckoutViewModel.checkoutMapped.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderView(it.data)
                }
                is Fail -> {
                    val data = it.throwable
                    view?.let {
                        Toaster.showErrorWithAction(it, data.message
                                ?: "", Snackbar.LENGTH_LONG, "OK", View.OnClickListener { /* do nothing */ })
                    }
                }
            }

        })


        umrahCheckoutViewModel.checkoutResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    val checkoutResultData = PaymentPassData()
                    checkoutResultData.queryString = it.data.data.data.queryString
                    checkoutResultData.redirectUrl = it.data.data.data.redirectUrl
                    val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                    val intent = RouteManager.getIntent(context, paymentCheckoutString)
                    intent?.run {
                        putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, checkoutResultData)
                        startActivityForResult(intent, REQUEST_CODE_CHECKOUT)
                    }
                }
                is Fail -> {
                    val data = it.throwable
                    view?.let {
                        Toaster.showErrorWithAction(it, data.message
                                ?: "", Snackbar.LENGTH_LONG, "OK", View.OnClickListener { /* do nothing */ })
                    }
                }
            }

        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        showLoadingBar()
        umrahCheckoutViewModel.execute(
                GraphqlHelper.loadRawString(resources,
                        R.raw.gql_query_umrah_pdp_simple),
                GraphqlHelper.loadRawString(resources,
                        R.raw.gql_query_umrah_checkout_summary),
                GraphqlHelper.loadRawString(resources,
                        R.raw.gql_query_umrah_checkout_payment_option),
                GraphqlHelper.loadRawString(resources,
                        R.raw.gql_query_umrah_checkout_tnc),
                slugName,
                variantId,
                pilgrimCount,
                price,
                departDate,
                idTermCondition
        )
    }

    fun renderView(data: UmrahCheckoutMapperEntity) {
        hideLoadingBar()

        data.apply {
            productModel = checkoutPDP
            renderHeader(checkoutPDP)
            userData = user
            listSchemes = paymentOptions.umrahPaymentOptions.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].schemes

            renderContentOrder(userData)
            renderContentSummary(summaryPayment.umrahCheckoutSummary, paymentOptions.umrahPaymentOptions)
            renderFooter(totalPrice)
            renderButtonCheckout()
        }

        tg_umrah_checkout_agreement_condition.makeLinks(
                Pair(getString(R.string.umrah_checkout_agreement_condition_click), View.OnClickListener {
                    showBottomSheetTermCondition(context!!, data.termCondition.umrahTermsConditions)
                })
        )

        tg_umrah_checkout_order_description_pilgrims.makeLinks(
                Pair(getString(R.string.umrah_checkout_order_description_pilgrims_click), View.OnClickListener {
                    val mandatoryDocuments = mappingMandatoryDocument()
                    showMandatoryDocument(context!!, mandatoryDocuments)
                })
        )
        container_widget_umrah_checkout_rect.setOnClickListener {
            showBottonSheetCheckout(data.user)
        }


        partial_umrah_checkout_installment_list.setOnClickListener {
            startActivityForResult(UmrahCheckoutInstallmentActivity.createIntent(it.context,
                    data.paymentOptions.umrahPaymentOptions.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT]
                            .schemes, optionSchemes), REQUEST_FILTER_PAYMENT)
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

    private fun onButtonCheckoutClicked(){
        progressDialog.show()

        if(validateData()){
            val checkoutResultParams = UmrahCheckoutResultParams(
                    metaData = mapToCheckoutMetaData()
            )

            umrahCheckoutViewModel.executeCheckout(GraphqlHelper.loadRawString(resources,
                    R.raw.gql_query_umrah_checkout_general), checkoutResultParams)
        }else{
            progressDialog.dismiss()
            view?.let {
                Toaster.showErrorWithAction(it, getString(R.string.umrah_checkout_validation_error)
                        ?: "", Snackbar.LENGTH_LONG, "OK", View.OnClickListener { /* do nothing */ })
            }
        }

    }

    private fun validateData(): Boolean{
        var isValid = false
        if (cb_umrah_checkout.isChecked && userData.id.isNotEmpty() && userData.email.isNotEmpty() &&
                userData.phoneNumber.isNotEmpty() && productModel.id.isNotEmpty() && price>0 && variantId.isNotEmpty() &&
                mapPilgrimstoMetaData().isNotEmpty())
            isValid = true

        return isValid
    }

    private fun mapToCheckoutMetaData(): String{
        val listPilgrims = mapPilgrimstoMetaData()
        val contact = Contact(
                name = userData.name,
                email = userData.email,
                phone = userData.phoneNumber,
                pilgrims = listPilgrims
        )
        var mappedCheckoutMetaData = UmrahCheckoutMetaDataParams(
                userId = userData.id,
                productId = productModel.id,
                price = price,
                productVariantId = variantId,
                contact = contact

        )

        return convertToJsonMetaData(mappedCheckoutMetaData)
    }

    private fun convertToJsonMetaData(umrahCheckoutMetaDataParams: UmrahCheckoutMetaDataParams): String{
        val gson = Gson()
        return gson.toJson(umrahCheckoutMetaDataParams)
    }

    private fun mapPilgrimstoMetaData():List<Pilgrims>{
       return listDataPilgrims.map {
           with(it){
               return@map Pilgrims(
                       title,firstName,lastName,dateBirth
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

    private fun renderContentOrder(data: ContactUser) {
        renderUser(data)
        renderPilgrimsList()
    }

    private fun renderContentSummary(summary: UmrahCheckoutSummary, paymentOption: UmrahPaymentOptions) {
        renderSummary(summary)
        renderPaymentOption(paymentOption)
    }

    private fun renderFooter(price:Int){
        tg_umrah_total_price.text = getRupiahFormat(price)
    }

    private fun renderButtonCheckout(){
        btn_umrah_checkout.isEnabled = validateData()
    }

    private fun renderSummary(summary: UmrahCheckoutSummary) {
        rv_umrah_checkout_list_summary.apply {
            adapter = umrahCheckoutSummaryAdapter
            layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL, false
            )
            umrahCheckoutSummaryAdapter.setList(summary.checkoutDetails)
        }

        tg_umrah_checkout_summary_total_price.text = getRupiahFormat(summary.total)
    }

    private fun renderPaymentOption(paymentOption: UmrahPaymentOptions) {
        if (paymentOption.paymentOptions.isNotEmpty()) {
            tg_umrah_checkout_full_payment_price.text = getRupiahFormat(
                    paymentOption.paymentOptions[DEFAULT_OPTION_FULL_PAYMENT].price)
            tg_umrah_checkout_installment_price.text =getRupiahFormat(
                    paymentOption.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].price)

            val schemes = paymentOption.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].schemes
            val defaultOptionSchemes = paymentOption.paymentOptions[DEFAULT_OPTION_INSTALLMENT_PAYMENT].defaultOption
            setPaymentTypeChecked(paymentOption.defaultOption,schemes, defaultOptionSchemes)
        }

    }

    private fun renderUser(data: ContactUser) {
        data.apply {
            tg_umrah_checkout_order_customer_title.text = data.name
            tg_umrah_checkout_order_customer_email.text = data.email
            tg_umrah_checkout_order_customer_phone.text = data.phoneNumber
        }
    }

    private fun renderPilgrimsList() {
        if (pilgrimCount > 0) {
            rv_umrah_checkout_pilgrims_list.apply {
                adapter = umrahCheckoutPilgrimsListAdapter
                layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL, false
                )
            }
            listDataPilgrims = getPilgrimsLists(pilgrimCount)
            umrahCheckoutPilgrimsListAdapter.setList(listDataPilgrims)
        }
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
        val departureDateFormatted = UmrahDateUtil.getDate("dd MMM", departureDate)
        val returningDateFormatted = UmrahDateUtil.getDate("dd MMM yyyy", returningDate)
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


    private fun setPaymentTypeChecked(defaultOption: Int, schemes: List<Schemes>, defaultOptionSchemes:Int) {
        val defaultSchemes = schemes[defaultOptionSchemes]
        tg_umrah_checkout_installment_list.text = getDefaultInstallmentTitle(defaultSchemes)
        tg_umrah_checkout_installment_first_due_date.text = getDefaultFirstDueDate(defaultSchemes)
        when (defaultOption) {
            DEFAULT_OPTION_FULL_PAYMENT -> rb_umrah_checkout_full_paid.isChecked = true
            DEFAULT_OPTION_INSTALLMENT_PAYMENT -> rb_umrah_checkout_down_payment.isChecked = true
        }

        rb_umrah_checkout_down_payment.setOnClickListener {
            if (rb_umrah_checkout_down_payment.isChecked) {
                rb_umrah_checkout_full_paid.isChecked = false
                rb_umrah_checkout_down_payment.isChecked = true
                partial_umrah_checkout_installment_list.visibility = View.VISIBLE
            }
        }

        rb_umrah_checkout_full_paid.setOnClickListener {
            if (rb_umrah_checkout_down_payment.isChecked) {
                rb_umrah_checkout_full_paid.isChecked = true
                rb_umrah_checkout_down_payment.isChecked = false
                partial_umrah_checkout_installment_list.visibility = View.GONE

            }
        }
    }

    private fun showBottonSheetCheckout(user: ContactUser) {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(context).apply {
            setCustomContentView(inflatingViewCheckout(context, R.layout.bottom_sheet_umrah_checkout_contact, user, this), "", false)
            img_umrah_checkout_bottom_sheet_closed.setOnClickListener { dismiss() }

        }
        bottomSheet.show()
    }

    fun showMandatoryDocument(context: Context, listBottomSheet: List<UmrahCheckoutMandatoryDocument>) {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(context).apply {
            setCustomContentView(inflatingViewMandatoryDocument(context, R.layout.bottom_sheet_umrah_mandatory_document,
                    listBottomSheet), "", false)
            img_umrah_checkout_bottom_sheet_closed.setOnClickListener { dismiss() }
        }
        bottomSheet.show()

    }

    fun showBottomSheetTermCondition(context: Context, listBottomSheet: List<UmrahTermCondition>) {
        val bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(context).apply {
            setCustomContentView(inflatingViewTermCondition(context, R.layout.bottom_sheet_umrah_checkout_term_condition,
                    listBottomSheet), "", false)
            img_umrah_checkout_bottom_sheet_closed.setOnClickListener { dismiss() }

        }
        bottomSheet.show()

    }

    private fun inflatingViewTermCondition(context: Context, id: Int,
                                           listBottomSheet: List<UmrahTermCondition>): View? {
        val view = LayoutInflater.from(context).inflate(id, null).apply {
            umrahCheckoutTermConditionAdapter.setList(listBottomSheet)
            rv_umrah_checkout_term_condition.adapter = umrahCheckoutTermConditionAdapter
            rv_umrah_checkout_term_condition.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL, false
            )
        }
        return view
    }

    private fun inflatingViewCheckout(context: Context, id: Int, user: ContactUser, bottomSheetDialog: CloseableBottomSheetDialog): View? {
        val view = LayoutInflater.from(context).inflate(id, null).apply {
            et_umrah_checkout_bottom_sheet_contact_name.setText(user.name)
            et_umrah_checkout_bottom_sheet_contact_email.setText(user.email)
            et_umrah_checkout_bottom_sheet_contact_phone.setText(user.phoneNumber)
            btn_umrah_checkout_bottom_sheet.setOnClickListener {
                tg_umrah_checkout_order_customer_title.text = et_umrah_checkout_bottom_sheet_contact_name.text
                tg_umrah_checkout_order_customer_email.text = et_umrah_checkout_bottom_sheet_contact_email.text
                tg_umrah_checkout_order_customer_phone.text = et_umrah_checkout_bottom_sheet_contact_phone.text
                user.apply {
                    name = et_umrah_checkout_bottom_sheet_contact_name.text.toString()
                    email = et_umrah_checkout_bottom_sheet_contact_email.text.toString()
                    phoneNumber = et_umrah_checkout_bottom_sheet_contact_phone.text.toString()
                }
                bottomSheetDialog.dismiss()
            }
        }
        return view
    }

    private fun inflatingViewMandatoryDocument(context: Context, id: Int, listBottomSheet: List<UmrahCheckoutMandatoryDocument>): View? {
        val view = LayoutInflater.from(context).inflate(id, null).apply {
            umrahCheckoutMandatoryDocumentAdapter.setList(listBottomSheet)
            rv_umrah_checkout_mandatory_document.adapter = umrahCheckoutMandatoryDocumentAdapter
            rv_umrah_checkout_mandatory_document.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL, false
            )
        }
        return view
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

    private fun getDefaultInstallmentTitle(scheme: Schemes): String{
        return "${scheme.title} - ${getRupiahFormat(scheme.price)}"
    }

    private fun getDefaultFirstDueDate(scheme: Schemes): String{
        val firstDueDate = getDay("dd MMMM YYYY",scheme.firstDueDate)
        return getString(R.string.umrah_checkout_summary_installment_next,firstDueDate)
    }

    override fun onPilgrimsClick(position: Int) {
        startActivityForResult(UmrahCheckoutPilgrimsActivity.createIntent(context!!, listDataPilgrims.get(position))
                , REQUEST_FILTER_PILGRIMS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER_PILGRIMS && data != null && data.hasExtra(CommonParam.ARG_CHECKOUT_ID)) {
                val cacheId = data.getStringExtra(CommonParam.ARG_CHECKOUT_ID)
                val cacheManager = context?.let { SaveInstanceCacheManager(it, cacheId) } ?: return
                val pilgrimsData = cacheManager.get(CommonParam.ARG_CHECKOUT, UmrahCheckoutPilgrims::class.java)
                        ?: UmrahCheckoutPilgrims()

                umrahCheckoutPilgrimsListAdapter.onReplaceData(pilgrimsData, pilgrimsData.pilgrimsNumber - 1)
                renderButtonCheckout()

            } else if (requestCode == REQUEST_FILTER_PAYMENT && data != null && data.hasExtra(CommonParam.ARG_CHECKOUT_PAYMENT_OPTION)) {
                val choosenPaymentTypeSchemes = data.getIntExtra(CommonParam.ARG_CHECKOUT_PAYMENT_OPTION,0)
                optionSchemes = choosenPaymentTypeSchemes
                setPaymentTypeChecked(DEFAULT_OPTION_INSTALLMENT_PAYMENT, listSchemes, optionSchemes)
                renderButtonCheckout()

            }
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

    companion object {


        const val DEFAULT_OPTION_FULL_PAYMENT = 0
        const val DEFAULT_OPTION_INSTALLMENT_PAYMENT = 1
        const val EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA"
        const val REQUEST_CODE_CHECKOUT = 105


        var optionSchemes = 0
        var userData = ContactUser()
        var listSchemes : List<Schemes> = arrayListOf()
        var listDataPilgrims: MutableList<UmrahCheckoutPilgrims> = mutableListOf()
        var productModel = UmrahProductModel.UmrahProduct()

        fun getInstance(slugName: String, variant: String, price: Int,
                        totalPrice:Int,totalPassenger: Int, departDate: String
        ): UmrahCheckoutFragment = UmrahCheckoutFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_SLUG_NAME, slugName)
                putString(EXTRA_VARIANT, variant)
                putInt(EXTRA_PRICE, price)
                putInt(EXTRA_TOTAL_PRICE,totalPrice)
                putInt(EXTRA_TOTAL_PASSENGER, totalPassenger)
                putString(EXTRA_DEPART_DATE, departDate)
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

}
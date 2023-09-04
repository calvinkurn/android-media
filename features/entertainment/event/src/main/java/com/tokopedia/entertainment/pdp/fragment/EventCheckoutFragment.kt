package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
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
import com.tokopedia.entertainment.common.util.EventGlobalError
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.common.util.EventQuery.eventPDPV3
import com.tokopedia.entertainment.databinding.BottomSheetEventCheckoutBinding
import com.tokopedia.entertainment.databinding.FragmentEventCheckoutBinding
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.Serializable
import javax.inject.Inject

class EventCheckoutFragment : BaseDaggerFragment(), OnAdditionalListener {

    private var urlPDP: String = ""
    private var metadata = MetaDataResponse()
    private var packageID: String = ""
    private var gatewayCode: String = ""

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

    private var binding by autoClearedNullable<FragmentEventCheckoutBinding>()

    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(ENT_CHECKOUT_PERFORMANCE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        activity?.let {
            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
            if (saveInstanceManager != null) {
                saveInstanceManager.get(EXTRA_SAVED_DATA_ADDITIONAL_PACKAGE, EventCheckoutAdditionalData::class.java, EventCheckoutAdditionalData())?.let {
                    if (it.listForm.isNotEmpty()) {
                        eventCheckoutAdditionalDataPackage = it
                    }
                }

                saveInstanceManager.get(EXTRA_SAVED_DATA_ADDITIONAL_ITEM, object : TypeToken<MutableList<EventCheckoutAdditionalData>>() {}.type, mutableListOf<EventCheckoutAdditionalData>())?.let {
                    if (it.isNotEmpty()) {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventCheckoutBinding.inflate(inflater)
        return binding?.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eventCheckoutViewModel.eventProductDetail.observe(
            viewLifecycleOwner,
            Observer {
                it.run {
                    renderLayout(it)
                    binding?.globalErrorCheckoutEvent?.hide()
                    binding?.containerErrorEventCheckout?.hide()
                    performanceMonitoring.stopTrace()
                }
            }
        )

        eventCheckoutViewModel.eventTNCPDP.observe(
            viewLifecycleOwner,
            Observer {
                showTNC(it)
            }
        )

        eventCheckoutViewModel.isError.observe(
            viewLifecycleOwner,
            Observer {
                it?.let { error ->
                    if (it.error) {
                        binding?.run {
                            pgEventCheckout.gone()
                            context?.let {
                                EventGlobalError.errorEventHandlerGlobalError(
                                    it,
                                    error.throwable,
                                    containerErrorEventCheckout,
                                    globalErrorCheckoutEvent,
                                    { requestData() }
                                )
                            }
                        }
                    }
                }
            }
        )

        eventCheckoutViewModel.errorGeneralValue.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    val error = it
                    view?.let {
                        progressDialog.dismiss()
                        Toaster.build(
                            it,
                            ErrorHandler.getErrorMessage(context, error),
                            Snackbar.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            it.context.getString(R.string.ent_checkout_error)
                        ).show()
                    }
                }
            }
        )

        eventCheckoutViewModel.eventCheckoutResponse.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    val data = it
                    context?.let {
                        progressDialog.dismiss()
                        val context = it
                        if (data.checkout.data.success == 0) {
                            view?.let {
                                Toaster.build(
                                    it,
                                    data.checkout.data.message,
                                    Snackbar.LENGTH_LONG,
                                    Toaster.TYPE_ERROR,
                                    context.getString(R.string.ent_checkout_error)
                                ).show()
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
                                    Toaster.build(
                                        it,
                                        data.checkout.data.error,
                                        Snackbar.LENGTH_LONG,
                                        Toaster.TYPE_ERROR,
                                        context.getString(R.string.ent_checkout_error)
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        )

        eventCheckoutViewModel.eventCheckoutInstantResponse.observe(
            viewLifecycleOwner,
            Observer {
                val data = it
                context?.let {
                    progressDialog.dismiss()
                    val context = it
                    if (data.checkout.data.success == 0) {
                        view?.let {
                            Toaster.build(
                                it,
                                data.checkout.data.message,
                                Snackbar.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                context.getString(R.string.ent_checkout_error)
                            ).show()
                        }
                    } else {
                        RouteManager.route(context, data.checkout.data.data.redirectUrl)
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        requestData()
    }

    private fun requestData() {
        binding?.run {
            urlPDP.let {
                pgEventCheckout.show()
                globalErrorCheckoutEvent.hide()
                containerErrorEventCheckout.hide()
                eventCheckoutViewModel.getDataProductDetail(
                    eventPDPV3(),
                    eventContentById(),
                    it
                )
            }
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.ent_checkout_payment))
        progressDialog.setCancelable(false)
    }

    private fun renderLayout(eventProductDetailEntity: EventProductDetailEntity) {
        binding?.pgEventCheckout?.gone()
        binding?.containerCheckout?.show()
        (activity as EventCheckoutActivity).supportActionBar?.title = getString(R.string.ent_event_checkout_title_page)
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
        binding?.partialEventCheckoutDesc?.run {
            tgEventCheckoutDate.text =
                getDateString(DATE_FORMAT, getItemMap(metadata).scheduleTimestamp.toIntSafely())
            tgEventCheckoutName.text = pdp.displayName
            tgEventCheckoutPacket.text = getPackage(pdp, packageID).name
            ivEventCheckoutImage.loadImageRounded(pdp.imageApp, ROUND_VALUE)
        }
    }

    private fun renderPassenger(pdp: ProductDetailData) {
        if (forms.isNullOrEmpty()) {
            forms = initialListForm(pdp.forms, userSessionInterface, getString(R.string.ent_checkout_data_nullable_form))
        }
        if (!forms.isNullOrEmpty()) {
            setPassengerData(forms)
        }
        binding?.partialEventCheckoutPassenger?.tickerEventCheckout?.setTextDescription(context?.resources?.getString(R.string.ent_event_checkout_pessanger_ticker).orEmpty())
        if (!forms.isNullOrEmpty()) {
            binding?.partialEventCheckoutPassenger?.widgetEventCheckoutPessanger?.clickEditPassenger {
                goToPageForm()
            }
            binding?.partialEventCheckoutPassenger?.widgetEventCheckoutPessanger?.setOnClickListener {
                goToPageForm()
            }
        } else {
            binding?.partialEventCheckoutPassenger?.widgetEventCheckoutPessanger?.hideBtnCheckoutPassenger()
            binding?.partialEventCheckoutPassenger?.widgetEventCheckoutPessanger?.hide()
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

        binding?.partialEventCheckoutSummary?.rvEventCheckoutPrice?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterItemPrice
        }

        binding?.partialEventCheckoutSummary?.tgEventCheckoutSummaryPricePrice?.text = if (metadata.totalPrice != ZERO_PRICE) getRupiahFormat(metadata.totalPrice) else getString(R.string.ent_free_price)

        eventPDPTracking.onViewCheckoutPage(pdp, metadata.itemMap, userSessionInterface.userId)
    }

    private fun showTNC(tnc: String) {
        context?.let {
            binding?.partialEventCheckoutSummary?.tgEventCheckoutTnc?.makeLinks(
                Pair(
                    getString(R.string.ent_event_checkout_summary_tnc_click),
                    View.OnClickListener {
                        showBottomSheetTnc(it.context, tnc)
                    }
                )
            )
        }
    }

    private fun renderAdditionalItem(pdp: ProductDetailData) {
        if (listAdditionalItem.isEmpty()) {
            listAdditionalItem = getAdditionalList(metadata.itemMap, pdp, packageID)
        }
        if (!listAdditionalItem.isNullOrEmpty()) {
            isItemFormActive = true
            adapterAdditional.setList(listAdditionalItem)
            binding?.partialEventCheckoutAdditionalItem?.rvEventCheckoutAdditional?.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = adapterAdditional
            }
        } else {
            binding?.partialEventCheckoutAdditionalItem?.root?.gone()
        }
    }

    private fun renderAdditionalPackage(pdp: ProductDetailData) {
        if (eventCheckoutAdditionalDataPackage.listForm.isNullOrEmpty()) {
            eventCheckoutAdditionalDataPackage = getAdditionalPackage(pdp, packageID)
        }
        if (eventCheckoutAdditionalDataPackage.additionalType.equals(AdditionalType.NULL_DATA)) {
            binding?.partialEventCheckoutAdditionalPackage?.root?.gone()
        } else {
            isPackageFormActive = true
            updateAdditionalPackage()
            binding?.partialEventCheckoutAdditionalPackage?.itemCheckoutEventDataTambahanPackage?.root?.setOnClickListener {
                clickAdditional(eventCheckoutAdditionalDataPackage, REQUEST_CODE_ADDITIONAL_PACKAGE)
            }
            binding?.partialEventCheckoutAdditionalPackage?.itemCheckoutEventDataTambahanPackage?.imgEventPackageAdditional?.setOnClickListener {
                clickAdditional(eventCheckoutAdditionalDataPackage, REQUEST_CODE_ADDITIONAL_PACKAGE)
            }
        }
    }

    private fun updateAdditionalPackage() {
        if (eventCheckoutAdditionalDataPackage.additionalType.equals(AdditionalType.PACKAGE_FILLED) &&
            eventCheckoutAdditionalDataPackage.listForm.isNotEmpty()
        ) {
            binding?.partialEventCheckoutAdditionalPackage?.itemCheckoutEventDataTambahanPackage?.root?.gone()
            binding?.partialEventCheckoutAdditionalPackage?.itemCheckoutEventDataTambahanPackageFilled?.root?.show()
            val adapter = EventCheckoutPassengerDataAdapter()
            adapter.setList(mapFormToString(eventCheckoutAdditionalDataPackage.listForm))
            binding?.partialEventCheckoutAdditionalPackage?.itemCheckoutEventDataTambahanPackageFilled?.rvEventCheckoutAdditionalPackageFilled?.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                this.adapter = adapter
            }
            binding?.partialEventCheckoutAdditionalPackage?.itemCheckoutEventDataTambahanPackageFilled?.root?.setOnClickListener {
                clickAdditional(eventCheckoutAdditionalDataPackage, REQUEST_CODE_ADDITIONAL_PACKAGE)
            }
        }
    }

    private fun renderFooter(productDetailData: ProductDetailData) {
        binding?.partialEventCheckoutSummary?.cbEventCheckout?.setOnCheckedChangeListener { _, isChecked ->
            binding?.partialEventCheckoutFooter?.btnEventCheckout?.isEnabled = isChecked
        }
        binding?.partialEventCheckoutFooter?.btnEventCheckout?.text = if (metadata.totalPrice == ZERO_PRICE) {
            getString(R.string.ent_event_checkout_footer_button_free)
        } else {
            getString(R.string.ent_event_checkout_footer_button)
        }

        binding?.partialEventCheckoutFooter?.btnEventCheckout?.setOnClickListener {
            binding?.let { binding ->
                context?.let {
                    when {
                        !userSessionInterface.isLoggedIn -> {
                            Toaster.build(binding.root, it.getString(R.string.ent_event_checkout_submit_login), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                        }
                        !forms.isNullOrEmpty() && isEmptyForms(forms, getString(R.string.ent_checkout_data_nullable_form)) -> {
                            Toaster.build(binding.root, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_event_checkout_passenger_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            binding.scrollViewEventCheckout.focusOnView(binding.partialEventCheckoutPassenger.root)
                            binding.partialEventCheckoutPassenger.widgetEventCheckoutPessanger.startAnimationWiggle()
                        }
                        isAdditionalItemFormNull() && isItemFormActive -> {
                            Toaster.build(binding.root, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_checkout_data_pengunjung_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            binding.scrollViewEventCheckout.focusOnView(binding.partialEventCheckoutAdditionalItem.root)
                            getRecycleViewWidgetAnimator()
                        }
                        eventCheckoutAdditionalDataPackage.listForm.isEmpty() && isPackageFormActive -> {
                            Toaster.build(binding.root, it.getString(R.string.ent_event_checkout_submit_name, it.getString(R.string.ent_checkout_data_tambahan_title).toLowerCase()), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, it.getString(R.string.ent_checkout_error)).show()
                            binding.scrollViewEventCheckout.focusOnView(binding.partialEventCheckoutAdditionalPackage.root)
                            binding.partialEventCheckoutAdditionalPackage.itemCheckoutEventDataTambahanPackage.root.startAnimationWiggle()
                        }
                        else -> {
                            progressDialog.show()
                            eventPDPTracking.onClickCheckoutButton(productDetailData, metadata.itemMap, userSessionInterface.userId)
                            metadata = getPassengerMetaData(
                                metadata,
                                forms,
                                listAdditionalItem,
                                eventCheckoutAdditionalDataPackage,
                                it.getString(R.string.ent_checkout_data_nullable_form)
                            )
                            if (gatewayCode.isNullOrEmpty()) {
                                eventCheckoutViewModel.checkoutEvent(
                                    EventQuery.mutationEventCheckoutV2(),
                                    getCheckoutParam(metadata, productDetailData, getPackage(productDetailData, packageID))
                                )
                            } else {
                                eventCheckoutViewModel.checkoutEventInstant(
                                    getCheckoutParamInstant(gatewayCode, metadata, productDetailData, getPackage(productDetailData, packageID))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showBottomSheetTnc(context: Context, tnc: String) {
        val bindingBottomsheet = BottomSheetEventCheckoutBinding.inflate(
            LayoutInflater.from(context)
        )
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(bindingBottomsheet.root)
            setTitle(context.getString(R.string.ent_event_checkout_summary_tnc_bottom_sheet))
            setCloseClickListener { bottomSheets.dismiss() }
        }
        bindingBottomsheet.tgEventCheckoutTncBottomSheet.apply {
            text = tnc
        }
        bottomSheets.show(childFragmentManager, "")
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
            spannableString.setSpan(
                clickableSpan,
                startIndexOfLink,
                startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
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
                        val additionalData = data.getParcelableExtra(EXTRA_DATA_PESSANGER) ?: EventCheckoutAdditionalData()
                        listAdditionalItem[additionalData.position] = additionalData
                        adapterAdditional.notifyDataSetChanged()
                    }
                    REQUEST_CODE_ADDITIONAL_PACKAGE -> {
                        val additionalData = data.getParcelableExtra(EXTRA_DATA_PESSANGER) ?: EventCheckoutAdditionalData()
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
        binding?.partialEventCheckoutPassenger?.widgetEventCheckoutPessanger?.renderRecycleView(list)
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
        val itemView = binding?.partialEventCheckoutAdditionalItem?.rvEventCheckoutAdditional?.layoutManager?.findViewByPosition(positionAdditionalItemFormNull())
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
        Handler().post(
            Runnable {
                this.smoothScrollTo(0, toView.y.toIntSafely())
            }
        )
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
        const val ZERO_PRICE = 0L

        const val ROUND_VALUE = 25f

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

package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.DatePickerUnify
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.pdp.activity.EventPDPFormActivity
import com.tokopedia.entertainment.pdp.activity.EventPDPFormActivity.Companion.EXTRA_ADDITIONAL_DATA
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPFormViewModel
import javax.inject.Inject
import com.tokopedia.entertainment.pdp.activity.EventPDPFormActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.adapter.EventFormBottomSheetAdapter
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_pdp_form_fragment.*
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.searchHashMap
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.setListBottomSheetString
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_event_list_form.view.*
import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

class EventPDPFormFragment : BaseDaggerFragment(), OnClickFormListener,
        EventPDPTextFieldViewHolder.TextFormListener, EventFormBottomSheetAdapter.Listener{

    private var urlPDP = ""
    private var keyActiveBottomSheet = ""
    private var positionActiveForm = 0
    private var selectedCalendar: Calendar? = null
    var eventCheckoutAdditionalData = EventCheckoutAdditionalData()
    var listBottomSheetTemp : LinkedHashMap<String, String> = linkedMapOf()
    val bottomSheets = BottomSheetUnify()


    @Inject
    lateinit var viewModel: EventPDPFormViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var formAdapter: EventPDPFormAdapter

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        urlPDP = arguments?.getString(EXTRA_URL_PDP, "") ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ent_pdp_form_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapter()
        setupView()
        observeViewModel()
        setupData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupAdapter() {
        formAdapter = EventPDPFormAdapter(userSession, this, this)
    }

    private fun setupView() {
        setupTicker()
        setupRecycler()
        setupSimpanButton()
    }

    private fun setupTicker() {
        tickerText.setTextDescription(String.format(resources.getString(R.string.ent_pdp_form_ticker_warn_text)))
    }

    private fun setupRecycler() {
        recycler_view.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = formAdapter
        }
    }

    private fun setupSimpanButton() {
        simpanBtn.setOnClickListener {
            if (formAdapter.getError(resources).isNotEmpty()) {
                view?.let {
                    val typeTitle = when (eventCheckoutAdditionalData.additionalType) {
                        AdditionalType.ITEM_UNFILL, AdditionalType.ITEM_FILLED -> resources.getString(R.string.ent_checkout_data_pengunjung_title)
                        AdditionalType.PACKAGE_UNFILL, AdditionalType.PACKAGE_FILLED -> resources.getString(R.string.ent_checkout_data_tambahan_title)
                        else -> resources.getString(R.string.ent_pdp_title_form)
                    }
                    val errorForm = String.format(resources.getString(R.string.ent_pdp_form_error_all_msg_fragment), typeTitle.toLowerCase().capitalize())
                    Toaster.build(it, errorForm, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, String.format(resources.getString(R.string.ent_pdp_form_toaster_click_msg))).show()
                }
            } else {
                activity?.run {
                    val intent = Intent()
                    if(eventCheckoutAdditionalData.additionalType.equals(AdditionalType.NULL_DATA)) {
                        intent.putExtra(EXTRA_DATA_PESSANGER, formAdapter.formData as Serializable)
                    } else {
                        eventCheckoutAdditionalData.listForm = formAdapter.formData
                        if(eventCheckoutAdditionalData.additionalType.equals(AdditionalType.ITEM_UNFILL) ||
                                eventCheckoutAdditionalData.additionalType.equals(AdditionalType.ITEM_FILLED)){
                            eventCheckoutAdditionalData.additionalType = AdditionalType.ITEM_FILLED
                        } else if(eventCheckoutAdditionalData.additionalType.equals(AdditionalType.PACKAGE_UNFILL) ||
                                eventCheckoutAdditionalData.additionalType.equals(AdditionalType.PACKAGE_FILLED)){
                            eventCheckoutAdditionalData.additionalType = AdditionalType.PACKAGE_FILLED
                        }
                        intent.putExtra(EXTRA_DATA_PESSANGER, eventCheckoutAdditionalData)
                    }
                    this.setResult(RESULT_OK, intent)
                    this.finish()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.mFormData.observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            renderList(it)
            showData()
        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showData() {
        recycler_view.visibility = View.VISIBLE
        tickerText.visibility = View.VISIBLE
        simpanBtn.visibility = View.VISIBLE
    }

    private fun setupData() {
        val listForm = activity?.intent?.getSerializableExtra(EXTRA_DATA_PESSANGER)
        eventCheckoutAdditionalData = activity?.intent?.extras?.getParcelable(EXTRA_ADDITIONAL_DATA)
                ?: EventCheckoutAdditionalData(additionalType = AdditionalType.NULL_DATA)

        if (listForm != null) {
            val listFormPemesan = listForm as List<Form>
            if (listFormPemesan.isNotEmpty()) {
                hideProgressBar()
                renderList(listForm.toMutableList())
                showData()
            } else {
                requestData(eventCheckoutAdditionalData)
            }
        }

        if (eventCheckoutAdditionalData.additionalType != AdditionalType.NULL_DATA) {
            hideProgressBar()
            if (eventCheckoutAdditionalData.additionalType == AdditionalType.ITEM_FILLED ||
                    eventCheckoutAdditionalData.additionalType == AdditionalType.PACKAGE_FILLED){
                renderList(eventCheckoutAdditionalData.listForm.toMutableList())
            } else requestData(eventCheckoutAdditionalData)
            showData()
        }

        (activity as EventPDPFormActivity).toolbarForm.title = getTitle()
    }

    private fun requestData(eventCheckoutAdditionalData: EventCheckoutAdditionalData) {
        viewModel.getData(urlPDP, EventQuery.eventPDPV3(),
                eventContentById(), eventCheckoutAdditionalData)
    }

    private fun renderList(formData: MutableList<Form>) {
        formAdapter.setList(formData)
    }

    override fun clickBottomSheet(list: LinkedHashMap<String, String>, title: String, positionForm: Int) {
        val adapterBottomSheet = EventFormBottomSheetAdapter(this)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_list_form, null)
        listBottomSheetTemp = list
        positionActiveForm = positionForm
        bottomSheets.apply {
            isFullpage = true
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }

        if (list.size > SEARCH_PAGE_LIMIT) {
            val searchTextField = view.event_search_list_form?.searchBarTextField
            val searchClearButton = view.event_search_list_form?.searchBarIcon

            view.event_search_list_form.searchBarPlaceholder = resources.getString(R.string.ent_bottomsheet_placeholder, title)
            searchTextField?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (keyword.toString().isEmpty()) {
                        listBottomSheetTemp = list
                        adapterBottomSheet.setList(setListBottomSheetString(list))
                    } else {
                        listBottomSheetTemp = searchHashMap(keyword.toString(), list)
                        adapterBottomSheet.setList(setListBottomSheetString(listBottomSheetTemp))
                    }
                }
            })
            searchClearButton?.setOnClickListener {
                searchTextField?.text?.clear()
                listBottomSheetTemp = list
                adapterBottomSheet.setList(setListBottomSheetString(list))
            }
        } else {
            view.event_search_list_form.visibility = View.GONE
        }

        adapterBottomSheet.setList(setListBottomSheetString(list))

        view.rv_event_bottom_sheet_form.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = adapterBottomSheet
        }

        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    private fun getTitle():String {
        return if(eventCheckoutAdditionalData.additionalType.equals(AdditionalType.NULL_DATA)) {
            String.format(resources.getString(R.string.ent_pdp_title_form))
        } else if(eventCheckoutAdditionalData.additionalType.equals(AdditionalType.ITEM_FILLED)
                || eventCheckoutAdditionalData.additionalType.equals(AdditionalType.ITEM_UNFILL)) {
             eventCheckoutAdditionalData.titleItem
        } else if(eventCheckoutAdditionalData.additionalType.equals(AdditionalType.PACKAGE_FILLED)
                || eventCheckoutAdditionalData.additionalType.equals(AdditionalType.PACKAGE_UNFILL)) {
            String.format(resources.getString(R.string.ent_checkout_data_tambahan_title))
        } else {
            String.format(resources.getString(R.string.ent_pdp_title_form))
        }

    }

    override fun getKeyActive(): String {
        return keyActiveBottomSheet
    }

    override fun resetActiveKey() {
        keyActiveBottomSheet = ""
    }

    override fun getAdditionalType(): AdditionalType {
        return eventCheckoutAdditionalData.additionalType
    }

    override fun getActiveKeyPosition(position: Int) {
        keyActiveBottomSheet = listBottomSheetTemp.getKeyByPosition(position)
        formAdapter.notifyItemChanged(positionActiveForm)
        bottomSheets.dismiss()
    }

    override fun clickDatePicker(title: String, helpText: String,  position: Int) {
        context?.let{
            val calMax = Calendar.getInstance()
            calMax.add(Calendar.YEAR, MAX_YEAR);
            val yearMax = calMax.get(Calendar.YEAR)
            val monthMax = calMax.get(Calendar.MONTH)
            val dayMax = calMax.get(Calendar.DAY_OF_MONTH)

            val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            val calMin = Calendar.getInstance()
            calMin.add(Calendar.YEAR, MIN_YEAR);
            val yearMin = calMin.get(Calendar.YEAR)
            val monthMin = calMin.get(Calendar.MONTH)
            val dayMin = calMin.get(Calendar.DAY_OF_MONTH)

            val minDate = GregorianCalendar(yearMin, monthMin, dayMin)
            var datepickerObject = DateTimePickerUnify(it, minDate, currentDate, maxDate).apply {
                 setTitle(title)
                 setInfo(helpText)
                 setInfoVisible(true)
                 datePickerButton.let { button ->
                    button.setOnClickListener {
                        selectedCalendar = getDate()
                        formAdapter.notifyItemChanged(position)
                        dismiss()
                    }
                }
            }
            fragmentManager?.let {
                datepickerObject.show(it, "")
            }
        }
    }

    override fun getDate(): Calendar? {
       return selectedCalendar
    }

    companion object {
        fun newInstance(url: String) = EventPDPFormFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, url)
            }
        }

        const val EXTRA_DATA_PESSANGER = "EXTRA_DATA_PESSANGER"
        val REQUEST_CODE = 100
        const val SEARCH_PAGE_LIMIT = 10
        const val DELAY_CONST: Long = 100

        const val MAX_YEAR = 10
        const val MIN_YEAR = -90
    }

    fun LinkedHashMap<String, String>.getKeyByPosition(position: Int) =
            this.keys.toTypedArray()[position]


    fun LinkedHashMap<String, String>.getValueByPosition(position: Int) =
            this.values.toTypedArray()[position]
}
package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.pdp.activity.EventPDPFormActivity.Companion.EXTRA_ADDITIONAL_DATA
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPFormViewModel
import javax.inject.Inject
import com.tokopedia.entertainment.pdp.activity.EventPDPFormActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_pdp_form_fragment.*
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.EMPTY_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.REGEX_TYPE
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventPDPTextFieldViewHolder
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.getSearchableList
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.setListBottomSheetForm
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_event_list_form.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_category.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

class EventPDPFormFragment : BaseDaggerFragment(), OnClickFormListener, EventPDPTextFieldViewHolder.TextFormListener {

    private var urlPDP = ""
    private var positionActiveData = 0

    @Inject
    lateinit var viewModel: EventPDPFormViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var formAdapter: EventPDPFormAdapter

    override fun getScreenName(): String = String.format(resources.getString(R.string.ent_pdp_title_form))

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
            if (formAdapter.getError().first.isNotBlank()) {
                view?.let {
                    if (formAdapter.getError().second == EMPTY_TYPE) {
                        Toaster.make(it, String.format(resources.getString(R.string.ent_pdp_form_error_empty_value_msg), formAdapter.getError().first), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, String.format(resources.getString(R.string.ent_pdp_form_toaster_click_msg)))
                    } else if (formAdapter.getError().second == REGEX_TYPE) {
                        Toaster.make(it, String.format(resources.getString(R.string.ent_pdp_form_error_regex_msg), formAdapter.getError().first), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, String.format(resources.getString(R.string.ent_pdp_form_toaster_click_msg)))
                    }
                }
            } else {
                activity?.run {
                    val intent = Intent()
                    intent.putExtra(EXTRA_DATA_PESSANGER, formAdapter.formData as Serializable)
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
        val eventCheckoutAdditionalData = activity?.intent?.extras?.getParcelable(EXTRA_ADDITIONAL_DATA)
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
            requestData(eventCheckoutAdditionalData)
            showData()
        }

    }

    private fun requestData(eventCheckoutAdditionalData: EventCheckoutAdditionalData) {
        viewModel.getData(urlPDP, EventQuery.eventPDPV3(),
                eventContentById(), eventCheckoutAdditionalData)
    }

    private fun renderList(formData: MutableList<Form>) {
        formAdapter.setList(formData)
    }

    override fun clickBottomSheet(list: List<String>, title: String, positionForm: Int, positionActiveBottomSheet: Int) {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_list_form, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            isFullpage = true
            setChild(view)
            setTitle("Pilih "+title)
            setCloseClickListener { bottomSheets.dismiss() }
        }

        var listBottomSheet = setListBottomSheetForm(list)

        if (list.size > SEARCH_PAGE_LIMIT) {
            val searchTextField = view.event_search_list_form?.searchBarTextField
            val searchClearButton = view.event_search_list_form?.searchBarIcon

            searchTextField?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(keyword.toString().isEmpty()) {
                        listBottomSheet = setListBottomSheetForm(list)
                        view.list_form.setData(listBottomSheet)
                    } else {
                        listBottomSheet = getSearchableList(keyword.toString(), list)
                        view.list_form.setData(listBottomSheet)
                    }
                }
            })
            searchClearButton?.setOnClickListener {
                searchTextField?.text?.clear()
                listBottomSheet = setListBottomSheetForm(list)
                view.list_form.setData(listBottomSheet)
            }
        } else {
            view.event_search_list_form.visibility = View.GONE
        }


        view.list_form.apply {
            setData(listBottomSheet)
            onLoadFinish {
                listBottomSheet.forEachIndexed { index, it ->
                    it.listTitle?.setOnClickListener {
                        positionActiveData = index
                        formAdapter.notifyItemChanged(positionForm)
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(DELAY_CONST)
                            bottomSheets.dismiss()
                        }
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    override fun getPositionActive(): Int {
        return positionActiveData
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
        const val DELAY_CONST : Long = 250
    }
}
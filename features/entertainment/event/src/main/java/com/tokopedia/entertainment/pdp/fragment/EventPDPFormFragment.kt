package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPFormViewModel
import javax.inject.Inject
import com.tokopedia.entertainment.pdp.activity.EventPDPFormActivity.Companion.EXTRA_URL_PDP
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_pdp_form_fragment.*
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.EMPTY_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.REGEX_TYPE
import timber.log.Timber
import java.io.Serializable

class EventPDPFormFragment : BaseDaggerFragment(){

    private var urlPDP = ""

    @Inject
    lateinit var viewModel: EventPDPFormViewModel
    @Inject
    lateinit var userSession:UserSessionInterface

    lateinit var formAdapter: EventPDPFormAdapter

    var mDataIntent: List<Form> = listOf()

    override fun getScreenName(): String = String.format(resources.getString(R.string.ent_pdp_title_form))

    override fun initInjector() { getComponent(EventPDPComponent::class.java).inject(this) }

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

    private fun setupAdapter(){
        formAdapter = EventPDPFormAdapter(userSession)
    }

    private fun setupView(){
        setupTicker()
        setupRecycler()
        setupSimpanButton()
    }

    private fun setupTicker(){
        tickerText.setTextDescription(String.format(resources.getString(R.string.ent_pdp_form_ticker_warn_text)))
    }

    private fun setupRecycler(){
        recycler_view.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = formAdapter
        }
    }

    private fun setupSimpanButton(){
        simpanBtn.setOnClickListener {
            if(formAdapter.getError().first.isNotBlank()) {
                view?.let {
                    if (formAdapter.getError().second == EMPTY_TYPE) {
                        Toaster.make(it, String.format(resources.getString(R.string.ent_pdp_form_error_empty_value_msg), formAdapter.getError().first), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, String.format(resources.getString(R.string.ent_pdp_form_toaster_click_msg)))
                    } else if (formAdapter.getError().second == REGEX_TYPE) {
                        Toaster.make(it, String.format(resources.getString(R.string.ent_pdp_form_error_regex_msg), formAdapter.getError().first), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, String.format(resources.getString(R.string.ent_pdp_form_toaster_click_msg)))
                    }
                }
            } else {
                activity?.run{
                    val intent = Intent()
                    intent.putExtra(EXTRA_DATA_PESSANGER, formAdapter.formData as Serializable)
                    this.setResult(RESULT_OK, intent)
                    this.finish()
                }
            }
        }
    }

    private fun observeViewModel(){
        viewModel.mFormData.observe(this, Observer {
            hideProgressBar()
            renderList(it)
            showData()
        })
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    private fun showData(){
        recycler_view.visibility = View.VISIBLE
        tickerText.visibility = View.VISIBLE
        simpanBtn.visibility = View.VISIBLE
    }

    private fun setupData() {
        val dataIntent = activity?.intent
        mDataIntent = dataIntent?.getSerializableExtra(EXTRA_DATA_PESSANGER) as List<Form>

        if(mDataIntent.isNotEmpty()){
            hideProgressBar()
            renderList(mDataIntent.toMutableList())
            showData()
        } else{
            viewModel.getData(urlPDP, GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_product_detail_v3),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_content_by_id))
        }
    }

    private fun renderList(formData: MutableList<Form>){
        formAdapter.setList(formData)
    }

    companion object{
        fun newInstance(url: String) = EventPDPFormFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, url)
            }
        }

        const val EXTRA_DATA_PESSANGER = "EXTRA_DATA_PESSANGER"
        val REQUEST_CODE = 100
    }
}
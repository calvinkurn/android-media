package com.rahullohra.fakeresponse.chuck.presentation.activities

import android.os.Bundle
import android.text.Editable
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.rahullohra.fakeresponse.FakeResponseTextWatcher
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.Router
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.chuck.presentation.adapters.SearchAdapter
import com.rahullohra.fakeresponse.chuck.presentation.viewmodels.SearchViewModel
import com.rahullohra.fakeresponse.data.diProvider.activities.SearchActivityProvider
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.Loading
import com.rahullohra.fakeresponse.presentaiton.livedata.Success

class SearchActivity : AppCompatActivity() {

    fun getLayout() = R.layout.fake_search_activity
    lateinit var etResponse: TextInputEditText
    lateinit var etRequest: TextInputEditText
    lateinit var etUrl: TextInputEditText
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var viewFlipper: ViewFlipper
    lateinit var tvResult: AppCompatTextView

    lateinit var viewModel: SearchViewModel
    lateinit var adapter: SearchAdapter
    val searchList = arrayListOf<TransactionEntity>()

    var responseText = ""
    var requestText = ""
    var urlText = ""

    val CONTAINER_LIST = 0
    val CONTAINER_PROGRESS = 1
    val CONTAINER_EMPTY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        initVars()
        initUi()
        setObservers()
        performSearch()
    }

    fun initVars() {
        SearchActivityProvider().inject(this)
    }

    fun initUi() {
        etResponse = findViewById(R.id.etResponse)
        etRequest = findViewById(R.id.etRequest)
        etUrl = findViewById(R.id.etUrl)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rv)
        viewFlipper = findViewById(R.id.viewFlipper)
        tvResult = findViewById(R.id.tvResult)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = SearchAdapter(searchList) {
            openEditActivity(it)
        }
        recyclerView.adapter = adapter
    }

    fun setObservers() {
        etResponse.addTextChangedListener(object : FakeResponseTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                responseText = s?.toString() ?: ""
                performSearch()
            }
        })
        etRequest.addTextChangedListener(object : FakeResponseTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                requestText = s?.toString() ?: ""
                performSearch()
            }
        })
        etUrl.addTextChangedListener(object : FakeResponseTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                urlText = s?.toString() ?: ""
                performSearch()
            }
        })

        viewModel.searchLiveData.observe(this, Observer {
            when (it) {
                is Success<List<TransactionEntity>> -> {
                    viewFlipper.displayedChild = CONTAINER_LIST
                    searchList.clear()
                    searchList.addAll(it.data)
                    adapter.notifyDataSetChanged()

                    if (searchList.isEmpty()) {
                        viewFlipper.displayedChild = CONTAINER_EMPTY
                        tvResult.text = "No matching record found"
                    }
                }
                is Fail -> {
                    viewFlipper.displayedChild = CONTAINER_EMPTY
                    tvResult.text = it.ex.message
                }
                is Loading -> {
                }
            }
        })
    }

    fun performSearch() {
        viewModel.search(responseBody = responseText, requestBody = requestText, url = urlText)
    }

    fun openEditActivity(transactionEntity: TransactionEntity) {
        if (transactionEntity.isGql) {
            Router.routeToAddGql(this, transactionEntity.id?.toInt(), true)
        } else {
            Router.routeToAddRest(this, transactionEntity.id?.toInt(), true)
        }
    }
}
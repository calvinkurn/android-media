package com.tokopedia.fakeresponse.chuck.presentation.activities

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.ViewFlipper
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.tokopedia.fakeresponse.FakeResponseTextWatcher
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.Router
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.presentation.adapters.SearchAdapter
import com.tokopedia.fakeresponse.chuck.presentation.viewmodels.SearchViewModel
import com.tokopedia.fakeresponse.data.diProvider.activities.SearchActivityProvider
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.data.models.SearchType
import com.tokopedia.fakeresponse.presentation.activities.BaseActivity
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.Loading
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.toast

class SearchActivity : BaseActivity() {

    override fun getLayout() = R.layout.fake_search_activity
    lateinit var etResponse: TextInputEditText
    lateinit var etRequest: TextInputEditText
    lateinit var etUrl: TextInputEditText
    lateinit var etTag: TextInputEditText
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var viewFlipper: ViewFlipper
    lateinit var tvResult: AppCompatTextView
    lateinit var toolbar: Toolbar

    lateinit var viewModel: SearchViewModel
    lateinit var adapter: SearchAdapter
    val searchList = arrayListOf<SearchType>()

    var responseText = ""
    var requestText = ""
    var urlText = ""
    var tag = ""

    val CONTAINER_LIST = 0
    val CONTAINER_PROGRESS = 1
    val CONTAINER_EMPTY = 2

    var isSelectionMode = false

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
        etTag = findViewById(R.id.etTag)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rv)
        viewFlipper = findViewById(R.id.viewFlipper)
        tvResult = findViewById(R.id.tvResult)
        toolbar = findViewById(R.id.toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val dividerLine = ContextCompat.getDrawable(this, R.drawable.fake_divider_line)
        dividerLine?.let {
            itemDecoration.setDrawable(it)
        }

        recyclerView.addItemDecoration(itemDecoration)
        adapter = SearchAdapter(searchList, {
            openEditActivity(it as TransactionEntity)
        }, { type, isChecked -> })

        recyclerView.adapter = adapter

        setToolbar()
    }

    fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            toggleSelectionMode(false)
        }
        val icon = if (isSelectionMode) R.drawable.fake_ic_close else R.drawable.fake_ic_back
        toolbar.setNavigationIcon(icon)
    }

    fun toggleSelectionMode(selectionMode: Boolean) {
        isSelectionMode = selectionMode
        searchList.forEach {
            it.isInExportMode = isSelectionMode
        }
        adapter.notifyDataSetChanged()

        val icon = if (isSelectionMode) R.drawable.fake_ic_close else R.drawable.fake_ic_back
        toolbar.setNavigationIcon(icon)
        invalidateOptionsMenu()
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

        etTag.addTextChangedListener(object : FakeResponseTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tag = s?.toString() ?: ""
                performSearch()
            }
        })

        viewModel.searchLiveData.observe(this, Observer {
            when (it) {
                is Success<Pair<List<TransactionEntity>, List<ResponseListData>>> -> {
                    viewFlipper.displayedChild = CONTAINER_LIST
                    searchList.clear()
                    searchList.addAll(it.data.second)
                    searchList.addAll(it.data.first)
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

        viewModel.exportLiveData.observe(this, Observer {
            when (it) {
                is Success<String> -> sendData(it.data)
                is Fail -> toast(it.ex.message)
                else -> {
                    // no-op
                }
            }
        })
    }

    fun performSearch() {
        viewModel.search(responseBody = responseText, requestBody = requestText, url = urlText, tag = tag)
    }

    fun openEditActivity(transactionEntity: TransactionEntity) {
        if (transactionEntity.isGql) {
            Router.routeToAddGql(this, transactionEntity.id?.toInt(), true)
        } else {
            Router.routeToAddRest(this, transactionEntity.id?.toInt(), true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuRes = if (isSelectionMode) R.menu.gql_send_menu else R.menu.gql_search_menu
        menuInflater.inflate(menuRes, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gql_menu_send -> {
                performSendOperation()
            }
            R.id.gql_menu_share -> {
                toggleSelectionMode(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun performSendOperation() {
        viewModel.export(searchList)
    }
}
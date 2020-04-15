package com.rahullohra.fakeresponse.presentation.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.google.gson.JsonParser
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.Router
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.data.diProvider.activities.RestActivityDiProvider
import com.rahullohra.fakeresponse.db.entities.RestRecord
import com.rahullohra.fakeresponse.presentation.livedata.Fail
import com.rahullohra.fakeresponse.presentation.livedata.Loading
import com.rahullohra.fakeresponse.presentation.livedata.Success
import com.rahullohra.fakeresponse.presentation.viewmodels.AddRestVM
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddRestData
import com.rahullohra.fakeresponse.toast

class AddRestResponseActivity : BaseActivity() {

    lateinit var etRestUrl: EditText
    lateinit var etMethodName: EditText
    lateinit var etResponse: EditText
    lateinit var etTag: EditText
    lateinit var toolbar: Toolbar

    override fun getLayout() = R.layout.activity_add_rest
    lateinit var viewModel: AddRestVM

    var id: Int? = null
    var isFromChuck: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initVars()
        initUi()
        setObservers()
        getDataFromId()
    }

    fun getDataFromId() {
        id?.let {
            if (isFromChuck) {
                viewModel.loadRecordFromChuck(it)
            } else {
                viewModel.loadRecord(it)
            }
        }
    }

    fun initUi() {
        etRestUrl = findViewById(R.id.etRest)
        etMethodName = findViewById(R.id.etMethodName)
        etResponse = findViewById(R.id.etResponse)
        etTag = findViewById(R.id.etTag)
        toolbar = findViewById(R.id.toolbar)

        id = intent.extras?.get(Router.BUNDLE_ARGS_ID) as Int?
        isFromChuck = intent.extras?.get(Router.BUNDLE_ARGS_FROM_CHUCK) as Boolean? ?: false
        setSupportActionBar(toolbar)
    }

    fun initVars() {
        RestActivityDiProvider().inject(this)
    }

    fun setObservers() {

        viewModel.liveDataRestResponse.observe(this, Observer {
            when (it) {
                is Success<RestRecord> -> {
                    etMethodName.setText(it.data.httpMethod)
                    etRestUrl.setText(it.data.url)
                    etResponse.setText(it.data.response)
                    etTag.setText(it.data.customTag)
                }
            }
        })

        viewModel.liveDataCreate.observe(this, Observer {
            when (it) {
                is Success<Long> -> {
                    toast("New entry Added")
                }
                is Fail -> {
                    toast(it.ex.message)
                }
                is Loading -> {
                }
            }
        })

        viewModel.liveDataUpdate.observe(this, Observer {
            when (it) {
                is Success<Boolean> -> {
                    toast("New entry Updated")
                }
                is Fail -> {
                    toast(it.ex.message)
                }
                is Loading -> {
                }
            }
        })

        viewModel.liveDataTransactionEntity.observe(this, Observer {
            when(it){
                is Success<TransactionEntity>->{
                    updateUi(it.data)
                }
                is Fail->{
                    toast(it.ex.message)
                }
            }
        })
    }


    fun updateUi(transactionEntity: TransactionEntity){

        etMethodName.setText(transactionEntity.method?:"")
        etRestUrl.setText(transactionEntity.url?:"")

        var response = transactionEntity.responseBody?:""
        response = gson.toJson(JsonParser().parse(response))
        etResponse.setText(response)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gql_add_response_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gql_menu_save -> {
                saveData()
            }
            R.id.gql_menu_pretty -> {
                try {
                    var response = etResponse.text.toString()
                    response = gson.toJson(JsonParser().parse(response))
                    etResponse.setText(response)
                } catch (e: Exception) {
                    Toast.makeText(this, "Wrong Json", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    fun saveData() {

        val addRestData = AddRestData(
                etRestUrl.text.toString(),
                etMethodName.text.toString(),
                etResponse.text.toString(),
                etTag.text.toString()
        )

        if (isExistingRecord()) {
            viewModel.updateRecord(id!!, addRestData)
        } else {
            viewModel.addRecord(addRestData)
        }

    }

    fun isExistingRecord(): Boolean {
        return id != null && !isFromChuck
    }
}
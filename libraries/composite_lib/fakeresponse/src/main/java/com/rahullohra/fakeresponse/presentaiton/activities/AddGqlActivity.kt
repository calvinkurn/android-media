package com.rahullohra.fakeresponse.presentaiton.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.data.diProvider.activities.AddGqlActivityProvider
import com.rahullohra.fakeresponse.db.entities.FakeGql
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.Loading
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import com.rahullohra.fakeresponse.presentaiton.viewmodels.AddGqlVM
import com.rahullohra.fakeresponse.presentaiton.viewmodels.data.AddGqlData
import com.rahullohra.fakeresponse.toast

class AddGqlActivity : BaseActivity() {

    lateinit var etGqlName: EditText
    lateinit var etCustomName: EditText
    lateinit var etResponse: EditText
    lateinit var toolbar: Toolbar

    override fun getLayout() = R.layout.activity_add_gql
    lateinit var viewModel: AddGqlVM

    var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initVars()
        initUi()
        setListeners()
        getDataFromId()
    }

    fun getDataFromId() {
        id?.let {
            viewModel.loadRecord(it)
        }
    }

    fun initUi() {
        etGqlName = findViewById(R.id.etGql)
        etCustomName = findViewById(R.id.etCustomName)
        etResponse = findViewById(R.id.etResponse)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
    }

    fun initVars() {
        AddGqlActivityProvider().inject(this)
    }

    fun setListeners() {

        viewModel.liveDataGqlResponse.observe(this, Observer {
            when (it) {
                is Success<FakeGql> -> {
                    etGqlName.setText(it.data.gqlOperationName)
                    etResponse.setText(it.data.response)
                    etCustomName.setText(it.data.customTag)
                }
            }
        })

        viewModel.liveDataCreate.observe(this, Observer {
            when (it) {
                is Success<Long> -> {
                    toast("New entry added")
                }
                is Fail -> {
                    toast(it.ex.message)
                }
                is Loading -> {
                }
            }
        })

        viewModel.liveDataGqlUpdate.observe(this, Observer {
            when (it) {
                is Success<Boolean> -> {
                    toast("New entry updated")
                }
                is Fail -> {
                    toast(it.ex.message)
                }
                is Loading -> {
                }
            }
        })
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
        }
        return true
    }

    fun saveData() {
        val customName = etCustomName.text.toString()
        val gqlName = etGqlName.text.toString()
        val response = etResponse.text.toString()

        val addGqlData =
                AddGqlData(gqlQueryName = gqlName, response = response, customTag = customName)

        if (isExistingRecord()) {
            viewModel.updateRecord(addGqlData)
        } else {
            viewModel.addToDb(addGqlData)
        }
    }

    fun isExistingRecord(): Boolean {
        return id != null
    }
}

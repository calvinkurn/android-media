package com.rahullohra.fakeresponse.presentaiton.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.Router
import com.rahullohra.fakeresponse.data.diProvider.activities.AddGqlActivityProvider
import com.rahullohra.fakeresponse.db.entities.GqlRecord
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

    override fun getLayout() = R.layout.fake_activity_add_gql
    lateinit var viewModel: AddGqlVM
    private val gson = GsonBuilder().setPrettyPrinting().create()

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

        id = intent.extras?.get(Router.BUNDLE_ARGS_ID) as Int?

        setSupportActionBar(toolbar)
    }

    fun initVars() {
        AddGqlActivityProvider().inject(this)
    }

    fun setListeners() {

        viewModel.liveDataGqlResponse.observe(this, Observer {
            when (it) {
                is Success<GqlRecord> -> {
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
        val customName = etCustomName.text.toString()
        val gqlName = etGqlName.text.toString()
        val response = etResponse.text.toString()

        val addGqlData =
                AddGqlData(gqlQueryName = gqlName, response = response, customTag = customName)

        if (isExistingRecord()) {
            viewModel.updateRecord(id!!, addGqlData)
        } else {
            viewModel.addToDb(addGqlData)
        }
    }

    fun isExistingRecord(): Boolean {
        return id != null
    }
}

package com.rahullohra.fakeresponse.presentaiton.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.lifecycle.Observer
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.data.diProvider.activities.AddGqlActivityProvider
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

    override fun getLayout() = R.layout.activity_add_gql
    lateinit var viewModel: AddGqlVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initVars()
        initUi()
        setListeners()
    }

    fun initUi() {
        etGqlName = findViewById(R.id.etGql)
        etCustomName = findViewById(R.id.etCustomName)
        etResponse = findViewById(R.id.etResponse)
    }

    fun initVars() {
        AddGqlActivityProvider().inject(this)
    }

    fun setListeners() {
        viewModel.liveData.observe(this, Observer {
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
        viewModel.addToDb(addGqlData)
    }
}

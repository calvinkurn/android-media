package com.rahullohra.fakeresponse.presentaiton.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.data.diProvider.activities.RestActivityDiProvider
import com.rahullohra.fakeresponse.presentaiton.livedata.Fail
import com.rahullohra.fakeresponse.presentaiton.livedata.Loading
import com.rahullohra.fakeresponse.presentaiton.livedata.Success
import com.rahullohra.fakeresponse.presentaiton.viewmodels.AddRestVM
import com.rahullohra.fakeresponse.presentaiton.viewmodels.data.AddRestData
import com.rahullohra.fakeresponse.toast

class AddRestResponseActivity : BaseActivity() {

    lateinit var etRest: EditText
    lateinit var etMethodName: EditText
    lateinit var etResponse: EditText
    lateinit var toolbar: Toolbar

    override fun getLayout() = R.layout.activity_add_rest
    lateinit var viewModel: AddRestVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initVars()
        initUi()
        setListeners()
    }

    fun initUi() {
        etRest = findViewById(R.id.etRest)
        etMethodName = findViewById(R.id.etMethodName)
        etResponse = findViewById(R.id.etResponse)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
    }

    fun initVars() {
        RestActivityDiProvider().inject(this)
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

        val addRestData = AddRestData(
            etRest.text.toString(),
            etMethodName.text.toString(),
            etResponse.text.toString()
        )

        viewModel.addToDb(addRestData)
    }
}
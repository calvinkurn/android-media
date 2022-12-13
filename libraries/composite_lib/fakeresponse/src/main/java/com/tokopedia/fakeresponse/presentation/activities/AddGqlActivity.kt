package com.tokopedia.fakeresponse.presentation.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.Router
import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.data.diProvider.activities.AddGqlActivityProvider
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.data.parsers.ParserRuleProvider
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.Loading
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.presentation.viewmodels.AddGqlVM
import com.tokopedia.fakeresponse.presentation.viewmodels.data.AddGqlData
import com.tokopedia.fakeresponse.toast
import kotlinx.android.synthetic.main.fake_activity_add_gql.*
import org.json.JSONArray

class AddGqlActivity : BaseActivity() {

    lateinit var etGqlName: EditText
    lateinit var etCustomName: EditText
    lateinit var etResponse: EditText
    lateinit var toolbar: Toolbar
    var radioGroup: RadioGroup? = null

    override fun getLayout() = R.layout.fake_activity_add_gql
    lateinit var viewModel: AddGqlVM

    var id: Int? = null
    var isFromChuck: Boolean = false
    var isResponseSuccess = true
    var isDelayResponseEnable = false

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
            if (isFromChuck) {
                viewModel.loadRecordFromChuck(it)
            } else {
                viewModel.loadRecord(it)
                supportActionBar?.title = "Update Gql"
            }
        }
    }

    fun initUi() {
        etGqlName = findViewById(R.id.etGql)
        etCustomName = findViewById(R.id.etCustomName)
        etResponse = findViewById(R.id.etResponse)
        toolbar = findViewById(R.id.toolbar)
        radioGroup = findViewById(R.id.rgCheckedResponseState)
        radioGroup?.setOnCheckedChangeListener { _, i ->
            isResponseSuccess = (i == R.id.rbCheckedSuccess)
        }
        cbDelayResponse.setOnCheckedChangeListener { compoundButton, b ->
            isDelayResponseEnable = b
        }
        id = intent.extras?.get(Router.BUNDLE_ARGS_ID) as Int?
        isFromChuck = intent.extras?.get(Router.BUNDLE_ARGS_FROM_CHUCK) as Boolean? ?: false

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Gql"
        toolbar.setNavigationIcon(R.drawable.fake_ic_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun initVars() {
        AddGqlActivityProvider().inject(this)
    }

    fun setListeners() {

        viewModel.liveDataGqlResponse.observe(this, Observer {
            when (it) {
                is Success<GqlRecord> -> {
                    etGqlName.setText(it.data.gqlOperationName)
                    etCustomName.setText(it.data.customTag)
                    isResponseSuccess = it.data.isResponseSuccess
                    if (it.data.isResponseSuccess) {
                        rbCheckedSuccess.isChecked = true
                    } else {
                        rbCheckedError.isChecked = true
                    }

                    cbDelayResponse.isChecked = it.data.isDelayResponse

                    try {
                        setPrettyText(it.data.response, etResponse)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        })

        viewModel.liveDataCreate.observe(this, Observer {
            when (it) {
                is Success<Long> -> {
                    toast("New entry added")
                    finish()
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
                    finish()
                }
                is Fail -> {
                    toast(it.ex.message)
                }
                is Loading -> {
                }
            }
        })

        viewModel.liveDataTransactionEntity.observe(this, Observer {
            when (it) {
                is Success<TransactionEntity> -> {
                    updateUi(it.data)
                }
                is Fail -> {
                    toast(it.ex.message)
                }
            }
        })

        viewModel.liveDataExport.observe(this, Observer {
            when (it) {
                is Success<String> -> sendData(it.data)
                is Fail -> toast(it.ex.message)
            }
        })

        viewModel.liveDataDeleteRecord.observe(this, Observer {
            when (it) {
                is Success<Boolean> -> finish()
                is Fail -> toast(it.ex.message)
            }
        })
    }

    fun updateUi(transactionEntity: TransactionEntity) {

        val parserRuleProvider = ParserRuleProvider()
        val jsonArray = JSONArray(transactionEntity.requestBody ?: "[]")
        if (jsonArray.length() > 0) {
            val item = jsonArray.getJSONObject(0)
            val query = item.optString("query") ?: ""
            val formattedOperationName = parserRuleProvider.parse(query)
            etGqlName.setText(formattedOperationName)
        }

        var response = transactionEntity.responseBody ?: ""
        response = gson.toJson(jsonParser.parse(response))
        etResponse.setText(response)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gql_add_response_menu_export, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gql_menu_save -> {
                saveData()
            }
            R.id.gql_menu_pretty -> {
                try {
                    setPrettyText(etResponse.text.toString(), etResponse)
                } catch (e: Exception) {
                    Toast.makeText(this, "Wrong Json", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.gql_menu_export -> {
                performExport()
            }
            R.id.gql_menu_delete -> {
                performDelete()
            }
        }
        return true
    }

    fun saveData() {
        val customName = etCustomName.text.toString()
        val gqlName = etGqlName.text.toString()
        val response = etResponse.text.toString()

        val addGqlData =
            AddGqlData(
                gqlQueryName = gqlName,
                response = response,
                customTag = customName,
                isResponseSuccess = isResponseSuccess,
                isDelayResponse = isDelayResponseEnable
            )

        if (isExistingRecord()) {
            viewModel.updateRecord(id!!, addGqlData)
        } else {
            viewModel.addToDb(addGqlData)
        }
    }

    fun isExistingRecord(): Boolean {
        return id != null && !isFromChuck
    }

    fun performExport() {
        id?.let {
            viewModel.export(it, ResponseItemType.GQL)
        }
    }

    fun performDelete() {
        id?.let {
            viewModel.delete(it)
        }
    }
}

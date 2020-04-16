package com.tokopedia.analyticsdebugger.validator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import timber.log.Timber

class AnalyticsValidatorActivity : AppCompatActivity() {

    val viewModel: ValidatorViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(this.application))
                .get(ValidatorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_validator)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia Client Analytics Validator"
        val jsonTest = Utils.getJsonDataFromAsset(this, "add_address_cvr.json")
        Timber.d("Validator Json Query \n %s", jsonTest)

        val jsonType = object : TypeToken<Map<String, Any>>() {}.type
        val testQuery = Gson().fromJson<Map<String, Any>>(jsonTest, jsonType)
        Timber.d("Validator Test Query Map \n %s", testQuery)

        viewModel.gtmLog.observe(this, Observer<List<GtmLogDB>> {
            Timber.d("Validator got ${it.size}")
        })
        val rv = findViewById<RecyclerView>(R.id.rv)
        val itemAdapter = ValidatorResultAdapter()
        itemAdapter.setData(testQuery["verifyOrder"] as List<Map<String, Any>>)
        with(rv) {
            layoutManager = LinearLayoutManager(this@AnalyticsValidatorActivity)
            adapter = itemAdapter
        }
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, AnalyticsValidatorActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
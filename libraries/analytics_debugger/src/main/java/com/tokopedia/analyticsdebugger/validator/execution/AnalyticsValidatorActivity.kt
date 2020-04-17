package com.tokopedia.analyticsdebugger.validator.execution

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.analyticsdebugger.validator.core.Validator
import com.tokopedia.analyticsdebugger.validator.core.toJson
import com.tokopedia.analyticsdebugger.validator.core.toJsonMap
import com.tokopedia.analyticsdebugger.validator.detail.ValidatorDetailActivity
import timber.log.Timber

class AnalyticsValidatorActivity : AppCompatActivity() {

    val viewModel: ValidatorViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(this.application))
                .get(ValidatorViewModel::class.java)
    }

    private val mAdapter: ValidatorResultAdapter by lazy {
        val itemAdapter = ValidatorResultAdapter { goToDetail(it) }
        itemAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_validator)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia Client Analytics Validator"
        val jsonTest = Utils.getJsonDataFromAsset(this, "add_address_cvr.json") ?: ""
        Timber.d("Validator Json Query \n %s", jsonTest)
        Timber.d("Asset %s", assets.list("")?.joinToString { "\n" })

        val testQuery = jsonTest.toJsonMap()
        Timber.d("Validator Test Query Map \n %s", testQuery)

        viewModel.testCases.observe(this, Observer<List<Validator>> {
            Timber.d("Validator got ${it.size}")
            mAdapter.setData(it)
        })
        val rv = findViewById<RecyclerView>(R.id.rv)
        viewModel.run(testQuery["verifyAll"] as List<Map<String, Any>>)
        with(rv) {
            layoutManager = LinearLayoutManager(this@AnalyticsValidatorActivity)
            addItemDecoration(DividerItemDecoration(this@AnalyticsValidatorActivity, DividerItemDecoration.VERTICAL))
            adapter = mAdapter
        }
    }

    private fun goToDetail(item: Validator) {
        val exp = item.data.toJson()
        val act = item.match?.data ?: ""

        val intent = ValidatorDetailActivity.newIntent(this, exp, act)
        startActivity(intent)
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, AnalyticsValidatorActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
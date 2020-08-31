package com.tokopedia.fakeresponse.presentation.activities

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.data.diProvider.activities.PasteTextActivityProvider
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.Loading
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.presentation.viewmodels.PasteTextViewModel
import com.tokopedia.fakeresponse.toast
import kotlinx.android.synthetic.main.fake_activity_paste_text.*

class PasteTextActivity : BaseActivity() {

    override fun getLayout() = R.layout.fake_activity_paste_text
    lateinit var viewModel: PasteTextViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        initVars()
        initUi()
        setObservers()
    }

    fun initUi() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Paste your text"
        toolbar.setNavigationIcon(R.drawable.fake_ic_back)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun initVars() {
        PasteTextActivityProvider().inject(this)
    }

    fun setObservers() {
        viewModel.importLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    finish()
                    toast("Import was successful")
                    button.text = "Success"
                }
                is Fail -> {
                    button.isEnabled = true
                    button.text = "Import"
                    toast(it.ex.message)
                }
                is Loading -> {
                    button.isEnabled = false
                    button.text = "Importing"
                }
            }
        })
        button.setOnClickListener {
            viewModel.performImport(etResponse.text.toString())
        }

        btnPretty.setOnClickListener {
            try {
                setPrettyText(etResponse.text.toString(), etResponse)
            } catch (e: Exception) {
                Toast.makeText(this, "Wrong Json", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
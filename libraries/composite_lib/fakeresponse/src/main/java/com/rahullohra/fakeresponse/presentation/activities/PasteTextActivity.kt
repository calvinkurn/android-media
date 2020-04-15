package com.rahullohra.fakeresponse.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.data.diProvider.activities.PasteTextActivityProvider
import com.rahullohra.fakeresponse.presentation.livedata.Fail
import com.rahullohra.fakeresponse.presentation.livedata.Loading
import com.rahullohra.fakeresponse.presentation.livedata.Success
import com.rahullohra.fakeresponse.presentation.viewmodels.PasteTextViewModel
import com.rahullohra.fakeresponse.toast
import kotlinx.android.synthetic.main.fake_activity_paste_text.*

class PasteTextActivity : AppCompatActivity() {

    fun getLayout() = R.layout.fake_activity_paste_text
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
    }
}
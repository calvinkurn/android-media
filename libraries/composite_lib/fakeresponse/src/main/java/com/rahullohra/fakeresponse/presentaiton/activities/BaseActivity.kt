package com.rahullohra.fakeresponse.presentaiton.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rahullohra.fakeresponse.App

open class BaseActivity : AppCompatActivity() {

    open fun getLayout() = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.setContext(application)
    }
}
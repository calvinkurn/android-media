package com.tokopedia.recharge_slice.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.AssistActionBuilder
import com.tokopedia.recharge_slice.R
import com.tokopedia.recharge_slice.di.DaggerRechargeSliceComponent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInject()
    }

    private fun initInject() {
        DaggerRechargeSliceComponent.builder().build().inject(this)
    }
}

package com.tokopedia.pms.paymentlist.presentation.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class CompletePayment : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window?.statusBarColor = ContextCompat.getColor(this,com.tokopedia.unifyprinciples.R.color.Unify_G500)
            } else {
                window?.statusBarColor = ContextCompat.getColor(this,com.tokopedia.unifyprinciples.R.color.Unify_G500)
            }
        }

        initView()

    }

    private fun initView() {

    }

}
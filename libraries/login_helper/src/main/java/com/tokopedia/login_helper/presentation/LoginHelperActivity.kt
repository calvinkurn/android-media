package com.tokopedia.login_helper.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tokopedia.unifycomponents.Toaster

class LoginHelperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.login_helper.R.layout.activity_login_helper)
        Toast.makeText(this,"TATAKAE", Toast.LENGTH_LONG).show()
    }
}

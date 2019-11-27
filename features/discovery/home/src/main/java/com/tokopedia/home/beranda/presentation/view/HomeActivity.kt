package com.tokopedia.home.beranda.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.home.R
import com.tokopedia.v2.home.ui.fragment.HomeFragment

class HomeActivity : AppCompatActivity() {

    companion object{
        fun newInstance(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment(), "home")
                .commit()
    }
}
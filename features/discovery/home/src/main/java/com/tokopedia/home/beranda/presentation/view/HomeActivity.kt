package com.tokopedia.home.beranda.presentation.view

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment

class HomeActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return HomeFragment.newInstance(false)
    }

    companion object{
        fun newInstance(context: Context) = Intent(context, HomeActivity::class.java)
    }



}
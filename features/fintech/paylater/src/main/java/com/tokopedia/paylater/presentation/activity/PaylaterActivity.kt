package com.tokopedia.paylater.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.fragments.PaylaterFragment

class PaylaterActivity : BaseSimpleActivity() {

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("")

    }

    override fun getLayoutRes() = R.layout.activity_paylater

    override fun getToolbarResourceID() = R.id.paylater_header

    override fun getParentViewResourceID(): Int = R.id.paylater_parent_view

    override fun getNewFragment(): Fragment? {
       return PaylaterFragment.newInstance()
    }

    companion object {
        const val SCREEN_NAME = "Paylater"
    }
}
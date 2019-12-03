package com.tokopedia.play.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.play.R
import com.tokopedia.play.view.fragment.PlayFragment

/**
 * Created by jegul on 29/11/19
 */
class PlayActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_play)

        initView()
        setupView()
    }

    private fun getFragment(): Fragment {
        return PlayFragment.newInstance()
    }

    private fun initView() {
    }

    private fun setupView() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, getFragment())
                .commit()
    }
}
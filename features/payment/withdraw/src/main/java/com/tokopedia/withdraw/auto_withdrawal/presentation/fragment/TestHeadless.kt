package com.tokopedia.withdraw.auto_withdrawal.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import javax.inject.Inject

class TestHeadless : BaseDaggerFragment() {


    override fun getScreenName(): String? = null

    override fun initInjector() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    companion object {
        private const val TAG = "TestHeadLess"

        fun addFragmentToActivity(fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                    .add(TestHeadless(),
                            TAG).commit();
        }

    }
}
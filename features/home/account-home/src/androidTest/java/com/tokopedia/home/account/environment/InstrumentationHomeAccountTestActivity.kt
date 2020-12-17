package com.tokopedia.home.account.environment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment
import com.tokopedia.home.account.test.R

class InstrumentationHomeAccountTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_account_test)

        val homeAccountFragment: Fragment = AccountHomeFragment.newInstance(Bundle())
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_home_account, homeAccountFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

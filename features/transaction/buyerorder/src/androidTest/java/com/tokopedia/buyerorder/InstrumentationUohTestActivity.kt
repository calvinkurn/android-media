package com.tokopedia.buyerorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.buyerorder.test.R
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment

class InstrumentationUohTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uoh_test)

        val uohListFragment: Fragment = UohListFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container_uoh, uohListFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

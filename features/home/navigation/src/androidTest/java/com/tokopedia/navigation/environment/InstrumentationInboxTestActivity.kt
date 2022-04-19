package com.tokopedia.navigation.environment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.navigation.presentation.fragment.InboxFragment
import com.tokopedia.navigation.test.R

class InstrumentationInboxTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_test)

        val homeAccountFragment: Fragment = InboxFragment.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_inbox, homeAccountFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
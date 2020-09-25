package com.tokopedia.tkpd.feed_component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.feedplus.test.R

import com.tokopedia.feedplus.view.fragment.FeedPlusFragment

class InstrumentationFeedPlusTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_test)
        val feedFragment: Fragment = FeedPlusFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container_feed, feedFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
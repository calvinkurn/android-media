package com.tokopedia.tkpd.feed_component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.feedplus.test.R as feedplustestR

import com.tokopedia.feedplus.oldFeed.view.fragment.FeedPlusFragment

class InstrumentationFeedPlusTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(feedplustestR.layout.activity_feed_container_test)
        val feedFragment: Fragment = FeedPlusFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(feedplustestR.id.container, feedFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

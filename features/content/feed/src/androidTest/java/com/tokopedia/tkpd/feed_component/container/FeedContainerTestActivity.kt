package com.tokopedia.tkpd.feed_component.container

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.feedplus.oldFeed.view.fragment.FeedPlusContainerFragment
import com.tokopedia.feedplus.test.R
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
class FeedContainerTestActivity : AppCompatActivity(), MainParentStatusBarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_container_test)

        val feedContainerFragment = FeedPlusContainerFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, feedContainerFragment)
            .commit()
    }

    override fun requestStatusBarDark() {

    }

    override fun requestStatusBarLight() {

    }
}

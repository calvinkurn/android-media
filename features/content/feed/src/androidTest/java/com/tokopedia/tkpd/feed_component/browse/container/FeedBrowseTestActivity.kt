package com.tokopedia.tkpd.feed_component.browse.container

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
class FeedBrowseTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FeedBrowseTestActivityAction.onStartActivity?.let {
            startActivity(it.invoke())
        }
    }
}

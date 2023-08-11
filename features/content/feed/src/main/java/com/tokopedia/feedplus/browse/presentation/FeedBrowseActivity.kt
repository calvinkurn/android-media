package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.di.DaggerFeedBrowseComponent
import com.tokopedia.feedplus.browse.di.FeedBrowseComponent

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseActivity : BaseActivity(), HasComponent<FeedBrowseComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_browse)
    }

    override fun getComponent(): FeedBrowseComponent {
        return DaggerFeedBrowseComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}

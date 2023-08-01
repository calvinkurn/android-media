package com.tokopedia.stories.view.activity

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.stories.di.DaggerStoriesComponent
import com.tokopedia.stories.di.StoriesModule

class StoriesActivity : StoriesBaseActivity() {

    override fun inject() {
        DaggerStoriesComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .storiesModule(StoriesModule(this))
            .build()
            .inject(this)
    }

    override fun getIntentData() {
        val data = intent.data ?: return
        viewModel.saveInitialData(data.pathSegments)
    }

}

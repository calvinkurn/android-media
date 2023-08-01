package com.tokopedia.stories

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.stories.databinding.ActivityStoriesBinding

class StoriesActivity : BaseActivity() {

    private var binding: ActivityStoriesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        getData()
    }

    private fun setupView() {
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    private fun getData() {
        val data = intent.data ?: return
        val pathSegment = data.pathSegments
        val shopId = pathSegment[1]
        val storiesId = pathSegment[2]
    }

}

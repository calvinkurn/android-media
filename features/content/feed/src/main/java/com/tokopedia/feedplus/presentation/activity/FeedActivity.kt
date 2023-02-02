package com.tokopedia.feedplus.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.feedplus.databinding.ActivityFeedBinding
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment

class FeedActivity : BaseActivity() {

    private var binding: ActivityFeedBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initView()
    }

    private fun initView() {
        binding?.let {
            supportFragmentManager.beginTransaction()
                .replace(it.containerFeed.id, FeedBaseFragment())
                .commit()
        }
    }

}

package com.tokopedia.feed.detail

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_FEED_RELEVANT_POST
import com.tokopedia.feedplus.databinding.ActivityFeedDetailBinding
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment.Companion.TAB_FIRST_INDEX
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment.Companion.TAB_TYPE_FOR_YOU
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.play_common.util.extension.commit

/**
 * Created by meyta.taliti on 16/05/23.
 */
class FeedDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityFeedDetailBinding
    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.data?.lastPathSegment ?: ""
        setupView()
    }

    private fun setupView() {
        val extrasData = Bundle().apply {
            putString(UF_EXTRA_FEED_RELEVANT_POST, postId)
            intent.extras?.let {
                putAll(it)
            }
        }
        supportFragmentManager.commit {
            replace(
                binding.root.id,
                FeedFragment.createFeedFragment(
                    FeedDataModel(
                        TAB_TYPE_FOR_YOU,
                        TAB_TYPE_FOR_YOU,
                        TAB_TYPE_FOR_YOU,
                        TAB_FIRST_INDEX,
                        true
                    ),
                    extrasData,
                    intent?.getStringExtra(ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT)
                        ?: ""
                )
            )
        }
    }
}

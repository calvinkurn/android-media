package com.tokopedia.feedplus.detail

import android.os.Bundle
import android.view.MenuItem
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        val extrasData = Bundle().apply {
            putString(UF_EXTRA_FEED_RELEVANT_POST, postId)
            intent.extras?.let {
                putAll(it)
            }
        }
        setSupportActionBar(binding.feedDetailHeader)
        supportFragmentManager.commit {
            replace(
                binding.feedDetailContainer.id,
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
                        ?: "",
                    true
                )
            )
        }
    }
}

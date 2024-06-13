package com.tokopedia.feedplus.detail

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.content.common.util.Router
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.FeedLocalSearchActivity
import com.tokopedia.feedplus.databinding.ActivityFeedDetailBinding
import com.tokopedia.feedplus.detail.analytic.FeedDetailAnalytic
import com.tokopedia.feedplus.detail.di.DaggerFeedDetailComponent
import com.tokopedia.feedplus.presentation.callback.FeedUiActionListener
import com.tokopedia.feedplus.presentation.callback.FeedUiListener
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment.Companion.TAB_FIRST_INDEX
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment.Companion.TAB_TYPE_CDP
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.fragment.FeedFragment.Companion.ENTRY_POINT_APPLINK
import com.tokopedia.feedplus.presentation.model.FeedContentUiModel
import com.tokopedia.feedplus.presentation.model.FeedDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.model.type.FeedContentType
import com.tokopedia.feedplus.presentation.model.type.isPlayContent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.util.extension.commit
import com.tokopedia.play_common.util.extension.marginLp
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 16/05/23.
 */
class FeedDetailActivity : BaseActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var router: Router

    @Inject lateinit var analytic: FeedDetailAnalytic

    private var _binding: ActivityFeedDetailBinding? = null
    private val binding: ActivityFeedDetailBinding
        get() = _binding!!

    private val postId: String
        get() = intent.data?.lastPathSegment.orEmpty()

    private val viewModel: FeedDetailViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setOnAttachFragmentListener()
        super.onCreate(savedInstanceState)
        _binding = ActivityFeedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupStatusBar()

        observeHeader()

        setupView()
    }

    private fun setupStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT

        binding.feedDetailBottomBar.doOnApplyWindowInsets { view, insets, padding, _ ->
            val systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                bottom = padding.bottom + systemBarInset.bottom
            )
        }

        binding.feedDetailHeader.doOnApplyWindowInsets { view, insets, _, margin ->
            val systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.marginLp.updateMargins(top = margin.top + systemBarInset.top)
            view.post {
                view.requestLayout()
            }
        }
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
        val source = intent.data?.getQueryParameter(KEY_QUERY_SOURCE) ?: TAB_TYPE_CDP
        val extrasData = Bundle().apply {
            putString(ApplinkConstInternalContent.UF_EXTRA_FEED_SOURCE_ID, postId)
            intent.extras?.let {
                putAll(it)
            }
        }
        setSupportActionBar(binding.feedDetailHeader.header)
        supportFragmentManager.commit {
            replace(
                binding.feedDetailContainer.id,
                FeedFragment.createFeedFragment(
                    FeedDataModel(
                        title = TAB_TYPE_CDP,
                        key = TAB_TYPE_CDP,
                        type = source,
                        position = TAB_FIRST_INDEX,
                        isActive = true,
                        isSelected = false,
                        hasNewContent = false
                    ),
                    extrasData,
                    intent?.getStringExtra(ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT)
                        ?: ENTRY_POINT_APPLINK,
                    true
                )
            )
        }

        binding.feedDetailHeader.onBackClicked { finish() }

        viewModel.getHeader(source)
    }

    private fun observeHeader() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.headerDetail.collectLatest {
                    if (it.isShowSearchBar) {
                        analytic.impressSearchBar()

                        binding.feedDetailHeader.post {
                            binding.feedDetailHeader.setFeedSearchBar {
                                analytic.clickSearchBar()

                                val intent = router.getIntent(this@FeedDetailActivity, it.applink)
                                startActivity(intent)
                            }
                        }
                    } else {
                        binding.feedDetailHeader.header.title = it.title
                    }
                }
            }
        }
    }

    private fun inject() {
        DaggerFeedDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setOnAttachFragmentListener() {
        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            if (fragment is FeedFragment) {
                fragment.setUiListener(object : FeedUiListener {

                    override fun onContentLoading() {
                        hideBottomActionView()
                    }

                    override fun onContentLoaded(
                        content: FeedContentUiModel,
                        trackerModel: FeedTrackerDataModel?,
                        uiActionListener: FeedUiActionListener,
                        contentPosition: Int
                    ) {
                        configureBottomActionView(
                            content,
                            trackerModel,
                            uiActionListener,
                            contentPosition
                        )
                    }

                    override fun onContentFailed() {
                        hideBottomActionView()
                    }
                })
            }
        }
    }

    private fun configureBottomActionView(
        content: FeedContentUiModel,
        trackerModel: FeedTrackerDataModel?,
        uiActionListener: FeedUiActionListener,
        contentPosition: Int
    ) {
        when (content.contentType) {
            FeedContentType.TopAds, FeedContentType.ProductHighlight -> {
                val shareUiModel = content.share
                if (shareUiModel != null && trackerModel != null) {
                    binding.feedDetailBottomBar.show()
                    binding.feedDetailBottomBar.text = getString(R.string.feed_bottom_action_share_label)
                    binding.feedDetailBottomBar.setOnClickListener {
                        uiActionListener.onSharePostClicked(shareUiModel, trackerModel)
                    }
                }
            }
            FeedContentType.PlayChannel,
            FeedContentType.PlayShortVideo,
            FeedContentType.Image,
            FeedContentType.Video -> {
                binding.feedDetailBottomBar.show()
                binding.feedDetailBottomBar.text = getString(R.string.feed_bottom_action_comment_label)
                binding.feedDetailBottomBar.setOnClickListener {
                    uiActionListener.onCommentClick(
                        trackerModel,
                        content.id,
                        content.contentType.isPlayContent(),
                        contentPosition
                    )
                }
            }
            else -> hideBottomActionView()
        }
    }

    private fun hideBottomActionView() {
        binding.feedDetailBottomBar.hide()
    }

    companion object {
        private const val KEY_QUERY_SOURCE = "source"
    }
}

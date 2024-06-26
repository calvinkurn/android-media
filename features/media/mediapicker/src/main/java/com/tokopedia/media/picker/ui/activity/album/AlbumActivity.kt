package com.tokopedia.media.picker.ui.activity.album

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ActivityAlbumBinding
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.data.mapper.toUiModel
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.ui.adapter.AlbumAdapter
import com.tokopedia.media.picker.ui.adapter.OnAlbumClickListener
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.component.NavToolbarComponent
import com.tokopedia.picker.common.component.ToolbarTheme
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AlbumActivity : BaseActivity(), NavToolbarComponent.Listener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var param: PickerCacheManager

    @Inject
    lateinit var pickerAnalytics: PickerAnalytics

    private val binding: ActivityAlbumBinding? by viewBinding()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[AlbumViewModel::class.java]
    }

    private val adapter by lazy {
        AlbumAdapter(listener = onAlbumClickListener)
    }

    private val navToolbar by uiComponent {
        NavToolbarComponent(
            listener = this,
            parent = it
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        initInjector()
        initObservable()
        initView()

        setupRecyclerView()
    }

    override fun onCloseClicked() {
        onBackPressed()
    }

    private fun initObservable() {
        lifecycleScope.launchWhenStarted {
            viewModel.getAlbums().collect {
                if (it.isNotEmpty()) {
                    adapter.setData(it.toUiModel())
                }
            }
        }

        viewModel.isLoading.observe(this) {
            binding?.shimmering?.container?.showWithCondition(it)
        }
    }

    private fun initView() {
        // set toolbar as solid theme and toolbar title
        navToolbar.setTitle(getString(R.string.picker_toolbar_album_title))
        navToolbar.onToolbarThemeChanged(ToolbarTheme.Solid)
    }

    private fun setupRecyclerView() {
        binding?.lstAlbum?.layoutManager = LinearLayoutManager(applicationContext)
        binding?.lstAlbum?.adapter = adapter
    }

    private val onAlbumClickListener = object : OnAlbumClickListener {
        override fun invoke(album: AlbumUiModel) {
            pickerAnalytics.clickAlbumFolder(album.name)

            setResult(RESULT_OK, Intent().apply {
                putExtra(INTENT_BUCKET_ID, album.id)
                putExtra(INTENT_BUCKET_NAME, album.name)
                putExtra(INTENT_BUCKET_COUNT, album.count)
            })

            finish()
        }
    }

    private fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onContinueClicked() {} // no-op

    companion object {
        const val INTENT_BUCKET_ID = "bucket_id"
        const val INTENT_BUCKET_NAME = "bucket_name"
        const val INTENT_BUCKET_COUNT = "bucket_count"

        fun start(fragment: Fragment, reqCode: Int) {
            fragment.startActivityForResult(
                Intent(
                    fragment.requireContext(),
                    AlbumActivity::class.java
                ), reqCode
            )
        }

        fun getIntentResult(intent: Intent?): Triple<Long, String, Int> {
            val bucketId = intent?.getLongExtra(INTENT_BUCKET_ID, 0) ?: -1
            val bucketName = intent?.getStringExtra(INTENT_BUCKET_NAME) ?: ""
            val bucketCount = intent?.getIntExtra(INTENT_BUCKET_COUNT, 0) ?: 0

            return Triple(bucketId, bucketName, bucketCount)
        }
    }

}

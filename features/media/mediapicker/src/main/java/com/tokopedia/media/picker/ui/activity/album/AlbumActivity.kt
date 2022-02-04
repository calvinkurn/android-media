package com.tokopedia.media.picker.ui.activity.album

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.media.common.component.uiComponent
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ActivityAlbumBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.activity.album.adapter.AlbumAdapter
import com.tokopedia.media.picker.ui.activity.component.NavToolbarComponent
import com.tokopedia.media.picker.ui.fragment.OnAlbumClickListener
import com.tokopedia.media.picker.ui.uimodel.AlbumUiModel
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class AlbumActivity : BaseActivity(), NavToolbarComponent.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: ActivityAlbumBinding? by viewBinding()
    private val param by lazy { PickerUiConfig.pickerParam() }

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
        viewModel.albums.observe(this) {
            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        }

        viewModel.isLoading.observe(this) {
            binding?.shimmering?.container?.showWithCondition(it)
        }
    }

    private fun initView() {
        // set toolbar as solid theme and toolbar title
        navToolbar.setTitle(getString(R.string.picker_toolbar_album_title))
        navToolbar.setNavToolbarColorState(false)

        // fetch the album list
        viewModel.fetch(param)
    }

    private fun setupRecyclerView() {
        binding?.lstAlbum?.layoutManager = LinearLayoutManager(applicationContext)
        binding?.lstAlbum?.adapter = adapter
    }

    private val onAlbumClickListener = object : OnAlbumClickListener {
        override fun invoke(album: AlbumUiModel) {
            setResult(RESULT_OK, Intent().apply {
                putExtra(INTENT_BUCKET_ID, album.id)
                putExtra(INTENT_BUCKET_NAME, album.name)
            })

            finish()
        }
    }

    private fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .pickerModule(PickerModule())
            .build()
            .inject(this)
    }

    override fun onContinueClicked() {} // no-op

    companion object {
        const val INTENT_BUCKET_ID = "bucket_id"
        const val INTENT_BUCKET_NAME = "bucket_name"
    }

}
package com.tokopedia.picker.ui.activity.album

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.databinding.ActivityAlbumBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.album.adapter.AlbumAdapter
import com.tokopedia.picker.ui.fragment.OnAlbumClickListener
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class AlbumActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: ActivityAlbumBinding? by viewBinding()
    private val param = PickerUiConfig.pickerParam()

    private val adapter by lazy {
        AlbumAdapter(listener = onAlbumClickListener)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[AlbumViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        initInjector()
        initObservable()
        initView()

        setupRecyclerView()
    }

    private fun initObservable() {
        viewModel.albums.observe(this) {
            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        }
    }

    private fun initView() {
        viewModel.fetch(param)
    }

    private fun setupRecyclerView() {
        binding?.lstAlbum?.layoutManager = LinearLayoutManager(applicationContext)
        binding?.lstAlbum?.adapter = adapter
    }

    private val onAlbumClickListener = object : OnAlbumClickListener {
        override fun invoke(album: Album) {
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

    companion object {
        const val INTENT_BUCKET_ID = "bucket_id"
        const val INTENT_BUCKET_NAME = "bucket_name"
    }

}
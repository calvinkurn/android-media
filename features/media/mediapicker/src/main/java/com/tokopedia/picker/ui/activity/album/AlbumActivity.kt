package com.tokopedia.picker.ui.activity.album

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Directory
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.databinding.ActivityAlbumBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.album.adapter.FileDirectoryAdapter
import com.tokopedia.picker.ui.fragment.OnDirectoryClickListener
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class AlbumActivity : BaseActivity() {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: ActivityAlbumBinding? by viewBinding()

    private val config by lazy {
        PickerUiConfig.getFileLoaderParam()
    }

    private val adapter by lazy {
        FileDirectoryAdapter(listener = onDirectoryClickListener)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        ).get(AlbumViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        initInjector()
        initObservable()
        initView()
    }

    private fun initObservable() {
        viewModel.result.observe(this, {
            val files = it.first
            val directories = it.second.toMutableList()

            // add recent medias
            directories.add(0, Directory("Recent").also { dir ->
                dir.medias.addAll(files)
            })

            // TODO, empty state
            if (files.isNotEmpty()) {
                adapter.setData(directories)
            }
        })
    }

    private fun initView() {
        setupRecyclerView()
        viewModel.fetch(config)
    }

    private fun setupRecyclerView() {
        binding?.lstAlbum?.layoutManager = LinearLayoutManager(applicationContext)
        binding?.lstAlbum?.adapter = adapter
    }

    private val onDirectoryClickListener = object : OnDirectoryClickListener {
        override fun invoke(directory: Directory) {
            setResult(RESULT_OK, createResultIntent(directory.medias))
            finish()
        }
    }

    fun createResultIntent(medias: List<Media>?): Intent {
        val data = Intent()
        val mediaList = ArrayList(medias ?: emptyList())
        data.putParcelableArrayListExtra(RC_SELECTED_DIRECTORY, mediaList)
        return data
    }

    private fun initInjector() {
        DaggerPickerComponent.builder()
            .pickerModule(PickerModule(applicationContext))
            .build()
            .inject(this)
    }

    companion object {
        const val RC_SELECTED_DIRECTORY = "dir_selection"
    }

}
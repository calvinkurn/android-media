package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.ThumbnailDrawerAdapter
import com.tokopedia.media.editor.ui.adapter.decoration.ThumbnailDrawerDecoration
import com.tokopedia.picker.common.basecomponent.UiComponent

class ThumbnailDrawerUiComponent constructor(
    viewGroup: ViewGroup
) : UiComponent(viewGroup, R.id.uc_drawer_thumbnail) {

    private val lstThumbnail: RecyclerView = findViewById(R.id.lst_thumbnail)

    private lateinit var drawerAdapter: ThumbnailDrawerAdapter

    fun setupView(images: List<String>) {
        setupRecyclerView(images)
    }

    private fun setupRecyclerView(images: List<String>) {
        if (!::drawerAdapter.isInitialized) {
            drawerAdapter = ThumbnailDrawerAdapter(images)
        }

        with(lstThumbnail) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            addItemDecoration(ThumbnailDrawerDecoration(context))
            adapter = drawerAdapter
        }
    }

}
package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.ThumbnailDrawerAdapter
import com.tokopedia.media.editor.ui.adapter.decoration.ThumbnailDrawerDecoration
import com.tokopedia.picker.common.basecomponent.UiComponent

class DrawerUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_drawer_thumbnail), ThumbnailDrawerAdapter.Listener {

    private val lstThumbnail: RecyclerView = findViewById(R.id.lst_thumbnail)

    private lateinit var drawerAdapter: ThumbnailDrawerAdapter

    fun setupView(images: List<String>) {
        setupRecyclerView(images)
    }

    override fun onItemClicked(url: String) {
        listener.onThumbnailDrawerClicked(url)
    }

    private fun setupRecyclerView(images: List<String>) {
        if (!::drawerAdapter.isInitialized) {
            drawerAdapter = ThumbnailDrawerAdapter(images, this)
        }

        with(lstThumbnail) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            addItemDecoration(ThumbnailDrawerDecoration(context))
            adapter = drawerAdapter
            itemAnimator = null
        }
    }

    interface Listener {
        fun onThumbnailDrawerClicked(url: String)
    }

}
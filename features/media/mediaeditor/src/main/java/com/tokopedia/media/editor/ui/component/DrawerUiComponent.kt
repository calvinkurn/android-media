package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.adapter.ThumbnailDrawerAdapter
import com.tokopedia.media.editor.ui.adapter.decoration.ThumbnailDrawerDecoration
import com.tokopedia.media.editor.ui.uimodel.EditorUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent

class DrawerUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_drawer_thumbnail), ThumbnailDrawerAdapter.Listener {

    private val lstThumbnail: RecyclerView = findViewById(R.id.lst_thumbnail)

    private val drawerAdapter: ThumbnailDrawerAdapter by lazy {
        ThumbnailDrawerAdapter(listener = this)
    }

    fun getCurrentIndex(): Int {
        return drawerAdapter.getCurrentIndex()
    }

    fun refreshItem(updateIndex: Int, newData: List<EditorUiModel>) {
        drawerAdapter.updateData(updateIndex, newData)
    }

    fun clickIndex(index: Int) {
        lstThumbnail.findViewHolderForAdapterPosition(index)?.itemView?.performClick()
    }

    override fun onItemClicked(originalUrl: String, resultUrl: String?, clickedIndex: Int) {
        listener.onThumbnailDrawerClicked(originalUrl, resultUrl, clickedIndex)
    }

    fun setupView(newData: List<EditorUiModel>) {
        drawerAdapter.setData(newData)

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
        fun onThumbnailDrawerClicked(originalUrl: String, resultUrl: String?, clickedIndex: Int)
    }

}
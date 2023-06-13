package com.tokopedia.media.picker.ui.component

import android.view.ViewGroup
import com.tokopedia.media.R
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifyprinciples.Typography

class AlbumSelectorUiComponent(
    private val listener: Listener,
    parent: ViewGroup
) : UiComponent(parent, R.id.album_selector) {

    private val txtAlbumName = findViewById<Typography>(R.id.txt_name)

    fun setupView() {
        container().setOnClickListener {
            listener.onAlbumSelectorClicked()
        }
    }

    fun setAlbumName(name: String) {
        txtAlbumName.text = name
    }

    interface Listener {
        fun onAlbumSelectorClicked()
    }
}

package com.tokopedia.editor.ui.main.component

import android.view.View
import android.view.ViewGroup
import com.tokopedia.editor.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.LoaderUnify

class GlobalLoaderUiComponent constructor(
    parent: ViewGroup
) : UiComponent(parent, R.id.uc_global_loader) {

    private val overlay: View = findViewById(R.id.overlay_loader)
    private val loader: LoaderUnify = findViewById(R.id.global_loader)

    fun showLoading() {
        loader.show()
        overlay.show()
    }

    fun hideLoading() {
        loader.hide()
        overlay.hide()
    }
}

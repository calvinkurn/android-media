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

    init {
        /**
         * Fix bubble elevation issue.
         *
         * Since this global loader uses to to preventing user interception,
         * hence we have to disable any kind of action behind the loader.
         *
         * We have an issue of bubble elevation if we haven't init this listener.
         */
        container().setOnClickListener {}
    }

    fun showLoading() {
        loader.show()
        overlay.show()
    }

    fun hideLoading() {
        loader.hide()
        overlay.hide()
    }
}

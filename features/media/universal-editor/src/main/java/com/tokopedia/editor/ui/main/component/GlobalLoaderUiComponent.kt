package com.tokopedia.editor.ui.main.component

import android.view.ViewGroup
import com.tokopedia.editor.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.common.basecomponent.UiComponent

class GlobalLoaderUiComponent constructor(
    parent: ViewGroup
) : UiComponent(parent, R.id.uc_global_loader) {

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
        container().show()
    }

    fun hideLoading() {
        container().hide()
    }
}

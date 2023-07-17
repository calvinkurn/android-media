package com.tokopedia.editor.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseEditorFragment(layoutId: Int) : Fragment(layoutId) {

    abstract fun initObserver()
    abstract fun onLoadSavedState(bundle: Bundle?)
    abstract fun initView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLoadSavedState(savedInstanceState)
        initObserver()
        initView()
    }

}

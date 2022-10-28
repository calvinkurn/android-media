package com.tokopedia.media.editor.base

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

abstract class BaseEditorFragment : TkpdBaseV4Fragment() {

    abstract fun initObserver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

}
package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

abstract class BaseBottomSheet<T : ViewBinding> : BottomSheetUnify() {

    protected var binding by autoClearedNullable<T>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ShcDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    abstract fun setupView(): Unit?
}

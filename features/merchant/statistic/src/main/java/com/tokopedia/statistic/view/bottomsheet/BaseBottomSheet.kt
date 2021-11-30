package com.tokopedia.statistic.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.binding.viewBinding
import java.lang.reflect.Method

/**
 * Created By @ilhamsuaib on 14/02/21
 */

abstract class BaseBottomSheet<T: ViewBinding> : BottomSheetUnify() {

    protected var binding by autoClearedNullable<T>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    abstract fun setupView(): Unit?
}
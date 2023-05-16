package com.tokopedia.sellerpersona.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

abstract class BaseBottomSheet<T: ViewBinding> : BottomSheetUnify() {

    protected var binding: T? = null

    abstract fun bind(view: View): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetNotOverlapStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(getChildResLayout(), container, false)
        binding = bind(view)
        setChild(view)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    protected abstract fun getChildResLayout(): Int

    protected abstract fun setupView(): Unit?
}
package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetKebabMenuUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetKebabMenuAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by fwidjaja on 01/10/21.
 */
class UohKebabMenuBottomSheet : BottomSheetUnify() {
    private var bottomSheetKebabMenu : BottomSheetUnify? = null
    private var binding by autoClearedNullable<BottomsheetKebabMenuUohBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = BottomsheetKebabMenuUohBinding.inflate(LayoutInflater.from(context), null, false)
    }

    fun show(context: Context, fragmentManager: FragmentManager, adapter: UohBottomSheetKebabMenuAdapter) {
        bottomSheetKebabMenu = BottomSheetUnify()
        binding?.run {
            // make sure this one after another
            rvKebab.adapter = adapter
            rvKebab.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        bottomSheetKebabMenu?.run {
            showCloseIcon = true
            showHeader = true
            setChild(binding?.root)
            setTitle(UohConsts.OTHERS)
            setCloseClickListener { dismiss() }
        }
        bottomSheetKebabMenu?.show(fragmentManager, "")
    }

    fun doDismiss() {
        if (bottomSheetKebabMenu?.isVisible == true) bottomSheetKebabMenu?.dismiss()
    }
}
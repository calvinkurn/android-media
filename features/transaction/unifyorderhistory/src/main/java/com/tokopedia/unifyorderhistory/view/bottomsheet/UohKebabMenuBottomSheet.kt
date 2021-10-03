package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetKebabMenuUohBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetKebabMenuAdapter

/**
 * Created by fwidjaja on 01/10/21.
 */
class UohKebabMenuBottomSheet {
    fun show(context: Context,
             fragmentManager: FragmentManager,
             adapter: UohBottomSheetKebabMenuAdapter
    ){
        val bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val binding = BottomsheetKebabMenuUohBinding.inflate(LayoutInflater.from(context))
        binding.run {
            // make sure this one after another
            rvKebab.adapter = adapter
            rvKebab.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        bottomSheet.setChild(binding.root)
        bottomSheet.setTitle(UohConsts.OTHERS)
        bottomSheet.show(fragmentManager, "")
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
    }
}
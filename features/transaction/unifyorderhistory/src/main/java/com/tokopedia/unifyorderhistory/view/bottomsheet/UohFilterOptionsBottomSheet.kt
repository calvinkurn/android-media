package com.tokopedia.unifyorderhistory.view.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyorderhistory.databinding.BottomsheetOptionUohBinding
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 02/10/21.
 */
class UohFilterOptionsBottomSheet {
    private var actionListener: ActionListener? = null
    var bottomSheet = BottomSheetUnify()

    fun show(context: Context,
             fragmentManager: FragmentManager,
             adapter: UohBottomSheetOptionAdapter,
             title: String
    ){
        bottomSheet = BottomSheetUnify()
        bottomSheet.showCloseIcon = true
        bottomSheet.showHeader = true

        val binding = BottomsheetOptionUohBinding.inflate(LayoutInflater.from(context))
        binding.run {
            rvOption.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvOption.adapter = adapter
            btnApply.setOnClickListener {  }
        }
        bottomSheet.setChild(binding.root)
        bottomSheet.setTitle(title)
        bottomSheet.show(fragmentManager, "")
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
    }

    fun dismiss() {
        if (bottomSheet.show)
    }

    interface ActionListener {
        fun onClickApply()
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}
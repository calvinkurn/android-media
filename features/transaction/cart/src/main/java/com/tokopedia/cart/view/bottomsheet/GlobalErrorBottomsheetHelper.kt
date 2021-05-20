package com.tokopedia.cart.view.bottomsheet

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.cart.databinding.LayoutBottomsheetGlobalErrorBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

fun showGlobalErrorBottomsheet(fragmentManager: FragmentManager, context: Context, actionCallback: () -> Unit) {

    val bottomSheet = BottomSheetUnify().apply {
        showCloseIcon = true
        showHeader = true
        isDragable = true
        isHideable = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
    }

    val binding = LayoutBottomsheetGlobalErrorBinding.inflate(LayoutInflater.from(context))
    binding.layoutGlobalError.setActionClickListener {
        actionCallback()
        bottomSheet.dismiss()
    }

    bottomSheet.setChild(binding.root)
    bottomSheet.show(fragmentManager, "Cart Global Error")

}
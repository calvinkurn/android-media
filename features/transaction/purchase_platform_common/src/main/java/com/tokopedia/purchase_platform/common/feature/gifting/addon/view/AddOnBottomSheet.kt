package com.tokopedia.purchase_platform.common.feature.gifting.addon.view

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.purchase_platform.common.databinding.LayoutAddOnBottomSheetBinding
import com.tokopedia.purchase_platform.common.feature.gifting.addon.view.di.DaggerAddOnComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class AddOnBottomSheet {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewBinding: LayoutAddOnBottomSheetBinding? = null
    private var viewModel: AddOnViewModel? = null
    private var bottomSheet: BottomSheetUnify? = null

    fun show(fragment: Fragment) {
        fragment.context?.let {
            initializeInjector(fragment)
            initializeViewModel(fragment)
            initializeView(it, fragment.parentFragmentManager)
        }
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel = ViewModelProvider(fragment, viewModelFactory).get(AddOnViewModel::class.java)
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }

    private fun initializeInjector(fragment: Fragment) {
        val application = fragment.activity?.application
        if (application is BaseMainApplication) {
            DaggerAddOnComponent.builder()
                    .baseAppComponent(application.baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    private fun initializeBottomSheet(viewBinding: LayoutAddOnBottomSheetBinding, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = true
            showHeader = true
            isDragable = true
            isHideable = true
            clearContentPadding = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            setOnDismissListener {
                this@AddOnBottomSheet.viewBinding = null
            }
            setChild(viewBinding.root)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeView(context: Context, fragmentManager: FragmentManager) {
        val viewBinding = LayoutAddOnBottomSheetBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding

        initializeBottomSheet(viewBinding, fragmentManager)
    }

}
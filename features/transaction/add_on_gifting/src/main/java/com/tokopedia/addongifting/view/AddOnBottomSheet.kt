package com.tokopedia.addongifting.view

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.addongifting.databinding.LayoutAddOnBottomSheetBinding
import com.tokopedia.addongifting.view.di.AddOnComponent
import com.tokopedia.addongifting.view.di.DaggerAddOnComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class AddOnBottomSheet : BottomSheetUnify(), HasComponent<AddOnComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddOnViewModel::class.java)
    }

    private var viewBinding: LayoutAddOnBottomSheetBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

//    fun show(fragmentManager: FragmentManager) {
//        initializeInjector(fragment)
//        initializeViewModel(fragment)
//        initializeView(it, fragment.parentFragmentManager)
//    }

//    private fun initializeViewModel(fragment: Fragment) {
//        viewModel = ViewModelProvider(fragment, viewModelFactory).get(AddOnViewModel::class.java)
//    }

//    fun dismiss() {
//        bottomSheet?.dismiss()
//    }

//    private fun initializeInjector(fragment: Fragment) {
//        val application = fragment.activity?.application
//        if (application is BaseMainApplication) {
//            DaggerAddOnComponent.builder()
//                    .baseAppComponent(application.baseAppComponent)
//                    .build()
//                    .inject(this)
//        }
//    }

    private fun initializeBottomSheet(viewBinding: LayoutAddOnBottomSheetBinding, fragmentManager: FragmentManager) {
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
    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.finish()
        super.onDismiss(dialog)
    }


//    private fun initializeView(context: Context, fragmentManager: FragmentManager) {
//        val viewBinding = LayoutAddOnBottomSheetBinding.inflate(LayoutInflater.from(context))
//        this.viewBinding = viewBinding
//
//        initializeBottomSheet(viewBinding, fragmentManager)
//    }

    override fun getComponent(): AddOnComponent {
        return DaggerAddOnComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

}
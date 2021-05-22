package com.tokopedia.minicart.cartlist

import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.di.DaggerMiniCartListComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MiniCartListViewModel

    fun show(shopIds: List<String>, fragment: Fragment, onDismiss: () -> Unit) {
        initializeInjector(fragment)
        initializeViewModel(fragment)
        initializeView(fragment, onDismiss)
        initializeCartData(shopIds)
    }

    private fun initializeCartData(shopIds: List<String>) {
        viewModel.getCartList(shopIds)
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartListViewModel::class.java)
        viewModel.cartListUiModel.observe(fragment, {
            Toast.makeText(fragment.activity, "SHOW CART LIST!", Toast.LENGTH_SHORT).show()
        })
    }

    private fun initializeView(fragment: Fragment, onDismiss: () -> Unit) {
        fragment.activity?.let { fragmentActivity ->
            val bottomSheet = BottomSheetUnify().apply {
                showCloseIcon = false
                setTitle("Title")
                showHeader = true
                isDragable = true
                showKnob = true
                isHideable = true
                clearContentPadding = true
                customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            }

            bottomSheet.setOnDismissListener {
                onDismiss.invoke()
            }

            val view = View.inflate(fragmentActivity, R.layout.layout_bottomsheet_mini_cart_list, null)

            bottomSheet.setChild(view)
            bottomSheet.show(fragment.parentFragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeInjector(fragment: Fragment) {
        fragment.activity?.let {
            val baseAppComponent = it.applicationContext
            if (baseAppComponent is BaseMainApplication) {
                DaggerMiniCartListComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

}
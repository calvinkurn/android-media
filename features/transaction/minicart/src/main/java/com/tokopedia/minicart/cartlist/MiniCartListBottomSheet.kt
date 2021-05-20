package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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

    fun show(fragmentManager: FragmentManager, activity: FragmentActivity) {
        initializeInjector(activity)
        initializeViewModel(activity)
        initializeView(activity, fragmentManager)
    }

    fun show(fragmentManager: FragmentManager, fragment: Fragment) {
        fragment.activity?.let {
            initializeInjector(it)
            initializeViewModel(it)
            initializeView(it, fragmentManager)
            initializeCartData()
        }
    }

    private fun initializeCartData() {
        viewModel.getCartList()
    }

    private fun initializeViewModel(activity: FragmentActivity) {
        viewModel = ViewModelProvider(activity, viewModelFactory).get(MiniCartListViewModel::class.java)
        viewModel.cartListUiModel.observe(activity, {
            Toast.makeText(activity, "SHOW CART LIST!", Toast.LENGTH_SHORT).show()
        })
    }

    private fun initializeView(activity: FragmentActivity, fragmentManager: FragmentManager) {
        val bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            setTitle("Title")
            showHeader = true
            isDragable = true
            showKnob = true
            isHideable = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        }

        bottomSheet.setOnDismissListener {

        }

        val view = View.inflate(activity, R.layout.layout_bottomsheet_mini_cart_list, null)

        bottomSheet.setChild(view)
        bottomSheet.show(fragmentManager, this.javaClass.simpleName)
    }

    private fun initializeInjector(context: Context) {
        val baseAppComponent = context.applicationContext
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartListComponent.builder()
                    .baseAppComponent(baseAppComponent.baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

}
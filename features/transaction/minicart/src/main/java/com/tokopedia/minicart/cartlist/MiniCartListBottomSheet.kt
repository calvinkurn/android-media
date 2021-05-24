package com.tokopedia.minicart.cartlist

import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.di.DaggerMiniCartListComponent
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor() : MiniCartWidgetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MiniCartListViewModel
    private var bottomSheet: BottomSheetUnify? = null
    private var miniCartWidget: MiniCartWidget? = null

    fun show(shopIds: List<String>, fragment: Fragment, onDismiss: () -> Unit) {
        initializeInjector(fragment)
        initializeView(shopIds, fragment, onDismiss)
        initializeViewModel(fragment)
        initializeCartData(shopIds)
    }

    private fun initializeCartData(shopIds: List<String>) {
        viewModel.getCartList(shopIds)
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartListViewModel::class.java)
        viewModel.miniCartUiModel.observe(fragment, {
            Toast.makeText(fragment.activity, "SHOW CART LIST!", Toast.LENGTH_SHORT).show()
            bottomSheet?.setTitle(it.title)
            miniCartWidget?.updateData(it.miniCartWidgetData)
        })
    }

    private fun initializeView(shopIds: List<String>, fragment: Fragment, onDismiss: () -> Unit) {
        fragment.activity?.let { fragmentActivity ->
            bottomSheet = BottomSheetUnify().apply {
                showCloseIcon = false
                showHeader = true
                isDragable = true
                showKnob = true
                isHideable = true
                clearContentPadding = true
                customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
                setOnDismissListener {
                    onDismiss.invoke()
                }
            }

            val view = View.inflate(fragmentActivity, R.layout.layout_bottomsheet_mini_cart_list, null)
            bottomSheet?.setChild(view)
            bottomSheet?.show(fragment.parentFragmentManager, this.javaClass.simpleName)

            miniCartWidget = view.findViewById(R.id.mini_cart_widget)
            miniCartWidget?.initialize(shopIds, fragment, this, false)
            miniCartWidget?.totalAmount?.amountChevronView?.setOnClickListener {

            }
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

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        // no-op
    }

}
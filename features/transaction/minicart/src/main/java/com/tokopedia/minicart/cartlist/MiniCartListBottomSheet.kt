package com.tokopedia.minicart.cartlist

import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapter
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.cartlist.di.DaggerMiniCartListComponent
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class MiniCartListBottomSheet :
        MiniCartWidgetListener,
        MiniCartListActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MiniCartListViewModel
    private var bottomSheet: BottomSheetUnify? = null
    private var miniCartWidget: MiniCartWidget? = null
    private var rvMiniCartList: RecyclerView? = null
    private var adapter: MiniCartListAdapter? = null

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
        viewModel.miniCartUiModel.observe(fragment.viewLifecycleOwner, {
            Toast.makeText(fragment.activity, "SHOW CART LIST!", Toast.LENGTH_SHORT).show()
            bottomSheet?.setTitle(it.title)
            miniCartWidget?.updateData(it.miniCartWidgetData)
            adapter?.clearAllElements()
            adapter?.addVisitableList(it.visitables)
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
            miniCartWidget?.setTotalAmountChevronListener {
                // Todo : open summary bottomsheet
            }

            intializeRecyclerView(view, fragment)
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

    private fun intializeRecyclerView(view: View, fragment: Fragment) {
        rvMiniCartList = view.findViewById(R.id.rv_mini_cart_list)
        val adapterTypeFactory = MiniCartListAdapterTypeFactory(this)
        adapter = MiniCartListAdapter(adapterTypeFactory)
        rvMiniCartList?.adapter = adapter
        rvMiniCartList?.layoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        // no-op
    }

    override fun onDeleteClicked() {

    }

    override fun onQuantityChanged() {

    }

    override fun onNotesChanged() {

    }

}
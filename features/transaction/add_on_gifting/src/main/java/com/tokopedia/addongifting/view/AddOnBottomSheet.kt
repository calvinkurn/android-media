package com.tokopedia.addongifting.view

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.LayoutAddOnBottomSheetBinding
import com.tokopedia.addongifting.view.adapter.AddOnListAdapter
import com.tokopedia.addongifting.view.adapter.AddOnListAdapterTypeFactory
import com.tokopedia.addongifting.view.di.AddOnComponent
import com.tokopedia.addongifting.view.di.DaggerAddOnComponent
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class AddOnBottomSheet(val addOnProductData: AddOnProductData) : BottomSheetUnify(), AddOnActionListener, HasComponent<AddOnComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var adapter: AddOnListAdapter? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddOnViewModel::class.java)
    }

    private var viewBinding: LayoutAddOnBottomSheetBinding? = null

    override fun getComponent(): AddOnComponent {
        return DaggerAddOnComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeView()
        initializeObserver()
        initializeData(addOnProductData)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initializeView() {
        val viewBinding = LayoutAddOnBottomSheetBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding

        initializeBottomSheet(viewBinding)
        initializeRecyclerView(viewBinding)
    }

    private fun initializeBottomSheet(viewBinding: LayoutAddOnBottomSheetBinding) {
        setTitle("Atur pelengkap barang")
        showCloseIcon = true
        showHeader = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
//        isFullpage = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun initializeRecyclerView(viewBinding: LayoutAddOnBottomSheetBinding) {
        val adapterTypeFactory = AddOnListAdapterTypeFactory(this)
        adapter = AddOnListAdapter(adapterTypeFactory)
        viewBinding.rvAddOn.adapter = adapter
        viewBinding.rvAddOn.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initializeData(addOnProductData: AddOnProductData) {
        val mockAddOnResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_add_on_response)
        val mockAddOnSavedStateResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_add_on_saved_state_response)
        viewModel.loadAddOnData(addOnProductData, mockAddOnResponse, mockAddOnSavedStateResponse)
    }

    private fun initializeObserver() {
        observeGlobalEvent()
        observeProductData()
        observeAddOnData()
    }

    private fun observeGlobalEvent() {
        viewModel.globalEvent.observe(this, {
            when (it.state) {
                GlobalEvent.STATE_FAILED_LOAD_ADD_ON_DATA -> {
                    // Todo : show global error
                }
            }
        })
    }

    private fun observeProductData() {
        viewModel.productData.observe(this, {
            addOrModify(it)
        })
    }

    private fun observeAddOnData() {
        viewModel.addOnData.observe(this, {
            addOrModify(it)
        })
    }

    private fun addOrModify(it: Visitable<*>) {
        if (adapter?.data?.contains(it) == true) {
            adapter?.modifyData(adapter?.data?.indexOf(it) ?: RecyclerView.NO_POSITION)
        } else {
            adapter?.addVisitable(it)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        activity?.finish()
        super.onDismiss(dialog)
    }

}
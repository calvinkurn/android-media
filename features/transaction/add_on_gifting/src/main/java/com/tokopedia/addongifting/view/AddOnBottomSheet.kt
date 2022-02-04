package com.tokopedia.addongifting.view

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.addongifting.databinding.LayoutAddOnBottomSheetBinding
import com.tokopedia.addongifting.view.adapter.AddOnListAdapter
import com.tokopedia.addongifting.view.adapter.AddOnListAdapterTypeFactory
import com.tokopedia.addongifting.view.di.AddOnComponent
import com.tokopedia.addongifting.view.di.DaggerAddOnComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class AddOnBottomSheet : BottomSheetUnify(), AddOnActionListener, HasComponent<AddOnComponent> {

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
        val view = super.onCreateView(inflater, container, savedInstanceState)
        initializeView()
        initializeData()
        return view
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
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setOnDismissListener {
            this@AddOnBottomSheet.viewBinding = null
        }
        setChild(viewBinding.root)
    }

    private fun initializeRecyclerView(viewBinding: LayoutAddOnBottomSheetBinding) {
        val adapterTypeFactory = AddOnListAdapterTypeFactory(this)
        adapter = AddOnListAdapter(adapterTypeFactory)
        viewBinding.rvAddOn.adapter = adapter
        viewBinding.rvAddOn.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initializeData() {

    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.finish()
        super.onDismiss(dialog)
    }

}
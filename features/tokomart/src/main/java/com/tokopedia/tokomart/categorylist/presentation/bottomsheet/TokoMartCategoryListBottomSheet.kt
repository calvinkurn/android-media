package com.tokopedia.tokomart.categorylist.presentation.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.di.component.DaggerTokoMartCategoryListComponent
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapter
import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListAdapterTypeFactory
import com.tokopedia.tokomart.categorylist.presentation.adapter.differ.TokoMartCategoryListDiffer
import com.tokopedia.tokomart.categorylist.presentation.viewmodel.TokoMartCategoryListViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_tokomart_category_list.*
import javax.inject.Inject

class TokoMartCategoryListBottomSheet : BottomSheetUnify() {

    companion object {
        private val TAG = TokoMartCategoryListBottomSheet::class.simpleName

        fun newInstance(): TokoMartCategoryListBottomSheet {
            return TokoMartCategoryListBottomSheet()
        }
    }

    @Inject
    lateinit var viewModel: TokoMartCategoryListViewModel

    private val adapter by lazy {
        TokoMartCategoryListAdapter(
            TokoMartCategoryListAdapterTypeFactory(),
            TokoMartCategoryListDiffer()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryList()
        observeLiveData()
        getCategoryList()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initInjector() {
        DaggerTokoMartCategoryListComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initView(inflater: LayoutInflater, container: ViewGroup?) {
        val itemView = inflater.inflate(R.layout.bottomsheet_tokomart_category_list, container)
        val menuTitle = itemView.context.getString(R.string.tokomart_category_list_bottom_sheet_title)
        isFullpage = true
        setTitle(menuTitle)
        setChild(itemView)
    }

    private fun setupCategoryList() {
        with(rvCategoryList) {
            adapter = this@TokoMartCategoryListBottomSheet.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLiveData() {
        observe(viewModel.categoryList) {
            if(it is Success) {
                adapter.submitList(it.data)
            }
        }
    }

    private fun getCategoryList() {
        viewModel.getCategoryList()
    }
}
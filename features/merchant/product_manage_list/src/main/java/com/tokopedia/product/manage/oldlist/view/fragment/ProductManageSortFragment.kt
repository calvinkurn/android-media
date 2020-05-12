package com.tokopedia.product.manage.oldlist.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant.EXTRA_SORT_SELECTED
import com.tokopedia.product.manage.oldlist.constant.option.SortProductOption
import com.tokopedia.product.manage.oldlist.data.model.ProductManageSortModel
import com.tokopedia.product.manage.oldlist.di.DaggerOldProductManageComponent
import com.tokopedia.product.manage.oldlist.view.adapter.ProductManageSortAdapter
import com.tokopedia.product.manage.oldlist.view.adapter.ProductManageSortViewHolder
import com.tokopedia.product.manage.oldlist.view.presenter.ProductManageSortViewModel
import kotlinx.android.synthetic.main.fragment_manage_sort.*
import javax.inject.Inject

class ProductManageSortFragment : BaseDaggerFragment(), ProductManageSortViewHolder.ProductManageSortViewHolderListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductManageSortViewModel::class.java) }

    companion object {
        fun createInstance(selectedSortProduct: String) = ProductManageSortFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_SORT_SELECTED, selectedSortProduct)
            }
        }
    }

    private val productSortAdapter: ProductManageSortAdapter by lazy {
        ProductManageSortAdapter(this)
    }

    private var selectedSortProduct = SortProductOption.POSITION

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            val appComponent = (it.application as BaseMainApplication).baseAppComponent

            DaggerOldProductManageComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            selectedSortProduct = getString(EXTRA_SORT_SELECTED, SortProductOption.POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.product.manage.R.layout.fragment_manage_sort, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.listOfSortData.observe(this, Observer {
            onSuccessGetListSort(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_sort_data.apply {
            context?.let {
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
                adapter = productSortAdapter
            }
        }

        context?.let{
            viewModel.getListSortManageProduct(it, resources.getStringArray(com.tokopedia.product.manage.R.array.sort_option))
        }
    }

    private fun onSuccessGetListSort(productManageSortModels: List<ProductManageSortModel>) {
        productSortAdapter.setAdapterData(productManageSortModels)
        productSortAdapter.setSortProductOption(selectedSortProduct)
    }

    override fun onClickItem(data: ProductManageSortModel) {
        productSortAdapter.setSortProductOption(data.sortId)
        productSortAdapter.notifyDataSetChanged()
        val intent = Intent()
        activity?.let {
            intent.putExtra(EXTRA_SORT_SELECTED, data)
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    override fun isItemChecked(id: String): Boolean? = null

}
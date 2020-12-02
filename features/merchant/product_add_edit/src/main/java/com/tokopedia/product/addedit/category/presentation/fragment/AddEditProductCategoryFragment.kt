package com.tokopedia.product.addedit.category.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.common.Constant.INIT_SELECTED
import com.tokopedia.product.addedit.category.di.AddEditProductCategoryComponent
import com.tokopedia.product.addedit.category.presentation.adapter.AddEditProductCategoryAdapter
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel
import com.tokopedia.product.addedit.category.common.CategoryMapper
import com.tokopedia.product.addedit.category.common.Constant.INCREMENT_CATEGORY_LEVEL
import com.tokopedia.product.addedit.category.presentation.viewholder.AddEditProductCategoryViewHolder
import com.tokopedia.product.addedit.category.presentation.viewmodel.AddEditProductCategoryViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_edit_product_category.*
import javax.inject.Inject

class AddEditProductCategoryFragment : BaseDaggerFragment(), AddEditProductCategoryViewHolder.CategoryItemViewHolderListener {

    companion object {
        @JvmStatic
        fun newInstance(selectedCategory: Long) =
                AddEditProductCategoryFragment().apply {
                    arguments = Bundle().apply {
                        putLong(INIT_SELECTED, selectedCategory)
                    }
                }
    }

    @Inject
    lateinit var viewModel: AddEditProductCategoryViewModel

    private var adapter: AddEditProductCategoryAdapter? = null
    private var selectedCategory: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCategory = it.getLong(INIT_SELECTED)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_category, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddEditProductCategoryComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategoryLiteTree()
        adapter = AddEditProductCategoryAdapter(this)
        rvCategory.adapter = adapter
        rvCategory.layoutManager = LinearLayoutManager(context)
        observer()
    }

    private fun observer() {
        observe(viewModel.categoryLiteTree) {
            when (it) {
                is Success -> {
                    it.data.categories.categories
                    adapter?.updateCategories(CategoryMapper.mapCategoryToCategoryUiModel(it.data.categories.categories, 0))
                    adapter?.putIntoTempCategories()
                }
                is Fail -> {
                    Log.d("Category Fail", it.toString())
                }
            }
        }
    }

    override fun selectCategoryItem(category: CategoryUiModel, isHasChild: Boolean, adapter: AddEditProductCategoryAdapter) {
        if (category.isSelected) {
            adapter.setCategories(category, isHasChild)
        } else {
            adapter.resetCategories()
        }
    }

    override fun selectCategoryItem(categoryId: String) {
        Toast.makeText(context, "Hello $categoryId", Toast.LENGTH_LONG).show()
    }
}
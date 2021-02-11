package com.tokopedia.product.addedit.category.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.globalerror.GlobalError
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.core.common.category.view.model.CategoryViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.common.CategoryMapper
import com.tokopedia.product.addedit.category.common.Constant.CATEGORY_RESULT_FULL_NAME
import com.tokopedia.product.addedit.category.common.Constant.CATEGORY_RESULT_ID
import com.tokopedia.product.addedit.category.common.Constant.CATEGORY_RESULT_LEVEL
import com.tokopedia.product.addedit.category.common.Constant.CATEGORY_RESULT_NAME
import com.tokopedia.product.addedit.category.common.Constant.INIT_SELECTED
import com.tokopedia.product.addedit.category.di.AddEditProductCategoryComponent
import com.tokopedia.product.addedit.category.presentation.adapter.AddEditProductCategoryAdapter
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel
import com.tokopedia.product.addedit.category.presentation.viewholder.AddEditProductCategoryViewHolder
import com.tokopedia.product.addedit.category.presentation.viewmodel.AddEditProductCategoryViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_edit_product_category.*
import java.util.*
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
    private var resultCategories = mutableListOf<CategoryUiModel>()

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
        setup()
        observer()
    }

    override fun selectCategoryItem(category: CategoryUiModel, isHasChild: Boolean, adapter: AddEditProductCategoryAdapter, position: Int) {
        if (category.isSelected) {
            adapter.setCategories(category, isHasChild)
        } else {
            adapter.resetCategories()
        }
    }

    override fun selectCategoryItem(categoryId: String, categories: List<CategoryUiModel>?) {
        val intent = Intent()
        categories?.let {
            val modelCategories = CategoryMapper.mapCategoryUiModelToCategoryModel(categories)
            intent.putParcelableArrayListExtra(CATEGORY_RESULT_LEVEL, modelCategories as ArrayList<CategoryViewModel?>?)
            val chosenCategory: CategoryViewModel = modelCategories[categories.size - 1]
            intent.putExtra(CATEGORY_RESULT_ID, chosenCategory.id)
            intent.putExtra(CATEGORY_RESULT_NAME, chosenCategory.name)
            intent.putExtra(CATEGORY_RESULT_FULL_NAME, getCategoryResultFullName(modelCategories))
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun getCategoryResultFullName(listCategory: List<CategoryViewModel>?): String? {
        val sb = StringBuilder()
        if (listCategory != null) {
            var prefix = ""
            for (category in listCategory) {
                sb.append(prefix)
                prefix = "/"
                sb.append(category.name)
            }
        }
        return sb.toString()
    }

    private fun setup() {
        viewModel.getCategoryLiteTree()
        displayLoader()
        adapter = AddEditProductCategoryAdapter(this, resultCategories)
        rvCategory.adapter = adapter
        rvCategory.layoutManager = LinearLayoutManager(context)

        // set bg color programatically, to reduce overdraw
        requireActivity().window.decorView.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    private fun observer() {
        observe(viewModel.categoryLiteTree) {
            dismissLoader()
            when (it) {
                is Success -> {
                    onSuccessGetCategory(it.data)
                }
                is Fail -> {
                    onFailGetCategory()
                }
            }
        }
    }

    private fun displayLoader() {
        geCategory.hide()
        loaderUnify.show()
        rvCategory.hide()
    }

    private fun dismissLoader() {
        loaderUnify.hide()
        rvCategory.show()
    }

    private fun onSuccessGetCategory(data: CategoriesResponse) {
        val categories = data.categories.categories
        adapter?.updateCategories(CategoryMapper.mapCategoryToCategoryUiModel(categories, 0))
        adapter?.putIntoTempCategories()
    }

    private fun onFailGetCategory() {
        geCategory.apply {
            setType(GlobalError.SERVER_ERROR)
            setActionClickListener {
                viewModel.getCategoryLiteTree()
                displayLoader()
            }
        }.show()
        rvCategory.hide()
    }
}
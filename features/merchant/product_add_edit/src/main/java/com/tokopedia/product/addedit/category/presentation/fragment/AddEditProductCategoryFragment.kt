package com.tokopedia.product.addedit.category.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.common.feature.category.model.CategoriesResponse
import com.tokopedia.product.manage.common.feature.category.model.CategoryUIModel
import com.tokopedia.globalerror.GlobalError
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
import com.tokopedia.product.addedit.common.util.setFragmentToUnifyBgColor
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    private var rvCategory: RecyclerView? = null
    private var geCategory: GlobalError? = null
    private var loaderUnify: LoaderUnify? = null
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
        setup(view)
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
            intent.putParcelableArrayListExtra(CATEGORY_RESULT_LEVEL, modelCategories as? ArrayList<CategoryUIModel?>)
            val chosenCategory: CategoryUIModel = modelCategories[categories.size - 1]
            intent.putExtra(CATEGORY_RESULT_ID, chosenCategory.id)
            intent.putExtra(CATEGORY_RESULT_NAME, chosenCategory.name)
            intent.putExtra(CATEGORY_RESULT_FULL_NAME, getCategoryResultFullName(modelCategories))
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun getCategoryResultFullName(listCategory: List<CategoryUIModel>?): String? {
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

    private fun setup(view: View) {
        rvCategory = view.findViewById(R.id.rvCategory)
        geCategory = view.findViewById(R.id.geCategory)
        loaderUnify = view.findViewById(R.id.loaderUnify)

        viewModel.getCategoryLiteTree()
        displayLoader()
        adapter = AddEditProductCategoryAdapter(this, resultCategories)
        rvCategory?.adapter = adapter
        rvCategory?.layoutManager = LinearLayoutManager(context)

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()
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
        geCategory?.hide()
        loaderUnify?.show()
        rvCategory?.hide()
    }

    private fun dismissLoader() {
        loaderUnify?.hide()
        rvCategory?.show()
    }

    private fun onSuccessGetCategory(data: CategoriesResponse) {
        val categories = data.categories.categories
        adapter?.updateCategories(CategoryMapper.mapCategoryToCategoryUiModel(categories, 0))
        adapter?.putIntoTempCategories()
    }

    private fun onFailGetCategory() {
        geCategory?.apply {
            setType(GlobalError.SERVER_ERROR)
            setActionClickListener {
                viewModel.getCategoryLiteTree()
                displayLoader()
            }
        }?.show()
        rvCategory?.hide()
    }
}

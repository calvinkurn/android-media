package com.tokopedia.product.manage.list.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED
import com.tokopedia.product.manage.list.constant.option.CatalogProductOption
import com.tokopedia.product.manage.list.constant.option.ConditionProductOption
import com.tokopedia.product.manage.list.constant.option.PictureStatusProductOption
import com.tokopedia.product.manage.list.data.model.ProductManageFilterModel
import com.tokopedia.seller.product.category.view.activity.CategoryDynamicPickerActivity
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity
import com.tokopedia.seller.product.etalase.view.activity.EtalaseDynamicPickerActivity
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel
import com.tokopedia.seller.product.manage.view.model.ProductManageCategoryViewModel
import kotlinx.android.synthetic.main.fragment_product_manage_filter.*
import java.util.*

class ProductManageFilterFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance(productManageFilterModel: ProductManageFilterModel) = ProductManageFilterFragment().also {
            it.arguments = Bundle().apply {
                putParcelable(EXTRA_FILTER_SELECTED, productManageFilterModel)
            }
        }
    }

    private var productManageFilterModel: ProductManageFilterModel? = null

    fun onResetFilter() {
        productManageFilterModel?.reset()
        updateFilterView()
    }

    override fun getScreenName(): String = javaClass.simpleName;

    override fun initInjector() {
        // NO OP
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productManageFilterModel = it.getParcelable(EXTRA_FILTER_SELECTED)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.product.manage.list.R.layout.fragment_product_manage_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateFilterView()

        label_etalase.setOnClickListener {
            showEtalaseOption()
        }

        label_category.setOnClickListener {
            showCategoryOption()
        }

        label_condition.setOnClickListener {
            showConditionOption()
        }

        label_catalog.setOnClickListener {
            showCatalogOption()
        }

        label_product_picture.setOnClickListener {
            showProductPictureOption()
        }

        btn_submit_manage.setOnClickListener {
            onSubmitFilter()
        }

    }

    private fun onSubmitFilter() {
        activity?.let {
            val intent = Intent()
            intent.putExtra(EXTRA_FILTER_SELECTED, productManageFilterModel)
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    private fun updateFilterView() {
        productManageFilterModel?.let {
            if (TextUtils.isEmpty(it.etalaseProductOptionName)) {
                it.etalaseProductOptionName = getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_etalase_all)
            }
            label_etalase.setContent(MethodChecker.fromHtml(it.etalaseProductOptionName))
            if (TextUtils.isEmpty(it.categoryName)) {
                it.categoryName = getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_category_all)
            }
            label_category.setContent(it.categoryName)
            when (it.conditionProductOption) {
                ConditionProductOption.ALL_CONDITION -> label_condition.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_both_condition))
                ConditionProductOption.NEW -> label_condition.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_condition_new))
                ConditionProductOption.USED -> label_condition.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_condition_old))
                else -> label_condition.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_both_condition))
            }
            when (it.catalogProductOption) {
                CatalogProductOption.WITH_AND_WITHOUT -> label_catalog.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_both_catalog))
                CatalogProductOption.WITH_CATALOG -> label_catalog.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_with_catalog))
                CatalogProductOption.WITHOUT_CATALOG -> label_catalog.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_without_catalog))
                else -> label_catalog.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_both_catalog))
            }
            when (it.pictureStatusOption) {
                PictureStatusProductOption.WITH_AND_WITHOUT -> label_product_picture.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_picture_both))
                PictureStatusProductOption.WITH_IMAGE -> label_product_picture.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_with_picture))
                PictureStatusProductOption.WITHOUT_IMAGE -> label_product_picture.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_without_picture))
                else -> label_product_picture.setContent(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_picture_both))
            }
        }
    }

    private fun showEtalaseOption() {
        val myEtalaseItemViewModels = ArrayList<MyEtalaseItemViewModel>()
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_ALL_PRODUK, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_etalase_all)))
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_SOLD_PRODUK, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_sold)))
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_EMPTY_STOK, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_empty_stok)))
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_PENDING, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_pending)))
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_FREE_RETURNS, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_free_returns)))
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_PREORDER, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_preorder)))
        myEtalaseItemViewModels.add(MyEtalaseItemViewModel(ProductManageListConstant.FILTER_ALL_SHOWCASE, getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_all_showcase)))
        productManageFilterModel?.let {
            val intent = EtalaseDynamicPickerActivity.createInstance(activity, it.etalaseProductOption.toLong(), myEtalaseItemViewModels)
            startActivityForResult(intent, ProductManageListConstant.REQUEST_CODE_ETALASE)
        }
    }

    private fun showCategoryOption() {
        var categoryId = 0L
        productManageFilterModel?.let {
            categoryId = if (it.categoryId.isNotEmpty()) {
                it.categoryId.toLongOrNull() ?: 0
            } else {
                0
            }
        }

        val categoryViewModels = ArrayList<ProductManageCategoryViewModel>()
        categoryViewModels.add(ProductManageCategoryViewModel(getString(com.tokopedia.product.manage.list.R.string.product_manage_filter_menu_category_all), ProductManageListConstant.FILTER_ALL_CATEGORY, false))
        val intent = CategoryDynamicPickerActivity.createIntent(activity, categoryId, categoryViewModels)
        startActivityForResult(intent, ProductManageListConstant.REQUEST_CODE_CATEGORY)
    }

    private fun showConditionOption() {
        showBottomSheetOption(label_condition.title, com.tokopedia.product.manage.list.R.menu.menu_product_manage_filter_condition_option,
                label_condition.content, BottomSheetItemClickListener { item ->
            val itemId = item.itemId
            productManageFilterModel?.let {
                when (itemId) {
                    com.tokopedia.product.manage.list.R.id.both_condtion_option_menu -> it.conditionProductOption = ConditionProductOption.ALL_CONDITION
                    com.tokopedia.product.manage.list.R.id.new_condition_option_menu -> it.conditionProductOption = ConditionProductOption.NEW
                    com.tokopedia.product.manage.list.R.id.old_condition_option_menu -> it.conditionProductOption = ConditionProductOption.USED
                }
                updateFilterView()
            }
        })
    }

    private fun showCatalogOption() {
        showBottomSheetOption(label_catalog.title, com.tokopedia.product.manage.list.R.menu.menu_product_manage_filter_catalog_option,
                label_catalog.content, BottomSheetItemClickListener { item ->
            val itemId = item.itemId
            productManageFilterModel?.let {
                when (itemId) {
                    com.tokopedia.product.manage.list.R.id.both_catalog_option_menu -> it.catalogProductOption = CatalogProductOption.WITH_AND_WITHOUT
                    com.tokopedia.product.manage.list.R.id.without_catalog_option_menu -> it.catalogProductOption = CatalogProductOption.WITHOUT_CATALOG
                    com.tokopedia.product.manage.list.R.id.with_catalog_option_menu -> it.catalogProductOption = CatalogProductOption.WITH_CATALOG
                }
                updateFilterView()
            }
        })
    }

    private fun showProductPictureOption() {
        showBottomSheetOption(label_product_picture.title, com.tokopedia.product.manage.list.R.menu.menu_product_manage_filter_picture_option,
                label_product_picture.content, BottomSheetItemClickListener { item ->
            val itemId = item.itemId
            productManageFilterModel?.let {
                when (itemId) {
                    com.tokopedia.product.manage.list.R.id.both_picture_option_menu -> it.pictureStatusOption = PictureStatusProductOption.WITH_AND_WITHOUT
                    com.tokopedia.product.manage.list.R.id.without_picture_option_menu -> it.pictureStatusOption = PictureStatusProductOption.WITHOUT_IMAGE
                    com.tokopedia.product.manage.list.R.id.with_picture_option_menu -> it.pictureStatusOption = PictureStatusProductOption.WITH_IMAGE
                }
                updateFilterView()
            }
        })
    }


    private fun showBottomSheetOption(title: String, @MenuRes menu: Int, titleSelected: String,
                                      bottomSheetItemClickListener: BottomSheetItemClickListener) {
        val bottomSheetBuilder = CheckedBottomSheetBuilder(activity)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(title)
                .setMenu(menu)

        (bottomSheetBuilder as CheckedBottomSheetBuilder).setSelection(titleSelected)

        val bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(bottomSheetItemClickListener)
                .createDialog()
        bottomSheetDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ProductManageListConstant.REQUEST_CODE_CATEGORY -> if (resultCode == Activity.RESULT_OK) {
                data?.run {
                    val categoryId = getLongExtra(CategoryPickerActivity.CATEGORY_RESULT_ID, -1)
                    val categoryName = getStringExtra(CategoryPickerActivity.CATEGORY_RESULT_NAME)
                    productManageFilterModel?.let {
                        it.categoryId = categoryId.toString()
                        it.categoryName = categoryName
                    }
                    updateFilterView()
                }
            }
            ProductManageListConstant.REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                data?.run {
                    val etalaseId = getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1)
                    val etalaseName = getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME)
                    productManageFilterModel?.let {
                        it.etalaseProductOption = etalaseId
                        it.etalaseProductOptionName = etalaseName
                    }
                    updateFilterView()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
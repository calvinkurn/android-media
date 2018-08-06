package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.constant.ProductExtraConstant
import com.tokopedia.product.edit.di.component.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.edit.di.module.ProductEditCategoryCatalogModule
import com.tokopedia.product.edit.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.price.viewholder.ProductEditNameViewHolder
import com.tokopedia.product.edit.view.activity.ProductAddActivity
import com.tokopedia.product.edit.view.listener.ProductEditCategoryView
import kotlinx.android.synthetic.main.fragment_product_add_name_category.*

class ProductAddNameCategoryFragment : BaseProductEditCategoryFragment(), ProductEditNameViewHolder.Listener, ProductEditCategoryView {

    private var productName = ProductName()

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_add_name_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditNameViewHolder(view, this)
        texViewMenu.text = getString(R.string.label_next)
        texViewMenu.setOnClickListener { goToNext() }
        validateData()
    }

    override fun onNameChanged(productName: ProductName) {
        if (productName.name.isNotEmpty()) {
            categoryCatalogSection.visibility = View.VISIBLE
        } else {
            categoryCatalogSection.visibility = View.GONE
        }
        this.productName = productName
        presenter.getCategoryRecommendation(productName.name)
        presenter.fetchCatalogData(productName.name, productCategory.categoryId.toLong(), 0, 1)
        validateData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data!!.getParcelableExtra(EXTRA_CATALOG)
                    setCatalogChosen(productCatalog)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    val newCategoryId = data!!.getLongExtra(ProductExtraConstant.CATEGORY_RESULT_ID, -1).toInt()
                    if(productCategory.categoryId != newCategoryId){
                        presenter.fetchCatalogData(productName.name, newCategoryId.toLong(), 0, 1)
                    }
                    productCategory.categoryId = newCategoryId
                    presenter.fetchCategory(productCategory.categoryId.toLong())
                }
                REQUEST_CODE_GET_IMAGES -> {
                    val imageUrlOrPathList = data!!.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    startActivity(ProductAddActivity.createInstance(activity, productCatalog, productCategory, productName, imageUrlOrPathList))
                    activity?.finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        super.onCategoryRecommendationChoosen(productCategory)
        validateData()
    }

    private fun validateData(){
        if (isDataValid()) {
            texViewMenu.setTextColor(ContextCompat.getColor(texViewMenu.context, R.color.tkpd_main_green))
        } else {
            texViewMenu.setTextColor(ContextCompat.getColor(texViewMenu.context, R.color.font_black_secondary_54))
        }
    }

    private fun isDataValid() = productName.name.isNotEmpty() && productCategory.categoryId > 0

    private fun goToNext(){
        if(isDataValid()){
            startActivityForResult(AddProductImagePickerBuilder.createPickerIntentPrimary(activity, ArrayList()), REQUEST_CODE_GET_IMAGES)
        }
    }

    companion object {
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2
        const val REQUEST_CODE_GET_IMAGES = 100
        fun createInstance() = ProductAddNameCategoryFragment()
    }
}

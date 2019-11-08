package com.tokopedia.product.manage.item.main.add.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.*
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.category.di.ProductEditCategoryCatalogModule
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.name.view.viewholder.ProductEditNameViewHolder
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddActivity
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IMAGES
import com.tokopedia.product.manage.item.catalog.view.listener.ProductEditCategoryView
import com.tokopedia.product.manage.item.category.di.DaggerProductEditCategoryCatalogComponent
import com.tokopedia.product.manage.item.category.view.fragment.BaseProductEditCategoryFragment
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant
import kotlinx.android.synthetic.main.fragment_product_add_name_category.*

class ProductAddNameCategoryFragment : BaseProductEditCategoryFragment(), ProductEditNameViewHolder.Listener, ProductEditCategoryView {

    private var flagReset = false
    private var productName = ProductName()
    private var productPictureList : ArrayList<String>? = ArrayList()
    private lateinit var productEditNameViewHolder: ProductEditNameViewHolder
    override fun initInjector() {
        DaggerProductEditCategoryCatalogComponent.builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productEditCategoryCatalogModule(ProductEditCategoryCatalogModule())
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productPictureList = arguments?.getStringArrayList(EXTRA_IMAGES)
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_RESET_DATA)) {
                flagReset = savedInstanceState.getBoolean(SAVED_RESET_DATA)
            }
            if (savedInstanceState.containsKey(SAVED_PRODUCT_NAME)) {
                productName = savedInstanceState.getParcelable(SAVED_PRODUCT_NAME)
            }
            if (savedInstanceState.containsKey(SAVED_PRODUCT_IMAGES)){
                productPictureList = savedInstanceState.getStringArrayList(SAVED_PRODUCT_IMAGES)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_add_name_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productEditNameViewHolder = ProductEditNameViewHolder(view, this)
        texViewMenu?.run {
            text = getString(R.string.label_next)
            setOnClickListener {
                goToNext()
            }
        }
        validateData()
    }

    override fun onNameChanged(productName: ProductName) {
        categoryCatalogSection?.visibility = if (productName.name.isNotEmpty()) View.VISIBLE else View.GONE
        if(!flagReset){
            this.productName = productName
            presenter.onProductNameChange(productName.name)
            name = productName.name
            validateData()
            resetCategoryCatalog()
        }
        flagReset = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_GET_IMAGES -> {
                    productPictureList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    startActivity(ProductAddActivity.createInstance(activity, productCatalog, productCategory, productName, productPictureList
                            ?: ArrayList()))
                    activity?.finish()
                }
            }
        }
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        super.onCategoryRecommendationChoosen(productCategory)
        validateData()
    }

    private fun validateData(){
        texViewMenu?.run {
            setTextColor(ContextCompat.getColor(context, if (isDataValid()) R.color.tkpd_main_green else R.color.font_black_secondary_54))
        }
    }

    private fun isNameValid(): Boolean{
        return productName.name.isNotEmpty()
    }

    private fun isCategoryChosen(): Boolean{
        return productCategory.categoryId > 0
    }

    private fun isDataValid() = isNameValid() && isCategoryChosen()

    private fun goToNext(){
        if(isDataValid()){
            var catalogId = ""
            if (productCatalog.catalogId > 0) {
                catalogId = productCatalog.catalogId.toString()
            }
            flagReset = true
            startActivityForResult(AddProductImagePickerBuilder.createPickerIntentWithCatalog(activity, productPictureList, catalogId), REQUEST_CODE_GET_IMAGES)
        } else {
            if(!isNameValid())
                productEditNameViewHolder.validateForm()
            else if(!isCategoryChosen())
                NetworkErrorHelper.showRedCloseSnackbar(activity, getString(R.string.error_select_category))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        flagReset = true
        outState.putBoolean(SAVED_RESET_DATA, flagReset)
        outState.putParcelable(SAVED_PRODUCT_NAME, productName)
        outState.putStringArrayList(SAVED_PRODUCT_IMAGES, productPictureList)
    }

    companion object {
        const val SAVED_RESET_DATA = "SAVED_RESET_DATA"
        const val SAVED_PRODUCT_NAME = "SAVED_PRODUCT_NAME"
        const val SAVED_PRODUCT_IMAGES = "SAVED_PRODUCT_IMAGES"
        const val REQUEST_CODE_GET_IMAGES = 100
        fun createInstance(imageUrls: ArrayList<String>?) : ProductAddNameCategoryFragment {
            val productAddNameCategoryFragment = ProductAddNameCategoryFragment()
            val bundle = Bundle()
            bundle.putStringArrayList(BaseProductAddEditFragment.EXTRA_IMAGES, imageUrls)
            productAddNameCategoryFragment.arguments =bundle
            return productAddNameCategoryFragment
        }
    }
}

package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.TextView
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.*
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IMAGES
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder
import com.tokopedia.product.edit.price.viewholder.ProductEditNameViewHolder
import com.tokopedia.product.edit.view.activity.ProductAddActivity
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity
import kotlinx.android.synthetic.main.fragment_product_add_name_category.*

class ProductAddNameCategoryFragment : Fragment(), ProductEditNameViewHolder.Listener, ProductEditCategoryCatalogViewHolder.Listener {

    private lateinit var productEditCategoryCatalogViewHolder: ProductEditCategoryCatalogViewHolder
    private var productCatalog = ProductCatalog()
    private var productName = ProductName()
    private var productCategory = ProductCategory()
    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_add_name_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditNameViewHolder(view, this)
        productEditCategoryCatalogViewHolder = ProductEditCategoryCatalogViewHolder(view, this, context)
        texViewMenu.text = getString(R.string.label_next)
        texViewMenu.setOnClickListener { goToNext() }
        validateData()
    }

    override fun onNameChanged(productName: ProductName) {
        if (productName.name!!.isNotEmpty()) {
            categoryCatalogViewHolder.visibility = View.VISIBLE
        } else {
            categoryCatalogViewHolder.visibility = View.GONE
        }
        this.productName = productName
        validateData()
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        this.productCategory = productCategory
        productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
        validateData()
    }

    override fun onLabelCategoryClicked() {

    }

    override fun onLabelCatalogClicked() {
        startActivityForResult(Intent(context, ProductEditCatalogPickerActivity::class.java)
                .putExtra(EXTRA_CATALOG, productCatalog), REQUEST_CODE_GET_CATALOG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data!!.getParcelableExtra(EXTRA_CATALOG)
                    productEditCategoryCatalogViewHolder.setCatalogChosen(productCatalog)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    productCategory = data!!.getParcelableExtra(EXTRA_CATEGORY)
                    productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory)
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

    private fun validateData(){
        if (isDataValid()) {
            texViewMenu.setTextColor(ContextCompat.getColor(texViewMenu.context, R.color.tkpd_main_green))
        } else {
            texViewMenu.setTextColor(ContextCompat.getColor(texViewMenu.context, R.color.font_black_secondary_54))
        }
    }

    private fun isDataValid() = productName.name != null && productName.name!!.isNotEmpty() && productCategory.categoryName != null

    private fun goToNext(){
        if(isDataValid()){
            val builder = ImagePickerBuilder(getString(R.string.product_label_product_picture),
                    intArrayOf(TYPE_GALLERY, TYPE_CAMERA), GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE),
                            false, null), null)
            val intent = ImagePickerActivity.getIntent(context, builder)
            startActivityForResult(intent, REQUEST_CODE_GET_IMAGES)
        }
    }

    companion object {
        const val REQUEST_CODE_GET_CATEGORY = 1
        const val REQUEST_CODE_GET_CATALOG = 2
        const val REQUEST_CODE_GET_IMAGES = 100
        fun createInstance() = ProductAddNameCategoryFragment()
    }
}

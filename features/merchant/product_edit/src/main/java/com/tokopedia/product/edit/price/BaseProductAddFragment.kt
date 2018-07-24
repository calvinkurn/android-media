package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
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
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_CATEGORY
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_IMAGES
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_NAME
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder.Companion.REQUEST_CODE_GET_CATALOG
import com.tokopedia.product.edit.price.viewholder.ProductEditCategoryCatalogViewHolder.Companion.REQUEST_CODE_GET_CATEGORY
import com.tokopedia.product.edit.price.viewholder.ProductEditNameViewHolder
import com.tokopedia.product.edit.view.activity.BaseProductEditActivity
import com.tokopedia.product.edit.view.activity.ProductEditCatalogPickerActivity
import kotlinx.android.synthetic.main.fragment_product_add_new.*

class BaseProductAddFragment : Fragment(), ProductEditNameViewHolder.Listener, ProductEditCategoryCatalogViewHolder.Listener {

    private lateinit var productEditCategoryCatalogViewHolder: ProductEditCategoryCatalogViewHolder
    private lateinit var productCatalog: ProductCatalog
    private lateinit var productName: ProductName
    private lateinit var productCategory: ProductCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_add_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ProductEditNameViewHolder(view, this)
        productEditCategoryCatalogViewHolder = ProductEditCategoryCatalogViewHolder(view, this, context)
    }

    override fun onNameChanged(productName: ProductName) {
        if (productName.name!!.isNotEmpty()) {
            categoryCatalogViewHolder.visibility = View.VISIBLE
        } else {
            categoryCatalogViewHolder.visibility = View.GONE
        }
        this.productName = productName
    }

    override fun onCategoryRecommendationChoosen(productCategory: ProductCategory) {
        this.productCategory = productCategory
        productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory.categoryName!!)
    }

    override fun onLabelCategoryClicked() {

    }

    override fun onLabelCatalogClicked() {
        startActivityForResult(Intent(context, ProductEditCatalogPickerActivity::class.java), REQUEST_CODE_GET_CATALOG)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_next, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_next) {
            val builder = ImagePickerBuilder(getString(R.string.product_label_product_picture),
                    intArrayOf(TYPE_GALLERY, TYPE_CAMERA), GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE),
                            false, null), null)
            val intent = ImagePickerActivity.getIntent(context, builder)
            startActivityForResult(intent, REQUEST_CODE_GET_IMAGES)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_CATALOG -> {
                    productCatalog = data.getParcelableExtra(EXTRA_CATALOG)
                    productEditCategoryCatalogViewHolder.setCatalogChosen(productCatalog.catalogName!!)
                }
                REQUEST_CODE_GET_CATEGORY -> {
                    productCategory = data.getParcelableExtra(EXTRA_CATEGORY)
                    productEditCategoryCatalogViewHolder.setCategoryChosen(productCategory.categoryName!!)
                }
                REQUEST_CODE_GET_IMAGES -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)

                    val intent = Intent(activity, BaseProductEditActivity::class.java)
                    intent.putExtra(EXTRA_NAME, productName)
                    intent.putExtra(EXTRA_CATALOG, productCatalog)
                    intent.putExtra(EXTRA_CATEGORY, productCategory)
                    intent.putStringArrayListExtra(EXTRA_IMAGES, imageUrlOrPathList)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val REQUEST_CODE_GET_IMAGES = 100
        fun createInstance(): Fragment {
            return BaseProductAddFragment()
        }
    }
}

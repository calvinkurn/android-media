package com.tokopedia.product.addedit.preview.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.presentation.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.unifyprinciples.Typography

class AddEditProductPreviewFragment : BaseDaggerFragment() {

    private var addEditProductPhotoButton: AppCompatTextView? = null

    companion object {
        fun createInstance(): Fragment {
            return AddEditProductPreviewFragment()
        }


        private const val MAX_PRODUCT_PHOTOS = 5
        private const val REQUEST_CODE_IMAGE = 0x01
        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addEditProductPhotoButton = view.findViewById(R.id.tv_start_add_edit_product_photo)
        addEditProductPhotoButton?.setOnClickListener {
            val intent = ImagePickerAddProductActivity.getIntent(context, createImagePickerBuilder())
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }

        val addProductPhotoSection: ViewGroup = view.findViewById(R.id.add_product_photo_section)
        val tvStartAddEditProductDescription: Typography = view.findViewById(R.id.tv_start_add_edit_product_description)
        addProductPhotoSection.setOnClickListener {
            showPhotoTips()
        }

        tvStartAddEditProductDescription.setOnClickListener {
            moveToDescriptionActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    val addEditProductDetailIntent = Intent(context, AddEditProductDetailActivity::class.java)
                    addEditProductDetailIntent.putStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS, imageUrlOrPathList)
                    startActivity(addEditProductDetailIntent)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

    private fun showPhotoTips() {
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<ImageTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_photo_tips)
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_1), TEST_IMAGE_URL))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_2), TEST_IMAGE_URL))
            tips.add(ImageTooltipModel(getString(R.string.message_tooltip_photo_tips_3), TEST_IMAGE_URL))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                show(it, null)
            }
        }
    }

    private fun moveToDescriptionActivity() {
        startActivity(AddEditProductDescriptionActivity.createInstance(context))
    }

    @SuppressLint("WrongConstant")
    private fun createImagePickerBuilder(): ImagePickerBuilder {

        val title = getString(R.string.action_pick_image)

        val placeholderDrawableRes = arrayListOf(
                R.drawable.ic_utama,
                R.drawable.ic_depan,
                R.drawable.ic_samping,
                R.drawable.ic_atas,
                R.drawable.ic_detail
        )

        val imagePickerPickerTabTypeDef = intArrayOf(
                ImagePickerTabTypeDef.TYPE_GALLERY,
                ImagePickerTabTypeDef.TYPE_CAMERA,
                ImagePickerTabTypeDef.TYPE_INSTAGRAM
        )

        val imagePickerEditorBuilder = ImagePickerEditorBuilder.getDefaultBuilder()

        val imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                null,
                placeholderDrawableRes,
                R.string.label_primary,
                MAX_PRODUCT_PHOTOS, false)

        return ImagePickerBuilder(
                title,
                imagePickerPickerTabTypeDef,
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                ImageRatioTypeDef.RATIO_1_1,
                true,
                imagePickerEditorBuilder,
                imagePickerMultipleSelectionBuilder)
    }
}

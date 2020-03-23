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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.AddEditProductDescriptionActivity
import com.tokopedia.product.addedit.description.presentation.AddEditProductDescriptionFragment.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment.Companion.EXTRA_DETAIL_INPUT
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment.Companion.REQUEST_CODE_DETAIL
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.service.AddEditProductUploadService
import com.tokopedia.product.addedit.preview.presentation.viewmodel.AddEditProductPreviewViewModel
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddEditProductPreviewFragment : BaseDaggerFragment() {

    private var addEditProductPhotoButton: AppCompatTextView? = null
    private lateinit var previewViewModel: AddEditProductPreviewViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun createInstance(): Fragment {
            return AddEditProductPreviewFragment()
        }

        private const val MAX_PRODUCT_PHOTOS = 5
        private const val REQUEST_CODE_IMAGE = 0x01

        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeProductUpdateLiveData()
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
                    moveToAddEditProductActivity(imageUrlOrPathList)
                }
            } else if (requestCode == REQUEST_CODE_DETAIL) {
                val shipmentInputModel =
                        data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                val descriptionInputModel =
                        data.getParcelableExtra<DescriptionInputModel>(EXTRA_DESCRIPTION_INPUT)
                val detailInputModel =
                        data.getParcelableExtra<DetailInputModel>(EXTRA_DETAIL_INPUT)
                AddEditProductUploadService.startService(context!!, detailInputModel, descriptionInputModel, shipmentInputModel)
            }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerAddEditProductPreviewComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductPreviewModule(AddEditProductPreviewModule())
                .build()
                .inject(this)
    }

    private fun initViewModel() {
        activity?.run {
            previewViewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(AddEditProductPreviewViewModel::class.java)
        }
    }

    // TODO faisalramd redesign toast
    private fun observeProductUpdateLiveData() {
        previewViewModel._productUpdateResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val isSuccess = result.data.productAddEditV3Data.isSuccess
                    var toasterType = Toaster.TYPE_NORMAL
                    var toasterMessage = "Success"

                    if (isSuccess) {
                        toasterMessage = result.data.productAddEditV3Data.header.reason
                        toasterType = Toaster.TYPE_ERROR
                    }

                    Toaster.make(view!!, toasterMessage, Toaster.LENGTH_LONG, toasterType)
                }
                is Fail -> {
                    result.throwable.printStackTrace()
                }
            }
        })
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

    private fun moveToDescriptionActivity() {
        startActivity(AddEditProductDescriptionActivity.createInstance(context))
    }

    private fun moveToAddEditProductActivity(imageUrlOrPathList: ArrayList<String>) {
        val addEditProductDetailIntent = Intent(context, AddEditProductDetailActivity::class.java)
        addEditProductDetailIntent.putStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS, imageUrlOrPathList)
        startActivityForResult(addEditProductDetailIntent, REQUEST_CODE_DETAIL)
    }
}

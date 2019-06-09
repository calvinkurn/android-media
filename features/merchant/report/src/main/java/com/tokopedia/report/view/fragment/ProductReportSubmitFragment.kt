package com.tokopedia.report.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.activity.ProductReportFormActivity
import com.tokopedia.report.view.activity.ReportInputDetailActivity
import com.tokopedia.report.view.adapter.ReportFormAdapter
import com.tokopedia.report.view.viewmodel.ProductReportSubmitViewModel
import kotlinx.android.synthetic.main.fragment_product_report.*
import javax.inject.Inject

class ProductReportSubmitFragment : BaseDaggerFragment() {
    private lateinit var adapter: ReportFormAdapter
    private var photoTypeSelected: String? = null
    override fun getScreenName(): String? = null
    private var dialogSubmit: Dialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductReportSubmitViewModel

    override fun initInjector() {
        getComponent(MerchantReportComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val vmProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = vmProvider.get(ProductReportSubmitViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cacheId = arguments?.getString(ProductReportFormActivity.REASON_CACHE_ID, "") ?: ""
        val reason: ProductReportReason? = context?.let {
            val cacheManager = SaveInstanceCacheManager(it, cacheId)
            cacheManager.get(ProductReportFormActivity.REASON_OBJECT, ProductReportReason::class.java)

        }
        recycler_view.clearItemDecoration()
        reason?.let {reasonItem ->
            val popupField = reasonItem.additionalFields.firstOrNull { additionalField -> additionalField.type == "popup" }
            if (popupField != null){
                dialogSubmit = Dialog(activity, Dialog.Type.PROMINANCE).apply {
                    setTitle(popupField.value)
                    setDesc(popupField.detail)
                    setBtnOk(getString(R.string.label_report))
                    setBtnCancel(getString(R.string.cancel))
                    setOnCancelClickListener { dismiss() }
                    setOnOkClickListener {
                        dismiss()
                        viewModel.submitReport(-1, reasonItem.categoryId, adapter.inputs)
                        adapter.inputs
                    }
                }
            }

            adapter = ReportFormAdapter(reasonItem, this::openInputDetail, this::openPhotoPicker, this::onSubmitClicked)
            recycler_view.adapter = adapter
        }
    }

    private fun onSubmitClicked(){
        val total = adapter.itemCount - 1
        var valid = true
        for (i in 1..total){
            val holder = recycler_view.findViewHolderForAdapterPosition(i-1)
            if ( holder is ReportFormAdapter.ValidateViewHolder)
                valid = valid and holder.validate()
        }
        if (valid){
            dialogSubmit?.show()
        } else {
            Toast.makeText(context, "YAH GA VALID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openInputDetail(key: String, value: String, minChar: Int, maxChar: Int){
        photoTypeSelected = key
        context?.let {
            startActivityForResult(ReportInputDetailActivity.createIntent(it, value, minChar, maxChar), REQUEST_CODE_DETAIL_INPUT)
        }
    }

    private fun openPhotoPicker(photoType: String){
        this.photoTypeSelected = photoType
        context?.let {
            val builder = ImagePickerBuilder(getString(R.string.report_choose_picture),
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_INSTAGRAM),
                    GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST,
                                    ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE),
                            false, null), null)

            val intent = ImagePickerActivity.getIntent(it, builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null){
            if (requestCode == REQUEST_CODE_IMAGE) {
                val imageUrlOrPathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
                if (imageUrlOrPathList != null && imageUrlOrPathList.size > 0) {
                    photoTypeSelected?.let {
                        adapter.updatePhotoForType(it, imageUrlOrPathList[0])
                    }
                    photoTypeSelected = null
                }
            } else if (requestCode == REQUEST_CODE_DETAIL_INPUT){
                photoTypeSelected?.let {
                    val input = data.getStringExtra(ReportInputDetailFragment.INPUT_VALUE)
                    adapter.updateTextInput(it, input)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    companion object {
        private const val REQUEST_CODE_IMAGE = 0x01
        private const val REQUEST_CODE_DETAIL_INPUT = 0x02

        fun createInstance(cacheId: String) = ProductReportSubmitFragment().apply {
            arguments = Bundle().also {
                it.putString(ProductReportFormActivity.REASON_CACHE_ID, cacheId)
            }
        }
    }
}
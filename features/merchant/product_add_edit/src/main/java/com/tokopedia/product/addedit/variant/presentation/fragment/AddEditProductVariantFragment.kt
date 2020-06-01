package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imagepicker.common.util.FileUtils
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantPhotoAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantValueAdapter
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_SIZECHART_IMAGE
import com.tokopedia.product.addedit.variant.presentation.dialog.AddEditProductVariantSizechartDialogFragment
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.add_edit_product_variant_photo_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_sizechart_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_type_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level1_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level2_layout.*
import javax.inject.Inject

class AddEditProductVariantFragment : BaseDaggerFragment(), VariantTypeAdapter.OnVariantTypeClickListener {

    companion object {
        fun createInstance(cacheManagerId: String?): Fragment {
            return AddEditProductVariantFragment().apply {
                arguments = Bundle().apply {
                    putString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: AddEditProductVariantViewModel
    private var variantTypeAdapter: VariantTypeAdapter? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        variantTypeAdapter = VariantTypeAdapter(this)
        val variantValueAdapter = VariantValueAdapter()
        val variantPhotoAdapterAdapter = VariantPhotoAdapter()
        recyclerViewVariantType.adapter = variantTypeAdapter
        recyclerViewVariantValueLevel1.adapter = variantValueAdapter
        recyclerViewVariantValueLevel2.adapter = variantValueAdapter
        recyclerViewVariantPhoto.adapter = variantPhotoAdapterAdapter
        setRecyclerViewToFlex(recyclerViewVariantType)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel1)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel2)
        setRecyclerViewToHorizontal(recyclerViewVariantPhoto)

        observeSizechartUrl()
        observeProductData()
        viewModel.getCategoryVariantCombination("916")

        cardSizechart.setOnClickListener {
            onSizechartClicked()
        }

        Handler().postDelayed({
            val variants: List<String> = listOf("ukuran yang menentukan semua yang terukur", "warna", "rasya", "dsdsdsf", "asdasdas dasd", "sadsdsdsdasda")
            variantValueAdapter.setData(variants)
            variantPhotoAdapterAdapter.setData(variants)
        }, 1000)
    }

    override fun onVariantTypeClicked(selectedVariantDetails: List<VariantDetail>) {
        // TODO implement selectedVariantDetails to variant values sections
        selectedVariantDetails.forEach {
            Log.e("--", it.name)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_SIZECHART_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    imageUrlOrPathList.forEach {
                        viewModel.variantSizechartUrl.value = it
                    }
                }
            }
        }
    }

    private fun onSizechartClicked() {
        if (viewModel.variantSizechartUrl.value.isNullOrEmpty()) {
            showReplaceSizechartPicker()
        } else {
            val fm = activity!!.supportFragmentManager
            val dialogFragment = AddEditProductVariantSizechartDialogFragment.newInstance()
            dialogFragment.show(fm, AddEditProductVariantSizechartDialogFragment.FRAGMENT_TAG)
            dialogFragment.setOnImageEditListener(object:
                    AddEditProductVariantSizechartDialogFragment.OnImageEditListener {
                override fun clickImageEditor() {
                    showEditorSizechartPicker()
                }

                override fun clickRemoveImage() {
                    removeSizechart()
                }

                override fun clickChangeImagePath() {
                    showReplaceSizechartPicker()
                }
            })
        }
    }

    private fun observeProductData() {
        viewModel.getCategoryVariantCombinationResult.observe(this, Observer { result ->
            when (result) {
                is Success -> {
                    val variantDetails =
                            result.data.getCategoryVariantCombination.data.variantDetails
                    variantTypeAdapter?.setData(variantDetails)
                    variantTypeAdapter?.setMaxSelectedItems(MAX_SELECTED_VARIANT_TYPE)
                }
                is Fail -> {
                    context?.let {
                        showGetCategoryVariantCombinationErrorToast(
                                ErrorHandler.getErrorMessage(it, result.throwable))
                    }
                }
            }
        })
    }

    private fun observeSizechartUrl() {
        viewModel.variantSizechartUrl.observe(this, Observer {
            if (it.isEmpty()){
                ivSizechartAddSign.visible()
                ivSizechartEditSign.gone()
                ivSizechart.gone()
                typographySizechartDescription.text = getString(R.string.label_variant_sizechart_description)
            } else {
                ivSizechartAddSign.gone()
                ivSizechartEditSign.visible()
                ivSizechart.visible()
                typographySizechartDescription.text = getString(R.string.label_variant_sizechart_edit_description)
            }
            ivSizechart.setImage(it, 0F)
        })
    }

    private fun removeSizechart() {
        val url = viewModel.variantSizechartUrl.value.orEmpty()
        viewModel.variantSizechartUrl.value = ""
        FileUtils.deleteFileInTokopediaFolder(url)
    }

    private fun showReplaceSizechartPicker(){
        context?.apply {
            val builder =  createSizechartImagePickerBuilder(this)
            val intent = ImagePickerActivity.getIntent(this, builder)
            startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
        }
    }

    private fun showEditorSizechartPicker() {
        val url = viewModel.variantSizechartUrl.value.orEmpty()
        context?.apply {
            val editorIntent = createEditorIntent(this, url)
            startActivityForResult(editorIntent, REQUEST_CODE_SIZECHART_IMAGE)
        }
    }

    private fun showGetCategoryVariantCombinationErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        viewModel.getCategoryVariantCombination("916")
                    })
        }
    }

    private fun setRecyclerViewToFlex(recyclerView: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }

    private fun setRecyclerViewToHorizontal(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }

    fun createSizechartImagePickerBuilder(context: Context): ImagePickerBuilder {
        return ImagePickerBuilder(context.getString(R.string.choose_image),
                intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY,
                        ImagePickerTabTypeDef.TYPE_CAMERA,
                        ImagePickerTabTypeDef.TYPE_INSTAGRAM),
                GalleryType.IMAGE_ONLY,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                ImageRatioTypeDef.RATIO_1_1,
                true,
                ImagePickerEditorBuilder(
                        intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                                ImageEditActionTypeDef.ACTION_CONTRAST,
                                ImageEditActionTypeDef.ACTION_CROP,
                                ImageEditActionTypeDef.ACTION_ROTATE),
                        false,
                        null),
                null)
    }

    fun createEditorIntent(context: Context, uriOrPath: String): Intent {
        return ImageEditorActivity.getIntent(context, uriOrPath,
                null,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION,
                intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS,
                        ImageEditActionTypeDef.ACTION_CONTRAST,
                        ImageEditActionTypeDef.ACTION_CROP,
                        ImageEditActionTypeDef.ACTION_ROTATE),
                ImageRatioTypeDef.RATIO_1_1, false,
                ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB.toLong(), null)
    }

    fun onBackPressed() {
        activity?.finish()
    }

}

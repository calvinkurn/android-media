package com.tokopedia.product.addedit.description.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_CURRENCY_TYPE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DEFAULT_PRICE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DEFAULT_SKU
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DESCRIPTION_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_HAS_ORIGINAL_VARIANT_LV1
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_HAS_ORIGINAL_VARIANT_LV2
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_HAS_WHOLESALE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_IS_ADD
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_IS_OFFICIAL_STORE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_IS_USING_CACHE_MANAGER
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_NEED_RETAIN_IMAGE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_SIZECHART
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_VARIANT_SELECTION
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_STOCK_TYPE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_INPUT
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_PICKER_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_CATEGORY_ID
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_DESCRIPTION_INPUT_MODEL
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_IS_EDIT_MODE
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_VARIANT_INPUT_MODEL
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.PictureViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.description.presentation.viewmodel.AddEditProductDescriptionViewModel
import com.tokopedia.product.addedit.shipment.presentation.activity.AddEditProductShipmentActivity
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment.Companion.REQUEST_CODE_SHIPMENT
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.ProductAddDescriptionTracking
import com.tokopedia.product.addedit.tracking.ProductEditDescriptionTracking
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.add_edit_product_description_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_video_input_layout.*
import kotlinx.android.synthetic.main.fragment_add_edit_product_description.*
import javax.inject.Inject

class AddEditProductDescriptionFragment:
        BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(),
        VideoLinkTypeFactory.VideoLinkListener {

    companion object {
        fun createInstance(categoryId: String): Fragment {
            return AddEditProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_CATEGORY_ID, categoryId)
                }
            }
        }
        fun createInstance(categoryId: String,
                           descriptionInputModel: DescriptionInputModel,
                           variantInputModel: ProductVariantInputModel,
                           isEditMode: Boolean): Fragment {
            return AddEditProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_CATEGORY_ID, categoryId)
                    putParcelable(PARAM_DESCRIPTION_INPUT_MODEL, descriptionInputModel)
                    putParcelable(PARAM_VARIANT_INPUT_MODEL, variantInputModel)
                    putBoolean(PARAM_IS_EDIT_MODE, isEditMode)
                }
            }
        }

        const val MAX_VIDEOS = 3
        const val REQUEST_CODE_VARIANT = 0

        const val TYPE_IDR = 1

        const val IS_ADD = 0
        const val REQUEST_CODE_DESCRIPTION = 0x03

        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }

    private var videoId = 0

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    @Inject
    lateinit var descriptionViewModel: AddEditProductDescriptionViewModel

    override fun getAdapterTypeFactory(): VideoLinkTypeFactory {
        val videoLinkTypeFactory = VideoLinkTypeFactory()
        videoLinkTypeFactory.setVideoLinkListener(this)

        return videoLinkTypeFactory
    }

    override fun onDeleteClicked(videoLinkModel: VideoLinkModel, position: Int) {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickRemoveVideoLink(shopId)
        } else {
            ProductAddDescriptionTracking.clickRemoveVideoLink(shopId)
        }
        adapter.data.removeAt(position)
        adapter.notifyDataSetChanged()
        textViewAddVideo.visibility =
                if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
    }

    override fun onTextChanged(url: String, position: Int) {
        adapter.data[position].inputUrl = url
    }

    override fun onItemClicked(t: VideoLinkModel?) {
        //no op
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerAddEditProductDescriptionComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductDescriptionModule(AddEditProductDescriptionModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        userSession = UserSession(requireContext())
        shopId = userSession.shopId
        super.onCreate(savedInstanceState)
        if (!descriptionViewModel.isEditMode) {
            ProductAddDescriptionTracking.trackScreen()
        }
        arguments?.let {
            val categoryId: String = it.getString(PARAM_CATEGORY_ID) ?: ""
            val isEditMode: Boolean = it.getBoolean(PARAM_IS_EDIT_MODE, false)
            val descriptionInputModel : DescriptionInputModel =
                    it.getParcelable(PARAM_DESCRIPTION_INPUT_MODEL) ?: DescriptionInputModel()
            val variantInputModel : ProductVariantInputModel =
                    it.getParcelable(PARAM_VARIANT_INPUT_MODEL) ?: ProductVariantInputModel()
            descriptionViewModel.categoryId = categoryId
            descriptionViewModel.descriptionInputModel = descriptionInputModel
            descriptionViewModel.variantInputModel = variantInputModel
            descriptionViewModel.isEditMode = isEditMode
        }
    }

    fun onBackPressed() {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickBack(shopId)
        } else {
            ProductAddDescriptionTracking.clickBack(shopId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFieldDescription.textFieldInput.setSingleLine(false)
        textFieldDescription.textFieldInput.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION

        if (descriptionViewModel.isEditMode) applyEditMode()

        textViewAddVideo.setOnClickListener {
            if (descriptionViewModel.isEditMode) {
                ProductEditDescriptionTracking.clickAddVideoLink(shopId)
            } else {
                ProductAddDescriptionTracking.clickAddVideoLink(shopId)
            }
            addEmptyVideoUrl()
        }

        layoutDescriptionTips.setOnClickListener {
            showDescriptionTips()
        }

        layoutVariantTips.setOnClickListener {
            showVariantTips()
        }

        tvAddVariant.setOnClickListener {
            if (descriptionViewModel.isEditMode) {
                ProductEditDescriptionTracking.clickAddProductVariant(shopId)
            } else {
                ProductAddDescriptionTracking.clickAddProductVariant(shopId)
            }
            descriptionViewModel.getVariants(descriptionViewModel.categoryId)
        }

        btnNext.setOnClickListener {
            moveToDescriptionActivity()
        }

        btnSave.setOnClickListener {
            submitInputEdit()
        }

        observeProductVariant()
    }

    private fun addEmptyVideoUrl() {
        videoId += 1
        loadData(videoId)
    }

    private fun observeProductVariant() {
        descriptionViewModel.productVariant.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> showVariantDialog(result.data)
                is Fail -> showVariantErrorToast(getString(R.string.title_tooltip_description_tips))
            }
        })
    }

    private fun applyEditMode() {
        val description = descriptionViewModel.descriptionInputModel.productDescription
        val videoLinks = descriptionViewModel.descriptionInputModel.videoLinkList

        if (videoLinks.isEmpty()) {
            addEmptyVideoUrl()
        } else {
            textFieldDescription.setText(description)
            super.clearAllData()
            super.renderList(videoLinks)
        }

        btnNext.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
    }

    private fun showVariantErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type =  Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    clickListener =  View.OnClickListener {
                        descriptionViewModel.getVariants(descriptionViewModel.categoryId)
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_SHIPMENT -> {
                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    submitInput(shipmentInputModel)
                }
                REQUEST_CODE_VARIANT -> {
                    val variantCacheId = data.getStringExtra(EXTRA_VARIANT_PICKER_RESULT_CACHE_ID)
                    val cacheManager = SaveInstanceCacheManager(context!!, variantCacheId)
                    if (data.hasExtra(EXTRA_PRODUCT_VARIANT_SELECTION)) {
                        val productVariantViewModel = cacheManager.get(EXTRA_PRODUCT_VARIANT_SELECTION,
                                object : TypeToken<ProductVariantInputModel>() {}.type) ?: ProductVariantInputModel()
                        descriptionViewModel.variantInputModel.variantOptionParent = productVariantViewModel.variantOptionParent
                        descriptionViewModel.variantInputModel.productVariant = productVariantViewModel.productVariant
                    }
                    if (data.hasExtra(EXTRA_PRODUCT_SIZECHART)) {
                        val productPictureViewModel = cacheManager.get(EXTRA_PRODUCT_SIZECHART,
                                object : TypeToken<PictureViewModel>() {}.type, PictureViewModel())
                        descriptionViewModel.variantInputModel.productSizeChart = productPictureViewModel
                    }
                }
            }
        }
    }

    private fun showDescriptionTips() {
        if (!descriptionViewModel.isEditMode) {
            ProductAddDescriptionTracking.clickHelpWriteDescription(shopId)
        }
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<NumericTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_description_tips)
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_description_tips_1)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_description_tips_2)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_description_tips_3)))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                show(it, null)
            }
        }
    }

    private fun showVariantTips() {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickHelpVariant(shopId)
        } else {
            ProductAddDescriptionTracking.clickHelpVariant(shopId)
        }
        fragmentManager?.let {
            val tooltipBottomSheet = TooltipBottomSheet()
            val tips: ArrayList<NumericTooltipModel> = ArrayList()
            val tooltipTitle = getString(R.string.title_tooltip_variant_tips)
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_1)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_2)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_3)))
            tips.add(NumericTooltipModel(getString(R.string.message_tooltip_variant_tips_4)))

            tooltipBottomSheet.apply {
                setTitle(tooltipTitle)
                setItemMenuList(tips)
                show(it, null)
            }
        }
    }

    override fun loadData(page: Int) {
        val videoLinkModels: ArrayList<VideoLinkModel> = ArrayList()
        videoLinkModels.add(VideoLinkModel(page, "", TEST_IMAGE_URL))
        super.renderList(videoLinkModels)

        textViewAddVideo.visibility =
                if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
    }

    private fun showVariantDialog(variants: List<ProductVariantByCatModel>) {
        activity?.let {
            val cacheManager = SaveInstanceCacheManager(it, true).apply {
                put(EXTRA_PRODUCT_VARIANT_BY_CATEGORY_LIST, variants)
                put(EXTRA_PRODUCT_VARIANT_SELECTION, descriptionViewModel.variantInputModel)
                put(EXTRA_PRODUCT_SIZECHART, descriptionViewModel.variantInputModel.productSizeChart)
                put(EXTRA_CURRENCY_TYPE, TYPE_IDR)
                put(EXTRA_DEFAULT_PRICE, 0.0) //TODO faisalramd put default price
                put(EXTRA_STOCK_TYPE, "")
                put(EXTRA_IS_OFFICIAL_STORE, false)
                put(EXTRA_DEFAULT_SKU, "")
                put(EXTRA_NEED_RETAIN_IMAGE, false)
                put(EXTRA_HAS_ORIGINAL_VARIANT_LV1, true)
                put(EXTRA_HAS_ORIGINAL_VARIANT_LV2, false)
                put(EXTRA_HAS_WHOLESALE, false)
                put(EXTRA_IS_ADD, IS_ADD)
            }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_VARIANT_DASHBOARD)
            intent?.run {
                putExtra(EXTRA_VARIANT_RESULT_CACHE_ID, cacheManager.id)
                putExtra(EXTRA_IS_USING_CACHE_MANAGER, true)
                startActivityForResult(this, REQUEST_CODE_VARIANT)
            }
        }
    }

    private fun moveToDescriptionActivity() {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickContinue(shopId)
        } else {
            ProductAddDescriptionTracking.clickContinue(shopId)
        }
        val intent = Intent(context, AddEditProductShipmentActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SHIPMENT)
    }

    private fun submitInput(shipmentInputModel: ShipmentInputModel) {
        val descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                adapter.data
        )
        val intent = Intent()
        intent.putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
        intent.putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
        intent.putExtra(EXTRA_VARIANT_INPUT, descriptionViewModel.variantInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun submitInputEdit() {
        val descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                adapter.data
        )
        val intent = Intent()
        intent.putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
        intent.putExtra(EXTRA_VARIANT_INPUT, descriptionViewModel.variantInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

}
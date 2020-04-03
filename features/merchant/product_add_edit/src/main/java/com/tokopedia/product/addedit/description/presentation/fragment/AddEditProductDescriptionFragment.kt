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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
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
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.add_edit_product_description_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_video_input_layout.*
import kotlinx.android.synthetic.main.fragment_add_edit_product_description.*
import javax.inject.Inject

class AddEditProductDescriptionFragment(
        private val categoryId: String
) : BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(), VideoLinkTypeFactory.VideoLinkListener {

    companion object {
        fun createInstance(categoryId: String): Fragment {
            return AddEditProductDescriptionFragment(categoryId)
        }

        const val MAX_VIDEOS = 3
        const val REQUEST_CODE_VARIANT = 0

        const val TYPE_IDR = 1
        const val TYPE_USD = 2

        const val IS_ADD = 0
        const val REQUEST_CODE_DESCRIPTION = 0x03

        // TODO faisalramd
        const val TEST_IMAGE_URL = "https://ecs7.tokopedia.net/img/cache/700/product-1/2018/9/16/36162992/36162992_778e5d1e-06fd-4e4a-b650-50c232815b24_1080_1080.jpg"
    }


    private var videoId = 0
    private var productVariantInputModel = ProductVariantInputModel()
    private lateinit var descriptionViewModel: AddEditProductDescriptionViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getAdapterTypeFactory(): VideoLinkTypeFactory {
        val videoLinkTypeFactory = VideoLinkTypeFactory()
        videoLinkTypeFactory.setVideoLinkListener(this)

        return videoLinkTypeFactory
    }

    override fun onDeleteClicked(videoLinkModel: VideoLinkModel, position: Int) {
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
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFieldDescription.textFieldInput.setSingleLine(false)
        textFieldDescription.textFieldInput.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION

        textViewAddVideo.setOnClickListener {
            videoId += 1
            loadData(videoId)
        }

        layoutDescriptionTips.setOnClickListener {
            showDescriptionTips()
        }

        layoutVariantTips.setOnClickListener {
            showVariantTips()
        }

        tvAddVariant.setOnClickListener {
            descriptionViewModel.getVariants(categoryId)
        }

        btnNext.setOnClickListener {
            moveToDescriptionActivity()
        }

        descriptionViewModel.productVariant.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> showVariantDialog(result.data)
                is Fail -> showVariantErrorToast(getString(R.string.title_tooltip_description_tips))
            }
        })
    }

    private fun showVariantErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type =  Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    clickListener =  View.OnClickListener {
                descriptionViewModel.getVariants(categoryId)
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
                        productVariantInputModel.variantOptionParent = productVariantViewModel.variantOptionParent
                        productVariantInputModel.productVariant = productVariantViewModel.productVariant
                    }
                    if (data.hasExtra(EXTRA_PRODUCT_SIZECHART)) {
                        val productPictureViewModel = cacheManager.get(EXTRA_PRODUCT_SIZECHART,
                                object : TypeToken<PictureViewModel>() {}.type, PictureViewModel())
                        productVariantInputModel.productSizeChart = productPictureViewModel
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        activity?.run {
            descriptionViewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(AddEditProductDescriptionViewModel::class.java)
        }
    }

    private fun showDescriptionTips() {
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
                put(EXTRA_PRODUCT_VARIANT_SELECTION, productVariantInputModel)
                put(EXTRA_PRODUCT_SIZECHART, productVariantInputModel.productSizeChart)
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
        intent.putExtra(EXTRA_VARIANT_INPUT, productVariantInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

}

package com.tokopedia.product.addedit.description.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
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
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_IS_ADD_MODE
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_IS_EDIT_MODE
import com.tokopedia.product.addedit.description.presentation.activity.AddEditProductDescriptionActivity.Companion.PARAM_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.PictureViewModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeVideoModel
import com.tokopedia.product.addedit.description.presentation.viewmodel.AddEditProductDescriptionViewModel
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
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
        fun createInstance(productInputModel: ProductInputModel): Fragment {
            return AddEditProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_PRODUCT_INPUT_MODEL, productInputModel)
                }
            }
        }
        fun createInstance(productInputModel: ProductInputModel,
                           isEditMode: Boolean,
                           isAddMode: Boolean): Fragment {
            return AddEditProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_PRODUCT_INPUT_MODEL, productInputModel)
                    putBoolean(PARAM_IS_EDIT_MODE, isEditMode)
                    putBoolean(PARAM_IS_ADD_MODE, isAddMode)
                }
            }
        }

        const val MAX_VIDEOS = 3
        const val MAX_DESCRIPTION_CHAR = 2000
        const val REQUEST_CODE_VARIANT = 0
        const val TYPE_IDR = 1
        const val IS_ADD = 0
        const val REQUEST_CODE_DESCRIPTION = 0x03
        const val VIDEO_REQUEST_DELAY = 250L
    }

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
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
            ProductEditDescriptionTracking.clickRemoveVideoLink(shopId)
        } else {
            ProductAddDescriptionTracking.clickRemoveVideoLink(shopId)
        }
        adapter.data.removeAt(position)
        adapter.notifyItemRemoved(position)
        with(descriptionViewModel) {
            for (i in position until adapter.dataSize) {
                adapter.data.getOrNull(i)?.run {
                    isFetchingVideoData[i] = false
                    urlToFetch[i] = inputUrl
                    if (fetchedUrl[i + 1].orEmpty() != inputUrl) {
                        onTextChanged(urlToFetch[i].orEmpty(), i)
                    }
                }
            }
        }
        textViewAddVideo.visibility =
                if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
    }

    override fun onTextChanged(url: String, position: Int) {
        adapter.data.getOrNull(position)?.run {
            inputUrl = url
            descriptionViewModel.urlToFetch[position] = url
            if (inputImage.isNotEmpty() || inputTitle.isNotEmpty() || inputDescription.isNotEmpty()) {
                inputImage = ""
                inputTitle = ""
                inputDescription = ""
                getRecyclerView(view).post { adapter.notifyItemChanged(position) }
            }
            getVideoYoutube(url, position)
        }
    }

    override fun onThumbnailClicked(url: String) {
        try {
            val uri = if (url.startsWith(AddEditProductConstants.HTTP_PREFIX)) {
                Uri.parse(url)
            } else {
                Uri.parse("${AddEditProductConstants.HTTP_PREFIX}://${url}")
            }
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
                ProductEditDescriptionTracking.clickPlayVideo(shopId)
            }
        } catch (e: Throwable) {
        }
    }

    override fun onItemClicked(t: VideoLinkModel?) { /* noop */ }

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
        arguments?.let {
            val isEditMode: Boolean = it.getBoolean(PARAM_IS_EDIT_MODE, false)
            val isAddMode: Boolean = it.getBoolean(PARAM_IS_ADD_MODE, false)
            descriptionViewModel.isEditMode = isEditMode
            descriptionViewModel.isAddMode = isAddMode
            descriptionViewModel.productInputModel =
                    it.getParcelable(PARAM_PRODUCT_INPUT_MODEL) ?: ProductInputModel()

        }
        if (descriptionViewModel.isAddMode || !descriptionViewModel.isEditMode) {
            ProductAddDescriptionTracking.trackScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_description, container, false)
    }

    override fun onResume() {
        super.onResume()
        btnNext.isLoading = false
        btnSave.isLoading = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textFieldDescription.setCounter(MAX_DESCRIPTION_CHAR)
        textFieldDescription.textFieldInput.apply {
            setSingleLine(false)
            imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            afterTextChanged {
                if (it.length >= MAX_DESCRIPTION_CHAR) {
                    textFieldDescription.setMessage(getString(R.string.error_description_character_limit))
                    textFieldDescription.setError(true)
                } else {
                    textFieldDescription.setMessage("")
                    textFieldDescription.setError(false)
                }
            }
        }

        if (descriptionViewModel.isEditMode) applyEditMode()

        textViewAddVideo.setOnClickListener {
            if (getFilteredValidVideoLink().size == adapter.dataSize) {
                if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
                    ProductEditDescriptionTracking.clickAddVideoLink(shopId)
                } else {
                    ProductAddDescriptionTracking.clickAddVideoLink(shopId)
                }
                addEmptyVideoUrl()
            }
        }

        layoutDescriptionTips.setOnClickListener {
            showDescriptionTips()
        }

        layoutVariantTips.setOnClickListener {
            showVariantTips()
        }

        tvAddVariant.setOnClickListener {
            if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
                ProductEditDescriptionTracking.clickAddProductVariant(shopId)
            } else {
                ProductAddDescriptionTracking.clickAddProductVariant(shopId)
            }
            descriptionViewModel.productVariantData?.let {
                showVariantDialog(it)
            }
        }

        btnNext.setOnClickListener {
            btnNext.isLoading = true
            moveToShipmentActivity()
        }

        btnSave.setOnClickListener {
            btnSave.isLoading = true
            submitInputEdit()
        }

        getRecyclerView(view).itemAnimator = object: DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }

        descriptionViewModel.getVariants(descriptionViewModel.categoryId)

        hideKeyboardWhenTouchOutside()
        observeProductVariant()
        observeProductVideo()
    }

    private fun addEmptyVideoUrl() {
        val lastData = adapter.data.lastOrNull()
        if (lastData == null) {
            loadData(adapter.dataSize + 1)
        } else {
            if (lastData.inputUrl.isNotEmpty()) {
                loadData(adapter.dataSize + 1)
            }
        }
    }

    override fun loadInitialData() {
        loadData(1)
    }

    private fun getVideoYoutube(url: String, index: Int) {
        if (!(descriptionViewModel.isFetchingVideoData[index] ?: false)) {
            descriptionViewModel.isFetchingVideoData[index] = true
            view?.postDelayed({
                if (descriptionViewModel.urlToFetch[index] == url) {
                    descriptionViewModel.fetchedUrl[index] = url
                    descriptionViewModel.getVideoYoutube(descriptionViewModel.fetchedUrl[index].orEmpty(), index)
                } else {
                    descriptionViewModel.isFetchingVideoData[index] = false
                    getVideoYoutube(descriptionViewModel.urlToFetch[index].orEmpty(), index)
                }
            }, VIDEO_REQUEST_DELAY)
        }
    }

    fun sendDataBack() {
        if(!descriptionViewModel.isEditMode) {
            inputAllDataInInputDraftModel()
            val intent = Intent()
            intent.putExtra(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel)
            intent.putExtra(EXTRA_BACK_PRESSED, 2)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            activity?.finish()
        }
    }

    fun onBackPressed() {
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
            ProductEditDescriptionTracking.clickBack(shopId)
        } else {
            ProductAddDescriptionTracking.clickBack(shopId)
        }
    }

    private fun inputAllDataInInputDraftModel() {
        descriptionViewModel.productInputModel.descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                getFilteredValidVideoLink()
        )
    }

    private fun observeProductVariant() {
        tvAddVariant.isEnabled = false
        descriptionViewModel.productVariant.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> tvAddVariant.isEnabled = true
                is Fail -> showVariantErrorToast(getString(R.string.default_request_error_timeout))
            }
        })
    }

    private fun observeProductVideo() {
        descriptionViewModel.videoYoutube.observe(viewLifecycleOwner, Observer { result ->
            val position = result.first
            val isItemStillTheSame: Boolean
            descriptionViewModel.isFetchingVideoData[position] = false
            isItemStillTheSame = when (val requestResult = result.second) {
                is Success -> {
                    val id = requestResult.data.id
                    if (id == null) {
                        displayErrorOnSelectedVideo(position)
                    } else {
                        setDataOnSelectedVideo(requestResult.data, position)
                    }
                }
                is Fail -> {
                    displayErrorOnSelectedVideo(position)
                }
            }
            adapter.notifyItemChanged(position)
            if (isItemStillTheSame && descriptionViewModel.fetchedUrl[position] != descriptionViewModel.urlToFetch[position]) {
                getVideoYoutube(descriptionViewModel.urlToFetch[position].orEmpty(), position)
            }
            refreshDuplicateVideo(position)
        })
    }

    private fun displayErrorOnSelectedVideo(index: Int): Boolean {
        var isItemTheSame = false
        adapter.data.getOrNull(index)?.apply {
            if (descriptionViewModel.fetchedUrl[index] == inputUrl) {
                inputTitle = ""
                inputDescription = ""
                inputImage = ""
                errorMessage = if (inputUrl.isBlank()) "" else getString(R.string.error_video_not_valid)
                isItemTheSame = true
            }
        }

        return isItemTheSame
    }

    private fun setDataOnSelectedVideo(youtubeVideoModel: YoutubeVideoModel, index: Int): Boolean {
        var isItemTheSame = false
        adapter.data.getOrNull(index)?.apply {
            if (descriptionViewModel.fetchedUrl[index] == inputUrl) {
                inputTitle = youtubeVideoModel.title.orEmpty()
                inputDescription = youtubeVideoModel.description.orEmpty()
                inputImage = youtubeVideoModel.thumbnailUrl.orEmpty()
                errorMessage = descriptionViewModel.validateDuplicateVideo(adapter.data, inputUrl)
                isItemTheSame = true
            }
        }

        return isItemTheSame
    }

    private fun refreshDuplicateVideo(excludeIndex: Int) {
        ResourceProvider(context).getDuplicateProductVideoErrorMessage()?.run {
            adapter.data.forEachIndexed { index, video ->
                if (index != excludeIndex && video.errorMessage == this) {
                    getVideoYoutube(video.inputUrl, index)
                }
            }
        }
    }

    private fun applyEditMode() {
        val description = descriptionViewModel.descriptionInputModel.productDescription
        val videoLinks = descriptionViewModel.descriptionInputModel.videoLinkList

        textFieldDescription.setText(description)
        if (videoLinks.isNotEmpty()) {
            super.clearAllData()
            super.renderList(videoLinks)
        }

        tvVariantHeaderSubtitle.text = descriptionViewModel.getVariantSelectedMessage()
        tvAddVariant.text = descriptionViewModel.getVariantButtonMessage()
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
                    if(data.getIntExtra(EXTRA_BACK_PRESSED, 0) != 0) {
                        activity?.setResult(Activity.RESULT_OK, data)
                        activity?.finish()
                        return
                    }
                    val shipmentInputModel =
                            data.getParcelableExtra<ShipmentInputModel>(EXTRA_SHIPMENT_INPUT)
                    submitInput(shipmentInputModel)
                }
                REQUEST_CODE_VARIANT -> {
                    val variantCacheId = data.getStringExtra(EXTRA_VARIANT_PICKER_RESULT_CACHE_ID)
                    val cacheManager = SaveInstanceCacheManager(context!!, variantCacheId)
                    val productPictureViewModel = if (data.hasExtra(EXTRA_PRODUCT_SIZECHART)) {
                        cacheManager.get(EXTRA_PRODUCT_SIZECHART,
                                object : TypeToken<PictureViewModel>() {}.type, PictureViewModel())
                    } else null
                    if (data.hasExtra(EXTRA_PRODUCT_VARIANT_SELECTION)) {
                        val productVariantViewModel = cacheManager.get(EXTRA_PRODUCT_VARIANT_SELECTION,
                                object : TypeToken<ProductVariantInputModel>() {}.type) ?: ProductVariantInputModel()
                        descriptionViewModel.setVariantInput(productVariantViewModel.productVariant,
                                productVariantViewModel.variantOptionParent, productPictureViewModel)
                        tvVariantHeaderSubtitle.text = descriptionViewModel.getVariantSelectedMessage()
                        tvAddVariant.text = descriptionViewModel.getVariantButtonMessage()
                    }
                }
            }
        }
    }

    private fun showDescriptionTips() {
        if (!descriptionViewModel.isEditMode || descriptionViewModel.isAddMode) {
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
                setDividerVisible(false)
                show(it, null)
            }
        }
    }

    private fun showVariantTips() {
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
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
                setDividerVisible(false)
                show(it, null)
            }
        }
    }

    override fun loadData(page: Int) {
        val videoLinkModels: ArrayList<VideoLinkModel> = ArrayList()
        videoLinkModels.add(VideoLinkModel())
        super.renderList(videoLinkModels)
        descriptionViewModel.isFetchingVideoData[adapter.dataSize - 1] = false
        descriptionViewModel.urlToFetch[adapter.dataSize - 1] = ""
        descriptionViewModel.fetchedUrl[adapter.dataSize - 1] = ""

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
                put(EXTRA_DEFAULT_PRICE, descriptionViewModel.productInputModel.detailInputModel.price)
                put(EXTRA_STOCK_TYPE, descriptionViewModel.getStatusStockViewVariant())
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

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardWhenTouchOutside() {
        mainLayout?.setOnTouchListener{ _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            true
        }
    }

    private fun moveToShipmentActivity() {
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
            ProductEditDescriptionTracking.clickContinue(shopId)
        } else {
            ProductAddDescriptionTracking.clickContinue(shopId)
        }
        inputAllDataInInputDraftModel()
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            val intent = Intent(context, AddEditProductShipmentActivity::class.java)
            intent.putExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel)
            startActivityForResult(intent, REQUEST_CODE_SHIPMENT)
        }
    }

    private fun submitInput(shipmentInputModel: ShipmentInputModel) {
        val descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                getFilteredValidVideoLink()
        )
        val intent = Intent()
        intent.putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
        intent.putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
        intent.putExtra(EXTRA_VARIANT_INPUT, descriptionViewModel.variantInputModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun submitInputEdit() {
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
            ProductEditDescriptionTracking.clickContinue(shopId)
        } else {
            ProductAddDescriptionTracking.clickContinue(shopId)
        }
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            val descriptionInputModel = DescriptionInputModel(
                    textFieldDescription.getText(),
                    getFilteredValidVideoLink()
            )
            val intent = Intent()
            intent.putExtra(EXTRA_DESCRIPTION_INPUT, descriptionInputModel)
            intent.putExtra(EXTRA_VARIANT_INPUT, descriptionViewModel.variantInputModel)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun getFilteredValidVideoLink() = adapter.data.filter {
        it.inputUrl.isNotBlank() && it.errorMessage.isBlank()
    }
}

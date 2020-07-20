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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_CURRENCY_TYPE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DEFAULT_PRICE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_DEFAULT_SKU
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
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_STOCK_TYPE
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_PICKER_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_VARIANT_RESULT_CACHE_ID
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductPicture
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.description.presentation.viewmodel.AddEditProductDescriptionViewModel
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DIALOG_EDIT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.activity.AddEditProductShipmentActivity
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment.Companion.REQUEST_CODE_SHIPMENT
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.ProductAddDescriptionTracking
import com.tokopedia.product.addedit.tracking.ProductEditDescriptionTracking
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantActivity
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import kotlinx.android.synthetic.main.add_edit_product_description_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_video_input_layout.*
import kotlinx.android.synthetic.main.fragment_add_edit_product_description.*
import javax.inject.Inject

class AddEditProductDescriptionFragment:
        BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(),
        VideoLinkTypeFactory.VideoLinkListener {

    companion object {
        const val MAX_VIDEOS = 3
        const val MAX_DESCRIPTION_CHAR = 2000
        const val REQUEST_CODE_DESCRIPTION = 0x03
        const val VIDEO_REQUEST_DELAY = 250L

        fun createInstance(cacheManagerId: String): Fragment {
            return AddEditProductDescriptionFragment().apply {
                arguments = Bundle().apply {
                    putString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
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
        // you must compare isEditMode and isAddMode to obtain actual editing status
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
        updateSaveButtonStatus()
        refreshSubmitButton()
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
            // you must compare isEditMode and isAddMode to obtain actual editing status
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

        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {
            val productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
            descriptionViewModel.isEditMode = saveInstanceCacheManager.get(AddEditProductPreviewConstants.EXTRA_IS_EDITING_PRODUCT, Boolean::class.java, false) ?: false
            descriptionViewModel.isAddMode = saveInstanceCacheManager.get(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, Boolean::class.java, false) ?: false
            descriptionViewModel.updateProductInputModel(productInputModel)
        }
        // you must compare isEditMode and isAddMode to obtain actual adding status
        if (descriptionViewModel.isAddMode || !descriptionViewModel.isEditMode) {
            ProductAddDescriptionTracking.trackScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.product.addedit.R.layout.fragment_add_edit_product_description, container, false)
    }

    override fun onResume() {
        super.onResume()
        btnNext.isLoading = false
        btnSave.isLoading = false
    }

    override fun getRecyclerViewResourceId(): Int {
        return com.tokopedia.product.addedit.R.id.recycler_view
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

        textViewAddVideo.setOnClickListener {
            if (getFilteredValidVideoLink().size == adapter.dataSize) {
                // you must compare isEditMode and isAddMode to obtain actual editing status
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
            // you must compare isEditMode and isAddMode to obtain actual editing status
            if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
                ProductEditDescriptionTracking.clickAddProductVariant(shopId)
            } else {
                ProductAddDescriptionTracking.clickAddProductVariant(shopId)
            }
            showVariantDialog()
        }

        tvEditVariant.setOnClickListener {
            showVariantDialog()
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

        if (descriptionViewModel.isEditMode) applyEditMode()

        hideKeyboardWhenTouchOutside()
        observeProductInputModel()
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
            val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel.value)

            val intent = Intent()
            intent.putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            intent.putExtra(EXTRA_BACK_PRESSED, 2)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            activity?.finish()
        }
    }

    fun onBackPressed() {
        // you must compare isEditMode and isAddMode to obtain actual editing status
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
            ProductEditDescriptionTracking.clickBack(shopId)
        } else {
            ProductAddDescriptionTracking.clickBack(shopId)
        }
    }

    private fun inputAllDataInInputDraftModel() {
        descriptionViewModel.productInputModel.value?.descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                getFilteredValidVideoLink()
        )
    }

    private fun observeProductInputModel() {
        descriptionViewModel.productInputModel.observe(this, Observer {
            updateVariantLayout()
        })
    }

    private fun updateVariantLayout() {
        if (descriptionViewModel.hasVariant) {
            tvEditVariant.visible()
            tvAddVariant.gone()
            layoutVariantTips.gone()
        } else {
            tvEditVariant.gone()
            tvAddVariant.visible()
            layoutVariantTips.visible()
        }
        tvVariantHeaderSubtitle.text = descriptionViewModel.getVariantSelectedMessage()
        tvVariantLevel1Type.setTextOrGone(descriptionViewModel
                .getVariantTypeMessage(VARIANT_VALUE_LEVEL_ONE_POSITION))
        tvVariantLevel2Type.setTextOrGone(descriptionViewModel
                .getVariantTypeMessage(VARIANT_VALUE_LEVEL_TWO_POSITION))
        tvVariantLevel1Count.setTextOrGone(descriptionViewModel
                .getVariantCountMessage(VARIANT_VALUE_LEVEL_ONE_POSITION))
        tvVariantLevel2Count.setTextOrGone(descriptionViewModel
                .getVariantCountMessage(VARIANT_VALUE_LEVEL_TWO_POSITION))
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
                    AddEditProductErrorHandler.logExceptionToCrashlytics(requestResult.throwable)
                    displayErrorOnSelectedVideo(position)
                }
            }
            adapter.notifyItemChanged(position)
            if (isItemStillTheSame && descriptionViewModel.fetchedUrl[position] != descriptionViewModel.urlToFetch[position]) {
                getVideoYoutube(descriptionViewModel.urlToFetch[position].orEmpty(), position)
            }
            refreshDuplicateVideo(position)
            refreshSubmitButton()
            updateSaveButtonStatus()
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

    private fun setDataOnSelectedVideo(youtubeVideoModel: YoutubeVideoDetailModel, index: Int): Boolean {
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

    // button will disabled if there is an video link error
    private fun refreshSubmitButton() {
        val enabled = adapter.data.all { video ->
            video.errorMessage.isEmpty()
        }

        btnSave.isEnabled = enabled
        btnNext.isEnabled = enabled
    }

    private fun applyEditMode() {
        val description = descriptionViewModel.descriptionInputModel.productDescription
        val videoLinks = descriptionViewModel.descriptionInputModel.videoLinkList

        textFieldDescription.setText(description)
        if (videoLinks.isNotEmpty()) {
            super.clearAllData()
            super.renderList(videoLinks)
        }

        updateVariantLayout()
        btnNext.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val cacheManagerId = data.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID) ?: ""
            when (requestCode) {
                REQUEST_CODE_SHIPMENT -> {
                    if(data.getIntExtra(EXTRA_BACK_PRESSED, 0) != 0) {
                        activity?.setResult(Activity.RESULT_OK, data)
                        activity?.finish()
                        return
                    }
                    submitInput(cacheManagerId)
                }
                REQUEST_CODE_VARIANT_DIALOG_EDIT -> {
                    SaveInstanceCacheManager(requireContext(), cacheManagerId).run {
                        val productInputModel = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                                        ?: ProductInputModel()
                        descriptionViewModel.updateProductInputModel(productInputModel)
                    }
                }
            }
        }
    }

    private fun showDescriptionTips() {
        // you must compare isEditMode and isAddMode to obtain actual adding status
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
        // you must compare isEditMode and isAddMode to obtain actual editing status
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

    private fun showVariantDialog() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel.value)
            }
            val intent = AddEditProductVariantActivity.createInstance(this, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_DIALOG_EDIT)
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
        // you must compare isEditMode and isAddMode to obtain actual editing status
        if (descriptionViewModel.isEditMode && !descriptionViewModel.isAddMode) {
            ProductEditDescriptionTracking.clickContinue(shopId)
        } else {
            ProductAddDescriptionTracking.clickContinue(shopId)
        }
        inputAllDataInInputDraftModel()
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel.value)
            val intent = Intent(context, AddEditProductShipmentActivity::class.java).apply { putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId) }
            startActivityForResult(intent, REQUEST_CODE_SHIPMENT)
        }
    }

    private fun submitInput(cacheManagerId: String) {
        val descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                getFilteredValidVideoLink()
        )

        SaveInstanceCacheManager(requireContext(), cacheManagerId).apply {
            val productInputModel = get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java, ProductInputModel())
            productInputModel?.descriptionInputModel = descriptionInputModel
            put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
        }

        val intent = Intent()
        intent.putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun submitInputEdit() {
        // you must compare isEditMode and isAddMode to obtain actual editing status
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

            descriptionViewModel.productInputModel.value?.descriptionInputModel = descriptionInputModel

            val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID) ?: ""
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel.value)

            val intent = Intent()
            intent.putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun getFilteredValidVideoLink() = adapter.data.filter {
        it.inputUrl.isNotBlank() && it.errorMessage.isBlank()
    }

    private fun updateSaveButtonStatus() {
        with (descriptionViewModel.validateInputVideo(adapter.data)) {
            btnSave.isEnabled = this
            btnNext.isEnabled = this
        }
    }
}

package com.tokopedia.product.addedit.description.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISADDING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISDRAFTING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISEDITING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISFIRSTMOVED
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.MAX_DESCRIPTION_CHAR
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.MAX_VIDEOS
import com.tokopedia.product.addedit.description.presentation.dialog.GiftingDescriptionBottomSheet
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.description.presentation.viewmodel.AddEditProductDescriptionViewModel
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DIALOG_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DESCRIPTION
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapJsonToObject
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper.mapObjectToJson
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DESCRIPTION_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DETAIL_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_FIRST_MOVED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.NO_DATA
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragmentArgs
import com.tokopedia.product.addedit.tooltip.model.NumericTooltipModel
import com.tokopedia.product.addedit.tooltip.presentation.TooltipBottomSheet
import com.tokopedia.product.addedit.tracking.ProductAddDescriptionTracking
import com.tokopedia.product.addedit.tracking.ProductEditDescriptionTracking
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantActivity
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import kotlinx.android.synthetic.main.add_edit_product_description_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_no_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_input_layout.*
import kotlinx.android.synthetic.main.add_edit_product_video_input_layout.*
import kotlinx.android.synthetic.main.fragment_add_edit_product_description.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AddEditProductDescriptionFragment:
        BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(),
        VideoLinkTypeFactory.VideoLinkListener,
        AddEditProductPerformanceMonitoringListener
{

    private lateinit var shopId: String
    private var isFragmentVisible = false
    private var youtubeAdapterPosition = 0
    private var isFirstTimeFetchYoutubeData = false

    // PLT Monitoring
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var descriptionViewModel: AddEditProductDescriptionViewModel

    override fun loadInitialData() {
        loadData(1)
    }

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
        adapter.notifyItemRemoved(position)
        textViewAddVideo.visibility = if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
        updateSaveButtonStatus()
    }

    override fun onTextChanged(url: String, position: Int) {
        getVideoYoutube(url)
        adapter.data.getOrNull(position)?.run {
            inputUrl = url
            youtubeAdapterPosition = position
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
            if (descriptionViewModel.isEditMode) {
                ProductEditDescriptionTracking.clickPlayVideo(shopId)
            }
        } catch (e: Throwable) {
        }
    }

    override fun onItemClicked(t: VideoLinkModel?) { /* noop */ }

    override fun getScreenName(): String? = null

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun initInjector() {
        DaggerAddEditProductDescriptionComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductDescriptionModule(AddEditProductDescriptionModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // PLT Monitoring
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId
        isFirstTimeFetchYoutubeData = true

        arguments?.let {
            val cacheManagerId = AddEditProductDescriptionFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

            cacheManagerId.run {
                val productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
                descriptionViewModel.updateProductInputModel(productInputModel)
                descriptionViewModel.isEditMode = saveInstanceCacheManager.get(EXTRA_IS_EDITING_PRODUCT, Boolean::class.java, false) ?: false
                descriptionViewModel.isAddMode = saveInstanceCacheManager.get(EXTRA_IS_ADDING_PRODUCT, Boolean::class.java, false) ?: false
                descriptionViewModel.isDraftMode = saveInstanceCacheManager.get(EXTRA_IS_DRAFTING_PRODUCT, Boolean::class.java) ?: false
                descriptionViewModel.isFirstMoved = saveInstanceCacheManager.get(EXTRA_IS_FIRST_MOVED, Boolean::class.java) ?: false
            }
            if (descriptionViewModel.isAddMode) {
                ProductAddDescriptionTracking.trackScreen()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        requireActivity().window.decorView.setBackgroundColor(ContextCompat.getColor(
                requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))

        // to check whether current fragment is visible or not
        isFragmentVisible = true

        setupDescriptionLayout()
        setupVideoListLayout()
        setupVariantLayout()
        setupSubmitButton()
        setupSellerMigrationLayout()

        if (!(descriptionViewModel.isAddMode && descriptionViewModel.isFirstMoved)) applyEditMode()

        onFragmentResult()
        setupOnBackPressed()
        hideKeyboardWhenTouchOutside()

        observeProductInputModel()
        observeDescriptionValidation()
        observeProductVideo()
        observeIsHampersProduct()

        // PLT Monitoring
        stopPreparePagePerformanceMonitoring()
    }

    override fun onResume() {
        super.onResume()
        btnNext.isLoading = false
        btnSave.isLoading = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isFragmentVisible) {
            inputAllDataInInputModel()
            outState.putString(KEY_SAVE_INSTANCE_INPUT_MODEL, mapObjectToJson(descriptionViewModel.productInputModel.value))
            outState.putBoolean(KEY_SAVE_INSTANCE_ISADDING, descriptionViewModel.isAddMode)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISEDITING, descriptionViewModel.isEditMode)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISDRAFTING, descriptionViewModel.isDraftMode)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED, descriptionViewModel.isFirstMoved)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val productInputModelJson = savedInstanceState.getString(KEY_SAVE_INSTANCE_INPUT_MODEL)
            descriptionViewModel.isAddMode = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISADDING)
            descriptionViewModel.isEditMode = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISEDITING)
            descriptionViewModel.isDraftMode = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISDRAFTING)
            descriptionViewModel.isFirstMoved = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED)

            if (!productInputModelJson.isNullOrBlank()) {
                //set product input model and and ui of the page
                mapJsonToObject(productInputModelJson, ProductInputModel::class.java).apply {
                    descriptionViewModel.updateProductInputModel(this)
                    if (!(descriptionViewModel.isAddMode && descriptionViewModel.isFirstMoved)) {
                        applyEditMode()
                    } else {
                        btnNext.visibility = View.VISIBLE
                        btnSave.visibility = View.GONE
                    }
                }
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentVisible = false
        removeObservers()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DESCRIPTION_PLT_PREPARE_METRICS,
                AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DESCRIPTION_PLT_NETWORK_METRICS,
                AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DESCRIPTION_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_DESCRIPTION_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
        getRecyclerView(view)?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                stopRenderPerformanceMonitoring()
                stopPerformanceMonitoring()
                getRecyclerView(view)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun setupSellerMigrationLayout() {
        with (GlobalConfig.isSellerApp()) {
            containerAddEditDescriptionFragmentNoInputVariant.showWithCondition(!this)
            containerAddEditDescriptionFragmentInputVariant.showWithCondition(this)
            tvNoVariantDescription.text = getString(com.tokopedia.seller_migration_common.R.string.seller_migration_add_edit_no_variant_description).parseAsHtml()
        }
    }

    private fun setupVideoListLayout() {
        textViewAddVideo.setOnClickListener {
            if (getFilteredValidVideoLink().size == adapter.dataSize) {
                if (descriptionViewModel.isEditMode) {
                    ProductEditDescriptionTracking.clickAddVideoLink(shopId)
                } else {
                    ProductAddDescriptionTracking.clickAddVideoLink(shopId)
                }
                adapter.data.add(VideoLinkModel())
                adapter.notifyDataSetChanged()
                textViewAddVideo.visibility = if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
            }
        }

        getRecyclerView(view)?.itemAnimator = object: DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
    }

    private fun setupSubmitButton() {
        btnNext.setOnClickListener {
            btnNext.isLoading = true
            moveToShipmentActivity()
        }

        btnSave.setOnClickListener {
            btnSave.isLoading = true
            val isAdding = descriptionViewModel.isAddMode
            val isDrafting = descriptionViewModel.isDraftMode
            if (isAdding && !isDrafting) {
                submitInput()
            } else {
                submitInputEdit()
            }
        }
    }

    private fun setupVariantLayout() {
        layoutVariantTips.setOnClickListener {
            showVariantTips()
        }

        tvAddVariant.setOnClickListener {
            sendClickAddProductVariant()
            showVariantDialog()
        }

        tvEditVariant.setOnClickListener {
            sendClickAddProductVariant()
            showVariantDialog()
        }
    }

    private fun setupDescriptionLayout() {
        layoutDescriptionTips.setOnClickListener {
            showDescriptionTips()
        }

        textFieldDescription?.setCounter(MAX_DESCRIPTION_CHAR)
        textFieldDescription?.textFieldInput?.apply {
            isSingleLine = false
            imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            afterTextChanged {
                if (it.length >= MAX_DESCRIPTION_CHAR) {
                    textFieldDescription?.setMessage(getString(R.string.error_description_character_limit))
                    textFieldDescription?.setError(true)
                } else {
                    textFieldDescription?.setMessage("")
                    textFieldDescription?.setError(false)
                }
                validateDescriptionText(it)
            }
        }
    }

    private fun sendClickAddProductVariant() {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickAddProductVariant(shopId)
        } else {
            ProductAddDescriptionTracking.clickAddProductVariant(shopId)
        }
    }

    private fun removeObservers() {
        descriptionViewModel.productInputModel.removeObservers(this)
        descriptionViewModel.videoYoutube.removeObservers(this)
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.removeObservers(this)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun getVideoYoutube(url: String) {
        descriptionViewModel.urlYoutubeChanged(url)
    }

    private fun onFragmentResult() {
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.observe(viewLifecycleOwner, Observer { bundle ->
            setNavigationResult(bundle, REQUEST_KEY_ADD_MODE)
            removeNavigationResult(REQUEST_KEY_ADD_MODE)
            findNavController().navigateUp()
        })
    }

    private fun sendDataBack() {
        if(descriptionViewModel.isAddMode && !descriptionViewModel.isDraftMode) {
            var dataBackPressed = NO_DATA
            if(descriptionViewModel.isFirstMoved) {
                inputAllDataInInputModel()
                dataBackPressed = DESCRIPTION_DATA
                descriptionViewModel.productInputModel.value?.requestCode = arrayOf(DETAIL_DATA, DESCRIPTION_DATA, NO_DATA)
            }
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        } else {
            setFragmentResultWithBundle(REQUEST_KEY_DESCRIPTION)
        }
    }

    private fun inputAllDataInInputModel() {
        descriptionViewModel.productInputModel.value?.isDataChanged = true
        descriptionViewModel.productInputModel.value?.descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                getFilteredValidVideoLink()
        )
    }

    private fun observeProductInputModel() {
        descriptionViewModel.productInputModel.observe(viewLifecycleOwner) {
            updateVariantLayout()
        }
    }

    private fun observeDescriptionValidation() {
        descriptionViewModel.descriptionValidationMessage.observe(viewLifecycleOwner) {
            updateDescriptionFieldErrorMessage(it)
        }
    }

    private fun observeIsHampersProduct() {
        descriptionViewModel.isHampersProduct.observe(viewLifecycleOwner) { isHampers ->
            if (isHampers && RollenceUtil.getHampersRollence() && GlobalConfig.isSellerApp()) {
                layoutDescriptionTips.tvTipsText?.text = getString(R.string.label_gifting_description_tips)
                layoutDescriptionTips.setOnClickListener {
                    showGiftingDescription()
                }
            }
        }
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
        descriptionViewModel.videoYoutube.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val id = result.data.id
                    if (id == null) {
                        displayErrorOnSelectedVideo(youtubeAdapterPosition)
                    } else {
                        stopNetworkRequestPerformanceMonitoring()
                        startRenderPerformanceMonitoring()
                        setDataOnSelectedVideo(result.data, youtubeAdapterPosition)
                    }
                }
                is Fail -> {
                    displayErrorOnSelectedVideo(youtubeAdapterPosition)
                    AddEditProductErrorHandler.logExceptionToCrashlytics(result.throwable)
                }
            }
            adapter.notifyItemChanged(youtubeAdapterPosition)
            updateSaveButtonStatus()
            getVideoYoutubeOneByOne()
        }
    }

    private fun getVideoYoutubeOneByOne() {
        if (!(descriptionViewModel.isAddMode && descriptionViewModel.isFirstMoved) && isFirstTimeFetchYoutubeData) {
            youtubeAdapterPosition -= 1
            if (youtubeAdapterPosition != -1) {
                getVideoYoutube(adapter.data[youtubeAdapterPosition].inputUrl)
            } else {
                isFirstTimeFetchYoutubeData = false
            }
        }
    }

    private fun displayErrorOnSelectedVideo(position: Int) {
        adapter.data.getOrNull(position)?.apply {
            inputTitle = ""
            inputDescription = ""
            inputImage = ""
            errorMessage = if (inputUrl.isBlank()) "" else getString(R.string.error_video_not_valid)
        }
    }

    private fun setDataOnSelectedVideo(youtubeVideoModel: YoutubeVideoDetailModel, position: Int) {
        adapter.data.getOrNull(position)?.apply {
            inputTitle = youtubeVideoModel.title.orEmpty()
            inputDescription = youtubeVideoModel.description.orEmpty()
            inputImage = youtubeVideoModel.thumbnailUrl.orEmpty()
            errorMessage = descriptionViewModel.validateDuplicateVideo(adapter.data, inputUrl)
        }
    }

    private fun validateDescriptionText(it: String) {
        descriptionViewModel.validateDescriptionChanged(it)
    }

    private fun updateDescriptionFieldErrorMessage(message: String) {
        textFieldDescription?.setMessage(message)
        textFieldDescription?.setError(message.isNotEmpty())
        btnSave.isEnabled = message.isEmpty()
    }

    private fun applyEditMode() {
        val description = descriptionViewModel.descriptionInputModel?.productDescription ?: ""
        val videoLinks = descriptionViewModel.descriptionInputModel?.videoLinkList?.toMutableList()

        textFieldDescription?.setText(description)
        if (!videoLinks.isNullOrEmpty()) {
            super.clearAllData()
            super.renderList(videoLinks)
            textViewAddVideo.visibility = if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
            // start network monitoring when videoLinks is not empty
            startNetworkRequestPerformanceMonitoring()
        } else {
            // end all monitoring when videoLinks is empty
            stopPreparePagePerformanceMonitoring()
            stopPerformanceMonitoring()
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

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendDataBack()

                if (descriptionViewModel.isEditMode) {
                    ProductEditDescriptionTracking.clickBack(shopId)
                } else {
                    ProductAddDescriptionTracking.clickBack(shopId)
                }
            }
        })
    }

    private fun showDescriptionTips() {
        if (descriptionViewModel.isAddMode) {
            ProductAddDescriptionTracking.clickHelpWriteDescription(shopId)
        }

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
        }
        tooltipBottomSheet.show(childFragmentManager, null)
    }

    private fun showGiftingDescription() {
        val fragmentManager = childFragmentManager
        GiftingDescriptionBottomSheet().apply {
            setOnCopyTemplateButtonListener {
                copyDescriptionTemplate()
            }
            show(fragmentManager)
        }
    }

    private fun showVariantTips() {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickHelpVariant(shopId)
        } else {
            ProductAddDescriptionTracking.clickHelpVariant(shopId)
        }

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
        }
        tooltipBottomSheet.show(childFragmentManager, null)
    }

    override fun loadData(page: Int) {
        super.renderList(mutableListOf(VideoLinkModel()))
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
        if (descriptionViewModel.isAddMode) {
            ProductAddDescriptionTracking.clickContinue(shopId)
        }
        inputAllDataInInputModel()
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            arguments?.let {
                val cacheManagerId = AddEditProductDescriptionFragmentArgs.fromBundle(it).cacheManagerId
                SaveInstanceCacheManager(requireContext(), cacheManagerId).apply {
                    put(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel.value)
                    put(EXTRA_IS_EDITING_PRODUCT, descriptionViewModel.isEditMode)
                    put(EXTRA_IS_ADDING_PRODUCT, descriptionViewModel.isAddMode)
                    put(EXTRA_IS_DRAFTING_PRODUCT, descriptionViewModel.isDraftMode)
                    put(EXTRA_IS_FIRST_MOVED, descriptionViewModel.isFirstMoved)
                }
                val destination = AddEditProductDescriptionFragmentDirections.actionAddEditProductDescriptionFragmentToAddEditProductShipmentFragment()
                destination.cacheManagerId = cacheManagerId
                NavigationController.navigate(this@AddEditProductDescriptionFragment, destination)
            }
        }
    }

    private fun submitInput() {
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            inputAllDataInInputModel()
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE)
        }
    }

    private fun submitInputEdit() {
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            inputAllDataInInputModel()
            setFragmentResultWithBundle(REQUEST_KEY_DESCRIPTION)
        }
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickContinue(shopId)
        }
    }

    private fun setFragmentResultWithBundle(requestKey: String, dataBackPressed: Int = DESCRIPTION_DATA) {
        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, descriptionViewModel.productInputModel.value)

            val bundle = Bundle().apply {
                putString(BUNDLE_CACHE_MANAGER_ID, cacheManagerId)
                putInt(BUNDLE_BACK_PRESSED, dataBackPressed)
            }
            setNavigationResult(bundle,requestKey)
            findNavController().navigateUp()
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

    private fun copyDescriptionTemplate() {
        val template = getString(R.string.label_gifting_description_template)
        val clipboard = requireView().context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(template, template)
        clipboard.setPrimaryClip(clipData)
        Toaster.build(requireView(), getString(R.string.label_gifting_description_copied_message),
                Snackbar.LENGTH_LONG).show()
    }
}

package com.tokopedia.product.addedit.description.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.Observer
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductFragment
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISADDING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISDRAFTING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISEDITING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISFIRSTMOVED
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.common.util.StringValidationUtil.fromHtmlWithSpaceAndLinebreak
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
import com.tokopedia.product.addedit.description.domain.model.GetYoutubeVideoSnippetResponse
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.MAX_VIDEOS
import com.tokopedia.product.addedit.description.presentation.dialog.GiftingDescriptionBottomSheet
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.description.presentation.viewmodel.AddEditProductDescriptionViewModel
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_VARIANT_DIALOG_EDIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_DESCRIPTION
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
import com.tokopedia.product.addedit.tooltip.presentation.TooltipCardView
import com.tokopedia.product.addedit.tracking.ProductAddDescriptionTracking
import com.tokopedia.product.addedit.tracking.ProductEditDescriptionTracking
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantActivity
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import com.tokopedia.product.addedit.R as productaddeditR
import com.tokopedia.seller_migration_common.R as seller_migration_commonR

@FlowPreview
@ExperimentalCoroutinesApi
class AddEditProductDescriptionFragment :
    BaseListFragment<VideoLinkModel, VideoLinkTypeFactory>(),
    VideoLinkTypeFactory.VideoLinkListener,
    AddEditProductPerformanceMonitoringListener {

    private var mainLayout: ViewGroup? = null
    private var layoutVariantTips: ViewGroup? = null
    private var containerAddEditDescriptionFragmentNoInputVariant: ViewGroup? = null
    private var containerAddEditDescriptionFragmentInputVariant: ViewGroup? = null
    private var textViewAddVideo: Typography? = null
    private var textFieldDescription: TextFieldUnify2? = null
    private var tvNoVariantDescription: Typography? = null
    private var tvEditVariant: Typography? = null
    private var tvAddVariant: Typography? = null
    private var tvVariantHeaderSubtitle: Typography? = null
    private var tvVariantLevel1Count: Typography? = null
    private var tvVariantLevel2Count: Typography? = null
    private var tvVariantLevel1Type: Typography? = null
    private var tvVariantLevel2Type: Typography? = null
    private var layoutDescriptionTips: TooltipCardView? = null
    private var btnNext: UnifyButton? = null
    private var btnSave: UnifyButton? = null

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
        adapter.data.getOrNull(position)?.run {
            adapter.data.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        textViewAddVideo?.visibility = if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
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
                Uri.parse("${AddEditProductConstants.HTTP_PREFIX}://$url")
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
        // handle issue: The specified child already has a parent. You must call removeView() on the child's parent first.
        if (view != null) {
            val parent = view?.parent as ViewGroup
            parent.removeView(view)
        }

        return try {
            inflater.inflate(R.layout.fragment_add_edit_product_description, container, false)
        } catch (e: InflateException) {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()

        // to check whether current fragment is visible or not
        isFragmentVisible = true

        // setup views and behavior
        setupViews(view)
        setupDescriptionLayout()
        setupVideoListLayout()
        setupVariantLayout()
        setupSubmitButton()
        setupSellerMigrationLayout()
        if (!(descriptionViewModel.isAddMode && descriptionViewModel.isFirstMoved)) applyEditMode()
        onFragmentResult()
        setupOnBackPressed()
        hideKeyboardWhenTouchOutside()
        highlightNavigationButton()

        // setup observer
        observeProductInputModel()
        observeDescriptionValidation()
        observeProductVideo()
        observeIsHampersProduct()
        observeHasDTStock()
        observeIsRemovingSingleVariant()

        // PLT Monitoring
        stopPreparePagePerformanceMonitoring()
    }

    private fun setupViews(view: View) {
        mainLayout = view.findViewById(R.id.mainLayout)
        layoutVariantTips = view.findViewById(R.id.layoutVariantTips)
        containerAddEditDescriptionFragmentNoInputVariant = view.findViewById(R.id.containerAddEditDescriptionFragmentNoInputVariant)
        containerAddEditDescriptionFragmentInputVariant = view.findViewById(R.id.containerAddEditDescriptionFragmentInputVariant)
        textViewAddVideo = view.findViewById(R.id.textViewAddVideo)
        textFieldDescription = view.findViewById(R.id.textFieldDescription)
        tvNoVariantDescription = view.findViewById(R.id.tvNoVariantDescription)
        tvEditVariant = view.findViewById(R.id.tvEditVariant)
        tvAddVariant = view.findViewById(R.id.tvAddVariant)
        tvVariantHeaderSubtitle = view.findViewById(R.id.tvVariantHeaderSubtitle)
        tvVariantLevel1Count = view.findViewById(R.id.tvVariantLevel1Count)
        tvVariantLevel2Count = view.findViewById(R.id.tvVariantLevel2Count)
        tvVariantLevel1Type = view.findViewById(R.id.tvVariantLevel1Type)
        tvVariantLevel2Type = view.findViewById(R.id.tvVariantLevel2Type)
        layoutDescriptionTips = view.findViewById(R.id.layoutDescriptionTips)
        btnNext = view.findViewById(R.id.btnNext)
        btnSave = view.findViewById(R.id.btnSave)
    }
    override fun onResume() {
        super.onResume()
        btnNext?.isLoading = false
        btnSave?.isLoading = false
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
                // set product input model and and ui of the page
                mapJsonToObject(productInputModelJson, ProductInputModel::class.java).apply {
                    descriptionViewModel.updateProductInputModel(this)
                    if (!(descriptionViewModel.isAddMode && descriptionViewModel.isFirstMoved)) {
                        applyEditMode()
                    } else {
                        btnNext?.visibility = View.VISIBLE
                        btnSave?.visibility = View.GONE
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
        with(GlobalConfig.isSellerApp()) {
            containerAddEditDescriptionFragmentNoInputVariant?.showWithCondition(!this)
            containerAddEditDescriptionFragmentInputVariant?.showWithCondition(this)
            tvNoVariantDescription?.text = getString(seller_migration_commonR.string.seller_migration_add_edit_no_variant_description).parseAsHtml()
        }
    }

    private fun setupVideoListLayout() {
        textViewAddVideo?.setOnClickListener {
            if (getFilteredValidVideoLink().size == adapter.dataSize) {
                if (descriptionViewModel.isEditMode) {
                    ProductEditDescriptionTracking.clickAddVideoLink(shopId)
                } else {
                    ProductAddDescriptionTracking.clickAddVideoLink(shopId)
                }
                adapter.data.add(VideoLinkModel())
                adapter.notifyDataSetChanged()
                textViewAddVideo?.visibility = if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
            }
        }

        getRecyclerView(view)?.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
    }

    private fun setupSubmitButton() {
        btnNext?.setOnClickListener {
            btnNext?.isLoading = true
            if (textFieldDescription.getText().trim().isEmpty()) {
                textFieldDescription?.isInputError = true
                textFieldDescription?.setMessage(getString(productaddeditR.string.error_product_description))
            } else {
                moveToShipmentActivity()
            }
        }

        btnSave?.setOnClickListener {
            if (textFieldDescription.getText().trim().isEmpty()) {
                textFieldDescription?.isInputError = true
                textFieldDescription?.setMessage(getString(productaddeditR.string.error_product_description))
            } else {
                btnSave?.isLoading = true
                val isAdding = descriptionViewModel.isAddMode
                val isDrafting = descriptionViewModel.isDraftMode
                if (isAdding && !isDrafting) {
                    submitInput()
                } else {
                    submitInputEdit()
                }
            }
        }
    }

    private fun setupVariantLayout() {
        layoutVariantTips?.setOnClickListener {
            showVariantTips()
        }

        tvAddVariant?.setOnClickListener {
            sendClickAddProductVariant()
            showVariantDialog()
        }

        tvEditVariant?.setOnClickListener {
            sendClickAddProductVariant()
            showVariantDialog()
        }
    }

    private fun setupDescriptionLayout() {
        layoutDescriptionTips?.setOnClickListener {
            showDescriptionTips()
        }

        val maxChar = context?.resources?.getInteger(R.integer.max_product_desc_length).orZero()
        textFieldDescription?.setCounter(maxChar)
        textFieldDescription?.editText?.apply {
            isSingleLine = false
            imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
            afterTextChanged {
                if (it.length >= maxChar) {
                    textFieldDescription?.setMessage(getString(R.string.error_description_character_limit))
                    textFieldDescription?.isInputError = true
                } else {
                    textFieldDescription?.setMessage("")
                    textFieldDescription?.isInputError = false
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
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.observe(
            viewLifecycleOwner,
            Observer { bundle ->
                setNavigationResult(bundle, REQUEST_KEY_ADD_MODE)
                removeNavigationResult(REQUEST_KEY_ADD_MODE)
                findNavController().navigateUp()
            }
        )
    }

    private fun sendDataBack() {
        if (descriptionViewModel.isAddMode && !descriptionViewModel.isDraftMode) {
            var dataBackPressed = NO_DATA
            if (descriptionViewModel.isFirstMoved) {
                inputAllDataInInputModel()
                dataBackPressed = DESCRIPTION_DATA
                descriptionViewModel.productInputModel.value?.requestCode = arrayListOf(DETAIL_DATA, DESCRIPTION_DATA, NO_DATA)
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
            if (isHampers && GlobalConfig.isSellerApp()) {
                layoutDescriptionTips?.tvTipsText?.text = getString(R.string.label_gifting_description_tips)
                layoutDescriptionTips?.setOnClickListener {
                    showGiftingDescription()
                }
            }
        }
    }

    private fun observeHasDTStock() {
        descriptionViewModel.hasDTStock.observe(viewLifecycleOwner) {
            if (it) {
                tvAddVariant?.setColorToDisabled()
                tvAddVariant?.setOnClickListener {
                    showDTDisableVariantChangeDialog()
                }
            }
        }
    }

    private fun observeIsRemovingSingleVariant() {
        descriptionViewModel.isRemovingSingleVariant.observe(viewLifecycleOwner) {
            view?.post {
                if (it) sendDataBack()
            }
        }
    }

    private fun updateVariantLayout() {
        if (descriptionViewModel.hasVariant) {
            tvEditVariant?.visible()
            tvAddVariant?.gone()
            layoutVariantTips?.gone()
        } else {
            tvEditVariant?.gone()
            tvAddVariant?.visible()
            layoutVariantTips?.visible()
        }
        tvVariantHeaderSubtitle?.text = descriptionViewModel.getVariantSelectedMessage()
        tvVariantLevel1Type?.setTextOrGone(
            descriptionViewModel
                .getVariantTypeMessage(VARIANT_VALUE_LEVEL_ONE_POSITION)
        )
        tvVariantLevel2Type?.setTextOrGone(
            descriptionViewModel
                .getVariantTypeMessage(VARIANT_VALUE_LEVEL_TWO_POSITION)
        )
        tvVariantLevel1Count?.setTextOrGone(
            descriptionViewModel
                .getVariantCountMessage(VARIANT_VALUE_LEVEL_ONE_POSITION)
        )
        tvVariantLevel2Count?.setTextOrGone(
            descriptionViewModel
                .getVariantCountMessage(VARIANT_VALUE_LEVEL_TWO_POSITION)
        )
    }

    private fun observeProductVideo() {
        descriptionViewModel.videoYoutube.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val resultData = result.data.getYoutubeVideoSnippet.items
                    if (resultData.size.isMoreThanZero()) {
                        stopNetworkRequestPerformanceMonitoring()
                        startRenderPerformanceMonitoring()
                        setDataOnSelectedVideo(resultData[0], youtubeAdapterPosition)
                    } else {
                        displayErrorOnSelectedVideo(
                            position = youtubeAdapterPosition,
                            errMessage = result.data.getYoutubeVideoSnippet.error.messages
                        )
                    }
                }
                is Fail -> {
                    displayErrorOnSelectedVideo(
                        position = youtubeAdapterPosition,
                        errMessage = getString(R.string.error_video_not_valid)
                    )
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

    private fun displayErrorOnSelectedVideo(position: Int, errMessage: String) {
        adapter.data.getOrNull(position)?.apply {
            inputTitle = ""
            inputDescription = ""
            inputImage = ""
            errorMessage = if (inputUrl.isBlank()) "" else errMessage
        }
    }

    private fun setDataOnSelectedVideo(youtubeVideoModel: GetYoutubeVideoSnippetResponse.GetYoutubeVideoSnippet.Items, position: Int) {
        adapter.data.getOrNull(position)?.apply {
            inputTitle = youtubeVideoModel.snippet.title
            inputDescription = youtubeVideoModel.snippet.description
            inputImage = youtubeVideoModel.snippet.thumbnails.default.url
            errorMessage = descriptionViewModel.validateDuplicateVideo(adapter.data, inputUrl)
        }
    }

    private fun validateDescriptionText(it: String) {
        descriptionViewModel.validateDescriptionChanged(it)
    }

    private fun updateDescriptionFieldErrorMessage(message: String) {
        textFieldDescription?.setMessage(message)
        textFieldDescription?.isInputError = message.isNotEmpty()
        btnSave?.isEnabled = message.isEmpty()
    }

    private fun applyEditMode() {
        val description = descriptionViewModel.descriptionInputModel?.productDescription ?: ""
        val videoLinks = descriptionViewModel.descriptionInputModel?.videoLinkList?.map {
            it.copy() // deep copy all object to prevent change at current productInputModel
        }.orEmpty()

        textFieldDescription?.setText(description.fromHtmlWithSpaceAndLinebreak())
        if (!videoLinks.isNullOrEmpty()) {
            super.clearAllData()
            super.renderList(videoLinks)
            textViewAddVideo?.visibility = if (adapter.dataSize < MAX_VIDEOS) View.VISIBLE else View.GONE
            // start network monitoring when videoLinks is not empty
            startNetworkRequestPerformanceMonitoring()
        } else {
            // end all monitoring when videoLinks is empty
            stopPreparePagePerformanceMonitoring()
            stopPerformanceMonitoring()
        }

        updateVariantLayout()
        btnNext?.visibility = View.GONE
        btnSave?.visibility = View.VISIBLE
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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    sendDataBack()

                    if (descriptionViewModel.isEditMode) {
                        ProductEditDescriptionTracking.clickBack(shopId)
                    }
                }
            }
        )
    }

    private fun showDescriptionTips() {
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
        }
//        } else {
//            ProductAddDescriptionTracking.clickHelpVariant(shopId)
//        }

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

    private fun showDTDisableVariantChangeDialog() {
        val dialog = DialogUnify(context ?: return, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
        val descriptionText = getString(R.string.product_add_edit_text_disabled_variant_deactivate_dialog)
        dialog.apply {
            setTitle(getString(R.string.product_add_edit_title_disabled_variant_deactivate_dialog))
            setDescription(descriptionText)
            setPrimaryCTAText(getString(R.string.action_oke_got_it))
            setPrimaryCTAClickListener {
                dismiss()
            }
        }
        dialog.show()
    }

    override fun loadData(page: Int) {
        super.renderList(mutableListOf(VideoLinkModel()))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val guideline: Guideline? = activity?.findViewById(R.id.guideline)
        val dividerNavigation: DividerUnify? = activity?.findViewById(R.id.divider_navigation)
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val guidelinePercent = if (isLandscape) AddEditProductFragment.GUIDELINE_PERCENT else Int.ZERO.toFloat()

        guideline?.setGuidelinePercent(guidelinePercent)
        dividerNavigation?.isVisible = isLandscape
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
        mainLayout?.setOnTouchListener { _, _ ->
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
            setNavigationResult(bundle, requestKey)
            findNavController().navigateUp()
        }
    }

    private fun getFilteredValidVideoLink() = adapter.data.filter {
        it.inputUrl.isNotBlank() && it.errorMessage.isBlank()
    }

    private fun updateSaveButtonStatus() {
        with(descriptionViewModel.validateInputVideo(adapter.data)) {
            btnSave?.isEnabled = this
            btnNext?.isEnabled = this
        }
    }

    private fun copyDescriptionTemplate() {
        val template = getString(R.string.label_gifting_description_template)
        val clipboard = requireView().context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(template, template)
        clipboard.setPrimaryClip(clipData)
        Toaster.build(
            requireView(),
            getString(R.string.label_gifting_description_copied_message),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun highlightNavigationButton() {
        val btnIndicatorMain: Typography? = activity?.findViewById(R.id.btn_indicator_main)
        val btnIndicatorDetail: Typography? = activity?.findViewById(R.id.btn_indicator_detail)
        val btnIndicatorDescription: Typography? = activity?.findViewById(R.id.btn_indicator_description)
        val btnIndicatorShipment: Typography? = activity?.findViewById(R.id.btn_indicator_shipment)

        btnIndicatorMain?.activateHighlight(false)
        btnIndicatorDetail?.activateHighlight(false)
        btnIndicatorDescription?.activateHighlight(true)
        btnIndicatorShipment?.activateHighlight(false)
    }
}

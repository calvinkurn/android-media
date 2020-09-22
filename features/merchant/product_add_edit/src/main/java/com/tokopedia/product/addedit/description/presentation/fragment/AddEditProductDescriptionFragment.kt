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
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.description.di.AddEditProductDescriptionModule
import com.tokopedia.product.addedit.description.di.DaggerAddEditProductDescriptionComponent
import com.tokopedia.product.addedit.description.presentation.adapter.VideoLinkTypeFactory
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
import kotlinx.android.synthetic.main.add_edit_product_no_variant_input_layout.*
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
        if (descriptionViewModel.isEditMode) {
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
            if (descriptionViewModel.isEditMode) {
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
                if (descriptionViewModel.isEditMode) {
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
            sendClickAddProductVariant()
            showVariantDialog()
        }

        tvEditVariant.setOnClickListener {
            sendClickAddProductVariant()
            showVariantDialog()
        }

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

        getRecyclerView(view).itemAnimator = object: DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }

        if (!(descriptionViewModel.isAddMode && descriptionViewModel.isFirstMoved)) applyEditMode()

        with (GlobalConfig.isSellerApp()) {
            containerAddEditDescriptionFragmentNoInputVariant.showWithCondition(!this)
            containerAddEditDescriptionFragmentInputVariant.showWithCondition(this)
            tvNoVariantDescription.text = getString(com.tokopedia.seller_migration_common.R.string.seller_migration_add_edit_no_variant_description).parseAsHtml()
        }
        onFragmentResult()
        setupOnBackPressed()

        hideKeyboardWhenTouchOutside()
        observeProductInputModel()
        observeProductVideo()
    }

    private fun sendClickAddProductVariant() {
        if (descriptionViewModel.isEditMode) {
            ProductEditDescriptionTracking.clickAddProductVariant(shopId)
        } else {
            ProductAddDescriptionTracking.clickAddProductVariant(shopId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    private fun removeObservers() {
        descriptionViewModel.productInputModel.removeObservers(this)
        descriptionViewModel.videoYoutube.removeObservers(this)
        getNavigationResult(REQUEST_KEY_ADD_MODE)?.removeObservers(this)
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
                inputAllDataInInputDraftModel()
                dataBackPressed = DESCRIPTION_DATA
                descriptionViewModel.productInputModel.value?.requestCode = arrayOf(DETAIL_DATA, DESCRIPTION_DATA, NO_DATA)
            }
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        } else {
            setFragmentResultWithBundle(REQUEST_KEY_DESCRIPTION)
        }
    }

    private fun inputAllDataInInputDraftModel() {
        descriptionViewModel.productInputModel.value?.descriptionInputModel = DescriptionInputModel(
                textFieldDescription.getText(),
                getFilteredValidVideoLink()
        )
    }

    private fun observeProductInputModel() {
        descriptionViewModel.productInputModel.observe(viewLifecycleOwner, Observer {
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
        if (descriptionViewModel.isAddMode) {
            ProductAddDescriptionTracking.clickContinue(shopId)
        }
        inputAllDataInInputDraftModel()
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
                findNavController().navigate(destination)
            }
        }
    }

    private fun submitInput() {
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            inputAllDataInInputDraftModel()
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE)
        }
    }

    private fun submitInputEdit() {
        if (descriptionViewModel.validateInputVideo(adapter.data)) {
            inputAllDataInInputDraftModel()
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
}

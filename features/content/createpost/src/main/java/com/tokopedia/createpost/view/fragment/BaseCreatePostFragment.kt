package com.tokopedia.createpost.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.affiliatecommon.analytics.AffiliateEventTracking
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.config.GlobalConfig
import com.tokopedia.createpost.CREATE_POST_ERROR_MSG
import com.tokopedia.createpost.DRAFT_ID
import com.tokopedia.createpost.TYPE_AFFILIATE
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.data.pojo.getcontentform.Author
import com.tokopedia.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.domain.entity.FeedDetail
import com.tokopedia.createpost.view.activity.*
import com.tokopedia.createpost.view.adapter.DefaultCaptionsAdapter
import com.tokopedia.createpost.view.adapter.ProductAttachmentAdapter
import com.tokopedia.createpost.view.adapter.ProductSuggestionAdapter
import com.tokopedia.createpost.view.adapter.ShareBottomSheetAdapter
import com.tokopedia.createpost.view.contract.CreatePostContract
import com.tokopedia.createpost.view.listener.CreatePostActivityListener
import com.tokopedia.createpost.view.service.SubmitPostService
import com.tokopedia.createpost.view.type.ShareType
import com.tokopedia.createpost.view.util.SpaceItemDecoration
import com.tokopedia.createpost.view.viewmodel.*
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.Dialog
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.twitter_share.TwitterAuthenticator
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_share_post.view.*
import kotlinx.android.synthetic.main.fragment_af_create_post.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseCreatePostFragment : BaseDaggerFragment(),
        CreatePostContract.View {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var affiliateAnalytics: AffiliateAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var affiliatePref: AffiliatePreference

    protected var viewModel: CreatePostViewModel = CreatePostViewModel()

    protected val adapter: ProductAttachmentAdapter by lazy {
        ProductAttachmentAdapter(onDeleteProduct = this::onDeleteProduct)
    }

    private val activityListener: CreatePostActivityListener? by lazy {
        activity as? CreatePostActivityListener
    }

    private val captionsAdapter: DefaultCaptionsAdapter by lazy {
        DefaultCaptionsAdapter(this::onDefaultCaptionClicked)
    }

    private val shareAdapter: ShareBottomSheetAdapter by lazy {
        ShareBottomSheetAdapter(::onShareButtonClicked)
    }

    private val productSuggestionAdapter: ProductSuggestionAdapter by lazy {
        ProductSuggestionAdapter(::onSuggestionItemClicked, ::onSuggestionItemFirstView)
    }

    private lateinit var shareDialogView: View

    private val shareDialog: CloseableBottomSheetDialog by lazy {
        CloseableBottomSheetDialog.createInstanceRounded(context).apply {
            setContentView(shareDialogView)
        }
    }

    protected val productAttachmentLayoutManager by lazy {
        LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
    }

    lateinit var productSmoothScroller: LinearSmoothScroller

    companion object {
        private const val MILLISECONDS_PER_INCH = 200f
        private const val VIEW_MODEL = "view_model"
        private const val PARAM_USER_ID = "{user_id}"
        private const val REQUEST_IMAGE_PICKER = 1234
        private const val REQUEST_VIDEO_PICKER = 1235
        private const val REQUEST_PREVIEW = 13
        private const val REQUEST_LOGIN = 83
        private const val MAX_CHAR = 2000
        private const val CHAR_LENGTH_TO_SHOW = 1900
        private const val IMAGE_EXIST = "image_exist"
        private const val VIDEOS_RESULT = "video_result"
    }

    abstract fun fetchContentForm()

    abstract fun onRelatedAddProductClick()

    abstract fun fetchProductSuggestion(onSuccess: (List<ProductSuggestionItem>) -> Unit,
                                        onError: (Throwable) -> Unit)

    override fun initInjector() {
        DaggerCreatePostComponent.builder()
                .createPostModule(CreatePostModule(requireContext().applicationContext))
                .build()
                .inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.let {
            productSmoothScroller = object : LinearSmoothScroller(it) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }
            }
        }
    }

    override fun getScreenName(): String {
        return AffiliateEventTracking.Screen.BYME_CREATE_POST
    }

    override fun onStart() {
        super.onStart()
        affiliateAnalytics.analyticTracker.sendScreenAuthenticated(screenName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        initVar(savedInstanceState)
        initView()
        if (userSession.isLoggedIn) {
            presenter.getFollowersCount(isTypeAffiliate())
            presenter.invalidateShareOptions()
            if (viewModel.isEditState) {
                presenter.getFeedDetail(viewModel.postId, isTypeAffiliate())
            } else {
                fetchContentForm()
            }
        } else {
            context?.let {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQUEST_LOGIN)
            }
        }
    }

    private fun updateMaxCharacter() {
        val cLength = viewModel.caption.length
        if (cLength >= CHAR_LENGTH_TO_SHOW) {
            counter.text = String.format(Locale.getDefault(), "%,d/%,d",
                    viewModel.caption.length,
                    MAX_CHAR
            )
            counter.visible()
        } else
            counter.gone()
    }

    override fun onPause() {
        if (caption.isFocused)
            caption.clearFocus()
        super.onPause()
    }

    private inline val isPostEnabled: Boolean
        get() = viewModel.postId.isNotBlank() ||
                (viewModel.completeImageList.isNotEmpty()
                        && viewModel.relatedProducts.isNotEmpty()
                        && (viewModel.adIdList.isNotEmpty()) || viewModel.productIdList.isNotEmpty())

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(VIEW_MODEL, viewModel)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_PICKER -> if (resultCode == Activity.RESULT_OK) {
                val imageList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
                val images = imageList.map { MediaModel(it, MediaType.IMAGE) }

                viewModel.fileImageList.removeAll { it.type == MediaType.IMAGE }
                viewModel.fileImageList.addAll(images)

                updateMediaPreview()

                if (viewModel.fileImageList.isNotEmpty()) {
                    viewModel.urlImageList.clear()
                } else {
                    fetchContentForm()
                }

                activityListener?.invalidatePostMenu(isPostEnabled)
            }
            REQUEST_VIDEO_PICKER -> if (resultCode == Activity.RESULT_OK) {
                val videoList = data?.getStringArrayListExtra(VIDEOS_RESULT) ?: arrayListOf()
                val videos = videoList.map { MediaModel(it, MediaType.VIDEO) }

                viewModel.fileImageList.removeAll { it.type == MediaType.VIDEO }
                viewModel.fileImageList.addAll(videos)
                updateMediaPreview()

                if (viewModel.fileImageList.isNotEmpty()) {
                    viewModel.urlImageList.clear()
                } else {
                    fetchContentForm()
                }

                activityListener?.invalidatePostMenu(isPostEnabled)
            }
            REQUEST_PREVIEW -> if (resultCode == Activity.RESULT_OK) {
                val resultViewModel = data?.getParcelableExtra<CreatePostViewModel>(
                        CreatePostViewModel.TAG
                )
                resultViewModel?.let {
                    viewModel = resultViewModel
                    updateRelatedProduct()
                    updateMediaPreview()

                    if (viewModel.completeImageList.isEmpty()) {
                        fetchContentForm()
                    }
                }
            }
            REQUEST_LOGIN -> fetchContentForm()
            else -> {
            }
        }
    }

    override fun showLoading() {
        action_bottom.gone()
        layout_default_caption.gone()
        hideProductSuggestion()
        view?.showLoading()
    }

    override fun hideLoading() {
        view?.hideLoading()
    }

    override fun onSuccessGetPostEdit(feedDetail: FeedDetail) {
        hideLoading()
        viewModel.caption = feedDetail.caption
        viewModel.productIdList.addAll(feedDetail.postTagId)
        viewModel.fileImageList.addAll(feedDetail.media)
        caption.setText(viewModel.caption)
        fetchContentForm()
    }

    override fun onErrorGetPostEdit(e: Throwable?) {
        hideLoading()
        showUnifyErrorToaster(ErrorHandler.getErrorMessage(context, e))
        activity?.finish()
    }

    override fun onSuccessGetContentForm(feedContentForm: FeedContentForm, isFromTemplateToken: Boolean) {
        if (viewModel.isEditState) action_bottom.gone() else action_bottom.visible()
        viewModel.token = feedContentForm.token
        viewModel.maxImage = feedContentForm.media.maxMedia
        viewModel.allowImage = feedContentForm.media.allowImage
        viewModel.allowVideo = feedContentForm.media.allowVideo
        viewModel.maxProduct = feedContentForm.maxTag
        viewModel.defaultPlaceholder = feedContentForm.defaultPlaceholder
        if (viewModel.caption.isEmpty()) viewModel.caption = feedContentForm.caption

        if (feedContentForm.media.media.isNotEmpty() && viewModel.fileImageList.isEmpty()) {
            viewModel.urlImageList.clear()
            viewModel.urlImageList.addAll(feedContentForm.media.media.map { MediaModel(it.mediaUrl, it.type) })
            media_attachment.bind(viewModel.urlImageList.map { MediaItem(thumbnail = it.path, type = it.type) })
        }

        if (feedContentForm.relatedItems.isNotEmpty()) {
            viewModel.relatedProducts.clear()
            viewModel.relatedProducts.addAll(feedContentForm.relatedItems.map {
                RelatedProductItem(
                        it.id,
                        it.title,
                        it.price,
                        it.image,
                        viewModel.authorType
                )
            })
        }

        captionsAdapter.updateCaptions(feedContentForm.defaultCaptions)
        updateRelatedProduct()
        updateMedia()
        updateMediaPreview()
        activityListener?.invalidatePostMenu(isPostEnabled)
        updateCaption()
        updateHeader(feedContentForm.authors)

        fetchAndRenderProductSuggestion()
    }

    override fun onErrorGetContentForm(message: String) {
        layout_default_caption.gone()
        action_bottom.gone()
        NetworkErrorHelper.showEmptyState(context, main_view, message) {
            fetchContentForm()
        }

    }

    override fun onErrorNoQuota() {
        activity?.let {
            showUnifyErrorToaster(getString(R.string.cp_text_full_affiliate_title))
            it.finish()
            affiliateAnalytics.onJatahRekomendasiHabisDialogShow()
        }
    }

    override fun onAuthenticateTwitter(authenticator: TwitterAuthenticator) {
        context?.let(authenticator::startAuthenticate)
    }

    override fun onGetAvailableShareTypeList(typeList: List<ShareType>) {
        shareAdapter.setItems(typeList)
    }

    override fun changeShareHeaderText(text: String) {
        activityListener?.updateShareHeader(text)
    }

    open fun updateRelatedProduct() {
        adapter.updateProduct(viewModel.relatedProducts)
        if (viewModel.relatedProducts.isEmpty() || viewModel.isEditState) {
            product_attachment.gone()
            label_title_product_attachment.gone()
        } else {
            product_attachment.visible()
            label_title_product_attachment.visible()
        }
    }

    fun openShareBottomSheetDialog() {
        presenter.invalidateShareOptions()
        if (!::shareDialogView.isInitialized) shareDialogView = createBottomSheetView()
        shareDialogView.apply {
            shareList.adapter = shareAdapter
            shareBtn.isEnabled = isPostEnabled
        }
        shareDialog.show()
    }

    protected open fun initVar(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: CreatePostViewModel()
        } else if (arguments != null) {
            if (requireArguments().getString(DRAFT_ID) != null) {
                initDraft(requireArguments())
            } else {
                viewModel.postId = requireArguments().getString(PARAM_POST_ID, "")
                viewModel.authorType = requireArguments().getString(PARAM_TYPE, "")

                initProductIds()
            }
        } else {
            activity?.finish()
        }
    }

    protected fun initProductIds() {
        val productIds = requireArguments().getString(PARAM_PRODUCT_ID, "")
                .split(',')
                .filterNot { it == "-1" }
                .toMutableList()
                .apply { removeAll { it.trim() == "" } }

        val adIds = requireArguments().getString(PARAM_AD_ID, "")
                .split(',')
                .filterNot { it == "-1" }
                .toMutableList()
                .apply { removeAll { it.trim() == "" } }

        if (!isDuplicateFound(productIds, adIds)) {
            viewModel.productIdList.addAll(productIds)
            viewModel.adIdList.addAll(adIds)
        } else {
            view?.let {
                Toaster.make(it, getString(R.string.cp_duplicate_product), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.affiliatecommon.R.string.af_title_ok))
            }
        }
    }

    private fun onAddProduct() {
        val numOfProducts = with(viewModel) { if (isTypeAffiliate()) adIdList else productIdList }.size
        if (numOfProducts < viewModel.maxProduct) {
            onRelatedAddProductClick()
            affiliateAnalytics.onTambahTagButtonClicked()
        } else {
            view?.run {
                Toaster.make(this, getString(R.string.string_attach_product_warning_max_product_format, viewModel.maxProduct.toString()), Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR, getString(com.tokopedia.resources.common.R.string.general_label_ok))
            }
        }
    }

    private fun initDraft(arguments: Bundle) {
        activity?.let {
            val draftId = arguments.getString(DRAFT_ID)
            val cacheManager = SaveInstanceCacheManager(it, draftId)
            val draft: CreatePostViewModel? = cacheManager.get(
                    CreatePostViewModel.TAG,
                    CreatePostViewModel::class.java
            )

            if (draft != null) {
                viewModel = draft
                handleDraftError(arguments)
            } else {
                it.finish()
            }
        }
    }

    private fun handleDraftError(arguments: Bundle) {
        val errorMessage = arguments.getString(CREATE_POST_ERROR_MSG, "")
        if (errorMessage.isNotBlank()) {
            view?.let {
                Toaster.make(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun initView() {
        adapter.updateProduct(viewModel.relatedProducts)
        product_attachment.adapter = adapter
        product_attachment.setHasFixedSize(true)
        product_attachment.layoutManager = productAttachmentLayoutManager
        product_attachment.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                LinearLayoutManager.HORIZONTAL))

        image_picker.setOnClickListener {
            affiliateAnalytics.onTambahGambarButtonClicked()
            goToImagePicker()
        }
        video_picker.setOnClickListener {
            affiliateAnalytics.onTambahVideoButtonClicked()
            goToVideoPicker()
        }
        media_attachment.setOnFileClickListener(object : FeedMultipleImageView.OnFileClickListener {
            private fun deleteMedia(position: Int) {
                viewModel.fileImageList.removeAt(position)
                if (position < viewModel.urlImageList.size)
                    viewModel.urlImageList.removeAt(position)

                if (viewModel.completeImageList.isEmpty())
                    fetchContentForm()
            }

            override fun onDeleteItem(item: MediaItem, position: Int) {
                if (viewModel.fileImageList.size == 1 &&
                        (viewModel.productIdList.isNotEmpty() || viewModel.adIdList.isNotEmpty())) {
                    val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
                    dialog.setTitle(getString(R.string.cp_update_post))
                    dialog.setDesc(getString(R.string.cp_delete_warning_desc))
                    dialog.setBtnOk(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
                    dialog.setBtnCancel(getString(com.tokopedia.design.R.string.title_delete))
                    dialog.setOnOkClickListener {
                        dialog.dismiss()
                        media_attachment.bind(listOf(item))
                    }
                    dialog.setOnCancelClickListener {
                        dialog.dismiss()
                        deleteMedia(position)
                    }
                    dialog.setCancelable(true)
                    dialog.show()
                } else {
                    deleteMedia(position)
                }

            }

            override fun onClickItem(item: MediaItem, position: Int) {
                goToMediaPreview()
            }
        })
        updateMediaPreview()
        caption.filters = arrayOf(InputFilter.LengthFilter(MAX_CHAR))
        caption.afterTextChanged {
            viewModel.caption = it
            updateMaxCharacter()
        }
        caption.setOnTouchListener { v, event ->
            if (v.id == R.id.caption) {
                showKeyboard()
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
        caption.hint = viewModel.defaultPlaceholder
        caption.setText(viewModel.caption)
        caption.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layout_default_caption.visible()
                hideProductSuggestion()
                action_bottom.gone()
            } else {
                layout_default_caption.gone()
                showProductSuggestion()
                if (!viewModel.isEditState) action_bottom.visible() else action_bottom.gone()
            }
        }
        list_captions.adapter = captionsAdapter
        list_captions.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        list_captions.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8), LinearLayoutManager.HORIZONTAL))
        icon_add_product.setOnClickListener { onAddProduct() }
        label_add_product.setOnClickListener { onAddProduct() }

        if (viewModel.isEditState) {
            media_attachment.gone()
        }
    }

    private fun updateMediaPreview() {
        val mItems = if (viewModel.fileImageList.isEmpty()) {
            viewModel.urlImageList.map { MediaItem(thumbnail = it.path, type = it.type, isSelected = true) }
        } else viewModel.fileImageList.map { MediaItem(thumbnail = it.path, type = it.type) }
        media_attachment.bind(mItems)
        media_attachment.visibility = if (mItems.isEmpty() || viewModel.isEditState) View.GONE else View.VISIBLE

    }

    private fun onDefaultCaptionClicked(_caption: String) {
        if ((caption.text?.length ?: 0) + _caption.length <= MAX_CHAR) {
            caption.text?.append(_caption)
        }
    }

    private fun goToVideoPicker() {
        activity?.let { activity ->
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.VIDEO_PICKER)
            intent.putExtra(IMAGE_EXIST, viewModel.fileImageList.any { it.type == MediaType.VIDEO })
            startActivityForResult(intent, REQUEST_VIDEO_PICKER)
        }
    }

    private fun goToImagePicker() {
        val imageOnly = viewModel.fileImageList.filter { it.type == MediaType.IMAGE }
        val countVid = viewModel.fileImageList.size - imageOnly.size
        activity?.let {
            startActivityForResult(
                    CreatePostImagePickerNavigation.getIntent(
                            it,
                            ArrayList(imageOnly),
                            viewModel.maxImage - countVid
                    ),
                    REQUEST_IMAGE_PICKER)
        }
    }

    private fun goToProfile() {
        activity?.let {
            var profileApplink = ApplinkConst.PROFILE_AFTER_POST
            profileApplink = profileApplink.replace(PARAM_USER_ID, userSession.userId)
            val intent = RouteManager.getIntent(
                    it,
                    profileApplink
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun goToFeed() {
        activity?.let {
            val applink = ApplinkConst.FEED.plus("?after_post=true")
            val intent = RouteManager.getIntent(it, applink)
            startActivity(intent)
        }
    }

    private fun goToMediaPreview() {
        context?.let {
            startActivityForResult(CreatePostMediaPreviewActivity.createIntent(it, viewModel), REQUEST_PREVIEW)
        }
    }

    private fun isDuplicateFound(productIds: MutableList<String>,
                                 adIds: MutableList<String>): Boolean {
        viewModel.productIdList.forEach { productId ->
            if (productIds.any { productId == it }) {
                return true
            }
        }
        viewModel.adIdList.forEach { adId ->
            if (adIds.any { adId == it }) {
                return true
            }
        }
        return false
    }

    private fun isFormInvalid(): Boolean {
        var isFormInvalid = false
        if (isTypeAffiliate() && viewModel.adIdList.isEmpty() && !viewModel.isEditState) {
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.cp_warning_empty_product), getString(com.tokopedia.abstraction.R.string.label_add)) {
                onRelatedAddProductClick()
            }
        } else if (!isTypeAffiliate() && viewModel.productIdList.isEmpty() && !viewModel.isEditState) {
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.cp_warning_empty_product), getString(com.tokopedia.abstraction.R.string.label_add)) {
                onRelatedAddProductClick()
            }
        } else if (viewModel.completeImageList.isEmpty() && !viewModel.isEditState) {
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.cp_warning_empty_photo), getString(com.tokopedia.abstraction.R.string.label_add)) {
                goToImagePicker()
            }
        } else if ((caption.text?.length ?: 0) > MAX_CHAR) {
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.cp_warning_over_char, MAX_CHAR.toString()), getString(com.tokopedia.resources.common.R.string.general_label_ok))
        }
        return isFormInvalid
    }

    private fun showUnifyErrorToaster(message: CharSequence, action: CharSequence? = null, actionClick: ((View) -> Unit)? = null) {
        view?.let { v ->
            if (action.isNullOrBlank()) {
                Toaster.make(v, message.toString(), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            } else {
                Toaster.apply {
                    toasterCustomCtaWidth = resources.getDimension(R.dimen.dp_100).toPx().toInt()
                    make(v, message.toString(), Snackbar.LENGTH_LONG, TYPE_ERROR, action.toString(), View.OnClickListener {
                        actionClick?.invoke(it)
                    })
                }
            }
        }
    }

    fun saveDraftAndSubmit(skipFirstTimeChecking: Boolean = false) {
        if (isFormInvalid()) {
            return
        }
        context?.let {
            val input = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view?.applicationWindowToken, 0)
        }
        if (affiliatePref.isFirstTimePost(userSession.userId) && !skipFirstTimeChecking) openShareBottomSheetDialog()
        else {
            submitPost()
            affiliatePref.setFirstTimePost(userSession.userId)
        }
    }

    private fun submitPost() {
        activity?.let {
            affiliateAnalytics.onSelesaiCreateButtonClicked(viewModel.productIdList)
            showLoading()

            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(CreatePostViewModel.TAG, viewModel, TimeUnit.DAYS.toMillis(7))

            SubmitPostService.startService(it, cacheManager.id!!)

            hideLoading()

            when {
                GlobalConfig.isSellerApp() -> {
                    activity?.setResult(Activity.RESULT_OK)
                }
                isTypeAffiliate() -> {
                    goToProfile()
                }
                else -> {
                    goToFeed()
                }
            }

            it.finish()
        }
    }

    private fun updateMedia() {
        video_picker.showWithCondition(viewModel.allowVideo)
        image_picker.showWithCondition(viewModel.allowImage)
    }


    private fun updateHeader(authors: List<Author>) {
        if (viewModel.isEditState) {
            activityListener?.updateHeader(HeaderViewModel(
                    getString(R.string.cp_title_edit_post),
                    "",
                    ""

            ))
        } else if (authors.isNotEmpty()) {
            activityListener?.updateHeader(HeaderViewModel(
                    authors.first().name,
                    authors.first().thumbnail,
                    authors.first().badge

            ))
        }
    }

    private fun updateCaption() {
        caption.apply {
            hint = viewModel.defaultPlaceholder
            setText(viewModel.caption)
        }
    }

    private fun onDeleteProduct(position: Int) {
        if (adapter.itemCount < 1) {
            label_title_product_attachment.gone()
            product_attachment.gone()
        } else {
            label_title_product_attachment.visible()
            product_attachment.visible()
        }

        val relatedProductItem = viewModel.relatedProducts.getOrNull(position) ?: return

        viewModel.relatedProducts.removeAt(position)

        if (viewModel.urlImageList.getOrNull(position)?.path == relatedProductItem.image) {
            viewModel.urlImageList.removeAt(position)
        } else {
            viewModel.urlImageList.removeFirst { it.path == relatedProductItem.image }
        }

        val idPosition = if (isTypeAffiliate()) {
            viewModel.adIdList.indexOf(relatedProductItem.id)
        } else {
            viewModel.productIdList.indexOf(relatedProductItem.id)
        }
        if (idPosition != -1 && viewModel.adIdList.size > idPosition) {
            viewModel.adIdList.removeAt(idPosition)
        }
        if (idPosition != -1 && viewModel.productIdList.size > idPosition) {
            viewModel.productIdList.removeAt(idPosition)
        }
        updateMediaPreview()
        activityListener?.invalidatePostMenu(isPostEnabled)

        fetchAndRenderProductSuggestion()
    }

    private fun fetchAndRenderProductSuggestion() {
        if (shouldLoadProductSuggestion()) {
            if (productSuggestionAdapter.isEmpty()) {
                fetchProductSuggestion(::onSuccessGetProductSuggestion, ::onErrorGetProductSuggestion)
                showProductSuggestionLoading()
            } else {
                showProductSuggestion()
            }
        }
    }

    private fun onSuccessGetProductSuggestion(tags: List<ProductSuggestionItem>) {
        productSuggestionAdapter.addAll(tags)
        list_product_suggestion.adapter = productSuggestionAdapter
        showProductSuggestion()
        hideProductSuggestionLoading()
    }

    private fun onErrorGetProductSuggestion(t: Throwable) {
        context?.let {
            Timber.d(t)
            val errorMessage = ErrorHandler.getErrorMessage(context, t)
            showUnifyErrorToaster(errorMessage)
            hideProductSuggestionLoading()
        }
    }

    private fun showProductSuggestionLoading() {
        loading_product_suggestion.visible()
    }

    private fun hideProductSuggestionLoading() {
        loading_product_suggestion.gone()
    }

    private fun showProductSuggestion() {
        if (shouldShowProductSuggestion()) {
            layout_product_suggestion.visible()
            showSuggestionCoachmark()
        } else {
            hideProductSuggestion()
        }
    }

    private fun hideProductSuggestion() {
        layout_product_suggestion.gone()
    }

    private fun shouldLoadProductSuggestion(): Boolean {
        return viewModel.relatedProducts.isEmpty() && !viewModel.isEditState
    }

    private fun shouldShowProductSuggestion(): Boolean {
        return !productSuggestionAdapter.isEmpty() && shouldLoadProductSuggestion()
    }

    private fun showSuggestionCoachmark() {
        val tag = "${userSession.userId}_${viewModel.authorType}"

        //Don't show if already shown
        if (affiliatePref.isCoarchmarkSuggestionShown(tag)) {
            return
        }

        val item: CoachMarkItem = if (isTypeAffiliate()) {
            CoachMarkItem(layout_product_suggestion,
                    getString(R.string.cp_suggestion_aff_cm_title),
                    getString(R.string.cp_suggestion_aff_cm_desc))
        } else {
            CoachMarkItem(layout_product_suggestion,
                    getString(R.string.cp_suggestion_shop_cm_title),
                    getString(R.string.cp_suggestion_shop_cm_desc))
        }
        val list: ArrayList<CoachMarkItem> = arrayListOf(item)

        val coachMark = CoachMark()
        coachMark.show(activity, tag, list)
        affiliatePref.setCoachmarkSuggestionShown(tag)
    }

    private fun onSuggestionItemClicked(item: ProductSuggestionItem) {
        affiliateAnalytics.onSuggestionItemClicked(item.productId, isTypeAffiliate())
        if (item.productId.isNotBlank()) {
            viewModel.productIdList.add(item.productId)
        }
        if (item.adId.isNotBlank()) {
            viewModel.adIdList.add(item.adId)
        }
        fetchContentForm()
    }

    private fun onSuggestionItemFirstView(item: ProductSuggestionItem) {
        affiliateAnalytics.onSuggestionItemAppeared(item.productId, isTypeAffiliate())
    }

    private fun isTypeAffiliate(): Boolean = viewModel.authorType == TYPE_AFFILIATE

    private fun onShareButtonClicked(type: ShareType, isChecked: Boolean) {
        presenter.onShareButtonClicked(type, isChecked)
    }

    private fun getShareTitleAndSubtitle(): Pair<String, String> {
        return if (isTypeAffiliate()) context?.getString(R.string.cp_share_title).orEmpty() to context?.getString(R.string.cp_share_subtitle).orEmpty()
        else context?.getString(R.string.cp_merchant_share_title).orEmpty() to context?.getString(R.string.cp_merchant_share_subtitle).orEmpty()
    }

    private fun createBottomSheetView(): View {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_share_post, null)
        val (title, subtitle) = getShareTitleAndSubtitle()
        return view.apply {
            shareSheetTitle.text = title
            shareSheetSubtitle.text = subtitle
            shareBtn.setOnClickListener {
                saveDraftAndSubmit(true)
                shareDialog.dismiss()
            }
        }
    }

    private fun showKeyboard() {
        activity?.let {
            (it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

}

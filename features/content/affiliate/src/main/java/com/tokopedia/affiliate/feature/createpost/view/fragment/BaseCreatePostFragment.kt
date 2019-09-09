package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.analytics.AffiliateAnalytics
import com.tokopedia.affiliate.analytics.AffiliateEventTracking
import com.tokopedia.affiliate.feature.createpost.CREATE_POST_ERROR_MSG
import com.tokopedia.affiliate.feature.createpost.DRAFT_ID
import com.tokopedia.affiliate.feature.createpost.TYPE_AFFILIATE
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Author
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.affiliate.feature.createpost.di.CreatePostModule
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent
import com.tokopedia.affiliate.feature.createpost.domain.entity.FeedDetail
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostVideoPickerActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.MediaPreviewActivity
import com.tokopedia.affiliate.feature.createpost.view.adapter.DefaultCaptionsAdapter
import com.tokopedia.affiliate.feature.createpost.view.adapter.ProductAttachmentAdapter
import com.tokopedia.affiliate.feature.createpost.view.adapter.ShareBottomSheetAdapter
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.listener.CreatePostActivityListener
import com.tokopedia.affiliate.feature.createpost.view.service.SubmitPostService
import com.tokopedia.affiliate.feature.createpost.view.type.ShareType
import com.tokopedia.affiliate.feature.createpost.view.util.SpaceItemDecoration
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.*
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.component.Dialog
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.twitter_share.TwitterAuthenticator
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videorecorder.main.VideoPickerActivity.Companion.VIDEOS_RESULT
import kotlinx.android.synthetic.main.bottom_sheet_share_post.view.*
import kotlinx.android.synthetic.main.fragment_af_create_post.*
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

    private val invalidatePostCallBack: OnCreatePostCallBack? by lazy {
        activity as? OnCreatePostCallBack
    }

    private val captionsAdapter: DefaultCaptionsAdapter by lazy {
        DefaultCaptionsAdapter(this::onDefaultCaptionClicked)
    }

    private val shareAdapter by lazy {
        ShareBottomSheetAdapter(::onShareButtonClicked)
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

    private fun onDeleteProduct(position: Int){
        if (adapter.itemCount < 1){
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
        invalidatePostCallBack?.invalidatePostMenu(isPostEnabled)
    }

    companion object {
        private const val MILLISECONDS_PER_INCH = 200f
        private const val VIEW_MODEL = "view_model"
        private const val PARAM_USER_ID = "{user_id}"
        private const val PRODUCT_ID_QUERY_PARAM = "?product_id="
        private const val REQUEST_IMAGE_PICKER = 1234
        private const val REQUEST_VIDEO_PICKER = 1235
        private const val REQUEST_PREVIEW = 13
        private const val REQUEST_LOGIN = 83
        private const val MAX_CHAR = 2000
        private const val CHAR_LENGTH_TO_SHOW = 1900
    }

    override fun initInjector() {
        DaggerCreatePostComponent.builder()
                .createPostModule(CreatePostModule(context!!.applicationContext))
                .build()
                .inject(this)
    }

    override fun onAttach(context: Context?) {
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
            if (viewModel.isEditState){
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
                val imageList = data?.getStringArrayListExtra(PICKER_RESULT_PATHS) ?: arrayListOf()
                val images = imageList.map { MediaModel(it, MediaType.IMAGE) }

                viewModel.fileImageList.removeAll { it.type == MediaType.IMAGE }
                viewModel.fileImageList.addAll(images)

                updateMediaPreview()

                if (viewModel.fileImageList.isNotEmpty()) {
                    viewModel.urlImageList.clear()
                } else {
                    fetchContentForm()
                }

                invalidatePostCallBack?.invalidatePostMenu(isPostEnabled)
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

                invalidatePostCallBack?.invalidatePostMenu(isPostEnabled)
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

    override fun onSuccessGetContentForm(feedContentForm: FeedContentForm) {
        if (viewModel.isEditState) action_bottom.gone() else action_bottom.visible()
        viewModel.token = feedContentForm.token
        viewModel.maxImage = feedContentForm.media.maxMedia
        viewModel.allowImage = feedContentForm.media.allowImage
        viewModel.allowVideo = feedContentForm.media.allowVideo
        viewModel.maxProduct = feedContentForm.maxTag
        viewModel.defaultPlaceholder = feedContentForm.defaultPlaceholder

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
        invalidatePostCallBack?.invalidatePostMenu(isPostEnabled)
        updateCaption()
        updateHeader(feedContentForm.authors)
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
            Toast.makeText(it, R.string.text_full_affiliate_title, Toast.LENGTH_LONG)
                    .show()
            it.finish()
            affiliateAnalytics.onJatahRekomendasiHabisDialogShow()
        }
    }

    abstract fun fetchContentForm()

    abstract fun onRelatedAddProductClick()

    protected open fun initVar(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: CreatePostViewModel()
        } else if (arguments != null) {
            if (arguments!!.getString(DRAFT_ID) != null) {
                initDraft(arguments!!)
            } else {
                viewModel.postId = arguments!!.getString(CreatePostActivity.PARAM_POST_ID, "")
                viewModel.authorType = arguments!!.getString(CreatePostActivity.PARAM_TYPE, "")

                initProductIds()
            }
        } else {
            activity?.finish()
        }
    }

    protected fun initProductIds() {
        val productIds = arguments!!.getString(CreatePostActivity.PARAM_PRODUCT_ID, "")
                .split(',')
                .filterNot { it == "-1" }
                .toMutableList()
                .apply { removeAll { it.trim() == "" } }

        val adIds = arguments!!.getString(CreatePostActivity.PARAM_AD_ID, "")
                .split(',')
                .filterNot { it == "-1" }
                .toMutableList()
                .apply { removeAll { it.trim() == "" } }

        if (!isDuplicateFound(productIds, adIds)) {
            viewModel.productIdList.addAll(productIds)
            viewModel.adIdList.addAll(adIds)
        } else {
            view?.showErrorToaster(
                    getString(R.string.af_duplicate_product),
                    getString(R.string.af_title_ok)
            ) { }
        }
    }

    private fun onAddProduct() {
        val numOfProducts = with(viewModel) { if (isTypeAffiliate()) adIdList else productIdList }.size
        if (numOfProducts < viewModel.maxProduct) {
            onRelatedAddProductClick()
            affiliateAnalytics.onTambahTagButtonClicked()
        } else {
            view?.run {
                Toaster.showErrorWithAction(this,
                        getString(R.string.string_attach_product_warning_max_product_format,
                                viewModel.maxProduct.toString()),
                        Snackbar.LENGTH_LONG, getString(R.string.general_label_ok), View.OnClickListener {  })
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
            view?.showErrorToaster(errorMessage)
        }
    }

    private fun initView() {
        adapter.updateProduct(viewModel.relatedProducts)
        product_attachment.adapter = adapter
        product_attachment.setHasFixedSize(true)
        product_attachment.layoutManager = productAttachmentLayoutManager
        product_attachment.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8),
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
            private fun deleteMedia(position: Int){
                viewModel.fileImageList.removeAt(position)
                if (position < viewModel.urlImageList.size)
                    viewModel.urlImageList.removeAt(position)

                if (viewModel.completeImageList.isEmpty())
                    fetchContentForm()
            }

            override fun onDeleteItem(item: MediaItem, position: Int) {
                if (viewModel.fileImageList.size == 1 &&
                        (viewModel.productIdList.isNotEmpty() || viewModel.adIdList.isNotEmpty())){
                    val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
                    dialog.setTitle(getString(R.string.af_update_post))
                    dialog.setDesc(getString(R.string.af_delete_warning_desc))
                    dialog.setBtnOk(getString(R.string.cancel))
                    dialog.setBtnCancel(getString(R.string.title_delete))
                    dialog.setOnOkClickListener{
                        dialog.dismiss()
                        media_attachment.bind(listOf(item))
                    }
                    dialog.setOnCancelClickListener{
                        dialog.dismiss()
                        deleteMedia(position)
                    }
                    dialog.setCancelable(true)
                    dialog.show()
                } else {
                    deleteMedia(position)
                }

            }

            override fun onClickItem(item: MediaItem, position: Int) { goToMediaPreview() }
        })
        updateMediaPreview()
        caption.filters = arrayOf(InputFilter.LengthFilter(MAX_CHAR))
        caption.afterTextChanged {
            viewModel.caption = it
            updateMaxCharacter()
        }
        caption.setOnTouchListener { v, event ->
            if (v.id == R.id.caption) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
        caption.hint = viewModel.defaultPlaceholder
        caption.setText(viewModel.caption)
        caption.onFocusChangeListener = View.OnFocusChangeListener{ _, hasFocus ->
            if (hasFocus){
                layout_default_caption.visible()
                action_bottom.gone()
            } else {
                layout_default_caption.gone()
                if (!viewModel.isEditState)action_bottom.visible() else action_bottom.gone()
            }
        }
        list_captions.adapter = captionsAdapter
        list_captions.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        list_captions.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8), LinearLayoutManager.HORIZONTAL))
        icon_add_product.setOnClickListener { onAddProduct() }
        label_add_product.setOnClickListener { onAddProduct() }

        if (viewModel.isEditState){
            media_attachment.gone()
        }
    }

    private fun updateMediaPreview(){
        val mItems = if (viewModel.fileImageList.isEmpty()){
            viewModel.urlImageList.map { MediaItem(thumbnail = it.path, type = it.type, isSelected = true) }
        } else viewModel.fileImageList.map { MediaItem(thumbnail = it.path, type = it.type) }
        media_attachment.bind(mItems)
        media_attachment.visibility = if (mItems.isEmpty() || viewModel.isEditState) View.GONE else View.VISIBLE

    }

    private fun onDefaultCaptionClicked(_caption: String){
        if ((caption.text?.length ?: 0) + _caption.length <= MAX_CHAR ){
            caption.text?.append(_caption)
        }
    }

    private fun goToVideoPicker() {
        activity?.let { activity ->
            startActivityForResult(
                    CreatePostVideoPickerActivity.getInstance(activity,
                            viewModel.fileImageList.any { it.type == MediaType.VIDEO }),
                    REQUEST_VIDEO_PICKER)
        }
    }

    private fun goToImagePicker() {
        val imageOnly = viewModel.fileImageList.filter { it.type == MediaType.IMAGE }
        val countVid = viewModel.fileImageList.size - imageOnly.size
        activity?.let {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            it,
                            ArrayList(imageOnly),
                            viewModel.maxImage - countVid,
                            viewModel.fileImageList.isEmpty()
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
            startActivityForResult(MediaPreviewActivity.createIntent(it, viewModel), REQUEST_PREVIEW)
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
            showUnifyErrorToaster(getString(R.string.af_warning_empty_product), getString(R.string.label_add)){
                onRelatedAddProductClick()
            }
        } else if (!isTypeAffiliate() && viewModel.productIdList.isEmpty() && !viewModel.isEditState) {
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.af_warning_empty_product), getString(R.string.label_add)) {
                onRelatedAddProductClick()
            }
        } else if (viewModel.completeImageList.isEmpty() && !viewModel.isEditState) {
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.af_warning_empty_photo), getString(R.string.label_add)) {
                goToImagePicker()
            }
        } else if ((caption.text?.length ?: 0) > MAX_CHAR){
            isFormInvalid = true
            showUnifyErrorToaster(getString(R.string.af_warning_over_char, MAX_CHAR.toString()), getString(R.string.general_label_ok))
        }
        return isFormInvalid
    }

    private fun showUnifyErrorToaster(message: CharSequence, action: CharSequence? = null, actionClick: ((View) -> Unit)? = null){
        view?.let {v ->
            if (action.isNullOrBlank()){
                Toaster.showError(v, message, Snackbar.LENGTH_LONG)
            } else {
                Toaster.showErrorWithAction(v, message, Snackbar.LENGTH_LONG, action, View.OnClickListener {
                    actionClick?.invoke(it)
                })
            }
        }
    }

    fun saveDraftAndSubmit(skipFirstTimeChecking: Boolean = false) {
        if (isFormInvalid()) {
            return
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

            if (isTypeAffiliate()) {
                goToProfile()
            } else {
                goToFeed()
            }

            it.finish()
        }
    }

    private fun updateMedia() {
        video_picker.showWithCondition(viewModel.allowVideo)
        image_picker.showWithCondition(viewModel.allowImage)
    }


    private fun updateHeader(authors: List<Author>) {
        if (activity is CreatePostActivityListener ) {
            if(viewModel.isEditState){
                (activity as CreatePostActivityListener).updateHeader(HeaderViewModel(
                        getString(R.string.af_title_edit_post),
                        "",
                        ""

                ))
            } else if (authors.isNotEmpty()) {
                (activity as CreatePostActivityListener).updateHeader(HeaderViewModel(
                        authors.first().name,
                        authors.first().thumbnail,
                        authors.first().badge

                ))
            }
        }
    }

    private fun updateCaption() {
        caption.hint = viewModel.defaultPlaceholder
    }

    open fun updateRelatedProduct() {
        adapter.updateProduct(viewModel.relatedProducts)
        if (viewModel.relatedProducts.isEmpty() || viewModel.isEditState){
            product_attachment.gone()
            label_title_product_attachment.gone()
        } else {
            product_attachment.visible()
            label_title_product_attachment.visible()
        }
    }

    interface OnCreatePostCallBack{
        fun invalidatePostMenu(isPostEnabled: Boolean)
    }

    private fun isTypeAffiliate(): Boolean = viewModel.authorType == TYPE_AFFILIATE

    override fun onGetAvailableShareTypeList(typeList: List<ShareType>) {
        shareAdapter.setItems(typeList)
    }

    override fun changeShareHeaderText(text: String) {
        if (activity is CreatePostActivityListener) {
            (activity as CreatePostActivityListener).updateShareHeader(text)
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

    override fun onAuthenticateTwitter(authenticator: TwitterAuthenticator) {
        context?.let(authenticator::startAuthenticate)
    }

    private fun onShareButtonClicked(type: ShareType, isChecked: Boolean) {
        presenter.onShareButtonClicked(type, isChecked)
    }

    private fun getShareTitleAndSubtitle(): Pair<String, String> {
        return if (isTypeAffiliate()) context?.getString(R.string.af_share_title).orEmpty() to context?.getString(R.string.af_share_subtitle).orEmpty()
        else context?.getString(R.string.af_merchant_share_title).orEmpty() to context?.getString(R.string.af_merchant_share_subtitle).orEmpty()
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
}

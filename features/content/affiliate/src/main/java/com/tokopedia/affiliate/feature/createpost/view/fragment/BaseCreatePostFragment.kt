package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputFilter
import android.view.*
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostVideoPickerActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.MediaPreviewActivity
import com.tokopedia.affiliate.feature.createpost.view.adapter.DefaultCaptionsAdapter
import com.tokopedia.affiliate.feature.createpost.view.adapter.ProductAttachmentAdapter
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.listener.CreatePostActivityListener
import com.tokopedia.affiliate.feature.createpost.view.service.SubmitPostService
import com.tokopedia.affiliate.feature.createpost.view.util.SpaceItemDecoration
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videorecorder.main.VideoPickerActivity.Companion.VIDEOS_RESULT
import kotlinx.android.synthetic.main.fragment_new_create_post.*
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

    protected var viewModel: CreatePostViewModel = CreatePostViewModel()

    private val adapter: ProductAttachmentAdapter by lazy {
        ProductAttachmentAdapter(onDeleteProduct = this::onDeleteProduct)
    }

    private val invalidatePostCallBack: OnCreatePostCallBack? by lazy {
        activity as? OnCreatePostCallBack
    }

    private val captionsAdapter: DefaultCaptionsAdapter by lazy {
        DefaultCaptionsAdapter(this::onDefaultCaptionClicked)
    }

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
    }

    companion object {
        private const val VIEW_MODEL = "view_model"
        private const val PARAM_USER_ID = "{user_id}"
        private const val PRODUCT_ID_QUERY_PARAM = "?product_id="
        private const val REQUEST_IMAGE_PICKER = 1234
        private const val REQUEST_VIDEO_PICKER = 1235
        private const val REQUEST_PREVIEW = 13
        private const val REQUEST_LOGIN = 83
        private const val MAX_CHAR = 2000
    }

    override fun initInjector() {
        DaggerCreatePostComponent.builder()
                .createPostModule(CreatePostModule(context!!.applicationContext))
                .build()
                .inject(this)
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
        return inflater.inflate(R.layout.fragment_new_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        initVar(savedInstanceState)
        initView()
        if (userSession.isLoggedIn) {
            fetchContentForm()
        } else {
            context?.let {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQUEST_LOGIN)
            }
        }
    }

    private inline val isPostEnabled: Boolean
        get() = viewModel.completeImageList.isNotEmpty()
                && viewModel.relatedProducts.isNotEmpty()
                && (viewModel.adIdList.isNotEmpty() || viewModel.productIdList.isNotEmpty())

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
        view?.showLoading()
    }

    override fun hideLoading() {
        view?.hideLoading()
    }

    override fun onSuccessGetContentForm(feedContentForm: FeedContentForm) {
        viewModel.token = feedContentForm.token
        viewModel.maxImage = feedContentForm.media.maxMedia
        viewModel.allowImage = feedContentForm.media.allowImage
        viewModel.allowVideo = feedContentForm.media.allowVideo
        viewModel.maxProduct = feedContentForm.maxTag
        viewModel.defaultPlaceholder = feedContentForm.defaultPlaceholder

        if (feedContentForm.media.media.isNotEmpty() && viewModel.fileImageList.isEmpty()) {
            viewModel.urlImageList.clear()
            viewModel.urlImageList.addAll(feedContentForm.media.media.map { MediaModel(it.mediaUrl, it.type) })
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
        //updateAddTagText()
        invalidatePostCallBack?.invalidatePostMenu(isPostEnabled)
        updateCaption()
        updateHeader(feedContentForm.authors)
    }

    override fun onErrorGetContentForm(message: String) {
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

        //updateAddTagText()
    }

    /*protected fun updateAddTagText() {
        context?.let {
            val numberOfProducts = if (isTypeAffiliate()) viewModel.adIdList.size else
                viewModel.productIdList.size
            if (numberOfProducts < viewModel.maxProduct) {
                icon_add_product.setOnClickListener { onAddProduct() }
                label_add_product.setOnClickListener { onAddProduct() }
            } else {
                icon_add_product.setOnClickListener { }
                label_add_product.setOnClickListener {  }
            }
        }
    }*/

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
        product_attachment.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
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
            override fun onDeleteItem(item: MediaItem, position: Int) {
                viewModel.fileImageList.removeAt(position)
                if (position < viewModel.urlImageList.size)
                    viewModel.urlImageList.removeAt(position)

                if (viewModel.completeImageList.isEmpty())
                    fetchContentForm()
            }

            override fun onClickItem(item: MediaItem, position: Int) { goToMediaPreview() }
        })
        updateMediaPreview()
        caption.filters = arrayOf(InputFilter.LengthFilter(MAX_CHAR))
        caption.afterTextChanged {
            viewModel.caption = it
            //updateMaxCharacter()
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
                action_bottom.visible()
            }
        }
        list_captions.adapter = captionsAdapter
        list_captions.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        list_captions.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.dp_8), LinearLayoutManager.HORIZONTAL))
        icon_add_product.setOnClickListener { onAddProduct() }
        label_add_product.setOnClickListener { onAddProduct() }
        //updateMaxCharacter()
        //updateAddTagText()
    }

    private fun updateMediaPreview(){
        val mItems = viewModel.fileImageList.map { MediaItem(thumbnail = it.path, type = it.type) }
        media_attachment.bind(mItems)
        media_attachment.visibility = if (mItems.isEmpty()) View.GONE else View.VISIBLE

    }

    private fun onDefaultCaptionClicked(_caption: String){
        if (caption.text.length + _caption.length <= MAX_CHAR ){
            caption.text.append(_caption)
        }
    }

    /*private fun updateMaxCharacter() {
        maxCharacter.text = String.format(Locale.GERMAN, "%,d/%,d",
                viewModel.caption.length,
                MAX_CHAR
        )
    }*/

    private fun goToVideoPicker() {
        activity?.let {
            startActivityForResult(
                    CreatePostVideoPickerActivity.getInstance(it,
                            viewModel.fileImageList.isNotEmpty()),
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
        if (isTypeAffiliate() && viewModel.adIdList.isEmpty()) {
            isFormInvalid = true
            view?.showErrorToaster(getString(R.string.af_warning_empty_product), R.string.label_add) {
                onRelatedAddProductClick()
            }
        } else if (!isTypeAffiliate() && viewModel.productIdList.isEmpty()) {
            isFormInvalid = true
            view?.showErrorToaster(getString(R.string.af_warning_empty_product), R.string.label_add) {
                onRelatedAddProductClick()
            }
        } else if (viewModel.completeImageList.isEmpty()) {
            isFormInvalid = true
            view?.showErrorToaster(getString(R.string.af_warning_empty_photo), R.string.label_add) {
                goToImagePicker()
            }
        } else if (caption.text.length > MAX_CHAR){
            isFormInvalid = true
            view?.showErrorToaster(getString(R.string.af_warning_over_char, MAX_CHAR.toString()), R.string.general_label_ok){}
        }
        return isFormInvalid
    }

    fun saveDraftAndSubmit() {
        if (isFormInvalid()) {
            return
        }

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
        if (activity is CreatePostActivityListener && authors.isNotEmpty()) {
            (activity as CreatePostActivityListener).updateHeader(HeaderViewModel(
                    authors.first().name,
                    authors.first().thumbnail,
                    authors.first().badge

            ))
        }
    }

    private fun updateCaption() {
        caption.hint = viewModel.defaultPlaceholder
    }

    private fun updateRelatedProduct() {
        adapter.updateProduct(viewModel.relatedProducts)
        if (viewModel.relatedProducts.size > 0){
            product_attachment.visible()
            label_title_product_attachment.visible()
        } else {
            product_attachment.gone()
            label_title_product_attachment.gone()
        }
    }

    interface OnCreatePostCallBack{
        fun invalidatePostMenu(isPostEnabled: Boolean)
    }

    private fun isTypeAffiliate(): Boolean = viewModel.authorType == TYPE_AFFILIATE
}

package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
import com.tokopedia.affiliate.feature.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.listener.CreatePostActivityListener
import com.tokopedia.affiliate.feature.createpost.view.service.SubmitPostService
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videorecorder.main.VideoPickerActivity.Companion.VIDEOS_RESULT
import kotlinx.android.synthetic.main.fragment_af_create_post.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

abstract class BaseCreatePostFragment : BaseDaggerFragment(),
        CreatePostContract.View,
        RelatedProductAdapter.RelatedProductListener {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var affiliateAnalytics: AffiliateAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    protected var viewModel: CreatePostViewModel = CreatePostViewModel()

    private val adapter: RelatedProductAdapter by lazy {
        RelatedProductAdapter(this)
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
        affiliateAnalytics.analyticTracker.sendScreen(activity, screenName)
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
            fetchContentForm()
        } else {
            context?.let {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQUEST_LOGIN)
            }
        }
    }

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

                viewModel.fileImageList.clear()
                viewModel.fileImageList.addAll(images)

                if (imageList.isNotEmpty()) {
                    viewModel.urlImageList.clear()
                }

                updateThumbnail()
                updateButton()
            }
            REQUEST_VIDEO_PICKER -> if (resultCode == Activity.RESULT_OK) {
                val videoList = data?.getStringArrayListExtra(VIDEOS_RESULT) ?: arrayListOf()
                val videos = videoList.map { MediaModel(it, MediaType.VIDEO) }

                viewModel.fileImageList.clear()
                viewModel.fileImageList.addAll(videos)

                if (videoList.isNotEmpty()) {
                    viewModel.urlImageList.clear()
                }

                updateThumbnail()
                updateButton()
            }
            REQUEST_PREVIEW -> if (resultCode == Activity.RESULT_OK) {
                val resultViewModel = data?.getParcelableExtra<CreatePostViewModel>(
                        CreatePostViewModel.TAG
                )
                resultViewModel?.let {
                    viewModel = resultViewModel
                    updateRelatedProduct()
                    updateThumbnail()

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

        if (feedContentForm.media.media.isNotEmpty() && viewModel.fileImageList.isEmpty()) {
            viewModel.urlImageList.clear()
            feedContentForm.media.media.forEach {
                viewModel.urlImageList.add(MediaModel(it.mediaUrl, MediaType.IMAGE))
            }
        }

        if (feedContentForm.relatedItems.isNotEmpty()) {
            viewModel.relatedProducts.clear()

            feedContentForm.relatedItems.forEach {
                viewModel.relatedProducts.add(RelatedProductItem(
                        it.id,
                        it.title,
                        it.price,
                        it.image,
                        viewModel.authorType
                ))
            }
        }
        adapter.notifyDataSetChanged()

        updateMedia()
        updateThumbnail()
        updateHeader(feedContentForm.authors)
    }

    override fun onErrorGetContentForm(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message) {
            fetchContentForm()
        }
    }

    override fun onErrorNotAffiliate() {
        activity?.let {
            val taskStackBuilder = TaskStackBuilder.create(it)

            val onboardingApplink = ApplinkConst.AFFILIATE_ONBOARDING + PRODUCT_ID_QUERY_PARAM + viewModel.productIdList.firstOrNull()
            val onboardingIntent = RouteManager.getIntent(it, onboardingApplink)
            onboardingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            taskStackBuilder.addNextIntent(onboardingIntent)

            val educationIntent = RouteManager.getIntent(
                    it,
                    ApplinkConst.AFFILIATE_EDUCATION)
            taskStackBuilder.addNextIntent(educationIntent)

            taskStackBuilder.startActivities()
            it.finish()
        }
    }

    override fun onErrorNoQuota() {
        activity?.let {
            Toast.makeText(it, R.string.text_full_affiliate_title, Toast.LENGTH_LONG)
                    .show()
            it.finish()
            affiliateAnalytics.onJatahRekomendasiHabisPdp()
        }
    }

    override fun onEmptyProductClick() {
        onRelatedAddProductClick()
    }

    override fun onItemDeleted(position: Int) {
        val relatedProductItem = viewModel.relatedProducts[position]

        viewModel.relatedProducts.removeAt(position)
        adapter.notifyItemRemoved(position)

        if (isTypeAffiliate()) {
            viewModel.adIdList.removeAll { it == relatedProductItem.id }
        } else {
            viewModel.productIdList.removeAll { it == relatedProductItem.id }
        }

        viewModel.urlImageList.removeAll { it.path == relatedProductItem.image }

        updateThumbnail()
        updateButton()
    }

    abstract fun fetchContentForm()

    abstract fun onRelatedAddProductClick()

    protected open fun getAddRelatedProductText(): String = getString(R.string.af_add_product_tag)

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
        val productIds = arguments!!.getString(CreatePostActivity.PARAM_PRODUCT_ID, "").split(',')
        val adIds = arguments!!.getString(CreatePostActivity.PARAM_AD_ID, "").split(',')

        viewModel.productIdList.addAll(productIds)
        viewModel.adIdList.addAll(adIds)
    }

    protected fun updateAddTagText() {
        context?.let {
            val numberOfProducts = if (isTypeAffiliate()) viewModel.adIdList.size else
                viewModel.productIdList.size
            if (numberOfProducts < viewModel.maxProduct) {
                relatedAddBtn.setOnClickListener { onRelatedAddProductClick() }
                relatedAddBtn.setTextColor(MethodChecker.getColor(it, R.color.medium_green))
            } else {
                relatedAddBtn.setOnClickListener { }
                relatedAddBtn.setTextColor(MethodChecker.getColor(it, R.color.af_add_disabled))
            }
        }
    }

    private fun initDraft(arguments: Bundle) {
        activity?.let {
            val draftId = arguments.getString(DRAFT_ID)
            val cacheManager = PersistentCacheManager(it, draftId)
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
        adapter.setList(viewModel.relatedProducts)
        relatedProductRv.adapter = adapter
        relatedProductRv.setHasFixedSize(true)
        doneBtn.setOnClickListener {
            affiliateAnalytics.onSelesaiCreateButtonClicked(viewModel.productIdList.firstOrNull())
            saveDraftAndSubmit()
        }
        addImageBtn.setOnClickListener {
            affiliateAnalytics.onTambahGambarButtonClicked(viewModel.productIdList.firstOrNull())
            goToImagePicker()
        }
        caption.afterTextChanged {
            viewModel.caption = it
        }
        addVideoBtn.setOnClickListener {
            goToVideoPicker()
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
        caption.hint = getString(if (isTypeAffiliate())
            R.string.af_caption_hint_affiliate else
            R.string.af_caption_hint
        )
        caption.setText(viewModel.caption)
        updateMaxCharacter()
        updateThumbnail()
        updateAddTagText()
    }

    private fun updateMaxCharacter() {
        maxCharacter.text = String.format(Locale.GERMAN, "%,d/%,d",
                viewModel.caption.length,
                MAX_CHAR
        )
    }

    private fun goToVideoPicker() {
        activity?.let {
            startActivityForResult(
                    CreatePostVideoPickerActivity.getInstance(it,
                            viewModel.fileImageList.isNotEmpty()),
                    REQUEST_VIDEO_PICKER)
        }
    }

    private fun goToImagePicker() {
        affiliateAnalytics.onTambahGambarButtonClicked(viewModel.productIdList.firstOrNull())
        activity?.let {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            it,
                            ArrayList(viewModel.fileImageList),
                            viewModel.maxImage - viewModel.urlImageList.size,
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
        }
        return isFormInvalid
    }

    private fun saveDraftAndSubmit() {
        affiliateAnalytics.onSelesaiCreateButtonClicked(viewModel.productIdList.firstOrNull())

        if (isFormInvalid()) {
            return
        }

        activity?.let {
            showLoading()

            val cacheManager = PersistentCacheManager(it, true)
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
        val shouldShowVideo = viewModel.allowVideo && false
        addImageBtn.showWithCondition(viewModel.allowImage)
        addVideoBtn.showWithCondition(true) //testing purpose
        separatorMedia.showWithCondition(viewModel.allowImage || shouldShowVideo)
    }

    private fun updateThumbnail() {
        //testing purpose
        thumbnail.setOnClickListener {
            goToMediaPreview()
        }

        if (viewModel.completeImageList.isNotEmpty()) {
            thumbnail.loadImageRounded(viewModel.completeImageList.first().path?:"", 25f)
            edit.show()
            carouselIcon.setOnClickListener {
                goToMediaPreview()
            }
            edit.setOnClickListener {
                goToMediaPreview()
            }
        } else {
            thumbnail.loadDrawable(R.drawable.ic_system_action_addimage_grayscale_62)
            edit.hide()
            thumbnail.setOnClickListener { }
            carouselIcon.setOnClickListener { }
            edit.setOnClickListener { }
        }
        carouselIcon.showWithCondition(viewModel.completeImageList.size > 1)
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

    private fun updateButton() {
        val isButtonEnabled = viewModel.completeImageList.isNotEmpty()
                && viewModel.relatedProducts.isNotEmpty()
                && (viewModel.adIdList.isNotEmpty() || viewModel.productIdList.isNotEmpty())
        doneBtn.isEnabled = isButtonEnabled
    }

    private fun updateRelatedProduct() {
        adapter.setList(viewModel.relatedProducts)
    }

    private fun isTypeAffiliate(): Boolean = viewModel.authorType == TYPE_AFFILIATE
}

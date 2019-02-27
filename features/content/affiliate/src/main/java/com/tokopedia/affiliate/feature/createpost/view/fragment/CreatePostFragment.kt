package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.TaskStackBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.analytics.AffiliateAnalytics
import com.tokopedia.affiliate.analytics.AffiliateEventTracking
import com.tokopedia.affiliate.feature.createpost.DRAFT_ID
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.MediaPreviewActivity
import com.tokopedia.affiliate.feature.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.service.SubmitPostService
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_af_create_post.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CreatePostFragment : BaseDaggerFragment(),
        CreatePostContract.View,
        RelatedProductAdapter.RelatedProductListener {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var affiliateAnalytics: AffiliateAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewModel: CreatePostViewModel = CreatePostViewModel()

    private val adapter: RelatedProductAdapter by lazy {
        RelatedProductAdapter(this)
    }

    companion object {

        private const val VIEW_MODEL = "view_model"
        private const val PARAM_USER_ID = "{user_id}"
        private const val PRODUCT_ID_QUERY_PARAM = "?product_id="
        private const val REQUEST_IMAGE_PICKER = 1234
        private const val REQUEST_EXAMPLE = 13
        private const val REQUEST_LOGIN = 83
        private const val REQUEST_ATTACH_PRODUCT = 10

        fun createInstance(bundle: Bundle): CreatePostFragment {
            val fragment = CreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {
        val baseAppComponent = (activity!!.application as BaseMainApplication)
                .baseAppComponent
        DaggerCreatePostComponent.builder()
                .baseAppComponent(baseAppComponent)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        initView()
        if (userSession.isLoggedIn) {
            //TODO milhamj handle multiple product or ad id
            presenter.fetchContentForm(viewModel.productIdList.firstOrNull(), viewModel.adIdList.firstOrNull())
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
                viewModel.fileImageList.clear()
                viewModel.fileImageList.addAll(imageList)
                updateThumbnail()
            }
            REQUEST_EXAMPLE -> goToImagePicker()
            REQUEST_LOGIN -> presenter.fetchContentForm(viewModel.productIdList.firstOrNull(), viewModel.adIdList.firstOrNull())
            REQUEST_ATTACH_PRODUCT -> if (resultCode == AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK) {
                val products = data?.getParcelableArrayListExtra<ResultProduct>(
                        AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                        ?: arrayListOf()
                viewModel.relatedProducts.clear()
                viewModel.relatedProducts.addAll(convertAttachProduct(products))
                updateRelatedProduct()
            }
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
        if (!feedContentForm.media.media.isEmpty()) {
            if (viewModel.urlImageList.isEmpty()) {
                viewModel.urlImageList.clear()
                feedContentForm.media.media.forEach {
                    viewModel.urlImageList.add(it.mediaUrl)
                }
            }
        }
        updateThumbnail()
    }

    override fun onErrorGetContentForm(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message
        ) { presenter.fetchContentForm(viewModel.productIdList.firstOrNull(), viewModel.adIdList.firstOrNull()) }
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

    override fun onSuccessSubmitPost() {
        goToProfile()
        activity?.finish()
    }

    override fun onErrorSubmitPost(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message) { this.submitPost() }
    }

    override fun onEmptyProductClick() {
        goToAttachProduct()
    }

    private fun initVar(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: CreatePostViewModel()
        } else if (arguments != null) {
            if (arguments!!.getString(DRAFT_ID) != null) {
                initDraft(arguments!!)
            } else {
                viewModel.productIdList.add(arguments!!.getString(CreatePostActivity.PARAM_PRODUCT_ID, ""))
                viewModel.adIdList.add(arguments!!.getString(CreatePostActivity.PARAM_AD_ID, ""))
                viewModel.postId = arguments!!.getString(CreatePostActivity.PARAM_POST_ID, "")
            }
        } else {
            activity?.finish()
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
            } else {
                it.finish()
            }
        }
    }

    private fun initView() {
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
        relatedAddBtn.setOnClickListener {
            goToAttachProduct()
        }
        thumbnail.setOnClickListener {
            goToMediaPreview()
        }
    }

    private fun goToImagePicker() {
        activity?.let {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            it,
                            ArrayList(viewModel.fileImageList),
                            viewModel.maxImage - viewModel.urlImageList.size
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

    private fun goToMediaPreview() {
        context?.let {
            startActivity(MediaPreviewActivity.createIntent(it, viewModel))
        }
    }


    private fun submitPost() {
        presenter.submitPost(
                viewModel.productIdList.firstOrNull(),
                viewModel.productIdList.firstOrNull(),
                viewModel.token,
                viewModel.completeImageList,
                viewModel.mainImageIndex
        )
    }

    private fun saveDraftAndSubmit() {
        activity?.let {
            showLoading()

            val cacheManager = PersistentCacheManager(it, true)
            cacheManager.put(CreatePostViewModel.TAG, viewModel, TimeUnit.DAYS.toMillis(7))

            val intent = SubmitPostService.createIntent(it, cacheManager.id!!)
            it.startService(intent)

            hideLoading()
            goToProfile()
            it.finish()
        }
    }

    private fun updateThumbnail() {
        if (viewModel.completeImageList.isNotEmpty()) {
            thumbnail.loadImageRounded(viewModel.completeImageList.first(), 25f)
            carouselIcon.showWithCondition(viewModel.completeImageList.size > 1)
        } else {
            thumbnail.loadDrawable(R.drawable.ic_system_action_addimage_grayscale_62)
        }
    }

    private fun goToAttachProduct() {
        val intent = AttachProductActivity.createInstance(context,
                userSession.shopId,
                "",
                true,
                "")
        startActivityForResult(intent, REQUEST_ATTACH_PRODUCT)
    }

    private fun updateRelatedProduct() {
        adapter.setList(viewModel.relatedProducts)
    }

    private fun convertAttachProduct(attachedProducts: MutableList<ResultProduct>): MutableList<RelatedProductItem> {
        val relatedProducts = arrayListOf<RelatedProductItem>()
        attachedProducts.forEach {
            relatedProducts.add(RelatedProductItem(
                    it.productId.toString(),
                    it.name,
                    it.price,
                    it.productImageThumbnail)
            )
        }
        return relatedProducts
    }
}
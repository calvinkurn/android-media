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
import com.tokopedia.affiliate.common.preference.AffiliatePreference
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity
import com.tokopedia.affiliate.feature.createpost.view.adapter.RelatedProductAdapter
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_af_create_post.*
import javax.inject.Inject

class CreatePostFragment : BaseDaggerFragment(),
        CreatePostContract.View,
        RelatedProductAdapter.RelatedProductListener {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var affiliatePreference: AffiliatePreference

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
            if (!viewModel.isEdit) {
                presenter.fetchContentForm(viewModel.productId, viewModel.adId)
            } else {
                presenter.fetchEditContentForm(viewModel.postId)
            }
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
                viewModel.fileImageList = data?.getStringArrayListExtra(PICKER_RESULT_PATHS)
                        ?: arrayListOf()
                updateThumbnail()
            }
            REQUEST_EXAMPLE -> goToImagePicker()
            REQUEST_LOGIN -> presenter.fetchContentForm(viewModel.productId, viewModel.adId)
            REQUEST_ATTACH_PRODUCT -> {
                val products = data?.getParcelableArrayListExtra<ResultProduct>(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                        ?: arrayListOf()
                setRelatedProduct(convertAttachProduct(products))
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
        ) { presenter.fetchContentForm(viewModel.productId, viewModel.adId) }
    }

    override fun onErrorGetEditContentForm(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message
        ) { presenter.fetchEditContentForm(viewModel.postId) }
    }

    override fun onErrorNotAffiliate() {
        activity?.let {
            val taskStackBuilder = TaskStackBuilder.create(it)

            val onboardingApplink = ApplinkConst.AFFILIATE_ONBOARDING + PRODUCT_ID_QUERY_PARAM + viewModel.productId
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

    override fun onErrorEditPost(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message) { this.editPost() }
    }

    override fun onEmptyProductClick() {
        goToAttachProduct()
    }

    private fun initVar(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: CreatePostViewModel()
        } else if (arguments != null) {
            viewModel.productId = arguments!!.getString(CreatePostActivity.PARAM_PRODUCT_ID, "")
            viewModel.adId = arguments!!.getString(CreatePostActivity.PARAM_AD_ID, "")
            viewModel.postId = arguments!!.getString(CreatePostActivity.PARAM_POST_ID, "")
            viewModel.isEdit = arguments!!.getBoolean(CreatePostActivity.PARAM_IS_EDIT, false)
        }
    }

    private fun initView() {
        relatedProductRv.adapter = adapter
        doneBtn.setOnClickListener {
            affiliateAnalytics.onSelesaiCreateButtonClicked(viewModel.productId)
//            if (!viewModel.isEdit) {
//                submitPost()
//            } else {
//                editPost()
//            }
        }
        addImageBtn.setOnClickListener {
            affiliateAnalytics.onTambahGambarButtonClicked(viewModel.productId)
            goToImagePicker()
        }
        relatedAddBtn.setOnClickListener {
            goToAttachProduct()
        }
    }

    private fun shouldShowExample(): Boolean {
        return affiliatePreference.isFirstTimeCreatePost(userSession.userId)
    }

    private fun goToImagePicker() {
        activity?.let {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            it,
                            viewModel.fileImageList,
                            viewModel.maxImage - viewModel.urlImageList.size
                    ),
                    REQUEST_IMAGE_PICKER)
        }
    }

    private fun goToProfile() {
        activity?.let {
            var profileApplink = if (viewModel.isEdit)
                ApplinkConst.PROFILE_AFTER_EDIT
            else
                ApplinkConst.PROFILE_AFTER_POST
            profileApplink = profileApplink.replace(PARAM_USER_ID, userSession.userId)
            val intent = RouteManager.getIntent(
                    it,
                    profileApplink
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun submitPost() {
        presenter.submitPost(
                viewModel.productId,
                viewModel.adId,
                viewModel.token,
                viewModel.completeImageList,
                viewModel.mainImageIndex
        )
    }

    private fun editPost() {
        presenter.editPost(
                viewModel.postId,
                viewModel.token,
                viewModel.completeImageList,
                viewModel.mainImageIndex
        )
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
                AttachProductActivity.SOURCE_TALK)
        startActivityForResult(intent, REQUEST_ATTACH_PRODUCT)
    }

    private fun setRelatedProduct(products: MutableList<RelatedProductItem>) {
        adapter.addAll(products)
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
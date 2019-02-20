package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.TaskStackBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.analytics.AffiliateAnalytics
import com.tokopedia.affiliate.analytics.AffiliateEventTracking
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Guide
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Medium
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostExampleActivity
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.common.preference.AffiliatePreference
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter
import com.tokopedia.affiliatecommon.view.widget.WrapContentViewPager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.user.session.UserSession

import javax.inject.Inject

import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import kotlinx.android.synthetic.main.fragment_af_create_post.*

class CreatePostFragment : BaseDaggerFragment(), CreatePostContract.View {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var affiliatePreference: AffiliatePreference

    @Inject
    lateinit var affiliateAnalytics: AffiliateAnalytics

    private var viewModel: CreatePostViewModel? = null
    private var adapter: PostImageAdapter? = null
    private var guide: Guide? = null

    companion object {

        private const val VIEW_MODEL = "view_model"
        private const val PARAM_USER_ID = "{user_id}"
        private const val PRODUCT_ID_QUERY_PARAM = "?product_id="
        private const val REQUEST_IMAGE_PICKER = 1234
        private const val REQUEST_EXAMPLE = 13
        private const val REQUEST_LOGIN = 83

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
            if (!viewModel!!.isEdit) {
                presenter.fetchContentForm(viewModel!!.productId, viewModel!!.adId)
            } else {
                presenter.fetchEditContentForm(viewModel!!.postId)
            }
        } else {
            startActivityForResult(
                    RouteManager.getIntent(
                            context!!,
                            ApplinkConst.LOGIN
                    ),
                    REQUEST_LOGIN
            )
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
                viewModel!!.fileImageList = data!!.getStringArrayListExtra(PICKER_RESULT_PATHS)
                setupViewPager()
            }
            REQUEST_EXAMPLE -> goToImagePicker()
            REQUEST_LOGIN -> presenter.fetchContentForm(viewModel!!.productId, viewModel!!.adId)
            else -> {
            }
        }
    }

    override fun getUserSession(): UserSession {
        return UserSession(context)
    }

    override fun showLoading() {
        view?.showLoading()
    }

    override fun hideLoading() {
        view?.hideLoading()
    }

    override fun onSuccessGetContentForm(feedContentForm: FeedContentForm) {
        viewModel!!.token = feedContentForm.token
        viewModel!!.maxImage = feedContentForm.media.maxMedia
        if (!feedContentForm.guides.isEmpty()) {
            setupHeader(feedContentForm.guides[0])
        }
        if (!feedContentForm.media.media.isEmpty()) {
            if (viewModel!!.urlImageList.isEmpty()) {
                viewModel!!.urlImageList.clear()
                for (i in 0 until feedContentForm.media.media.size) {
                    val medium = feedContentForm.media.media[i]
                    viewModel!!.urlImageList.add(medium.mediaUrl)
                }
            }
            setupViewPager()
            setMain!!.visibility = if (adapter!!.count > 1) View.VISIBLE else View.INVISIBLE
            deleteImageLayout!!.visibility = if (adapter!!.count > 1) View.VISIBLE else View.GONE
        }
        updateSetMainView()
    }

    override fun onErrorGetContentForm(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message
        ) { presenter.fetchContentForm(viewModel!!.productId, viewModel!!.adId) }
    }

    override fun onErrorGetEditContentForm(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message
        ) { presenter.fetchEditContentForm(viewModel!!.postId) }
    }

    override fun onErrorNotAffiliate() {
        if (activity != null) {
            val taskStackBuilder = TaskStackBuilder.create(activity!!)

            val onboardingApplink = ApplinkConst.AFFILIATE_ONBOARDING + PRODUCT_ID_QUERY_PARAM + viewModel!!.productId
            val onboardingIntent = RouteManager.getIntent(activity!!, onboardingApplink)
            onboardingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            taskStackBuilder.addNextIntent(onboardingIntent)

            val educationIntent = RouteManager.getIntent(
                    activity!!,
                    ApplinkConst.AFFILIATE_EDUCATION)
            taskStackBuilder.addNextIntent(educationIntent)

            taskStackBuilder.startActivities()
            activity!!.finish()
        }
    }

    override fun onErrorNoQuota() {
        if (activity != null) {
            Toast.makeText(activity, R.string.text_full_affiliate_title, Toast.LENGTH_LONG)
                    .show()
            activity!!.finish()
            affiliateAnalytics.onJatahRekomendasiHabisPdp()
        }
    }

    override fun onSuccessSubmitPost() {
        goToProfile()
        activity!!.finish()
    }

    override fun onErrorSubmitPost(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message) { this.submitPost() }
    }

    override fun onErrorEditPost(message: String) {
        NetworkErrorHelper.showEmptyState(context, mainView, message) { this.editPost() }
    }

    private fun initVar(savedInstanceState: Bundle?) {
        if (viewModel == null) {
            viewModel = CreatePostViewModel()
        }
        if (adapter == null) {
            adapter = PostImageAdapter()
        }

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(VIEW_MODEL)
        } else if (arguments != null) {
            viewModel!!.productId = arguments!!.getString(CreatePostActivity.PARAM_PRODUCT_ID, "")
            viewModel!!.adId = arguments!!.getString(CreatePostActivity.PARAM_AD_ID, "")
            viewModel!!.postId = arguments!!.getString(CreatePostActivity.PARAM_POST_ID, "")
            viewModel!!.isEdit = arguments!!.getBoolean(CreatePostActivity.PARAM_IS_EDIT, false)
        }
    }

    private fun initView() {
        doneBtn!!.setOnClickListener {
            affiliateAnalytics.onSelesaiCreateButtonClicked(viewModel!!.productId)
            if (!viewModel!!.isEdit) {
                submitPost()
            } else {
                editPost()
            }
        }
        addImageBtn!!.setOnClickListener {
            affiliateAnalytics.onTambahGambarButtonClicked(viewModel!!.productId)
            if (shouldShowExample()) {
                goToImageExample(true)
                affiliatePreference.setFirstTimeCreatePost(userSession.userId)
            } else {
                goToImagePicker()
            }
        }
        deleteImageLayout!!.setOnClickListener {
            if (viewModel!!.mainImageIndex == tabLayout!!.selectedTabPosition && tabLayout!!.selectedTabPosition == adapter!!.count - 2) {
                if (tabLayout!!.selectedTabPosition - 1 > 0) {
                    viewModel!!.mainImageIndex = tabLayout!!.selectedTabPosition - 1
                } else {
                    viewModel!!.mainImageIndex = 0
                }
                updateSetMainView()
            }

            if (tabLayout!!.selectedTabPosition < viewModel!!.fileImageList.size) {
                viewModel!!.fileImageList.removeAt(tabLayout!!.selectedTabPosition)
            } else {
                viewModel!!.urlImageList.removeAt(
                        tabLayout!!.selectedTabPosition - viewModel!!.fileImageList.size
                )
            }
            adapter!!.imageList.removeAt(tabLayout!!.selectedTabPosition)
            adapter!!.notifyDataSetChanged()
        }
        setMain!!.setOnClickListener {
            viewModel!!.mainImageIndex = tabLayout!!.selectedTabPosition
            updateSetMainView()
        }
    }

    private fun shouldShowExample(): Boolean {
        return affiliatePreference.isFirstTimeCreatePost(userSession.userId)
    }

    private fun setupHeader(guide: Guide) {
        this.guide = guide
        title!!.text = guide.header
        seeExample!!.text = guide.moreText
        seeExample!!.setOnClickListener {
            affiliateAnalytics.onLihatContohButtonClicked(viewModel!!.productId)
            goToImageExample(false)
        }
    }

    private fun setupViewPager() {
        adapter!!.setList(viewModel!!.completeImageList)
        imageViewPager!!.adapter = adapter
        imageViewPager!!.offscreenPageLimit = adapter!!.count
        tabLayout!!.setupWithViewPager(imageViewPager)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                deleteImageLayout!!.visibility = if (tab.position == adapter!!.count - 1)
                    View.GONE
                else
                    View.VISIBLE
                setMain!!.visibility = if (tab.position == adapter!!.count - 1)
                    View.INVISIBLE
                else
                    View.VISIBLE
                updateSetMainView()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun updateSetMainView() {
        if (viewModel!!.mainImageIndex == tabLayout!!.selectedTabPosition) {
            setMain!!.setText(R.string.af_main_image)
            setMain!!.setTextColor(MethodChecker.getColor(context, R.color.black_38))
            setMain!!.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    MethodChecker.getDrawable(context, R.drawable.ic_af_check_gray), null
            )
        } else {
            setMain!!.setText(R.string.af_set_main_image)
            setMain!!.setTextColor(MethodChecker.getColor(context, R.color.medium_green))
            setMain!!.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    private fun goToImageExample(needResult: Boolean) {
        val intent = CreatePostExampleActivity.createIntent(
                context,
                guide?.imageUrl ?: "",
                guide?.imageDescription ?: ""
        )

        if (needResult) {
            startActivityForResult(intent, REQUEST_EXAMPLE)
        } else {
            startActivity(intent)
        }
    }

    private fun goToImagePicker() {
        activity?.let {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            it,
                            viewModel!!.fileImageList,
                            viewModel!!.maxImage - viewModel!!.urlImageList.size
                    ),
                    REQUEST_IMAGE_PICKER)
        }
    }

    private fun goToProfile() {
        activity?.let {
            var profileApplink = if (viewModel!!.isEdit)
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
                viewModel!!.productId,
                viewModel!!.adId,
                viewModel!!.token,
                viewModel!!.completeImageList,
                viewModel!!.mainImageIndex
        )
    }

    private fun editPost() {
        presenter.editPost(
                viewModel!!.postId,
                viewModel!!.token,
                viewModel!!.completeImageList,
                viewModel!!.mainImageIndex
        )
    }
}
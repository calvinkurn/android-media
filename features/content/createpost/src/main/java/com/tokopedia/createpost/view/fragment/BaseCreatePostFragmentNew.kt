package com.tokopedia.createpost.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.data.pojo.getcontentform.Author
import com.tokopedia.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.domain.entity.FeedDetail
import com.tokopedia.createpost.view.activity.PARAM_POST_ID
import com.tokopedia.createpost.view.activity.PARAM_TYPE
import com.tokopedia.createpost.view.contract.CreatePostContract
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.type.ShareType
import com.tokopedia.createpost.view.viewmodel.CreateContentPostViewModel
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.createpost.view.viewmodel.MediaModel
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.twitter_share.TwitterAuthenticator
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

abstract class BaseCreatePostFragmentNew : BaseDaggerFragment(),
    CreatePostContract.View {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var affiliateAnalytics: AffiliateAnalytics

    protected var createPostModel: CreatePostViewModel = CreatePostViewModel()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected val createContentPostViewModel: CreateContentPostViewModel by lazy {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory)
        viewModelProvider.get(CreateContentPostViewModel::class.java)
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private val activityListener: CreateContentPostCOmmonLIstener? by lazy {
        activity as? CreateContentPostCOmmonLIstener
    }
    companion object {
        private const val VIEW_MODEL = "view_model"
    }
    abstract fun fetchContentForm()


    override fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostModule(CreatePostModule(requireContext().applicationContext))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)

        initVar(savedInstanceState)
            fetchContentForm()

    }
    protected open fun initVar(savedInstanceState: Bundle?) {
        when {
            savedInstanceState != null -> {
                createPostModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: CreatePostViewModel()
                createContentPostViewModel.setNewContentData(createPostModel)
            }
            arguments != null -> {
                createPostModel.postId = requireArguments().getString(PARAM_POST_ID, "")
                createPostModel.authorType = requireArguments().getString(PARAM_TYPE, "")
            }
            else -> {
                activity?.finish()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createContentPostViewModel._createPostViewModelData.observe(viewLifecycleOwner, Observer {
            createPostModel = it
        })
    }



    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
    override fun showLoading() {
        view?.showLoading()
    }

    override fun hideLoading() {
        view?.hideLoading()
    }

    override fun onSuccessGetContentForm(
        feedContentForm: FeedContentForm,
        isFromTemplateToken: Boolean,
    ) {
        updateHeader(feedContentForm.authors)

        createPostModel.token = feedContentForm.token
        createPostModel.maxImage = feedContentForm.media.maxMedia
        createPostModel.allowImage = feedContentForm.media.allowImage
        createPostModel.allowVideo = feedContentForm.media.allowVideo
        createPostModel.maxProduct = feedContentForm.maxTag
        createPostModel.defaultPlaceholder = feedContentForm.defaultPlaceholder
        if (createPostModel.caption.isEmpty()) createPostModel.caption = feedContentForm.caption

        if (feedContentForm.media.media.isNotEmpty() && createPostModel.fileImageList.isEmpty()) {
            createPostModel.urlImageList.clear()
            createPostModel.urlImageList.addAll(feedContentForm.media.media.map {
                MediaModel(it.mediaUrl,
                    it.type)
            })
        }
        createContentPostViewModel.setNewContentData(createPostModel)

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(VIEW_MODEL, createPostModel)
    }
    private fun updateHeader(authors: List<Author>) {
            activityListener?.updateHeader(HeaderViewModel(
                authors.first().name,
                authors.first().thumbnail,
                authors.first().badge
            ))

    }


    override fun onErrorGetContentForm(message: String) {
//        NetworkErrorHelper.showEmptyState(context, main_view, message) {
//            fetchContentForm()
//        }
    }

    override fun onErrorNoQuota() {
        TODO("Not yet implemented")
    }

    override fun onSuccessGetPostEdit(feedDetail: FeedDetail) {
        hideLoading()
    }

    override fun onErrorGetPostEdit(e: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun onGetAvailableShareTypeList(typeList: List<ShareType>) {
        TODO("Not yet implemented")
    }

    override fun onAuthenticateTwitter(authenticator: TwitterAuthenticator) {
        TODO("Not yet implemented")
    }

    override fun changeShareHeaderText(text: String) {
        TODO("Not yet implemented")
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }



}
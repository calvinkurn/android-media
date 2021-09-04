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

    protected var viewModel: CreatePostViewModel = CreatePostViewModel()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var createContentViewModelPresenter: CreateContentPostViewModel



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
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            createContentViewModelPresenter = viewModelProvider.get(CreateContentPostViewModel::class.java)
        }
        initVar(savedInstanceState)
            fetchContentForm()

    }
    protected open fun initVar(savedInstanceState: Bundle?) {
        when {
            savedInstanceState != null -> {
                viewModel = savedInstanceState.getParcelable(VIEW_MODEL) ?: CreatePostViewModel()
                createContentViewModelPresenter.setNewContentData(viewModel)
            }
            arguments != null -> {
                viewModel.postId = requireArguments().getString(PARAM_POST_ID, "")
                viewModel.authorType = requireArguments().getString(PARAM_TYPE, "")
            }
            else -> {
                activity?.finish()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createContentViewModelPresenter._createPostViewModelData.observe(this, Observer {
            viewModel = it
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

        viewModel.token = feedContentForm.token
        viewModel.maxImage = feedContentForm.media.maxMedia
        viewModel.allowImage = feedContentForm.media.allowImage
        viewModel.allowVideo = feedContentForm.media.allowVideo
        viewModel.maxProduct = feedContentForm.maxTag
        viewModel.defaultPlaceholder = feedContentForm.defaultPlaceholder
        if (viewModel.caption.isEmpty()) viewModel.caption = feedContentForm.caption

        if (feedContentForm.media.media.isNotEmpty() && viewModel.fileImageList.isEmpty()) {
            viewModel.urlImageList.clear()
            viewModel.urlImageList.addAll(feedContentForm.media.media.map {
                MediaModel(it.mediaUrl,
                    it.type)
            })
        }
        createContentViewModelPresenter.setNewContentData(viewModel)

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(VIEW_MODEL, viewModel)
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
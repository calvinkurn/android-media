package com.tokopedia.createpost.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.createpost.view.viewmodel.CreateContentPostViewModel
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.common.view.viewmodel.MediaModel
import com.tokopedia.createpost.view.activity.CreatePostActivityNew.Companion.PARAM_POST_ID
import com.tokopedia.createpost.view.activity.CreatePostActivityNew.Companion.PARAM_TYPE
import com.tokopedia.createpost.view.util.ConnectionLiveData
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.feedcomponent.bottomsheets.FeedNetworkErrorBottomSheet
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseCreatePostFragmentNew : BaseDaggerFragment(),
    CreatePostContract.View {

    @Inject
    lateinit var presenter: CreatePostContract.Presenter

    @Inject
    lateinit var createPostAnalytics: CreatePostAnalytics

    protected var createPostModel: CreatePostViewModel = CreatePostViewModel()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var createContentPostViewModel: CreateContentPostViewModel
    private lateinit var sheet :FeedNetworkErrorBottomSheet


    @Inject
    lateinit var userSession: UserSessionInterface

    val activityListener: CreateContentPostCommonListener? by lazy {
        activity as? CreateContentPostCommonListener
    }
    companion object {
        private const val VIEW_MODEL = "view_model"
    }
    abstract fun fetchContentForm()


    override fun initInjector() {
        DaggerCreatePostComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(requireContext().applicationContext))
            .createPostModule(CreatePostModule(requireContext().applicationContext)).build()
            .inject(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            createContentPostViewModel = viewModelProvider.get(CreateContentPostViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpConnectionListener()

        presenter.attachView(this)
        initVar(savedInstanceState)
        fetchContentForm()
    }
    private fun setUpConnectionListener(){
        val connectionLiveData = context?.let { ConnectionLiveData(it) }
        if (!(::sheet.isInitialized)) {
            sheet = FeedNetworkErrorBottomSheet.newInstance(true)
        }
        connectionLiveData?.observe(context as AppCompatActivity) {
            if (!it) {
                sheet.onRetry = {
                    fetchContentForm()
                }
                sheet.show(childFragmentManager, "")
            } else {
                if (sheet.isVisible)
                    sheet.dismiss()
            }
        }
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
        val feedAccountList = feedContentForm.authors.map {
            ContentAccountUiModel(
                id = it.id,
                name = it.name,
                iconUrl = it.thumbnail,
                badge = it.badge,
                type = it.type,
                hasUsername = feedContentForm.hasUsername,
                hasAcceptTnc = feedContentForm.hasAcceptTnc,
            )
        }

        activityListener?.setContentAccountList(feedAccountList)
        createPostModel.shopName = feedAccountList.firstOrNull { it.isShop }?.name ?: ""
        createPostModel.shopBadge = feedAccountList.firstOrNull { it.isShop }?.badge ?: ""
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
        createPostModel.productTagSources = feedContentForm.productTagSources
        createContentPostViewModel.setNewContentData(createPostModel)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(VIEW_MODEL, createPostModel)
    }

    override fun onErrorGetContentForm(message: String, throwable: Throwable?) {
        throwable?.let {
            if (it is UnknownHostException || it is ConnectException || it is SocketTimeoutException){
                context?.let { it1 -> showNoConnectionBottomSheet(it1) }

            }
        }

    }

    private fun showNoConnectionBottomSheet(context: Context) {
        sheet = FeedNetworkErrorBottomSheet.newInstance(true)
        sheet.onRetry = {
            fetchContentForm()
        }
        sheet.show((context as FragmentActivity).supportFragmentManager, "")

    }

    override fun getScreenName(): String {
        return ""
    }

    fun getLatestCreatePostData(): CreatePostViewModel {
        return createContentPostViewModel.getPostData() ?: CreatePostViewModel()
    }
}

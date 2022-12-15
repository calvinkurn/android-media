package com.tokopedia.kol.feature.comment.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.bottomsheets.MenuOptionsBottomSheet
import com.tokopedia.feedcomponent.bottomsheets.ReportBottomSheet
import com.tokopedia.feedcomponent.util.MentionTextHelper.createValidMentionText
import com.tokopedia.feedcomponent.view.adapter.mention.MentionableUserAdapter
import com.tokopedia.feedcomponent.view.adapter.mention.MentionableUserAdapter.MentionAdapterListener
import com.tokopedia.feedcomponent.view.custom.MentionEditText
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.di.DaggerKolCommentComponent
import com.tokopedia.kol.feature.comment.di.KolCommentModule
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain
import com.tokopedia.kol.feature.comment.view.activity.KolCommentNewActivity
import com.tokopedia.kol.feature.comment.view.adapter.KolCommentAdapter
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_AUTHOR_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_ID
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_IS_POST_FOLLOWED
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_POST_TYPE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARGS_VIDEO
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.ARG_IS_FROM_CONTENT_DETAIL_PAGE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.COMMENT_ARGS_TOTAL_COMMENT
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailArgumentModel.Companion.CONTENT_DETAIL_PAGE_SOURCE
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailPageAnalyticsDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject

private const val REQUEST_LOGIN = 345
private const val TOASTER_DURATION_DELETE_COMMENT = 3000

class KolCommentNewFragment : BaseDaggerFragment(), KolComment.View, KolComment.View.ViewHolder,
    MentionAdapterListener {

    private var adapter: KolCommentAdapter? = null
    private var header: KolCommentHeaderNewModel? = null
    private var listComment: RecyclerView? = null
    private var sendButton: ImageView? = null
    private var userSession: UserSessionInterface? = null
    private var kolComment: MentionEditText? = null
    private var avatarShop: ImageView? = null
    private var mentionAdapter: MentionableUserAdapter? = null
    private var totalNewComment = 0
    private lateinit var globalError: GlobalError
    var postId: String = "0"
    private var authorId: String = "0"
    private var isVideoPost: Boolean = false
    private var isFollowed: Boolean = true
    private var postType: String = ""
    private var contentDetailSource: String = ""
    private var isFromContentDetailPage: Boolean = false

    @Inject
    internal lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var analyticsTracker: ContentDetailNewPageAnalytics

    @Inject
    lateinit var presenter: KolComment.Presenter

    @Inject
    lateinit var typeFactory: KolCommentTypeFactory

    private lateinit var reportBottomSheet: ReportBottomSheet

    override fun getScreenName(): String {
        return KolCommentNewFragment::class.java.name
    }

    override fun initInjector() {
        DaggerKolCommentComponent.builder()
            .kolComponent(KolComponentInstance.getKolComponent(activity?.application))
            .kolCommentModule(KolCommentModule(this, this))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val parentView: View = inflater.inflate(R.layout.fragment_kol_comment_new, container, false)
        listComment = parentView.findViewById(R.id.comment_list)
        kolComment = parentView.findViewById(R.id.new_comment)
        avatarShop = parentView.findViewById(R.id.avatar_shop)
        globalError = parentView.findViewById(R.id.globalError)
        sendButton = parentView.findViewById(R.id.send_but)
        prepareView()
        presenter.attachView(this)
        return parentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        userSession = UserSession(activity)
        totalNewComment = 0
        postId = (arguments?.getInt(ARGS_ID) ?: 0).toString()
        authorId = arguments?.getString(ARGS_AUTHOR_TYPE) ?: "0"
        isVideoPost = arguments?.getBoolean(ARGS_VIDEO) ?: false
        isFollowed = arguments?.getBoolean(ARGS_IS_POST_FOLLOWED) ?: false
        postType = arguments?.getString(ARGS_POST_TYPE) ?: "0"
        contentDetailSource = arguments?.getString(CONTENT_DETAIL_PAGE_SOURCE) ?: ""
        isFromContentDetailPage = arguments?.getBoolean(ARG_IS_FROM_CONTENT_DETAIL_PAGE) ?: false


    }

    companion object {
        fun createInstance(bundle: Bundle?): KolCommentNewFragment {
            val fragment = KolCommentNewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSuccessChangeWishlist() {
    }

    override fun onServerErrorGetCommentsFirstTime(errorMessage: String?) {
        removeLoading()
        showError(false) {
            presenter.getCommentFirstTime(
                requireArguments().getLong(ARGS_ID)
            )
        }
    }

    override fun onErrorDeleteComment(errorMessage: String?) {
        showToastMessage(getString(R.string.kol_deleting_comment_error))
    }

    override fun onDeleteCommentKol(
        id: String?,
        canDeleteComment: Boolean,
        adapterPosition: Int
    ): Boolean {
        if (canDeleteComment || isInfluencer()) {
            if (userSession != null && userSession?.isLoggedIn != false) {
                val view = this.view ?: return false

                var toBeDeleted = true
                deleteComment(adapterPosition)

                Toaster.toasterCustomCtaWidth = com.tokopedia.unifyprinciples.R.dimen.unify_space_96
                val toaster = Toaster.build(
                    view,
                    getString(R.string.kol_delete_1_comment),
                    TOASTER_DURATION_DELETE_COMMENT,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.kol_delete_comment_ok)
                ) {
                    if (isFromContentDetailPage)
                        analyticsTracker.sendClickKembalikanToUndoDeleteSgcImageEvent(
                            getContentDetailAnalyticsData()
                        )
                    else
                        feedAnalytics.clickKembalikanCommentPage(
                            postId,
                            authorId,
                            isVideoPost,
                            isFollowed,
                            postType,
                            ""
                        )
                    adapter?.clearList()
                    presenter.getCommentFirstTime(arguments?.getLong(ARGS_ID) ?: 0)
                    toBeDeleted = false
                }

                val deleteFn = {
                    if (activity != null && isAdded) {
                        if (toBeDeleted) presenter.deleteComment(id, adapterPosition)
                        toBeDeleted = false
                    }
                }

                val lifecycleObserver = object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event != Lifecycle.Event.ON_DESTROY) return
                        deleteFn()
                    }
                }
                viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)

                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    delay(TOASTER_DURATION_DELETE_COMMENT.toLong())
                    deleteFn()
                    viewLifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                }

                toaster.show()

            } else {
                goToLogin()
            }
            return true
        } else {
            return false
        }
    }

    private fun isInfluencer(): Boolean {
        return (header != null && userSession != null && !TextUtils.isEmpty(header?.userId)
            && userSession?.userId == header?.userId)
    }

    @SuppressLint("Method Call Prohibited")
    fun reportAction(
        id: String,
        reasonType: String,
        reasonDesc: String
    ) {
        if (::reportBottomSheet.isInitialized)
            reportBottomSheet.setFinalView()

        presenter.sendReport(id.toIntOrZero(), reasonType, reasonDesc, "comment")
        if (isFromContentDetailPage)
            analyticsTracker.sendClickReportOnComment(getContentDetailAnalyticsData())
        else
            feedAnalytics.clickReportCommentPage(
                id,
                authorId,
                isVideoPost,
                isFollowed,
                postType,
                ""
            )
    }

    @SuppressLint("Method Call Prohibited")
    override fun onMenuClicked(
        id: String,
        canDeleteComment: Boolean,
        canReportComment: Boolean,
        adapterPosition: Int
    ) {
        val sheet = MenuOptionsBottomSheet.newInstance(
            isReportable = canReportComment,
            canUnfollow = false,
            isDeletable = canDeleteComment
        )
        sheet.show((context as FragmentActivity).supportFragmentManager, "")
        sheet.setIsCommentPage(true)
        sheet.onReport = {
            if (userSession?.isLoggedIn == true) {
                reportBottomSheet = ReportBottomSheet.newInstance(
                    context = object : ReportBottomSheet.OnReportOptionsClick {
                        override fun onReportAction(reasonType: String, reasonDesc: String) {
                            reportAction(
                                id,
                                reasonType,
                                reasonDesc
                            )
                        }
                    })
                reportBottomSheet.show((context as FragmentActivity).supportFragmentManager, "")
            } else {
                goToLogin()
            }
        }
        sheet.onDelete = {
            if (userSession?.isLoggedIn == true) {
                onDeleteCommentKol(id, canDeleteComment, adapterPosition)
            } else {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        requireActivity().startActivityForResult(intent, REQUEST_LOGIN)
    }

    override fun onResume() {
        super.onResume()
        setAvatar()
    }

    override fun replyToUser(user: MentionableUserModel?) {

        if (user?.isShop == false) {
            val userToMention = createValidMentionText(user.toString())
            kolComment?.append(userToMention)
        } else {
            val mentionFormatBuilder = StringBuilder()
            val isCommentNotEmpty = kolComment?.text?.isNotEmpty() == true
            val isLastCharNotBlank =
                if (isCommentNotEmpty) kolComment?.text?.last() != ' ' else false
            if (isLastCharNotBlank) mentionFormatBuilder.append(" ")

            mentionFormatBuilder
                .append("@")
                .append(MethodChecker.fromHtml(user?.fullName))
                .append(" ")
            kolComment?.append(mentionFormatBuilder.toString())
        }
        kolComment?.setSelection(kolComment?.length() ?: 0)
        kolComment?.requestFocus()
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onHashTagClicked(hashTag: String?) {
        if (isFromContentDetailPage)
            analyticsTracker.sendClickHashtagEventCommentPage(
                getContentDetailAnalyticsData(
                    hashTag ?: ""
                )
            )
        else
            feedAnalytics.clickHashTag(
                hashTag ?: "",
                authorId,
                postId,
                postType,
                isVideoPost,
                isFollowed,
                true
            )
    }

    private fun getContentDetailAnalyticsData(
        hashTag: String = "",
    ) = ContentDetailPageAnalyticsDataModel(

        activityId = postId,
        shopId = authorId,
        isFollowed = isFollowed,
        type = postType,
        hashtag = hashTag,
        source = contentDetailSource
    )

    override fun onGoToProfile(url: String, userId: String) {
        if (isFromContentDetailPage)
            analyticsTracker.sendClickCommentCreator(getContentDetailAnalyticsData())
        else
            feedAnalytics.clickCreatorPageCommentPage(
                postId,
                authorId,
                isVideoPost,
                isFollowed,
                postType,
                userId,
                ""
            )
        routeUrl(url)
    }

    override fun onClickMentionedProfile(id: String?) {
        RouteManager.route(
            context,
            ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, id ?: "")
        )
    }

    override fun onSuccessGetComments(kolComments: KolComments?) {
        val list = ArrayList<Visitable<*>?>()
        kolComments?.let { list.addAll(it.listNewComments) }
        list.reverse()
        totalNewComment = list.size
        adapter?.addList(list)

        header?.isCanLoadMore = kolComments?.isHasNextPage ?: false
        header?.isLoading = false
        adapter?.notifyItemChanged(0)

    }

    override fun openRedirectUrl(url: String) {
        if (isFromContentDetailPage)
            analyticsTracker.sendClickShopOnConmmentPage(getContentDetailAnalyticsData())
        else
            feedAnalytics.clickShopCommentPage(
                postId,
                authorId,
                isVideoPost,
                isFollowed,
                postType,
                ""
            )
        routeUrl(url)
    }

    private fun routeUrl(url: String?) {
        if (RouteManager.isSupportApplink(context, url)) {
            RouteManager.route(context, url)
        } else {
            RouteManager.route(
                context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
            )
        }
    }

    override fun onSuccessSendComment(sendKolCommentDomain: SendKolCommentDomain?) {
        adapter?.addItem(
            KolCommentNewModel(
                sendKolCommentDomain?.id, sendKolCommentDomain?.domainUser?.id.toString(),
                null,
                sendKolCommentDomain?.domainUser?.photo,
                sendKolCommentDomain?.domainUser?.name,
                sendKolCommentDomain?.comment,
                sendKolCommentDomain?.time,
                sendKolCommentDomain?.domainUser?.isKol ?: false,
                sendKolCommentDomain?.canDeleteComment() ?: false,
                "",
                false,
                false
            )
        )

        kolComment?.setText("")
        enableSendComment()
        KeyboardHandler.DropKeyboard(context, kolComment)
        val numberOfComments = adapter?.itemCount ?: -1
        /**totalNewComment is number of comment item in adapter excluding first caption item*/
        totalNewComment = if (numberOfComments != -1) numberOfComments - 1 else totalNewComment

        listComment?.scrollToPosition(adapter?.itemCount ?: 1 - 1)
        activity?.setResult(Activity.RESULT_OK, getReturnIntent(totalNewComment))
    }

    private fun getReturnIntent(totalNewComment: Int): Intent {
        val intent = Intent()
        val arguments = arguments
        if (arguments != null && arguments.size() > 0) intent.putExtras(arguments)
        intent.putExtra(COMMENT_ARGS_TOTAL_COMMENT, totalNewComment)
        return intent
    }

    override fun updateCursor(lastcursor: String?) {
        presenter.updateCursor(lastcursor)
    }

    /***
    totalNewComment is number of comment item in adapter excluding first caption item
     ***/
    override fun onSuccessDeleteComment(adapterPosition: Int) {
        if (adapterPosition <= adapter?.itemCount ?: 0) {
            val numberOfComments = adapter?.itemCount ?: -1
            totalNewComment = if (numberOfComments != -1) numberOfComments - 1 else totalNewComment
            activity?.setResult(Activity.RESULT_OK, getReturnIntent(totalNewComment))
        }
        adapter?.removeLoading()
    }

    private fun deleteComment(adapterPosition: Int) {
        if (isFromContentDetailPage)
            analyticsTracker.sendClickDeleteComment(getContentDetailAnalyticsData())
        else
            feedAnalytics.clickDeleteCommentPage(
                postId,
                authorId,
                isVideoPost,
                isFollowed,
                postType,
                ""
            )
        adapter?.deleteItem(adapterPosition)
    }

    override fun showLoading() {
        adapter?.showLoading()
    }

    override fun onSuccessSendReport() {
        adapter?.removeLoading()
    }

    override fun onErrorSendReport(message: String) {
        showToastMessage(message)
    }

    override fun showMentionUserSuggestionList(userList: MutableList<MentionableUserModel>?) {
        userList?.let {
            mentionAdapter?.setMentionableUser(it)
        }
    }

    override fun onErrorSendComment(errorMessage: String?) {
        view?.let {
            Toaster.build(
                it,
                getString(R.string.kol_adding_comment_error),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            )
                .show()
        }
        enableSendComment()
    }

    override fun removeLoading() {
        adapter?.removeLoading()
    }

    override fun onErrorGetCommentsFirstTime(errorMessage: String?) {
        globalError.visible()
        globalError.setOnClickListener {
            presenter.getCommentFirstTime(
                requireArguments().getLong(ARGS_ID)
            )
        }

        showError(
            true
        ) {
            presenter.getCommentFirstTime(
                requireArguments().getLong(ARGS_ID)
            )
        }
    }

    private fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        removeLoading()
        globalError.visible()
        globalError.setActionClickListener {
            showLoading()
            onError()
        }
        globalError.setType(
            if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    override fun enableSendComment() {
        sendButton?.isClickable = true
    }

    override fun loadMoreComments() {
        header?.isLoading = true
        adapter?.notifyItemChanged(0)

        arguments?.getLong(KolCommentNewActivity.ARGS_ID)?.let { presenter.loadMoreComments(it) }

    }

    override fun onErrorLoadMoreComment(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
        header?.isLoading = false
        header?.isCanLoadMore = true
        adapter?.notifyItemChanged(0)

    }

    override fun dismissProgressDialog() {
    }

    override fun onSuccessGetCommentsFirstTime(kolComments: KolComments?) {
        globalError.gone()
        removeLoading()
        header = kolComments?.headerNewModel
        header?.isCanLoadMore = kolComments?.isHasNextPage ?: false

        setHeader(header)
        val list = ArrayList<Visitable<*>?>()
        kolComments?.let { list.addAll(it.listNewComments) }
        list.reverse()
        totalNewComment = list.size
        adapter?.setList(list)
        adapter?.notifyDataSetChanged()
    }

    private fun setHeader(header: KolCommentHeaderNewModel?) {
        adapter?.addHeaderNew(header)
    }

    override fun showProgressDialog() {
    }

    override fun disableSendComment() {
        sendButton?.isClickable = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            setAvatar()
        }
    }

    private fun setAvatar() {
        /*check if the user commenting is a shop to show shop avatar*/
        val authorId = arguments?.getString(ARGS_AUTHOR_TYPE)
        ImageHandler.loadImageCircle2(context, avatarShop, userSession?.profilePicture)
        if (authorId?.isNotEmpty() == true && authorId == userSession?.shopId) {
            ImageHandler.loadImageCircle2(context, avatarShop, userSession?.shopAvatar)
        }
    }

    private fun prepareView() {
        userSession?.isLoggedIn?.let {
            if (isFromContentDetailPage)
                analyticsTracker.openCommentPageAnalytics()
            else
                feedAnalytics.openCommentDetailPage(it)
        }
        adapter = KolCommentAdapter(typeFactory)
        listComment?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listComment?.adapter = adapter
        sendButton?.setOnClickListener { v: View? ->
            if (isFromContentDetailPage)
                analyticsTracker.sendClickSendComment(getContentDetailAnalyticsData())
            else
                feedAnalytics.clickSendCommentPage(
                    postId,
                    authorId,
                    isVideoPost,
                    isFollowed,
                    postType,
                    ""
                )
            if (userSession != null && userSession?.isLoggedIn != false) {
                presenter.sendComment(
                    arguments?.getLong(ARGS_ID) ?: 0,
                    kolComment?.getRawText()
                )
            } else {
                goToLogin()
            }
        }
        mentionAdapter = MentionableUserAdapter(this)
        kolComment?.setAdapter(mentionAdapter)
    }

    private fun showToastMessage(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getCommentFirstTime(arguments?.getLong(ARGS_ID) ?: 0)
    }

    override fun shouldGetMentionableUser(keyword: String) {
        presenter.getMentionableUserByKeyword(keyword)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        try {
            Toaster.onCTAClick = View.OnClickListener { }
        } catch (ignored: UninitializedPropertyAccessException) {
        }
    }
}

package com.tokopedia.kol.feature.comment.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.comment.di.DaggerKolCommentComponent
import com.tokopedia.kol.feature.comment.di.KolCommentModule
import com.tokopedia.kol.feature.comment.domain.model.SendKolCommentDomain
import com.tokopedia.kol.feature.comment.view.activity.KolCommentNewActivity.Companion.ARGS_AUTHOR_TYPE
import com.tokopedia.kol.feature.comment.view.activity.KolCommentNewActivity.Companion.ARGS_ID
import com.tokopedia.kol.feature.comment.view.adapter.KolCommentAdapter
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel
import com.tokopedia.kol.feature.comment.view.viewmodel.KolComments
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val REQUEST_LOGIN = 345

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

    @Inject
    internal lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var presenter: KolComment.Presenter

    @Inject
    lateinit var typeFactory: KolCommentTypeFactory


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
                requireArguments().getInt(ARGS_ID)
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
                deleteComment(adapterPosition)
                var toBeDeleted = true
                view?.let {
                    Toaster.toasterCustomCtaWidth =
                        com.tokopedia.unifyprinciples.R.dimen.unify_space_96
                    Toaster.build(
                        it,
                        getString(R.string.kol_delete_1_comment),
                        3000,
                        Toaster.TYPE_NORMAL,
                        getString(R.string.kol_delete_comment_ok)
                    ) {
                        feedAnalytics.clickKembalikanCommentPage(ARGS_ID)
                        adapter?.clearList()
                        presenter.getCommentFirstTime(arguments?.getInt(ARGS_ID) ?: 0)
                        toBeDeleted = false
                    }.show()
                    val coroutineScope = CoroutineScope(Dispatchers.Main)
                    coroutineScope.launch {
                        delay(3000L)
                        if (activity != null && isAdded) {
                            if (toBeDeleted)
                                presenter.deleteComment(id, adapterPosition)
                        }
                    }
                }
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

    fun reportAction(
        id: String,
        reasonType: String,
        reasonDesc: String
    ) {
        presenter.sendReport(id.toInt(), reasonType, reasonDesc, "comment")
        feedAnalytics.clickReportCommentPage(id)
    }

    override fun onMenuClicked(
        id: String,
        canDeleteComment: Boolean,
        adapterPosition: Int
    ) {
        val sheet = MenuOptionsBottomSheet.newInstance(
            isReportable = !canDeleteComment,
            canUnfollow = false,
            isDeletable = canDeleteComment
        )
        sheet.show((context as FragmentActivity).supportFragmentManager, "")
        sheet.onReport = {
            if (userSession?.isLoggedIn == true) {
                ReportBottomSheet.newInstance(
                    id.toInt(),
                    context = object : ReportBottomSheet.OnReportOptionsClick {
                        override fun onReportAction(reasonType: String, reasonDesc: String) {
                            reportAction(
                                id,
                                reasonType,
                                reasonDesc
                            )
                        }
                    }).show((context as FragmentActivity).supportFragmentManager, "")
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

    override fun replyToUser(user: MentionableUserViewModel?) {

        if (user?.isShop == false) {
            val userToMention = createValidMentionText(user.toString())
            kolComment?.append(userToMention)
        } else {
            val mentionFormatBuilder = StringBuilder()
            if (kolComment?.text?.isNotEmpty() == true && kolComment?.text?.get(
                    kolComment?.length()
                        ?: 1 - 1
                ) != ' '
            ) mentionFormatBuilder.append(" ")
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

    override fun onHashTagClicked(hashTag: String?, id: String?) {
        feedAnalytics.clickHashTag(hashTag ?: "", id ?: "0")
    }

    override fun onGoToProfile(url: String) {
        feedAnalytics.clickShopCommentPage(ARGS_ID)
        openRedirectUrl(url)
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
        adapter?.addList(list)

        if (adapter?.header != null) {
            adapter?.header?.isCanLoadMore = kolComments?.isHasNextPage ?: false
            adapter?.header?.isLoading = false
            adapter?.notifyItemChanged(0)
        }
    }

    override fun openRedirectUrl(url: String) {
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
                false
            )
        )

        kolComment?.setText("")
        enableSendComment()
        KeyboardHandler.DropKeyboard(context, kolComment)
        totalNewComment += 1

        listComment?.scrollToPosition(adapter?.itemCount ?: 1 - 1)
        activity?.setResult(Activity.RESULT_OK, getReturnIntent(totalNewComment))
    }

    private fun getReturnIntent(totalNewComment: Int): Intent {
        val intent = Intent()
        val arguments = arguments
        if (arguments != null && arguments.size() > 0) intent.putExtras(arguments)
        intent.putExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, totalNewComment)
        return intent
    }

    override fun updateCursor(lastcursor: String?) {
    }

    override fun onSuccessDeleteComment(adapterPosition: Int) {
        if (adapterPosition < adapter?.itemCount ?: 0) {
            totalNewComment -= 1
            activity?.setResult(Activity.RESULT_OK, getReturnIntent(totalNewComment))
        }
        adapter?.removeLoading()
    }

    private fun deleteComment(adapterPosition: Int) {
        feedAnalytics.clickDeleteCommentPage(ARGS_ID)
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

    override fun showMentionUserSuggestionList(userList: MutableList<MentionableUserViewModel>?) {
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
                requireArguments().getInt(ARGS_ID)
            )
        }

        showError(
            true
        ) {
            presenter.getCommentFirstTime(
                requireArguments().getInt(ARGS_ID)
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
    }

    override fun onErrorLoadMoreComment(errorMessage: String?) {
    }

    override fun dismissProgressDialog() {
    }

    override fun onSuccessGetCommentsFirstTime(kolComments: KolComments?) {
        globalError.gone()
        removeLoading()
        header = kolComments?.headerNewModel
        setHeader(header)
        val list = ArrayList<Visitable<*>?>()
        kolComments?.let { list.addAll(it.listNewComments) }
        list.reverse()
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
        val authorId = arguments?.getString(ARGS_AUTHOR_TYPE)
        if (authorId?.isNotEmpty() == true) {
            if (authorId == userSession?.shopId) {
                ImageHandler.loadImageCircle2(context, avatarShop, userSession?.shopAvatar)
            }
        } else {
            ImageHandler.loadImageCircle2(context, avatarShop, userSession?.profilePicture)
        }
    }

    private fun prepareView() {
        userSession?.isLoggedIn?.let { feedAnalytics.openCommentDetailPage(it) }
        adapter = KolCommentAdapter(typeFactory)
        listComment?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listComment?.adapter = adapter
        sendButton?.setOnClickListener { v: View? ->
            feedAnalytics.clickSendCommentPage(ARGS_ID)
            if (userSession != null && userSession?.isLoggedIn != false) {
                presenter.sendComment(
                    arguments?.getInt(ARGS_ID) ?: 0,
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
        presenter.getCommentFirstTime(arguments?.getInt(ARGS_ID) ?: 0)
    }

    override fun shouldGetMentionableUser(keyword: String) {
        presenter.getMentionableUserByKeyword(keyword)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
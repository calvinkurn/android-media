package com.tokopedia.profile.view.fragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.feature.onboarding.view.fragment.UsernameInputFragment
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.kol.common.util.PostMenuListener
import com.tokopedia.kol.common.util.createBottomMenu
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.*
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity.PARAM_POST_ID
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.profile.ProfileModuleRouter
import com.tokopedia.profile.R
import com.tokopedia.profile.analytics.ProfileAnalytics
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.view.activity.FollowingListActivity
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.adapter.viewholder.EmptyAffiliateViewHolder
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.preference.ProfilePreference
import com.tokopedia.profile.view.util.ShareBottomSheets
import com.tokopedia.profile.view.viewmodel.DynamicFeedProfileViewModel
import com.tokopedia.profile.view.viewmodel.EmptyAffiliateViewModel
import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.showcase.ShowCaseBuilder
import com.tokopedia.showcase.ShowCaseContentPosition
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import javax.inject.Inject


/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View, KolPostListener.View.ViewHolder, KolPostListener.View.Like,
        DynamicPostViewHolder.DynamicPostListener,
        BannerAdapter.BannerItemListener,
        TopadsShopViewHolder.TopadsShopListener,
        RecommendationCardAdapter.RecommendationCardListener,
        CardTitleView.CardTitleListener,
        ImagePostViewHolder.ImagePostListener,
        YoutubeViewHolder.YoutubePostListener,
        PollAdapter.PollOptionListener,
        GridPostAdapter.GridItemListener,
        VideoViewHolder.VideoViewListener,
        EmptyAffiliateViewHolder.OnEmptyItemClickedListener {

    private var userId: Int = 0
    private var afterPost: Boolean = false
    private var afterEdit: Boolean = false
    private var successPost: Boolean = false
    private var onlyOnePost: Boolean = false
    private var isAffiliate: Boolean = false
    private var isOwner: Boolean = false
    private var openShare: Boolean = false
    private var resultIntent: Intent? = null
    private var affiliatePostQuota: AffiliatePostQuota? = null
    private var profileHeader: ProfileHeaderViewModel? = null
    private val submitPostReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) {
                    return
                }

                if (intent.action == BROADCAST_SUBMIT_POST
                        && intent.extras?.getBoolean(SUBMIT_POST_SUCCESS) == true) {
                    onSwipeRefresh()
                }
            }
        }
    }
    private var isAppBarCollapse = false

    private lateinit var layoutManager: LinearLayoutManager

    override lateinit var profileRouter: ProfileModuleRouter

    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    @Inject
    lateinit var profileAnalytics: ProfileAnalytics

    @Inject
    lateinit var postTagAnalytics: PostTagAnalytics

    @Inject
    lateinit var profilePreference: ProfilePreference

    companion object {
        private const val PARAM_TAB_NAME = "{tab_name}"
        private const val PARAM_CATEGORY_ID = "{category_id}"
        private const val YOUTUBE_URL = "{youtube_url}"
        private const val TAB_INSPIRASI = "inspirasi"
        private const val CATEGORY_0 = "0"
        private const val TEXT_PLAIN = "text/plain"
        private const val KOL_COMMENT_CODE = 13
        private const val SETTING_PROFILE_CODE = 83
        private const val ONBOARDING_CODE = 10
        private const val EDIT_POST_CODE = 1310
        private const val LOGIN_CODE = 1383
        private const val LOGIN_FOLLOW_CODE = 1384
        private const val OPEN_CONTENT_REPORT = 1130
        private const val FOLLOW_HEADER = "follow_header"
        private const val FOLLOW_FOOTER = "follow_footer"

        fun createInstance(bundle: Bundle): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initVar(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        profileAnalytics.sendScreen(activity!!, screenName)
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()
        hideLoadingLayout()
    }

    override fun onPause() {
        super.onPause()
        unregisterBroadcastReceiver()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ProfileActivity.EXTRA_PARAM_USER_ID, userId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            KOL_COMMENT_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    onSuccessAddDeleteKolComment(
                            data.getIntExtra(KolCommentActivity.ARGS_POSITION, -1),
                            data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0))
                }
            }
            LOGIN_FOLLOW_CODE -> {
                if (resultCode == Activity.RESULT_OK)
                    doFollowAfterLogin()
            }
            SETTING_PROFILE_CODE, ONBOARDING_CODE, EDIT_POST_CODE, LOGIN_CODE -> {
                onSwipeRefresh()
            }
            OPEN_CONTENT_REPORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        if (it.getBooleanExtra(ContentReportActivity.RESULT_SUCCESS, false)) {
                            onSuccessReportContent()
                        } else {
                            onErrorReportContent(it.getStringExtra(ContentReportActivity.RESULT_ERROR_MSG))
                        }
                    }
                }
            }
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return view!!.findViewById(R.id.recyclerView)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view!!.findViewById(R.id.swipeToRefresh)
    }

    override fun onSwipeRefresh() {
        footerOwn.visibility = View.GONE
        app_bar_layout.visibility = View.GONE
        super.onSwipeRefresh()
    }

    override fun getScreenName(): String {
        return if (userId.toString() == userSession.userId) ProfileAnalytics.Screen.MY_PROFILE
        else ProfileAnalytics.Screen.PROFILE
    }

    override fun initInjector() {
        DaggerProfileComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ProfileTypeFactoryImpl(
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                userSession)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            hideLoadingLayout()
        }
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadData(page: Int) {
        if (isLoadingInitialData) {
            footerOwn.visibility = View.GONE
            app_bar_layout.visibility = View.GONE
            presenter.getProfileFirstPage(userId, false)
        } else {
            presenter.getProfilePost(userId)
        }
    }

    override fun getUserSession(): UserSession = UserSession(context)

    override fun onSuccessGetProfileFirstPage(element: DynamicFeedProfileViewModel, isFromLogin: Boolean) {
        presenter.cursor = element.dynamicFeedDomainModel.cursor
        onlyOnePost = element.dynamicFeedDomainModel.postList.size == 1
        isAffiliate = element.profileHeaderViewModel.isAffiliate
        affiliatePostQuota = element.affiliatePostQuota

        if (element.profileHeaderViewModel.isAffiliate) {
            setToolbarTitle(element.profileHeaderViewModel.affiliateName)
            addFooter(
                    element.profileHeaderViewModel,
                    element.affiliatePostQuota
            )
        }
        setProfileToolbar(element.profileHeaderViewModel, isFromLogin)

        val visitables: ArrayList<Visitable<*>> = ArrayList()
        if (!element.dynamicFeedDomainModel.postList.isEmpty()) {
            element.dynamicFeedDomainModel.postList
                    .filterIsInstance<BaseKolViewModel>()
                    .forEach { it.isKol = element.profileHeaderViewModel.isKol }
            visitables.addAll(element.dynamicFeedDomainModel.postList)
        } else {
            visitables.add(getEmptyModel(
                    element.profileHeaderViewModel.isShowAffiliateContent,
                    element.profileHeaderViewModel.isOwner,
                    element.profileHeaderViewModel.isAffiliate)
            )
        }
        trackKolPostImpression(visitables)
        renderList(visitables, !TextUtils.isEmpty(element.dynamicFeedDomainModel.cursor))

        when {
            afterPost -> {
                showAfterPostToast()
                afterPost = false

            }
            afterEdit -> {
                showAfterEditToaster()
                afterEdit = false

            }
            successPost -> {
                when {
                    isAutomaticOpenShareUser() -> {
                        shareLink(element.profileHeaderViewModel.link)
                        profileAnalytics.eventClickBagikanProfile(isOwner, userId.toString())
                    }
                    onlyOnePost -> showShowCaseDialog(shareProfile)
                    else -> showAfterPostToaster(affiliatePostQuota?.number != 0)
                }
                successPost = false
            }
            openShare -> {
                shareLink(element.profileHeaderViewModel.link)
                openShare = false
            }
        }
        recyclerView.scrollTo(0, 0)
    }

    override fun hideHeader() {
        app_bar_layout.setExpanded(false)
        app_bar_layout.visibility = View.GONE
        footer.visibility = View.GONE
    }

    override fun onSuccessShouldChangeUsername(shouldChange: Boolean, link: String) {
        if (shouldChange) {
            val usernameInputFragment = UsernameInputFragment.createInstance(
                    profileHeader?.affiliateName ?: ""
            )
            usernameInputFragment.show(
                    childFragmentManager,
                    UsernameInputFragment::class.java.simpleName
            )
            usernameInputFragment.onDismissListener = {
                if (usernameInputFragment.isSuccessRegister) {
                    openShare = true
                    onSwipeRefresh()
                    profilePreference.setShouldChangeUsername(false)
                }
            }
        } else {
            doShare(link)
            profilePreference.setShouldChangeUsername(shouldChange)
        }
    }

    override fun onErrorShouldChangeUsername(errorMessage: String, link: String) {
        view?.showErrorToaster(errorMessage, R.string.title_try_again) {
            presenter.shouldChangeUsername(userSession.userId.toIntOrZero(), link)
        }
    }

    override fun onSuccessGetProfilePost(visitables: List<Visitable<*>>, lastCursor: String) {
        presenter.cursor = lastCursor
        trackKolPostImpression(visitables)
        renderList(visitables, !TextUtils.isEmpty(lastCursor))
    }

    override fun goToFollowing() {
        profileAnalytics.eventClickFollowing(isOwner, userId.toString())
        startActivity(FollowingListActivity.createIntent(context, userId.toString()))
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: Int) {
        if (adapter.data.size > rowNumber
                && adapter.data[rowNumber] != null
                && adapter.data[rowNumber] is DynamicPostViewModel) {
            val model = adapter.data[rowNumber] as DynamicPostViewModel
            val like = model.footer.like
            like.isChecked = !model.footer.like.isChecked
            if (like.isChecked) {
                try {
                    val likeValue = Integer.valueOf(like.fmt) + 1
                    like.fmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                }

                like.value = like.value + 1
            } else {
                try {
                    val likeValue = Integer.valueOf(like.fmt) - 1
                    like.fmt = likeValue.toString()
                } catch (ignored: NumberFormatException) {
                }

                like.value = like.value - 1
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_LIKE)

            if (activity != null &&
                    arguments != null &&
                    arguments!!.getInt(PARAM_POST_ID, -1) == model.id) {

                if (resultIntent == null) {
                    resultIntent = Intent()
                    resultIntent!!.putExtras(arguments!!)
                }
                resultIntent!!.putExtra(
                        PARAM_IS_LIKED,
                        if (like.isChecked) IS_LIKE_TRUE else IS_LIKE_FALSE)
                resultIntent!!.putExtra(PARAM_TOTAL_LIKES, like.value)
                activity!!.setResult(Activity.RESULT_OK, resultIntent)
            }
        }
    }

    override fun onLikeKolError(message: String) = showError(message)

    override fun onGoToKolProfile(rowNumber: Int, userId: String?, postId: Int) {

    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String?) {

    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String, url: String) {
        if (!TextUtils.isEmpty(uniqueTrackingId) && userId.toString() != userSession.userId) {
            presenter.trackPostClick(uniqueTrackingId, url)
        }

        profileRouter.openRedirectUrl(activity as Activity, url)
    }

    override fun trackContentClick(hasMultipleContent: Boolean, activityId: String,
                                   activityType: String, position: String) {
    }

    override fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String,
                                   activityType: String, position: String) {
        profileAnalytics.eventClickTag(
                isOwner,
                hasMultipleContent,
                activityId,
                activityType,
                position
        )
    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {

    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {

    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
            if (isOwner.not()) {
                profileAnalytics.eventClickLike(hasMultipleContent, id.toString(), activityType)
            }
        } else {
            goToLogin()
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                    activityType: String) {
        if (userSession.isLoggedIn) {
            presenter.unlikeKol(id, rowNumber, this)
            if (isOwner.not()) {
                profileAnalytics.eventClickUnlike(hasMultipleContent, id.toString(), activityType)
            }
        } else {
            goToLogin()
        }
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        val intent = KolCommentActivity.getCallingIntent(
                context, id, rowNumber
        )
        startActivityForResult(intent, KOL_COMMENT_CODE)
        if (isOwner.not()) {
            profileAnalytics.eventClickComment(hasMultipleContent, id.toString(), activityType)
        }
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String,
                               activityType: String) {
    }

    override fun onMenuClicked(rowNumber: Int, element: BaseKolViewModel) {
        context?.let {
            val menus = createBottomMenu(it, element, object : PostMenuListener {
                override fun onDeleteClicked() {
                    createDeleteDialog(rowNumber, element.contentId).show()
                }

                override fun onReportClick() {
                    goToContentReport(element.contentId)
                }

                override fun onEditClick() {

                }
            })
            menus.show()
        }
    }

    override fun onSuccessFollowKol() {
        profileHeader?.let {
            it.isFollowed = !it.isFollowed

            try {
                var followers = it.followers.toInt()
                followers += if (it.isFollowed) 1 else -1
                it.followers = followers.toString()
            } catch (e: NumberFormatException) {
            }

            if (it.isFollowed) {
                ToasterNormal
                        .make(view,
                                getString(R.string.follow_success_toast),
                                BaseToaster.LENGTH_LONG)
                        .setAction(getString(R.string.follow_success_check_now),
                                followSuccessOnClickListener(it))
                        .show()
            }

            setFollowBtn(it, false)
            if (activity != null && arguments != null) {
                if (resultIntent == null) {
                    resultIntent = Intent()
                    resultIntent!!.putExtras(arguments!!)
                }
                resultIntent!!.putExtra(
                        ProfileActivity.PARAM_IS_FOLLOWING,
                        if (it.isFollowed) ProfileActivity.IS_FOLLOWING_TRUE
                        else ProfileActivity.IS_FOLLOWING_FALSE
                )
                activity!!.setResult(Activity.RESULT_OK, resultIntent)
            }
        }
        if (!isOwner && footerOthers.isVisible) showFooterOthers()
    }

    override fun onErrorFollowKol(errorMessage: String) = showError(errorMessage)

    override fun onSuccessDeletePost(rowNumber: Int) {
        adapter.data.removeAt(rowNumber)
        adapter.notifyItemRemoved(rowNumber)

        affiliatePostQuota?.let {
            it.number += 1
            bindCurationQuota(it)
        }

        val snackbar = ToasterNormal.make(view,
                getString(R.string.profile_post_deleted),
                BaseToaster.LENGTH_LONG
        )
        snackbar.setAction(R.string.af_title_ok) { snackbar.dismiss() }.show()

        if (adapter.data.isEmpty()) {
            onSwipeRefresh()
        }
    }

    override fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int) {
        showError(errorMessage, View.OnClickListener { presenter.deletePost(id, rowNumber) })
    }

    override fun onChangeAvatarClicked() {
        startActivityForResult(
                RouteManager.getIntent(context!!, ApplinkConst.SETTING_PROFILE),
                SETTING_PROFILE_CODE
        )
    }

    override fun showLoadingLayout() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoadingLayout() {
        loadingLayout.visibility = View.GONE
    }


    //newfeed section
    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.AVATAR,
                    redirectUrl
            )
        }
    }

    override fun onHeaderActionClick(positionInFeed: Int, id: String, type: String, isFollow: Boolean) {
        if (type == FollowCta.AUTHOR_USER) {
            var userIdInt = 0
            try {
                userIdInt = Integer.valueOf(id)
            } catch (ignored: NumberFormatException) {
            }
            if (isFollow) {
                onUnfollowKolClicked(positionInFeed, userIdInt)
            } else {
                onFollowKolClicked(positionInFeed, userIdInt)
            }
            if (adapter.list[positionInFeed] is DynamicPostViewModel) {
                val model = adapter.list[positionInFeed] as DynamicPostViewModel
                trackCardPostClick(
                        positionInFeed,
                        model.trackingPostModel,
                        ProfileAnalytics.Element.FOLLOW,
                        ""
                )
            }
        }
    }

    override fun onMenuClick(positionInFeed: Int, postId: Int, reportable: Boolean, deletable: Boolean, editable: Boolean) {
        context?.let {
            val menus = createBottomMenu(it, deletable, reportable, false, object : PostMenuListener {
                override fun onDeleteClicked() {
                    createDeleteDialog(positionInFeed, postId).show()
                }

                override fun onReportClick() {
                    goToContentReport(postId)
                }

                override fun onEditClick() {

                }
            })
            menus.show()
        }
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onLikeClick(positionInFeed: Int, id: Int, isLiked: Boolean) {
        profileAnalytics.eventClickLike(isOwner, userId.toString())
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id, false, "")
        } else {
            onLikeKolClicked(positionInFeed, id, false, "")
        }
    }

    override fun onCommentClick(positionInFeed: Int, id: Int) {
        profileAnalytics.eventClickComment(isOwner, userId.toString())
        onGoToKolComment(positionInFeed, id, false, "")
    }

    override fun onShareClick(positionInFeed: Int, id: Int, title: String, description: String,
                              url: String, iamgeUrl: String) {
        activity?.let {
            val linkerData = constructShareData("","", url, String.format("%s %s", description, "%s"), title)
            ShareBottomSheets().show(fragmentManager!!, linkerData, isOwner, userId.toString(), false)
        }
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.TAG,
                    redirectUrl
            )
        }

    }

    override fun onPostTagItemClick(positionInFeed: Int, redirectUrl: String, postTagItem: PostTagItem, itemPosition: Int) {
        onGoToLink(redirectUrl)
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            if (isOwner) {
                postTagAnalytics.trackClickPostTagProfileSelf(
                        model.id,
                        postTagItem,
                        itemPosition,
                        model.trackingPostModel
                )
            } else {
                postTagAnalytics.trackClickPostTagProfileOther(
                        model.id,
                        postTagItem,
                        itemPosition,
                        model.trackingPostModel
                )
            }
        }
    }

    override fun onBannerItemClick(positionInFeed: Int, adapterPosition: Int, redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onShopItemClicked(positionInFeed: Int, adapterPosition: Int, shop: com.tokopedia.topads.sdk.domain.model.Shop) {
        val intent = profileRouter.getShopPageIntent(activity!!, shop.id)
        startActivity(intent)
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: com.tokopedia.topads.sdk.domain.model.Data) {

    }

    override fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int, redirectLink: String) {
        onGoToLink(redirectLink)
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int, id: String, type: String, isFollow: Boolean) {

    }

    override fun onActionPopup() {

    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onTitleCtaClick(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String) {
        onGoToLink(redirectLink)
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.IMAGE,
                    redirectLink
            )
        }
    }

    override fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean) {
        for (tracking in trackList) {
            if (isClick) {
                presenter.trackPostClickUrl(tracking.clickURL)
            } else {
                presenter.trackPostClickUrl(tracking.viewURL)
            }
        }
    }

    override fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(
                    context!!,
                    redirectUrl
            )
        }
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.VIDEO,
                    redirectUrl
            )
        }
    }

    override fun onPollOptionClick(positionInFeed: Int, contentPosition: Int, option: Int, pollId: String, optionId: String, isVoted: Boolean, redirectLink: String) {
        if (isVoted) {
            onGoToLink(redirectLink)
        }
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.OPTION + option,
                    redirectLink
            )
        }
    }

    override fun onGridItemClick(positionInFeed: Int, contentPosition: Int, productPosition: Int,
                                 redirectLink: String) {
        onGoToLink(redirectLink)
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.PRODUCT,
                    redirectLink
            )
        }
    }

    override fun onVideoPlayerClicked(
            positionInFeed: Int,
            contentPosition: Int,
            postId: String) {
        startActivity(VideoDetailActivity.getInstance(
                activity!!,
                postId))

    }

    override fun onEmptyComponentClicked() {
        goToAffiliateExplore()
        profileAnalytics.eventClickEmptyStateCta()
    }
//end of new feed section

    private fun initVar(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            userId = savedInstanceState.getInt(ProfileActivity.EXTRA_PARAM_USER_ID, 0)
        } else if (arguments != null) {
            try {
                userId = arguments!!
                        .getString(ProfileActivity.EXTRA_PARAM_USER_ID, ProfileActivity.ZERO)
                        .toInt()
            } catch (e: java.lang.NumberFormatException) {
                activity?.finish()
            }

            afterPost = TextUtils.equals(
                    arguments!!.getString(ProfileActivity.EXTRA_PARAM_AFTER_POST, ""),
                    ProfileActivity.TRUE
            )
            afterEdit = TextUtils.equals(
                    arguments!!.getString(ProfileActivity.EXTRA_PARAM_AFTER_EDIT, ""),
                    ProfileActivity.TRUE
            )
            successPost = TextUtils.equals(
                    arguments!!.getString(ProfileActivity.EXTRA_PARAM_SUCCESS_POST, ""),
                    ProfileActivity.TRUE
            )
        }

        if (context!!.applicationContext is ProfileModuleRouter) {
            profileRouter = context!!.applicationContext as ProfileModuleRouter
        } else {
            throw IllegalStateException("Application must implement "
                    .plus(ProfileModuleRouter::class.java.simpleName))
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)

        isOwner = userId.toString() == userSession.userId

        layoutManager = recyclerView.layoutManager as LinearLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) { // going up
                    if (adapter.dataSize > 0 && isAppBarCollapse && !isOwner && !footerOthers.isVisible) {
                       showFooterOthers()
                    }
                } else if (dy > 0) { // going down
                    if (isAppBarCollapse && !isOwner && footerOthers.isVisible) hideFootersOthers()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (hasFeed() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val item: Visitable<*>?
                        val position = when {
                            itemIsFullScreen() -> layoutManager.findLastVisibleItemPosition()
                            layoutManager.findFirstCompletelyVisibleItemPosition() != -1 -> layoutManager.findFirstCompletelyVisibleItemPosition()
                            layoutManager.findLastCompletelyVisibleItemPosition() != -1 -> layoutManager.findLastCompletelyVisibleItemPosition()
                            else -> 0
                        }
                        item = adapter.list[position]

                        if (item is DynamicPostViewModel) {
                            if (!TextUtils.isEmpty(item.footer.buttonCta.appLink)) {
                                adapter.notifyItemChanged(position, DynamicPostViewHolder.PAYLOAD_ANIMATE_FOOTER)
                            }
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        })
    }

    private fun hasFeed(): Boolean {
        return (adapter.list != null
                && !adapter.list.isEmpty()
                && adapter.list.size > 1
                && adapter.list[0] !is EmptyModel)
    }

    private fun itemIsFullScreen(): Boolean {
        return layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition() == 0
    }

    private fun showFooterOthers() {
        footerOthers.show()
        if (profileHeader?.isFollowed == true){
            footerOthersText.text = getString(R.string.sticky_footer_following)
            footerOthersFollow.hide()
            footerOthersShareText.show()
        } else {
            footerOthersText.text = getString(R.string.sticky_footer_follow)
            footerOthersFollow.show()
            footerOthersFollow.setOnClickListener { _ ->
                profileHeader?.let { followUnfollowUser(it.userId, !it.isFollowed, FOLLOW_FOOTER) }
            }
            footerOthersShareText.hide()
        }
        footerOthersShare.setOnClickListener {
            profileHeader?.let {
                val linkerData = constructShareData(
                        it.name,
                        it.avatar,
                        it.link,
                        String.format(getString(R.string.profile_share_text),
                                it.link),
                        String.format(getString(R.string.profile_share_title)))
                ShareBottomSheets().show(fragmentManager!!, linkerData, isOwner, userId.toString(), true)
            }
        }
    }

    private fun hideFootersOthers() {
        footerOthers.hide()
    }

    private fun setToolbarTitle(title: String) {
        if (activity != null && activity is AppCompatActivity && !TextUtils.isEmpty(title)) {
            (activity as AppCompatActivity).supportActionBar?.title = title
        }
    }

    private fun setProfileToolbar(element: ProfileHeaderViewModel, isFromLogin: Boolean) {
        this.profileHeader = element
        app_bar_layout.visibility = View.VISIBLE
        app_bar_layout.setExpanded(true)
        iv_image_collapse.loadImageRounded(element.avatar)
        iv_profile.loadImageCircle(element.avatar)
        tv_name.text = element.name
        val affName = if (element.affiliateName.isNotBlank()) {
            element.affiliateName
        } else {
            context?.resources?.getString(R.string.title_profile)
        }
        tv_name_parallax.text = affName
        tv_aff_name.text = affName
        iv_back_parallax.setOnClickListener {
            activity?.finish()
        }
        iv_back.setOnClickListener {
            activity?.finish()
        }
        app_bar_layout.addOnOffsetChangedListener (AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            try {
                toolbar.let {
                    if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                        it.visibility = View.VISIBLE
                        isAppBarCollapse = true
                    } else {
                        it.visibility = View.GONE
                        isAppBarCollapse = false
                        if (!isOwner && footerOthers.isVisible) hideFootersOthers()
                    }
                }
            } catch (e: IllegalStateException) {

            }
        }
        )

        val selfProfile = userSession.userId == userId.toString()
                && element.isAffiliate
        lateinit var action: View.OnClickListener
        action = if (!selfProfile) {
            iv_action_parallax.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_share_white))
            iv_action.gone()
            View.OnClickListener {
                val linkerData = constructShareData(
                        element.name,
                        element.avatar,
                        element.link,
                        String.format(getString(R.string.profile_share_text),
                                element.link),
                        String.format(getString(R.string.profile_share_title)))
                ShareBottomSheets().show(fragmentManager!!, linkerData, isOwner, userId.toString(), true)
            }
        } else {
            iv_action_parallax.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_af_graph))
            iv_action.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_af_graph))
            View.OnClickListener {
                goToDashboard()
            }
        }
        if (!element.isAffiliate) {
            iv_action_parallax.visibility = View.GONE
            iv_action.visibility = View.GONE
        }
        iv_action.setOnClickListener(action)
        iv_action_parallax.setOnClickListener(action)

        if (element.isKol || element.isAffiliate) {
            kolBadge.visibility = if (element.isKol) View.VISIBLE else View.GONE

            followers.visibility =
                    if (getFollowersText(element).length > ProfileHeaderViewHolder.TEXT_LENGTH_MIN) View.VISIBLE
                    else View.GONE
            followers.text = getFollowersText(element)
            followers.movementMethod = LinkMovementMethod.getInstance()

            setFollowBtn(element, isFromLogin)
        } else {
            kolBadge.visibility = View.GONE
            if (element.isOwner) {
                followers.visibility =
                        if (getFollowersText(element).length > ProfileHeaderViewHolder.TEXT_LENGTH_MIN) View.VISIBLE
                        else View.GONE
                followers.text = getFollowersText(element)
                followers.movementMethod = LinkMovementMethod.getInstance()
            } else {
                followers.visibility = View.GONE
            }
        }
    }

    private fun setFollowBtn(element: ProfileHeaderViewModel, isFromLogin: Boolean) {
        if (GlobalConfig.isCustomerApp()) {
            if (!element.isOwner) {
                editButton.visibility = View.GONE
                followBtn.visibility = View.VISIBLE
                followBtn.setOnClickListener {
                    followUnfollowUser(element.userId, !element.isFollowed, FOLLOW_HEADER)
                }
                updateButtonState(element.isFollowed)
                if (isFromLogin) {
                    followBtn.performClick()
                }
            } else {
                editButton.visibility = View.VISIBLE
                followBtn.visibility = View.GONE
                editButton.setOnClickListener {
                    onChangeAvatarClicked()
                }
            }
        }
    }

    private fun followUnfollowUser(userId: Int, follow: Boolean, source: String) {
        if (userSession.isLoggedIn) {
            if (follow) {
                presenter.followKol(userId)
                if (source == FOLLOW_HEADER) {
                    profileAnalytics.eventClickFollow(isOwner, userId.toString())
                } else if (source == FOLLOW_FOOTER) {
                    profileAnalytics.eventClickFollowFooter(isOwner, userId.toString())
                }
            } else {
                presenter.unfollowKol(userId)
                if (source == FOLLOW_HEADER) {
                    profileAnalytics.eventClickUnfollow(isOwner, userId.toString())
                } else if (source == FOLLOW_FOOTER) {
                    profileAnalytics.eventClickUnfollowFooter(isOwner, userId.toString())
                }
            }
        } else {
            followAfterLogin()
        }
    }

    private fun constructShareData(name: String, avatar: String, link: String, shareFormat: String, shareTitle: String): LinkerData {
        val linkerData = LinkerData()
        linkerData.name = name
        linkerData.imgUri = avatar
        linkerData.uri = link
        linkerData.textContent = String.format(shareFormat, link)
        linkerData.ogTitle = shareTitle
        return linkerData
    }

    private fun getFollowersText(element: ProfileHeaderViewModel): SpannableString {
        val spannableString: SpannableString
        val following =
                if (element.following != ProfileHeaderViewModel.ZERO)
                    String.format(getString(R.string.profile_following_number), element.following)
                else ""
        spannableString = if ((element.isKol || element.isAffiliate)
                && element.followers != ProfileHeaderViewModel.ZERO) {

            val followers = String.format(
                    getString(R.string.profile_followers_number),
                    element.followers
            )
            val followersAndFollowing =
                    if (!TextUtils.isEmpty(following)) String.format(
                            getString(R.string.profile_followers_and_following),
                            followers,
                            following
                    )
                    else followers
            SpannableString(followersAndFollowing)

        } else {
            SpannableString(following)
        }

        val goToFollowing = object : ClickableSpan() {
            override fun onClick(p0: View?) {
                goToFollowing()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(activity, R.color.white)
            }
        }

        if (following.isNotEmpty() && spannableString.indexOf(following) != -1) {
            spannableString.setSpan(
                    goToFollowing,
                    spannableString.indexOf(following),
                    spannableString.indexOf(following) + following.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
    }

    private fun updateButtonState(isFollowed: Boolean) {
        if (isFollowed) {
            followBtn.text = getString(R.string.profile_following)
        } else {
            followBtn.text = getString(R.string.profile_follow)
        }
    }

    private fun trackKolPostImpression(visitableList: List<Visitable<*>>) {
        visitableList.forEachIndexed { position, model ->
            val adapterPosition = adapter.data.size + position
            when (model) {
                is DynamicPostViewModel -> {
                    val trackingPostModel = model.trackingPostModel
                    profileAnalytics.eventViewCard(
                            trackingPostModel.templateType,
                            trackingPostModel.activityName,
                            trackingPostModel.trackingType,
                            trackingPostModel.mediaType,
                            trackingPostModel.tagsType,
                            trackingPostModel.redirectUrl,
                            trackingPostModel.totalContent,
                            trackingPostModel.postId,
                            adapterPosition,
                            userId,
                            isOwner
                    )
                    if (model.postTag.totalItems != 0 && !model.postTag.items.isEmpty()) {
                        for (i in 0 until model.postTag.totalItems) {
                            if (isOwner) {
                                postTagAnalytics.trackViewPostTagProfileSelf(
                                        model.id,
                                        model.postTag.items.get(i),
                                        i,
                                        trackingPostModel)
                            } else {
                                postTagAnalytics.trackViewPostTagProfileOther(
                                        model.id,
                                        model.postTag.items.get(i),
                                        i,
                                        trackingPostModel
                                )
                            }
                        }
                    }

                    onAffiliateTrackClicked(model.tracking, false)
                }
            }
        }
    }

    private fun showAfterPostToaster(addAction: Boolean) {
        if (addAction) {
            view?.showNormalToaster(getString(R.string.profile_recommend_success), getString(R.string.profile_add_more)) {
                goToAffiliateExplore()
            }
        } else {
            view?.showNormalToaster(getString(R.string.profile_recommend_success))
        }
    }

    private fun showAfterEditToaster() {
        view?.showNormalToaster(getString(R.string.profile_edit_success), getString(R.string.af_title_ok)) {
        }
    }

    private fun addFooter(headerViewModel: ProfileHeaderViewModel,
                          affiliatePostQuota: AffiliatePostQuota) {
        footer.visibility = View.VISIBLE
        if (headerViewModel.isOwner) {
            footerOwn.visibility = View.VISIBLE
            bindCurationQuota(affiliatePostQuota)
            addCuration.setOnClickListener {
                goToAffiliateExplore()
                profileAnalytics.eventClickTambahRekomendasi()
            }
            addCuration.setOnLongClickListener {
                showToast(getString(R.string.profile_add_recommendation))
                true
            }

            shareProfile.setOnClickListener {
                profileAnalytics.eventClickBagikanProfile(isOwner, userId.toString())

                val linkerData = constructShareData(
                        headerViewModel.name,
                        headerViewModel.avatar,
                        headerViewModel.link,
                        String.format(getString(R.string.profile_share_text),
                                headerViewModel.link),
                        String.format(getString(R.string.profile_share_title)))
                fragmentManager?.let {
                    ShareBottomSheets().show(it, linkerData, isOwner, userId.toString(), true)
                }
            }
            shareProfile.setOnLongClickListener {
                showToast(getString(R.string.profile_share_this_profile))
                true
            }
        } else {
            footerOwn.visibility = View.GONE
        }
    }

    private fun bindCurationQuota(affiliatePostQuota: AffiliatePostQuota) {
        if (affiliatePostQuota.number == 0) {
            addCuration.text = getString(R.string.profile_see_by_me_prouct)
        } else {
            val addCurationText = "${getString(R.string.profile_add_rec)} (${affiliatePostQuota.number})"
            addCuration.text = addCurationText
        }
    }

    private fun showAfterPostToast() {
        context?.let {
            Toast.makeText(it, R.string.profile_after_post, Toast.LENGTH_LONG).show()
        }
    }

    private fun showShowCaseDialog(view: View?) {
        val showCaseTag = this::class.java.simpleName
        val showCaseDialog = createShowCaseDialog()
        val showcases = ArrayList<ShowCaseObject>()
        showcases.add(ShowCaseObject(
                view,
                getString(R.string.profile_showcase_title),
                getString(R.string.profile_showcase_description),
                ShowCaseContentPosition.UNDEFINED))
        showCaseDialog.show(this.activity, showCaseTag, showcases)
    }

    private fun createShowCaseDialog(): ShowCaseDialog {
        return ShowCaseBuilder()
                .backgroundContentColorRes(R.color.profile_showcase_black)
                .shadowColorRes(R.color.shadow)
                .titleTextColorRes(R.color.white)
                .titleTextSizeRes(R.dimen.sp_16)
                .textColorRes(R.color.white)
                .textSizeRes(R.dimen.sp_14)
                .nextStringRes(R.string.af_title_ok)
                .finishStringRes(R.string.af_title_ok)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    private fun shareLink(link: String) {
        if (shouldChangeUsername()) {
            presenter.shouldChangeUsername(userSession.userId.toIntOrZero(), link)
        } else {
            doShare(link)
        }
    }

    private fun shouldChangeUsername(): Boolean = isOwner && profilePreference.shouldChangeUsername()

    private fun doShare(link: String) {
        doShare(link, getString(R.string.profile_share_text), getString(R.string.profile_share_title))
    }

    private fun doShare(link: String, formatString: String, shareTitle: String) {
        val shareBody = String.format(formatString, link)
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = TEXT_PLAIN
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(
                Intent.createChooser(sharingIntent, shareTitle)
        )
    }

    private fun getEmptyModel(isShowAffiliateContent: Boolean,
                              isOwner: Boolean,
                              isAffiliate: Boolean): Visitable<*> {
        return if (!isShowAffiliateContent && isOwner && !isAffiliate) ProfileEmptyViewModel()
        else getEmptyResultModel(isOwner, isAffiliate)
    }

    private fun getEmptyResultModel(isOwner: Boolean, isAffiliate: Boolean): Visitable<*> {
        if (isOwner) {
            return EmptyAffiliateViewModel()
        } else {
            val emptyResultViewModel = EmptyResultViewModel()
            emptyResultViewModel.iconRes = R.drawable.ic_af_empty
            emptyResultViewModel.title = getString(R.string.profile_no_content)
            emptyResultViewModel.buttonTitle = getString(R.string.profile_see_others)
            emptyResultViewModel.callback = object : BaseEmptyViewHolder.Callback {
                override fun onEmptyContentItemTextClicked() {
                }

                override fun onEmptyButtonClicked() {
                    goToExplore()
                }
            }
            return emptyResultViewModel
        }
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        if (adapter.data.size > rowNumber &&
                adapter.data[rowNumber] != null &&
                adapter.data[rowNumber] is KolPostViewModel) {
            val kolPostViewModel = adapter.data[rowNumber] as KolPostViewModel
            kolPostViewModel.totalComment = kolPostViewModel.totalComment + totalNewComment
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_COMMENT)

            if (activity != null &&
                    arguments != null &&
                    arguments!!.getInt(PARAM_POST_ID, -1) == kolPostViewModel.contentId) {

                if (resultIntent == null) {
                    resultIntent = Intent()
                    resultIntent!!.putExtras(arguments!!)
                }
                resultIntent!!.putExtra(PARAM_TOTAL_COMMENTS, kolPostViewModel.totalComment)
                activity!!.setResult(Activity.RESULT_OK, resultIntent)
            }
        }

        if (adapter.data.size > rowNumber &&
                adapter.data[rowNumber] != null &&
                adapter.data[rowNumber] is DynamicPostViewModel) {
            val comment = ((adapter.data[rowNumber]) as DynamicPostViewModel).footer.comment
            try {
                val commentValue = Integer.valueOf(comment.fmt) + totalNewComment
                comment.fmt = commentValue.toString()
            } catch (ignored: NumberFormatException) {
            }

            comment.value = comment.value + totalNewComment
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_COMMENT)
        }
    }

    private fun followSuccessOnClickListener(profileHeaderViewModel: ProfileHeaderViewModel)
            : View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(context, ApplinkConst.FEED)
            profileAnalytics.eventClickAfterFollow(profileHeaderViewModel.name)
        }
    }

    private fun createDeleteDialog(rowNumber: Int, id: Int): Dialog {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.profile_delete_post))
        dialog.setDesc(getString(R.string.profile_after_delete_cant))
        dialog.setBtnOk(getString(R.string.kol_title_delete))
        dialog.setBtnCancel(getString(R.string.kol_title_cancel))
        dialog.setOnOkClickListener {
            presenter.deletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener { dialog.dismiss() }
        return dialog
    }

    private fun goToAffiliateExplore() {
        val intent = RouteManager.getIntent(context, ApplinkConst.AFFILIATE_EXPLORE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun goToExplore() {
        RouteManager.route(
                context,
                ApplinkConst.CONTENT_EXPLORE
                        .replace(PARAM_TAB_NAME, TAB_INSPIRASI)
                        .replace(PARAM_CATEGORY_ID, CATEGORY_0)
        )
    }

    private fun goToDashboard() {
        RouteManager.route(context, ApplinkConst.AFFILIATE_DASHBOARD)
        profileAnalytics.eventClickStatistic(userId.toString())
    }

    private fun goToLogin() {
        startActivityForResult(profileRouter.getLoginIntent(context!!), LOGIN_CODE)
    }

    private fun followAfterLogin() {
        startActivityForResult(profileRouter.getLoginIntent(context!!), LOGIN_FOLLOW_CODE)
    }

    private fun doFollowAfterLogin() {
        swipeToRefresh.isRefreshing = true
        footerOwn.visibility = View.GONE
        app_bar_layout.visibility = View.GONE
        presenter.getProfileFirstPage(userId, true)
    }

    private fun showError(message: String) {
        showError(message, null)
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(R.string.title_try_again, listener)
                .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun isAutomaticOpenShareUser(): Boolean {
        val userId = userSession.userId.toIntOrNull() ?: 0
        return (userId % 50 == 17
                || userId % 50 == 23
                || userId == 32044530 //dev's userId
                || userId == 6215930 //QA's userId
                || userId == 17211048 //QA's userId
                || remoteConfig.getBoolean(RemoteConfigKey.AFFILIATE_PROFILE_SHARE_ALL, false))
                && remoteConfig.getBoolean(RemoteConfigKey.AFFILIATE_PROFILE_SHARE_RULES, true)
    }

    private fun goToContentReport(contentId: Int) {
        if (context != null) {
            if (userSession.isLoggedIn) {
                val intent = ContentReportActivity.createIntent(
                        context!!,
                        contentId
                )
                startActivityForResult(intent, OPEN_CONTENT_REPORT)
            } else {
                goToLogin()
            }
        }
    }

    private fun onSuccessReportContent() {
        ToasterNormal
                .make(view,
                        getString(R.string.profile_feed_content_reported),
                        BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close) { }
                .show()
    }

    private fun onErrorReportContent(errorMsg: String?) {
        ToasterError
                .make(view, errorMsg, BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close) { }
                .show()
    }

    private fun onGoToLink(link: String) {
        activity?.let {
            if (!TextUtils.isEmpty(link)) {
                profileRouter.openRedirectUrl(it, link)
            }
        }
    }

    private fun trackCardPostClick(positionInFeed: Int, trackingPostModel: TrackingPostModel,
                                   element: String, redirectUrl: String) {
        trackCardPostClick(positionInFeed, -1, trackingPostModel, element, redirectUrl)
    }

    private fun trackCardPostClick(positionInFeed: Int, contentPosition: Int,
                                   trackingPostModel: TrackingPostModel, element: String,
                                   redirectUrl: String) {
        var userId = 0
        try {
            userId = Integer.valueOf(userSession.userId)
        } catch (ignored: NumberFormatException) {
        }

        profileAnalytics.eventClickCard(
                trackingPostModel.templateType,
                trackingPostModel.activityName,
                trackingPostModel.trackingType,
                trackingPostModel.mediaType,
                trackingPostModel.tagsType,
                redirectUrl,
                element,
                trackingPostModel.totalContent,
                trackingPostModel.postId,
                positionInFeed,
                if (contentPosition != -1) contentPosition.toString() else "",
                userId,
                isOwner
        )
    }

    private fun registerBroadcastReceiver() {
        context?.applicationContext?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(BROADCAST_SUBMIT_POST)

            LocalBroadcastManager
                    .getInstance(it)
                    .registerReceiver(submitPostReceiver, intentFilter)
        }
    }

    private fun unregisterBroadcastReceiver() {
        context?.applicationContext?.let {
            LocalBroadcastManager
                    .getInstance(it)
                    .unregisterReceiver(submitPostReceiver)
        }
    }
}

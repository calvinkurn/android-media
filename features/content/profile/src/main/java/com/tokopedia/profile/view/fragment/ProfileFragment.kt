package com.tokopedia.profile.view.fragment

import android.animation.LayoutTransition
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.feature.onboarding.view.fragment.UsernameInputFragment
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST
import com.tokopedia.affiliatecommon.DISCOVERY_BY_ME
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.util.FeedScrollListener
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.ByMeInstastoryView
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedcomponent.view.widget.PostStatisticBottomSheet
import com.tokopedia.kolcommon.util.PostMenuListener
import com.tokopedia.kolcommon.util.createBottomMenu
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kolcommon.view.listener.KolPostViewHolderListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.profile.R
import com.tokopedia.profile.analytics.ProfileAnalytics
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.following_list.view.activity.FollowingListActivity
import com.tokopedia.profile.following_list.view.activity.UserFollowerListActivity
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.adapter.viewholder.EmptyAffiliateViewHolder
import com.tokopedia.profile.view.adapter.viewholder.OtherRelatedProfileViewHolder
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.preference.ProfilePreference
import com.tokopedia.profile.view.viewmodel.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import javax.inject.Inject


/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View, KolPostViewHolderListener, KolPostLikeListener,
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
        FeedMultipleImageView.FeedMultipleImageViewListener,
        EmptyAffiliateViewHolder.OnEmptyItemClickedListener,
        HighlightAdapter.HighlightListener,
        ShareBottomSheets.OnShareItemClickListener, TopAdsBannerViewHolder.TopAdsBannerListener {

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

        //region Content Report Param
        private const val CONTENT_REPORT_RESULT_SUCCESS = "result_success"
        private const val CONTENT_REPORT_RESULT_ERROR_MSG = "error_msg"
        //endregion

        //region Media Preview Param
        private const val MEDIA_PREVIEW_INDEX = "media_index"
        //endregion

        //region Kol Comment Param
        private const val COMMENT_ARGS_POSITION = "ARGS_POSITION"
        private const val COMMENT_ARGS_TOTAL_COMMENT = "ARGS_TOTAL_COMMENT"
        //endregion

        private const val PARAM_IS_LIKED = "is_liked"
        private const val PARAM_TOTAL_COMMENTS = "total_comments"
        private const val PARAM_TOTAL_LIKES = "total_likes"
        private const val PARAM_POST_ID = "post_id"
        private const val IS_LIKE_TRUE = 1
        private const val IS_LIKE_FALSE = 0

        fun createInstance(bundle: Bundle): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override val androidContext: Context
        get() = requireContext()

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    @Inject
    lateinit var profileAnalytics: ProfileAnalytics

    @Inject
    lateinit var feedAnalytics: FeedAnalyticTracker

    @Inject
    lateinit var postTagAnalytics: PostTagAnalytics

    @Inject
    lateinit var profilePreference: ProfilePreference

    @Inject
    lateinit var affiliatePreference: AffiliatePreference

    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var layoutManager: GridLayoutManager

    lateinit var remoteConfig: RemoteConfig

    private lateinit var byMeInstastoryView: ByMeInstastoryView

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
    private var isAppBarCollapse = false
    private var linkerData: LinkerData? = null
    private var isShareProfile = false

    private lateinit var postStatisticBottomSheet: PostStatisticBottomSheet

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initVar(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        layoutManager = recyclerView.layoutManager as GridLayoutManager
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
                    if (hasFeed()
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        recyclerView?.let {
                            FeedScrollListener.onFeedScrolled(it, adapter.list)
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {
                }
            }
        })
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            val spacing = requireContext().resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
            val halfSpacing = spacing / 2
            val spanCount = 2
            override fun getItemOffsets(outRect: Rect, view: View,
                                        parent: RecyclerView, state: RecyclerView.State) {
                // if in feed mode, will have no item decoration
                if (hasFeed()) return
                val position = parent.getChildAdapterPosition(view)
                val viewType = adapter.getItemViewType(position)
                val inGrid = viewType == OtherRelatedProfileViewHolder.LAYOUT
                if (inGrid) {
                    val column = position % spanCount
                    if (column == 0) {
                        outRect.left = spacing
                        outRect.right = halfSpacing
                    } else {
                        outRect.left = halfSpacing
                        outRect.right = spacing
                    }
                    outRect.bottom = spacing
                }
            }
        })
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
        feedAnalytics.sendPendingAnalytics()
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
                            data.getIntExtra(COMMENT_ARGS_POSITION, -1),
                            data.getIntExtra(COMMENT_ARGS_TOTAL_COMMENT, 0))
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
                        if (it.getBooleanExtra(CONTENT_REPORT_RESULT_SUCCESS, false)) {
                            onSuccessReportContent()
                        } else {
                            onErrorReportContent(it.getStringExtra(CONTENT_REPORT_RESULT_ERROR_MSG))
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
        app_bar_layout.visibility = View.GONE
        super.onSwipeRefresh()
    }

    override fun getScreenName(): String {
        return if (userId.toString() == userSession.userId) ProfileAnalytics.Screen.MY_PROFILE
        else ProfileAnalytics.Screen.PROFILE
    }

    override fun initInjector() {
        DaggerProfileComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
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
                this,
                this::onOtherProfilePostItemClick,
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
            app_bar_layout.visibility = View.GONE
            presenter.getProfileFirstPage(userId, false)
        } else {
            presenter.getProfilePost(userId)
        }
    }

    override fun getUserSession(): UserSession = UserSession(requireContext())

    fun onOtherProfilePostItemClick(applink: String, position: Int, datum: FeedPostRelated.Datum) {
        RouteManager.route(requireContext(), applink)
        profileAnalytics.eventClickOtherPost(userId.toString(), position, datum, userSession.userId, userSession.name)
    }

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
            setCreatePostButton(element.profileHeaderViewModel)
        }
        setProfileToolbar(element.profileHeaderViewModel, isFromLogin)

        val visitables: ArrayList<Visitable<*>> = ArrayList()
        if (!element.dynamicFeedDomainModel.postList.isEmpty()) {
//            element.dynamicFeedDomainModel.postList
//                    .filterIsInstance<BaseKolViewModel>()
//                    .forEach { it.isKol = element.profileHeaderViewModel.isKol }
            visitables.addAll(element.dynamicFeedDomainModel.postList)
            trackKolPostImpression(visitables)
        } else {
            if (element.profileHeaderViewModel.isOwner) {
                visitables.add(getEmptyModelOwner(
                        element.profileHeaderViewModel.isShowAffiliateContent,
                        element.profileHeaderViewModel.isAffiliate))
            } else {
                visitables.add(NoPostCardViewModel(element.profileHeaderViewModel.name,
                        element.profileHeaderViewModel.isKol,
                        element.profileHeaderViewModel.isAffiliate))
                getRelatedProfile()
            }
        }
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
                        checkShouldChangeUsername(element.profileHeaderViewModel.link) { showShareBottomSheets(this) }
                        profileAnalytics.eventClickShareProfileIni(isOwner, userId.toString())
                    }
                    else -> showAfterPostToaster(affiliatePostQuota?.number != 0)
                }
                successPost = false
            }
            openShare -> {
                checkShouldChangeUsername(element.profileHeaderViewModel.link) { showShareBottomSheets(this) }
                openShare = false
            }
        }
        recyclerView.scrollTo(0, 0)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(getActivity(), 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        OtherRelatedProfileViewHolder.LAYOUT -> 1
                        else -> 2
                    }
                }
            }
        }
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
            showShareBottomSheets(this)
            profilePreference.setShouldChangeUsername(shouldChange)
        }
    }

    override fun onErrorShouldChangeUsername(errorMessage: String, link: String) {
        view?.let {
            Toaster.make(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                presenter.shouldChangeUsername(userSession.userId.toIntOrZero(), link)
            })
        }
    }

    override fun onSuccessGetProfilePost(visitables: List<Visitable<*>>, lastCursor: String) {
        presenter.cursor = lastCursor
        trackKolPostImpression(visitables)
        renderList(visitables, !TextUtils.isEmpty(lastCursor))
    }

    override fun goToFollowing() {
        profileAnalytics.eventClickFollowing(isOwner, userId.toString())
        startActivity(FollowingListActivity.createIntent(requireContext(), userId.toString()))
    }

    override fun goToFollower() {
        startActivity(UserFollowerListActivity.getFollowingIntent(requireContext(), userId))
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    override fun onLikeKolSuccess(rowNumber: Int, action: com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.LikeKolPostAction) {
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
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_LIKE)

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

    override fun onGoToKolProfile(rowNumber: Int, userId: String, postId: Int) {
    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String) {
    }

    override fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String, url: String) {
        if (!TextUtils.isEmpty(uniqueTrackingId) && userId.toString() != userSession.userId) {
            presenter.trackPostClick(uniqueTrackingId, url)
        }

        onGoToLink(url)
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
            presenter.likeKol(id, rowNumber, this, isLiked = true)
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
            presenter.likeKol(id, rowNumber, this, isLiked = false)
            if (isOwner.not()) {
                profileAnalytics.eventClickUnlike(hasMultipleContent, id.toString(), activityType)
            }
        } else {
            goToLogin()
        }
    }

    override fun onLikeClick(positionInFeed: Int, columnNumber: Int, id: Int, isLiked: Boolean) {
    }

    override fun onCommentClick(positionInFeed: Int, columnNumber: Int, id: Int) {
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                                  activityType: String) {
        RouteManager.getIntent(
                requireContext(),
                UriUtil.buildUriAppendParam(
                        ApplinkConstInternalContent.COMMENT,
                        mapOf(
                                COMMENT_ARGS_POSITION to rowNumber.toString()
                        )
                ),
                id.toString()
        ).run { startActivityForResult(this, KOL_COMMENT_CODE) }
        if (isOwner.not()) {
            profileAnalytics.eventClickComment(hasMultipleContent, id.toString(), activityType)
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
                Toaster.make(view!!, getString(R.string.follow_success_toast),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.follow_success_check_now), followSuccessOnClickListener(it))
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

        Toaster.make(view!!, getString(R.string.profile_post_deleted), Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL, getString(com.tokopedia.affiliatecommon.R.string.af_title_ok), View.OnClickListener {  })
        if (adapter.data.isEmpty()) {
            onSwipeRefresh()
        }
    }

    override fun onErrorDeletePost(errorMessage: String, id: Int, rowNumber: Int) {
        showError(errorMessage, View.OnClickListener { presenter.deletePost(id, rowNumber) })
    }

    override fun onChangeAvatarClicked() {
        startActivityForResult(
                RouteManager.getIntent(requireContext(), ApplinkConst.SETTING_PROFILE),
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
    override fun onAvatarClick(positionInFeed: Int, redirectUrl: String, activityId: Int, activityName: String, followCta: FollowCta) {
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
            val menus = createBottomMenu(it, deletable, reportable, editable, object : PostMenuListener {
                override fun onDeleteClicked() {
                    createDeleteDialog(positionInFeed, postId).show()
                }

                override fun onReportClick() {
                    goToContentReport(postId)
                }

                override fun onEditClick() {
                    goToEditPostAffiliate(postId)
                }
            })
            menus.show()
        }
    }

    override fun onEditClicked(hasMultipleContent: Boolean, activityId: String,
                               activityType: String) {

    }

    private fun goToEditPostAffiliate(postId: Int) {
        context?.let { RouteManager.route(it, ApplinkConstInternalContent.AFFILIATE_EDIT, postId.toString()) }
    }

    private fun getShopIntent(
            context: Context,
            shopId: String
    ) = RouteManager.getIntent(context,ApplinkConst.SHOP,shopId)

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
                              url: String, imageUrl: String) {
        activity?.let {
            profileAnalytics.eventClickSharePostIni(isOwner, userId.toString())
            isShareProfile = false
            checkShouldChangeUsername(url) {
                linkerData = showShareBottomSheet(
                        ProfileHeaderViewModel(link = url),
                        description,
                        title,
                        null
                )
            }
        }
    }

    override fun onStatsClick(title: String, activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int) {
        showPostStatistic(title, activityId, productIds, likeCount, commentCount)
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
        if (adapter.list[positionInFeed] is TopadsShopViewModel) {
            val (_, dataList, _, _) = adapter.list[positionInFeed] as TopadsShopViewModel
            if (adapterPosition != RecyclerView.NO_POSITION) {
                presenter.doTopAdsTracker(dataList[adapterPosition].shopClickUrl, shop.id, shop.name, dataList[adapterPosition].shop.imageShop.xsEcs, true)
            }
        }
        context?.let {
            startActivity(getShopIntent(it, shop.id))
        }
    }

    override fun onAddFavorite(positionInFeed: Int, adapterPosition: Int, data: com.tokopedia.topads.sdk.domain.model.Data) {

    }

    override fun onRecommendationAvatarClick(positionInFeed: Int, adapterPosition: Int, redirectLink: String, postType: String, authorId: String) {
        onGoToLink(redirectLink)
    }

    override fun onRecommendationActionClick(positionInFeed: Int, adapterPosition: Int, id: String, type: String, isFollow: Boolean) {

    }

    override fun onActionPopup() {

    }

    override fun onActionRedirect(redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onTitleCtaClick(redirectUrl: String, adapterPosition: Int) {
        onGoToLink(redirectUrl)
    }

    override fun userImagePostImpression(positionInFeed: Int, contentPosition: Int) {

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

    override fun onMediaGridClick(positionInFeed: Int, contentPosition: Int,
                                  redirectLink: String, isSingleItem: Boolean) {
        if (adapter.list[positionInFeed] is DynamicPostViewModel) {
            val model = adapter.list[positionInFeed] as DynamicPostViewModel
            trackCardPostClick(
                    positionInFeed,
                    contentPosition,
                    model.trackingPostModel,
                    ProfileAnalytics.Element.IMAGE,
                    redirectLink
            )
            if (!isSingleItem) {
                RouteManager.route(
                        requireContext(),
                        UriUtil.buildUriAppendParam(
                                ApplinkConstInternalContent.MEDIA_PREVIEW,
                                mapOf(
                                        MEDIA_PREVIEW_INDEX to contentPosition.toString()
                                )
                        ),
                        model.id.toString()
                )
            }
        }
    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean) {
        for (tracking in trackList) {
            if (isClick) {
                presenter.trackPostClickUrl(tracking.clickURL)
            } else {
                presenter.trackPostClickUrl(tracking.viewURL)
            }
        }
    }

    override fun onTopAdsImpression(url: String, shopId: String, shopName: String, imageUrl: String) {
        presenter.doTopAdsTracker(url, shopId, shopName, imageUrl, false)
    }

    override fun onHighlightItemClicked(positionInFeed: Int, item: HighlightCardViewModel) {

    }

    override fun onPostTagItemBuyClicked(positionInFeed: Int, postTagItem: PostTagItem, authorType: String) {
        val shop = postTagItem.shop.firstOrNull()
        feedAnalytics.eventProfileAddToCart(
                postTagItem.id,
                postTagItem.text,
                postTagItem.price,
                1,
                shop?.shopId?.toIntOrZero() ?: -1,
                ""
        )
        presenter.addPostTagItemToCart(postTagItem)
    }

    override fun onYoutubeThumbnailClick(positionInFeed: Int, contentPosition: Int, youtubeId: String) {
        val redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId)

        if (context != null) {
            RouteManager.route(
                    context,
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
            postId: String,
            redirectUrl: String) {
        onGoToLink(redirectUrl)
    }

    override fun onEmptyComponentClicked() {
        goToAffiliateExplore()
        profileAnalytics.eventClickEmptyStateCta()
    }

    override fun onShareItemClicked(packageName: String) {
        sendTracker(packageName)
    }

    override fun onAddToCartSuccess() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
    }

    override fun onAddToCartFailed(pdpAppLink: String) {
        onGoToLink(pdpAppLink)
    }

    override fun onHashtagClicked(hashtagText: String, trackingPostModel: TrackingPostModel) {
        feedAnalytics.eventProfileClickHashtag(
                isOwner,
                trackingPostModel.postId.toString(),
                hashtagText
        )
    }

    override fun onReadMoreClicked(trackingPostModel: TrackingPostModel) {
        feedAnalytics.eventProfileClickReadMore(
                isOwner,
                trackingPostModel.postId.toString(),
                trackingPostModel.activityName,
                trackingPostModel.mediaType
        )
    }

    override fun onSuccessGetPostStatistic(statisticCommissionModel: PostStatisticCommissionUiModel) {
        getPostStatisticBottomSheet()
                .setPostStatisticCommissionModel(statisticCommissionModel)
    }

    override fun onErrorGetPostStatistic(error: Throwable, activityId: String, productIds: List<String>) {
        getPostStatisticBottomSheet()
                .setError(error, activityId, productIds)
    }

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

        remoteConfig = FirebaseRemoteConfigImpl(requireContext())

        isOwner = userId.toString() == userSession.userId
    }

    private fun hasFeed(): Boolean {
        return (adapter.list != null
                && !adapter.list.isEmpty()
                && adapter.list[0] !is NoPostCardViewModel
                && adapter.list[0] !is EmptyModel)
    }

    private fun showFooterOthers() {
        parentView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        footer.show()
        footerOthers.show()
        if (profileHeader?.isFollowed == true) {
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
            profileHeader?.let { header ->
                linkerData = showShareBottomSheet(
                        header,
                        String.format(getString(com.tokopedia.feedcomponent.R.string.profile_share_text), header.link),
                        String.format(getString(com.tokopedia.feedcomponent.R.string.profile_other_share_title)),
                        null
                )
                profileAnalytics.eventClickShareProfileIni(isOwner, userId.toString())
                isShareProfile = true
            }
        }
    }

    private fun hideFootersOthers() {
        parentView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        footer.hide()
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
        iv_image_collapse.loadImageWithoutPlaceholder(element.avatar)
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
        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
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

        if (!::byMeInstastoryView.isInitialized) context?.let { ctx -> byMeInstastoryView = ByMeInstastoryView(ctx) }
        byMeInstastoryView.setUserName(element.formattedAffiliateName)

        val selfProfile = userSession.userId == userId.toString()
                && element.isAffiliate
        lateinit var action: View.OnClickListener
        action = if (!selfProfile) {
            iv_action_parallax.setImageDrawable(MethodChecker.getDrawable(requireContext(), com.tokopedia.design.R.drawable.ic_share_white))
            iv_action2_parallax.gone()
            iv_action.gone()
            View.OnClickListener {
                showShareBottomSheet(
                        element,
                        String.format(getString(com.tokopedia.feedcomponent.R.string.profile_share_text), element.link),
                        String.format(getString(com.tokopedia.feedcomponent.R.string.profile_other_share_title)),
                        null
                )
                profileAnalytics.eventClickShareProfileIni(isOwner, userId.toString())
                isShareProfile = true
            }
        } else {
            iv_action_parallax.setImageDrawable(MethodChecker.getDrawable(requireContext(), R.drawable.ic_af_graph))
            iv_action2_parallax.setImageDrawable(MethodChecker.getDrawable(requireContext(), com.tokopedia.design.R.drawable.ic_share_white))
            iv_action.setImageDrawable(MethodChecker.getDrawable(requireContext(), R.drawable.ic_af_graph))
            iv_action2.setImageDrawable(MethodChecker.getDrawable(requireContext(), com.tokopedia.design.R.drawable.ic_share_white))
            View.OnClickListener {
                goToDashboard()
            }
        }
        if (!element.isAffiliate) {
            iv_action_parallax.visibility = View.GONE
            iv_action2_parallax.visibility = View.GONE
            iv_action.visibility = View.GONE
            iv_action2.visibility = View.GONE
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
        if (!GlobalConfig.isSellerApp()) {
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
            presenter.followKol(userId, follow)
            if (follow) {
                if (source == FOLLOW_HEADER) {
                    profileAnalytics.eventClickFollow(isOwner, userId.toString())
                } else if (source == FOLLOW_FOOTER) {
                    profileAnalytics.eventClickFollowFooter(isOwner, userId.toString())
                }
            } else {
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

    private fun getFollowersText(element: ProfileHeaderViewModel): SpannableString {
        val spannableString: SpannableString
        val following =
                if (element.following != ProfileHeaderViewModel.ZERO)
                    String.format(getString(R.string.profile_following_number), element.following)
                else ""
        val followers = String.format(
                getString(R.string.profile_followers_number),
                element.followers
        )
        spannableString = if ((element.isKol || element.isAffiliate)
                && element.followers != ProfileHeaderViewModel.ZERO) {
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

        val goToFollower = object : ClickableSpan() {
            override fun onClick(widget: View) {
                goToFollower()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
                ds.color = MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }

        val goToFollowing = object : ClickableSpan() {
            override fun onClick(p0: View) {
                goToFollowing()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
                ds.color = MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0)
            }
        }
        if (spannableString.indexOf(followers) != -1) {
            spannableString.setSpan(
                    goToFollower,
                    spannableString.indexOf(followers),
                    spannableString.indexOf(followers) + followers.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (spannableString.indexOf(following) != -1) {
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
                    if (model.postTag.totalItems != 0 && model.postTag.items.isNotEmpty()) {
                        if (isOwner) {
                            postTagAnalytics.trackViewPostTagProfileSelf(
                                    model.id,
                                    model.postTag.items,
                                    trackingPostModel)
                        } else {
                            postTagAnalytics.trackViewPostTagProfileOther(
                                    model.id,
                                    model.postTag.items,
                                    trackingPostModel
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showAfterPostToaster(addAction: Boolean) {
        if (addAction) {
            view?.let {
                Toaster.make(it, getString(R.string.profile_recommend_success), Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL, getString(R.string.profile_add_more), View.OnClickListener {
                    goToAffiliateExplore()
                })
            }
        } else {
            view?.let {
                Toaster.make(it, getString(R.string.profile_recommend_success), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
            }
        }
    }

    private fun showAfterEditToaster() {
        view?.let {
            Toaster.make(it, getString(R.string.profile_edit_success), Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL, getString(com.tokopedia.affiliatecommon.R.string.af_title_ok))
        }
    }

    private fun addFooter(headerViewModel: ProfileHeaderViewModel,
                          affiliatePostQuota: AffiliatePostQuota) {
        footer.visibility = View.VISIBLE
        if (headerViewModel.isOwner) {
            bindCurationQuota(affiliatePostQuota)

            val onClickListener = View.OnClickListener {
                checkShouldChangeUsername(headerViewModel.link) {
                    byMeInstastoryView.setAvatarDrawable(iv_profile.drawable)
                    linkerData = showShareBottomSheet(
                            headerViewModel,
                            String.format(getString(com.tokopedia.feedcomponent.R.string.profile_share_text), headerViewModel.link),
                            String.format(getString(com.tokopedia.feedcomponent.R.string.profile_share_title)),
                            byMeInstastoryView.getTempFileUri()
                    )
                    profileAnalytics.eventClickShareProfileIni(isOwner, userId.toString())
                    isShareProfile = true
                }
            }

            iv_action2_parallax.setOnClickListener(onClickListener)
            iv_action2.setOnClickListener(onClickListener)
        }
    }

    private fun setCreatePostButton(model: ProfileHeaderViewModel) {
        if (model.isOwner && model.isCreatePostToggleOn) {
            fab_create_post.visibility = View.VISIBLE
            fab_create_post.setOnClickListener {
                goToAffiliateExplore()
                profileAnalytics.eventClickTambahRekomendasi()
            }
        } else {
            fab_create_post.visibility = View.GONE
        }

    }

    private fun bindCurationQuota(affiliatePostQuota: AffiliatePostQuota) {
    }

    private fun showAfterPostToast() {
        context?.let {
            Toast.makeText(it, R.string.profile_after_post, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkShouldChangeUsername(link: String, doIfNotChange: () -> Unit) {
        if (shouldChangeUsername()) {
            presenter.shouldChangeUsername(userSession.userId.toIntOrZero(), link)
        } else {
            doIfNotChange()
        }
    }

    private fun shouldChangeUsername(): Boolean = isOwner && profilePreference.shouldChangeUsername()

    private fun getEmptyModelOwner(isShowAffiliateContent: Boolean,
                                   isAffiliate: Boolean): Visitable<*> {
        return if (!isShowAffiliateContent && !isAffiliate) ProfileEmptyViewModel()
        else EmptyAffiliateViewModel()
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
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
            RouteManager.route(requireContext(), ApplinkConst.FEED)
            profileAnalytics.eventClickAfterFollow(profileHeaderViewModel.name)
        }
    }

    private fun createDeleteDialog(rowNumber: Int, id: Int): Dialog {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.profile_delete_post))
        dialog.setDesc(getString(R.string.profile_after_delete_cant))
        dialog.setBtnOk(getString(com.tokopedia.kolcommon.R.string.kol_title_delete))
        dialog.setBtnCancel(getString(com.tokopedia.kolcommon.R.string.kol_title_cancel))
        dialog.setOnOkClickListener {
            presenter.deletePost(id, rowNumber)
            dialog.dismiss()
        }
        dialog.setOnCancelClickListener { dialog.dismiss() }
        return dialog
    }

    private fun goToAffiliateExplore() {
        if (affiliatePreference.isFirstTimeEducation(userSession.userId)) {

            val intent = RouteManager.getIntent(requireContext(),
                    ApplinkConst.DISCOVERY_PAGE.replace("{page_id}", DISCOVERY_BY_ME)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            affiliatePreference.setFirstTimeEducation(userSession.userId)

        } else {
            val intent = RouteManager.getIntent(requireContext(), ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun goToExplore() {
        RouteManager.route(
                requireContext(),
                ApplinkConst.CONTENT_EXPLORE
                        .replace(PARAM_TAB_NAME, TAB_INSPIRASI)
                        .replace(PARAM_CATEGORY_ID, CATEGORY_0)
        )
    }

    private fun goToDashboard() {
        RouteManager.route(requireContext(), ApplinkConstInternalContent.AFFILIATE_DASHBOARD)
        profileAnalytics.eventClickStatistic(userId.toString())
    }

    private fun goToLogin() {
        context?.let {
            startActivityForResult(getLoginIntent(it), LOGIN_CODE)
        }
    }

    private fun followAfterLogin() {
        context?.let {
            startActivityForResult(getLoginIntent(it), LOGIN_FOLLOW_CODE)
        }
    }

    private fun getLoginIntent(context: Context) = RouteManager.getIntent(context, ApplinkConst.LOGIN)

    private fun doFollowAfterLogin() {
        swipeToRefresh.isRefreshing = true
        app_bar_layout.visibility = View.GONE
        presenter.getProfileFirstPage(userId, true)
    }

    private fun showError(message: String) {
        showError(message, null)
    }

    private fun showError(message: String, listener: View.OnClickListener?) {
        listener?.let {
            Toaster.make(view!!, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.title_try_again), it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                val intent = RouteManager.getIntent(
                        requireContext(),
                        ApplinkConstInternalContent.CONTENT_REPORT,
                        contentId.toString()
                )
                startActivityForResult(intent, OPEN_CONTENT_REPORT)
            } else {
                goToLogin()
            }
        }
    }

    private fun onSuccessReportContent() {
        Toaster.make(view!!,
                        getString(R.string.profile_feed_content_reported),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.design.R.string.label_close), View.OnClickListener {  })
    }

    private fun onErrorReportContent(errorMsg: String?) {
        errorMsg?.let {
            Toaster.make(view!!, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.design.R.string.label_close), View.OnClickListener { })
        }
    }

    private fun onGoToLink(link: String) {
        if (context != null && !TextUtils.isEmpty(link)) {
            if (RouteManager.isSupportApplink(requireContext(), link)) {
                RouteManager.route(requireContext(), link)
            } else {
                RouteManager.route(
                        requireContext(),
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
                )
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

    private fun getRelatedProfile() {
        presenter.getRelatedProfile(this::onErrorGetRelatedProfile,
                this::onSuccessGetRelatedProfile)
    }

    private fun onErrorGetRelatedProfile(throwable: Throwable?) {
        view?.let {
            Toaster.make(it, ErrorHandler.getErrorMessage(requireContext(), throwable),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.title_try_again), View.OnClickListener {
                getRelatedProfile()
            })
        }
    }

    private fun onSuccessGetRelatedProfile(feedPostRelated: FeedPostRelated?) {
        val visitables: ArrayList<Visitable<*>> = ArrayList()
        if (feedPostRelated != null && feedPostRelated.meta.totalItems > 0) {
            visitables.add(TitleViewModel())
            feedPostRelated.data
                    .filter { it.content.body.media[0].thumbnail.isNotEmpty() }
                    .also {
                        profileAnalytics.eventImpressionOtherPost(userId.toString(), it, userSession.userId, userSession.name)
                    }
                    .forEachIndexed { index, datum ->
                        visitables.add(OtherRelatedProfileViewModel(datum, index))
                    }
        }
        renderList(visitables, false)
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

    private fun showShareBottomSheets(listener: ShareBottomSheets.OnShareItemClickListener) {
        fragmentManager?.let { fragmentManager ->
            linkerData?.let { linkerData ->
                ShareBottomSheets().show(
                        fragmentManager,
                        linkerData,
                        listener)
            }
        }
        linkerData = null
    }

    private fun showShareBottomSheet(
            element: ProfileHeaderViewModel,
            shareFormat: String,
            shareTitle: String,
            imageUri: String?
    ): LinkerData {
        val bottomSheet = ShareBottomSheets.newInstance(
                this@ProfileFragment,
                element.name,
                element.avatar,
                element.link,
                shareFormat,
                shareTitle,
                imageUri
        ).also {
            fragmentManager?.run {
                it.show(this)
            }
        }

        return bottomSheet.data?: LinkerData()
    }

    private fun sendTracker(packageName: String) {
        if (isShareProfile)
            profileAnalytics.eventClickShareProfileOpsiIni(isOwner, userId.toString(), packageName)
        else
            profileAnalytics.eventClickSharePostOpsiIni(isOwner, userId.toString(), packageName)
    }

    private fun getPostStatisticBottomSheet(): PostStatisticBottomSheet {
        if (!::postStatisticBottomSheet.isInitialized) {
            postStatisticBottomSheet = PostStatisticBottomSheet.newInstance(requireContext())
        }
        return postStatisticBottomSheet
    }

    private fun showPostStatistic(title: String, activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int) {
        getPostStatisticBottomSheet()
                .show(
                        fragmentManager = requireFragmentManager(),
                        activityId = activityId,
                        title = title,
                        productIds = productIds,
                        listener = object : PostStatisticBottomSheet.Listener {
                            override fun onGetPostStatisticModelList(bottomSheet: PostStatisticBottomSheet, activityId: String, productIds: List<String>) {
                                presenter.getPostStatistic(activityId, productIds, likeCount, commentCount)
                            }

                            override fun onSeeMoreDetailClicked(bottomSheet: PostStatisticBottomSheet, type: PostStatisticDetailType) {
                                if (type == PostStatisticDetailType.Comment) {
                                    RouteManager.route(
                                            requireContext(),
                                            ApplinkConst.KOL_COMMENT,
                                            activityId
                                    )
                                    bottomSheet.dismiss()
                                }
                            }
                        }
                )
    }

    override fun onTopAdsViewImpression(bannerId: String, imageUrl: String) {

    }
}

package com.tokopedia.profile.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kol.feature.following_list.view.activity.KolFollowingListActivity
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
import com.tokopedia.profile.ProfileModuleRouter
import com.tokopedia.profile.R
import com.tokopedia.profile.analytics.ProfileAnalytics.Action.CLICK_PROMPT
import com.tokopedia.profile.analytics.ProfileAnalytics.Category.KOL_TOP_PROFILE
import com.tokopedia.profile.analytics.ProfileAnalytics.Event.EVENT_CLICK_TOP_PROFILE
import com.tokopedia.profile.analytics.ProfileAnalytics.Label.GO_TO_FEED_FORMAT
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject


/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View, KolPostListener.View.ViewHolder, KolPostListener.View.Like {

    private var userId: Int = 0

    @Inject
    lateinit var presenter: ProfileContract.Presenter
    lateinit var profileRouter: ProfileModuleRouter

    companion object {
        const val TEXT_PLAIN = "text/plain"
        const val KOL_COMMENT_CODE = 13

        fun createInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        initVar()
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        when (requestCode) {
            KOL_COMMENT_CODE ->
                onSuccessAddDeleteKolComment(
                        data!!.getIntExtra(KolCommentActivity.ARGS_POSITION, -1),
                        data.getIntExtra(KolCommentFragment.ARGS_TOTAL_COMMENT, 0))
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return recyclerView
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerProfileComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(activity!!.application))
                .build()
                .inject(this)
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ProfileTypeFactoryImpl(this, this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadData(page: Int) {
        if (isLoadingInitialData) {
            presenter.getProfileFirstPage(userId)
        } else {
            presenter.getProfilePost(userId)
        }
    }

    override fun getUserSession(): UserSession {
        return UserSession(context)
    }

    override fun getAbstractionRouter(): AbstractionRouter {
        if (context!!.applicationContext is AbstractionRouter) {
            return context!!.applicationContext as AbstractionRouter
        } else {
            throw IllegalStateException("Application must implement "
                    .plus(AbstractionRouter::class.java.simpleName))
        }
    }

    override fun onSuccessGetProfileFirstPage(firstPageViewModel: ProfileFirstPageViewModel) {
        if (!TextUtils.isEmpty(firstPageViewModel.profileHeaderViewModel.affiliateName)) {
            addFooter(
                    firstPageViewModel.profileHeaderViewModel,
                    firstPageViewModel.affiliatePostQuota.formatted
            )
        }

        val visitables: ArrayList<Visitable<*>> = ArrayList()
        visitables.add(firstPageViewModel.profileHeaderViewModel)
        visitables.addAll(firstPageViewModel.visitableList)
        renderList(visitables, !TextUtils.isEmpty(firstPageViewModel.lastCursor))
    }

    override fun goToFollowing() {
        startActivity(KolFollowingListActivity.getCallingIntent(context, userId))
    }

    override fun followUnfollowUser(userId: Int, follow: Boolean) {
        if (follow) {
            presenter.followKol(userId)
        } else {
            presenter.unfollowKol(userId)
        }
    }

    override fun goToProduct(productId: Int) {
        //TODO milhamj
        Toast.makeText(context, "Going to product~", Toast.LENGTH_SHORT).show()
    }

    override fun addImages(productId: Int) {
        //TODO milhamj
        Toast.makeText(context, "Adding more images", Toast.LENGTH_SHORT).show()
    }

    override fun updateCursor(cursor: String) {
        presenter.cursor = cursor
    }

    override fun onLikeKolSuccess(rowNumber: Int) {
        if (adapter.data[rowNumber] != null && adapter.data[rowNumber] is KolPostViewModel) {
            val kolPostViewModel = adapter.data[rowNumber] as KolPostViewModel
            kolPostViewModel.isLiked = !kolPostViewModel.isLiked
            if (kolPostViewModel.isLiked) {
                kolPostViewModel.totalLike = kolPostViewModel.totalLike + 1
            } else {
                kolPostViewModel.totalLike = kolPostViewModel.totalLike - 1
            }
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_LIKE)

            //TODO milhamj
//            if (activity != null &&
//                    arguments != null &&
//                    arguments!!.getInt(PARAM_POST_ID, -1) == kolPostViewModel.kolId) {
//
//                if (resultIntent == null) {
//                    resultIntent = Intent()
//                    resultIntent.putExtras(arguments!!)
//                }
//                resultIntent.putExtra(
//                        PARAM_IS_LIKED,
//                        if (kolPostViewModel.isLiked) IS_LIKE_TRUE else IS_LIKE_FALSE)
//                resultIntent.putExtra(PARAM_TOTAL_LIKES, kolPostViewModel.totalLike)
//                activity!!.setResult(Activity.RESULT_OK, resultIntent)
//            }
        }
    }

    override fun onLikeKolError(message: String?) = showError(message)

    override fun onGoToKolProfile(rowNumber: Int, userId: String?, postId: Int) {

    }

    override fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String?) {

    }

    override fun onOpenKolTooltip(rowNumber: Int, url: String) {
        profileRouter.openRedirectUrl(activity as Activity, url)
    }

    override fun onFollowKolClicked(rowNumber: Int, id: Int) {

    }

    override fun onUnfollowKolClicked(rowNumber: Int, id: Int) {

    }

    override fun onLikeKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
        } else {
            startActivity(profileRouter.getLoginIntent(context!!))
        }
    }

    override fun onUnlikeKolClicked(rowNumber: Int, id: Int) {
        if (userSession.isLoggedIn) {
            presenter.likeKol(id, rowNumber, this)
        } else {
            startActivity(profileRouter.getLoginIntent(context!!))
        }
    }

    override fun onGoToKolComment(rowNumber: Int, id: Int) {
        val intent = KolCommentActivity.getCallingIntent(
                context, id, rowNumber
        )
        startActivityForResult(intent, KOL_COMMENT_CODE)
    }

    override fun onEditClicked(rowNumber: Int, id: Int) {

    }

    override fun onSuccessFollowKol() {
        if (adapter.isContainData && adapter.data.first() is ProfileHeaderViewModel) {
            val profileHeaderViewModel = adapter.data.first() as ProfileHeaderViewModel
            profileHeaderViewModel.isFollowed = !profileHeaderViewModel.isFollowed

            try {
                var followers = profileHeaderViewModel.followers.toInt()
                followers += if (profileHeaderViewModel.isFollowed) 1 else -1
                profileHeaderViewModel.followers = followers.toString()
            } catch (e: NumberFormatException) {
            }

            if (profileHeaderViewModel.isFollowed) {
                ToasterNormal
                        .make(view,
                                getString(R.string.follow_success_toast),
                                BaseToaster.LENGTH_LONG)
                        .setAction(getString(R.string.follow_success_check_now),
                                followSuccessOnClickListener(profileHeaderViewModel))
                        .show()
            }
            adapter.notifyItemChanged(0, ProfileHeaderViewHolder.PAYLOAD_FOLLOW)
        }
    }

    override fun onErrorFollowKol(errorMessage: String) {
        showError(errorMessage)
    }

    private fun initVar() {
        arguments?.let {
            userId = it.getString(ProfileActivity.EXTRA_PARAM_USER_ID, ProfileActivity.ZERO).toInt()
        }
        if (context!!.applicationContext is ProfileModuleRouter) {
            profileRouter = context!!.applicationContext as ProfileModuleRouter
        } else {
            throw IllegalStateException("Application must implement "
                    .plus(ProfileModuleRouter::class.java.simpleName))
        }
    }

    private fun initView() {

    }

    private fun addFooter(headerViewModel: ProfileHeaderViewModel, quota: String) {
        footer.visibility = View.VISIBLE
        if (headerViewModel.isOwner) {
            footerOwn.visibility = View.VISIBLE
            footerOther.visibility = View.GONE

            if (!TextUtils.isEmpty(quota)) {
                recommendationQuota.visibility = View.VISIBLE
                recommendationQuota.text = quota
            } else {
                recommendationQuota.visibility = View.GONE
            }
            addRecommendation.setOnClickListener {
                RouteManager.route(context, ApplinkConst.AFFILIATE_EXPLORE)
            }
            shareOwn.setOnClickListener(shareLink(headerViewModel.link))
        } else {
            footerOwn.visibility = View.GONE
            footerOther.visibility = View.VISIBLE

            shareOther.setOnClickListener(shareLink(headerViewModel.link))
        }
    }

    private fun shareLink(link: String): View.OnClickListener {
        return View.OnClickListener {
            val shareBody = String.format(getString(R.string.profile_share_text), link)
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = TEXT_PLAIN
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(
                    Intent.createChooser(sharingIntent, getString(R.string.profile_share_title))
            )
        }
    }

    private fun showError(message: String?) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(activity)
        } else {
            NetworkErrorHelper.showSnackbar(activity, message)
        }
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        if (rowNumber != -1 &&
                adapter.data[rowNumber] != null &&
                adapter.data[rowNumber] is KolPostViewModel) {
            val kolPostViewModel = adapter.data[rowNumber] as KolPostViewModel
            kolPostViewModel.totalComment = kolPostViewModel.totalComment + totalNewComment
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_COMMENT)

            //TODO milhamj
//            if (activity != null &&
//                    arguments != null &&
//                    arguments!!.getInt(PARAM_POST_ID, -1) == kolPostViewModel.kolId) {
//
//                if (resultIntent == null) {
//                    resultIntent = Intent()
//                    resultIntent.putExtras(arguments!!)
//                }
//                resultIntent.putExtra(PARAM_TOTAL_COMMENTS, kolPostViewModel.totalComment)
//                activity!!.setResult(Activity.RESULT_OK, resultIntent)
//            }
        }
    }

    private fun followSuccessOnClickListener(profileHeaderViewModel: ProfileHeaderViewModel)
            : View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(getContext(), ApplinkConst.FEED)
            abstractionRouter.analyticTracker.sendEventTracking(
                    EVENT_CLICK_TOP_PROFILE,
                    KOL_TOP_PROFILE,
                    CLICK_PROMPT,
                    String.format(GO_TO_FEED_FORMAT, profileHeaderViewModel.name)
            )
        }
    }
}
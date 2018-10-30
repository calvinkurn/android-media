package com.tokopedia.profile.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentFragment
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.fragment.KolPostFragment.*
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity.PARAM_POST_ID
import com.tokopedia.profile.ProfileModuleRouter
import com.tokopedia.profile.R
import com.tokopedia.profile.analytics.ProfileAnalytics.Action.CLICK_PROMPT
import com.tokopedia.profile.analytics.ProfileAnalytics.Category.KOL_TOP_PROFILE
import com.tokopedia.profile.analytics.ProfileAnalytics.Event.EVENT_CLICK_TOP_PROFILE
import com.tokopedia.profile.analytics.ProfileAnalytics.Label.GO_TO_FEED_FORMAT
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota
import com.tokopedia.profile.di.DaggerProfileComponent
import com.tokopedia.profile.view.activity.FollowingListActivity
import com.tokopedia.profile.view.activity.ProfileActivity
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.viewmodel.ProfileFirstPageViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
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
        ProfileContract.View, KolPostListener.View.ViewHolder, KolPostListener.View.Like {

    private var userId: Int = 0
    private var afterPost: Boolean = false
    private var onlyOnePost: Boolean = false
    private var isAffiliate: Boolean = false
    private var resultIntent: Intent? = null
    private var affiliatePostQuota: AffiliatePostQuota? = null

    override lateinit var profileRouter: ProfileModuleRouter

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    companion object {
        private const val POST_ID = "{post_id}"
        private const val PARAM_TAB_NAME = "{tab_name}"
        private const val PARAM_CATEGORY_ID = "{category_id}"
        private const val TAB_INSPIRASI = "inspirasi"
        private const val CATEGORY_0 = "0"
        private const val TEXT_PLAIN = "text/plain"
        private const val KOL_COMMENT_CODE = 13
        private const val SETTING_PROFILE_CODE = 83
        private const val ONBOARDING_CODE = 10
        private const val EDIT_POST_CODE = 1310

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
        initVar()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (userSession.userId == userId.toString()) {
            inflater?.inflate(R.menu.menu_profile, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_dashboard) {
            if (isAffiliate) goToDashboard()
            else goToOnboading()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
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
            SETTING_PROFILE_CODE, ONBOARDING_CODE, EDIT_POST_CODE -> {
                onSwipeRefresh()
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
        footerOther.visibility = View.GONE
        super.onSwipeRefresh()
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            hideLoadingLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        hideLoadingLayout()
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

    override fun getUserSession(): UserSession = UserSession(context)

    override fun getAbstractionRouter(): AbstractionRouter {
        if (context!!.applicationContext is AbstractionRouter) {
            return context!!.applicationContext as AbstractionRouter
        } else {
            throw IllegalStateException("Application must implement "
                    .plus(AbstractionRouter::class.java.simpleName))
        }
    }

    override fun onSuccessGetProfileFirstPage(firstPageViewModel: ProfileFirstPageViewModel) {
        presenter.cursor = firstPageViewModel.lastCursor
        onlyOnePost = firstPageViewModel.visitableList.size == 1
        isAffiliate = firstPageViewModel.profileHeaderViewModel.isAffiliate
        affiliatePostQuota = firstPageViewModel.affiliatePostQuota
        setHasOptionsMenu(true)

        if (firstPageViewModel.profileHeaderViewModel.isAffiliate) {
            setToolbarTitle(firstPageViewModel.profileHeaderViewModel.affiliateName)
            addFooter(
                    firstPageViewModel.profileHeaderViewModel,
                    firstPageViewModel.affiliatePostQuota
            )
        }

        val visitables: ArrayList<Visitable<*>> = ArrayList()
        visitables.add(firstPageViewModel.profileHeaderViewModel)
        if (!firstPageViewModel.visitableList.isEmpty()) {
            visitables.addAll(firstPageViewModel.visitableList)
        } else {
            visitables.add(getEmptyModel(
                    firstPageViewModel.profileHeaderViewModel.isOwner,
                    firstPageViewModel.profileHeaderViewModel.isAffiliate)
            )
        }
        renderList(visitables, !TextUtils.isEmpty(firstPageViewModel.lastCursor))

        if (afterPost && !onlyOnePost) {
            ToasterNormal
                    .make(view,
                            getString(R.string.profile_recommend_success),
                            BaseToaster.LENGTH_LONG
                    )
                    .setAction(getString(R.string.profile_add_more)) {
                        goToAffiliateExplore()
                    }
                    .show()
            afterPost = false
        }
    }

    override fun onSuccessGetProfilePost(visitables: List<Visitable<*>>, lastCursor: String) {
        presenter.cursor = lastCursor
        renderList(visitables, !TextUtils.isEmpty(lastCursor))
    }

    override fun goToFollowing() {
        startActivity(FollowingListActivity.createIntent(context, userId.toString()))
    }

    override fun followUnfollowUser(userId: Int, follow: Boolean) {
        if (userSession.isLoggedIn) {
            if (follow) {
                presenter.followKol(userId)
            } else {
                presenter.unfollowKol(userId)
            }
        } else {
            startActivity(profileRouter.getLoginIntent(context!!))
        }
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

            if (activity != null &&
                    arguments != null &&
                    arguments!!.getInt(PARAM_POST_ID, -1) == kolPostViewModel.contentId) {

                if (resultIntent == null) {
                    resultIntent = Intent()
                    resultIntent!!.putExtras(arguments!!)
                }
                resultIntent!!.putExtra(
                        PARAM_IS_LIKED,
                        if (kolPostViewModel.isLiked) IS_LIKE_TRUE else IS_LIKE_FALSE)
                resultIntent!!.putExtra(PARAM_TOTAL_LIKES, kolPostViewModel.totalLike)
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

    override fun onEditClicked(id: Int) {
        startActivityForResult(
                RouteManager.getIntent(
                        context,
                        ApplinkConst.AFFILIATE_EDIT_POST.replace(POST_ID, id.toString())
                ),
                EDIT_POST_CODE
        )
    }

    override fun onMenuClicked(rowNumber: Int, element: BaseKolViewModel) {
        val menus = Menus(context!!)
        val menuList = ArrayList<String>()
        if (element.isDeletable) {
            menuList.add(getString(R.string.profile_delete_post))
        }
        menus.setItemMenuList(menuList.toTypedArray())
        menus.setActionText(getString(R.string.close))
        menus.setOnActionClickListener { menus.dismiss() }
        menus.setOnItemMenuClickListener { itemMenus, _ ->
            when (itemMenus.title) {
                getString(R.string.profile_delete_post) ->
                    createDeleteDialog(rowNumber, element.contentId).show()
            }
            menus.dismiss()
        }
        menus.show()
    }

    override fun onSuccessFollowKol() {
        if (adapter.dataSize > 0 && adapter.data.first() is ProfileHeaderViewModel) {
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

            if (activity != null && arguments != null) {
                if (resultIntent == null) {
                    resultIntent = Intent()
                    resultIntent!!.putExtras(arguments!!)
                }
                resultIntent!!.putExtra(
                        ProfileActivity.PARAM_IS_FOLLOWING,
                        if (profileHeaderViewModel.isFollowed) ProfileActivity.IS_FOLLOWING_TRUE
                        else ProfileActivity.IS_FOLLOWING_FALSE
                )
                activity!!.setResult(Activity.RESULT_OK, resultIntent)
            }
        }
    }

    override fun onErrorFollowKol(errorMessage: String) = showError(errorMessage)

    override fun onSuccessDeletePost(rowNumber: Int) {
        adapter.data.removeAt(rowNumber)
        adapter.notifyItemRemoved(rowNumber)

        affiliatePostQuota?.let {
            it.number += 1
            try {
                recommendationQuota.text = String.format(it.format, it.number)
            } catch (e: IllegalFormatException) {
            } catch (e: NullPointerException) {
            }
        }


        val snackbar = ToasterNormal.make(view,
                getString(R.string.profile_post_deleted),
                BaseToaster.LENGTH_LONG
        )
        snackbar.setAction(R.string.af_title_ok) { snackbar.dismiss() }.show()
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

    private fun initVar() {
        arguments?.let {
            userId = it.getString(ProfileActivity.EXTRA_PARAM_USER_ID, ProfileActivity.ZERO).toInt()
            afterPost = TextUtils.equals(
                    it.getString(ProfileActivity.EXTRA_PARAM_AFTER_POST, ""),
                    ProfileActivity.TRUE
            )
        }
        if (context!!.applicationContext is ProfileModuleRouter) {
            profileRouter = context!!.applicationContext as ProfileModuleRouter
        } else {
            throw IllegalStateException("Application must implement "
                    .plus(ProfileModuleRouter::class.java.simpleName))
        }
    }

    private fun setToolbarTitle(title: String) {
        if (activity != null && activity is AppCompatActivity && !TextUtils.isEmpty(title)) {
            (activity as AppCompatActivity).supportActionBar?.title = title
        }
    }

    private fun addFooter(headerViewModel: ProfileHeaderViewModel,
                          affiliatePostQuota: AffiliatePostQuota) {
        footer.visibility = View.VISIBLE
        if (headerViewModel.isOwner) {
            footerOwn.visibility = View.VISIBLE
            footerOther.visibility = View.GONE

            if (!TextUtils.isEmpty(affiliatePostQuota.formatted) && affiliatePostQuota.number > 0) {
                recommendationQuota.visibility = View.VISIBLE
                recommendationQuota.text = affiliatePostQuota.formatted
            } else {
                recommendationQuota.visibility = View.GONE
            }
            addRecommendation.setOnClickListener {
                goToAffiliateExplore()
            }
            addRecommendation.setOnLongClickListener {
                showToast(getString(R.string.profile_add_recommendation))
                true
            }

            shareOwn.setOnClickListener(shareLink(headerViewModel.link))
            shareOwn.setOnLongClickListener {
                showToast(getString(R.string.profile_share_profile))
                true
            }

            showShowCaseDialog(shareOwn)
        } else {
            footerOwn.visibility = View.GONE
            footerOther.visibility = View.VISIBLE

            shareOther.setOnClickListener(shareLink(headerViewModel.link))
            shareOther.setOnLongClickListener {
                showToast(getString(R.string.profile_share_profile))
                true
            }
        }
    }

    private fun showShowCaseDialog(view: View?) {
        if (afterPost && onlyOnePost) {
            val showCaseTag = this::class.java.simpleName
            val showCaseDialog = createShowCaseDialog()
            val showcases = ArrayList<ShowCaseObject>()
            showcases.add(ShowCaseObject(
                    view,
                    getString(R.string.profile_showcase_title),
                    getString(R.string.profile_showcase_description),
                    ShowCaseContentPosition.UNDEFINED))
            showCaseDialog.show(this.activity, showCaseTag, showcases)
            afterPost = false
        }
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

    private fun shareLink(link: String): View.OnClickListener {
        return View.OnClickListener {
            val shareBody = String.format(getString(R.string.profile_share_text), link)
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = TEXT_PLAIN
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(
                    Intent.createChooser(sharingIntent, getString(R.string.profile_share_profile))
            )
        }
    }

    private fun getEmptyModel(isOwner: Boolean, isAffiliate: Boolean): Visitable<*> {
        val emptyResultViewModel = EmptyResultViewModel()
        emptyResultViewModel.iconRes = R.drawable.ic_af_empty
        if (isOwner) {
            if (isAffiliate) {
                emptyResultViewModel.title = getString(R.string.profile_add_recommendation)
            } else {
                emptyResultViewModel.title = getString(R.string.profile_recommend_to_friends)
                emptyResultViewModel.buttonTitle = getString(R.string.profile_find_out)
                emptyResultViewModel.callback = object : BaseEmptyViewHolder.Callback {
                    override fun onEmptyContentItemTextClicked() {
                    }

                    override fun onEmptyButtonClicked() {
                        goToOnboading()
                    }
                }
            }
        } else {
            emptyResultViewModel.title = getString(R.string.profile_no_content)
            emptyResultViewModel.buttonTitle = getString(R.string.profile_see_others)
            emptyResultViewModel.callback = object : BaseEmptyViewHolder.Callback {
                override fun onEmptyContentItemTextClicked() {
                }

                override fun onEmptyButtonClicked() {
                    goToExplore()
                }
            }
        }
        return emptyResultViewModel
    }

    private fun onSuccessAddDeleteKolComment(rowNumber: Int, totalNewComment: Int) {
        if (rowNumber != -1 &&
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
    }

    private fun followSuccessOnClickListener(profileHeaderViewModel: ProfileHeaderViewModel)
            : View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(context, ApplinkConst.FEED)
            abstractionRouter.analyticTracker.sendEventTracking(
                    EVENT_CLICK_TOP_PROFILE,
                    KOL_TOP_PROFILE,
                    CLICK_PROMPT,
                    String.format(GO_TO_FEED_FORMAT, profileHeaderViewModel.name)
            )
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

    private fun goToOnboading() {
        val intent = RouteManager.getIntent(context, ApplinkConst.AFFILIATE_ONBOARDING)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, ONBOARDING_CODE)
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
}
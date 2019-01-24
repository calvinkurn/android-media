package com.tokopedia.talk.shoptalk.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.R
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.view.TalkDialog
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.shoptalk.di.DaggerShopTalkComponent
import com.tokopedia.talk.shoptalk.view.activity.ShopTalkActivity
import com.tokopedia.talk.shoptalk.view.listener.ShopTalkContract
import com.tokopedia.talk.shoptalk.view.presenter.ShopTalkPresenter
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_talk_shop.*
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 9/17/18.
 */

class ShopTalkFragment : BaseDaggerFragment(), ShopTalkContract.View,
        InboxTalkItemViewHolder.TalkItemListener, CommentTalkViewHolder.TalkCommentItemListener, TalkProductAttachmentAdapter.ProductAttachmentItemClickListener, LoadMoreCommentTalkViewHolder.LoadMoreListener {

    private val REQUEST_REPORT_TALK: Int = 101
    private val REQUEST_GO_TO_DETAIL: Int = 102

    private lateinit var alertDialog: Dialog
    private lateinit var adapter: InboxTalkAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bottomMenu: Menus

    private var shopId: String = ""

    @Inject
    lateinit var talkDialog: TalkDialog

    @Inject
    lateinit var presenter: ShopTalkPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: TalkAnalytics

    private lateinit var performanceMonitoring: PerformanceMonitoring

    private var isTraceStopped: Boolean = false

    companion object {

        const val TALK_SHOP_TRACE = "talk_list_shop"
        fun newInstance(bundle: Bundle): ShopTalkFragment {
            val fragment = ShopTalkFragment()
            fragment.arguments = bundle
            return fragment
        }


    }

    override fun initInjector() {
        val shopTalkComponent = DaggerShopTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        shopTalkComponent.inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_SHOP_TALK
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.sendScreen(this, screenName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(TALK_SHOP_TRACE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.run {
            shopId = savedInstanceState.getString(ShopTalkActivity.EXTRA_SHOP_ID, "")
        } ?: arguments?.run {
            shopId = getString(ShopTalkActivity.EXTRA_SHOP_ID, "")
        } ?: activity?.run {
            finish()
        }

        setupView()
        presenter.getShopTalk(shopId)
    }

    private fun setupView() {
        val adapterTypeFactory = InboxTalkTypeFactoryImpl(this, this, this, this)
        val listTalk = ArrayList<Visitable<*>>()
        adapter = InboxTalkAdapter(adapterTypeFactory, listTalk)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        talk_rv.layoutManager = linearLayoutManager
        talk_rv.adapter = adapter
        talk_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val index = linearLayoutManager.findLastVisibleItemPosition()
                if (index != -1 && adapter.checkCanLoadMore(index)) {
                    presenter.getShopTalk(shopId)
                }
            }

        })
        swipeToRefresh.setOnRefreshListener { onRefreshData() }
    }

    private fun onRefreshData() {
        swipeToRefresh.isRefreshing = true
        presenter.refreshTalk(shopId)
    }

    override fun hideRefreshLoad() {
        swipeToRefresh.isRefreshing = false
    }

    override fun onSuccessRefreshTalk(listTalk: ArrayList<Visitable<*>>) {
        adapter.clearAllElements()
        adapter.setList(listTalk)
    }

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun hideLoading() {
        adapter.hideLoading()
    }

    override fun onEmptyTalk() {
        adapter.clearAllElements()
        adapter.showEmpty()
    }

    override fun onSuccessGetShopTalk(talkViewModel: InboxTalkViewModel) {
        adapter.hideEmpty()
        adapter.addList(talkViewModel.listTalk)
        stopTrace()
    }

    fun stopTrace() {
        if (!isTraceStopped) {
            performanceMonitoring.stopTrace()
            isTraceStopped = true
        }
    }

    override fun onErrorGetShopTalk(errorMessage: String) {
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
                presenter.getShopTalk(shopId)
            }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
                presenter.getShopTalk(shopId)
            }.showRetrySnackbar()
        }
    }

    override fun onErrorActionTalk(errorMessage: String) {
        NetworkErrorHelper.showRedSnackbar(view, errorMessage)
    }

    override fun onItemTalkClick(allowReply: Boolean, talkId: String, shopId: String) {
        if (userSession.isLoggedIn) {
            goToDetailTalk(talkId, shopId, allowReply)
        } else {
            goToLogin()
        }
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        if (userSession.isLoggedIn) {
            analytics.trackClickReplyButtonFromShop(talkId)
            goToDetailTalk(talkId, shopId, allowReply)
        } else {
            goToLogin()
        }
    }

    private fun goToLogin() {
        context?.applicationContext?.run {
            val intent = (this as TalkRouter).getLoginIntent(this)
            this@ShopTalkFragment.startActivity(intent)
        }
    }

    private fun goToDetailTalk(talkId: String, shopId: String, allowReply: Boolean) {
        if (allowReply) {
            context?.run {
                this@ShopTalkFragment.startActivityForResult(
                        TalkDetailsActivity.getCallingIntent(talkId, shopId, this,
                                TalkDetailsActivity.SOURCE_SHOP)
                        , REQUEST_GO_TO_DETAIL)
            }
        } else {
            showErrorReplyTalk()
        }
    }

    private fun showErrorReplyTalk() {
        NetworkErrorHelper.showRedSnackbar(view, getString(R.string
                .error_default_cannot_reply_talk))
    }

    override fun onMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, productId: String) {
        if (userSession.isLoggedIn) {
            context?.run {
                val listMenu = ArrayList<Menus.ItemMenus>()
                if (menu.allowDelete) listMenu.add(Menus.ItemMenus(getString(R.string
                        .menu_delete_talk), -1))
                if (menu.allowUnfollow) listMenu.add(Menus.ItemMenus(getString(R.string
                        .menu_unfollow_talk), -1))
                if (menu.allowFollow) listMenu.add(Menus.ItemMenus(getString(R.string
                        .menu_follow_talk), -1))
                if (menu.allowReport) listMenu.add(Menus.ItemMenus(getString(R.string
                        .menu_report_talk), -1))

                if (!::bottomMenu.isInitialized) bottomMenu = Menus(this)
                bottomMenu.itemMenuList = listMenu
                bottomMenu.setActionText(getString(R.string.button_cancel))
                bottomMenu.setOnActionClickListener { bottomMenu.dismiss() }
                bottomMenu.setOnItemMenuClickListener { itemMenus, _ ->
                    onMenuItemClicked(itemMenus, bottomMenu, shopId, talkId, productId)
                }
                bottomMenu.show()
            }
        } else {
            goToLogin()
        }
    }

    private fun onMenuItemClicked(itemMenus: Menus.ItemMenus, bottomMenu: Menus, shopId: String,
                                  talkId: String, productId: String) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        when (itemMenus.title) {
            getString(R.string.menu_delete_talk) -> showDeleteTalkDialog(alertDialog, shopId,
                    talkId)
            getString(R.string.menu_follow_talk) -> showFollowTalkDialog(alertDialog, talkId)
            getString(R.string.menu_unfollow_talk) -> showUnfollowTalkDialog(alertDialog, talkId)
            getString(R.string.menu_report_talk) -> {
                analytics.trackClickOnMenuReport()
                goToReportTalk(talkId, shopId, productId, "")
            }
        }
        bottomMenu.dismiss()
    }


    private fun goToReportTalk(talkId: String, shopId: String,
                               productId: String, commentId: String) {
        activity?.run {
            val intent: Intent = if (commentId.isBlank()) {
                ReportTalkActivity.createIntentReportTalk(this, talkId, shopId, productId)
            } else {
                ReportTalkActivity.createIntentReportComment(this, talkId, commentId,
                        shopId, productId)
            }
            this@ShopTalkFragment.startActivityForResult(intent, REQUEST_REPORT_TALK)
        }
    }

    private fun showUnfollowTalkDialog(alertDialog: Dialog, talkId: String) {
        context?.run {
            analytics.trackClickOnMenuUnfollow()
            talkDialog.createUnfollowTalkDialog(
                    this,
                    alertDialog,
                    View.OnClickListener {
                        alertDialog.dismiss()
                        presenter.unfollowTalk(talkId)
                    }
            ).show()
        }
    }

    private fun showFollowTalkDialog(alertDialog: Dialog, talkId: String) {
        context?.run {
            analytics.trackClickOnMenuFollow()
            talkDialog.createFollowTalkDialog(
                    this,
                    alertDialog,
                    View.OnClickListener {
                        alertDialog.dismiss()
                        presenter.followTalk(talkId)
                    }
            ).show()
        }
    }

    private fun showDeleteTalkDialog(alertDialog: Dialog, shopId: String, talkId: String) {
        context?.run {
            analytics.trackClickOnMenuDelete()
            talkDialog.createDeleteTalkDialog(
                    this,
                    alertDialog,
                    View.OnClickListener {
                        alertDialog.dismiss()
                        presenter.deleteTalk(shopId, talkId)
                    }
            ).show()
        }
    }

    private fun showDeleteCommentTalkDialog(shopId: String, talkId: String, commentId: String) {

        analytics.trackClickOnMenuDelete()

        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        context?.run {
            talkDialog.createDeleteCommentTalkDialog(
                    this,
                    alertDialog,
                    View.OnClickListener {
                        alertDialog.dismiss()
                        presenter.deleteCommentTalk(shopId, talkId, commentId)
                    }
            ).show()
        }
    }

    override fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String) {
        goToReportTalk(talkId, shopId, productId, "")
    }

    override fun onNoShowTalkItemClick(talkId: String) {
        presenter.markTalkNotFraud(talkId)
    }

    override fun onGoToPdpFromProductItemHeader(productId: String) {
        analytics.trackClickProduct()
        onGoToPdp(productId)
    }

    private fun onGoToPdp(productId: String) {
        activity?.applicationContext?.run {
            analytics.trackClickProduct()
            val intent: Intent = (this as TalkRouter).getProductPageIntent(this, productId)
            this@ShopTalkFragment.startActivity(intent)
        }
    }

    override fun onGoToUserProfile(userId: String) {
        analytics.trackClickUserProfileFromShop()
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getTopProfileIntent(this, userId)
            this@ShopTalkFragment.startActivity(intent)
        }
    }

    override fun onCommentMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, commentId: String, productId: String) {
        if (userSession.isLoggedIn) {
            context?.run {
                val listMenu = ArrayList<Menus.ItemMenus>()
                if (menu.allowReport) {
                    listMenu.add(Menus.ItemMenus(getString(R.string
                            .menu_report_comment), -1))
                }
                if (menu.allowDelete) {
                    listMenu.add(Menus.ItemMenus(getString(R.string
                            .menu_delete_comment), -1))
                }

                if (!::bottomMenu.isInitialized) bottomMenu = Menus(this)
                bottomMenu.itemMenuList = listMenu
                bottomMenu.setActionText(getString(R.string.button_cancel))
                bottomMenu.setOnActionClickListener { bottomMenu.dismiss() }
                bottomMenu.setOnItemMenuClickListener { itemMenus, _ ->
                    onCommentMenuItemClicked(itemMenus, bottomMenu, shopId, talkId, commentId, productId)
                }
                bottomMenu.show()
            }
        } else {
            goToLogin()
        }
    }

    private fun onCommentMenuItemClicked(itemMenu: Menus.ItemMenus, bottomMenu: Menus,
                                         shopId: String, talkId: String, commentId: String,
                                         productId: String) {
        when (itemMenu.title) {
            getString(R.string.menu_report_comment) -> {
                analytics.trackClickOnMenuReport()
                goToReportTalk(talkId, shopId, productId, commentId)
            }
            getString(R.string.menu_delete_comment) -> showDeleteCommentTalkDialog(shopId,
                    talkId, commentId)
        }
        bottomMenu.dismiss()
    }

    override fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String, commentId: String) {
        goToReportTalk(talkId, shopId, productId, commentId)
    }

    override fun onNoShowTalkCommentClick(talkId: String, commentId: String) {
        presenter.markCommentNotFraud(talkId, commentId)
    }

    override fun onGoToShopPage(shopId: String) {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getShopPageIntent(this, shopId)
            this@ShopTalkFragment.startActivity(intent)
        }
    }

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
        analytics.trackClickProductFromAttachmentFromShop()
        onGoToPdp(attachProduct.productId.toString())
    }

    override fun onLoadMoreCommentClicked(talkId: String, shopId: String, allowReply: Boolean) {
        goToDetailTalk(talkId, shopId, allowReply)
    }

    override fun showLoadingAction() {
        progressBar.visibility = View.VISIBLE
        talk_rv.visibility = View.GONE
        swipeToRefresh.isEnabled = false
    }

    override fun hideLoadingAction() {
        progressBar.visibility = View.GONE
        talk_rv.visibility = View.VISIBLE
        swipeToRefresh.isEnabled = true
    }

    override fun onSuccessMarkTalkNotFraud(talkId: String) {
        adapter.showReportedTalk(talkId)
    }

    override fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String) {
        adapter.showReportedCommentTalk(talkId, commentId)
    }

    override fun onSuccessDeleteTalk(talkId: String) {
        adapter.deleteTalkByTalkId(talkId)
        NetworkErrorHelper.showGreenSnackbar(view, getString(R.string.success_delete_comment_talk))

    }

    override fun onSuccessDeleteCommentTalk(talkId: String, commentId: String) {
        adapter.deleteComment(talkId, commentId)
        NetworkErrorHelper.showGreenSnackbar(view, getString(R.string.success_delete_comment_talk))

    }

    override fun onSuccessUnfollowTalk(talkId: String) {
        adapter.setStatusFollow(talkId, false)
        NetworkErrorHelper.showGreenSnackbar(view, getString(R.string.success_unfollow_talk))

    }

    override fun onSuccessFollowTalk(talkId: String) {
        adapter.setStatusFollow(talkId, true)
        NetworkErrorHelper.showGreenSnackbar(view, getString(R.string.success_follow_talk))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ShopTalkActivity.EXTRA_SHOP_ID, shopId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_REPORT_TALK && resultCode == Activity.RESULT_OK) {
            data?.extras?.run {
                val talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
                onSuccessReportTalk(talkId)

            }
        } else if (requestCode == REQUEST_GO_TO_DETAIL) {
            data?.run {
                when (resultCode) {
                    TalkDetailsActivity.RESULT_OK_DELETE_TALK -> updateDeleteTalk(data)
                    TalkDetailsActivity.RESULT_OK_DELETE_COMMENT -> updateDeleteComment(data)
                    TalkDetailsActivity.RESULT_OK_REFRESH_TALK -> onRefreshData()
                    else -> {
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun updateDeleteTalk(data: Intent) {
        val talkId = data.getStringExtra(TalkDetailsActivity.THREAD_TALK_ID)

        if (!talkId.isEmpty()) {
            adapter.getItemById(talkId)?.run {
                adapter.deleteTalkByTalkId(talkId)
            }

        } else {
            onRefreshData()
        }
    }

    private fun updateDeleteComment(data: Intent) {
        val talkId = data.getStringExtra(TalkDetailsActivity.THREAD_TALK_ID)
        val commentId = data.getStringExtra(TalkDetailsActivity.COMMENT_ID)

        if (!talkId.isEmpty() && !commentId.isEmpty()) {

            if (adapter.getCommentById(talkId, commentId) != null) {
                adapter.deleteComment(talkId, commentId)
            } else if (adapter.getItemById(talkId) != null
                    && !(adapter.getItemById(talkId) as InboxTalkItemViewModel).talkThread
                            .listChild.isEmpty()
                    && (adapter.getItemById(talkId) as InboxTalkItemViewModel)
                            .talkThread.listChild[0] is LoadMoreCommentTalkViewModel) {
                ((adapter.getItemById(talkId) as
                        InboxTalkItemViewModel).talkThread.listChild[0] as
                        LoadMoreCommentTalkViewModel).counter -= 1

                //TODO : Actually just need to update this item
                adapter.updateReadStatus(talkId)
            }

        } else {
            onRefreshData()
        }

    }

    private fun onSuccessReportTalk(talkId: String) {
        activity?.run {
            NetworkErrorHelper.showGreenSnackbar(this, getString(R.string.success_report_talk))
        }

        onRefreshData()
    }


    override fun shouldHandleUrlManually(url: String): Boolean {
        val urlManualHandlingList = arrayOf("tkp.me", "tokopedia.me", "tokopedia.link")
        return Arrays.asList(*urlManualHandlingList).contains(url)
    }

    override fun onGoToWebView(url: String, id: String) {
        if (url.isNotEmpty() && activity != null) {
            KeyboardHandler.DropKeyboard(activity, view)

            when {
                RouteManager.isSupportApplink(activity, url) -> RouteManager.route(activity, url)
                isBranchIOLink(url) -> handleBranchIOLinkClick(url)
                else -> {
                    val applinkRouter = activity!!.applicationContext as ApplinkRouter
                    applinkRouter.goToApplinkActivity(activity,
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
                }
            }
        }
    }

    override fun handleBranchIOLinkClick(url: String) {
        activity?.run {
            val talkRouter = this.applicationContext as TalkRouter
            val intent = talkRouter.getSplashScreenIntent(this)
            intent.putExtra("branch", url)
            intent.putExtra("branch_force_new_session", true)
            startActivity(intent)
        }
    }

    override fun isBranchIOLink(url: String): Boolean {
        val BRANCH_IO_HOST = "tokopedia.link"
        val uri = Uri.parse(url)
        return uri.host != null && uri.host == BRANCH_IO_HOST
    }

}
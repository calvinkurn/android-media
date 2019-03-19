package com.tokopedia.talk.inboxtalk.view.fragment

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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
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
import com.tokopedia.talk.common.domain.pojo.UnreadCount
import com.tokopedia.talk.common.view.TalkDialog
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.listener.GetUnreadNotificationListener
import com.tokopedia.talk.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.view.presenter.InboxTalkPresenter
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

open class InboxTalkFragment : BaseDaggerFragment(),
        InboxTalkContract.View, InboxTalkItemViewHolder.TalkItemListener, CommentTalkViewHolder
        .TalkCommentItemListener, TalkProductAttachmentAdapter.ProductAttachmentItemClickListener,
        LoadMoreCommentTalkViewHolder.LoadMoreListener {

    private val REQUEST_REPORT_TALK: Int = 101
    private val REQUEST_GO_TO_DETAIL: Int = 102

    private val POS_FILTER_ALL: Int = 0
    private val POS_FILTER_UNREAD: Int = 1
    private val FILTER_ALL: String = "all"
    private val FILTER_UNREAD: String = "unread"

    private lateinit var alertDialog: Dialog
    private lateinit var adapter: InboxTalkAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: InboxTalkViewModel
    private lateinit var filter: String
    private lateinit var bottomMenu: Menus
    private lateinit var filterMenuList: ArrayList<Menus.ItemMenus>

    @Inject
    lateinit var talkDialog: TalkDialog

    @Inject
    lateinit var presenter: InboxTalkPresenter

    @Inject
    lateinit var analytics: TalkAnalytics

    companion object {
        fun newInstance(nav: String): InboxTalkFragment {
            val fragment = InboxTalkFragment()
            val bundle = Bundle()
            bundle.putString(InboxTalkActivity.NAVIGATION, nav)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_INBOX_TALK
    }

    override fun initInjector() {
        val inboxTalkComponent = DaggerInboxTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        inboxTalkComponent.inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_inbox, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.sendScreen(this, screenName)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var nav: String = ""
        savedInstanceState?.run {
            nav = savedInstanceState.getString(InboxTalkActivity.NAVIGATION, "")
        } ?: arguments?.run {
            nav = getString(InboxTalkActivity.NAVIGATION, "")
        } ?: activity?.run {
            finish()
        }

        viewModel = InboxTalkViewModel(nav)

        setupView()
        initData()
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
                    presenter.getInboxTalk(filter, viewModel.screen)
                }
            }

        })
        swipeToRefresh.setOnRefreshListener { onRefreshData() }
        icon_filter.setButton1OnClickListener(showFilterDialog())
    }

    private fun initData() {

        filter = "all"
        presenter.getInboxTalk(filter, viewModel.screen)

        filterMenuList = ArrayList()
        filterMenuList.add(Menus.ItemMenus(getString(R.string.filter_all_talk), -1))
        filterMenuList[0].iconEnd = R.drawable.ic_check
        filterMenuList.add(Menus.ItemMenus(getString(R.string.filter_not_read), -1))

    }

    private fun onRefreshData() {
        presenter.refreshTalk(filter, viewModel.screen)
        swipeToRefresh.isRefreshing = true

    }

    private fun showFilterDialog(): View.OnClickListener {
        return View.OnClickListener { _ ->
            context?.run {
                if (!::bottomMenu.isInitialized) bottomMenu = Menus(this)
                bottomMenu.itemMenuList = filterMenuList
                bottomMenu.setActionText(getString(R.string.button_cancel))
                bottomMenu.setOnActionClickListener { bottomMenu.dismiss() }
                bottomMenu.setOnItemMenuClickListener { _, pos ->
                    onFilterClicked(pos, bottomMenu)
                }
                bottomMenu.show()
            }
        }
    }

    private fun onFilterClicked(pos: Int, filterMenu: Menus) {
        when (pos) {
            POS_FILTER_ALL -> filter = FILTER_ALL
            POS_FILTER_UNREAD -> filter = FILTER_UNREAD
        }

        for (itemMenu in filterMenuList) {
            itemMenu.iconEnd = 0
            itemMenu.icon = -1
        }
        filterMenuList[pos].iconEnd = R.drawable.ic_check
        presenter.getInboxTalkWithFilter(filter, viewModel.screen)
        analytics.trackClickFilter(filter)
        filterMenu.dismiss()
    }

    private fun goToReportTalk(talkId: String, shopId: String, productId: String, commentId:
    String) {
        activity?.run {
            val intent: Intent = if (commentId.isBlank()) {
                ReportTalkActivity.createIntentReportTalk(this, talkId, shopId, productId)
            } else {
                ReportTalkActivity.createIntentReportComment(this, talkId, commentId,
                        shopId, productId)
            }
            this@InboxTalkFragment.startActivityForResult(intent, REQUEST_REPORT_TALK)
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

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun hideLoading() {
        adapter.hideLoading()
    }

    override fun onSuccessGetInboxTalk(talkViewModel: InboxTalkViewModel) {
        adapter.hideEmpty()
        adapter.addList(talkViewModel.listTalk)

        setNotificationTab(talkViewModel.unreadNotification)
    }

    private fun setNotificationTab(unreadNotification: UnreadCount) {
        viewModel.unreadNotification = unreadNotification

        if (activity is GetUnreadNotificationListener) {
            val notifCount: Int = when (viewModel.screen) {
                InboxTalkActivity.INBOX_ALL -> unreadNotification.all
                InboxTalkActivity.FOLLOWING -> unreadNotification.following
                InboxTalkActivity.MY_PRODUCT -> unreadNotification.my_product
                else -> 0
            }
            (activity as GetUnreadNotificationListener).onGetNotification(notifCount, viewModel.screen)
        }
    }

    override fun onErrorGetInboxTalk(errorMessage: String) {
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
                presenter.getInboxTalk(filter, viewModel.screen)
            }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
                presenter.getInboxTalk(filter, viewModel.screen)
            }.showRetrySnackbar()
        }
    }

    override fun onErrorActionTalk(errorMessage: String) {
        NetworkErrorHelper.showRedSnackbar(view, errorMessage)
    }

    override fun hideRefreshLoad() {
        swipeToRefresh.isRefreshing = false
    }

    override fun onEmptyTalk() {
        adapter.clearAllElements()
        adapter.showEmpty()

        setNotificationTab(UnreadCount())

    }

    override fun onSuccessGetListFirstPage(talkViewModel: InboxTalkViewModel) {
        adapter.clearAllElements()
        adapter.setList(talkViewModel.listTalk)

        setNotificationTab(talkViewModel.unreadNotification)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REPORT_TALK && resultCode == Activity.RESULT_OK) {
            data?.extras?.run {
                val talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
                onSuccessReportTalk(talkId)

            }
        } else if (requestCode == REQUEST_GO_TO_DETAIL) {
            data?.run {
                when (resultCode) {
                    TalkDetailsActivity.RESULT_OK_READ -> updateReadStatusTalk(data)
                    TalkDetailsActivity.RESULT_OK_DELETE_TALK -> updateDeleteTalk(data)
                    TalkDetailsActivity.RESULT_OK_DELETE_COMMENT -> updateDeleteComment(data)
                    TalkDetailsActivity.RESULT_OK_REFRESH_TALK -> onRefreshData()
                    else -> {
                    }
                }
            }

        }
    }

    private fun updateDeleteTalk(data: Intent) {
        val talkId = data.getStringExtra(TalkDetailsActivity.THREAD_TALK_ID)

        if (!talkId.isEmpty()) {
            adapter.getItemById(talkId)?.run {
                val shouldDecreaseNotifCount = !this.talkThread.headThread.isRead
                if (shouldDecreaseNotifCount) {
                    adapter.updateReadStatus(talkId)

                    when (viewModel.screen) {
                        InboxTalkActivity.INBOX_ALL -> viewModel.unreadNotification.all -= 1
                        InboxTalkActivity.FOLLOWING -> viewModel.unreadNotification.following -= 1
                        InboxTalkActivity.MY_PRODUCT -> viewModel.unreadNotification.my_product -= 1
                        else -> {
                        }
                    }

                    setNotificationTab(viewModel.unreadNotification)
                }

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

            if (adapter.getCommentById(talkId, commentId) != null
                    && adapter.getItemById(talkId) != null) {

                val shouldDecreaseNotifCount = !(adapter.getItemById(talkId) as InboxTalkItemViewModel)
                        .talkThread.headThread.isRead
                if (shouldDecreaseNotifCount) {
                    adapter.updateReadStatus(talkId)

                    when (viewModel.screen) {
                        InboxTalkActivity.INBOX_ALL -> viewModel.unreadNotification.all -= 1
                        InboxTalkActivity.FOLLOWING -> viewModel.unreadNotification.following -= 1
                        InboxTalkActivity.MY_PRODUCT -> viewModel.unreadNotification.my_product -= 1
                        else -> {
                        }
                    }

                    setNotificationTab(viewModel.unreadNotification)
                }
                adapter.deleteComment(talkId, commentId)
            } else if (adapter.getItemById(talkId) != null
                    && !(adapter.getItemById(talkId) as InboxTalkItemViewModel).talkThread
                            .listChild.isEmpty()
                    && (adapter.getItemById(talkId) as InboxTalkItemViewModel).talkThread.listChild[0] is LoadMoreCommentTalkViewModel) {
                ((adapter.getItemById(talkId) as
                        InboxTalkItemViewModel).talkThread.listChild[0] as
                        LoadMoreCommentTalkViewModel).counter -= 1

                adapter.updateReadStatus(talkId)
            }

        } else {
            onRefreshData()
        }

    }

    private fun updateReadStatusTalk(data: Intent) {
        val talkId = data.getStringExtra(TalkDetailsActivity.THREAD_TALK_ID)
        if (!talkId.isEmpty()) {

            adapter.getItemById(talkId)?.run {
                val shouldDecreaseNotifCount = !this.talkThread.headThread.isRead
                if (shouldDecreaseNotifCount) {
                    adapter.updateReadStatus(talkId)

                    when (viewModel.screen) {
                        InboxTalkActivity.INBOX_ALL -> viewModel.unreadNotification.all -= 1
                        InboxTalkActivity.FOLLOWING -> viewModel.unreadNotification.following -= 1
                        InboxTalkActivity.MY_PRODUCT -> viewModel.unreadNotification.my_product -= 1
                        else -> {
                        }
                    }

                    setNotificationTab(viewModel.unreadNotification)

                }
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

    override fun onItemTalkClick(allowReply: Boolean, talkId: String, shopId: String) {
        goToDetailTalk(talkId, shopId, allowReply)
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        analytics.trackClickReplyButton(talkId)
        goToDetailTalk(talkId, shopId, allowReply)
    }

    override fun onMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, productId: String) {
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
    }

    private fun onMenuItemClicked(itemMenu: Menus.ItemMenus, bottomMenu: Menus, shopId: String, talkId: String, productId: String) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        when (itemMenu.title) {
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

    override fun onCommentMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, commentId: String, productId: String) {

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

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
        analytics.trackClickProductFromAttachment()
        onGoToPdp(attachProduct.productId.toString())
    }


    private fun showErrorReplyTalk() {
        NetworkErrorHelper.showRedSnackbar(view, getString(R.string
                .error_default_cannot_reply_talk))
    }

    override fun hideFilter() {
        icon_filter.visibility = View.GONE
    }

    override fun showFilter() {
        icon_filter.visibility = View.VISIBLE
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

    override fun onGoToPdpFromProductItemHeader(productId: String) {
        analytics.trackClickProduct()
        onGoToPdp(productId)
    }

    private fun onGoToPdp(productId: String) {
        activity?.applicationContext?.run {
            val intent: Intent? = getProductIntent(productId)
            this@InboxTalkFragment.startActivity(intent)
        }
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(context!!,
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId))
        } else {
            null
        }
    }

    override fun onGoToUserProfile(userId: String) {
        analytics.trackClickUserProfile()
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getTopProfileIntent(this, userId)
            this@InboxTalkFragment.startActivity(intent)
        }
    }

    override fun onGoToShopPage(shopId: String) {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getShopPageIntent(this, shopId)
            this@InboxTalkFragment.startActivity(intent)
        }
    }

    override fun onNoShowTalkItemClick(talkId: String) {
        presenter.markTalkNotFraud(talkId)
    }

    override fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String) {
        goToReportTalk(talkId, shopId, productId, "")
    }

    override fun onSuccessMarkTalkNotFraud(talkId: String) {
        adapter.showReportedTalk(talkId)
    }

    override fun onNoShowTalkCommentClick(talkId: String, commentId: String) {
        presenter.markCommentNotFraud(talkId, commentId)

    }

    override fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String, commentId: String) {
        goToReportTalk(talkId, shopId, productId, commentId)
    }

    override fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String) {
        adapter.showReportedCommentTalk(talkId, commentId)
    }

    private fun goToDetailTalk(talkId: String, shopId: String, allowReply: Boolean) {
        if (allowReply) {
            context?.run {
                this@InboxTalkFragment.startActivityForResult(
                        TalkDetailsActivity.getCallingIntent(talkId, shopId, this,
                                TalkDetailsActivity.SOURCE_INBOX)
                        , REQUEST_GO_TO_DETAIL)
            }
        } else {
            showErrorReplyTalk()
        }
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

    override fun onLoadMoreCommentClicked(talkId: String, shopId: String, allowReply: Boolean) {
        goToDetailTalk(talkId, shopId, allowReply)
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}

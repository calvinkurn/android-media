package com.tokopedia.talk.inboxtalk.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.domain.UnreadCount
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.listener.GetUnreadNotificationListener
import com.tokopedia.talk.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.view.presenter.InboxTalkPresenter
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkFragment(val nav: String = InboxTalkActivity.FOLLOWING) : BaseDaggerFragment(),
        InboxTalkContract.View, InboxTalkItemViewHolder.TalkItemListener, CommentTalkViewHolder
        .TalkCommentItemListener, TalkProductAttachmentAdapter.ProductAttachmentItemClickListener {


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
    lateinit var presenter: InboxTalkPresenter

    companion object {
        fun newInstance(nav: String) = InboxTalkFragment(nav)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initData()

    }


    private fun setupView() {
        val adapterTypeFactory = InboxTalkTypeFactoryImpl(this, this, this)
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
                    presenter.getInboxTalk(filter, nav)
                }
            }

        })
        swipeToRefresh.setOnRefreshListener { onRefreshData() }
        icon_filter.setButton1OnClickListener(showFilterDialog())
    }

    private fun initData() {
        activity?.intent?.extras?.run {
            viewModel = InboxTalkViewModel(nav)
        }

        filter = "all"
        presenter.getInboxTalk(filter, nav)

        filterMenuList = ArrayList()
        filterMenuList.add(Menus.ItemMenus(getString(R.string.filter_all_talk)))
        filterMenuList[0].iconEnd = R.drawable.ic_check
        filterMenuList.add(Menus.ItemMenus(getString(R.string.filter_not_read)))

    }

    private fun onRefreshData() {
        presenter.refreshTalk(filter, nav)
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
        }
        filterMenuList[pos].iconEnd = R.drawable.ic_check
        presenter.getInboxTalkWithFilter(filter, nav)
        filterMenu.dismiss()
    }

    private fun goToReportTalk(talkId: String, shopId: String, productId: String, commentId:
    String) {
        activity?.run {
            val intent: Intent = if (commentId.isBlank()) {
                ReportTalkActivity.createIntentReportTalk(this, talkId, shopId, productId)
            } else {
                ReportTalkActivity.createIntentReportComment(this, talkId, shopId,
                        productId, commentId)
            }
            this@InboxTalkFragment.startActivityForResult(intent, REQUEST_REPORT_TALK)
        }
    }

    private fun showUnfollowTalkDialog(talkId: String) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.unfollow_talk_dialog_title))
        alertDialog.setDesc(getString(R.string.unfollow_talk_dialog_desc))
        alertDialog.setBtnCancel(getString(R.string.button_cancel))
        alertDialog.setBtnOk(getString(R.string.button_unfollow_talk))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener {
            presenter.unfollowTalk(talkId)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


    private fun showFollowTalkDialog(talkId: String) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.follow_talk_dialog_title))
        alertDialog.setDesc(getString(R.string.follow_talk_dialog_desc))
        alertDialog.setBtnCancel(getString(R.string.button_cancel))
        alertDialog.setBtnOk(getString(R.string.button_follow_talk))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener {
            presenter.followTalk(talkId)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showDeleteTalkDialog(shopId: String, talkId: String) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.delete_talk_dialog_title))
        alertDialog.setDesc(getString(R.string.delete_talk_dialog_desc))
        alertDialog.setBtnCancel(getString(R.string.button_cancel))
        alertDialog.setBtnOk(getString(R.string.button_delete))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener {
            presenter.deleteTalk(shopId, talkId)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showDeleteCommentTalkDialog(shopId: String, talkId: String, commentId: String) {

        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.delete_comment_talk_dialog_title))
        alertDialog.setDesc(getString(R.string.delete_comment_talk_dialog_desc))
        alertDialog.setBtnCancel(getString(R.string.button_cancel))
        alertDialog.setBtnOk(getString(R.string.button_delete))
        alertDialog.setOnCancelClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setOnOkClickListener {
            presenter.deleteCommentTalk(shopId, talkId, commentId)
            alertDialog.dismiss()
        }

        alertDialog.show()
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
        if (activity is GetUnreadNotificationListener) {
            val notifCount: Int = when (nav) {
                InboxTalkActivity.INBOX_ALL -> unreadNotification.all
                InboxTalkActivity.FOLLOWING -> unreadNotification.following
                InboxTalkActivity.MY_PRODUCT -> unreadNotification.my_product
                else -> 0
            }
            (activity as GetUnreadNotificationListener).onGetNotification(notifCount, nav)
        }
    }

    override fun onErrorGetInboxTalk(errorMessage: String) {
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
                presenter.getInboxTalk(filter, nav)
            }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
                presenter.getInboxTalk(filter, nav)
            }.showRetrySnackbar()
        }
    }

    override fun hideRefreshLoad() {
        swipeToRefresh.isRefreshing = false
    }

    override fun onEmptyTalk() {
        adapter.clearAllElements()
        adapter.showEmpty()
        setNotificationTab(UnreadCount())

    }

    override fun onSuccessGetListFirstPage(listTalk: ArrayList<Visitable<*>>) {
        adapter.clearAllElements()
        adapter.setList(listTalk)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REPORT_TALK && resultCode == Activity.RESULT_OK) {
            data?.extras?.run {
                val talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
                onSuccessReportTalk(talkId)

            }
        } else if (requestCode == REQUEST_GO_TO_DETAIL && resultCode == Activity.RESULT_OK) {
            //TODO UPDATE NOTIFICATION READ
            data?.run {
                //                val talkThread : ProductTalkItemViewModel = data.getParcelableExtra<ProductTalkItemViewModel>()
            }

        }
    }

    private fun onSuccessReportTalk(talkId: String) {
        activity?.run {
            NetworkErrorHelper.showGreenSnackbar(this, getString(R.string.success_report_talk))
        }

        if (!talkId.isBlank()) {
            adapter.updateReportTalk(talkId)
        } else {
            onRefreshData()
        }
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        if (allowReply) goToDetailTalk(talkId, shopId)
        else showErrorReplyTalk()
    }

    override fun onMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, productId: String) {
        context?.run {
            val listMenu = ArrayList<Menus.ItemMenus>()
            if (menu.allowDelete) listMenu.add(Menus.ItemMenus(getString(R.string
                    .menu_delete_talk)))
            if (menu.allowUnfollow) listMenu.add(Menus.ItemMenus(getString(R.string
                    .menu_unfollow_talk)))
            if (menu.allowFollow) listMenu.add(Menus.ItemMenus(getString(R.string
                    .menu_follow_talk)))
            if (menu.allowReport) listMenu.add(Menus.ItemMenus(getString(R.string
                    .menu_report_talk)))

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
        when (itemMenu.title) {
            getString(R.string.menu_delete_talk) -> showDeleteTalkDialog(shopId, talkId)
            getString(R.string.menu_follow_talk) -> showFollowTalkDialog(talkId)
            getString(R.string.menu_unfollow_talk) -> showUnfollowTalkDialog(talkId)
            getString(R.string.menu_report_talk) -> goToReportTalk(talkId, shopId, productId, "")
        }
        bottomMenu.dismiss()
    }

    override fun onCommentMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, commentId: String, productId: String) {
        context?.run {
            val listMenu = ArrayList<Menus.ItemMenus>()
            if (menu.allowReport) {
                listMenu.add(Menus.ItemMenus(getString(R.string
                        .menu_report_comment)))
            }
            if (menu.allowDelete) {
                listMenu.add(Menus.ItemMenus(getString(R.string
                        .menu_delete_comment)))
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

    private fun onCommentMenuItemClicked(itemMenu: Menus.ItemMenus, bottomMenu: Menus, shopId: String, talkId: String, commentId: String, productId: String) {
        when (itemMenu.title) {
            getString(R.string.menu_report_comment) -> goToReportTalk(talkId, shopId, productId,
                    commentId)
            getString(R.string.menu_delete_comment) -> showDeleteCommentTalkDialog(shopId,
                    talkId, commentId)
        }
        bottomMenu.dismiss()
    }

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
        //TODO NISIE GO TO PDP
    }


    private fun showErrorReplyTalk() {
        //TODO NISIE GET ERROR MESSAGE FOR REPLY TALK
        NetworkErrorHelper.showRedSnackbar(view, "Error dud")
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

    private fun goToDetailTalk(talkId: String, shopId: String) {
        context?.run {
            this@InboxTalkFragment.startActivityForResult(
                    TalkDetailsActivity.getCallingIntent(talkId, shopId, this)
                    , REQUEST_GO_TO_DETAIL)
        }
    }

    override fun onSuccessDeleteTalk(talkId: String) {
        adapter.deleteTalkByTalkId(talkId)
    }

    override fun onSuccessDeleteCommentTalk(talkId: String, commentId: String) {
        adapter.deleteComment(talkId, commentId)
    }

    override fun onSuccessUnfollowTalk(talkId: String) {
        adapter.setStatusFollow(talkId, false)
    }

    override fun onSuccessFollowTalk(talkId: String) {
        adapter.setStatusFollow(talkId, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}

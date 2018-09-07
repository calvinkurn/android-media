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
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.view.presenter.InboxTalkPresenter
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkFragment(val nav: String = InboxTalkActivity.FOLLOWING) : BaseDaggerFragment(),
        InboxTalkContract.View, InboxTalkItemViewHolder.TalkItemListener, CommentTalkViewHolder
        .TalkCommentItemListener, TalkProductAttachmentAdapter.ProductAttachmentItemClickListener {

    val REQUEST_REPORT_TALK: Int = 101
    val POS_FILTER_ALL: Int = 0
    val POS_FILTER_UNREAD: Int = 1
    val FILTER_ALL: String = "all"
    val FILTER_UNREAD: String = "unread"

    private lateinit var alertDialog: Dialog
    private lateinit var adapter: InboxTalkAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: InboxTalkViewModel
    private lateinit var filter: String
    private lateinit var bottomMenu: Menus

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

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
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
    }

    private fun onRefreshData() {
        presenter.refreshTalk(filter, nav)
        swipeToRefresh.isRefreshing = true

    }

    private fun showFilterDialog(): View.OnClickListener {
        return View.OnClickListener {
            context?.run {
                val menuItem = arrayOf(resources.getString(R.string.filter_all_talk),
                        resources.getString(R.string.filter_not_read))
                if (!::bottomMenu.isInitialized) bottomMenu = Menus(this)
                bottomMenu.setItemMenuList(menuItem)
                bottomMenu.setActionText(getString(R.string.button_cancel))
                bottomMenu.setOnActionClickListener { bottomMenu.dismiss() }
                bottomMenu.setOnItemMenuClickListener { itemMenus, pos ->
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


        //TODO NISIE: SET MENU ICON TO CHECKED
        presenter.getInboxTalkWithFilter(filter, nav)
        filterMenu.dismiss()
    }

    private fun goToReportTalk() {
        activity?.run {
            val intent = ReportTalkActivity.createIntent(this)
            startActivityForResult(intent, REQUEST_REPORT_TALK)
        }
    }

    private fun showUnfollowTalkDialog() {
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
        alertDialog.setOnOkClickListener({
            //asd
            alertDialog.dismiss()
        })

        alertDialog.show()
    }


    private fun showFollowTalkDialog() {
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
        alertDialog.setOnOkClickListener({
            //asd
            alertDialog.dismiss()
        })

        alertDialog.show()
    }

    private fun showDeleteTalkDialog() {
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
        alertDialog.setOnOkClickListener({
            //asd
            alertDialog.dismiss()
        })

        alertDialog.show()
    }

    private fun showDeleteCommentTalkDialog() {

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
        alertDialog.setOnOkClickListener({
            //asd
            alertDialog.dismiss()
        })

        alertDialog.show()
    }

    override fun showLoading() {
        adapter.showLoading()
    }

    override fun hideLoading() {
        adapter.hideLoading()
    }

    override fun onSuccessGetInboxTalk(list: ArrayList<Visitable<*>>) {
        adapter.hideEmpty()
        adapter.addList(list)

        if(activity is )
    }

    override fun onErrorGetInboxTalk(errorMessage: String) {
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
                presenter.getInboxTalk(filter, nav)
            }
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage, NetworkErrorHelper.RetryClickedListener {
                presenter.getInboxTalk(filter, nav)
            })
        }
    }

    override fun hideRefreshLoad() {
        swipeToRefresh.isRefreshing = false
    }

    override fun onEmptyTalk() {
        adapter.clearAllElements()
        adapter.showEmpty()
    }

    override fun onSuccessGetListFirstPage(listTalk: ArrayList<Visitable<*>>) {
        adapter.clearAllElements()
        adapter.setList(listTalk)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REPORT_TALK && resultCode == Activity.RESULT_OK) {
            onSuccessReportTalk()
        }
    }

    private fun onSuccessReportTalk() {
        activity?.run {
            NetworkErrorHelper.showGreenSnackbar(this, getString(R.string.success_report_talk))
        }
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean) {
        if (allowReply) goToDetailTalk()
        else showErrorReplyTalk()
    }

    override fun onMenuButtonClicked(menu: TalkState) {
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
            bottomMenu.setOnItemMenuClickListener { itemMenus, pos ->
                onMenuItemClicked(itemMenus, bottomMenu)
            }
            bottomMenu.show()
        }
    }

    private fun onMenuItemClicked(itemMenu: Menus.ItemMenus, bottomMenu: Menus) {
        when (itemMenu.title) {
            getString(R.string.menu_delete_talk) -> showDeleteTalkDialog()
            getString(R.string.menu_follow_talk) -> showFollowTalkDialog()
            getString(R.string.menu_unfollow_talk) -> showUnfollowTalkDialog()
            getString(R.string.menu_report_talk) -> goToReportTalk()
        }
        bottomMenu.dismiss()
    }

    override fun onCommentMenuButtonClicked(menu: TalkState) {
        context?.run {
            val listMenu = ArrayList<Menus.ItemMenus>()
            if (menu.allowReport) listMenu.add(Menus.ItemMenus(getString(R.string
                    .menu_report_comment)))
            if (menu.allowDelete) listMenu.add(Menus.ItemMenus(getString(R.string
                    .menu_delete_comment)))

            if (!::bottomMenu.isInitialized) bottomMenu = Menus(this)
            bottomMenu.itemMenuList = listMenu
            bottomMenu.setActionText(getString(R.string.button_cancel))
            bottomMenu.setOnActionClickListener { bottomMenu.dismiss() }
            bottomMenu.setOnItemMenuClickListener { itemMenus, pos ->
                onCommentMenuItemClicked(itemMenus, bottomMenu)
            }
            bottomMenu.show()
        }
    }

    private fun onCommentMenuItemClicked(itemMenu: Menus.ItemMenus, bottomMenu: Menus) {
        when (itemMenu.title) {
            getString(R.string.menu_report_comment) -> goToReportTalk()
            getString(R.string.menu_delete_comment) -> showDeleteCommentTalkDialog()
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

    override fun showLoadingFilter() {
        progressBar.visibility = View.VISIBLE
        talk_rv.visibility = View.GONE
        swipeToRefresh.isEnabled = false
    }

    override fun hideLoadingFilter() {
        progressBar.visibility = View.GONE
        talk_rv.visibility = View.VISIBLE
        swipeToRefresh.isEnabled = true
    }

    override fun onNoShowTalkItemClick(adapterPosition: Int) {
        adapter.showReportedTalk(adapterPosition)
    }

    override fun onYesReportTalkItemClick() {
        goToReportTalk()
    }

    private fun goToDetailTalk() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}

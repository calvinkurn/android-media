package com.tokopedia.talk.inboxtalk.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.R
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
        InboxTalkContract.View, InboxTalkItemViewHolder.TalkItemListener {


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
        val adapterTypeFactory = InboxTalkTypeFactoryImpl(this)
        val listTalk = ArrayList<Visitable<*>>()
        adapter = InboxTalkAdapter(adapterTypeFactory, listTalk)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        talk_rv.layoutManager = linearLayoutManager
        talk_rv.adapter = adapter
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
        presenter.getInboxTalk(filter, nav)
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


    private fun showFollowTalkDialog() {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.follow_talk_dialog_title))
        alertDialog.setDesc(getString(R.string.follow_talk_dialog_desc))
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

    override fun showLoadingFull() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoadingFull() {
        progressBar.visibility = View.GONE
    }

    override fun onSuccessGetInboxTalk(list: ArrayList<Visitable<*>>) {
        adapter.addList(list)
    }

    override fun onErrorGetInboxTalk(errorMessage: String) {
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            presenter.getInboxTalk(filter, nav)
        }
    }

    override fun hideRefreshLoad() {
        swipeToRefresh.isRefreshing = false
    }

    override fun onSuccessRefreshInboxTalk(listTalk: ArrayList<Visitable<*>>) {
        adapter.clearAllElements()
        adapter.addList(listTalk)
    }

    override fun onEmptyTalk() {
        adapter.showEmpty()
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

    private fun showErrorReplyTalk() {
        //TODO NISIE GET ERROR MESSAGE FOR REPLY TALK
        NetworkErrorHelper.showRedSnackbar(view, "Error dud")
    }

    private fun goToDetailTalk() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
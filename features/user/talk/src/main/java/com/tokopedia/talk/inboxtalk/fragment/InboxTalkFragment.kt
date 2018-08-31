package com.tokopedia.talk.inboxtalk.fragment

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
import com.tokopedia.talk.inboxtalk.activity.InboxTalkActivity
import com.tokopedia.talk.inboxtalk.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.adapter.InboxTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.talk.inboxtalk.listener.InboxTalkContract
import com.tokopedia.talk.inboxtalk.presenter.InboxTalkPresenter
import com.tokopedia.talk.inboxtalk.viewmodel.InboxTalkViewModel
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import kotlinx.android.synthetic.main.fragment_inbox_talk.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkFragment : BaseDaggerFragment(), InboxTalkContract.View {

    val REQUEST_REPORT_TALK: Int = 101

    private lateinit var alertDialog: Dialog
    private lateinit var adapter: InboxTalkAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: InboxTalkViewModel

    @Inject
    lateinit var presenter: InboxTalkPresenter


    companion object {
        fun newInstance() = InboxTalkFragment()
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
        return inflater.inflate(R.layout.fragment_inbox_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        initData()

    }


    private fun setupView() {
        val adapterTypeFactory = InboxTalkTypeFactoryImpl()
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
            viewModel = InboxTalkViewModel(InboxTalkActivity.Companion.INBOX_ALL)
        }

        presenter.getInboxTalk()
    }

    private fun onRefreshData() {
        presenter.refreshTalk()
        swipeToRefresh.isRefreshing = true

    }

    private fun showFilterDialog(): View.OnClickListener {
        return View.OnClickListener {
            context?.run {
                val menuItem = arrayOf(resources.getString(R.string.filter_all_talk),
                        resources.getString(R.string.filter_not_read))
                val filterMenu = Menus(this)
                filterMenu.setItemMenuList(menuItem)
                filterMenu.setActionText(getString(R.string.button_cancel))
                filterMenu.setOnActionClickListener { filterMenu.dismiss() }
                filterMenu.setOnItemMenuClickListener { itemMenus, pos ->
                    onFilterClicked(pos,
                            filterMenu)
                }
                filterMenu.show()
            }
        }
    }

    private fun onFilterClicked(pos: Int, filterMenu: Menus) {
        when {
            pos == 0 -> goToReportTalk()
            pos == 1 -> showDeleteTalkDialog()
            pos == 2 -> showDeleteCommentTalkDialog()
            pos == 2 -> showUnfollowTalkDialog()
        }

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

    override fun onSuccessGetInboxTalk(list: ArrayList<Visitable<*>>) {
        adapter.addList(list)
        progressBar.visibility = View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
package com.tokopedia.talk.producttalk.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.ProductTalkTypeFactoryImpl
import com.tokopedia.talk.R
import com.tokopedia.talk.addtalk.view.activity.AddTalkActivity
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.view.TalkDialog
import com.tokopedia.talk.producttalk.di.DaggerProductTalkComponent
import com.tokopedia.talk.producttalk.presenter.ProductTalkPresenter
import com.tokopedia.talk.producttalk.view.adapter.EmptyProductTalkViewHolder
import com.tokopedia.talk.producttalk.view.adapter.LoadProductTalkThreadViewHolder
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkAdapter
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkThreadViewHolder
import com.tokopedia.talk.producttalk.view.listener.ProductTalkContract
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkTitleViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import kotlinx.android.synthetic.main.product_talk.*
import javax.inject.Inject

/**
 * @author by Steven
 */

class ProductTalkFragment : BaseDaggerFragment(),
        ProductTalkContract.View,
        ProductTalkThreadViewHolder.TalkItemListener,
        LoadProductTalkThreadViewHolder.LoadTalkListener,
        CommentTalkViewHolder.TalkCommentItemListener,
        TalkProductAttachmentAdapter.ProductAttachmentItemClickListener,
        EmptyProductTalkViewHolder.TalkItemListener,
        LoadMoreCommentTalkViewHolder.LoadMoreListener {

    override fun getContext(): Context? {
        return activity
    }


    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_PRODUCT_TALK
    }

    @Inject
    lateinit var presenter: ProductTalkPresenter

    @Inject
    lateinit var talkDialog: TalkDialog

    lateinit var adapter: ProductTalkAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bottomMenu: Menus
    private lateinit var alertDialog: Dialog
    private lateinit var swiper: SwipeToRefresh

    val REQUEST_REPORT_TALK: Int = 18
    val REQUEST_CREATE_TALK: Int = 2132
    val REQUEST_GO_TO_DETAIL: Int = 102
    val REQUEST_GO_TO_LOGIN: Int = 200


    var productId: String = ""
    var productName: String = ""
    var productPrice: String = ""
    var productImage: String = ""
    var intentChat: Intent? = null

    override fun initInjector() {
        val productTalkComponent = DaggerProductTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        productTalkComponent.inject(this)
        presenter.attachView(this)
    }

    companion object {

        fun newInstance(extras: Bundle): ProductTalkFragment {
            val fragment = ProductTalkFragment()
            fragment.productId = extras.getString("product_id")
            fragment.productPrice = extras.getString("product_price")
            fragment.productName = extras.getString("prod_name")
            fragment.productImage = MethodChecker.fromHtml(extras.getString("product_image")).toString()
            fragment.intentChat = extras.getParcelable("intent_chat")
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.product_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        presenter.initProductTalk(productId)
    }

    override fun onResume() {
        super.onResume()
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.product_talk, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_add).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_add -> {
                goToCreateTalk(productId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCreateTalk(productId: String) {
        activity?.run {
            val intent = AddTalkActivity.createIntent(this, productId)
            startActivityForResult(intent, REQUEST_CREATE_TALK)
        }
    }

    private fun getProductTalk() {
        presenter.getProductTalk(productId)
    }

    private fun setUpView(view: View) {
        val adapterTypeFactory = ProductTalkTypeFactoryImpl(this, this, this, this, this, this)
        val listProductTalk = ArrayList<Visitable<*>>()
        adapter = ProductTalkAdapter(adapterTypeFactory, listProductTalk)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        list_thread.layoutManager = linearLayoutManager
        list_thread.adapter = adapter

        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }
        swiper = view.findViewById(R.id.swipeToRefresh)
        swiper.setOnRefreshListener { onRefreshData() }
    }


    private fun onRefreshData() {
        presenter.resetProductTalk(productId)
    }

    override fun showRefresh() {
        swiper.isRefreshing = true
    }

    override fun hideRefresh() {
        swiper.isRefreshing = false
    }

    override fun showLoadingFull() {
        talkProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoadingFull() {
        talkProgressBar.visibility = View.GONE
    }

    override fun onEmptyTalk() {
        setHasOptionsMenu(false)
        adapter.showEmpty()
    }

    override fun setCanLoad() {
        adapter.setLoadModel()
    }

    override fun onSuccessResetTalk(listThread: ArrayList<Visitable<*>>) {
        adapter.setList(listThread, ProductTalkTitleViewModel(productImage, productName, productPrice))
    }

    override fun onSuccessGetTalks(listThread: ArrayList<Visitable<*>>) {
        adapter.hideLoading()
        adapter.addList(listThread)
    }

    override fun onLoadClicked() {
        adapter.dismissLoadModel()
        presenter.getProductTalk(productId)
    }

    override fun onErrorGetTalks(errorMessage: String?) {
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            presenter.getProductTalk(productId)
        }
    }

    override fun onSuccessDeleteCommentTalk(talkId: String, commentId: String) {
        adapter.deleteComment(talkId, commentId)
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        if (!presenter.isLoggedIn()) {
            goToLogin()
        }else {
            goToDetailTalk(talkId, shopId, allowReply)
        }
    }

    private fun showErrorReplyTalk() {
        NetworkErrorHelper.showRedSnackbar(view, getString(R.string.error_default_cannot_reply_talk))
    }

    private fun goToDetailTalk(talkId: String, shopId: String, allowReply: Boolean) {
        if (allowReply) {
            context?.run {
                this@ProductTalkFragment.startActivityForResult(
                        TalkDetailsActivity.getCallingIntent(talkId, shopId, this)
                        , REQUEST_GO_TO_DETAIL)
            }
        } else {
            showErrorReplyTalk()
        }
    }


    private fun goToLogin() {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getLoginIntent(this)
            activity!!.startActivityForResult(intent, REQUEST_GO_TO_LOGIN)
        }
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
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        when (itemMenu.title) {
            getString(R.string.menu_delete_talk) -> showDeleteTalkDialog(alertDialog, shopId,
                    talkId)
            getString(R.string.menu_follow_talk) -> showFollowTalkDialog(alertDialog, talkId)
            getString(R.string.menu_unfollow_talk) -> showUnfollowTalkDialog(alertDialog, talkId)
            getString(R.string.menu_report_talk) -> goToReportTalk(talkId, shopId, productId, "")
        }
        bottomMenu.dismiss()
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
            this@ProductTalkFragment.startActivityForResult(intent, REQUEST_REPORT_TALK)
        }
    }

    private fun showUnfollowTalkDialog(alertDialog: Dialog, talkId: String) {
        context?.run {
            talkDialog.createUnfollowTalkDialog(
                    this,
                    this@ProductTalkFragment.alertDialog,
                    View.OnClickListener {
                        this@ProductTalkFragment.alertDialog.dismiss()
                        presenter.unfollowTalk(talkId)
                    }
            ).show()
        }
    }


    private fun showFollowTalkDialog(alertDialog: Dialog, talkId: String) {
        context?.run {
            talkDialog.createFollowTalkDialog(
                    this,
                    this@ProductTalkFragment.alertDialog,
                    View.OnClickListener {
                        this@ProductTalkFragment.alertDialog.dismiss()
                        presenter.followTalk(talkId)
                    }
            ).show()
        }
    }

    private fun showDeleteTalkDialog(alertDialog: Dialog, shopId: String, talkId: String) {

        context?.run {
            talkDialog.createDeleteTalkDialog(
                    this,
                    this@ProductTalkFragment.alertDialog,
                    View.OnClickListener {
                        this@ProductTalkFragment.alertDialog.dismiss()
                        presenter.deleteTalk(shopId, talkId)
                    }
            ).show()
        }
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

    private fun showDeleteCommentTalkDialog(shopId: String, talkId: String, commentId: String) {
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

    override fun showLoadingAction() {
        talkProgressBar.visibility = View.VISIBLE
        list_thread.visibility = View.GONE
        swipeToRefresh.isEnabled = false
    }

    override fun hideLoadingAction() {
        talkProgressBar.visibility = View.GONE
        list_thread.visibility = View.VISIBLE
        swipeToRefresh.isEnabled = true
    }

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
        activity?.applicationContext?.run {
            (this as TalkRouter).goToProductDetailById(this, attachProduct.productId.toString())
        }
    }

    override fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String, commentId: String) {
        goToReportTalk(talkId, shopId, productId, commentId)
    }

    override fun onNoShowTalkItemClick(talkId: String) {
        presenter.markTalkNotFraud(talkId)
    }

    override fun onNoShowTalkCommentClick(talkId: String, commentId: String) {
        presenter.markCommentNotFraud(talkId, commentId)
    }

    override fun onSuccessMarkTalkNotFraud(talkId: String) {
        adapter.showReportedTalk(talkId)
    }

    override fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String) {
        adapter.showReportedCommentTalk(talkId, commentId)
    }

    override fun onSuccessDeleteTalk(talkId: String) {
        adapter.deleteTalkByTalkId(talkId)
    }

    override fun onSuccessUnfollowTalk(talkId: String) {
        adapter.setStatusFollow(talkId, false)
    }

    override fun onSuccessFollowTalk(talkId: String) {
        adapter.setStatusFollow(talkId, true)
    }

    override fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String) {
        goToReportTalk(talkId, shopId, productId, "")
    }

    override fun onLoadMoreCommentClicked(talkId: String, shopId: String, allowReply: Boolean) {
        goToDetailTalk(talkId, shopId, allowReply)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onAskButtonClick() {
        goToCreateTalk(productId)
    }

    override fun onChatClicked() {
        startActivity(intentChat)
    }

    override fun onGoToUserProfile(userId: String) {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getTopProfileIntent(this, userId)
            this@ProductTalkFragment.startActivity(intent)
        }
    }

    override fun onGoToShopPage(shopId: String) {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getShopPageIntent(this, shopId)
            this@ProductTalkFragment.startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_REPORT_TALK && resultCode == Activity.RESULT_OK) {
            data?.extras?.run {
                val talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
                onSuccessReportTalk(talkId)

            }
        } else if (requestCode == REQUEST_CREATE_TALK) {
            if(resultCode == Activity.RESULT_OK){
                onRefreshData()
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


}

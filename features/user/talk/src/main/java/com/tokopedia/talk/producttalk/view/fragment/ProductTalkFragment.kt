package com.tokopedia.talk.producttalk.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.component.ToasterNormal
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
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.producttalk.di.DaggerProductTalkComponent
import com.tokopedia.talk.producttalk.presenter.ProductTalkPresenter
import com.tokopedia.talk.producttalk.view.activity.TalkProductActivity
import com.tokopedia.talk.producttalk.view.adapter.EmptyProductTalkViewHolder
import com.tokopedia.talk.producttalk.view.adapter.LoadProductTalkThreadViewHolder
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkAdapter
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkThreadViewHolder
import com.tokopedia.talk.producttalk.view.listener.ProductTalkContract
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkTitleViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import kotlinx.android.synthetic.main.product_talk.*
import java.util.*
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

    private lateinit var performanceMonitoring: PerformanceMonitoring

    override fun getContext(): Context? {
        return activity
    }


    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_PRODUCT_TALK
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.sendScreen(this, screenName)
        }
    }

    @Inject
    lateinit var presenter: ProductTalkPresenter

    @Inject
    lateinit var talkDialog: TalkDialog

    @Inject
    lateinit var analytics: TalkAnalytics

    lateinit var adapter: ProductTalkAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bottomMenu: Menus
    private lateinit var alertDialog: Dialog
    private lateinit var swiper: SwipeToRefresh

    val REQUEST_REPORT_TALK: Int = 18
    val REQUEST_CREATE_TALK: Int = 2132
    val REQUEST_GO_TO_DETAIL: Int = 102
    val REQUEST_GO_TO_LOGIN: Int = 200


    var shopId: String = ""
    var productId: String = ""
    var productName: String = ""
    var productPrice: String = ""
    var productImage: String = ""
    var productUrl: String = ""
    var shopName: String = ""
    var shopAvatar: String = ""

    private var isTraceStopped: Boolean = false

    override fun initInjector() {
        val productTalkComponent = DaggerProductTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        productTalkComponent.inject(this)
        presenter.attachView(this)
    }

    companion object {
        const val TALK_PRODUCT_TRACE = "talk_list_product"

        fun newInstance(extras: Bundle): ProductTalkFragment {
            val fragment = ProductTalkFragment()
            fragment.arguments = extras
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(TALK_PRODUCT_TRACE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.product_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.run {
            shopId = getString(TalkProductActivity.SHOP_ID, "")
            productId = getString(TalkProductActivity.PRODUCT_ID, "")
            productPrice = getString(TalkProductActivity.PRODUCT_PRICE, "")
            productName = getString(TalkProductActivity.PRODUCT_NAME, "")
            productImage = MethodChecker.fromHtml(getString(TalkProductActivity
                    .PRODUCT_IMAGE, "")).toString()
            productUrl = getString(TalkProductActivity.PRODUCT_URL, "")
            shopName = getString(TalkProductActivity.SHOP_NAME, "")
            shopAvatar = getString(TalkProductActivity.SHOP_AVATAR, "")
        } ?: arguments?.run {
            shopId = getString(TalkProductActivity.SHOP_ID, "")
            productId = getString(TalkProductActivity.PRODUCT_ID, "")
            productPrice = getString(TalkProductActivity.PRODUCT_PRICE, "")
            productName = getString(TalkProductActivity.PRODUCT_NAME, "")
            productImage = MethodChecker.fromHtml(getString(TalkProductActivity
                    .PRODUCT_IMAGE, "")).toString()
            productUrl = getString(TalkProductActivity.PRODUCT_URL, "")
            shopName = getString(TalkProductActivity.SHOP_NAME, "")
            shopAvatar = getString(TalkProductActivity.SHOP_AVATAR, "")
        }

        setUpView(view)
        presenter.initProductTalk(productId)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TalkProductActivity.SHOP_ID, shopId)
        outState.putString(TalkProductActivity.PRODUCT_ID, productId)
        outState.putString(TalkProductActivity.PRODUCT_PRICE, productPrice)
        outState.putString(TalkProductActivity.PRODUCT_NAME, productName)
        outState.putString(TalkProductActivity.PRODUCT_URL, productUrl)
        outState.putString(TalkProductActivity.PRODUCT_IMAGE, productImage)
        outState.putString(TalkProductActivity.SHOP_NAME, shopName)
        outState.putString(TalkProductActivity.SHOP_AVATAR, shopAvatar)
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
        menu.findItem(R.id.action_add).isVisible = !presenter.isMyShop(shopId)
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
            if (!presenter.isLoggedIn()) {
                goToLogin()
                return
            }
            val intent = AddTalkActivity.createIntent(this, productId)
            this@ProductTalkFragment.startActivityForResult(intent, REQUEST_CREATE_TALK)
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
        list_thread.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val index = linearLayoutManager.findLastVisibleItemPosition()
                if (index != -1 && adapter.checkCanLoadMore(index)) {
                    presenter.getProductTalk(productId)
                }
            }

        })

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

    override fun onEmptyTalk(productTalkViewModel: ProductTalkViewModel) {
        setupViewModel(productTalkViewModel)

        setHasOptionsMenu(false)
        adapter.showEmpty(presenter.isMyShop(shopId))
    }

    override fun setCanLoad() {
        adapter.showLoading()
    }

    override fun onSuccessResetTalk(productTalkViewModel: ProductTalkViewModel) {
        setupViewModel(productTalkViewModel)


        adapter.setList(productTalkViewModel.listThread, ProductTalkTitleViewModel(productImage,
                productName, productPrice))

    }

    private fun setupViewModel(productTalkViewModel: ProductTalkViewModel) {
        productId = productTalkViewModel.productId.toString()
        shopId = productTalkViewModel.shopId.toString()
        productName = productTalkViewModel.productName
        productImage = productTalkViewModel.productImage
        productPrice = productTalkViewModel.productPrice
        productUrl = productTalkViewModel.productUrl
        shopName = productTalkViewModel.shopName
        shopAvatar = productTalkViewModel.shopAvatar
    }

    override fun onSuccessGetTalks(productTalkViewModel: ProductTalkViewModel) {
        setupViewModel(productTalkViewModel)
        adapter.hideLoading()
        adapter.addList(productTalkViewModel.listThread)
        stopTrace()
    }

    fun stopTrace() {
        if (!isTraceStopped) {
            performanceMonitoring.stopTrace()
            isTraceStopped = true
        }
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
        NetworkErrorHelper.showGreenSnackbar(view, getString(R.string.success_delete_comment_talk))
    }

    override fun onItemTalkClick(allowReply: Boolean, talkId: String, shopId: String) {
        if (!presenter.isLoggedIn()) {
            goToLogin()
        } else {
            goToDetailTalk(talkId, shopId, allowReply)
        }
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        if (!presenter.isLoggedIn()) {
            goToLogin()
        } else {
            analytics.trackClickReplyButton(talkId)
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
                        TalkDetailsActivity.getCallingIntent(talkId, shopId, this,
                                TalkDetailsActivity.SOURCE_PDP)
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
        if (presenter.isLoggedIn()) {
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

    private fun goToReportTalk(talkId: String, shopId: String, productId: String, commentId:
    String) {
        activity?.run {
            val intent: Intent = if (commentId.isBlank()) {
                ReportTalkActivity.createIntentReportTalk(this, talkId, shopId, productId)
            } else {
                ReportTalkActivity.createIntentReportComment(this, talkId, commentId,
                        shopId, productId)
            }
            this@ProductTalkFragment.startActivityForResult(intent, REQUEST_REPORT_TALK)
        }
    }

    private fun showUnfollowTalkDialog(alertDialog: Dialog, talkId: String) {
        context?.run {
            analytics.trackClickOnMenuUnfollow()
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
            analytics.trackClickOnMenuFollow()
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
            analytics.trackClickOnMenuDelete()
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
        if (presenter.isLoggedIn()) {

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


    private fun onCommentMenuItemClicked(itemMenu: Menus.ItemMenus, bottomMenu: Menus, shopId: String, talkId: String, commentId: String, productId: String) {
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
            analytics.trackClickProductFromAttachment()
            val intent: Intent = (this as TalkRouter).getProductPageIntent(this, attachProduct
                    .productId.toString())
            this@ProductTalkFragment.startActivity(intent)
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
        if (presenter.isLoggedIn()) {
            if (shopId.isNotBlank()) {
                activity?.applicationContext?.run {
                    val intent: Intent = (this as TalkRouter).getAskSellerIntent(
                            this,
                            shopId,
                            shopName,
                            "",
                            productUrl,
                            "product",
                            shopAvatar)
                    this@ProductTalkFragment.startActivity(intent)
                }
            }
        } else {
            goToLogin()
        }
    }

    override fun onGoToUserProfile(userId: String) {
        analytics.trackClickUserProfile()
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

            if (adapter.getCommentById(talkId, commentId) != null
                    && adapter.getItemById(talkId) != null) {
                adapter.deleteComment(talkId, commentId)
            } else if (adapter.getItemById(talkId) != null
                    && !(adapter.getItemById(talkId) as TalkThreadViewModel).listChild.isEmpty()
                    && (adapter.getItemById(talkId) as TalkThreadViewModel).listChild[0] is LoadMoreCommentTalkViewModel) {
                ((adapter.getItemById(talkId) as TalkThreadViewModel).listChild[0] as LoadMoreCommentTalkViewModel).counter -= 1
            }

        } else {
            onRefreshData()
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
            if (resultCode == Activity.RESULT_OK) {
                onRefreshData()
                ToasterNormal.make(view, activity!!.getString(R.string.success_send_talk), Snackbar.LENGTH_LONG).show()
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

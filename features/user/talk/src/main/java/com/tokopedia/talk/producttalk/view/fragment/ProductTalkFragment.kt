package com.tokopedia.talk.producttalk.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.ProductTalkTypeFactoryImpl
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.producttalk.di.DaggerProductTalkComponent
import com.tokopedia.talk.producttalk.presenter.ProductTalkPresenter
import com.tokopedia.talk.producttalk.view.adapter.LoadProductTalkThreadViewHolder
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkAdapter
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkThreadViewHolder
import com.tokopedia.talk.producttalk.view.listener.ProductTalkContract
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkTitleViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
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
        TalkProductAttachmentAdapter.ProductAttachmentItemClickListener {

    override fun getContext(): Context? {
        return activity
    }


    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var presenter: ProductTalkPresenter

    lateinit var adapter: ProductTalkAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var bottomMenu: Menus
    private lateinit var alertDialog: Dialog
    private lateinit var swiper: SwipeToRefresh

    val REQUEST_REPORT_TALK: Int = 123478

    var productId: String = ""
    var productName: String = ""
    var productPrice: String = ""
    var productImage: String = ""
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
            fragment.productImage = extras.getString("product_image")
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        presenter.initProductTalk(productId)
    }


    private fun getProductTalk() {
        presenter.getProductTalk(productId)
    }

    private fun setUpView(view: View) {
        val adapterTypeFactory = ProductTalkTypeFactoryImpl(this, this, this, this)
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

    override fun onReplyTalkButtonClick(allowReply: Boolean) {
        if (allowReply) goToDetailTalk()
        else showErrorReplyTalk()
    }

    private fun showErrorReplyTalk() {
        //TODO
        NetworkErrorHelper.showRedSnackbar(view, "Error dud")
    }

    private fun goToDetailTalk() {

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

    private fun goToReportTalk() {
        activity?.run {
            val intent = ReportTalkActivity.createIntent(this, "", "" , "")
            startActivityForResult(intent, REQUEST_REPORT_TALK)
        }
    }

    private fun showUnfollowTalkDialog() {
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

    override fun onCommentMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, commentId: String, productId: String) {
        //TODO STEVENFe
    }

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
       // TODO STEVEN
    }

    override fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNoShowTalkCommentClick(talkId: String, commentId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
}

package com.tokopedia.talk.talkdetails.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.talk.R
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.view.TalkDialog
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.di.DaggerTalkDetailsComponent
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.talk.talkdetails.view.adapter.AttachingProductListAdapter
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactoryImpl
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.presenter.TalkDetailsPresenter
import kotlinx.android.synthetic.main.fragment_talk_comments.*
import javax.inject.Inject

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsFragment : BaseDaggerFragment(),
        TalkDetailsContract.View,
        CommentTalkViewHolder.TalkCommentItemListener,
        TalkProductAttachmentAdapter.ProductAttachmentItemClickListener,
        InboxTalkItemViewHolder.TalkItemListener,
        LoadMoreCommentTalkViewHolder.LoadMoreListener,
        AttachingProductListAdapter.ProductAttachingItemClickListener {

    @Inject
    lateinit var presenter: TalkDetailsPresenter

    lateinit var adapter: InboxTalkAdapter
    lateinit var talkRecyclerView: RecyclerView
    lateinit var attachProductButton: ImageView
    lateinit var sendMessageButton: ImageView
    lateinit var sendMessageEditText: EditText
    lateinit var attachedProductList: RecyclerView
    lateinit var progressBar: ProgressBar
    private var attachedProductListAdapter: AttachingProductListAdapter =
            AttachingProductListAdapter(ArrayList(), this)

    private lateinit var bottomMenu: Menus
    private lateinit var alertDialog: Dialog

    @Inject
    lateinit var talkDialog: TalkDialog

    private var talkId: String = ""
    private var shopId: String = ""

    companion object {
        const val GO_TO_REPORT_TALK_REQ_CODE = 101
        const val GO_TO_ATTACH_PRODUCT_REQ_CODE = 102
    }

    override fun getScreenName(): String {
        return "Talk Details"
    }

    override fun initInjector() {
        val detailTalkComponent = DaggerTalkDetailsComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        detailTalkComponent.inject(this)
        presenter.attachView(this)

        attachedProductListAdapter.data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.run {
            talkId = savedInstanceState.getString(TalkDetailsActivity.THREAD_TALK_ID, "")
            shopId = savedInstanceState.getString(TalkDetailsActivity.SHOP_ID, "")

        } ?: arguments?.run {
            talkId = getString(TalkDetailsActivity.THREAD_TALK_ID, "")
            shopId = getString(TalkDetailsActivity.SHOP_ID, "")
        } ?: activity?.run {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TalkDetailsActivity.THREAD_TALK_ID, talkId)
        outState.putString(TalkDetailsActivity.SHOP_ID, shopId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        attachedProductList = view.findViewById(R.id.talk_details_attach_product_list)
        talkRecyclerView = view.findViewById(R.id.talk_details_reply_list)
        attachProductButton = view.findViewById(R.id.add_url)
        attachProductButton.setOnClickListener { goToAttachProductScreen() }
        sendMessageEditText = view.findViewById(R.id.new_comment)
        sendMessageButton = view.findViewById(R.id.send_but)
        sendMessageButton.setOnClickListener {

            KeyboardHandler.DropKeyboard(context, view)
            presenter.sendComment(talkId,
                    shopId,
                    sendMessageEditText.text.toString(),
                    attachedProductListAdapter.data)
        }
        setupView()
        loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_comments, container, false)
    }


    private fun setupView() {
        attachedProductList.layoutManager = LinearLayoutManager(context, LinearLayoutManager
                .HORIZONTAL, false)
        attachedProductList.adapter = attachedProductListAdapter

        val adapterTypeFactory = TalkDetailsTypeFactoryImpl(this,
                this,
                this,
                this)
        val listTalk = ArrayList<Visitable<*>>()
        adapter = InboxTalkAdapter(adapterTypeFactory, listTalk)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        talkRecyclerView.layoutManager = linearLayoutManager
        talkRecyclerView.adapter = adapter
    }

    fun loadData() {
        showLoadingAction()
        presenter.loadTalkDetails(talkId)
    }

    override fun showLoadingAction() {
        progressBar.visibility = View.VISIBLE
        mainView.visibility = View.GONE

    }

    override fun hideLoadingAction() {
        progressBar.visibility = View.GONE
        mainView.visibility = View.VISIBLE
    }


    private fun showErrorTalk(message: String) {
        if (adapter.itemCount == 0) {
            NetworkErrorHelper.showEmptyState(context, view, message) {
                loadData()
            }
        } else {
            NetworkErrorHelper.showRedSnackbar(view, message)
        }
    }

    //TalkDetailsContract.View
    override fun onError(throwable: Throwable) {
        hideLoadingAction()
        showErrorTalk(throwable.message ?: "Unknown Error")
    }

    override fun onSuccessLoadTalkDetails(data: ArrayList<Visitable<*>>) {
        hideLoadingAction()
        adapter.setList(data)
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString(TalkDetailsActivity.THREAD_TALK_ID, talkId)
            intent.putExtras(bundle)
            setResult(TalkDetailsActivity.RESULT_OK_READ, intent)
        }
    }

    override fun onSuccessDeleteTalkComment(id: String) {
        hideLoadingAction()
        adapter.deleteComment(id, id)
    }

    override fun onSuccessSendTalkComment(commentId: String) {
        sendMessageEditText.setText("")
        attachedProductListAdapter.clearAllElements()
        attachedProductList.visibility = View.GONE
        adapter.clearAllElements()
        presenter.loadTalkDetails(talkId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GO_TO_ATTACH_PRODUCT_REQ_CODE &&
                resultCode == AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK) {
            val products = data?.getParcelableArrayListExtra<ResultProduct>(AttachProductActivity
                    .TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)!!
            setAttachedProduct(convertAttachProduct(products))
        } else if (requestCode == GO_TO_REPORT_TALK_REQ_CODE
                && resultCode == Activity.RESULT_OK) {
            data?.extras?.run {
                val talkId = getString(ReportTalkActivity.EXTRA_TALK_ID, "")
                onSuccessReportTalk(talkId)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onSuccessReportTalk(talkId: String) {
        activity?.run {
            NetworkErrorHelper.showGreenSnackbar(this, getString(R.string.success_report_talk))
        }

        context?.run {
            if (!talkId.isBlank()) {
                adapter.updateReportTalk(talkId, this)
            }
        }

    }

    private fun goToAttachProductScreen() {
        val intent = AttachProductActivity.createInstance(context, shopId, "", false)
        startActivityForResult(intent, GO_TO_ATTACH_PRODUCT_REQ_CODE)

    }

    private fun convertAttachProduct(products: ArrayList<ResultProduct>)
            : ArrayList<TalkProductAttachmentViewModel> {
        val list = ArrayList<TalkProductAttachmentViewModel>()
        for (product in products) {
            list.add(TalkProductAttachmentViewModel(
                    productId = product.productId,
                    productImage = product.productImageThumbnail,
                    productName = product.name,
                    productPrice = product.price
            ))
        }
        return list
    }

    private fun setAttachedProduct(products: ArrayList<TalkProductAttachmentViewModel>) {
        attachedProductListAdapter.data = products
        attachedProductListAdapter.notifyDataSetChanged()
        if (attachedProductListAdapter.data.isEmpty())
            attachedProductList.visibility = View.GONE
        else
            attachedProductList.visibility = View.VISIBLE
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
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        when (itemMenu.title) {
            getString(R.string.menu_report_comment) -> goToReportTalkPage(talkId, shopId, productId,
                    commentId)
            getString(R.string.menu_delete_comment) -> showDeleteCommentTalkDialog(shopId,
                    talkId, commentId)
        }
        bottomMenu.dismiss()
    }

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
        onGoToPdp(attachProduct.productId.toString())
    }

    override fun onDeleteAttachProduct(element: TalkProductAttachmentViewModel) {
        attachedProductListAdapter.remove(element)
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

    private fun onMenuItemClicked(itemMenus: Menus.ItemMenus, bottomMenu: Menus, shopId: String, talkId: String, productId: String) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        when (itemMenus.title) {
            getString(R.string.menu_delete_talk) -> showDeleteTalkDialog(alertDialog, shopId, talkId)
            getString(R.string.menu_follow_talk) -> showFollowTalkDialog(alertDialog, talkId)
            getString(R.string.menu_unfollow_talk) -> showUnfollowTalkDialog(alertDialog, talkId)
            getString(R.string.menu_report_talk) -> goToReportTalkPage(talkId, shopId, productId,
                    "")
        }
        bottomMenu.dismiss()
    }


    private fun showDeleteTalkDialog(alertDialog: Dialog, shopId: String, talkId: String) {
        context?.run {
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

    private fun showFollowTalkDialog(alertDialog: Dialog, talkId: String) {

        context?.run {
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


    private fun showUnfollowTalkDialog(alertDialog: Dialog, talkId: String) {
        context?.run {
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

    override fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String) {
        goToReportTalkPage(talkId, shopId, productId, "")
    }

    override fun onNoShowTalkItemClick(talkId: String) {
        presenter.markTalkNotFraud(talkId)
    }

    override fun goToReportTalkPage(talkId: String, shopId: String, productId: String, commentId
    : String) {
        activity?.run {
            val intent: Intent = if (commentId.isBlank()) {
                ReportTalkActivity.createIntentReportTalk(this, talkId, shopId, productId)
            } else {
                ReportTalkActivity.createIntentReportComment(this, talkId, shopId,
                        productId, commentId)
            }
            this@TalkDetailsFragment.startActivityForResult(intent, GO_TO_REPORT_TALK_REQ_CODE)
        }
    }

    override fun onNoShowTalkCommentClick(talkId: String, commentId: String) {
        presenter.markCommentNotFraud(talkId, commentId)
    }

    override fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String, commentId: String) {
        goToReportTalkPage(talkId, shopId, productId, commentId)
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        //There should not be reply button
    }

    override fun onGoToPdp(productId: String) {
        activity?.applicationContext?.run {
            (this as TalkRouter).goToProductDetailById(this, productId)
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

    override fun onSuccessMarkCommentNotFraud(talkId: String, commentId: String) {
        adapter.showReportedCommentTalk(talkId, commentId)
    }

    override fun onSuccessMarkTalkNotFraud(talkId: String) {
        adapter.showReportedTalk(talkId)
    }

    override fun onGoToUserProfile(userId: String) {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getTopProfileIntent(this, userId)
            this@TalkDetailsFragment.startActivity(intent)
        }
    }

    override fun onGoToShopPage(shopId: String) {
        activity?.applicationContext?.run {
            val intent: Intent = (this as TalkRouter).getShopPageIntent(this, shopId)
            this@TalkDetailsFragment.startActivity(intent)
        }
    }

    override fun onLoadMoreCommentClicked(talkId: String, shopId: String, allowReply: Boolean) {
        //TODO : TO BE IMPLEMENTED
    }

}
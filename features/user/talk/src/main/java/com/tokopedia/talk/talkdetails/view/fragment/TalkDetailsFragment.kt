package com.tokopedia.talk.talkdetails.view.fragment

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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.di.DaggerTalkDetailsComponent
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactoryImpl
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.presenter.TalkDetailsPresenter
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkAdapter
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.talkdetails.view.adapter.AttachedProductListAdapter
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsFragment : BaseDaggerFragment(),
        TalkDetailsContract.View,
        CommentTalkViewHolder.TalkCommentItemListener,
        TalkProductAttachmentAdapter.ProductAttachmentItemClickListener,
        InboxTalkItemViewHolder.TalkItemListener,
        LoadMoreCommentTalkViewHolder.LoadMoreListener{
    @Inject
    lateinit var presenter: TalkDetailsPresenter

    lateinit var adapter: InboxTalkAdapter
    lateinit var talkRecyclerView: RecyclerView
    lateinit var attachProductButton: ImageView
    lateinit var sendMessageButton: ImageView
    lateinit var sendMessageEditText: EditText
    lateinit var attachedProductList: RecyclerView
    lateinit var progressBar: ProgressBar
    var attachedProductListAdapter: AttachedProductListAdapter =
            AttachedProductListAdapter(ArrayList<TalkProductAttachmentViewModel>())

    private lateinit var bottomMenu: Menus
    private lateinit var alertDialog: Dialog

    fun loadData() {
        showLoadingAction()
        presenter.loadTalkDetails(arguments?.getString(TalkDetailsActivity.THREAD_TALK_ID) ?: "")
    }

    override fun getScreenName(): String {
        return "Talk Details"
    }

    override fun initInjector() {
        val talkDetailsComponent = DaggerTalkDetailsComponent.builder().baseAppComponent(
                (activity!!.application as BaseMainApplication).baseAppComponent).build()
        talkDetailsComponent.inject(this)
        presenter.attachView(this)

        attachedProductListAdapter.data
    }

    private fun setupView(){
        attachedProductList.layoutManager = LinearLayoutManager(context,LinearLayoutManager
                .HORIZONTAL,false)
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

    override fun showLoadingAction() {
        progressBar.visibility = View.VISIBLE
        talkRecyclerView.visibility = View.GONE
    }

    override fun hideLoadingAction() {
        progressBar.visibility = View.GONE
        talkRecyclerView.visibility = View.VISIBLE
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
        sendMessageButton.setOnClickListener{
            val talkId = arguments?.getString(TalkDetailsActivity.THREAD_TALK_ID) ?:""
            val shopId = arguments?.getString(TalkDetailsActivity.SHOP_ID)?:""
            val userId = UserSession(context).userId
            presenter.sendComment(talkId = talkId,
                    productId = shopId,
                    attachedProduct = ArrayList(),
                    userId = userId,
                    message = sendMessageEditText.text.toString())
        }
        setupView()
        loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_talk_comments, container,false)
        return view
    }


    fun showErrorTalk(message:String) {
        NetworkErrorHelper.showRedSnackbar(view, message)
    }

    //TalkDetailsContract.View
    override fun onError(throwable: Throwable) {
        hideLoadingAction()
        showErrorTalk(throwable.message?: "Unknown Error")
    }

    override fun onSuccessLoadTalkDetails(data: ArrayList<Visitable<*>>) {
        hideLoadingAction()
        adapter.setList(data)
    }

    override fun onSuccessDeleteTalkComment(id: String) {
        hideLoadingAction()
        adapter.deleteComment(id,id)
    }

    override fun onSuccessSendTalkComment(commentId: String) {
        sendMessageEditText.setText("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GO_TO_ATTACH_PRODUCT_REQ_CODE &&
                resultCode == AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK) {
            val products = data?.getParcelableArrayListExtra<ResultProduct>(AttachProductActivity
                    .TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)!!
            setAttachedProduct(convertAttachProduct(products))
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    fun goToAttachProductScreen(){
        val shopId = arguments?.getString(TalkDetailsActivity.SHOP_ID)?:""
        val intent = AttachProductActivity.createInstance(context,shopId,"",false)
        startActivityForResult(intent, GO_TO_ATTACH_PRODUCT_REQ_CODE)

    }
    fun convertAttachProduct(products:ArrayList<ResultProduct>)
            :ArrayList<TalkProductAttachmentViewModel> {
        var list = ArrayList<TalkProductAttachmentViewModel>()
        for(product in products) {
            list.add(TalkProductAttachmentViewModel(
                    productId = product.productId,
                    productImage = product.productImageThumbnail,
                    productName = product.name,
                    productPrice = product.price
            ))
        }
        return list
    }

    fun setAttachedProduct(products:ArrayList<TalkProductAttachmentViewModel>){
        attachedProductListAdapter.data = products
        attachedProductListAdapter.notifyDataSetChanged()
        if(attachedProductListAdapter.data.isEmpty())
            attachedProductList.visibility = View.GONE
        else
            attachedProductList.visibility = View.VISIBLE
    }

    override fun onCommentMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, commentId: String, productId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNoShowTalkCommentClick(talkId: String, commentId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickProductAttachment(attachProduct: TalkProductAttachmentViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, productId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNoShowTalkItemClick(talkId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToReportTalkPage(id: String, shopId: String, productId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String, commentId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToPdp(productId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToUserProfile(userId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadMoreCommentClicked(talkId: String, shopId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val GO_TO_REPORT_TALK_REQ_CODE = 101
        const val GO_TO_ATTACH_PRODUCT_REQ_CODE = 102
    }

}
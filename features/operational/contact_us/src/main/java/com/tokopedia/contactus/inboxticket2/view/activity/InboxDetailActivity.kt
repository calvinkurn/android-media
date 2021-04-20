package com.tokopedia.contactus.inboxticket2.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.model.Tickets
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem
import com.tokopedia.contactus.inboxticket2.view.adapter.ImageUploadAdapter
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxDetailAdapter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract.InboxDetailView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.fragment.CloseComplainBottomSheet
import com.tokopedia.contactus.inboxticket2.view.fragment.CloseComplainBottomSheet.CloseComplainBottomSheetListner
import com.tokopedia.contactus.inboxticket2.view.fragment.HelpFullBottomSheet
import com.tokopedia.contactus.inboxticket2.view.fragment.HelpFullBottomSheet.CloseSHelpFullBottomSheet
import com.tokopedia.contactus.inboxticket2.view.fragment.ImageViewerFragment
import com.tokopedia.contactus.inboxticket2.view.fragment.ImageViewerFragment.Companion.newInstance
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxticket2.view.fragment.ServicePrioritiesBottomSheet.CloseServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxticket2.view.listeners.InboxDetailListener
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

const val TICKET_STATUS_IN_PROCESS = "in_process"
const val TICKET_STATUS_NEED_RATING = "need_rating"
const val TICKET_STATUS_CLOSED = "closed"

class InboxDetailActivity : InboxBaseActivity(), InboxDetailView, ImageUploadAdapter.OnSelectImageClick, View.OnClickListener, CloseServicePrioritiesBottomSheet, CloseSHelpFullBottomSheet, CloseComplainBottomSheetListner, InboxDetailListener {

    private lateinit var rvMessageList: RecyclerView
    private lateinit var rvSelectedImages: RecyclerView
    private lateinit var ivUploadImg: ImageView
    private lateinit var ivSendButton: ImageView
    private lateinit var edMessage: EditText
    private lateinit var sendProgress: View
    private lateinit var viewHelpRate: View
    private lateinit var textToolbar: View
    private lateinit var viewLinkBottom: View
    private lateinit var editText: CustomEditText
    private lateinit var searchView: View
    private lateinit var ivPrevious: View
    private lateinit var ivNext: View
    private lateinit var totalRes: TextView
    private lateinit var currentRes: TextView
    private lateinit var btnInactive1: ImageView
    private lateinit var btnInactive2: ImageView
    private lateinit var btnInactive3: ImageView
    private lateinit var btnInactive4: ImageView
    private lateinit var btnInactive5: ImageView
    private lateinit var txtHyper: TextView
    private lateinit var noTicketFound: View
    private lateinit var tvNoTicket: TextView
    private lateinit var tvOkButton: TextView
    private lateinit var rootView: ConstraintLayout
    private lateinit var viewReplyButton: View
    private lateinit var tvReplyButton: TextView
    private lateinit var imageUploadAdapter: ImageUploadAdapter
    private lateinit var detailAdapter: InboxDetailAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var mCommentID: String? = null
    private var isCustomReason = false
    private var helpFullBottomSheet: CloseableBottomSheetDialog? = null
    private var closeComplainBottomSheet: CloseableBottomSheetDialog? = null
    private var servicePrioritiesBottomSheet: CloseableBottomSheetDialog? = null
    private val commentsItems: MutableList<CommentsItem> by lazy { mutableListOf<CommentsItem>() }
    private var iscloseAllow = false
    private var isSendButtonEnabled = false

    override fun getCommentID(): String = mCommentID ?: ""

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun renderMessageList(ticketDetail: Tickets) {
        commentsItems.clear()
        commentsItems.addAll(ticketDetail.comments?: mutableListOf())

        iscloseAllow = ticketDetail.isAllowClose
        edMessage.text.clear()
        setSubmitButtonEnabled(false)
        viewHelpRate.hide()
        textToolbar.show()
        if (!commentsItems.isNullOrEmpty()){
            updateUiBasedOnStatus(ticketDetail)
            setHeaderPriorityLabel()
            detailAdapter = InboxDetailAdapter(this,
                    commentsItems,
                    ticketDetail.isNeedAttachment,
                    (mPresenter as InboxDetailContract.Presenter),
                    this,
                    (mPresenter as? InboxDetailContract.Presenter)?.getUserId() ?: "",
                    (mPresenter as? InboxDetailContract.Presenter)?.getTicketId() ?: "")
            rvMessageList.adapter = detailAdapter
            rvMessageList.show()
            scrollTo(detailAdapter.itemCount - 1)
        }else{
            rvMessageList.hide()
        }
    }

    private fun setHeaderPriorityLabel() {
        commentsItems[0].priorityLabel = intent.getBooleanExtra(IS_OFFICIAL_STORE, false)
    }

    private fun updateUiBasedOnStatus(ticketDetail: Tickets) {
        when (commentsItems[0].ticketStatus) {
            TICKET_STATUS_IN_PROCESS -> {
                rvMessageList.setPadding(0, 0, 0,
                        resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_collapsed))
                if (ROLE_TYPE_AGENT.equals(commentsItems[commentsItems.size - 1].createdBy?.role, ignoreCase = true) && "".equals(commentsItems[commentsItems.size - 1].rating, ignoreCase = true)) {
                    viewReplyButton.show()
                    mCommentID = commentsItems[commentsItems.size - 1].id
                }
                if (ticketDetail.isShowRating) {
                    toggleTextToolbar(View.GONE)
                    mCommentID = commentsItems[commentsItems.size - 1].id
                }
            }
            TICKET_STATUS_CLOSED -> {
                showIssueClosed()
            }
            TICKET_STATUS_NEED_RATING -> {
                toggleTextToolbar(View.GONE)
                mCommentID = commentsItems[commentsItems.size - 1].id
            }
        }
    }

    override fun updateAddComment(newItem: CommentsItem) {
        edMessage.text.clear()
        setSubmitButtonEnabled(false)
        imageUploadAdapter.clearAll()
        imageUploadAdapter.notifyDataSetChanged()
        rvSelectedImages.hide()
        rvMessageList.setPadding(0, 0, 0,
                resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_collapsed))
        detailAdapter.setNeedAttachment(false)
        detailAdapter.addComment(newItem)
    }

    override fun toggleSearch(visibility: Int) {
        searchView.visibility = visibility
        mMenu?.findItem(R.id.action_search)?.isVisible = visibility != View.VISIBLE
    }

    override fun updateDataSet() {}
    override fun clearSearch() {
        editText.setText("")
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_ticket_details_activity
    }

    override fun getPresenter(): InboxBasePresenter {
        return component.getInboxDetailPresenter()
    }

    override fun initView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        findingViewsId()
        (mPresenter as InboxDetailContract.Presenter).getTicketDetails((mPresenter as InboxDetailContract.Presenter).getTicketId())
        rvMessageList.layoutManager = layoutManager
        editText.setListener((mPresenter as InboxDetailContract.Presenter).getSearchListener())
        settingClickListner()
    }

    private fun findingViewsId() {
        rootView = findViewById(R.id.root_view)
        rvMessageList = findViewById(R.id.rv_message_list)
        rvSelectedImages = findViewById(R.id.rv_selected_images)
        ivUploadImg = findViewById(R.id.iv_upload_img)
        ivSendButton = findViewById(R.id.iv_send_button)
        edMessage = findViewById(R.id.ed_message)
        sendProgress = findViewById(R.id.send_progress)
        viewHelpRate = findViewById(R.id.view_help_rate)
        textToolbar = findViewById(R.id.text_toolbar)
        viewLinkBottom = findViewById(R.id.view_link_bottom)
        editText = findViewById(R.id.custom_search)
        searchView = findViewById(R.id.inbox_search_view)
        ivPrevious = findViewById(R.id.iv_previous_up)
        ivNext = findViewById(R.id.iv_next_down)
        totalRes = findViewById(R.id.tv_count_total)
        currentRes = findViewById(R.id.tv_count_current)
        btnInactive1 = findViewById(R.id.btn_inactive_1)
        btnInactive2 = findViewById(R.id.btn_inactive_2)
        btnInactive3 = findViewById(R.id.btn_inactive_3)
        btnInactive4 = findViewById(R.id.btn_inactive_4)
        btnInactive5 = findViewById(R.id.btn_inactive_5)
        txtHyper = findViewById(R.id.txt_hyper)
        tvReplyButton = findViewById(R.id.tv_reply_button)
        viewReplyButton = findViewById(R.id.view_rply_botton_before_csat_rating)
        noTicketFound = findViewById(R.id.no_ticket_found)
        tvNoTicket = findViewById(R.id.tv_no_ticket)
        tvOkButton = findViewById(R.id.tv_ok_button)
    }

    private fun settingClickListner() {
        btnInactive1.setOnClickListener(this)
        btnInactive2.setOnClickListener(this)
        btnInactive3.setOnClickListener(this)
        btnInactive4.setOnClickListener(this)
        btnInactive5.setOnClickListener(this)
        ivUploadImg.setOnClickListener(this)
        ivSendButton.setOnClickListener(this)
        ivNext.setOnClickListener(this)
        ivPrevious.setOnClickListener(this)
        txtHyper.setOnClickListener(this)
        tvReplyButton.setOnClickListener(this)
    }

    override fun getMenuRes(): Int {
        return R.menu.contactus_menu_details
    }

    override fun doNeedReattach(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.let {
            val paths = UriUtil.destructureUri(ApplinkConst.TICKET_DETAIL, it)
            if (paths.isNotEmpty()) {
                val ticketId = paths[0]
                intent.putExtra(PARAM_TICKET_T_ID, ticketId)
            }
        }
        super.onCreate(savedInstanceState)
        imageUploadAdapter = ImageUploadAdapter(this, this, onClickCross)
        rvSelectedImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSelectedImages.adapter = imageUploadAdapter
        edMessage.addTextChangedListener((mPresenter as InboxDetailContract.Presenter).watcher())
    }

    private val onClickCross = {
        rvSelectedImages.hide()
        rvMessageList.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_collapsed))
    }

    private fun showImagePickerDialog() {
        val builder = ImagePickerBuilder.getOriginalImageBuilder(this);
        val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        startActivityForResult(intent, InboxBaseView.REQUEST_IMAGE_PICKER)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == InboxBaseView.REQUEST_IMAGE_PICKER
                && resultCode == Activity.RESULT_OK) {
            val imagePathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
            if (imagePathList.size <= 0) {
                return
            }
            val imagePath = imagePathList[0]
            if (!TextUtils.isEmpty(imagePath)) {
                val position = imageUploadAdapter.itemCount
                val image = ImageUpload()
                image.position = position
                image.imageId = "image" + UUID.randomUUID().toString()
                image.fileLoc = imagePath
                (mPresenter as InboxDetailContract.Presenter).onImageSelect(image)
            }
        }
    }

    private fun onClickUpload() {
        showImagePickerDialog()
        ContactUsTracking.sendGTMInboxTicket(this,"",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickAttachImage,
                "")
    }

    private fun onEmojiClick(v: View) {
        val id = v.id
        val presenter = mPresenter as InboxDetailContract.Presenter
        when (id) {
            R.id.btn_inactive_1 -> presenter.onClickEmoji(1)
            R.id.btn_inactive_2 -> presenter.onClickEmoji(2)
            R.id.btn_inactive_3 -> presenter.onClickEmoji(3)
            R.id.btn_inactive_4 -> presenter.onClickEmoji(4)
            R.id.btn_inactive_5 -> presenter.onClickEmoji(5)
        }
    }

    override fun showNoTicketView(messageError: List<String?>?) {
        noTicketFound.show()
        tvNoTicket.text = messageError?.get(0) ?: ""
        tvOkButton.setOnClickListener { finish() }
    }

    override fun showErrorMessage(error: String?) {
        Toaster.make(getRootView(), error
                ?: "", Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, "", View.OnClickListener { })
    }

    private fun sendMessage() {
        (mPresenter as InboxDetailContract.Presenter).sendMessage()
        edMessage.setHint(R.string.contact_us_type_here)
        ContactUsTracking.sendGTMInboxTicket(this, "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickSubmitReply,
                "")
    }

    private fun onClickListener(v: View) {
        val id = v.id
        if (id == R.id.txt_hyper) {
            setResult(InboxBaseView.RESULT_FINISH)
            ContactUsTracking.sendGTMInboxTicket(this, "",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickHubungi,
                    InboxTicketTracking.Label.TicketClosed)
            finish()
        }
    }

    private fun onClickNextPrev(v: View?) {
        val id = v?.id
        val index: Int
        index = if (id == R.id.iv_next_down) {
            (mPresenter as InboxDetailContract.Presenter).getNextResult()
        } else {
            (mPresenter as InboxDetailContract.Presenter).getPreviousResult()
        }
        scrollToResult(index)
    }

    private fun scrollToResult(index: Int) {
        if (index != -1) {
            layoutManager.scrollToPositionWithOffset(index, 0)
        }
    }

    override fun addImage(image: ImageUpload) {
        if (rvSelectedImages.visibility != View.VISIBLE) {
            rvSelectedImages.show()
            rvMessageList.setPadding(0, 0, 0,
                    resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_expanded))
        }
        imageUploadAdapter.addImage(image)
    }

    override fun showSendProgress() {
        sendProgress.show()
        ivSendButton.invisible()
    }

    override fun hideSendProgress() {
        sendProgress.hide()
        ivSendButton.show()
    }

    override fun toggleTextToolbar(visibility: Int) {
        if (visibility == View.VISIBLE) {
            viewHelpRate.hide()
            rvMessageList.setPadding(0, 0, 0,
                    resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_collapsed))
        } else {
            viewHelpRate.show()
            rvMessageList.setPadding(0, 0, 0,
                    resources.getDimensionPixelSize(R.dimen.help_rate_height))
        }
        textToolbar.visibility = visibility
    }

    override fun askCustomReason() {
        ivUploadImg.hide()
        rvSelectedImages.hide()
        edMessage.text.clear()
        setSubmitButtonEnabled(false)
        edMessage.setHint(R.string.contact_us_type_here)
        viewHelpRate.hide()
        textToolbar.show()
        rvMessageList.setPadding(0, 0, 0,
                resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_collapsed))
        isCustomReason = true
    }

    override fun showIssueClosed() {
        viewHelpRate.hide()
        textToolbar.hide()
        viewLinkBottom.show()
        rvMessageList.setPadding(0, 0, 0,
                resources.getDimensionPixelSize(R.dimen.contact_us_text_toolbar_height_collapsed))
    }

    override fun enterSearchMode(search: String, total: Int) {
        textToolbar.hide()
        viewHelpRate.hide()
        viewLinkBottom.hide()
        detailAdapter.enterSearchMode(search)
        val placeHolder = "/%s"
        if (total <= 0) {
            if (total == 0) {
                currentRes.text = "0"
                totalRes.text = "/0"
            } else {
                currentRes.text = ""
                totalRes.text = ""
            }
            ivPrevious.isClickable = false
            ivNext.isClickable = false
        } else {
            totalRes.text = String.format(placeHolder, total.toString())
            ivPrevious.isClickable = true
            ivNext.isClickable = true
            onClickNextPrev(ivPrevious)
        }
        rvMessageList.setPadding(0, 0, 0, 0)
    }

    override fun exitSearchMode() {
        detailAdapter.exitSearchMode()
    }

    override fun showImagePreview(position: Int, imagesURL: ArrayList<String>) {
        var imageViewerFragment = supportFragmentManager.findFragmentByTag(ImageViewerFragment.TAG) as ImageViewerFragment?
        if (imageViewerFragment == null) {
            imageViewerFragment = newInstance(position, imagesURL)
        } else {
            imageViewerFragment.setImageData(position, imagesURL)
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, imageViewerFragment, ImageViewerFragment.TAG)
        transaction.addToBackStack(ImageViewerFragment.TAG)
        transaction.commit()
    }

    override fun setCurrentRes(currentRes: Int) {
        this.currentRes.text = currentRes.toString()
    }

    override fun updateClosedStatus() {
        if (!commentsItems.isNullOrEmpty()){
            commentsItems[0].ticketStatus = TICKET_STATUS_CLOSED
            detailAdapter.notifyItemChanged(0)
        }
    }

    override fun isSearchMode(): Boolean {
        return searchView.visibility == View.VISIBLE
    }

    override fun setSubmitButtonEnabled(enabled: Boolean) {
        isSendButtonEnabled = enabled
        if (enabled) {
            ivSendButton.setColorFilter(ContextCompat.getColor(this, com.tokopedia.design.R.color.green_nob))
        } else {
            ivSendButton.clearColorFilter()
        }
    }

    override val imageList: List<ImageUpload>
        get() = imageUploadAdapter.getUploadedImageList()

    override val userMessage: String
        get() = edMessage.text.toString()

    override val ticketID: String
        get() = intent.getStringExtra(PARAM_TICKET_ID)?:""

    override fun onClick() {
        showImagePickerDialog()
    }

    fun scrollTo(position: Int) {
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Long>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(aLong: Long) {
                        if ((rvMessageList.layoutManager as LinearLayoutManager?)?.findFirstCompletelyVisibleItemPosition() != position) scrollToResult(position)
                    }
                })
    }

    override fun onBackPressed() {
        if (imageUploadAdapter.itemCount > 1 || textToolbar.visibility == View.VISIBLE &&
                edMessage.isFocused && edMessage.text.isNotEmpty() && supportFragmentManager.backStackEntryCount <= 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.inbox_title_dialog_wrong_scan))
            builder.setMessage(R.string.abandon_message_warning)
            builder.setNegativeButton(getString(R.string.inbox_cancel)
            ) { dialog: DialogInterface, i: Int -> dialog.dismiss() }
            builder.setPositiveButton(getString(R.string.inbox_exit)
            ) { _, _ ->
                ContactUsTracking.sendGTMInboxTicket(this, "",
                        InboxTicketTracking.Category.EventInboxTicket,
                        InboxTicketTracking.Action.EventAbandonReplySubmission,
                        getString(R.string.inbox_cancel))
                super.onBackPressed()
            }.create().show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.iv_upload_img) {
            onClickUpload()
        } else if (id == R.id.btn_inactive_1 || id == R.id.btn_inactive_2 || id == R.id.btn_inactive_3 || id == R.id.btn_inactive_4 || id == R.id.btn_inactive_5) {
            onEmojiClick(view)
        } else if (id == R.id.iv_send_button) {
            sendMessage(isSendButtonEnabled)
        } else if (id == R.id.txt_hyper) {
            onClickListener(view)
        } else if (id == R.id.iv_next_down || id == R.id.iv_previous_up) {
            onClickNextPrev(view)
        } else {
            var rating: String? = ""
            if (view.id == R.id.tv_reply_button) {
                rating = commentsItems[commentsItems.size - 1].rating
                if (rating != null && (rating == KEY_LIKED || rating == KEY_DIS_LIKED)) {
                    viewReplyButton.hide()
                    textToolbar.show()
                } else {
                    helpFullBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity())
                    helpFullBottomSheet?.setCustomContentView(HelpFullBottomSheet(this@InboxDetailActivity, this), "", true)
                    helpFullBottomSheet?.show()
                    viewReplyButton.hide()
                    textToolbar.show()
                }
            }
        }
    }

    override fun onClick(agreed: Boolean) {
        var item: CommentsItem? = null
        var commentPosition = 0
        for (i in detailAdapter.itemCount - 1 downTo 0) {
            val item1 = commentsItems[i]
            if (item1.createdBy?.role == "agent") {
                item = item1
                commentPosition = i
                break
            }
        }
        if (agreed) {
            viewReplyButton.hide()
            (mPresenter as InboxDetailContract.Presenter).onClick(true, commentPosition, item?.id ?: "")
            helpFullBottomSheet?.dismiss()
            if (iscloseAllow) {
                sendGTmEvent(InboxTicketTracking.Label.EventHelpful,
                        InboxTicketTracking.Action.EventRatingCsatOnSlider)
                closeComplainBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity())
                closeComplainBottomSheet?.setCustomContentView(CloseComplainBottomSheet(this@InboxDetailActivity, this), "", false)
                closeComplainBottomSheet?.show()
            } else {
                textToolbar.show()
            }
        } else {
            sendGTmEvent(InboxTicketTracking.Label.EventNotHelpful,
                    InboxTicketTracking.Action.EventRatingCsatOnSlider)
            (mPresenter as InboxDetailContract.Presenter).onClick(false, commentPosition, item?.id ?: "")
            textToolbar.show()
            helpFullBottomSheet?.dismiss()
        }
    }

    override fun onSuccessSubmitOfRating(rating: Int, commentPosition: Int) {
        val item = commentsItems[commentPosition]
        val rate = if (rating == INT_KEY_LIKED) KEY_LIKED else KEY_DIS_LIKED
        item.rating = rate
        detailAdapter.notifyItemChanged(commentPosition, item)
    }

    override fun onClickComplain(agreed: Boolean) {
        if (agreed) {
            sendGTmEvent(InboxTicketTracking.Label.EventYes,
                    InboxTicketTracking.Action.EventClickCloseTicket)
            (mPresenter as InboxDetailContract.Presenter).closeTicket()
            closeComplainBottomSheet?.dismiss()
        } else {
            sendGTmEvent(InboxTicketTracking.Label.EventNo,
                    InboxTicketTracking.Action.EventClickCloseTicket)
            viewReplyButton.hide()
            viewHelpRate.hide()
            textToolbar.show()
            closeComplainBottomSheet?.dismiss()
        }
    }

    override fun OnSucessfullTicketClose() {
        Observable.timer(DELAY_FOUR_MILLIS.toLong(), TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Long>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {}
                    override fun onNext(aLong: Long) {
                        mPresenter?.refreshLayout()
                    }
                })
        (mPresenter as InboxDetailContract.Presenter).onClickEmoji(0)
    }

    override fun showMessage(message: String) {
        super.showMessage(message)
        Toaster.make(rootView, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, SNACKBAR_OK, View.OnClickListener {})
    }

    override fun onClickClose() {
        servicePrioritiesBottomSheet?.dismiss()
    }

    private fun sendMessage(isSendButtonEnabled: Boolean) {
        if (isSendButtonEnabled) {
            sendMessage()
        } else {
            Toaster.make(getRootView(), this.getString(R.string.contact_us_minimum_length_error_text), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, SNACKBAR_OK, View.OnClickListener { })
        }
    }

    private fun sendGTmEvent(eventLabel: String, action: String) {
        ContactUsTracking.sendGTMInboxTicket(this, InboxTicketTracking.Event.EventName,
                InboxTicketTracking.Category.EventHelpMessageInbox,
                action,
                eventLabel)
    }

    override fun onPriorityLabelClick() {
        servicePrioritiesBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity())
        servicePrioritiesBottomSheet?.setCustomContentView(ServicePrioritiesBottomSheet(this@InboxDetailActivity, this@InboxDetailActivity), "", false)
        servicePrioritiesBottomSheet?.show()
    }

    override fun onTransactionDetailsClick() {
        ContactUsTracking.sendGTMInboxTicket(this, "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickDetailTrasanksi,
                "")
    }

    companion object {
        const val KEY_LIKED = "101"
        const val KEY_DIS_LIKED = "102"
        const val INT_KEY_LIKED = 101
        const val DELAY_FOUR_MILLIS = 4000
        const val SNACKBAR_OK = "Ok"
        const val ROLE_TYPE_AGENT = "agent"
        const val PARAM_TICKET_ID = "ticket_id"
        const val PARAM_TICKET_T_ID = "id"
        const val IS_OFFICIAL_STORE = "is_official_store"
        fun getIntent(context: Context?, ticketId: String?, officialStore: Boolean = false): Intent {
            val intent = Intent(context, InboxDetailActivity::class.java)
            intent.putExtra(PARAM_TICKET_ID, ticketId)
            intent.putExtra(IS_OFFICIAL_STORE, officialStore)
            return intent
        }
    }
}
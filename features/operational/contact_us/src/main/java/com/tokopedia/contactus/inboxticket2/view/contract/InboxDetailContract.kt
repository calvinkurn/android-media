package com.tokopedia.contactus.inboxticket2.view.contract

import android.text.TextWatcher
import com.tokopedia.contactus.inboxticket2.data.ImageUpload
import com.tokopedia.contactus.inboxticket2.data.model.Tickets
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.utils.Utils
import java.util.*

interface InboxDetailContract {
    interface InboxDetailView : InboxBaseView {
        fun renderMessageList(ticketDetail: Tickets)
        fun updateAddComment(newItem: CommentsItem)
        fun addImage(image: ImageUpload)
        fun setSubmitButtonEnabled(enabled: Boolean)
        val imageList: List<ImageUpload>
        val userMessage: String
        val ticketID: String
        fun showSendProgress()
        fun hideSendProgress()
        fun toggleTextToolbar(visibility: Int)
        fun askCustomReason()
        fun showIssueClosed()
        fun enterSearchMode(search: String, total: Int)
        fun exitSearchMode()
        fun showImagePreview(position: Int, imagesURL: ArrayList<String>)
        fun setCurrentRes(currentRes: Int)
        fun updateClosedStatus()
        fun getCommentID(): String
        fun showErrorMessage(error: String?)
        fun onSuccessSubmitOfRating(rating: Int, commentPosition: Int)
        fun OnSucessfullTicketClose()
        fun showNoTicketView(messageError: List<String?>?)
    }

    interface Presenter : InboxBasePresenter {
        fun getSearchListener(): CustomEditText.Listener
        fun onImageSelect(image: ImageUpload)
        fun watcher(): TextWatcher
        fun sendMessage()
        fun getNextResult(): Int
        fun getPreviousResult(): Int
        fun getUtils(): Utils
        fun showImagePreview(position: Int, imagesURL: List<AttachmentItem>)
        fun onClickEmoji(number: Int)
        fun onClick(agreed: Boolean, commentPosition: Int, commentId: String)
        fun closeTicket()
        fun getTicketStatus(): String
        fun getTicketDetails(ticketId: String?)
        fun getTicketId(): String?
        fun getUserId():String

    }
}
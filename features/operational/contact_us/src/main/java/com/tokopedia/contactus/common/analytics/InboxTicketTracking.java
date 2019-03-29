package com.tokopedia.contactus.common.analytics;

/**
 * Created by pranaymohapatra on 17/08/18.
 */

public interface InboxTicketTracking {

    interface Category {
        String EventInboxTicket = "Inbox Ticket";
    }
    interface Action{
        String EventTicketClick = "Click Ticket";
        String EventClickHubungi = "Click Hubungi Kami";
        String EventClickDetailTrasanksi = "Click Detail Transaksi";
        String EventClickAttachImage = "Click Attach Image";
        String EventClickSubmitReply = "Click Submit Reply";
        String EventClickReason = "Click Reason";
        String EventClickFilter = "Click Filter";
        String EventClickSelectAllMessage = "Click Select All Message";
        String EventClickDeselectAllMessage = "Click Deselect All Message";
        String EventClickDeleteMessage = "Click Delete Message";
        String EventAbandonReplySubmission = "Abandon Reply Submission";
        String EventClickOpenImage = "Click Open Image";
        String EventClickSearch = "Click Search";
        String EventClickSearchDetails = "Click Search In Details";
        String EventNotAttachImageRequired = "Not Attach Image Required";
    }
    interface Label {
        String InboxEmpty = "Inbox - Empty State";
        String TicketClosed = "Details - Closed";
        String ImageAttached = "Image Attached";
        String ImageError1 = "Error Message 1";
        String ImageError2 = "Error Message 2";
        String GetResult = "Get Result";
        String NoResult = "No Result";
    }
}

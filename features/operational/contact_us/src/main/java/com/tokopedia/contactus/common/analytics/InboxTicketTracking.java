package com.tokopedia.contactus.common.analytics;

/**
 * Created by pranaymohapatra on 17/08/18.
 */

public interface InboxTicketTracking {

    interface Event{
        String EventName = "clickInboxBantuan";
        String EventView = "viewInboxBantuan";
        String Event = "newContactUsEvent";
    }

    interface Category {
        String EventInboxTicket = "Inbox Ticket";
        String EventHelpMessageInbox = "inbox pesan bantuan";
        String EventCategoryInbox= "inbox pesan bantuan";
    }

    interface Action{
        String EventTicketClick = "click ticket";
        String EventClickHubungi = "Click Hubungi Kami";
        String EventClickDetailTrasanksi = "Click Detail Transaksi";
        String EventClickAttachImage = "Click Attach Image";
        String EventClickReplyTicket = "click reply ticket";
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
        String EventClickCsatPerReply = "click rating csat per reply";
        String EventClickOnCsatRating= "click rating csat";
        String EventRatingCsatOnSlider = "click jawaban membantu";
        String EventClickCloseTicket = "click close ticket";
        String EventImpressionOnCsatRating= "impression on csat rating";
        String EventClickSubmitCsatRating= "click submit rating csat";
        String EventClickTicketFilter= "click use filter";
        String EventClickChatbotButton= "click use chatbot";
    }
    interface Label {
        String InboxEmpty = "Inbox - Empty State";
        String TicketClosed = "Details - Closed";
        String ImageAttached = "Image Attached";
        String ImageError1 = "Error Message 1";
        String ImageError2 = "Error Message 2";
        String GetResult = "Get Result";
        String NoResult = "No Result";
        String EventHelpful = "helpful";
        String EventNotHelpful = "not helpful";
        String EventLabelYaTutup = "Ya,tutup";
        String EventLabelBatal = "Batal";
        String EventLabelYa = "Ya";
        String EventLabelTidak = "Ya";
    }
}

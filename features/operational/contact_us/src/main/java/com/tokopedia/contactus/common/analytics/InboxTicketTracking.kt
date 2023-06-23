package com.tokopedia.contactus.common.analytics

/**
 * Created by pranaymohapatra on 17/08/18.
 */
interface InboxTicketTracking {
    interface Event {
        companion object {
            const val EventName = "clickInboxBantuan"
            const val EventView = "viewInboxBantuan"
            const val Event = "newContactUsEvent"
        }
    }

    interface Category {
        companion object {
            const val EventInboxTicket = "Inbox Ticket"
            const val EventHelpMessageInbox = "inbox pesan bantuan"
            const val EventCategoryInbox = "inbox pesan bantuan"
        }
    }

    interface Action {
        companion object {
            const val EventTicketClick = "click ticket"
            const val EventClickHubungi = "Click Hubungi Kami"
            const val EventClickDetailTrasanksi = "Click Detail Transaksi"
            const val EventClickAttachImage = "Click Attach Image"
            const val EventClickReplyTicket = "click reply ticket"
            const val EventClickReason = "Click Reason"
            const val EventClickFilter = "Click Filter"
            const val EventAbandonReplySubmission = "Abandon Reply Submission"
            const val EventClickSearchDetails = "Click Search In Details"
            const val EventNotAttachImageRequired = "Not Attach Image Required"
            const val EventClickCsatPerReply = "click rating csat per reply"
            const val EventClickOnCsatRating = "click rating csat"
            const val EventRatingCsatOnSlider = "click jawaban membantu"
            const val EventClickCloseTicket = "click close ticket"
            const val EventImpressionOnCsatRating = "impression on csat rating"
            const val EventClickSubmitCsatRating = "click submit rating csat"
            const val EventClickTicketFilter = "click use filter"
            const val EventClickChatbotButton = "click use chatbot"
        }
    }

    interface Label {
        companion object {
            const val InboxEmpty = "Inbox - Empty State"
            const val TicketClosed = "Details - Closed"
            const val ImageAttached = "Image Attached"
            const val ImageError1 = "Error Message 1"
            const val ImageError2 = "Error Message 2"
            const val GetResult = "Get Result"
            const val NoResult = "No Result"
            const val EventHelpful = "helpful"
            const val EventNotHelpful = "not helpful"
            const val EventLabelYaTutup = "Ya,tutup"
            const val EventLabelBatal = "Batal"
            const val EventLabelYa = "Ya"
            const val EventLabelTidak = "Ya"
        }
    }
}
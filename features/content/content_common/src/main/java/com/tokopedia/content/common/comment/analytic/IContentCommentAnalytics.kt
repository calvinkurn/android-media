package com.tokopedia.content.common.comment.analytic

/**
 * @author by astidhiyaa on 21/03/23
 */
interface IContentCommentAnalytics {
    fun clickCommentIcon()
    fun closeCommentSheet()
    fun clickReplyChild()
    fun clickCommentName()
    fun clickProfilePicture()
    fun impressLihatBalasan()
    fun clickLihatBalasan()
    fun clickSembunyikan()
    fun clickTextBox()
    fun clickSendParentComment()
    fun clickSendChildComment()
    fun longClickComment()
    fun clickRemoveComment()
    fun clickReportComment()
    fun clickReportReason(reportType: String)
    fun impressSuccessReport()
}

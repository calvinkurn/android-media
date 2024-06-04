package com.tokopedia.feed.common.comment

import com.tokopedia.network.exception.MessageErrorException

/**
 * @author by astidhiyaa on 13/03/23
 */
class CommentException(
    message: String
) : MessageErrorException(message) {
    companion object {
        fun createSendCommentFailed(): CommentException =
            CommentException("Oops, tidak bisa memberi komentar.")
        fun createDeleteFailed(): CommentException =
            CommentException("Oops, gagal menghapus komentar.")
        fun createLinkNotAllowed(): CommentException =
            CommentException("Oops, kamu tidak bisa memberikan komentar dalam bentuk tautan, ya.")
    }
}

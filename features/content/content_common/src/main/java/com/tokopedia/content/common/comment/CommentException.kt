package com.tokopedia.content.common.comment

/**
 * @author by astidhiyaa on 13/03/23
 */
enum class CommentException (val message: String) {
    LinkNotAllowed("Oops, kamu tidak bisa memberikan komentar dalam bentuk tautan, ya."),
    FailedDelete("Oops, gagal menghapus komentar."),
    SpammedComment("Oops, tidak bisa memberi komentar.")
}

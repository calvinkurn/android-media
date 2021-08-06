package com.tokopedia.mediauploader.data.consts

import com.tokopedia.kotlin.extensions.view.formattedToMB

const val NETWORK_ERROR = "Oops, ada gangguan yang perlu kami bereskan. Refresh atau balik lagi nanti."
const val TIMEOUT_ERROR = "Oops, upload gambar perlu waktu lebih lama dari biasanya. Coba upload lagi, yuk!"
const val UNKNOWN_ERROR = "Oops, Upload gagal, silakan coba kembali beberapa saat lagi."
const val FILE_NOT_FOUND = "Oops, file tidak ditemukan."
const val SOURCE_NOT_FOUND = "Oops, source tidak ditemukan."

fun formatNotAllowedMessage(allowedFormat: String): String {
    return "Yah, formatnya belum sesuai. Pastikan format gambar kamu dalam $allowedFormat."
}

fun maxFileSizeMessage(allowedFileSize: Int): String {
    return "Wah, ukuran gambar kebesaran. Yuk, upload ulang dengan ukuran max ${allowedFileSize.toLong().formattedToMB()} Mb, ya!"
}

fun minResBitmapMessage(allowedWidth: Int, allowedHeight: Int): String {
    return "Wah, ukuran gambar kekecilan. Yuk, upload ulang dengan ukuran minimum $allowedWidth x $allowedHeight, ya!"
}

fun maxResBitmapMessage(allowedWidth: Int, allowedHeight: Int): String {
    return "Wah, ukuran gambar kebesaran. Yuk, upload ulang dengan ukuran maksimum $allowedWidth x $allowedHeight, ya!"
}
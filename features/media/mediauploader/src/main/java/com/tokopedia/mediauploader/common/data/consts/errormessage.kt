package com.tokopedia.mediauploader.common.data.consts

import com.tokopedia.kotlin.extensions.view.formattedToMB

const val TIMEOUT_ERROR = "Oops, upload gambar perlu waktu lebih lama dari biasanya. Coba upload lagi, yuk! (401)"
const val NETWORK_ERROR = "Oops, ada gangguan yang perlu kami bereskan. Refresh atau balik lagi nanti. (600)"
const val UNKNOWN_ERROR = "Oops, Upload gagal, silakan coba kembali beberapa saat lagi. (601)"
const val FILE_NOT_FOUND = "Oops, file tidak ditemukan. (602)"
const val SOURCE_NOT_FOUND = "Oops, source tidak ditemukan. (603)"

const val CHUNK_UPLOAD = "Oops, upload gagal nih, retry yuk! (500)"
const val UPLOAD_ABORT = "Yah, upload gagal nih, ulang dari awal ya! (501)"
const val TRANSCODING_FAILED = "Yah, transcodig nya gagal nih, ulang dari awal ya! (502)"
const val POLICY_NOT_FOUND = "Yah, layanan tidak ditemukan, ulang dari awal ya! (503)"

fun formatNotAllowedMessage(allowedFormat: String): String {
    return "Yah, formatnya belum sesuai. Pastikan format gambar kamu dalam $allowedFormat. (301)"
}

fun maxFileSizeMessage(allowedFileSize: Int): String {
    return "Wah, ukuran gambar kebesaran. Yuk, upload ulang dengan ukuran max ${allowedFileSize.toLong().formattedToMB()} Mb, ya! (302)"
}

fun minResBitmapMessage(allowedWidth: Int, allowedHeight: Int): String {
    return "Wah, ukuran gambar kekecilan. Yuk, upload ulang dengan ukuran minimum $allowedWidth x $allowedHeight, ya! (303)"
}

fun maxResBitmapMessage(allowedWidth: Int, allowedHeight: Int): String {
    return "Wah, ukuran gambar kebesaran. Yuk, upload ulang dengan ukuran maksimum $allowedWidth x $allowedHeight, ya! (304)"
}
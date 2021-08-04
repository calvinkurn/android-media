package com.tokopedia.mediauploader.data.consts

const val NETWORK_ERROR = "Oops, ada gangguan yang perlu kami bereskan. Refresh atau balik lagi nanti."
const val TIMEOUT_ERROR = "Oops, upload gambar perlu waktu lebih lama dari biasanya. Coba upload lagi, yuk!"
const val UNKNOWN_ERROR = "Oops, Upload gagal, silakan coba kembali beberapa saat lagi."
const val FILE_NOT_FOUND = "Oops, file tidak ditemukan."
const val SOURCE_NOT_FOUND = "Oops, source tidak ditemukan."

fun formatNotAllowedMessage(allowedFormat: String): String {
    val errorMessage = "Yah, formatnya belum sesuai. Pastikan format gambar kamu dalam "
    return "$errorMessage $allowedFormat."
}
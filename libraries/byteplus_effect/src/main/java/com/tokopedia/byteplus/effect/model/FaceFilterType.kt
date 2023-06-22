package com.tokopedia.byteplus.effect.model

/**
 * Created By : Jonathan Darwin on March 28, 2023
 */
enum class FaceFilterType(
    val id: String,
    val key: String,
) {
    Unknown(id = "", key = ""),
    Buffing(id = "buffing", key = "smooth"),
    Sharpen(id = "sharpen", key = "sharp"),
    Toning(id = "toning", key = "whiten");

    val isUnknown: Boolean
        get() = this == Unknown

    companion object {
        fun getById(id: String): FaceFilterType {
            return values().firstOrNull { it.id == id } ?: Unknown
        }
    }
}

package com.tokopedia.imagepicker.editor.watermark.utils

const val MAX_IMAGE_SIZE = 2000

fun String.takeAndEllipsizeOf(size: Int = 25): String {
    return if (this.length >= size) "${this.take(size)}…" else this
}
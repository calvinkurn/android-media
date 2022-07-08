package com.tokopedia.media.editor.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getEditorSaveFolderDir(context: Context): String {
    return "${context.externalCacheDir?.path}/editor-cache/"
}

fun getDestinationUri(context: Context): Uri {
    val folderPath = getEditorSaveFolderDir(context)
    val dir = File(folderPath)
    if(!dir.exists()) dir.mkdir()

    return Uri.fromFile(File("${folderPath}/${generateFileName()}.png"))
}

@SuppressLint("SimpleDateFormat")
fun generateFileName(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
}
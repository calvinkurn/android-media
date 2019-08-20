package com.tokopedia.kotlin.extensions.media

import android.content.Context
import android.net.Uri
import android.support.v4.content.FileProvider
import java.io.File

/**
 * Created by jegul on 2019-08-20.
 */
fun File.toUri(context: Context): Uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", this)
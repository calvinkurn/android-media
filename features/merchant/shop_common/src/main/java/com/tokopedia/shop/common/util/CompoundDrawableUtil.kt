package com.tokopedia.shop.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

fun convertUrlToBitmapAndLoadImage(
        context: Context,
        url: String?,
        convertIntoSize: Int,
        loadImage: (resource: Bitmap) -> Unit
) {
    url?.getBitmapImageUrl(context, {
        overrideSize(Resize(convertIntoSize, convertIntoSize))
    }) {
        loadImage(it)
    }
}

fun UnifyButton.loadLeftDrawable(context: Context, url: String?, convertIntoSize: Int) {
    convertUrlToBitmapAndLoadImage(
            context,
            url,
            convertIntoSize
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
                BitmapDrawable(resources, it),
                null,
                null,
                null
        )
    }
}

fun Typography.loadLeftDrawable(context: Context, url: String?, convertIntoSize: Int) {
    convertUrlToBitmapAndLoadImage(
            context,
            url,
            convertIntoSize
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
                BitmapDrawable(resources, it),
                null,
                null,
                null
        )
    }
}

fun UnifyButton.removeDrawable() {
    setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            null,
            null
    )
}

fun Typography.removeDrawable() {
    setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            null,
            null
    )
}

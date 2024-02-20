package com.tokopedia.sdui.extention

import android.content.Context
import android.graphics.Typeface
import com.tokopedia.unifyprinciples.getTypeface
import com.yandex.div.core.font.DivTypefaceProvider

class TypeFaceProvider constructor(
    private val context: Context
) : DivTypefaceProvider {
    override fun getRegular(): Typeface? {
        return getTypeface(context, "OpenSauceOneRegular.ttf")
    }

    override fun getMedium(): Typeface? {
        return getTypeface(context, "OpenSauceOneRegular.ttf")
    }

    override fun getLight(): Typeface? {
        return getTypeface(context, "OpenSauceOneRegular.ttf")
    }

    override fun getBold(): Typeface? {
        return getTypeface(context, "OpenSauceOneExtraBold.ttf")
    }
}

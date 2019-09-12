package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.media.toTempFile
import com.tokopedia.kotlin.extensions.view.toSquareBitmap

/**
 * Created by jegul on 2019-09-12.
 */
class ByMeInstastoryView : BaseCustomView {

    companion object {
        private const val IG_TEMP = "ig_temp"
    }

    private val tvUserName by lazy { findViewById<TextView>(R.id.tv_user_name) }
    val ivAvatar by lazy { findViewById<ImageView>(R.id.iv_avatar) }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.item_share_ig_by_me, this)
    }

    fun setUserName(userName: String) {
        tvUserName.text = userName
    }

    fun setAvatarDrawable(image: Drawable) {
        ivAvatar.setImageDrawable(image)
    }

    fun getAsBitmap(): Bitmap = toSquareBitmap()

    fun getTempFileUri(): String = MethodChecker.getUri(context, getAsBitmap().toTempFile(context, IG_TEMP)).toString()
}
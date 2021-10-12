package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R

class ToggleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var isChecked = false

    @DrawableRes
    var onDrawableId: Int = R.drawable.imagepicker_insta_ic_check_circle

    @DrawableRes
    var offDrawableId: Int? = null
    var toggleCallback:Function1<Boolean,Unit>?=null

    fun toggle(isChecked: Boolean) {
        this.isChecked = isChecked
        toggleCallback?.invoke(isChecked)

        if (isChecked) {
            setImageResource(onDrawableId)
        } else {
            if (offDrawableId != null) {
                setImageResource(offDrawableId!!)
            } else {
                setImageDrawable(null)
            }
        }
    }

    fun toggle() {
        toggle(!isChecked)
    }
}
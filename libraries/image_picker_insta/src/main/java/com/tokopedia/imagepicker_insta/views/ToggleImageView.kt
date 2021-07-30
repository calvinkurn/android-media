package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R

class ToggleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    @DrawableRes
    var mask:Int = R.drawable.imagepicker_insta_ic_check_circle

    fun toggle(isChecked:Boolean){
        if(isChecked){
            setImageResource(mask)
        }else{
            setImageDrawable(null)
        }
    }
}
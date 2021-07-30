package com.tokopedia.imagepicker_insta.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.imagepicker_insta.R

class ToggleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun toggle(isChecked:Boolean){
        if(isChecked){
            setImageResource(R.drawable.imagepicker_insta_ic_check_circle)
        }else{
            setImageDrawable(null)
        }
    }
}
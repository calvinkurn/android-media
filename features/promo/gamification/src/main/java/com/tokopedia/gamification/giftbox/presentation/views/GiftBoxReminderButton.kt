package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.Constants
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify


class GiftBoxReminderButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val LAYOUT_ID = R.layout.gami_giftbox_reminder_button
    val loaderReminder: LoaderUnify
    val imageBell: ImageUnify

    private val bellImageSource : String
    get() {
        val isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false
        return if(isTablet){
            Constants.BELL_IMAGE_4X
        }
        else Constants.BELL_IMAGE_3X
    }

    private val bellFilledImageSource : String
        get() {
            val isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false
            return if(isTablet){
               Constants.FILLED_BELL_IMAGE_4X
            }
            else Constants.FILLED_BELL_IMAGE_3X
        }


    init {
        View.inflate(context, LAYOUT_ID, this)
        loaderReminder = findViewById(R.id.loaderReminder1)
        imageBell = findViewById(R.id.imageBell)

        val paddingTop = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_green_gradient_btn_top_padding)?.toInt() ?: 0
        val paddingSide = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_green_gradient_btn_side_padding)?.toInt() ?: 0
        val isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false

        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.apply {
            setPadding(paddingSide, paddingTop, paddingSide, paddingTop)
        }
        layoutParams = lp
        background = ContextCompat.getDrawable(context, R.drawable.gami_bg_reminder)

        imageBell.layoutParams.apply {
            if (isTablet) {
                width = dpToPx(28).toInt()
                height = dpToPx(28).toInt()
            }
        }
    }

    fun performLoading() {
        loaderReminder.visibility = View.VISIBLE
        imageBell.visibility = View.INVISIBLE
    }

    fun stopLoading() {
        loaderReminder.visibility = View.GONE
        imageBell.visibility = View.VISIBLE
    }

    fun setIcon(isReminderSet: Boolean) {
        val imageSource = if(isReminderSet) bellFilledImageSource else bellImageSource
        Glide.with(context)
            .asDrawable()
            .load(imageSource)
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageBell.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}

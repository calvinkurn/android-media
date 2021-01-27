package com.tokopedia.homenav.common.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.loadImage

fun TextView.animateProfileName(text: String, duration: Long = 300, completion: (() -> Unit)? = null) {
    slideUpFromMiddle(duration) {
        this.text = text
        slideUpFromBottom(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun ImageView.animateProfileBadge(imageUrl: String, duration: Long = 300, completion: (() -> Unit)? = null) {
    shrinkToCenter(duration) {
        loadImage(imageUrl)
        expandFromCenter(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.shrinkToCenter(duration: Long = 300, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate()
            .scaleX(0.3f)
            .scaleY(0.3f)
            .setDuration(duration)
            .withEndAction {
                this.visibility = visibility
                completion?.let {
                    it()
                }
            }
}

fun View.expandFromCenter(duration: Long = 300, completion: (() -> Unit)? = null) {
    visibility = View.VISIBLE
    animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(duration)
            .withEndAction {
                completion?.let {
                    it()
                }
            }
}

fun View.slideUpFromMiddle(duration: Long = 300, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate()
            .alpha(0f)
            .translationY(-height.toFloat()/3)
            .setDuration(duration)
            .withEndAction {
                this.visibility = visibility
                completion?.let {
                    it()
                }
            }

}

fun View.slideUpFromBottom(duration: Long = 300, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    y = height.toFloat()/3
    animate()
            .alpha(1f)
            .setDuration(duration)
            .translationY(0f)
            .withEndAction {
                completion?.let {
                    it()
                }
            }
}
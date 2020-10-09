package com.tokopedia.home_recom.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.unifycomponents.Toaster


fun View.fadeHide(duration: Int = 50){
    animate().alpha(0f).setDuration(duration.toLong()).setListener(object: AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            // reset alpha to 1
            visibility = View.GONE
        }
    })
}
fun View.fadeShow(duration: Int = 200){
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration.toLong()).setListener(null)
}

fun View.startFade(isShow: Boolean){
    if(isShow) this.fadeShow()
    else this.fadeHide()
}

fun FragmentActivity.showToastSuccess(message: String){
    Toaster.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
}

fun FragmentActivity.showToastError(throwable: Throwable?){
    Toaster.make(
            findViewById(android.R.id.content),
            RecommendationPageErrorHandler.getErrorMessage(this, throwable),
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_ERROR
    )
}

fun FragmentActivity.showToastSuccessWithAction(message: String, actionString: String, action: () -> Unit){
    Toaster.make(
            findViewById(android.R.id.content),
            message,
            Toaster.LENGTH_LONG,
            Toaster.TYPE_NORMAL,
            actionString,
            View.OnClickListener {
                action.invoke()
            }
    )
}
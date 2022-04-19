package com.tokopedia.sellerorder.list.presentation.animator

import android.animation.AnimatorInflater
import android.content.Context
import android.view.animation.BounceInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R

class SomFadeRightAnimator(private val context: Context): DefaultItemAnimator() {

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        AnimatorInflater.loadAnimator(context, R.animator.animator_fade_right).run {
            interpolator = BounceInterpolator()
            setTarget(holder?.itemView)
            start()
        }
        return true
    }

}
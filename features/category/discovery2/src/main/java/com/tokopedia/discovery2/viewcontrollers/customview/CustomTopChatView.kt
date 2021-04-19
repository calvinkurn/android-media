package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography


class CustomTopChatView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var topChatBackground: CardView
    private var topChatTextView: Typography
    private var topChatFabButton: ImageUnify
    private var topChatFabButtonParent: CardView

    init {
        val view = View.inflate(context, R.layout.widget_custom_top_chat, this)
        topChatBackground = view.findViewById(R.id.textCardView)
        topChatTextView = view.findViewById(R.id.topChatText)
        topChatFabButton = view.findViewById(R.id.fabButton)
        topChatFabButtonParent = view.findViewById(R.id.fabButtonParent)
    }

    fun slideUp(view: View) {
        view.animate().translationY(0F).setInterpolator(LinearInterpolator()).start()
    }

    fun slideDown(view: View) {
        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        val fabBottomMargin = layoutParams.bottomMargin
        view.animate().translationY((view.height + fabBottomMargin).toFloat()).setInterpolator(LinearInterpolator()).start()
    }

    private fun inFromRightAnimation(): Animation? {
        val inFromRight: Animation = ScaleAnimation(
                0.0f, 1.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f)
        inFromRight.duration = 1000
        inFromRight.interpolator = DecelerateInterpolator()
        return inFromRight
    }

    private fun outToRightAnimation(): Animation? {
        val outToRight: Animation = ScaleAnimation(
                1.0f, 0.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f)
        outToRight.duration = 1000
        outToRight.fillAfter = true
        outToRight.interpolator = DecelerateInterpolator()
        return outToRight
    }

    private fun slideLeft(view: View) {
        view.startAnimation(inFromRightAnimation())
    }

    private fun slideRight(view: View) {
        view.startAnimation(outToRightAnimation())
    }

    fun getScrollListener(): CustomTopChatOnScrollListener {
        return CustomTopChatOnScrollListener(this)
    }

    fun showImageOnFab(context: Context, thumbnailUrlMobile: String) {
        topChatFabButton.loadImageCircle(thumbnailUrlMobile)
        topChatFabButtonParent.show()
    }

    fun showTextAnimation(data: DataItem) {
        setTextBackgroundIfAny(data.boxColor ?: "")
        setTextColorIfAny(data.fontColor ?: "")
        setTextIfAny(data.name ?: "")
        handleAnimations()
    }

    private fun handleAnimations() {
        topChatBackground.show()
        slideLeft(topChatBackground)
        Handler().postDelayed({
            slideRight(topChatBackground)
        }, 3000)
    }

    private fun setTextIfAny(name: String) {
        topChatTextView.text = name
    }

    private fun setTextColorIfAny(fontColor: String) {
        fontColor.let {
            if (it.length > 1) {
                topChatTextView.setTextColor((Color.parseColor(it)))
            }
        }
    }

    private fun setTextBackgroundIfAny(boxColor: String) {
        boxColor.let {
            if (it.length > 1) {
                topChatBackground.setCardBackgroundColor(Color.parseColor(it))
            }
        }
    }

    fun getFabButton(): ImageUnify {
        return topChatFabButton
    }

    inner class CustomTopChatOnScrollListener(val view: View) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy < 0) {
                slideUp(view)
            } else if (dy > 0) {
                slideDown(view)
            }
        }
    }
}
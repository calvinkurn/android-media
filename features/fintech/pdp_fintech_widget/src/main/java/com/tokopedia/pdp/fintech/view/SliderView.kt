package com.tokopedia.pdp.fintech.view

import android.animation.Animator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SliderView: ScrollView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var binding: SliderViewLayoutBinding? = null

    private val handler = Handler()

    private var atEnd: Boolean = false

    init {
        binding = SliderViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setItems(firstItem: View, secondItem: View) {

    }

    fun setItems(views: List<View>) {
        val keyframeList = mutableListOf<Keyframe>()

        views.forEachIndexed { index, view ->
            binding?.container?.addView(view)
            view.post {
                keyframeList.add(Keyframe.ofInt((2000f * index + 1) / (views.size * 2000f), index * view.height))

                if (index == views.size - 1) {
                    layoutParams.height = view.height
                    requestLayout()

                    val pvh = PropertyValuesHolder.ofKeyframe("scrollTo", *keyframeList.toTypedArray())
                    val anim = ObjectAnimator.ofPropertyValuesHolder(this@SliderView, pvh)
                    anim.duration = views.size * 2000L
                    anim.repeatMode = ValueAnimator.REVERSE
                    anim.repeatCount = ValueAnimator.INFINITE
                    anim.start()

//                    GlobalScope.launch(Dispatchers.Main) {
////                        val va = ValueAnimator.ofInt(0, secondItem.height)
////                        va.duration = 3000
////                        va.addUpdateListener { animation -> this@SliderView.scrollTo(0, animation.animatedValue as Int) }
////                        va.repeatCount = ValueAnimator.INFINITE
////                        va.repeatMode = ValueAnimator.REVERSE
////                        va.start()
//                    }
                }
            }
        }



//        GlobalScope.launch(Dispatchers.Main) {
//
//        }
    }
}

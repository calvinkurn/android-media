package com.tokopedia.mvcwidget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.ceil

class MvcTokomemberBmView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var carousel: CarouselUnify
    lateinit var tvMessage: Typography
    lateinit var button: UnifyButton


    init {
        View.inflate(context, R.layout.mvc_tokomember_bm, this)
        carousel = findViewById(R.id.carousel)
        tvMessage = findViewById(R.id.tvMessage)
        button = findViewById(R.id.btn)
    }

    fun setData(viewData: MvcTokomemberBmViewData) {
//        tvMessage.doOnNextLayout {textView->




    }

    fun setData2(viewData: MvcTokomemberBmViewData) {
        button.text = viewData.buttonText

        val bounds = Rect()
        val paint = Paint()
        paint.textSize = (tvMessage as Typography).textSize
        var maxLines = 0
        viewData.messages.forEach { text ->
            paint.getTextBounds(text, 0, text.length, bounds)
            val numLines = ceil(bounds.width().toFloat() / (tvMessage as Typography).width.toDouble()).toInt()
            if (numLines > maxLines) {
                maxLines = numLines
            }
        }
        tvMessage.setLines(maxLines)
        tvMessage.minLines = (maxLines)
        tvMessage.maxLines = (maxLines)
        tvMessage.text = viewData.messages[0]

        carousel.apply {
            activeIndex = 0
            autoplay = false
            bannerItemMargin = 20
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                // you can set margin right here as view params
//                setMargins(20, 20, 20, 20)
            }
            val item1 = ImageUnify(context).apply {
                layoutParams = param
                setBackgroundColor(Color.CYAN)
            }
            val item2 = ImageUnify(context).apply {
                layoutParams = param
                setBackgroundColor(Color.CYAN)
            }
            addItem(item1)
            addItem(item2)

//            addItems(R.layout.mvc_carousel_item, viewData.imageUrls as ArrayList<Any>) { view, data ->
//                val url = data as String
//                val image: ImageUnify = view.findViewById(R.id.image)
////                image.setImageUrl(url)
//                image.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
//            }

            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    tvMessage.text = viewData.messages[current]
                }
            }
//            }
        }
    }
}

fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(left, top, right, bottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = left
        layoutParams.marginEnd = right
    }
}

inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
        action(this)
    } else {
        doOnNextLayout {
            action(it)
        }
    }
}

inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
        ) {
            view.removeOnLayoutChangeListener(this)
            action(view)
        }
    })
}

data class MvcTokomemberBmViewData(val imageUrls: List<String>, val messages: List<String>, val buttonText: String)
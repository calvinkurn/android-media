package com.tokopedia.createpost.view.posttag;

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.viewmodel.MediaType
import com.tokopedia.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.round
import android.view.ViewConfiguration
import com.tokopedia.imagepicker_insta.toPx


class TagViewProvider {
    var dX = 0f
    var dY = 0f
    var listener: CreateContentPostCOmmonLIstener? = null

    companion object {
        private const val POINTER_HALF_DIMEN = 8
    }


    fun getTagView(
        context: Context?,
        products: List<RelatedProductItem>,
        index: Int,
        listener: CreateContentPostCOmmonLIstener?,
        feedXTag: FeedXMediaTagging,
    ): View? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.temp_view_tag, null)
        val tv = view.findViewById<View>(R.id.product_tag_slashed_price_text) as Typography
        val productViewPrice = view.findViewById<View>(R.id.product_tag_price_text) as Typography
        val productName = view.findViewById<View>(R.id.product_tag_name_text) as Typography
        var productTagViewDelete: IconUnify = view.findViewById(R.id.product_tag_clear)
        var productTagViewDeleteRight: IconUnify = view.findViewById(R.id.product_tag_clear_right)

        val productItem = products[index]
        this.listener = listener
        productName.text = productItem.name
        productViewPrice.text = productItem.price
        if (productItem.isDiscount) {
            tv.text = productItem.priceOriginalFmt
            tv.visibility = View.VISIBLE
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tv.visibility = View.GONE
        }
        productTagViewDelete.setOnClickListener {
            listener?.deleteItemFromProductTagList(
                feedXTag.tagIndex,
                productItem.id,
                true,
                MediaType.IMAGE
            )
            view.gone()
        }
        productTagViewDeleteRight.setOnClickListener {
            listener?.deleteItemFromProductTagList(
                feedXTag.tagIndex,
                productItem.id,
                true,
                MediaType.IMAGE
            )
            view.gone()
        }

        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addViewToParent(
        child: View,
        parent: ConstraintLayout,
        feedXMediaTagging: FeedXMediaTagging,
        index: Int,
        bitmap: Bitmap
    ) {
        val greyAreaX = calculateGreyAreaY(parent, bitmap)
        val greyAreaY = calculateGreyAreaX(parent, bitmap)

        child.visibility = View.INVISIBLE
        parent.addView(child)
        var productTagViewDelete: IconUnify = child.findViewById(R.id.product_tag_clear)
        var productTagViewDeleteRight: IconUnify = child.findViewById(R.id.product_tag_clear_right)
        var productTagViewDeleteFinal: IconUnify = child.findViewById(R.id.product_tag_clear)
        var isDrag = false
        var isLongPress = false

        val handler = Handler()
        val mLongPressed: Runnable = Runnable {
            scaleUp(child)
            isLongPress = true
            child.postDelayed({
                scaleDown(child)
            }, 1000)
        }

        child.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isLongPress = false
                    handler.postDelayed(mLongPressed,
                        ViewConfiguration.getLongPressTimeout().toLong())
                    isDrag = false
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {

                    isLongPress = false
                    handler.removeCallbacks(
                        mLongPressed)
                    isDrag = true
                    val gotoX: Float = when {
                        (motionEvent.rawX.plus(dX)
                            ?: 0f) + view.width > (parent.right - greyAreaX) -> {
                            parent.right - greyAreaX - view.width.toFloat() /*Blocking right on drag*/
                        }
                        motionEvent.rawX.plus(dX) ?: 0f < greyAreaX -> {
                            greyAreaX.toFloat() /*Blocking left on drag*/
                        }
                        else -> {
                            motionEvent.rawX.plus(dX) /*Normal horizontal drag*/
                        }
                    }

                    val gotoY: Float = when {
                        motionEvent.rawY.plus(dY) < greyAreaY -> {
                            greyAreaY.toFloat() /*Blocking top on drag*/
                        }
                        (motionEvent.rawY?.plus(dY)
                            ?: 0f) + view.height > (parent.bottom - greyAreaY) -> {
                            parent.bottom - greyAreaY - view.height.toFloat() /*Blocking bottom on drag*/
                        }
                        else -> {
                            motionEvent.rawY.plus(dY)  /*Normal vertical drag*/
                        }
                    }

                    view.animate()
                        .x(gotoX)
                        .y(gotoY)
                        .setDuration(0)
                        .start()

                    val bitmapCurrentHeight = (parent.height - (2 * greyAreaY))
                    if (view.y < (bitmapCurrentHeight + greyAreaY) * 0.70) {
                        view.findViewById<View>(R.id.topNotch).visibility = View.VISIBLE
                        view.findViewById<View>(R.id.bottomNotch).visibility = View.GONE
                    } else {
                        view.findViewById<View>(R.id.topNotch).visibility = View.GONE
                        view.findViewById<View>(R.id.bottomNotch).visibility = View.VISIBLE
                    }

                    return@OnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacks(
                        mLongPressed)

                    var isRightClearIconVisible = false
                    if (!isDrag) {
                        if (!productTagViewDeleteFinal.isVisible) {
                            productTagViewDeleteFinal =
                                if (((view.parent as ConstraintLayout).right) - (view.x + view.width + greyAreaX) > 88) {
                                    isRightClearIconVisible = false
                                    productTagViewDelete
                                } else {
                                    isRightClearIconVisible = true
                                    productTagViewDeleteRight
                                }
                        }

                        if (productTagViewDeleteFinal.isVisible) {
                            productTagViewDeleteFinal.gone()
                        } else if (!isLongPress) {
                            val endSpace =
                                ((view.parent as ConstraintLayout).right) - (view.x + view.width + greyAreaX)
                            val endSpaceToBeAdded = 88 - endSpace
                            if (isRightClearIconVisible) {
                                view.x = view.x - endSpaceToBeAdded
                            }
                            productTagViewDeleteFinal.visible()
                        }
                    }

                    val location = IntArray(2)
                    val locationParent = IntArray(2)
                    val view = view.findViewById<View>(R.id.topNotch)
                    view.getLocationOnScreen(location)
                    parent.getLocationOnScreen(locationParent)
                    feedXMediaTagging.run {
                        X = (location[0].toFloat() ?: 0f) + POINTER_HALF_DIMEN.toPx()
                        Y = (location[1] - locationParent[1]).toFloat() ?: 0f
                        posX = round((feedXMediaTagging.X!! / parent.width) * 10) / 10
                        posY = round((feedXMediaTagging.Y!! / parent.height) * 10) / 10
                    }
                    listener?.updateTaggingInfoInViewModel(feedXMediaTagging, index)
                }
            }

            false
        })

        Handler().postDelayed(Runnable {
            /*Handling for X position*/
            var xTapped: Float =
                feedXMediaTagging.X?.minus(child.width / 2) ?: 0f
            val x2Want: Float = xTapped + child.width.toFloat()
            val x2Diff = parent.right - x2Want
            if (x2Diff < 0) {
                xTapped += x2Diff
            }

            /*Handling for Y position*/
            var yTapped = feedXMediaTagging.Y!!
            val y2Want: Float = yTapped + child.height.toFloat()
            val y2Diff = parent.bottom - y2Want
            if (y2Diff < 0) {
                yTapped += y2Diff
            }

            //Handling for negative X axis
            if (xTapped < 0) {
                child.x = 0f
            } else {
                child.x = xTapped
            }

            child.y = yTapped
            child.visibility = View.VISIBLE

            if (child.y < parent.height * 0.75) {
                child.findViewById<View>(R.id.topNotch).visibility = View.VISIBLE
                child.findViewById<View>(R.id.bottomNotch).visibility = View.GONE
            } else {
                child.findViewById<View>(R.id.topNotch).visibility = View.GONE
                child.findViewById<View>(R.id.bottomNotch).visibility = View.VISIBLE
            }

        }, 50)

        listener?.updateTaggingInfoInViewModel(feedXMediaTagging, index)

    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun scaleUp(view: View) {
        val scaleDownX2 = ObjectAnimator.ofFloat(
            view, "scaleX", 1.05f)
        val scaleDownY2 = ObjectAnimator.ofFloat(
            view, "scaleY", 1.05f)
        scaleDownX2.duration = 1000
        scaleDownY2.duration = 1000

        val scaleDown2 = AnimatorSet()
        scaleDown2.play(scaleDownX2).with(scaleDownY2)

        scaleDown2.start()

    }

    private fun scaleDown(view: View) {
        val scaleDownX = ObjectAnimator.ofFloat(view,
            ConstraintLayout.SCALE_X, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(view,
            ConstraintLayout.SCALE_Y, 1f)
        scaleDownX.duration = 1000
        scaleDownY.duration = 1000

        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        scaleDown.start()
    }

    private fun calculateGreyAreaDistance(parent: ConstraintLayout, bitmap: Bitmap) {
        when {
            bitmap.width == bitmap.height -> {
                //do nothing
            }
            bitmap.width > bitmap.height -> {
                val newBitmapHeight = (parent.height * bitmap.height) / bitmap.width
                val greyAreaY = (parent.height - newBitmapHeight) / 2
            }
            bitmap.height > bitmap.width -> {
                val newBitmapWidth = (parent.width * bitmap.width) / bitmap.height
                val greyAreaX = (parent.width - newBitmapWidth) / 2
            }
        }

    }
    private fun calculateGreyAreaX(parent: ConstraintLayout, bitmap: Bitmap): Int {
        return if (bitmap.width > bitmap.height) {
            val newBitmapHeight = (parent.height * bitmap.height) / bitmap.width
            (parent.height - newBitmapHeight) / 2
        } else
            0
    }

    private fun calculateGreyAreaY(parent: ConstraintLayout, bitmap: Bitmap): Int {
        return if (bitmap.height > bitmap.width) {
            val newBitmapHeight = (parent.width * bitmap.width) / bitmap.height
            (parent.width - newBitmapHeight) / 2
        } else
            0
    }
}

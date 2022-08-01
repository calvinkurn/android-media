package com.tokopedia.createpost.view.posttag;

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Handler
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.createpost.common.view.viewmodel.MediaType
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.abs
import kotlin.math.ceil


class TagViewProvider {
    var dX = 0f
    var dY = 0f
    var listener: CreateContentPostCommonListener? = null

    fun getTagView(
        context: Context?,
        products: List<RelatedProductItem>,
        index: Int,
        listener: CreateContentPostCommonListener?,
        feedXTag: FeedXMediaTagging,
        parent: ConstraintLayout
    ): View? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.temp_view_tag, null)
        val tv = view.findViewById<View>(R.id.product_tag_slashed_price_text) as Typography
        val productViewPrice = view.findViewById<View>(R.id.product_tag_price_text) as Typography
        val productName = view.findViewById<View>(R.id.product_tag_name_text) as Typography
        var productTagViewDelete: IconUnify = view.findViewById(R.id.product_tag_clear)
        var productTagViewDeleteRight: IconUnify = view.findViewById(R.id.product_tag_clear_right)

        val productItem = if (products.size > index) products[index] else return null
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
            parent.removeView(view)
        }
        productTagViewDeleteRight.setOnClickListener {
            listener?.deleteItemFromProductTagList(
                feedXTag.tagIndex,
                productItem.id,
                true,
                MediaType.IMAGE
            )
            parent.removeView(view)
        }
        view.tag = productItem.id

        return view
    }

    @Suppress("MagicNumber")
    @SuppressLint("ClickableViewAccessibility")
    fun addViewToParent(
        child: View,
        parent: ConstraintLayout,
        feedXMediaTagging: FeedXMediaTagging,
        bitmap: Bitmap?,
    ) {
        parent.addView(child)
        val greyAreaX = calculateGreyAreaY(parent, bitmap)
        val greyAreaY = calculateGreyAreaX(parent, bitmap)

        child.visibility = View.INVISIBLE
        var productTagViewDelete: IconUnify = child.findViewById(R.id.product_tag_clear)
        var productTagViewDeleteRight: IconUnify = child.findViewById(R.id.product_tag_clear_right)
        var productTagViewDeleteFinal: IconUnify = productTagViewDelete
        var productTagViewTopNotch: View = child.findViewById<View>(R.id.topNotch)
        var productTagViewBottomNotch: View = child.findViewById<View>(R.id.bottomNotch)
        var productTagNotchViewFinal: View = productTagViewTopNotch
        var productTopNotchVisible = true
        var isDrag = false
        var isLongPress = false

        val handler = Handler()
        val mLongPressed: Runnable = Runnable {
            if (productTagViewDeleteFinal.isVisible)
                productTagViewDeleteFinal.gone()
            scaleUp(child)
            isLongPress = true
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

                    isDrag = true
                    resetPositionOfNotchToCenter(productTagNotchViewFinal, view.width.toFloat())
                    val diff = view.x - motionEvent.rawX.plus(dX)

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
                        else ->
                            motionEvent.rawY.plus(dY)  /*Normal vertical drag*/
                        }

                    if (gotoX - view.x > 10 && gotoY - view.y > 10) {
                        isLongPress = false
                        handler.removeCallbacks(
                            mLongPressed)
                    }

                    view.animate()
                        .x(gotoX)
                        .y(gotoY)
                        .setDuration(0)
                        .start()
                    //corner cases when pointer needs to slide
                    if (view.x <= greyAreaX) {
                        var pointerXDiff = productTagNotchViewFinal.x.minus(diff)
                        if (pointerXDiff < 20)
                            pointerXDiff = 20f
                        productTagNotchViewFinal.animate()
                            .x(pointerXDiff)
                            .y(productTagNotchViewFinal.y)
                            .setDuration(0)
                            .start()
                    } else if (view.x + view.width >= (parent.right - greyAreaX)) {
                        var pointerXDiff = productTagNotchViewFinal.x.minus(diff)
                        if (pointerXDiff > (view.width - productTagNotchViewFinal.width - 20))
                            pointerXDiff =
                                (view.width - productTagNotchViewFinal.width - 20).toFloat()
                        productTagNotchViewFinal.animate()
                            .x(pointerXDiff)
                            .y(productTagNotchViewFinal.y)
                            .setDuration(0)
                            .start()
                    }

                    val location = IntArray(2)
                    val locationParent = IntArray(2)
                    val bitmapCurrentHeight = (parent.height - (2 * greyAreaY))

                    productTagNotchViewFinal.getLocationOnScreen(location)
                    parent.getLocationOnScreen(locationParent)
                    val pointerPositionY = (location[1] - locationParent[1]).toFloat()
                    if (pointerPositionY  < (bitmapCurrentHeight) * 0.70 + greyAreaY) {
                        productTopNotchVisible = true
                        productTagViewTopNotch.visibility = View.VISIBLE
                        productTagViewBottomNotch.visibility = View.INVISIBLE
                        productTagNotchViewFinal = productTagViewTopNotch

                    } else {
                        productTopNotchVisible = false
                        productTagViewBottomNotch.visibility = View.VISIBLE
                        productTagViewTopNotch.visibility = View.INVISIBLE
                        productTagNotchViewFinal = productTagViewBottomNotch
                    }

                    return@OnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    if (isLongPress) {
                        scaleDown(child)
                    }
                    handler.removeCallbacks(
                        mLongPressed)

                    var isRightClearIconVisible = false
                    //clear tag icon visibility logic
                    if (!isDrag) {
                        if (productTagViewDeleteFinal.isVisible) {
                            productTagViewDeleteFinal.gone()
                        } else if (!isLongPress) {
                            productTagViewDeleteFinal =
                                if (((view.parent as ConstraintLayout).right) - (view.x + view.width + greyAreaX) > 88) {
                                    isRightClearIconVisible = false
                                    productTagViewDelete
                                } else {
                                    isRightClearIconVisible = true
                                    productTagViewDeleteRight
                                }

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
                    productTagNotchViewFinal.getLocationOnScreen(location)
                    parent.getLocationOnScreen(locationParent)
                    feedXMediaTagging.run {
                        X = ((location[0].toFloat() ?: 0f) + productTagNotchViewFinal.width / 2)
                        Y = if (productTopNotchVisible)
                            (location[1] - locationParent[1]).toFloat() ?: 0f
                        else
                            ((location[1] - locationParent[1]).toFloat() + productTagNotchViewFinal.height)
                                ?: 0f
                        posX = ceil((abs(feedXMediaTagging.X!!) / parent.width) * 1000) / 1000
                        posY = ceil((abs(feedXMediaTagging.Y!!) / parent.height) * 1000) / 1000
                        pointerPosition = productTagNotchViewFinal.x
                    }
                    listener?.updateTaggingInfoInViewModel(feedXMediaTagging)
                }
            }

            false
        })

        Handler().postDelayed(Runnable {
            val bitmapCurrentHeight = (parent.height - (2 * greyAreaY))
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
            if (feedXMediaTagging.Y!! < (bitmapCurrentHeight) * 0.70 + greyAreaY) {
                val y2Want: Float = yTapped + (child.height.toFloat() - productTagNotchViewFinal.height)
                val y2Diff = parent.bottom - y2Want
                if (y2Diff < 0) {
                    yTapped += y2Diff
                }
            } else {
                val y2Want: Float =
                    yTapped - (child.height.toFloat() - productTagNotchViewFinal.height)
                if (y2Want > greyAreaY)
                    yTapped = y2Want
            }

            //Handling for negative X axis
            when {
                xTapped < greyAreaX ->
                     child.x = greyAreaX.toFloat()
                xTapped + child.width > (parent.right - greyAreaX) ->
                    child.x = (parent.width - greyAreaX - child.width).toFloat()
                else -> child.x = xTapped

            }
            when {
                yTapped < greyAreaY -> child.y = greyAreaY.toFloat()
                yTapped + child.height > parent.bottom - greyAreaY ->
                    child.y = (parent.bottom - greyAreaY - child.height).toFloat()
                else -> child.y = yTapped
            }

            if (feedXMediaTagging.Y!! < (bitmapCurrentHeight) * 0.70 + greyAreaY) {
                productTopNotchVisible = true
                productTagViewBottomNotch.visibility = View.GONE
                productTagNotchViewFinal = productTagViewTopNotch

            } else {
                productTopNotchVisible = false
                productTagViewTopNotch.visibility = View.GONE
                productTagNotchViewFinal = productTagViewBottomNotch
            }
            //reposition the pointer on the view
            if (feedXMediaTagging.pointerPosition != -1f) {
                productTagNotchViewFinal.x = feedXMediaTagging.pointerPosition!!
            } else {
                productTagNotchViewFinal.x = ((child.width / 2) - (productTagNotchViewFinal.width / 2)).toFloat()
            }
            productTagNotchViewFinal.visible()
            child.visibility = View.VISIBLE

        }, 30)


        listener?.updateTaggingInfoInViewModel(feedXMediaTagging)

    }

    @Suppress("MagicNumber")
    private fun resetPositionOfNotchToCenter(view: View, parentWidth: Float) {
        view.x = parentWidth/2 - view.width/2
    }

    @Suppress("MagicNumber")
    private fun scaleUp(view: View) {
        val scaleDownX2 = ObjectAnimator.ofFloat(
            view, ConstraintLayout.SCALE_X, 1.05f)
        val scaleDownY2 = ObjectAnimator.ofFloat(
            view, ConstraintLayout.SCALE_Y, 1.05f)
        scaleDownX2.duration = 150
        scaleDownY2.duration = 150

        val scaleDown2 = AnimatorSet()
        scaleDown2.play(scaleDownX2).with(scaleDownY2)

        scaleDown2.start()

    }

    @Suppress("MagicNumber")
    private fun scaleDown(view: View) {
        val scaleDownX = ObjectAnimator.ofFloat(view,
            ConstraintLayout.SCALE_X, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(view,
            ConstraintLayout.SCALE_Y, 1f)
        scaleDownX.duration = 150
        scaleDownY.duration = 150

        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)
        scaleDown.start()
    }

    @Suppress("MagicNumber")
    private fun calculateGreyAreaX(parent: ConstraintLayout, bitmap: Bitmap?): Int {
        bitmap?.let {
            return if (bitmap.width > bitmap.height) {
                val newBitmapHeight = (parent.height * bitmap.height) / bitmap.width
                (parent.height - newBitmapHeight) / 2
            } else
                0
        }
        return 0
    }
    @Suppress("MagicNumber")
    private fun calculateGreyAreaY(parent: ConstraintLayout, bitmap: Bitmap?): Int {
        bitmap?.let {
            return if (bitmap.height > bitmap.width) {
                val newBitmapHeight = (parent.width * bitmap.width) / bitmap.height
                (parent.width - newBitmapHeight) / 2
            } else
                0
        }
        return 0
    }
}

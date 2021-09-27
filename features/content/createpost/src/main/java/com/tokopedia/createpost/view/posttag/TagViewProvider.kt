package com.tokopedia.createpost.view.posttag;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography

class TagViewProvider {
    var dX = 0f
    var dY = 0f
    var listener: CreateContentPostCOmmonLIstener? = null



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

        val (id, name, price, _, _, _, priceDiscountFmt, isDiscount) = products[index]
        this.listener = listener
        productName.text = name
        if (isDiscount) {
            tv.visibility = View.VISIBLE
            productViewPrice.text = priceDiscountFmt
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tv.visibility = View.GONE
            productViewPrice.text = price
        }
        productTagViewDelete.setOnClickListener {
            listener?.deleteItemFromProductTagList(
                feedXTag.tagIndex,
                id,
                true,
                MediaType.IMAGE
            )
            view.gone()
        }
        productTagViewDeleteRight.setOnClickListener {
            listener?.deleteItemFromProductTagList(
                feedXTag.tagIndex,
                id,
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
        index: Int

    ) {
        child.visibility = View.INVISIBLE
        parent.addView(child)
        var productTagViewDelete: IconUnify = child.findViewById(R.id.product_tag_clear)
        var productTagViewDeleteRight: IconUnify = child.findViewById(R.id.product_tag_clear_right)
        var productTagViewDeleteFinal: IconUnify = child.findViewById(R.id.product_tag_clear)
        child.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            var isDrag = false
            view.parent.requestDisallowInterceptTouchEvent(true)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDrag = false
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                    return@OnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    isDrag = true
                    val gotoX: Float = when {
                        (motionEvent.rawX.plus(dX) ?: 0f) + view.width > parent.right -> {
                            parent.right - view.width.toFloat() /*Blocking right on drag*/
                        }
                        motionEvent.rawX.plus(dX) ?: 0f < 0 -> {
                            0f /*Blocking left on drag*/
                        }
                        else -> {
                            motionEvent.rawX.plus(dX) /*Normal horizontal drag*/
                        }
                    }

                    val gotoY: Float = when {
                        motionEvent.rawY.plus(dY) < 0 -> {
                            0f /*Blocking top on drag*/
                        }
                        (motionEvent.rawY?.plus(dY) ?: 0f) + view.height > parent.bottom -> {
                            parent.bottom - view.height.toFloat() /*Blocking bottom on drag*/
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

                    if (view.y < parent.height * 0.75) {
                        view.findViewById<View>(R.id.topNotch).visibility = View.VISIBLE
                        view.findViewById<View>(R.id.bottomNotch).visibility = View.GONE
                    } else {
                        view.findViewById<View>(R.id.topNotch).visibility = View.GONE
                        view.findViewById<View>(R.id.bottomNotch).visibility = View.VISIBLE
                    }

                    return@OnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isDrag) {
                        if (!productTagViewDeleteFinal.isVisible) {
                            productTagViewDeleteFinal =
                                if (((view.parent as ConstraintLayout).right) - (view.x + view.width) > 88)
                                    productTagViewDelete
                                else
                                    productTagViewDeleteRight
                        }

                        if (productTagViewDeleteFinal.isVisible) {
                            productTagViewDeleteFinal.gone()
                        } else {
                            productTagViewDeleteFinal.visible()
                        }
                    }

                    val location = IntArray(2)
                    view.findViewById<View>(R.id.topNotch).getLocationOnScreen(location)
                    feedXMediaTagging.X =  location[0].toFloat()
                    feedXMediaTagging.Y =  location[1].toFloat()
                    listener?.updateTaggingInfoInViewModel(feedXMediaTagging, index)
                }
            }

            false
        })

        android.os.Handler().postDelayed(Runnable {
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
}

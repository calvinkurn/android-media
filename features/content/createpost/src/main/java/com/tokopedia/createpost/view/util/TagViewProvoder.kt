package com.tokopedia.createpost.view.util;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
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
import com.tokopedia.imagepicker_insta.toPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography

class TagViewProvider
  {
     var dX = 0f
     var dY = 0f
     var listener: CreateContentPostCOmmonLIstener? = null


     /*

     val tagImg = findViewById<ConstraintLayout>(R.id.tag)

         tagImg.setOnTouchListener(object : View.OnTouchListener {
             override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                 when (event?.action) {
                     MotionEvent.ACTION_DOWN -> {
                         return true
                     }

                     MotionEvent.ACTION_UP -> {
                         val vv = TagViewProvider.getTagView(this@MainActivity)
 //                        vv.x = event.x
 //                        vv.y = event.y
 //                        tagImg.addView(vv)
                         TagViewProvider.addViewToParent(vv, v as ConstraintLayout, event)
                     }

                 }

                 return v?.onTouchEvent(event) ?: true
             }
         })

      */

    /*

    val tagImg = findViewById<ConstraintLayout>(R.id.tag)

        tagImg.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        val vv = TagViewProvider.getTagView(this@MainActivity)
//                        vv.x = event.x
//                        vv.y = event.y
//                        tagImg.addView(vv)
                        TagViewProvider.addViewToParent(vv, v as ConstraintLayout, event)
                    }

                }

                return v?.onTouchEvent(event) ?: true
            }
        })

     */
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
            listener?.deleteItemFromProductTagList(feedXTag.tagIndex,
                id,
                true,
                MediaType.IMAGE)
            view.gone()
        }
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addViewToParent(
        child: View,
        parent: ConstraintLayout,
        feedXMediaTagging: FeedXMediaTagging,
    ) {
        child.visibility = View.INVISIBLE

        val location = IntArray(2)
        parent.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        Log.d("Lavekush", "X=$x ,Y=$y")

        var productTagViewDelete: IconUnify = child.findViewById(R.id.product_tag_clear)
        child.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!productTagViewDelete.isVisible)
                        productTagViewDelete.visible()
                    else
                        productTagViewDelete.gone()
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    val gotoX: Float = when {
                        (motionEvent.rawX?.plus(dX) ?: 0f) + pxFromDp( view.context, 145f) > parent.right -> {
                            parent.right - pxFromDp(view.context, 145f) /*Blocking right on drag*/
                        }
                        motionEvent.rawX?.plus(dX) ?: 0f < 0 -> {
                            0f /*Blocking left on drag*/
                        }
                        else -> {
                            motionEvent.rawX.plus(dX) /*Normal horizontal drag*/
                        }
                    }

                    Log.d("Lavekush", "rawX=${motionEvent.rawX} ,rawY=${motionEvent.rawY}")
                    Log.d("Lavekush", "ppX=${motionEvent.x} ,ppY=${motionEvent.y}")
                    val gotoY: Float = when {
                        motionEvent.rawY < (0 + y) + parent.top-> {
                            0f /*Blocking top on drag*/
                        }
//                        (motionEvent.rawY?.plus(dX) ?: 0f) + pxFromDp(child.context, 68f) > parent.bottom -> {
//                            parent.bottom - pxFromDp(view.context, 68f) /*Blocking bottom on drag*/
//                        }
                        else -> {
                            motionEvent.rawY.plus(dY)  /*Normal vertical drag*/
                        }
                    }

//                    val gotoY: Float
//                    if (event.getY() - TagViewProvider.pxFromDp(child.context, 68f) < 0) {
//                        gotoY = 0f /*Blocking top on drag*/
//                    } else if (event.getRawY() + TagViewProvider.dX + TagViewProvider.pxFromDp(
//                            child.context,
//                            68f
//                        ) > parent.bottom
//                    ) {
//                        gotoY = parent.bottom - TagViewProvider.pxFromDp(
//                            child.context,
//                            68f
//                        ) /*Blocking bottom on drag*/
//                    } else {
//                        gotoY = event.getRawY() + TagViewProvider.dY /*Normal vertical drag*/
//                    }


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
            }
            false
        })

        /*Handling for X position*/
        var xTapped: Float =
            feedXMediaTagging.X?.minus(pxFromDp(child.context, 145f) / 2) ?: 0f
        val x2Want: Float = xTapped + pxFromDp(child.context, 145f)
        val x2Diff = parent.right - x2Want
        if (x2Diff < 0) {
            xTapped += x2Diff
        }


        /*Handling for Y position*/
        var yTapped = feedXMediaTagging.Y!!
        val y2Want: Float = yTapped + pxFromDp(child.context, 68f)
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
        parent.addView(child)
        if (child.y < parent.height * 0.75) {
            child.findViewById<View>(R.id.topNotch).visibility = View.VISIBLE
            child.findViewById<View>(R.id.bottomNotch).visibility = View.GONE
        } else {
            child.findViewById<View>(R.id.topNotch).visibility = View.GONE
            child.findViewById<View>(R.id.bottomNotch).visibility = View.VISIBLE
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                ((Activity)parent.getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        /*Handling for X position*/
//                        float xTapped = event.getX() - temp.getWidth() / 2;
//                        float x2Want = xTapped + temp.getWidth();
//                        float x2Diff = parent.getRight() - x2Want;
//                        if (x2Diff < 0) {
//                            xTapped += x2Diff;
//                        }
//
//
//                        /*Handling for Y position*/
//                        float yTapped = event.getY();
//                        float y2Want = yTapped + temp.getHeight();
//                        float y2Diff = parent.getBottom() - y2Want;
//                        if (y2Diff < 0) {
//                            yTapped += y2Diff;
//                        }
//
//                        child.setX(xTapped);
//                        child.setY(yTapped);
//                        child.setVisibility(View.VISIBLE);
//                        parent.addView(child);
//                        parent.removeView(temp);
//                    }
//                });
//            }
//        });
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}

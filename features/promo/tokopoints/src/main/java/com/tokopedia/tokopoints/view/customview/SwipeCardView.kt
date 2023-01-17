package com.tokopedia.tokopoints.view.customview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil.sendEvent
import com.tokopedia.tokopoints.view.util.CommonConstant
import kotlin.math.abs

class SwipeCardView : FrameLayout,View.OnTouchListener {
    private var mTextSwipeTitle: TextView? = null
    private var mTouchView: RelativeLayout? = null
    private var mSwipeIcon: ImageButton? = null
    private var mView: View? = null
    private var mMinWidth = -1
    private var isSwipeEnable = true
    private var mCouponContainer: ViewFlipper? = null
    private var mTextCoupon: TextView? = null
    private var mBtnCopyCode: TextView? = null
    private var mOnSwipeListener: OnSwipeListener? = null

    var couponCode:String?
      get() = mTextCoupon?.text.toString()
      set(code) {
          if (code == null || code.isEmpty()) {
              return
          }
          mCouponContainer?.displayedChild = CONTAINER_CODE
          mTextCoupon?.text = code
          isSwipeEnable = false
      }

    var mPreviousX = 0f
    var x1 = 0f

    constructor(context: Context) : super(context){
        initView(context)
    }

    constructor(context: Context,attrs:AttributeSet) : super(context,attrs){
        initView(context)
    }

    constructor(context: Context,attrs:AttributeSet,defStyleAttr:Int) : super(context,attrs,defStyleAttr){
        initView(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onFinishInflate() {
        super.onFinishInflate()
        val params = mTouchView?.layoutParams as? RelativeLayout.LayoutParams
        mMinWidth = params?.width ?: 0
        mTouchView?.setOnTouchListener(this)
        mBtnCopyCode?.setOnClickListener {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                CommonConstant.CLIPBOARD_COUPON_CODE,
                mTextCoupon?.text.toString()
            )
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip)
                Toast.makeText(
                    context,
                    resources.getString(R.string.tp_mes_copy_code),
                    Toast.LENGTH_LONG
                ).show()
            }
            sendEvent(
                context,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.COPY_CODE,
                mTextCoupon?.text.toString()
            )
        }
    }

    private fun initView(context: Context) {
        mView = inflate(context,R.layout.tp_layout_swipe, this)
        mTextSwipeTitle = mView?.findViewById(R.id.text_swipe)
        mTouchView = mView?.findViewById(R.id.rel_touch_view)
        mSwipeIcon = mView?.findViewById(R.id.btn_swipe_icon)
        mCouponContainer = mView?.findViewById(R.id.container_inner)
        mTextCoupon = mView?.findViewById(R.id.text_code)
        mBtnCopyCode = mView?.findViewById(R.id.btn_copy_code)
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (!isSwipeEnable) {
            return false
        }
        val x = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                    this.parent.requestDisallowInterceptTouchEvent(true)
                    x1 = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                this.parent.requestDisallowInterceptTouchEvent(true)
                val dx = x - mPreviousX
                if (event.x > mMinWidth && isLeftSwipe(dx)
                    && event.x < getMaxSwipeWidth()
                ) {
                    val layoutParams = mTouchView?.layoutParams as? RelativeLayout.LayoutParams
                    layoutParams?.width = event.x.toInt()
                    mTouchView?.layoutParams = layoutParams
                    if (event.x > getMaxSwipeWidth() * .3) {
                        mTextSwipeTitle?.setTextColor(
                            ContextCompat.getColor(
                                context, com.tokopedia.unifyprinciples.R.color.Unify_N0
                            )
                        )
                        mSwipeIcon?.visibility = GONE
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                this.parent.requestDisallowInterceptTouchEvent(false)
                if (abs(x1 - event.x) > MIN_SWIPE_AMOUNT_PX) {
                    if (event.x > getMaxSwipeWidth() * .75) {
                        isSwipeEnable = false
                        val layoutParams = mTouchView?.layoutParams as? RelativeLayout.LayoutParams
                        val anim = ValueAnimator.ofInt(layoutParams?.width?:0, getMaxSwipeWidth())
                        anim.addUpdateListener { valueAnimator: ValueAnimator ->
                            val `val` = valueAnimator.animatedValue as Int
                            val layoutParams12 = mTouchView!!.layoutParams
                            layoutParams12.width = `val`
                            mTouchView?.layoutParams = layoutParams12
                        }
                        anim.duration = DELAY_SHOW_CHECK_MS.toLong()
                        anim.start()
                        mCouponContainer?.postDelayed({
                            mCouponContainer?.displayedChild = CONTAINER_CHECK
                            mSwipeIcon?.visibility = GONE
                            mOnSwipeListener?.onComplete()
                        }, DELAY_SHOW_CHECK_MS.toLong())
                    } else {
                        isSwipeEnable = true
                        val layoutParams = mTouchView?.layoutParams as? RelativeLayout.LayoutParams
                        val anim = ValueAnimator.ofInt(layoutParams?.width?:0, mMinWidth)
                        anim.addUpdateListener { valueAnimator: ValueAnimator ->
                            val `val` = valueAnimator.animatedValue as Int
                            val layoutParams1 = mTouchView!!.layoutParams
                            layoutParams1.width = `val`
                            mTouchView?.layoutParams = layoutParams1
                        }
                        anim.duration = DELAY_BACK_MS.toLong()
                        anim.start()
                        mTextSwipeTitle?.setTextColor(
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N700_32
                            )
                        )
                        mSwipeIcon?.visibility = VISIBLE
                        mOnSwipeListener?.onPartialSwipe()
                    }
                }
            }
        }
        mPreviousX = x
        return true
    }

    fun reset() {
        val layoutParams = mTouchView?.layoutParams as? RelativeLayout.LayoutParams
        val anim = ValueAnimator.ofInt(layoutParams?.width?:0, mMinWidth)
        anim.addUpdateListener { valueAnimator: ValueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams1 = mTouchView!!.layoutParams
            layoutParams1.width = `val`
            mTouchView!!.layoutParams = layoutParams1
        }
        anim.duration = DELAY_BACK_MS.toLong()
        anim.start()
        mTextSwipeTitle?.setTextColor(
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_32
            )
        )
        mCouponContainer?.postDelayed({
            mSwipeIcon?.visibility = VISIBLE
            isSwipeEnable = true
            mCouponContainer?.displayedChild = CONTAINER_TEXT
        }, DELAY_BACK_MS.toLong())
    }

    private fun getMaxSwipeWidth(): Int {
        if(mView==null) return 0
        return mView!!.width - resources.getDimensionPixelOffset(R.dimen.gami_core_floating_egg_dp_6)
    }

    private fun isLeftSwipe(dx: Float) = dx > 0

    fun setTitle(title: String?) {
        mTextSwipeTitle?.text = title
    }

    fun setOnSwipeListener(listener: OnSwipeListener){
        this.mOnSwipeListener = listener
    }

    interface OnSwipeListener {
        fun onComplete()
        fun onPartialSwipe()
    }

    companion object{
        private const val MIN_SWIPE_AMOUNT_PX = 10
        private const val CONTAINER_TEXT = 0
        private const val CONTAINER_CHECK = 1
        private const val CONTAINER_CODE = 2
        private const val DELAY_SHOW_CHECK_MS = 150
        private const val DELAY_BACK_MS = 250
    }
}

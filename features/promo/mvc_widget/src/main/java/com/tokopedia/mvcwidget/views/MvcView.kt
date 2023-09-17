package com.tokopedia.mvcwidget.views

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.MVCActivityCallbacks
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.user.session.UserSession
import java.lang.ref.WeakReference

/*
* 1. It has internal Padding of 6dp to render its shadows
* */
class MvcView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val REQUEST_CODE = 121
        const val RESULT_CODE_OK = 1
    }

    var imageChevron: AppCompatImageView?
    var mvcTextContainerFirst: MvcTextContainer?
    var mvcTextContainerSecond: MvcTextContainer?
    var mvcContainer: View?
    var imageCouponBackground: SquareImageView?
    var imgIconChevron: AppCompatImageView?
    var mvcBgImg: AppCompatImageView?

    var mvcAnimationHandler: MvcAnimationHandler
    private var startActivityForResultFunction: (() -> Unit)? = null
    private val mvcActivityCallbacks = MVCActivityCallbacks()

    var shopId: String = ""
    var additionalParamJson = ""
    var productId = ""
    var isTokomember = false
    val mvcTracker = MvcTracker()

    @MvcSource
    var source: Int = MvcSource.SHOP

    init {
        View.inflate(context, R.layout.mvc_entry_view, this)

        imageChevron = this.findViewById(R.id.image_chevron)
        mvcContainer = this.findViewById(R.id.mvc_container)
        mvcTextContainerFirst = this.findViewById(R.id.mvc_text_container_first)
        mvcTextContainerSecond = this.findViewById(R.id.mvc_text_container_second)
        imageCouponBackground = this.findViewById(R.id.image_coupon_bg)
        imgIconChevron = this.findViewById(R.id.image_chevron)
        mvcBgImg = this.findViewById(R.id.mvc_bg_img)

        setClicks()

        mvcAnimationHandler = MvcAnimationHandler(WeakReference(mvcTextContainerFirst), WeakReference(mvcTextContainerSecond))
        mvcAnimationHandler.checkToCancelTimer()
    }

    private fun setClicks() {
        mvcContainer?.setOnClickListener {
            (context.applicationContext as Application).let {
                it.unregisterActivityLifecycleCallbacks(mvcActivityCallbacks)
                it.registerActivityLifecycleCallbacks(mvcActivityCallbacks)
            }
            if (startActivityForResultFunction != null) {
                startActivityForResultFunction?.invoke()
            } else {
                if (context is AppCompatActivity) {
                    (context as AppCompatActivity).startActivityForResult(TransParentActivity.getIntent(context, shopId, this.source, productId = this.productId, hashCode = mvcActivityCallbacks.hashCodeForMVC, additionalParamJson = additionalParamJson), REQUEST_CODE)
                } else {
                    (context).startActivity(TransParentActivity.getIntent(context, shopId, this.source, productId = this.productId, hashCode = mvcActivityCallbacks.hashCodeForMVC, additionalParamJson = additionalParamJson))
                }
            }

            mvcTracker.userClickEntryPoints(shopId, UserSession(context).userId, this.source, isTokomember, this.productId)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (context.applicationContext as Application).unregisterActivityLifecycleCallbacks(mvcActivityCallbacks)
    }

    fun setData(
        mvcData: MvcData,
        shopId: String,
        @MvcSource source: Int,
        startActivityForResultFunction: (() -> Unit)? = null,
        mvcTrackerImpl: MvcTrackerImpl = DefaultMvcTrackerImpl(),
        productId: String = ""
    ) {
        this.source = source
        this.shopId = shopId
        this.productId = productId
        this.startActivityForResultFunction = startActivityForResultFunction
        this.mvcTracker.trackerImpl = mvcTrackerImpl
        mvcActivityCallbacks.mvcTrackerImpl = mvcTrackerImpl
        mvcActivityCallbacks.hashCodeForMVC = mvcData.hashCode()
        setMVCData(mvcData.animatedInfoList)
    }

    private fun setMVCData(animatedInfos: List<AnimatedInfos?>?) {
        mvcAnimationHandler.stopAnimation()

        if (!animatedInfos.isNullOrEmpty()) {
            mvcAnimationHandler.animatedInfoList = animatedInfos

            if (animatedInfos.size == 1) {
                isTokomember = false
                mvcAnimationHandler.isTokomember = isTokomember
                val animatedInfo = animatedInfos.first()
                animatedInfo?.let {
                    mvcTextContainerFirst?.setData(it.title ?: "", it.subTitle ?: "", it.iconURL ?: "")
                }
            } else {
                isTokomember = true
                mvcAnimationHandler.isTokomember = isTokomember
                mvcAnimationHandler.startTimer()
            }
        }
    }

    fun sendImpressionTrackerForPdp() {
        if (this.shopId.isNotEmpty()) {
            mvcTracker.tokomemberImpressionOnPdp(this.shopId, UserSession(context).userId, isTokomember)
        }
    }

    fun setOverrideWidgetTheme(isOverrideWidgetTheme: Boolean) {
        if (!isOverrideWidgetTheme) return

        imgIconChevron?.setColorFilter(
            ContextCompat.getColor(context, R.color.mvc_dms_static_nn900)
        )
        imgIconChevron?.setColorFilter(Color.parseColor("#2E3137"), PorterDuff.Mode.SRC_IN)
        imgIconChevron?.setBackgroundColor(
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        )
        mvcBgImg?.setColorFilter(
            Color.parseColor("#FFFFFF"),
            PorterDuff.Mode.SRC_IN
        )

        // First container
        mvcTextContainerFirst?.tvTitle?.setTextColor(ContextCompat.getColor(context, R.color.mvc_dms_static_nn950))
        mvcTextContainerFirst?.tvSubTitle?.setTextColor(ContextCompat.getColor(context, R.color.mvc_dms_static_nn950))

        // Second container
        mvcTextContainerSecond?.tvTitle?.setTextColor(ContextCompat.getColor(context, R.color.mvc_dms_static_nn950))
        mvcTextContainerSecond?.tvSubTitle?.setTextColor(ContextCompat.getColor(context, R.color.mvc_dms_static_nn950))
    }
}

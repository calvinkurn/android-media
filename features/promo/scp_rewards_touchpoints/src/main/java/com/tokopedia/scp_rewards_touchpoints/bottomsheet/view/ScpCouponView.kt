package com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.scp_rewards_touchpoints.R

class PocCouponView @JvmOverloads constructor(context:Context,attrs:AttributeSet?=null,defStyle:Int = 0) : FrameLayout(context, attrs,defStyle) {

    private val paint = Paint()

    init {
        inflate(context,R.layout.layout_scp_coupon_view,this)
        paint.color = ResourcesCompat.getColor(resources,com.tokopedia.unifyprinciples.R.color.Unify_NN1000,null)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        this.setWillNotDraw(false)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Glide.with(context)
            .load("https://images.tokopedia.net/img/cache/576x192/uqilkZ/2022/6/13/daa537fe-fa65-4cb6-bf6c-6f196e224134.jpg")
            .into(findViewById<ImageUnify>(R.id.coupon_image))
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.drawCircle(0f,height/2f,12f.dpToPx(),paint)
        canvas?.drawCircle(width.toFloat(),height/2f,12f.dpToPx(),paint)
    }
}

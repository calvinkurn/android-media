package com.tokopedia.tokopoints.view.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.tokopoints.R

class MerchantRewardToolbar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {
    var tvToolbarTitle: TextView? = null
    var backArrowWhite: Drawable? = null
    var mContext: Context? = null
    var view: View? = null


    init {
        mContext = context
        view = View.inflate(context, R.layout.tp_toolbar_merchant_coupon, this)
        tvToolbarTitle = findViewById(R.id.tv_tpToolbar_title)
        initDrawableResources()
        setNavIcon(context)
    }

    private fun setNavIcon(context: Context) {
        post {
            val NAV_ICON_POSITION = 1
            val v = getChildAt(NAV_ICON_POSITION)
            if (v != null && v.layoutParams is androidx.appcompat.widget.Toolbar.LayoutParams && v is AppCompatImageButton) {
                val lp = v.getLayoutParams() as androidx.appcompat.widget.Toolbar.LayoutParams
                lp.width = context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_48)
                lp.gravity = Gravity.CENTER_VERTICAL
                v.setLayoutParams(lp)
                v.invalidate()
                v.requestLayout()
            }
        }
    }

    private fun initDrawableResources() {
        backArrowWhite = getBitmapDrawableFromVectorDrawable(mContext, R.drawable.ic_new_action_back_tokopoints)
        backArrowWhite?.setColorFilter(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N400), PorterDuff.Mode.SRC_ATOP)
        navigationIcon = backArrowWhite
    }

    private fun getBitmapDrawableFromVectorDrawable(context: Context?, drawableId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context?.let { ContextCompat.getDrawable(it, drawableId) }
        } else BitmapDrawable(context?.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    private fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        var drawable = context?.let { ContextCompat.getDrawable(it, drawableId) }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = drawable?.let { DrawableCompat.wrap(it).mutate() }
        }
        val bitmap = drawable?.intrinsicWidth?.let {
            Bitmap.createBitmap(it,
                    drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        } ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun setTitle(title: CharSequence) {
        if (title == resources.getString(R.string.tp_kupon_toko)) return
        super.setTitle(resources.getString(R.string.tp_kupon_toko))
        tvToolbarTitle?.text = resources.getString(R.string.tp_kupon_toko)
    }

}
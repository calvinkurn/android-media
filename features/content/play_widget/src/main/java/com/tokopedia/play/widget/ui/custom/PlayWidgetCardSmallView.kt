package com.tokopedia.play.widget.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel
import com.tokopedia.play_common.widget.playBannerCarousel.extension.loadImage

/**
 * Created by jegul on 06/10/20
 */
class PlayWidgetCardSmallView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val ivCover: ImageView
    private val ivDiscount: ImageView
    private val tvTotalViews: TextView
    private val tvTitle: TextView
    private val tvUpcoming: TextView

//    private val borderPath = Path()
//    private val borderWidth by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1).toFloat() }
//    private val borderRadius by lazy { resources.getDimensionPixelSize(R.dimen.play_widget_small_radius).toFloat() }
//    private val gradientShader by lazy {
//        LinearGradient(
//                0f,
//                0f,
//                0f,
//                height.toFloat(),
//                MethodChecker.getColor(context, R.color.play_widget_small_gradient_top),
//                MethodChecker.getColor(context, com.tokopedia.unifycomponents.R.color.Red_R400),
//                Shader.TileMode.CLAMP
//        )
//    }
//    private val paint = Paint().apply {
//        isAntiAlias = true
//        isDither = true
//    }

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_small, this)
        ivCover = view.findViewById(R.id.iv_cover)
        ivDiscount = view.findViewById(R.id.iv_discount)
        tvTotalViews = view.findViewById(R.id.tv_total_views)
        tvTitle = view.findViewById(R.id.tv_title)
        tvUpcoming = view.findViewById(R.id.tv_upcoming)
    }

    fun setModel(model: PlayWidgetCardUiModel) {
        ivCover.loadImage(model.video.coverUrl)
        if (model.hasPromo) ivDiscount.visible() else ivDiscount.gone()
        if (model.totalViewVisible) {
            tvTotalViews.visible()
            tvTotalViews.text = model.totalView
        } else tvTotalViews.gone()
        tvTitle.text = "Kuliner Lokal Lezatnya Total Lezatnya"
        tvUpcoming.text = "10 Jan - 17.00"
    }

//    override fun dispatchDraw(canvas: Canvas?) {
//        canvas?.let { drawBorder(it) }
//        super.dispatchDraw(canvas)
//    }

//    private fun drawBorder(canvas: Canvas) {
//        canvas.save()
//        canvas.clipRoundRect(
//                RectF(borderWidth, borderWidth, width - borderWidth, height - borderWidth),
//                borderRadius
//        )
//        paint.shader = gradientShader
//        canvas.drawRoundRect(
//                RectF(0f, 0f, width.toFloat(), height.toFloat()),
//                borderRadius,
//                borderRadius,
//                paint
//        )
//        canvas.restore()
//    }
//
//    private fun Canvas.clipRoundRect(rect: RectF, radius: Float) {
//        borderPath.reset()
//        borderPath.addRoundRect(rect, radius, radius, Path.Direction.CW)
//        borderPath.close()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) clipOutPath(borderPath)
//        else clipPath(borderPath, Region.Op.DIFFERENCE)
//    }
}
package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.data.AddTextColorProvider
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.BackgroundTemplateDetail
import com.tokopedia.media.editor.data.entity.AddTextBackgroundTemplate
import com.tokopedia.media.editor.data.entity.AddTextPosition
import com.tokopedia.unifyprinciples.getTypeface
import javax.inject.Inject
import kotlin.math.ceil

interface AddTextFilterRepository {
    fun generateOverlayText(
        size: Pair<Int, Int>,
        data: EditorAddTextUiModel
    ): Bitmap?
}

class AddTextFilterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val colorProvider: AddTextColorProvider,
    private val bitmapCreationRepository: BitmapCreationRepository
) : AddTextFilterRepository {

    private var paddingVertical = 0f
    private var paddingHorizontal = 0f
    private var adjustmentPadding = 0f
    private var floatingWidthAdjustment = 0f
    private var paddingFloating = 0f

    private var backgroundModel: AddTextBackgroundTemplate? = null

    private var fontSize = 0f
        set(value) {
            field = value

            // base padding
            paddingVertical = PADDING_VERTICAL_PERCENTAGE * value
            paddingHorizontal = PADDING_HORIZONTAL_PERCENTAGE * value
            adjustmentPadding = ADJUSTMENT_PADDING_PERCENTAGE * value

            if (backgroundModel == AddTextBackgroundTemplate.FLOATING) {
                // when using template floating need extra space
                paddingFloating = PADDING_FLOATING_ENABLE * value

                // get total horizontal padding (padding x2) to reduce background width
                floatingWidthAdjustment = paddingFloating * 2
            }
        }

    override fun generateOverlayText(
        size: Pair<Int, Int>,
        data: EditorAddTextUiModel
    ): Bitmap? {
        paddingFloating = 0f
        floatingWidthAdjustment = 0f

        val (originalImageWidth, originalImageHeight) = size

        return bitmapCreationRepository.createBitmap(
            BitmapCreation.emptyBitmap(
                originalImageWidth,
                originalImageHeight,
                Bitmap.Config.ARGB_8888
            )
        )?.let { bitmap ->
            val canvas = Canvas(bitmap)

            backgroundModel = data.getBackgroundTemplate()?.addTextBackgroundModel
            fontSize = originalImageHeight * FONT_SIZE_PERCENTAGE

            val mTextPaint = createTextPaint(data.textColor, data.getTypeFaceStyle())

            var mTextLayout: StaticLayout? = null

            canvas.save()

            var backgroundWidth = originalImageWidth

            when (data.textPosition) {
                AddTextPosition.RIGHT -> {
                    // update font size according to image width since text on side
                    fontSize = originalImageWidth * FONT_SIZE_PERCENTAGE
                    mTextPaint.textSize = fontSize

                    // update background size
                    backgroundWidth = originalImageHeight

                    // update text width bound to follow image height
                    mTextLayout = createStaticLayout(data ,canvas.height - (paddingHorizontal * 2).toInt(), mTextPaint)

                    val yOffset = canvas.width - mTextLayout.height - paddingVertical - paddingFloating
                    canvas.rotate(ROTATE_90_DEGREE_COUNTER_CLOCKWISE)
                    canvas.translate(-(canvas.height.toFloat() - paddingHorizontal), yOffset)
                }
                AddTextPosition.LEFT -> {
                    // update font size according to image width since text on side
                    fontSize = originalImageWidth * FONT_SIZE_PERCENTAGE
                    mTextPaint.textSize = fontSize

                    // update text width bound to follow image height
                    mTextLayout = createStaticLayout(data, canvas.height - (paddingHorizontal * 2).toInt(), mTextPaint)

                    val yOffset = -(mTextLayout.height + paddingVertical)
                    canvas.rotate(ROTATE_90_DEGREE_CLOCKWISE)
                    canvas.translate(paddingHorizontal, yOffset)
                }
                AddTextPosition.BOTTOM -> {
                    mTextLayout = createStaticLayout(data, (canvas.width - (paddingHorizontal * 2)).toInt(), mTextPaint)

                    val yOffset = (canvas.height - mTextLayout.height).toFloat() - (paddingVertical + paddingFloating)
                    canvas.translate(paddingHorizontal, yOffset)
                }
                AddTextPosition.TOP -> {
                    mTextLayout = createStaticLayout(data, (canvas.width - (paddingHorizontal * 2)).toInt(), mTextPaint)

                    canvas.translate(paddingHorizontal, paddingVertical)
                }
            }

            // background only on bottom & right
            getAddTextBackgroundDrawable(data.getBackgroundTemplate())?.let {
                val backgroundHeight = ceil((mTextLayout.height + paddingVertical + adjustmentPadding)).toInt()
                backgroundWidth -= floatingWidthAdjustment.toInt()

                it.toBitmap().scale(backgroundWidth, backgroundHeight).apply {
                    canvas.drawBitmap(this, -(paddingHorizontal - paddingFloating), -adjustmentPadding, null)
                }
            }

            mTextLayout.draw(canvas)
            canvas.restore()

            bitmap
        }
    }

    private fun getAddTextBackgroundDrawable(backgroundDetail: BackgroundTemplateDetail?): Drawable?{
        if (backgroundDetail == null) return null

        when (backgroundDetail.addTextBackgroundModel) {
            AddTextBackgroundTemplate.FULL -> editorR.drawable.add_text_background_full
            AddTextBackgroundTemplate.FLOATING -> editorR.drawable.add_text_background_floating
            AddTextBackgroundTemplate.SIDE_CUT -> editorR.drawable.add_text_background_cut
        }.apply {
            this.let {
                ContextCompat.getDrawable(context, it)?.let { backgroundDrawable ->
                    colorProvider.implementDrawableColor(backgroundDrawable, backgroundDetail.addTextBackgroundColor)
                    return backgroundDrawable
                }
            }
            return null
        }
    }

    private fun createStaticLayout(data: EditorAddTextUiModel, width: Int, paint: TextPaint): StaticLayout {
        val text = data.textValue.let {
            if (data.getBackgroundTemplate() != null) {
                ellipsizeDrawText(it, paint, width.toFloat())
            } else {
                it
            }
        }

        val alignment = data.getLayoutAlignment()

        return StaticLayout(
            text,
            paint,
            width,
            alignment,
            1f,
            0f,
            false
        )
    }

    private fun ellipsizeDrawText(text: String, paint: TextPaint, width: Float): CharSequence {
        val indexOfNewLine = text.indexOf("\n")
        return if (indexOfNewLine < 0 || indexOfNewLine > text.length - 1) {
            // ellipsize without substring for new line
            TextUtils.ellipsize(text, paint, width, TextUtils.TruncateAt.END)
        } else {
            // ellipsize with substring for new line
            TextUtils.ellipsize(
                text.substring(0, indexOfNewLine),
                paint,
                width,
                TextUtils.TruncateAt.END
            )
        }
    }

    private fun createTextPaint(color: Int, fontStyle: Int): TextPaint {
        val mTextPaint = TextPaint()

        mTextPaint.textSize = fontSize
        mTextPaint.color = color

        val typeFace = getTypeface(context, TYPEFACE)
        mTextPaint.typeface = Typeface.create(typeFace, fontStyle)

        return mTextPaint
    }

    companion object {
        private const val TYPEFACE = "OpenSauceOneRegular.ttf"

        // 4,5%
        private const val FONT_SIZE_PERCENTAGE = 1 / 22f
        private const val PADDING_VERTICAL_PERCENTAGE = 0.39f
        private const val PADDING_HORIZONTAL_PERCENTAGE = 0.8f

        // used to adjust font position since the text boundary ascendant is lower than descendant
        private const val ADJUSTMENT_PADDING_PERCENTAGE = 0.1f

        private const val PADDING_FLOATING_ENABLE = 0.25f

        private const val ROTATE_90_DEGREE_CLOCKWISE = 90f
        private const val ROTATE_90_DEGREE_COUNTER_CLOCKWISE = -90f
    }
}

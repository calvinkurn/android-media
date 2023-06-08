package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.core.graphics.toColorInt
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.data.entity.AddTextColorManager
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import com.tokopedia.media.editor.ui.uimodel.BitmapCreationModel
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_FULL
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_FLOATING
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_SIDE_CUT
import com.tokopedia.media.editor.ui.uimodel.LatarTemplateDetail
import com.tokopedia.unifyprinciples.getTypeface
import javax.inject.Inject

interface AddTextFilterRepository {
    fun generateTextOverlay(
        size: Pair<Int, Int>,
        data: EditorAddTextUiModel
    ): Bitmap?
}

class AddTextFilterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val colorCollection: AddTextColorManager,
    private val bitmapCreationRepository: BitmapCreationRepository
) : AddTextFilterRepository {

    private var paddingVertical = 0f
    private var paddingHorizontal = 0f
    private var adjustmentPadding = 0f
    private var floatingWidthAdjustment = 0f
    private var paddingFloating = 0f

    private var latarModel: Int? = null

    private var fontSize = 0f
        set(value) {
            field = value

            // base padding
            paddingVertical = PADDING_VERTICAL_PERCENTAGE * value
            paddingHorizontal = PADDING_HORIZONTAL_PERCENTAGE * value
            adjustmentPadding = ADJUSTMENT_PADDING_PERCENTAGE * value

            if (latarModel == TEXT_LATAR_TEMPLATE_FLOATING) {
                // when using template floating need extra space
                paddingFloating = PADDING_FLOATING_ENABLE * value

                // get total horizontal padding (padding x2) to reduce latar width
                floatingWidthAdjustment = paddingFloating * 2
            }
        }

    override fun generateTextOverlay(
        size: Pair<Int, Int>,
        data: EditorAddTextUiModel
    ): Bitmap? {
        val originalImageWidth = size.first
        val originalImageHeight = size.second

        return bitmapCreationRepository.createBitmap(
            BitmapCreation.emptyBitmap(
                originalImageWidth,
                originalImageHeight,
                Bitmap.Config.ARGB_8888
            )
        )?.let { bitmap ->
            val canvas = Canvas(bitmap)

            latarModel = data.getLatarTemplate()?.latarModel
            fontSize = originalImageHeight * FONT_SIZE_PERCENTAGE

            val mTextPaint = createTextPaint(data.textColor.toColorInt(), data.getTypeFaceStyle())

            var mTextLayout: StaticLayout? = null

            canvas.save()

            var latarWidth = originalImageWidth

            when (data.textPosition) {
                EditorAddTextUiModel.TEXT_POSITION_RIGHT -> {
                    // update font size according to image width since text on side
                    fontSize = originalImageWidth * FONT_SIZE_PERCENTAGE
                    mTextPaint.textSize = fontSize

                    // update latar size
                    latarWidth = originalImageHeight

                    // update text width bound to follow image height
                    mTextLayout = createStaticLayout(data ,canvas.height - (paddingHorizontal * 2).toInt(), mTextPaint)

                    val yOffset = canvas.width - mTextLayout.height - paddingVertical - paddingFloating
                    canvas.rotate(-90f)
                    canvas.translate(-(canvas.height.toFloat() - paddingHorizontal), yOffset)
                }
                EditorAddTextUiModel.TEXT_POSITION_LEFT -> {
                    // update font size according to image width since text on side
                    fontSize = originalImageWidth * FONT_SIZE_PERCENTAGE
                    mTextPaint.textSize = fontSize

                    // update text width bound to follow image height
                    mTextLayout = createStaticLayout(data, canvas.height - (paddingHorizontal * 2).toInt(), mTextPaint)

                    val yOffset = -(mTextLayout.height + paddingVertical)
                    canvas.rotate(90f)
                    canvas.translate(paddingHorizontal, yOffset)
                }
                EditorAddTextUiModel.TEXT_POSITION_BOTTOM -> {
                    mTextLayout = createStaticLayout(data, (canvas.width - (paddingHorizontal * 2)).toInt(), mTextPaint)

                    val yOffset = (canvas.height - mTextLayout.height).toFloat() - (paddingVertical + paddingFloating)
                    canvas.translate(paddingHorizontal, yOffset)
                }
                EditorAddTextUiModel.TEXT_POSITION_TOP -> {
                    mTextLayout = createStaticLayout(data, (canvas.width - (paddingHorizontal * 2)).toInt(), mTextPaint)

                    canvas.translate(paddingHorizontal, paddingVertical)
                }
            }

            // latar only on bottom & right
            getLatarDrawable(data.getLatarTemplate())?.let {
                val latarHeight = (mTextLayout!!.height + paddingVertical + adjustmentPadding).toInt()
                latarWidth -= floatingWidthAdjustment.toInt()

                it.toBitmap().scale(latarWidth, latarHeight).apply {
                    canvas.drawBitmap(this, -(paddingHorizontal - paddingFloating), -adjustmentPadding, null)
                }
            }

            mTextLayout!!.draw(canvas)
            canvas.restore()

            bitmap
        }
    }

    private fun getLatarDrawable(latarDetail: LatarTemplateDetail?): Drawable?{
        if (latarDetail == null) return null

        when (latarDetail.latarModel) {
            TEXT_LATAR_TEMPLATE_FULL -> editorR.drawable.add_text_latar_full
            TEXT_LATAR_TEMPLATE_FLOATING -> editorR.drawable.add_text_latar_floating
            TEXT_LATAR_TEMPLATE_SIDE_CUT -> editorR.drawable.add_text_latar_cut
            else -> null
        }.apply {
            this?.let {
                ContextCompat.getDrawable(context, it)?.let { latarBg ->
                    colorCollection.implementDrawableColor(latarBg, latarDetail.latarColor)
                    return latarBg
                }
            }
            return null
        }
    }

    private fun createStaticLayout(data: EditorAddTextUiModel, width: Int, paint: TextPaint): StaticLayout {
        val text = data.textValue.let {
            if (data.getLatarTemplate() != null) {
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
    }
}

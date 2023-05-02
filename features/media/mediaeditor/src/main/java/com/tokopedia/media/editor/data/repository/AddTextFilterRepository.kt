package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_FULL
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_FLOATING
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_SIDE_CUT
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel.Companion.TEXT_LATAR_TEMPLATE_WHITE
import com.tokopedia.media.editor.ui.uimodel.LatarTemplateDetail
import com.tokopedia.media.editor.utils.toWhite
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.getTypeface
import javax.inject.Inject

interface AddTextFilterRepository {
    fun generateTextOverlay(
        size: Pair<Int, Int>,
        data: EditorAddTextUiModel
    ): Bitmap
}

class AddTextFilterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AddTextFilterRepository {
    override fun generateTextOverlay(
        size: Pair<Int, Int>,
        data: EditorAddTextUiModel
    ): Bitmap {
        // TODO: create generator for latar text, current only support free text
        val originalImageWidth = size.first
        val originalImageHeight = size.second

        val bitmap =
            Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // base padding
        var paddingBottom = 6f.toPx()
        var paddingSide = 16f.toPx()
        var paddingTop = 0f

        // if floating type
        data.getLatarTemplate()?.let {
            if (it.latarModel == TEXT_LATAR_TEMPLATE_FLOATING) {
                val extraPaddingFloating = 8f.toPx()
                paddingBottom += extraPaddingFloating
                paddingSide += extraPaddingFloating
                paddingTop += extraPaddingFloating
            }
        }

        val sizePercentage = 1 / 12f // 8,3%
        val fontSize = originalImageHeight * sizePercentage

        val mTextPaint = TextPaint()

        mTextPaint.textSize = fontSize
        mTextPaint.color = data.getColor(context)

        val typeFace = getTypeface(context, TYPEFACE)
        mTextPaint.typeface = Typeface.create(typeFace, data.getTypeFaceStyle())

        val alignment = data.getLayoutAlignment()
        var mTextLayout = StaticLayout(
            data.textValue,
            mTextPaint,
            (canvas.width - (paddingSide * 2)).toInt(),
            alignment,
            1.0f,
            0.0f,
            false
        )

        canvas.save()

        when (data.textPosition) {
            EditorAddTextUiModel.TEXT_POSITION_RIGHT -> {
                mTextLayout = StaticLayout(
                    data.textValue,
                    mTextPaint,
                    (canvas.height - (paddingSide * 2)).toInt(),
                    alignment,
                    1.0f,
                    0.0f,
                    false
                )
                val yOffset = canvas.width - mTextLayout.height - paddingBottom
                canvas.rotate(-90f)
                canvas.translate(-(canvas.height.toFloat() - paddingSide), yOffset)
            }
            EditorAddTextUiModel.TEXT_POSITION_LEFT -> {
                mTextLayout = StaticLayout(
                    data.textValue,
                    mTextPaint,
                    (canvas.height - paddingSide).toInt(),
                    alignment,
                    1.0f,
                    0.0f,
                    false
                )

                val yOffset = -(mTextLayout.height + paddingBottom)
                canvas.rotate(90f)
                canvas.translate(paddingSide, yOffset)
            }
            EditorAddTextUiModel.TEXT_POSITION_BOTTOM -> {
                var yOffset = (canvas.height - mTextLayout.height).toFloat() - paddingBottom
                canvas.translate(paddingSide, yOffset)
            }
            EditorAddTextUiModel.TEXT_POSITION_TOP -> {
                canvas.translate(paddingSide, paddingTop)
            }
        }

        getLatarDrawable(data.getLatarTemplate())?.let {
            it.toBitmap().scale(canvas.width, mTextLayout.height + paddingBottom.toInt()).apply {
                canvas.drawBitmap(this, -paddingSide, 0f, null)
            }
        }

        mTextLayout.draw(canvas)
        canvas.restore()

        return bitmap
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
                    if (latarDetail.latarColor == TEXT_LATAR_TEMPLATE_WHITE) latarBg.toWhite()
                    return latarBg
                }
            }
            return null
        }
    }

    companion object {
        private const val TYPEFACE = "OpenSauceOneRegular.ttf"
    }
}

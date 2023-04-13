package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.StaticLayout
import android.text.TextPaint
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
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
        val originalImageWidth = size.first
        val originalImageHeight = size.second

        val bitmap =
            Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val padding = 16f.toPx()
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
            (canvas.width - padding).toInt(),
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
                    (canvas.height - padding).toInt(),
                    alignment,
                    1.0f,
                    0.0f,
                    false
                )
                val yOffset = canvas.width - mTextLayout.height - padding
                canvas.rotate(-90f)
                canvas.translate(-(canvas.height.toFloat() + padding), yOffset)
            }
            EditorAddTextUiModel.TEXT_POSITION_LEFT -> {
                mTextLayout = StaticLayout(
                    data.textValue,
                    mTextPaint,
                    (canvas.height - padding).toInt(),
                    alignment,
                    1.0f,
                    0.0f,
                    false
                )

                val yOffset = -(mTextLayout.height + padding)
                canvas.rotate(90f)
                canvas.translate(padding, yOffset)
            }
            EditorAddTextUiModel.TEXT_POSITION_BOTTOM -> {
                var yOffset = canvas.height - mTextLayout.height - padding
                canvas.translate(padding, yOffset)
            }
            EditorAddTextUiModel.TEXT_POSITION_TOP -> {
                canvas.translate(padding, 0f)
            }
        }

        mTextLayout.draw(canvas)
        canvas.restore()

        return bitmap
    }

    companion object {
        private const val TYPEFACE = "OpenSauceOneRegular.ttf"
    }
}

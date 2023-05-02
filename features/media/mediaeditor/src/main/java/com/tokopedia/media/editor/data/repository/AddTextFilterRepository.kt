package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
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

        getLatarDrawable(data.getLatarTemplate()?.latarModel)?.let {
            it.toBitmap().scale(canvas.width, mTextLayout.height + padding.toInt()).apply {
                canvas.drawBitmap(this, -padding, 0f, null)
            }
        }

        mTextLayout.draw(canvas)
        canvas.restore()

        return bitmap
    }

    private fun getLatarDrawable(latarModel: Int?): Drawable?{
        if (latarModel == null) return null

        when (latarModel) {
            TEXT_LATAR_TEMPLATE_FULL -> editorR.drawable.add_text_latar_full
            TEXT_LATAR_TEMPLATE_FLOATING -> editorR.drawable.add_text_latar_floating
            TEXT_LATAR_TEMPLATE_SIDE_CUT -> editorR.drawable.add_text_latar_cut
            else -> null
        }.apply {
            this?.let {
                return ContextCompat.getDrawable(context, it)
            }
            return null
        }
    }

    companion object {
        private const val TYPEFACE = "OpenSauceOneRegular.ttf"
    }
}

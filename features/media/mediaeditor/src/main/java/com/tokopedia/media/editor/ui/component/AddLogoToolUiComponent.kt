package com.tokopedia.media.editor.ui.component

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.databinding.AddLogoTipsBottomsheetBinding
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

class AddLogoToolUiComponent constructor(
    viewGroup: ViewGroup,
    val listener: Listener
) : UiComponent(viewGroup, editorR.id.uc_tool_add_logo) {

    private val shopAvatar = findViewById<ImageView>(editorR.id.add_logo_shop_avatar)
    private val uploadAvatar = findViewById<ImageView>(editorR.id.add_logo_upload_avatar)
    private val uploadButton = findViewById<CardUnify2>(editorR.id.add_logo_upload_button)

    private var originalImageWidth = 1
    private var originalImageHeight = 1

    fun bottomSheet() = BottomSheetUnify().apply {
        val child = AddLogoTipsBottomsheetBinding.inflate(
            LayoutInflater.from(container().context),
            null,
            false
        )

        child.addLogoTipsMore.text = getTipsMoreText()
        child.addLogoTipsList.text = getTipsListText()

        setTitle(BOTTOM_SHEET_TITLE)
        setChild(child.root)
    }

    private fun getTipsMoreText(): SpannableString {
        val modifiedText = resources().getString(editorR.string.add_logo_tips_more)
        val editFormat = resources().getString(editorR.string.add_logo_tips_more_format)

        val span = SpannableString(modifiedText)
        val drawable = getIconUnifyDrawable(
            container().context,
            IconUnify.INFORMATION
        )
        drawable?.setBounds(0, 0, BOTTOM_SHIT_ICON_SIZE.toPx(), BOTTOM_SHIT_ICON_SIZE.toPx())
        drawable?.let {
            val image = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)
            val startIndex = modifiedText.indexOf(editFormat)

            span.setSpan(image, startIndex, startIndex + editFormat.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        return span
    }

    private fun getTipsListText(): CharSequence? {
        val text = resources().getString(editorR.string.add_logo_tips_list)
        return HtmlLinkHelper(
            container().context,
            text
        ).spannedString
    }

    fun setupView(imageWidth: Int, imageHeight: Int, avatarUrl: String) {
        originalImageWidth = imageWidth
        originalImageHeight = imageHeight

        initImageView(avatarUrl)
        initListener()
        container().show()
    }

    private fun initImageView(avatarUrl: String) {
        loadImageWithEmptyTarget(context, avatarUrl, {},
            MediaBitmapEmptyTarget(
                onReady = {
                    shopAvatar.setImageBitmap(
                        roundedBitmap(it, isCircular = true)
                    )
                }
            )
        )

        loadImageWithEmptyTarget(context, "/storage/emulated/0/Pictures/Rectangle 18.png", {},
            MediaBitmapEmptyTarget(
                onReady = {
                    uploadAvatar.setImageBitmap(
                        roundedBitmap(it, cornerRadius = 8f)
                    )
                }
            )
        )
    }

    private fun initListener() {
        uploadAvatar.setOnClickListener {
            listener.onLogoChosen(getUploadAvatarBitmap())
        }

        shopAvatar.setOnClickListener {
            listener.onLogoChosen(getShopAvatarBitmap())
        }

        uploadButton.setOnClickListener {
            listener.onUpload()
        }
    }

    private fun getUploadAvatarBitmap(): Bitmap {
        val resultBitmap = Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        drawBitmap(canvas, uploadAvatar.drawable.toBitmap())

        return resultBitmap
    }

    private fun getShopAvatarBitmap(): Bitmap {
        val resultBitmap = Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        drawBitmap(canvas, shopAvatar.drawable.toBitmap())

        return resultBitmap
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(bitmap, LOGO_X_POS, LOGO_Y_POS, Paint())
    }

    private fun roundedBitmap(source: Bitmap, cornerRadius: Float = 0f, isCircular: Boolean = false): Bitmap {
        val roundedBitmap = RoundedBitmapDrawableFactory.create(resources(), source)
        roundedBitmap.cornerRadius = if (isCircular) (source.width.toFloat() / 2) else cornerRadius

        return roundedBitmap.toBitmap()
    }

    interface Listener {
        fun onLogoChosen(bitmap: Bitmap)
        fun onUpload()
    }

    companion object {
        private const val LOGO_X_POS = 100f
        private const val LOGO_Y_POS = 100f

        private const val BOTTOM_SHEET_TITLE = "Tips upload logo"
        private const val BOTTOM_SHIT_ICON_SIZE = 17
    }
}

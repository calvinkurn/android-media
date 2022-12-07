package com.tokopedia.media.editor.ui.component

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.android.material.snackbar.Snackbar
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
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class AddLogoToolUiComponent constructor(
    viewGroup: ViewGroup,
    val listener: Listener
) : UiComponent(viewGroup, editorR.id.uc_tool_add_logo) {

    private val shopAvatar = findViewById<ImageView>(editorR.id.add_logo_shop_avatar)
    private val uploadAvatar = findViewById<ImageView>(editorR.id.add_logo_upload_avatar)
    private val uploadAvatarWrapper = findViewById<LinearLayout>(editorR.id.add_logo_upload_avatar_wrapper)
    private val uploadButton = findViewById<CardUnify2>(editorR.id.add_logo_upload_button)
    private val uploadText = findViewById<Typography>(editorR.id.add_logo_upload_text)

    private var originalImageWidth = 1
    private var originalImageHeight = 1

    private var shopAvatarUrl = ""
    private var isShopAvatarReady = false

    private var toaster: Snackbar? = null
    private var retryNumber = 0

    private var logoUrl: String = ""

    fun bottomSheet(isUpload: Boolean = true) = BottomSheetUnify().apply {
        val child = AddLogoTipsBottomsheetBinding.inflate(
            LayoutInflater.from(container().context),
            null,
            false
        )

        child.addLogoBottomsheetButton.apply {
            if (isUpload) {
                text = container().context.getString(editorR.string.add_logo_tips_upload_text)
            }
            setOnClickListener {
                if (isUpload) listener.onPickerCall()
                dismiss()
            }
        }

        child.addLogoTipsMore.text = getTipsMoreText()
        child.addLogoTipsList.text = getTipsListText()

        setTitle(BOTTOM_SHEET_TITLE)
        setChild(child.root)
    }

    fun setupView(imageWidth: Int, imageHeight: Int, avatarUrl: String, localAvatarUrl: String = "") {
        originalImageWidth = imageWidth
        originalImageHeight = imageHeight

        shopAvatarUrl = avatarUrl

        if (localAvatarUrl.isNotEmpty()) {
            initUploadAvatar(localAvatarUrl)
        }

        initShopAvatar()
        initListener()
        container().show()
    }

    fun getLogoUrl(): String {
        return logoUrl
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

    private fun initShopAvatar() {
        loadImageWithEmptyTarget(context, shopAvatarUrl, {},
            MediaBitmapEmptyTarget(
                onReady = {
                    shopAvatar.setImageBitmap(
                        roundedBitmap(it, isCircular = true)
                    )
                    isShopAvatarReady = true
                    logoUrl = shopAvatarUrl
                },
                onFailed = {
                    shopAvatar.setImageDrawable(it)
                    shopLoadFailedToaster()
                }
            )
        )
    }

    fun initUploadAvatar(imageUrl: String) {
        loadImageWithEmptyTarget(context, imageUrl, {},
            MediaBitmapEmptyTarget(
                onReady = {
                    uploadAvatarWrapper.show()
                    uploadAvatar.setImageBitmap(
                        roundedBitmap(it, cornerRadius = 8f)
                    )
                    logoUrl = shopAvatarUrl

                    uploadText.text = resources().getString(editorR.string.editor_add_logo_upload)
                }
            )
        )
    }

    private fun initListener() {
        uploadAvatar.setOnClickListener {
            listener.onLogoChosen(getUploadAvatarBitmap())
        }

        shopAvatar.setOnClickListener {
            if (isShopAvatarReady) listener.onLogoChosen(getShopAvatarBitmap())
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

    private fun shopLoadFailedToaster() {
        toaster?.dismiss()
        Handler().postDelayed({
            toaster = Toaster.build(container(),
                resources().getString(editorR.string.editor_add_logo_toast_text),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                resources().getString(editorR.string.editor_add_logo_toast_cta),
                clickListener = {
                    if (retryNumber >= RETRY_LIMIT) listener.onLoadFailed()
                    retryNumber++
                    initShopAvatar()
                }
            )
            toaster?.show()
        }, TOASTER_DELAY_TIME)
    }

    interface Listener {
        fun onLogoChosen(bitmap: Bitmap)
        fun onUpload()
        fun onLoadFailed()
        fun onPickerCall()
    }

    companion object {
        private const val LOGO_X_POS = 100f
        private const val LOGO_Y_POS = 100f

        private const val BOTTOM_SHEET_TITLE = "Tips upload logo"
        private const val BOTTOM_SHIT_ICON_SIZE = 17

        private const val TOASTER_DELAY_TIME = 300L
        private const val RETRY_LIMIT = 3
    }
}

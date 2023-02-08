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
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.editor.R as editorR
import com.tokopedia.media.editor.databinding.AddLogoTipsBottomsheetBinding
import com.tokopedia.media.editor.ui.uimodel.EditorAddLogoUiModel
import com.tokopedia.media.editor.utils.cropCenterImage
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.ImageRatioType
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
    private val uploadAvatarWrapper =
        findViewById<LinearLayout>(editorR.id.add_logo_upload_avatar_wrapper)
    private val uploadButton = findViewById<CardUnify2>(editorR.id.add_logo_upload_button)
    private val uploadText = findViewById<Typography>(editorR.id.add_logo_upload_text)

    private var originalImageWidth = 1
    private var originalImageHeight = 1

    private var uploadAvatarUrl = ""
    private var shopAvatarUrl = ""
    private var isShopAvatarReady = false

    private var toaster: Snackbar? = null
    private var retryNumber = 0

    private var selectedLogoUrl: String = ""

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
                if (isUpload) listener.onUpload()
                dismiss()
            }
        }

        child.addLogoTipsMore.text = getTipsMoreText()
        child.addLogoTipsList.text = getTipsListText()

        setTitle(BOTTOM_SHEET_TITLE)
        setChild(child.root)
    }

    fun setupView(
        imageWidth: Int,
        imageHeight: Int,
        avatarUrl: String,
        localAvatarUrl: String = "",
        addLogoData: EditorAddLogoUiModel
    ) {
        originalImageWidth = imageWidth
        originalImageHeight = imageHeight

        shopAvatarUrl = avatarUrl
        uploadAvatarUrl = localAvatarUrl
        selectedLogoUrl = addLogoData.logoUrl

        initListener()
        initShopAvatar()
        container().show()

        if (localAvatarUrl.isNotEmpty()) {
            initUploadAvatar(localAvatarUrl)
        }
    }

    fun getLogoUrl(): String {
        return selectedLogoUrl
    }

    fun isUploadAvatarReady(): Boolean {
        return uploadAvatarWrapper.isVisible
    }

    fun initUploadAvatar(imageUrl: String) {
        loadImageWithEmptyTarget(
            context,
            imageUrl,
            {},
            MediaBitmapEmptyTarget(
                onReady = {
                    uploadAvatarWrapper.show()
                    uploadAvatar.setImageBitmap(
                        roundedBitmap(it, cornerRadius = 8f)
                    )
                    uploadText.text = resources().getString(editorR.string.editor_add_logo_upload)

                    uploadAvatarUrl = imageUrl
                    isLogoChosen(imageUrl)
                }
            )
        )
    }

    fun getLogoState(): Int? {
        return if (selectedLogoUrl.isNotEmpty()) {
            when (selectedLogoUrl) {
                uploadAvatarUrl -> LOGO_UPLOAD
                shopAvatarUrl -> LOGO_SHOP
                else -> null
            }
        } else {
            null
        }
    }

    /**
     * generate new overlay image if product image size is change
     */
    fun generateOverlayImage(
        bitmap: Bitmap,
        newSize: Pair<Int, Int>,
        isCircular: Boolean = false
    ): Bitmap {
        originalImageWidth = newSize.first
        originalImageHeight = newSize.second

        val resultBitmap =
            Bitmap.createBitmap(newSize.first, newSize.second, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        val logoBitmap = if (isCircular) {
            roundedBitmap(bitmap, isCircular = true)
        } else {
            roundedBitmap(bitmap, 8f.toPx())
        }

        drawBitmap(
            canvas,
            getDrawLogo(logoBitmap)
        )

        return resultBitmap
    }

    private fun initShopAvatar() {
        loadImageWithEmptyTarget(
            context,
            shopAvatarUrl,
            {},
            MediaBitmapEmptyTarget(
                onReady = { loadedBitmap ->
                    val croppedBitmap = cropCenterImage(loadedBitmap, ImageRatioType.RATIO_1_1)

                    shopAvatar.setImageBitmap(
                        roundedBitmap(croppedBitmap?.first ?: loadedBitmap, isCircular = true)
                    )
                    isShopAvatarReady = true
                    isLogoChosen(shopAvatarUrl)
                },
                onFailed = {
                    shopAvatar.setImageDrawable(it)
                    shopLoadFailedToaster()
                }
            )
        )
    }

    private fun isLogoChosen(finishedUrl: String) {
        // restore previous selected logo from state
        if (selectedLogoUrl.isNotEmpty() && selectedLogoUrl == finishedUrl) {
            when (finishedUrl) {
                uploadAvatarUrl -> uploadAvatar.performClick()
                shopAvatarUrl -> shopAvatar.performClick()
            }
            return
        }

        if (uploadAvatarUrl.isNotEmpty() && finishedUrl == uploadAvatarUrl) {
            uploadAvatar.performClick()
        } else if (uploadAvatarUrl.isEmpty() && finishedUrl == shopAvatarUrl) {
            shopAvatar.performClick()
        }
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

            span.setSpan(
                image,
                startIndex,
                startIndex + editFormat.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
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

    private fun initListener() {
        uploadAvatar.setOnClickListener {
            selectedLogoUrl = uploadAvatarUrl
            listener.onLogoChosen(getUploadAvatarBitmap())
        }

        shopAvatar.setOnClickListener {
            selectedLogoUrl = shopAvatarUrl
            if (isShopAvatarReady) listener.onLogoChosen(getShopAvatarBitmap())
        }

        uploadButton.setOnClickListener {
            listener.onUpload()
        }
    }

    private fun getUploadAvatarBitmap(): Bitmap {
        val resultBitmap =
            Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        drawBitmap(
            canvas,
            getDrawLogo(
                uploadAvatar.drawable.toBitmap()
            )
        )

        return resultBitmap
    }

    private fun getShopAvatarBitmap(): Bitmap {
        val resultBitmap =
            Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        drawBitmap(
            canvas,
            getDrawLogo(
                shopAvatar.drawable.toBitmap()
            )
        )

        return resultBitmap
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(
            bitmap,
            LOGO_X_POS * originalImageWidth,
            LOGO_Y_POS * originalImageHeight,
            Paint()
        )
    }

    private fun roundedBitmap(
        source: Bitmap,
        cornerRadius: Float = 0f,
        isCircular: Boolean = false
    ): Bitmap {
        val roundedBitmap = RoundedBitmapDrawableFactory.create(resources(), source)
        roundedBitmap.cornerRadius = if (isCircular) (source.width.toFloat() / 2) else cornerRadius

        return roundedBitmap.toBitmap()
    }

    private fun shopLoadFailedToaster() {
        toaster?.dismiss()
        Handler().postDelayed({
            toaster = Toaster.build(
                container(),
                resources().getString(editorR.string.editor_add_logo_toast_text),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                resources().getString(editorR.string.editor_add_logo_toast_cta),
                clickListener = {
                    if (retryNumber >= RETRY_LIMIT) {
                        listener.onLoadFailed()
                    } else {
                        listener.onLoadRetry()
                    }
                    retryNumber++
                    initShopAvatar()
                }
            )
            toaster?.show()
        }, TOASTER_DELAY_TIME)
    }

    // get logo bitmap that rescaled to 1/6 from base image
    private fun getDrawLogo(source: Bitmap): Bitmap {
        val widthTarget = originalImageWidth / LOGO_SIZE_DIVIDER
        val heightTarget = ((widthTarget.toFloat() / source.width) * source.height).toInt()

        return Bitmap.createScaledBitmap(
            source,
            widthTarget,
            heightTarget,
            true
        )
    }

    interface Listener {
        fun onLogoChosen(bitmap: Bitmap)
        fun onUpload()
        fun onLoadFailed()
        fun onLoadRetry()
    }

    companion object {
        private var LOGO_X_POS = 0.03f
        private var LOGO_Y_POS = 0.03f

        private const val BOTTOM_SHEET_TITLE = "Tips upload logo"
        private const val BOTTOM_SHIT_ICON_SIZE = 17

        private const val TOASTER_DELAY_TIME = 300L
        private const val RETRY_LIMIT = 3

        // logo size 1/6 from base image
        private const val LOGO_SIZE_DIVIDER = 6

        const val LOGO_SHOP = 0
        const val LOGO_UPLOAD = 1
    }
}

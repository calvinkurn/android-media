package com.tokopedia.media.editor.ui.component

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.media.editor.R
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.basecomponent.UiComponent

class AddLogoToolUiComponent constructor(
    viewGroup: ViewGroup,
    val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_add_logo) {

    private val shopAvatar = findViewById<ImageView>(R.id.add_logo_shop_avatar)
    private val uploadAvatar = findViewById<ImageView>(R.id.add_logo_upload_avatar)

    private var originalImageWidth = 1
    private var originalImageHeight = 1

    fun setupView(imageWidth: Int, imageHeight: Int) {
        originalImageWidth = imageWidth
        originalImageHeight = imageHeight

        initImageView()
        initListener()
        container().show()
    }

    private fun initImageView() {
        loadImageWithEmptyTarget(context, "https://images.tokopedia.net/img/seller_no_logo_1.png", {},
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
    }

    private fun getUploadAvatarBitmap(): Bitmap {
        val resultBitmap = Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

//        canvas.drawBitmap(uploadAvatar.drawable.toBitmap(), 100f, 100f, Paint())
        drawBitmap(canvas, uploadAvatar.drawable.toBitmap())

        return resultBitmap
    }

    private fun getShopAvatarBitmap(): Bitmap {
        val resultBitmap = Bitmap.createBitmap(originalImageWidth, originalImageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

//        canvas.drawBitmap(shopAvatar.drawable.toBitmap(), LOGO_X_POS, LOGO_Y_POS, Paint())
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
    }

    companion object {
        private const val LOGO_X_POS = 100f
        private const val LOGO_Y_POS = 100f
    }
}

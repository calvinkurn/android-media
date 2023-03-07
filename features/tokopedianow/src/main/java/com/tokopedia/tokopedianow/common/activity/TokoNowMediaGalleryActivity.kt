package com.tokopedia.tokopedianow.common.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.image_gallery.ImageGallery
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.MediaGalleryUiModel
import com.tokopedia.unifycomponents.ImageUnify

class TokoNowMediaGalleryActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_MEDIA_GALLERY_DATA = "extra_media_gallery_data"

        fun createIntent(context: Context, data: MediaGalleryUiModel): Intent {
            return Intent(context, TokoNowMediaGalleryActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(EXTRA_MEDIA_GALLERY_DATA, data)
                })
            }
        }
    }

    private var mediaGallery: ImageGallery? = null
    private var closeButton: ImageUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_media_gallery)

        mediaGallery = findViewById(R.id.media_gallery)
        closeButton = findViewById(R.id.close_button)

        intent?.extras?.getParcelable<MediaGalleryUiModel>(EXTRA_MEDIA_GALLERY_DATA)?.let {
            setupImageGallery(it)
        }
    }

    private fun setupImageGallery(data: MediaGalleryUiModel) {
        val isAutoPlay = DeviceConnectionInfo.isConnectWifi(this)
        val arrayDrawable = data.mapToImageGalleryItems(isAutoPlay)
        val selectedPosition = data.selectedPosition

        mediaGallery?.apply {
            setImages(
                arrayDrawable = arrayDrawable,
                defaultIndex = selectedPosition
            )

            onOverlayHiddenChange = { isHidden ->
                closeButton?.showWithCondition(!isHidden)
            }

            thumbnailRecyclerView.hide()
        }

        closeButton?.setOnClickListener { finish() }
    }
}
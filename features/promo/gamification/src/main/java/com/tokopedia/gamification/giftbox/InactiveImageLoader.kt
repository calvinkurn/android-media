package com.tokopedia.gamification.giftbox

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.gamification.taptap.data.entiity.TokenAsset
import com.tokopedia.media.loader.loadImage
import java.util.concurrent.atomic.AtomicInteger

class InactiveImageLoader {

    var totalImages = 0
    var imagesLoaded = AtomicInteger(0)

    fun loadImages(imageInactive: AppCompatImageView, imageInactiveBg: AppCompatImageView, tokenAsset: TokenAsset, imageCallback: (() -> Unit)) {

        fun sendCallback() {
            val count = imagesLoaded.incrementAndGet()
            if (count == totalImages) {
                imageCallback.invoke()
            }
        }

        val imageUrls = tokenAsset.imageV2URLs
        if (!imageUrls.isNullOrEmpty()) {
            totalImages += 1
        }
        val bgUrl = tokenAsset.backgroundImgURL
        if (!bgUrl.isNullOrEmpty()) {
            totalImages += 1
        }


        if (!imageUrls.isNullOrEmpty()) {
            imageInactive.loadImage(imageUrls.first()) {
                this.listener(
                    onSuccess = { _, _ ->
                        sendCallback()
                    }
                )
            }
        }


        if (!bgUrl.isNullOrEmpty()) {
            imageInactiveBg.loadImage(bgUrl) {
                this.listener(
                    onSuccess = { _, _ ->
                        sendCallback()
                    }
                )
            }
        }
    }
}

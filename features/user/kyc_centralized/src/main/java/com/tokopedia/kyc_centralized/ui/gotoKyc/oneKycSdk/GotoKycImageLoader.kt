package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.widget.ImageView
import com.gojek.kyc.plus.image.KycSdkImageLoader
import com.gojek.kyc.plus.image.KycSdkImageLoadingRequestListener
import com.tokopedia.media.loader.loadImage
import javax.inject.Inject

class GotoKycImageLoader @Inject constructor(): KycSdkImageLoader {
    override fun loadLocalImageInto(
        activity: Activity,
        imageView: ImageView,
        imagePath: String,
        requestListener: KycSdkImageLoadingRequestListener?
    ) {
        imageView.loadImage(imagePath)
    }
}

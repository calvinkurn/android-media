package com.tokopedia.vouchercreation.create.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.common.utils.getSavedImageDirPath
import javax.inject.Inject

class SaveBannerVoucherUseCase @Inject constructor(@ApplicationContext private val context: Context): UseCase<String>() {

    var bannerBitmap: Bitmap? = null

    override suspend fun executeOnBackground(): String =
            bannerBitmap?.getSavedImageDirPath(context, System.currentTimeMillis().toString()).toBlankOrString()

}
package com.tokopedia.vouchercreation.shop.create.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.common.utils.getSavedImageDirPath
import javax.inject.Inject

class SaveSquareVoucherUseCase @Inject constructor(@ApplicationContext private val context: Context): UseCase<String>() {

    var squareBitmap: Bitmap? = null

    override suspend fun executeOnBackground(): String =
            squareBitmap?.getSavedImageDirPath(context, System.currentTimeMillis().toString()).toBlankOrString()

}
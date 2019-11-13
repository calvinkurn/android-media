package com.tokopedia.kolcommon.view.listener

import android.content.Context
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase

/**
 * Created by jegul on 2019-11-13
 */
interface KolPostLikeListener {

    val androidContext: Context

    fun onLikeKolSuccess(rowNumber: Int, action: LikeKolPostUseCase.LikeKolPostAction)

    fun onLikeKolError(message: String)
}

package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.utils.image.ImageProcessingUtil
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author : Steven 2019-08-27
 */
class CompressImageUseCase @Inject constructor(){

    fun compressImage(imageUrl: String): Observable<String> {
        return Observable.just(imageUrl)
                .concatMap {
                    val file = ImageProcessingUtil.compressImageFile(it, QUALITY_COMPRESS)
                    Observable.just(file.absolutePath)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        var QUALITY_COMPRESS = 80
    }
}
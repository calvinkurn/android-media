package com.tokopedia.vouchercreation.create.view.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.create.domain.model.MerchantPromotionCreateMvData
import com.tokopedia.vouchercreation.create.domain.model.upload.ImageUploadResponse
import com.tokopedia.vouchercreation.create.domain.usecase.CreateVoucherUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.SaveBannerVoucherUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.SaveSquareVoucherUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.UploadVoucherUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class ReviewVoucherViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val createVoucherUseCase: CreateVoucherUseCase,
        private val uploadVoucherUseCase: UploadVoucherUseCase,
        private val saveBannerVoucherUseCase: SaveBannerVoucherUseCase,
        private val saveSquareVoucherUseCase: SaveSquareVoucherUseCase) : BaseViewModel(dispatcher) {

    private val mCreateVoucherResponseLiveData = MutableLiveData<Result<MerchantPromotionCreateMvData>>()
    val createVoucherResponseLiveData: LiveData<Result<MerchantPromotionCreateMvData>>
        get() = mCreateVoucherResponseLiveData

    fun createVoucher(bannerBitmap: Bitmap,
                      squareBitmap: Bitmap,
                      createVoucherParam: CreateVoucherParam) {
        launchCatchError(
                block = {
                    val bannerImagePath = async {
                        saveBannerVoucherUseCase.bannerBitmap = bannerBitmap
                        saveBannerVoucherUseCase.executeOnBackground()}
                    val squareImagePath = async {
                        saveSquareVoucherUseCase.squareBitmap = squareBitmap
                        saveSquareVoucherUseCase.executeOnBackground()
                    }
                    uploadAndCreateVoucher(bannerImagePath.await(), squareImagePath.await(), createVoucherParam)
                },
                onError = {
                    mCreateVoucherResponseLiveData.value = Fail(it)
                })
    }

    private fun uploadAndCreateVoucher(bannerImagePath: String,
                                       squareImagePath: String,
                                       createVoucherParam: CreateVoucherParam) {
        getUploadVoucherImagesObservable(bannerImagePath, squareImagePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( object : Subscriber<Pair<String,String>>() {
                    override fun onNext(resultPair: Pair<String, String>?) {
                        createVoucherParam.run {
                            image = resultPair?.first.toBlankOrString()
                            image_square = resultPair?.second.toBlankOrString()
                        }
                        createVoucher(createVoucherParam)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        mCreateVoucherResponseLiveData.value = Fail(e)
                    }
                })
    }

    private fun getUploadVoucherImagesObservable(bannerImagePath: String,
                                                 squareImagePath: String): Observable<Pair<String, String>> =
        Observable.zip(
                uploadBannerImage(bannerImagePath),
                uploadSquareImage(squareImagePath)) { bannerResponse, squareResponse ->
            Pair(bannerResponse.data.picObj.toBlankOrString(), squareResponse.data.picObj.toBlankOrString())
        }

    private fun uploadBannerImage(bannerImagePath: String): Observable<ImageUploadResponse> =
            uploadVoucherUseCase.createObservable(UploadVoucherUseCase.createRequestParams(bannerImagePath))
                    .subscribeOn(Schedulers.io())

    private fun uploadSquareImage(squareImagePath: String): Observable<ImageUploadResponse> =
            uploadVoucherUseCase.createObservable(UploadVoucherUseCase.createRequestParams(squareImagePath))
                    .subscribeOn(Schedulers.io())

    fun createVoucher(createVoucherParam: CreateVoucherParam) {
        launchCatchError(
                block = {
                    mCreateVoucherResponseLiveData.value = Success(withContext(Dispatchers.IO) {
                        createVoucherUseCase.params = CreateVoucherUseCase.createRequestParam(createVoucherParam)
                        createVoucherUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mCreateVoucherResponseLiveData.value = Fail(it)
                }
        )
    }
}
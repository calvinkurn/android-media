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
import com.tokopedia.vouchercreation.common.domain.model.UpdateVoucherParam
import com.tokopedia.vouchercreation.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.create.domain.usecase.*
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ReviewVoucherViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val createVoucherUseCase: CreateVoucherUseCase,
        private val updateVoucherUseCase: UpdateVoucherUseCase,
        private val uploadVoucherUseCase: UploadVoucherUseCase,
        private val saveBannerVoucherUseCase: SaveBannerVoucherUseCase,
        private val saveSquareVoucherUseCase: SaveSquareVoucherUseCase) : BaseViewModel(dispatcher) {

    private val mCreateVoucherResponseLiveData = MutableLiveData<Result<Int>>()
    val createVoucherResponseLiveData: LiveData<Result<Int>>
        get() = mCreateVoucherResponseLiveData

    private val mUpdateVoucherSuccessLiveData = MutableLiveData<Result<Boolean>>()
    val updateVoucherSuccessLiveData: LiveData<Result<Boolean>>
        get() = mUpdateVoucherSuccessLiveData

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



    fun updateVoucher(bannerBitmap: Bitmap,
                      squareBitmap: Bitmap,
                      voucherReviewUiModel: VoucherReviewUiModel,
                      token: String,
                      voucherId: Int) {
        launchCatchError(
                block = {
                    val bannerImagePath = async {
                        saveBannerVoucherUseCase.bannerBitmap = bannerBitmap
                        saveBannerVoucherUseCase.executeOnBackground()}
                    val squareImagePath = async {
                        saveSquareVoucherUseCase.squareBitmap = squareBitmap
                        saveSquareVoucherUseCase.executeOnBackground()
                    }
                    val updateVoucherParam = UpdateVoucherParam.mapToParam(voucherReviewUiModel, token, voucherId)
                    uploadAndUpdateVoucher(bannerImagePath.await(), squareImagePath.await(), updateVoucherParam)
                },
                onError = {
                    mUpdateVoucherSuccessLiveData.value = Fail(it)
                }
        )
    }

    private fun uploadAndCreateVoucher(bannerImagePath: String,
                                       squareImagePath: String,
                                       createVoucherParam: CreateVoucherParam) {
        getUploadVoucherImagesObservable(bannerImagePath, squareImagePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( object : Subscriber<MutableList<String?>>() {
                    override fun onNext(urlList: MutableList<String?>?) {
                        createVoucherParam.run {
                            image = urlList?.getOrNull(0).toBlankOrString()
                            imageSquare = urlList?.getOrNull(1).toBlankOrString()
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

    private fun createVoucher(createVoucherParam: CreateVoucherParam) {
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

    private fun uploadAndUpdateVoucher(bannerImagePath: String,
                                       squareImagePath: String,
                                       updateVoucherParam: UpdateVoucherParam) {
        getUploadVoucherImagesObservable(bannerImagePath, squareImagePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( object : Subscriber<MutableList<String?>>() {
                    override fun onNext(urlList: MutableList<String?>?) {
                        updateVoucherParam.run {
                            image = urlList?.getOrNull(0).toBlankOrString()
                            imageSquare = urlList?.getOrNull(1).toBlankOrString()
                        }
                        updateVoucher(updateVoucherParam)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        mCreateVoucherResponseLiveData.value = Fail(e)
                    }
                })
    }

    private fun updateVoucher(updateVoucherParam: UpdateVoucherParam) {
        launchCatchError(
                block = {
                    mUpdateVoucherSuccessLiveData.value = Success(withContext(Dispatchers.IO) {
                        updateVoucherUseCase.params = UpdateVoucherUseCase.createRequestParam(updateVoucherParam)
                        updateVoucherUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mUpdateVoucherSuccessLiveData.value = Fail(it)
                }
        )
    }

    private fun getUploadVoucherImagesObservable(bannerImagePath: String,
                                                 squareImagePath: String): Observable<MutableList<String?>> =
            uploadVoucherUseCase.createObservable(UploadVoucherUseCase.createRequestParams(bannerImagePath, squareImagePath))

}
package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.create.domain.usecase.SaveSquareVoucherUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.UploadVoucherUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.ChangeVoucherPeriodUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetTokenUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.withContext
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ChangeVoucherPeriodViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers,
                                                       @Named("io") private val ioScheduler: Scheduler,
                                                       @Named("main") private val mainThreadScheduler: Scheduler,
                                                       private val changeVoucherPeriodUseCase: ChangeVoucherPeriodUseCase,
                                                       private val uploadVoucherUseCase: UploadVoucherUseCase,
                                                       private val saveSquareVoucherUseCase: SaveSquareVoucherUseCase,
                                                       private val getTokenUseCase: GetTokenUseCase) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"

        private const val DATE_ERROR_MESSAGE = "Incorrect start/end period :"
    }

    private val mVoucherBitmapPairLiveData = MutableLiveData<Pair<VoucherUiModel, Bitmap>>()

    private val mDateStartLiveData = MutableLiveData<String>()
    private val mDateEndLiveData = MutableLiveData<String>()
    private val mHourStartLiveData = MutableLiveData<String>()
    private val mHourEndLiveData = MutableLiveData<String>()

    private val mStartEndDatePairLiveData = MutableLiveData<Pair<String, String>>()
    val startEndDatePairLiveData: LiveData<Pair<String, String>>
        get() = mStartEndDatePairLiveData

    private val mStartDateCalendarLiveData = MutableLiveData<Calendar>()
    val startDateCalendarLiveData: LiveData<Calendar>
        get() = mStartDateCalendarLiveData
    private val mEndDateCalendarLiveData = MutableLiveData<Calendar>()
    val endDateCalendarLiveData: LiveData<Calendar>
        get() = mEndDateCalendarLiveData

    private val mUpdateVoucherSuccessLiveData = MediatorLiveData<Result<Boolean>>().apply {
        addSource(mVoucherBitmapPairLiveData) { (_, squareBitmap) ->
            launchCatchError(
                    block = {
                        withContext(dispatchers.io) {
                            saveSquareVoucherUseCase.squareBitmap = squareBitmap
                            val imageSquare = saveSquareVoucherUseCase.executeOnBackground()
                            uploadAndUpdateVoucher(imageSquare)
                        }
                    },
                    onError = {
                        value = Fail(it)
                    }
            )
        }
    }
    val updateVoucherSuccessLiveData: LiveData<Result<Boolean>>
        get() = mUpdateVoucherSuccessLiveData

    private fun uploadAndUpdateVoucher(squareImagePath: String) {
        getUploadVoucherImagesObservable(squareImagePath)
                .subscribeOn(ioScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe( object : Subscriber<MutableList<String?>>() {
                    override fun onNext(urlList: MutableList<String?>?) {
                        val imageUrl = urlList?.getOrNull(0).toBlankOrString()
                        updateVoucherPeriod(imageUrl)
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        mUpdateVoucherSuccessLiveData.value = Fail(e)
                    }
                })
    }

    private fun getUploadVoucherImagesObservable(squareImagePath: String): Observable<MutableList<String?>> =
            uploadVoucherUseCase.createObservable(UploadVoucherUseCase.createRequestParams(squareImagePath))

    private fun updateVoucherPeriod(imageSquareUrl: String) {
        mVoucherBitmapPairLiveData.value?.let { (uiModel, _) ->
            mDateStartLiveData.value?.let { dateStart ->
                mDateEndLiveData.value?.let { dateEnd ->
                    mHourStartLiveData.value?.let { hourStart ->
                        mHourEndLiveData.value?.let { hourEnd ->
                            launchCatchError(
                                    block = {
                                        mUpdateVoucherSuccessLiveData.value = Success(withContext(dispatchers.io) {
                                            val token = getTokenUseCase.executeOnBackground()
                                            changeVoucherPeriodUseCase.params =
                                                    ChangeVoucherPeriodUseCase.createRequestParam(
                                                            uiModel,
                                                            token,
                                                            dateStart,
                                                            hourStart,
                                                            dateEnd,
                                                            hourEnd,
                                                            imageSquareUrl)
                                            changeVoucherPeriodUseCase.executeOnBackground()
                                        })
                                    },
                                    onError = {
                                        mUpdateVoucherSuccessLiveData.value = Fail(it)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    fun setStartDateCalendar(startDate: Calendar) {
        mStartDateCalendarLiveData.value = startDate
        mDateStartLiveData.value = startDate.time.toFormattedString(DATE_FORMAT)
        mHourStartLiveData.value = startDate.time.toFormattedString(HOUR_FORMAT)
    }

    fun setEndDateCalendar(endDate: Calendar) {
        mEndDateCalendarLiveData.value = endDate
        mDateEndLiveData.value = endDate.time.toFormattedString(DATE_FORMAT)
        mHourEndLiveData.value = endDate.time.toFormattedString(HOUR_FORMAT)
    }

    fun startValidating() {
        mDateStartLiveData.value.let { startDate ->
            mDateEndLiveData.value.let { endDate ->
                if (startDate != null && endDate != null) {
                    mStartEndDatePairLiveData.value = Pair(startDate, endDate)
                } else {
                    mUpdateVoucherSuccessLiveData.value = Fail(
                            MessageErrorException(
                                    "$DATE_ERROR_MESSAGE $startDate / $endDate"
                            )
                    )
                }
            }
        }
    }

    fun validateVoucherPeriod(uiModel: VoucherUiModel,
                              squareBitmap: Bitmap) {
        mVoucherBitmapPairLiveData.postValue(Pair(uiModel, squareBitmap))
    }

}
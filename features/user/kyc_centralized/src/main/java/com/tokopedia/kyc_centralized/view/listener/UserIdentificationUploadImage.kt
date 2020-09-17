package com.tokopedia.kyc_centralized.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.kyc_centralized.view.model.ImageUploadModel
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user_identification_common.subscriber.GetKtpStatusSubscriber
import rx.Observable

/**
 * @author by alvinatin on 21/11/18.
 */
interface UserIdentificationUploadImage {
    interface View : CustomerView {
        val getContext: Context?
        fun onSuccessUpload(kycType: Int, picObjKyc: String, projectId: Int)
        fun onErrorUpload(error: String?)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : CustomerPresenter<View?> {
        fun uploadImage(model: UserIdentificationStepperModel?, projectid: Int)
        fun uploadImageUseCase(imageUploadModel: ImageUploadModel?): Observable<ImageUploadModel?>?
        fun createParam(cameraLoc: String?): RequestParams?
    }
}
package com.tokopedia.affiliate.feature.createpost.view.presenter

import android.util.Log
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadvideo.UploadVideoResponse
import com.tokopedia.affiliate.feature.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract
import com.tokopedia.affiliate.feature.createpost.view.subscriber.GetContentFormSubscriber
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videouploader.domain.UploadVideoUseCase
import com.tokopedia.videouploader.domain.model.VideoUploadDomainModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class CreatePostPresenter @Inject constructor(
        private val getContentFormUseCase: GetContentFormUseCase,
        private val uploadVideoUseCase : UploadVideoUseCase<UploadVideoResponse>,
        private val userSessionInterface : UserSessionInterface)
    : BaseDaggerPresenter<CreatePostContract.View>(), CreatePostContract.Presenter {

    override fun detachView() {
        super.detachView()
        getContentFormUseCase.unsubscribe()
        uploadVideoUseCase.unsubscribe()
    }

    override fun fetchContentForm(idList: MutableList<String>, type: String) {
        view.showLoading()
        getContentFormUseCase.execute(
                GetContentFormUseCase.createRequestParams(idList, type),
                GetContentFormSubscriber(view, type)
        )

        uploadVideoUseCase.execute(
                UploadVideoUseCase.createParam(userSessionInterface.userId,
                        userSessionInterface.accessToken,
                        "/storage/emulated/0/DCIM/Camera/VID_20190315_115325.mp4"),
              object : Subscriber<VideoUploadDomainModel<UploadVideoResponse>>(){
                  override fun onNext(t: VideoUploadDomainModel<UploadVideoResponse>?) {
                     Log.d("NGENG", "onNext")
                  }

                  override fun onCompleted() {

                  }

                  override fun onError(e: Throwable?) {
                     Log.d("NGENG", e.toString())
                  }
              }
        )
    }
}

package com.tokopedia.kyc_centralized

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage
import com.tokopedia.kyc_centralized.view.model.AttachmentImageModel
import com.tokopedia.kyc_centralized.view.model.ImageUploadModel
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationUploadImagePresenter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.util.TestSchedulerProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import rx.subscriptions.CompositeSubscription

class UserIdentificationUploadImagePresenterTest {

    private val uploadImageUseCase: UploadImageUseCase<AttachmentImageModel> = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private lateinit var presenter: UserIdentificationUploadImage.Presenter

    private val compositeSubscription = CompositeSubscription()
    private lateinit var genericResponseSubscriber: TestSubscriber<ImageUploadModel>

    private fun imageUploadModelMock(): ImageUploadModel {
        val imageUploadModel = ImageUploadModel(0, "")
        imageUploadModel.picObjKyc = ""
        imageUploadModel.error = ""
        imageUploadModel.fileName = ""
        imageUploadModel.isSuccess = 0
        return imageUploadModel
    }

    @Before
    fun setup() {
        presenter = UserIdentificationUploadImagePresenter(uploadImageUseCase,
                userSession,
                compositeSubscription,
                TestSchedulerProvider())

        genericResponseSubscriber = TestSubscriber.create<ImageUploadModel>()
    }

    @Test
    fun `it should upload image`() {
        val imageUploadModel = imageUploadModelMock()
        val imageUploadModelObservable = Observable.just(ImageUploadDomainModel(AttachmentImageModel::class.java))

        every {
            userSession.userId
        } returns "1"

        val params = presenter.createParam(imageUploadModel.filePath)

        every {
            uploadImageUseCase.createObservable(params)
        } returns imageUploadModelObservable

        val uploadImageUseCaseObservable= presenter.uploadImageUseCase(imageUploadModel)
        uploadImageUseCaseObservable?.subscribe(genericResponseSubscriber)
        genericResponseSubscriber.assertNoErrors()
    }
}
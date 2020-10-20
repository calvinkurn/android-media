package com.tokopedia.kyc_centralized

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage
import com.tokopedia.kyc_centralized.view.model.AttachmentImageModel
import com.tokopedia.kyc_centralized.view.model.ImageUploadModel
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationUploadImagePresenter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user_identification_common.util.TestSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import rx.subscriptions.CompositeSubscription

class UserIdentificationUploadImagePresenterTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var uploadImageUseCase: UploadImageUseCase<AttachmentImageModel>

    @RelaxedMockK
    private lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    private lateinit var compositeSubscription: CompositeSubscription

    private lateinit var presenter: UserIdentificationUploadImage.Presenter

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
        MockKAnnotations.init(this)
        presenter = UserIdentificationUploadImagePresenter(uploadImageUseCase,
                userSession,
                compositeSubscription,
                TestSchedulerProvider())
    }

    @Test
    fun `it should upload image`() {
        val imageUploadModel = imageUploadModelMock()
        val imageUploadModelObservable = Observable.just(ImageUploadDomainModel(AttachmentImageModel::class.java))
        val testSubscriber: TestSubscriber<ImageUploadModel> = TestSubscriber()

        val params = presenter.createParam(imageUploadModel.filePath)

        every {
            uploadImageUseCase.createObservable(params)
        } returns imageUploadModelObservable

        val uploadImageUseCaseObservable= presenter.uploadImageUseCase(imageUploadModel)
        uploadImageUseCaseObservable?.subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
    }

    @Test
    fun `it should subscribe upload image`() {
        val imageUploadModels = listOf(imageUploadModelMock())
        val testSubscriber: TestSubscriber<List<ImageUploadModel>> = TestSubscriber()

        val userIdentificationStepperModel = mockk<UserIdentificationStepperModel>(relaxed = true)
        val testProjectId = 1

        every {
            compositeSubscription.add(any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(imageUploadModels)
        }

        presenter.uploadImage(userIdentificationStepperModel, testProjectId)

        verify {
            uploadImageUseCase.createObservable(any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(imageUploadModels)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `on detach` () {
        presenter.detachView()
        verify {
            compositeSubscription.unsubscribe()
        }
    }
}
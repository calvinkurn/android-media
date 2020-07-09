package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.data.pojo.NotifCenterSendNotifData
import com.tokopedia.home.account.domain.SendNotifUseCase
import com.tokopedia.home.account.presentation.listener.RedDotGimmickView
import com.tokopedia.user.session.UserSession
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RedDotGimmickPresenterTest {

    @RelaxedMockK
    lateinit var sendNotifUseCase: SendNotifUseCase

    @RelaxedMockK
    lateinit var userSession: UserSession

    @RelaxedMockK
    lateinit var redDotGimmickView: RedDotGimmickView

    private val redDotPresenter by lazy {
        RedDotGimmickPresenter(sendNotifUseCase)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        redDotPresenter.attachView(redDotGimmickView)
    }

    /**
     * notification
     * */
    @Test
    fun `send notification`() {
        val notificaSendNotifData = NotifCenterSendNotifData()

        every {
            sendNotifUseCase.executeCoroutines(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(NotifCenterSendNotifData) -> Unit>()
            onSuccess.invoke(notificaSendNotifData)
        }

        redDotPresenter.sendNotif()

        verify {
            sendNotifUseCase.executeCoroutines(any(), any())
        }
    }
}
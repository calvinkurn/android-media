package com.tokopedia.kol.presenter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.feature.video.domain.usecase.GetVideoDetailUseCase
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kol.feature.video.view.presenter.VideoDetailPresenter
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong

/**
 * Created by meyta.taliti on 24/01/23.
 */
class VideoDetailPresenterTest : KolPostLikeListener {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    @RelaxedMockK
    lateinit var likeKolPostUseCase: LikeKolPostUseCase

    @RelaxedMockK
    lateinit var followKolPostGqlUseCase: FollowKolPostGqlUseCase

    @RelaxedMockK
    lateinit var getVideoDetailUseCase: GetVideoDetailUseCase

    lateinit var presenter: VideoDetailContract.Presenter
    lateinit var view: VideoDetailContract.View

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = VideoDetailPresenter(
            testDispatcher,
            likeKolPostUseCase,
            followKolPostGqlUseCase,
            getVideoDetailUseCase
        )
        view = mockk(relaxed = true)
        presenter.attachView(view)
    }

    @Test
    fun `follow kol should be triggered`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {}

        presenter.followKol(anyInt())

        verify {
            followKolPostGqlUseCase.clearRequest()
            followKolPostGqlUseCase.addRequest(any())
            followKolPostGqlUseCase.execute(any())
        }
    }

    @Test
    fun `unfollow kol should be triggered`() {
        every {
            followKolPostGqlUseCase.execute(any())
        } answers {}

        presenter.unFollowKol(anyInt())

        verify {
            followKolPostGqlUseCase.clearRequest()
            followKolPostGqlUseCase.addRequest(any())
            followKolPostGqlUseCase.execute(any())
        }
    }

    @Test
    fun `like kol should be triggered`() {
        every {
            likeKolPostUseCase.execute(any(), any())
        } answers {}

        presenter.likeKol(
            anyLong(),
            anyInt(),
            this
        )

        verify {
            likeKolPostUseCase.execute(any(), any())
        }
    }

    @Test
    fun `unlike kol should be triggered`() {
        every {
            likeKolPostUseCase.execute(any(), any())
        } answers {}

        presenter.unlikeKol(
            anyLong(),
            anyInt(),
            this
        )

        verify {
            likeKolPostUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be triggered`() {
        presenter.detachView()

        verify {
            likeKolPostUseCase.unsubscribe()
            followKolPostGqlUseCase.unsubscribe()
        }
    }

    override val androidContext: Context get() = mockk(relaxed = true)

    override fun onLikeKolSuccess(rowNumber: Int, action: LikeKolPostUseCase.LikeKolPostAction) {}

    override fun onLikeKolError(message: String) {}
}

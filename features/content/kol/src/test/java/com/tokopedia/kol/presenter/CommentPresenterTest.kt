package com.tokopedia.kol.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.domain.usecase.GetMentionableUserUseCase
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserModel
import com.tokopedia.kol.feature.comment.domain.interactor.DeleteKolCommentUseCase
import com.tokopedia.kol.feature.comment.domain.interactor.GetKolCommentsUseCase
import com.tokopedia.kol.feature.comment.domain.interactor.SendKolCommentUseCase
import com.tokopedia.kol.feature.comment.view.listener.KolComment
import com.tokopedia.kol.feature.comment.view.presenter.KolCommentPresenter
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import rx.Subscriber

/**
 * Created by meyta.taliti on 24/01/23.
 */
class CommentPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getKolCommentsUseCase: GetKolCommentsUseCase

    @RelaxedMockK
    lateinit var sendKolCommentUseCase: SendKolCommentUseCase

    @RelaxedMockK
    lateinit var deleteKolCommentUseCase: DeleteKolCommentUseCase

    @RelaxedMockK
    lateinit var getMentionableUserUseCase: GetMentionableUserUseCase

    @RelaxedMockK
    lateinit var sendReportUseCase: SendReportUseCase

    private lateinit var presenter: KolComment.Presenter
    lateinit var view: KolComment.View

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = KolCommentPresenter(
            getKolCommentsUseCase,
            sendKolCommentUseCase,
            deleteKolCommentUseCase,
            getMentionableUserUseCase,
            sendReportUseCase
        )

        view = mockk(relaxed = true)
        presenter.attachView(view)
    }

    @Test
    fun `get comment should be triggered`() {
        every {
            getKolCommentsUseCase.execute(any(), any())
        } answers {}

        presenter.getCommentFirstTime(
            anyLong()
        )

        verify {
            view.showLoading()
            getKolCommentsUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get more comment should be triggered`() {
        every {
            getKolCommentsUseCase.execute(any(), any())
        } answers {}

        presenter.loadMoreComments(
            anyLong()
        )

        verify {
            getKolCommentsUseCase.execute(any(), any())
        }
    }

    @Test
    fun `if comment is valid then send comment should be triggered`() {
        every {
            sendKolCommentUseCase.execute(any(), any())
        } answers {}

        presenter.sendComment(
            anyLong(),
            "test comment"
        )

        verify {
            view.showProgressDialog()
            view.disableSendComment()
            sendKolCommentUseCase.execute(any(), any())
        }
    }

    @Test
    fun `if comment is not valid then send comment should not be triggered`() {
        val method = presenter.javaClass.getDeclaredMethod("isValid", String::class.java)
        method.isAccessible = true

        presenter.sendComment(anyLong(), "")

        val result = method.invoke(presenter, "") as Boolean
        Assert.assertFalse(result)
    }

    @Test
    fun `update cursor should not update the cursor`() {
        val mockCursor = "cursor"

        presenter.updateCursor("cursor")

        val field = presenter.javaClass.getDeclaredField(mockCursor)
        field.isAccessible = true

        val fieldValue = field.get(presenter)

        Assert.assertEquals(mockCursor, fieldValue)
    }

    @Test
    fun `delete comment should be triggered`() {
        every {
            deleteKolCommentUseCase.execute(any(), any())
        } answers {}

        presenter.deleteComment(
            "1",
            anyInt()
        )

        verify {
            view.showProgressDialog()
            deleteKolCommentUseCase.execute(any(), any())
        }
    }

    @Test
    fun `send report should be triggered`() {
        every {
            sendReportUseCase.execute(any(), any())
        } answers {}

        presenter.sendReport(
            anyInt(),
            anyString(),
            anyString(),
            anyString()
        )

        verify {
            sendReportUseCase.execute(any(), any())
        }
    }

    @Test
    fun `if keyword is not empty then get mentionable user should be triggered`() {
        every {
            getMentionableUserUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<MentionableUserModel?>?>>().onCompleted()
            secondArg<Subscriber<List<MentionableUserModel?>?>>().onNext(emptyList())
        }

        presenter.getMentionableUserByKeyword("keyword")

        verify {
            getMentionableUserUseCase.unsubscribe()
            getMentionableUserUseCase.execute(any(), any())
        }
    }

    @Test
    fun `if keyword is null then get mentionable user should not be triggered`() {
        presenter.getMentionableUserByKeyword(null)

        verify(exactly = 0) {
            getMentionableUserUseCase.unsubscribe()
            getMentionableUserUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be triggered`() {
        presenter.detachView()

        verify {
            getKolCommentsUseCase.unsubscribe()
            sendKolCommentUseCase.unsubscribe()
            getMentionableUserUseCase.unsubscribe()
            sendReportUseCase.unsubscribe()
        }
    }
}

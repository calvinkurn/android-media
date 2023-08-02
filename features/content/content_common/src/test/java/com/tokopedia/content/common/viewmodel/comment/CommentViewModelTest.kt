package com.tokopedia.content.common.viewmodel.comment

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.comment.CommentAction
import com.tokopedia.content.common.comment.CommentEvent
import com.tokopedia.content.common.comment.CommentException
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.uimodel.CommentParam
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.UserType
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.content.common.util.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 21/03/23
 */
@ExperimentalStdlibApi
class CommentViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val mockRepo: ContentCommentRepository = mockk(relaxed = true)

    private val helper = CommentHelper()

    @Test
    fun `initial fetch when init vm, call gql`() {
        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {}
        robot.use {
            coVerify { mockRepo.getComments(any(), any(), any()) }
        }
    }

    @Test
    fun `open applink action - emit open applink event `() {
        val appLink = ApplinkConst.LOGIN
        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {}
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.OpenAppLinkAction(appLink))
            }
            event.last().assertEqualTo(CommentEvent.OpenAppLink(appLink))
        }
    }

    @Test
    fun `open report logged in, emit open report event`() {
        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.RequestReportAction)
            }
            event.last().assertEqualTo(CommentEvent.OpenReportEvent)
        }
    }

    @Test
    fun `open report non log in, open login`() {
        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(false)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.RequestReportAction)
            }
            event.last().assertEqualTo(CommentEvent.OpenAppLink(ApplinkConst.LOGIN))
        }
    }

    @Test
    fun `report comment, success from gql, open success report sheet`() {
        coEvery { mockRepo.reportComment(any()) } returns true

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.ReportComment(
                        FeedComplaintSubmitReportUseCase.Param(
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }
            event.last().assertEqualTo(CommentEvent.ReportSuccess)
        }
    }

    @Test
    fun `report comment, failed from gql, emit show error toaster`() {
        coEvery { mockRepo.reportComment(any()) } returns false

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.ReportComment(
                        FeedComplaintSubmitReportUseCase.Param(
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }
            event.last().assertType<CommentEvent.ShowErrorToaster> {}
        }
    }

    @Test
    fun `report comment, failed to call gql, emit show error toaster`() {
        val exception = MessageErrorException("Hi")
        coEvery { mockRepo.reportComment(any()) } throws exception

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.ReportComment(
                        FeedComplaintSubmitReportUseCase.Param(
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }
            event.last().assertType<CommentEvent.ShowErrorToaster> {
                it.message.assertEqualTo(exception)
            }
        }
    }

    @Test
    fun `click comment text box, logged in, emit autotype`() {
        val item = helper.buildItemComment()

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.EditTextClicked(item)
                )
            }
            event.last().assertEqualTo(CommentEvent.AutoType(item))
        }
    }

    @Test
    fun `click comment text box, non log in, open login`() {
        val item = helper.buildItemComment()

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(false)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.EditTextClicked(item)
                )
            }
            event.last().assertEqualTo(CommentEvent.OpenAppLink(ApplinkConst.LOGIN))
        }
    }

    @Test
    fun `permanent remove delete comment is success from gql`() {
        coEvery { mockRepo.deleteComment(any()) } returns true

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.PermanentRemoveComment
                )
            }
            coVerify { mockRepo.deleteComment(any()) }
            event.assertEmpty()
        }
    }

    @Test
    fun `permanent remove delete comment is error from gql`() {
        coEvery { mockRepo.deleteComment(any()) } throws MessageErrorException()

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.PermanentRemoveComment
                )
            }
            coVerify { mockRepo.deleteComment(any()) }
            event.last().assertType<CommentEvent.ShowErrorToaster> {
                it.message.message?.assertEqualTo(CommentException.createDeleteFailed().message.orEmpty())
            }
        }
    }

    @Test
    fun `permanent remove delete comment is error from gql, undo from list`() {
        val item = helper.buildItemComment(id = "1111")
        val listOfComment = listOf(helper.buildItemComment(), item)
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(
            list = listOfComment
        )
        coEvery { mockRepo.deleteComment(any()) } throws MessageErrorException()

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val comment = it.recordComments {
                submitAction(
                    CommentAction.SelectComment(item.id)
                )
                submitAction(
                    CommentAction.PermanentRemoveComment
                )
            }
            comment.list.assertType<List<CommentUiModel>> {
                it.contains(item).assertTrue()
            }
            coVerify { mockRepo.deleteComment(any()) }
            val selected = it.vm
                .getPrivateField<MutableStateFlow<Pair<CommentUiModel.Item, Int>>>("_selectedComment")
            selected.value.first.assertEqualTo(item)
            selected.value.second.assertEqualTo(listOfComment.indexOf(item))
        }
    }

    @Test
    fun `delete comment from list then undo from list`() {
        val item = helper.buildItemComment(id = "1111")
        val listOfComment = listOf(helper.buildItemComment(), item)
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(
            list = listOfComment
        )

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val comment = it.recordComments {
                submitAction(
                    CommentAction.SelectComment(item.id)
                )
                submitAction(
                    CommentAction.DeleteComment(isFromToaster = true)
                )
            }
            comment.list.assertType<List<CommentUiModel>> {
                it.contains(item).assertTrue()
            }
            coVerify { mockRepo.deleteComment(any()) wasNot called }
            val selected = it.vm
                .getPrivateField<MutableStateFlow<Pair<CommentUiModel.Item, Int>>>("_selectedComment")
            selected.value.first.assertEqualTo(item)
            selected.value.second.assertEqualTo(listOfComment.indexOf(item))
        }
    }

    @Test
    fun `delete comment, make sure it doesnt include in list - not from undo`() {
        val item = helper.buildItemComment(id = "1111")
        val listOfComment = listOf(helper.buildItemComment(), item, helper.buildItemComment(id = "1"))
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(list = listOfComment)

        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val comment = it.recordComments {
                submitAction(
                    CommentAction.SelectComment(item.id)
                )
                submitAction(
                    CommentAction.DeleteComment(false)
                )
            }
            comment.list.assertType<List<CommentUiModel>> {
                it.contains(item).assertFalse()
            }
            coVerify { mockRepo.deleteComment(any()) wasNot called }
            val selected = it.vm
                .getPrivateField<MutableStateFlow<Pair<CommentUiModel.Item, Int>>>("_selectedComment")
            selected.value.first.assertEqualTo(item)
            selected.value.second.assertEqualTo(listOfComment.indexOf(item))
        }
    }

    @Test
    fun `delete comment, show success toaster - not from undo`() {
        val listOfComment = listOf(helper.buildItemComment(), helper.buildItemComment(id = "44"), helper.buildItemComment(id = "1"))
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(list = listOfComment)

        val item = helper.buildItemComment()
        val robot = createCommentRobot(repository = mockRepo, dispatchers = testDispatcher) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(
                    CommentAction.SelectComment(item.id)
                )
                submitAction(
                    CommentAction.DeleteComment(false)
                )
            }
            event.last().assertEqualTo(CommentEvent.ShowSuccessToaster())
            coVerify { mockRepo.deleteComment(any()) wasNot called }
            val selected = it.vm
                .getPrivateField<MutableStateFlow<Pair<CommentUiModel.Item, Int>>>("_selectedComment")
            selected.value.first.assertEqualTo(item)
            selected.value.second.assertEqualTo(listOfComment.indexOf(item))
        }
    }

    @Test
    fun `send reply in non-login state, open login`() {
        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(false)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.ReplyComment("", CommentType.Parent))
            }
            event.last().assertEqualTo(CommentEvent.OpenAppLink(ApplinkConst.LOGIN))
        }
    }

    @Test
    fun `send reply in login state, with link non-tokopedia link would return error`() {
        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.ReplyComment("www.shopee.com beli disni aj", CommentType.Parent))
            }
            event.first().assertEqualTo(CommentEvent.HideKeyboard)
            event.last().assertType<CommentEvent.ShowErrorToaster> {
                it.message.message?.assertEqualTo(CommentException.createLinkNotAllowed().message.orEmpty())
            }
        }
    }

    @Test
    fun `send reply in login state, with link tokopedia link would return success - parent`() {
        val item1 = helper.buildItemComment(id = "1")
        val item2 = helper.buildItemComment(id = "2")
        val item3 = helper.buildItemComment(id = "3")
        val item4 = helper.buildItemComment()

        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(
            list = listOf(item1, item2, item3)
        )
        coEvery { mockRepo.replyComment(any(), any(), any(), any()) } returns item4

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.ReplyComment("www.tokopedia.com beli disni aj", CommentType.Parent))
            }
            event.first().assertEqualTo(CommentEvent.HideKeyboard)
            event.last().assertEqualTo(CommentEvent.ReplySuccess(0)) // parent

            val comments = it.recordComments {
                submitAction(CommentAction.ReplyComment("www.tokopedia.com beli disni aj", CommentType.Parent))
            }
            comments.list.first().assertEqualTo(item4)
        }
    }

    @Test
    fun `send reply in login state, with link tokopedia link would return success - child`() {
        val item1 = helper.buildItemComment(id = "1")
        val item2 = helper.buildItemComment(id = "2")
        val item3 = helper.buildItemComment(id = "3")
        val item4 = helper.buildItemComment(commentType = CommentType.Child("2"))

        val comments = helper.buildCommentWidget(
            list = listOf(item1, item2, item3)
        )
        coEvery { mockRepo.getComments(any(), any(), any()) } returns comments
        coEvery { mockRepo.replyComment(any(), any(), any(), any()) } returns item4

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.ReplyComment("www.tokopedia.com beli disni aj", CommentType.Child("2")))
            }
            event.first().assertEqualTo(CommentEvent.HideKeyboard)
            val index = comments.list
                .indexOfFirst { item -> item is CommentUiModel.Item && item.id == item4.commentType.parentId } + 1 // under parent
            event.last().assertEqualTo(CommentEvent.ReplySuccess(index)) // child

            val comment = it.recordComments {
                submitAction(CommentAction.ReplyComment("www.tokopedia.com beli disni aj", CommentType.Parent))
            }
            comment.list[index].assertEqualTo(item4)
        }
    }

    @Test
    fun `send reply in login state, failed from gql`() {
        val exception = MessageErrorException()

        coEvery { mockRepo.replyComment(any(), any(), any(), any()) } throws  exception

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }
        robot.use {
            val event = it.recordEvent {
                submitAction(CommentAction.ReplyComment("www.tokopedia.com beli disni aj", CommentType.Child("2")))
            }
            event.first().assertType<CommentEvent.HideKeyboard> {  }
            event.last().assertType<CommentEvent.ShowErrorToaster> {  }
        }
    }

    @Test
    fun `if commenter type from gql return shop, then its creator`() {
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(commenterType = UserType.Shop)
        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }
        robot.use {
            it.vm.isCreator.assertTrue()
        }
    }

    @Test
    fun `if commenter type from gql return not shop, then it is not creator`() {
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget(commenterType = UserType.People)
        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }
        robot.use {
            it.vm.isCreator.assertFalse()
        }
    }

    @Test
    fun `init view model - change query based on return, if there's any cursor`() {
        val expected = helper.buildCommentWidget(cursor = "2773ubehb")
        coEvery { mockRepo.getComments(any(), any(), any()) } returns expected

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val q = it.recordQueries {}
            q.size.assertEqualTo(1) //only called once
            q.last().commentType.assertEqualTo(expected.commentType)
            q.last().lastParentCursor.assertEqualTo(expected.cursor)
            q.last().needToRefresh.assertFalse() // after getting data make sure not to refresh unless we force to refresh / want to fetch
        }
    }

    @Test
    fun `init view model - change query based on return, if there's no cursor`() {
        val expected = helper.buildCommentWidget()
        coEvery { mockRepo.getComments(any(), any(), any()) } returns expected

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val q = it.recordQueries {}
            q.size.assertEqualTo(1) //only called once
            q.last().commentType.assertEqualTo(expected.commentType)
            q.last().lastParentCursor.assertEqualTo(expected.cursor)
            q.last().needToRefresh.assertFalse()
        }
    }

    @Test
    fun `dismiss bottom sheet - reset query`() {
        val initialParam = helper.buildCommentWidget(
            cursor = "uBgdk",
            commentType = CommentType.Child("1")
        )
        val expected = CommentParam() //resetting to default
        coEvery { mockRepo.getComments(any(), any(), any()) } returns initialParam

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val q = it.recordQueries {
                submitAction(CommentAction.DismissComment)
            }
            q.last().commentType.assertEqualTo(expected.commentType)
            q.last().lastParentCursor.assertEqualTo(expected.lastParentCursor)
            q.last().lastChildCursor.assertEqualTo(expected.lastChildCursor)
            q.last().needToRefresh.assertFalse() //dont refresh

            q.assertNotEqualTo(initialParam)
        }
    }

    @Test
    fun `refresh bottom sheet - reset query`() {
        val expected = CommentParam() //resetting to default
        coEvery { mockRepo.getComments(any(), any(), any()) } returns helper.buildCommentWidget()

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val q = it.recordQueries {
                submitAction(CommentAction.RefreshComment)
            }
            q.last().commentType.assertEqualTo(expected.commentType)
            q.last().lastParentCursor.assertEqualTo(expected.lastParentCursor)
            q.last().lastChildCursor.assertEqualTo(expected.lastChildCursor)
            q.last().needToRefresh.assertFalse() //after get comments
        }
    }

    @Test
    fun `load next page from parent - success` () {
        val expected = helper.buildCommentWidget(cursor = "KgBDVYB8", commentType = CommentType.Parent)
        coEvery { mockRepo.getComments(any(), any(), any()) } returns expected

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val result = it.recordQueryAndComment {
                submitAction(CommentAction.LoadNextPage(CommentType.Parent))
            }
            result.first.lastParentCursor.assertEqualTo(expected.cursor)
            result.first.lastChildCursor.assertNotEqualTo(expected.cursor)
            result.first.needToRefresh.assertFalse()
            result.first.commentType.assertEqualTo(expected.commentType)

            result.second.assertEqualTo(expected)
        }
    }

    @Test
    fun `load next page from parent - failed` () {
        val exception = MessageErrorException()
        coEvery { mockRepo.getComments(any(), any(), any()) } throws exception

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val result = it.recordQueryAndComment {
                submitAction(CommentAction.LoadNextPage(CommentType.Parent))
            }
            result.first.needToRefresh.assertFalse()

            result.second.state.assertEqualTo(ResultState.Fail(exception))
        }
    }

    @Test
    fun `load next page from parent - content same with prev - use current list` () {
        val expected = helper.buildCommentWidget(
            list = helper.buildCommentList(
                listOf(
                    helper.buildItemComment(id = "1"), helper.buildItemComment(id = "2"), helper.buildItemComment(id = "3")
                )
            )
        )

        coEvery { mockRepo.getComments(any(), any(), any()) } returns expected

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val result = it.recordQueryAndComment {
                submitAction(CommentAction.LoadNextPage(CommentType.Parent))
            }
            result.first.needToRefresh.assertFalse()

            result.second.state.assertEqualTo(ResultState.Success)
            result.second.list.size.assertEqualTo(expected.list.size)
            result.second.list.assertEqualTo(expected.list)
        }
    }

    @Test
    fun `load next page from parent - content not same with prev - merge two list` () {
        val expected = helper.buildCommentWidget(
            list = helper.buildCommentList(
                listOf(
                    helper.buildItemComment(id = "1"), helper.buildItemComment(id = "2"), helper.buildItemComment(id = "3")
                )
            )
        )

        val new = helper.buildCommentWidget(
            list = helper.buildCommentList(
                listOf(
                    helper.buildItemComment(id = "11"), helper.buildItemComment(id = "12"), helper.buildItemComment(id = "13")
                )
            )
        )

        coEvery { mockRepo.getComments(any(), any(), any()) } returns expected

        val robot = createCommentRobot(dispatchers = testDispatcher, repository = mockRepo) {
            setLogin(true)
        }

        robot.use {
            val result = it.recordQueryAndComment {
                submitAction(CommentAction.LoadNextPage(CommentType.Parent))
                coEvery { mockRepo.getComments(any(), any(), any()) } returns new
                submitAction(CommentAction.LoadNextPage(CommentType.Parent))
            }
            result.first.needToRefresh.assertFalse()

            result.second.state.assertEqualTo(ResultState.Success)
            result.second.list.size.assertEqualTo(expected.list.size + new.list.size)
            result.second.list.assertEqualTo(expected.list + new.list)
        }
    }
}

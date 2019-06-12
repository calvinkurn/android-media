package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.kolcommon.model.FollowResponseModel
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.result.domain.usecase.TestErrorUseCase
import com.tokopedia.search.result.domain.usecase.TestUseCase
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.usecase.UseCase
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as mockitoWhen

class ProfileListPresenterTest {

    private abstract class MockSearchProfileMapper : Mapper<SearchProfileModel, ProfileListViewModel>

    private val profileListView = mock(ProfileListSectionContract.View::class.java)
    private val profileListPresenter = ProfileListPresenter()
    private val followResponseModelSuccess = FollowResponseModel(true, "")
    private val followResponseModelErrorMessage = "Mock error message toggle follow"
    private val followResponseModelError = FollowResponseModel(false, followResponseModelErrorMessage)
    private val searchProfileModel = SearchProfileModel()
    private val profileDataList = createProfileDataList()
    private val profileListViewModel = ProfileListViewModel(profileDataList, false, 0)
    private val profileListViewModelMapper = mock(MockSearchProfileMapper::class.java)
    private val exception = Exception("Mock exception, should be handled in Subscriber onError")

    private fun createProfileDataList() : List<ProfileViewModel> {
        val profileDataList = mutableListOf<ProfileViewModel>()

        for(i in 0..SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE) {
            profileDataList.add(
                ProfileViewModel(
                    "",
                    "",
                    "",
                    "",
                    false,
                    false,
                    false,
                    0,
                    0)
            )
        }

        return profileDataList
    }

    private fun profileListPresenterInjectDependencies(
        searchProfileUseCase: UseCase<SearchProfileModel>,
        followKolUseCase: UseCase<FollowResponseModel>
    ) {
        profileListPresenter.attachView(profileListView)
        profileListPresenter.followKolPostGqlUseCase = followKolUseCase
        profileListPresenter.searchProfileListUseCase = searchProfileUseCase
        profileListPresenter.profileListViewModelMapper = profileListViewModelMapper
    }

    @Test
    fun requestProfileListSuccess_RenderViewSuccess() {
        val page = 1

        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(searchProfileModel),
            TestUseCase<FollowResponseModel>(null)
        )

        mockitoWhen(profileListViewModelMapper.convert(searchProfileModel)).thenReturn(profileListViewModel)

        profileListPresenter.requestProfileListData("", page)

        verifyProfileListDataPosition(page)
        verify(profileListView).onSuccessGetProfileListData(profileListViewModel)
    }

    private fun verifyProfileListDataPosition(page : Int) {
        var position = ((page - 1) * SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE) + 1

        for(profileData in profileDataList) {
            val expectedPosition = position++
            val actualPosition = profileData.position
            assert(actualPosition == expectedPosition) {
                "Verify Profile List Data Position failed. Expected $expectedPosition, Actual $actualPosition"
            }
        }
    }

    @Test
    fun requestProfileListReturnedNull_RenderViewSuccess() {
        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(null),
            TestUseCase<FollowResponseModel>(null)
        )

        profileListPresenter.requestProfileListData("", 0)

        verify(profileListView).onErrorGetProfileListData()
    }

    @Test
    fun requestProfileListError_RenderViewSuccess() {
        profileListPresenterInjectDependencies(
            TestErrorUseCase<SearchProfileModel, Exception>(exception),
            TestUseCase<FollowResponseModel>(null)
        )

        profileListPresenter.requestProfileListData("", 0)

        verify(profileListView).onErrorGetProfileListData()
    }

    @Test
    fun requestProfileListNullError_RenderViewSuccess() {
        profileListPresenterInjectDependencies(
            TestErrorUseCase<SearchProfileModel, Exception>(null),
            TestUseCase<FollowResponseModel>(null)
        )

        profileListPresenter.requestProfileListData("", 0)

        verify(profileListView).onErrorGetProfileListData()
    }

    @Test
    fun handleFollowActionSuccess_RenderViewSuccessToggle() {
        val followStatus = true

        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(null),
            TestUseCase<FollowResponseModel>(followResponseModelSuccess)
        )

        profileListPresenter.handleFollowAction(0, 0, followStatus)

        verify(profileListView).onSuccessToggleFollow(0, !followStatus)
    }

    @Test
    fun handleFollowActionNotSuccess_RenderViewErrorToggleFollowWithMessage() {
        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(null),
            TestUseCase<FollowResponseModel>(followResponseModelError)
        )

        profileListPresenter.handleFollowAction(0, 0, true)

        verify(profileListView).onErrorToggleFollow(followResponseModelErrorMessage)
    }

    @Test
    fun handleFollowActionNull_RenderViewErrorToggleFollow() {
        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(null),
            TestUseCase<FollowResponseModel>(null)
        )

        profileListPresenter.handleFollowAction(0, 0, true)

        verify(profileListView).onErrorToggleFollow()
    }

    @Test
    fun handleFollowActionError_RenderViewErrorToggleFollow() {
        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(null),
            TestErrorUseCase<FollowResponseModel, Exception>(exception)
        )

        profileListPresenter.handleFollowAction(0, 0, true)

        verify(profileListView).onErrorToggleFollow()
    }

    @Test
    fun handleFollowActionNullError_RenderViewErrorToggleFollow() {
        profileListPresenterInjectDependencies(
            TestUseCase<SearchProfileModel>(null),
            TestErrorUseCase<FollowResponseModel, Exception>(null)
        )

        profileListPresenter.handleFollowAction(0, 0, true)

        verify(profileListView).onErrorToggleFollow()
    }
}
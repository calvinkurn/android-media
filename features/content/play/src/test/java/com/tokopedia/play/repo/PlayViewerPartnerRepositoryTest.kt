package com.tokopedia.play.repo

import com.tokopedia.play.data.*
import com.tokopedia.play.data.repository.PlayViewerPartnerRepositoryImpl
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author by astidhiyaa on 09/03/22
 */
@ExperimentalCoroutinesApi
class PlayViewerPartnerRepositoryTest {
    lateinit var partnerRepo : PlayViewerPartnerRepository

    private val getPartnerInfoUseCase: GetPartnerInfoUseCase = mockk(relaxed = true)
    private val postFollowPartnerUseCase: PostFollowPartnerUseCase = mockk(relaxed = true)
    private val getProfileInfoUseCase: GetProfileInfoUseCase = mockk(relaxed = true)
    private val getFollowingKOLUseCase: GetFollowingKOLUseCase = mockk(relaxed = true)
    private val postFollowKolUseCase: PostFollowKolUseCase = mockk(relaxed = true)
    private val postUnfollowKolUseCase: PostUnfollowKolUseCase = mockk(relaxed = true)

    lateinit var playUiModelMapper: PlayUiModelMapper

    private val testDispatcher = CoroutineTestDispatchers

    private val partnerId = 22L

    private val encUserId = "adfhajg761"

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        playUiModelMapper = PlayUiModelMapper(
            productTagMapper = mockk(relaxed = true),
            merchantVoucherMapper = mockk(relaxed = true),
            chatMapper = mockk(relaxed = true),
            channelStatusMapper = mockk(relaxed = true),
            channelInteractiveMapper = mockk(relaxed = true),
            interactiveLeaderboardMapper = mockk(relaxed = true),
            cartMapper = mockk(relaxed = true),
            playUserReportMapper = mockk(relaxed = true)
        )

        partnerRepo = PlayViewerPartnerRepositoryImpl(
            getPartnerInfoUseCase, postFollowPartnerUseCase, getProfileInfoUseCase, getFollowingKOLUseCase, postFollowKolUseCase, postUnfollowKolUseCase, playUiModelMapper, testDispatcher
        )
    }


    @Test
    fun `when user has followed shop return true`(){
        //alreadyFavorited = 1 means success
        runBlockingTest {
            val response = ShopInfo(
                favoriteData = ShopInfo.FavoriteData(totalFavorite = 28, alreadyFavorited = 1)
            )

            coEvery { getPartnerInfoUseCase.executeOnBackground() } returns response

            val result = partnerRepo.getIsFollowingPartner(
               partnerId = partnerId
            )

            coVerify { getPartnerInfoUseCase.executeOnBackground() }

            result.assertTrue()
        }
    }

    @Test
    fun `when user has not followed shop return false`(){
        runBlockingTest {
            val response = ShopInfo(
                favoriteData = ShopInfo.FavoriteData(totalFavorite = 28, alreadyFavorited = 0)
            )

            coEvery { getPartnerInfoUseCase.executeOnBackground() } returns response

            val result = partnerRepo.getIsFollowingPartner(
                partnerId = partnerId
            )

            coVerify { getPartnerInfoUseCase.executeOnBackground() }

            result.assertFalse()
        }
    }

    @Test
    fun `when user follow shop return true`(){
        runBlockingTest {
            //this use case returns isFollowing value, if it's a true then the user has followed the shop
            coEvery { postFollowPartnerUseCase.executeOnBackground() } returns true

            val result = partnerRepo.postFollowStatus(
                shopId = partnerId.toString(),
                followAction = PartnerFollowAction.Follow
            )

            coVerify { postFollowPartnerUseCase.executeOnBackground() }

            result.assertTrue()
        }
    }

    @Test
    fun `when user unfollow shop return false`(){
        runBlockingTest {
            //this use case returns isFollowing value, if it's a false then the user has unfollowed the shop
            coEvery { postFollowPartnerUseCase.executeOnBackground() } returns false

            val result = partnerRepo.postFollowStatus(
                shopId = partnerId.toString(),
                followAction = PartnerFollowAction.UnFollow
            )

            coVerify { postFollowPartnerUseCase.executeOnBackground() }

            result.assertFalse()
        }
    }

    @Test
    fun `when user want to enter live room and the streamer is buyer fetch enc userid`(){
        runBlockingTest {
            val response = ProfileHeader.Response(
                response = ProfileHeader(
                    profileInfo = ProfileHeader.ProfileInfo(encryptedUserId = encUserId)
                )
            )

            coEvery { getProfileInfoUseCase.executeOnBackground() } returns response

            val result = partnerRepo.getProfileHeader(
                kolId = partnerId.toString()
            )

            coVerify { getProfileInfoUseCase.executeOnBackground() }

            result.assertEqualTo(response.response.profileInfo.encryptedUserId)
        }
    }

    @Test
    fun `when user has followed buyer return true`(){
        runBlockingTest {
            val response = FollowKOL.Response(
                response = FollowKOL(
                    followedKOLInfo = listOf(
                        FollowKOL.FollowedKOL(status = true)
                    )
                )
            )

            coEvery { getFollowingKOLUseCase.executeOnBackground() } returns response

            val result = partnerRepo.getFollowingKOL(
                followedKol = partnerId.toString()
            )

            coVerify { getFollowingKOLUseCase.executeOnBackground() }

            result.assertTrue()
        }
    }

    @Test
    fun `when user has not followed buyer return false`(){
        runBlockingTest {
            val response = FollowKOL.Response(
                response = FollowKOL(
                    followedKOLInfo = listOf(
                        FollowKOL.FollowedKOL(status = false)
                    )
                )
            )

            coEvery { getFollowingKOLUseCase.executeOnBackground() } returns response

            val result = partnerRepo.getFollowingKOL(
                followedKol = partnerId.toString()
            )

            coVerify { getFollowingKOLUseCase.executeOnBackground() }

            result.assertFalse()
        }
    }
    @Test
    fun `when user follow buyer return true`(){
        runBlockingTest {
            //this use case returns error message, if it's empty then its true / the op is success
            val response = KOLFollowStatus(
                followedKOLInfo = FollowInfo(
                    errorCode = "",
                    message = emptyList()
                )
            )
            coEvery { postFollowKolUseCase.executeOnBackground() } returns response

            val result = partnerRepo.postFollowKol(
                followedKol = encUserId,
                followAction = PartnerFollowAction.Follow
            )

            coVerify { postFollowKolUseCase.executeOnBackground() }

            result.assertTrue()
        }
    }

    @Test
    fun `when user unfollow buyer return true`(){
        runBlockingTest {
            //this use case returns the operation result value, if isSuccess equals to 1 then it's true
            val response = KOLUnFollowStatus(
                unFollowedKOLInfo = FollowInfo(
                    data = FollowInfo.Data(isSuccess = 1)
                )
            )
            coEvery { postUnfollowKolUseCase.executeOnBackground() } returns response

            val result = partnerRepo.postFollowKol(
                followedKol = encUserId,
                followAction = PartnerFollowAction.UnFollow
            )

            coVerify { postUnfollowKolUseCase.executeOnBackground() }

            result.assertTrue()
        }
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
}
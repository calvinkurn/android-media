package com.tokopedia.play.repo

import com.tokopedia.play.data.*
import com.tokopedia.play.data.repository.PlayViewerPartnerRepositoryImpl
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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

    private val getSellerInfoUsecase: GetSellerInfoUsecase = mockk(relaxed = true)
    private val postFollowPartnerUseCase: PostFollowPartnerUseCase = mockk(relaxed = true)
    private val getFollowingKOLUseCase: GetFollowingKOLUseCase = mockk(relaxed = true)
    private val postFollowKolUseCase: PostFollowKolUseCase = mockk(relaxed = true)
    private val postUnfollowKolUseCase: PostUnfollowKolUseCase = mockk(relaxed = true)

    private val classBuilder = ClassBuilder()
    private val mapper = classBuilder.getPlayUiModelMapper()

    private val testDispatcher = CoroutineTestDispatchers

    private val partnerId = 22L

    private val encUserId = "adfhajg761"

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        partnerRepo = PlayViewerPartnerRepositoryImpl(
            getSellerInfoUsecase, postFollowPartnerUseCase, getFollowingKOLUseCase, postFollowKolUseCase, postUnfollowKolUseCase, mapper, testDispatcher
        )
    }


    @Test
    fun `when user has followed shop return true`(){
        //alreadyFavorited = 1 means success
        runTest {
            val response = ShopInfo(
                favoriteData = ShopInfo.FavoriteData(totalFavorite = 28, alreadyFavorited = 1)
            )

            coEvery { getSellerInfoUsecase.executeOnBackground() } returns response

            val result = partnerRepo.getIsFollowingPartner(
               partnerId = partnerId
            )

            coVerify { getSellerInfoUsecase.executeOnBackground() }

            result.assertTrue()
        }
    }

    @Test
    fun `when user has not followed shop return false`(){
        runTest {
            val response = ShopInfo(
                favoriteData = ShopInfo.FavoriteData(totalFavorite = 28, alreadyFavorited = 0)
            )

            coEvery { getSellerInfoUsecase.executeOnBackground() } returns response

            val result = partnerRepo.getIsFollowingPartner(
                partnerId = partnerId
            )

            coVerify { getSellerInfoUsecase.executeOnBackground() }

            result.assertFalse()
        }
    }

    @Test
    fun `when user follow shop return true`(){
        runTest {
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
        runTest {
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
    fun `when user enter live room and the streamer is buyer fetch enc userid`(){
        runTest {
            val response = FollowKOL.Response(
                response = FollowKOL(
                    followedKOLInfo = listOf(
                        FollowKOL.FollowedKOL(encryptedUserId = encUserId)
                    )
                )
            )

            coEvery { getFollowingKOLUseCase.executeOnBackground() } returns response

            val result = partnerRepo.getFollowingKOL(
                followedKol = partnerId.toString()
            )

            coVerify { getFollowingKOLUseCase.executeOnBackground() }

            result.second.assertEqualTo(encUserId)
        }
    }

    @Test
    fun `when user has followed buyer return true`(){
        runTest {
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

            result.first.assertTrue()
        }
    }

    @Test
    fun `when user has not followed buyer return false`(){
        runTest {
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

            result.first.assertFalse()
        }
    }
    @Test
    fun `when user follow buyer return true`(){
        runTest {
            //this use case returns error message, if it's empty then its true / the op is success
            val response = KOLFollowStatus(
                followedKOLInfo = FollowInfo(
                    errorCode = "",
                    messages = emptyList()
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
        runTest {
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

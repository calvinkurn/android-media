package com.tokopedia.loginregister.shopcreation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.loginregister.FileUtil
import com.tokopedia.loginregister.shopcreation.data.GetUserProfileCompletionPojo
import com.tokopedia.loginregister.shopcreation.data.RegisterCheckData
import com.tokopedia.loginregister.shopcreation.data.RegisterCheckPojo
import com.tokopedia.loginregister.shopcreation.data.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.data.ShopInfoPojo
import com.tokopedia.loginregister.shopcreation.data.UserProfileCompletionData
import com.tokopedia.loginregister.shopcreation.data.UserProfileUpdate
import com.tokopedia.loginregister.shopcreation.data.UserProfileUpdatePojo
import com.tokopedia.loginregister.shopcreation.data.UserProfileValidate
import com.tokopedia.loginregister.shopcreation.data.UserProfileValidatePojo
import com.tokopedia.loginregister.shopcreation.domain.GetUserProfileCompletionUseCase
import com.tokopedia.loginregister.shopcreation.domain.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.ShopInfoUseCase
import com.tokopedia.loginregister.shopcreation.domain.UpdateUserProfileUseCase
import com.tokopedia.loginregister.shopcreation.domain.ValidateUserProfileUseCase
import com.tokopedia.loginregister.shopcreation.view.ShopCreationViewModel
import com.tokopedia.sessioncommon.data.Error
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

/**
 * Created by Ade Fulki on 18/01/21.
 */

@ExperimentalCoroutinesApi
class ShopCreationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var registerUseCase: RegisterUseCase

    @RelaxedMockK
    lateinit var registerCheckUseCase: RegisterCheckUseCase

    @RelaxedMockK
    lateinit var getUserProfileCompletionUseCase: GetUserProfileCompletionUseCase

    @RelaxedMockK
    lateinit var validateUserProfileUseCase: ValidateUserProfileUseCase

    @RelaxedMockK
    lateinit var updateUserProfileUseCase: UpdateUserProfileUseCase

    @RelaxedMockK
    lateinit var getProfileUseCase: GetProfileUseCase

    @RelaxedMockK
    lateinit var shopInfoUseCase: ShopInfoUseCase

    @RelaxedMockK
    lateinit var addNameObserver: Observer<Result<UserProfileUpdate>>

    @RelaxedMockK
    lateinit var addPhoneObserver: Observer<Result<UserProfileUpdate>>

    @RelaxedMockK
    lateinit var registerCheckObserver: Observer<Result<RegisterCheckData>>

    @RelaxedMockK
    lateinit var getShopInfoObserver: Observer<Result<ShopInfoByID>>

    @RelaxedMockK
    lateinit var registerPhoneAndNameObserver: Observer<Result<RegisterInfo>>

    @RelaxedMockK
    lateinit var getUserProfileObserver: Observer<Result<UserProfileCompletionData>>

    @RelaxedMockK
    lateinit var validateUserProfileObserver: Observer<Result<UserProfileValidate>>

    @RelaxedMockK
    lateinit var getUserInfoObserver: Observer<Result<ProfileInfo>>

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var graphqlResponse: GraphqlResponse

    private lateinit var viewmodel: ShopCreationViewModel
    private val mockValidateToken = "validateToken"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewmodel = ShopCreationViewModel(
                registerUseCase,
                registerCheckUseCase,
                getUserProfileCompletionUseCase,
                validateUserProfileUseCase,
                updateUserProfileUseCase,
                getProfileUseCase,
                shopInfoUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `Success add name`() {
        successAddNameResponse.data.isSuccess = 1
        successAddNameResponse.data.errors = listOf()

        viewmodel.addNameResponse.observeForever(addNameObserver)
        coEvery { updateUserProfileUseCase(any()) } returns successAddNameResponse

        viewmodel.addName("", mockValidateToken)

        verify { addNameObserver.onChanged(any<Success<UserProfileUpdate>>()) }
        assert(viewmodel.addNameResponse.value is Success)

        val result = viewmodel.addNameResponse.value as Success<UserProfileUpdate>
        assert(result.data == successAddNameResponse.data)
    }

    @Test
    fun `Success add name has errors`() {
        successAddNameResponse.data.errors = errors
        viewmodel.addNameResponse.observeForever(addNameObserver)
        coEvery { updateUserProfileUseCase(any()) } returns successAddNameResponse

        viewmodel.addName("", mockValidateToken)

        verify { addNameObserver.onChanged(any<Fail>()) }
        assert(viewmodel.addNameResponse.value is Fail)

        val result = viewmodel.addNameResponse.value as Fail
        assert(result.throwable.message == errors[0])
    }

    @Test
    fun `Success add name has other errors`() {
        val errors = listOf("")
        successAddNameResponse.data.errors = errors
        viewmodel.addNameResponse.observeForever(addNameObserver)
        coEvery { updateUserProfileUseCase(any()) } returns successAddNameResponse

        viewmodel.addName("", mockValidateToken)

        verify { addNameObserver.onChanged(any<Fail>()) }
        assert(viewmodel.addNameResponse.value is Fail)
        MatcherAssert.assertThat((viewmodel.addNameResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
    }

    @Test
    fun `Failed add name`() {
        viewmodel.addNameResponse.observeForever(addNameObserver)
        coEvery { updateUserProfileUseCase(any()) } coAnswers { throw throwable }

        viewmodel.addName("", mockValidateToken)

        verify { addNameObserver.onChanged(any<Fail>()) }
        assert(viewmodel.addNameResponse.value is Fail)

        val result = viewmodel.addNameResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success add phone`() {
        successAddPhoneResponse.data.isSuccess = 1
        successAddPhoneResponse.data.errors = listOf()

        viewmodel.addPhoneResponse.observeForever(addPhoneObserver)
        coEvery { updateUserProfileUseCase(any()) } returns successAddPhoneResponse

        viewmodel.addPhone("", mockValidateToken)

        verify { addPhoneObserver.onChanged(any<Success<UserProfileUpdate>>()) }
    }

    @Test
    fun `Success add phone has errors`() {
        successAddPhoneResponse.data.errors = errors

        viewmodel.addPhoneResponse.observeForever(addPhoneObserver)
        coEvery { updateUserProfileUseCase(any()) } returns successAddPhoneResponse

        viewmodel.addPhone("", mockValidateToken)

        verify { addPhoneObserver.onChanged(any<Fail>()) }
        assert(viewmodel.addPhoneResponse.value is Fail)

        val result = viewmodel.addPhoneResponse.value as Fail
        assert(result.throwable.message == errors[0])
    }

    @Test
    fun `Success add phone has other errors`() {
        val errors = listOf("")
        successAddPhoneResponse.data.errors = errors

        viewmodel.addPhoneResponse.observeForever(addPhoneObserver)
        coEvery { updateUserProfileUseCase(any()) } returns successAddPhoneResponse

        viewmodel.addPhone("", mockValidateToken)

        verify { addPhoneObserver.onChanged(any<Fail>()) }
        assert(viewmodel.addPhoneResponse.value is Fail)
        MatcherAssert.assertThat((viewmodel.addPhoneResponse.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
    }


    @Test
    fun `Failed add phone`() {
        viewmodel.addPhoneResponse.observeForever(addPhoneObserver)
        coEvery { updateUserProfileUseCase(any()) } coAnswers { throw throwable }

        viewmodel.addPhone("", mockValidateToken)

        verify { addPhoneObserver.onChanged(any<Fail>()) }
        assert(viewmodel.addPhoneResponse.value is Fail)

        val result = viewmodel.addPhoneResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success register check`() {
        successRegisterCheckResponse.data.errors = arrayListOf()

        viewmodel.registerCheckResponse.observeForever(registerCheckObserver)
        coEvery { registerCheckUseCase(any()) } returns successRegisterCheckResponse

        viewmodel.registerCheck("")

        verify { registerCheckObserver.onChanged(any<Success<RegisterCheckData>>()) }
        assert(viewmodel.registerCheckResponse.value is Success)

        val result = viewmodel.registerCheckResponse.value as Success<RegisterCheckData>
        assert(result.data == successRegisterCheckResponse.data)
    }

    @Test
    fun `Success register check has errors`() {
        successRegisterCheckResponse.data.errors = ArrayList(errors)

        viewmodel.registerCheckResponse.observeForever(registerCheckObserver)
        coEvery { registerCheckUseCase(any()) } returns successRegisterCheckResponse

        viewmodel.registerCheck("")

        verify { registerCheckObserver.onChanged(any<Fail>()) }
        assert(viewmodel.registerCheckResponse.value is Fail)
        MatcherAssert.assertThat((viewmodel.registerCheckResponse.value as Fail).throwable, CoreMatchers.instanceOf(com.tokopedia.network.exception.MessageErrorException::class.java))
    }

    @Test
    fun `Failed register check`() {
        viewmodel.registerCheckResponse.observeForever(registerCheckObserver)
        coEvery { registerCheckUseCase(any()) } coAnswers { throw throwable }

        viewmodel.registerCheck("")

        verify { registerCheckObserver.onChanged(any<Fail>()) }
        assert(viewmodel.registerCheckResponse.value is Fail)

        val result = viewmodel.registerCheckResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get shop info`() {
        viewmodel.getShopInfoResponse.observeForever(getShopInfoObserver)
        coEvery { shopInfoUseCase(any()) } returns successGetShopInfoResponse

        viewmodel.getShopInfo(0)

        verify { getShopInfoObserver.onChanged(any<Success<ShopInfoByID>>()) }
        assert(viewmodel.getShopInfoResponse.value is Success)

        val result = viewmodel.getShopInfoResponse.value as Success<ShopInfoByID>
        assert(result.data == successGetShopInfoResponse.data)
    }

    @Test
    fun `Failed get shop info`() {
        viewmodel.getShopInfoResponse.observeForever(getShopInfoObserver)
        coEvery { shopInfoUseCase(any()) } coAnswers { throw throwable }

        viewmodel.getShopInfo(0)

        verify { getShopInfoObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getShopInfoResponse.value is Fail)

        val result = viewmodel.getShopInfoResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success register phone and name`() {
        successRegisterPhoneAndNameResponse.register.accessToken = "abc123"
        successRegisterPhoneAndNameResponse.register.refreshToken = "abc123"
        successRegisterPhoneAndNameResponse.register.userId = "123"

        viewmodel.registerPhoneAndName.observeForever(registerPhoneAndNameObserver)

        every { graphqlResponse.getData<RegisterPojo>(any()) } returns successRegisterPhoneAndNameResponse

        coEvery { registerUseCase.execute(any(), any()) } coAnswers {
            secondArg<Subscriber<GraphqlResponse>>().onNext(graphqlResponse)
        }

        viewmodel.registerPhoneAndName("", "")

        verify { registerPhoneAndNameObserver.onChanged(any<Success<RegisterInfo>>()) }
    }

    @Test
    fun `Success register phone and name has errors`() {
        val errors = arrayListOf(Error("error", "msg"))
        successRegisterPhoneAndNameResponse.register.accessToken = ""
        successRegisterPhoneAndNameResponse.register.refreshToken = ""
        successRegisterPhoneAndNameResponse.register.userId = ""
        successRegisterPhoneAndNameResponse.register.errors = errors

        viewmodel.registerPhoneAndName.observeForever(registerPhoneAndNameObserver)

        every { graphqlResponse.getData<RegisterPojo>(any()) } returns successRegisterPhoneAndNameResponse

        coEvery { registerUseCase.execute(any(), any()) } coAnswers {
            secondArg<Subscriber<GraphqlResponse>>().onNext(graphqlResponse)
        }

        viewmodel.registerPhoneAndName("", "")

        verify { registerPhoneAndNameObserver.onChanged(any<Fail>()) }
        assert((viewmodel.registerPhoneAndName.value as Fail).throwable.message == "msg")
        MatcherAssert.assertThat((viewmodel.registerPhoneAndName.value as Fail).throwable, CoreMatchers.instanceOf(com.tokopedia.network.exception.MessageErrorException::class.java))
    }

    @Test
    fun `Success register phone and name has other errors`() {
        successRegisterPhoneAndNameResponse.register.accessToken = ""
        successRegisterPhoneAndNameResponse.register.refreshToken = ""
        successRegisterPhoneAndNameResponse.register.userId = ""
        successRegisterPhoneAndNameResponse.register.errors = arrayListOf()

        viewmodel.registerPhoneAndName.observeForever(registerPhoneAndNameObserver)

        every { graphqlResponse.getData<RegisterPojo>(any()) } returns successRegisterPhoneAndNameResponse

        coEvery { registerUseCase.execute(any(), any()) } coAnswers {
            secondArg<Subscriber<GraphqlResponse>>().onNext(graphqlResponse)
        }

        viewmodel.registerPhoneAndName("", "")

        verify { registerPhoneAndNameObserver.onChanged(any<Fail>()) }
        MatcherAssert.assertThat((viewmodel.registerPhoneAndName.value as Fail).throwable, CoreMatchers.instanceOf(RuntimeException::class.java))
    }

    @Test
    fun `Failed register phone and name`() {
        viewmodel.registerPhoneAndName.observeForever(registerPhoneAndNameObserver)
        coEvery { registerUseCase.execute(any(), any()) } coAnswers {
            secondArg<Subscriber<GraphqlResponse>>().onError(throwable)
        }

        viewmodel.registerPhoneAndName("", "")

        verify { registerPhoneAndNameObserver.onChanged(any<Fail>()) }
        assert(viewmodel.registerPhoneAndName.value is Fail)

        val result = viewmodel.registerPhoneAndName.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `on complete register phone and name`() {
        viewmodel.registerPhoneAndName.observeForever(registerPhoneAndNameObserver)
        coEvery { registerUseCase.execute(any(), any()) } coAnswers {
            secondArg<Subscriber<GraphqlResponse>>().onCompleted()
        }

        viewmodel.registerPhoneAndName("", "")

        verify(exactly = 0) { registerPhoneAndNameObserver.onChanged(any<Fail>()) }
    }

    @Test
    fun `Success get user profile`() {
        viewmodel.getUserProfileResponse.observeForever(getUserProfileObserver)
        coEvery { getUserProfileCompletionUseCase(Unit) } returns successGetUserProfileResponse

        viewmodel.getUserProfile()

        verify { getUserProfileObserver.onChanged(any<Success<UserProfileCompletionData>>()) }
        assert(viewmodel.getUserProfileResponse.value is Success)

        val result = viewmodel.getUserProfileResponse.value as Success<UserProfileCompletionData>
        assert(result.data == successGetUserProfileResponse.data)
    }

    @Test
    fun `Failed get user profile`() {
        viewmodel.getUserProfileResponse.observeForever(getUserProfileObserver)
        coEvery { getUserProfileCompletionUseCase(Unit) } coAnswers { throw throwable }

        viewmodel.getUserProfile()

        verify { getUserProfileObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getUserProfileResponse.value is Fail)

        val result = viewmodel.getUserProfileResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success validate user profile`() {
        viewmodel.validateUserProfileResponse.observeForever(validateUserProfileObserver)
        coEvery { validateUserProfileUseCase(any()) } returns successValidateUserProfileResponse

        viewmodel.validateUserProfile("")

        verify { validateUserProfileObserver.onChanged(any<Success<UserProfileValidate>>()) }
        assert(viewmodel.validateUserProfileResponse.value is Success)

        val result = viewmodel.validateUserProfileResponse.value as Success<UserProfileValidate>
        assert(result.data == successValidateUserProfileResponse.data)
    }

    @Test
    fun `Failed validate user profile`() {
        viewmodel.validateUserProfileResponse.observeForever(validateUserProfileObserver)
        coEvery { validateUserProfileUseCase(any()) } coAnswers { throw throwable }

        viewmodel.validateUserProfile("")

        verify { validateUserProfileObserver.onChanged(any<Fail>()) }
        assert(viewmodel.validateUserProfileResponse.value is Fail)

        val result = viewmodel.validateUserProfileResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `Success get user info`() {
        viewmodel.getUserInfoResponse.observeForever(getUserInfoObserver)

        coEvery { getProfileUseCase.execute(any()) } coAnswers {
            firstArg<GetProfileSubscriber>().onSuccessGetProfile.invoke(successGetUserInfoResponse)
        }

        viewmodel.getUserInfo()

        verify { getUserInfoObserver.onChanged(any<Success<ProfileInfo>>()) }
        assert(viewmodel.getUserInfoResponse.value is Success)

        val result = viewmodel.getUserInfoResponse.value as Success<ProfileInfo>
        assert(result.data == successGetUserInfoResponse.profileInfo)
    }

    @Test
    fun `Failed get user info`() {
        viewmodel.getUserInfoResponse.observeForever(getUserInfoObserver)

        coEvery { getProfileUseCase.execute(any()) } coAnswers {
            firstArg<GetProfileSubscriber>().onErrorGetProfile.invoke(throwable)
        }

        viewmodel.getUserInfo()

        verify { getUserInfoObserver.onChanged(any<Fail>()) }
        assert(viewmodel.getUserInfoResponse.value is Fail)

        val result = viewmodel.getUserInfoResponse.value as Fail
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `clear background task`() {
        viewmodel.clearBackgroundTask()
        verify {
            registerUseCase.unsubscribe()
            getProfileUseCase.unsubscribe()
        }
    }

    companion object {
        private val successAddNameResponse: UserProfileUpdatePojo = FileUtil.parse(
                "/success_add_name_response.json",
                UserProfileUpdatePojo::class.java
        )
        private val successAddPhoneResponse: UserProfileUpdatePojo = FileUtil.parse(
                "/success_add_phone_response.json",
                UserProfileUpdatePojo::class.java
        )
        private val successRegisterCheckResponse: RegisterCheckPojo = FileUtil.parse(
                "/success_register_check_response.json",
                RegisterCheckPojo::class.java
        )
        private val successGetShopInfoResponse: ShopInfoPojo = FileUtil.parse(
                "/success_get_shop_info_response.json",
                ShopInfoPojo::class.java
        )
        private val successGetUserProfileResponse: GetUserProfileCompletionPojo = FileUtil.parse(
                "/success_get_user_profile_response.json",
                GetUserProfileCompletionPojo::class.java
        )
        private val successValidateUserProfileResponse: UserProfileValidatePojo = FileUtil.parse(
                "/success_validate_user_profile_response.json",
                UserProfileValidatePojo::class.java
        )
        private val successGetUserInfoResponse: ProfilePojo = FileUtil.parse(
                "/success_get_user_info_response.json",
                ProfilePojo::class.java
        )
        private val successRegisterPhoneAndNameResponse: RegisterPojo = FileUtil.parse(
                "/success_register_phone_and_name_response.json",
                RegisterPojo::class.java
        )
        private val throwable = Throwable()
        private val errors = listOf("errors")
    }
}

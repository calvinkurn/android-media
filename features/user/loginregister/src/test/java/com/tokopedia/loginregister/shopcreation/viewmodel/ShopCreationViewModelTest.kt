package com.tokopedia.loginregister.shopcreation.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.loginregister.InstantRunExecutorSpek
import com.tokopedia.loginregister.shopcreation.domain.usecase.RegisterCheckUseCase
import com.tokopedia.loginregister.shopcreation.domain.usecase.ShopInfoUseCase
import com.tokopedia.profilecommon.domain.pojo.UserProfileUpdate
import com.tokopedia.profilecommon.domain.usecase.*
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Ade Fulki on 2020-01-13.
 * ade.hadian@tokopedia.com
 */

object ShopCreationViewModelTest : Spek({

    InstantRunExecutorSpek(this)

    Feature("ShopCreationViewModel") {
        val registerUseCase = mockk<RegisterUseCase>(relaxed = true)
        val registerCheckUseCase = mockk<RegisterCheckUseCase>(relaxed = true)
        val getUserProfileCompletionUseCase = mockk<GetUserProfileCompletionUseCase>(relaxed = true)
        val validateUserProfileUseCase = mockk<ValidateUserProfileUseCase>(relaxed = true)
        val updateUserProfileUseCase = mockk<UpdateUserProfileUseCase>(relaxed = true)
        val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)
        val shopInfoUseCase = mockk<ShopInfoUseCase>(relaxed = true)
        val userSession = mockk<UserSessionInterface>(relaxed = true)
        val dispatcher = Dispatchers.Unconfined

        val shopCreationViewModel = ShopCreationViewModel(
                registerUseCase,
                registerCheckUseCase,
                getUserProfileCompletionUseCase,
                validateUserProfileUseCase,
                updateUserProfileUseCase,
                getProfileUseCase,
                shopInfoUseCase,
                userSession,
                dispatcher
        )

        Scenario("Add Name") {

            val userProfileUpdate = UserProfileUpdate()
            val observer = mockk<Observer<Result<UserProfileUpdate>>>(relaxed = true)

            Given("usecase on success properly") {
                coEvery { updateUserProfileUseCase.getData(any()) } returns Success(userProfileUpdate)
            }

            When("add name") {
                shopCreationViewModel.addNameResponse.observeForever(observer)
                shopCreationViewModel.addName(String())
            }

            Then("it should return user profile update correctly") {
                verify { observer.onChanged(Success(userProfileUpdate)) }
            }
        }
    }
})
package com.tokopedia.managepassword.usecase

import com.tokopedia.managepassword.ext.InstantRunExecutorSpek
import org.spekframework.spek2.Spek

class ChangePasswordUseCaseTest : Spek ({
    InstantRunExecutorSpek(this)

//    Feature("submit change password") {
//        Scenario("[onSuccess] Change password") {
//            val multiRequestGraphqlUseCase by memoized { mockk<GraphqlUseCase>(relaxed = true) }
//            val changePasswordUseCase = ChangePasswordUseCase(multiRequestGraphqlUseCase)
//            val changePasswordResponseModel = ChangePasswordResponseModel()
//
//            Given("[Set] Success Response") {
//                changePasswordUseCase.stubExecuteOnBackground().returns(changePasswordResponseModel)
//            }
//
//            When("") {
//
//            }
//
//            Then("[Result] Should return - ChangePasswordResponseModel") {
//               runBlocking {
//                   val result = multiRequestGraphqlUseCase
//                           .executeOnBackground()
//                           .getData<ChangePasswordResponseModel>(ChangePasswordResponseModel::class.java)
//                   assert(changePasswordResponseModel == result)
//               }
//            }
//        }
//    }
})
package com.tokopedia.age_restriction

import com.tokopedia.age_restriction.usecase.FetchUserDobUseCaseTest
import com.tokopedia.age_restriction.usecase.UpdateUserDobUseCaseTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        ARHomeViewModelTest::class,
        VerifyDOBViewModelTest::class,
        FetchUserDobUseCaseTest::class,
        UpdateUserDobUseCaseTest::class
)
class AgeRestrictionSuite {
}
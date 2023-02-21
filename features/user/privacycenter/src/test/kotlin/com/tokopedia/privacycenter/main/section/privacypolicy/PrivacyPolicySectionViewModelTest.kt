package com.tokopedia.privacycenter.main.section.privacypolicy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.domain.GetPrivacyPolicyListUseCase
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicySectionViewModel
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PrivacyPolicySectionViewModelTest {

    lateinit var sut: PrivacyPolicySectionViewModel
    private val usecase = mockk<GetPrivacyPolicyListUseCase>()

    private val stateObserver: Observer<PrivacyPolicyUiModel> = mockk(relaxUnitFun = true)
    private val bsObserver: Observer<PrivacyPolicyUiModel.InnerState> = mockk(relaxUnitFun = true)

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dummyModel: List<PrivacyPolicyDataModel> = listOf(
        PrivacyPolicyDataModel(
            sectionTitle = "Privacy Policy",
            lastUpdate = "2011-11-02T02:50:12.208Z"
        )
    )

    @Before
    fun setup() {
        sut = PrivacyPolicySectionViewModel(usecase, CoroutineTestDispatchersProvider)
        sut.state.observeForever(stateObserver)
        sut.bottomSheetState.observeForever(bsObserver)
    }

    @Test
    fun getPrivacyPolicyAllList_failed() {
        coEvery { usecase.invoke(any()) } throws MessageErrorException()

        sut.getPrivacyPolicyAllList()

        verifyOrder {
            bsObserver.onChanged(PrivacyPolicyUiModel.InnerState.Loading)
            bsObserver.onChanged(PrivacyPolicyUiModel.InnerState.Error)
        }
    }

    @Test
    fun getPrivacyPolicyAllList_success() {
        coEvery { usecase.invoke(any()) } returns dummyModel

        sut.getPrivacyPolicyAllList()

        verifyOrder {
            bsObserver.onChanged(PrivacyPolicyUiModel.InnerState.Loading)
            bsObserver.onChanged(PrivacyPolicyUiModel.InnerState.Success(dummyModel))
        }
    }

    @Test
    fun getPrivacyPolicyTopFiveList_success() {
        coEvery { usecase.invoke(any()) } returns dummyModel

        sut.getPrivacyPolicyTopFiveList()

        verifyOrder {
            stateObserver.onChanged(
                PrivacyPolicyUiModel(
                    false,
                    PrivacyPolicyUiModel.InnerState.Loading
                )
            )
            stateObserver.onChanged(
                PrivacyPolicyUiModel(
                    false,
                    PrivacyPolicyUiModel.InnerState.Success(dummyModel)
                )
            )
        }
    }

    @Test
    fun getPrivacyPolicyTopFiveList_failed() {
        coEvery { usecase.invoke(any()) } throws MessageErrorException()

        sut.getPrivacyPolicyTopFiveList()

        verifyOrder {
            stateObserver.onChanged(
                PrivacyPolicyUiModel(
                    false,
                    PrivacyPolicyUiModel.InnerState.Loading
                )
            )
            stateObserver.onChanged(
                PrivacyPolicyUiModel(
                    false,
                    PrivacyPolicyUiModel.InnerState.Error
                )
            )
        }
    }

    @Test
    fun toggleContentVisibility() {
        sut.toggleContentVisibility()

        verifySequence {
            stateObserver.onChanged(PrivacyPolicyUiModel(expanded = false))
            stateObserver.onChanged(PrivacyPolicyUiModel(expanded = true))
        }
    }
}

package com.tokopedia.emoney.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibrary
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule

class BCAFlazzBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var electronicMoneyEncryption: ElectronicMoneyEncryption

    @RelaxedMockK
    lateinit var bcaFlazzUseCase: GetBCAFlazzUseCase

    @RelaxedMockK
    lateinit var bcaLibrary: BCALibrary

    private lateinit var bcaBalanceViewModel: BCABalanceViewModel

    private val gson = Gson()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils)
        bcaBalanceViewModel = spyk(
            BCABalanceViewModel(
                Dispatchers.Unconfined,
                bcaLibrary,
                gson,
                electronicMoneyEncryption,
                bcaFlazzUseCase
            )
        )
    }



}

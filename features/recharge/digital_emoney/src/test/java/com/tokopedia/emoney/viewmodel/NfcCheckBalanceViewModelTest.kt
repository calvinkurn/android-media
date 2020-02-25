package com.tokopedia.emoney.viewmodel

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NfcCheckBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var nfcCheckBalanceViewModel: NfcCheckBalanceViewModel
    private lateinit var intent: Intent

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        nfcCheckBalanceViewModel = NfcCheckBalanceViewModel(Dispatchers.Unconfined)
    }

    @Test
    fun intentFromNfc_setValue_SuccessGetData() {
        //given
        intent = spyk()

        //when
        nfcCheckBalanceViewModel.setIntentFromNfc(intent)

        //then
        Assert.assertNotNull(nfcCheckBalanceViewModel.intentFromNfc.value)
        Assert.assertEquals(intent, nfcCheckBalanceViewModel.intentFromNfc.value)
    }
}
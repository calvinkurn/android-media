package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.emoney.domain.usecase.GetJakCardUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JakCardBalanceViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var jakCardUseCase: GetJakCardUseCase

    @MockK
    lateinit var isoDep: IsoDep

    private lateinit var jakCardBalanceViewModel: JakCardBalanceViewModel
    private lateinit var emptyByteNFC: ByteArray

    private val ERROR_MESSAGE = "Maaf, cek saldo belum berhasil"

    private val COMMAND_SELECT_JAK_CARD_PROD = "00A4040008"
    private val COMMAND_SELECT_JAK_CARD_STAG = "00A4040007"
    private val COMMAND_AID_JAK_CARD_PROD = "A0000005714E4A43"
    private val COMMAND_AID_JAK_CARD_STAG = "D3600000030003"
    private val COMMAND_CHECK_BALANCE = "904C000004"

    private lateinit var resultFail: ByteArray
    private val resultFailString = "6700"
    private lateinit var resultSelectSuccess: ByteArray
    private val resultSelectSuccessString = "6F31B02F001001019360885008689978000000111020220622203206220100001E84800001000000000E48A301B19800000C009000"
    private lateinit var resultCheckBalanceSuccess: ByteArray
    private val resultCheckBalanceSuccessString = "00004E209000"

    private val COMMAND_SELECT_STAG_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_SELECT_JAK_CARD_STAG + COMMAND_AID_JAK_CARD_STAG)
    private val COMMAND_SELECT_PROD_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_SELECT_JAK_CARD_PROD + COMMAND_AID_JAK_CARD_PROD)
    private val COMMAND_CHECK_BALANCE_BYTE_ARRAY = NFCUtils.stringToByteArrayRadix(COMMAND_CHECK_BALANCE)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils.Companion)
        mockkStatic(GlobalConfig::class)

        emptyByteNFC = byteArrayOf()
        resultFail = NFCUtils.stringToByteArrayRadix(resultFailString)
        resultSelectSuccess = NFCUtils.stringToByteArrayRadix(resultSelectSuccessString)
        resultCheckBalanceSuccess = NFCUtils.stringToByteArrayRadix(resultCheckBalanceSuccessString)
        jakCardBalanceViewModel = spyk(JakCardBalanceViewModel(Dispatchers.Unconfined, jakCardUseCase))
    }

    private fun initSuccessData() {
        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.isConnected } returns true
        every { isoDep.tag.id } returns emptyByteNFC
    }

    @Test
    fun `processJakCardTagIntent failed select staging process and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns true
        every { isoDep.transceive(COMMAND_SELECT_STAG_BYTE_ARRAY) }  returns resultFail
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent failed select production process and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultFail
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }

    @Test
    fun `processJakCardTagIntent success select production process but checkBalance error and return error` () {
        //given
        initSuccessData()
        every { GlobalConfig.isAllowDebuggingTools() } returns false
        every { isoDep.transceive(COMMAND_SELECT_PROD_BYTE_ARRAY) }  returns resultSelectSuccess
        every { isoDep.transceive(COMMAND_CHECK_BALANCE_BYTE_ARRAY)} returns resultFail
        //when
        jakCardBalanceViewModel.processJakCardTagIntent(isoDep)
        //then
        assertEquals(((jakCardBalanceViewModel.errorCardMessage.value) as Throwable).message, ERROR_MESSAGE)
    }
}

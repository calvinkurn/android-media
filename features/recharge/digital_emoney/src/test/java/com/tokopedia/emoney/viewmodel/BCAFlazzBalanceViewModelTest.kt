package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.emoney.domain.request.BCAFlazzRequestMapper
import com.tokopedia.emoney.domain.request.CommonBodyEnc
import com.tokopedia.emoney.domain.response.BCAFlazzData
import com.tokopedia.emoney.domain.response.BCAFlazzResponse
import com.tokopedia.emoney.domain.response.BCAFlazzResponseMapper
import com.tokopedia.emoney.domain.response.JakCardResponseMapper
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibrary
import com.tokopedia.emoney.integration.data.JNIResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BCAFlazzBalanceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var electronicMoneyEncryption: ElectronicMoneyEncryption

    @RelaxedMockK
    lateinit var bcaFlazzUseCase: GetBCAFlazzUseCase

    @MockK
    private var bcaLibrary = mockk<BCALibrary>()

    @MockK
    lateinit var isoDep: IsoDep

    private lateinit var bcaBalanceViewModel: BCABalanceViewModel

    private val gson = Gson()

    private val mockPrivateKeyString = "7dgf8agd7isgdifushkdfhskdfh"
    private val mockPublicKeyString = "f8eh8fhe8fh8fy8eyf8eyf8yef8"
    private val encKeyAes = "ihcdhicjhd9cj9di"
    private val encPayloadAes = "8cdis8hdvios8dvosdvh"
    val pairEncryptionResult = Pair("ihcdhicjhd9cj9di", "8cdis8hdvios8dvosdvh")
    private val GEN_ONE = "1"
    private val GEN_TWO = "2"

    private val checkNoPendingBalanceGen1Result = """
        {"Action":0,"Status":1,"Attributes":{"CardNumber":"0145201100001171","CardData":"","Amount":0,"LastBalance":2000,"TransactionID":"","ButtonText":"Topup Sekarang","ImageIssuer":"https://images.tokopedia.net/img/recharge/operator/FlazzBCA.png","Message":"Ini saldo kamu yang paling baru, ya.","HasMorePendingBalance":false,"AccessCardNumber":"","AccessCode":""},"EncKey":"","EncPayload":""}
    """

    private lateinit var emptyByteNFC: ByteArray
    val cardNumber = "0145201100001171"
    val balance = 2000

    private val checkBalanceResult = JNIResult(
        1,
        "0000XXXXXXX",
        1,
        balance,
        cardNumber,
        "0000"
    )
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(NFCUtils)
        emptyByteNFC = byteArrayOf()
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

    private fun initSuccessData() {
        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.isConnected } returns true
        every { isoDep.tag.id } returns emptyByteNFC
    }

    private fun initSuccessDataIsoDepNotConnected() {
        every { isoDep.close() } returns mockk()
        every { isoDep.connect() } returns mockk()
        every { isoDep.setTimeout(any()) } returns mockk()
        every { isoDep.isConnected } returns false
        every { isoDep.tag.id } returns emptyByteNFC
    }


    fun checkBalanceGen1ShouldReturnLastBalance() {
        //given
        initSuccessData()
        val createPendingBalanceParam = BCAFlazzRequestMapper.createGetPendingBalanceParam(
            gson,
            checkBalanceResult.cardNo,
            checkBalanceResult.balance,
            GEN_ONE
        )
        mockkObject (System::class){
            every { System.loadLibrary("bcabridgelibrary")  } returns Unit
            bcaLibrary = mockk()
        }

        every { bcaLibrary.C_BCACheckBalance() } returns checkBalanceResult

        every { electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam) } returns  pairEncryptionResult

        val encParam = electronicMoneyEncryption.createEncryptedPayload(mockPublicKeyString, createPendingBalanceParam)
        val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)
        val responseCheckBalanceEnc = BCAFlazzResponse(data = CommonBodyEnc(
            encKeyAes,
            encPayloadAes
        ))
        val responseCheckBalance = gson.fromJson(checkNoPendingBalanceGen1Result, BCAFlazzData::class.java)

        every {
            electronicMoneyEncryption.createDecryptedPayload(mockPrivateKeyString, encKeyAes, encPayloadAes)
        } returns checkNoPendingBalanceGen1Result

        coEvery {
            bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
        } returns responseCheckBalanceEnc
        //when
        bcaBalanceViewModel.processBCACheckBalanceGen1(isoDep, mockPublicKeyString, mockPrivateKeyString)
        //then
        Assert.assertEquals(
            (bcaBalanceViewModel.bcaInquiry.value) as EmoneyInquiry,
            BCAFlazzResponseMapper.bcaMapper(
                cardNumber,
                responseCheckBalance.attributes.lastBalance,
                responseCheckBalance.attributes.imageIssuer,
                true,
                responseCheckBalance.attributes.amount,
                responseCheckBalance.status,
                responseCheckBalance.attributes.message,
                responseCheckBalance.attributes.hasMorePendingBalance,
                ackStatusOverride = true
            )
        )
    }

}

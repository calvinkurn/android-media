package com.tokopedia.home_account.fundsAndInvestment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.AssetConfig
import com.tokopedia.home_account.data.model.BalanceAndPointDataModel
import com.tokopedia.home_account.data.model.CentralizedUserAssetConfig
import com.tokopedia.home_account.data.model.CentralizedUserAssetDataModel
import com.tokopedia.home_account.data.model.CoBrandCCBalanceDataModel
import com.tokopedia.home_account.data.model.SaldoBalanceDataModel
import com.tokopedia.home_account.data.model.TokopointsBalanceDataModel
import com.tokopedia.home_account.data.model.WalletappGetAccountBalance
import com.tokopedia.home_account.domain.usecase.GetBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.GetCentralizedUserAssetConfigUseCase
import com.tokopedia.home_account.domain.usecase.GetCoBrandCCBalanceAndPointUseCase
import com.tokopedia.home_account.domain.usecase.GetSaldoBalanceUseCase
import com.tokopedia.home_account.domain.usecase.GetTokopointsBalanceAndPointUseCase
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentViewModel
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentViewModel.Companion.GOPAYLATERCICIL_PARTNER_CODE
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentViewModel.Companion.GOPAYLATER_PARTNER_CODE
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentViewModel.Companion.GOPAY_PARTNER_CODE
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentViewModel.Companion.OVO_PARTNER_CODE
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentResult
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FundsAndInvestmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: FundsAndInvestmentViewModel

    private val getCentralizedUserAssetConfigUseCase = mockk<GetCentralizedUserAssetConfigUseCase>(relaxed = true)
    private val getTokopointsBalanceAndPointUseCase = mockk<GetTokopointsBalanceAndPointUseCase>(relaxed = true)
    private val getSaldoBalanceUseCase = mockk<GetSaldoBalanceUseCase>(relaxed = true)
    private val getCoBrandCCBalanceAndPointUseCase = mockk<GetCoBrandCCBalanceAndPointUseCase>(relaxed = true)
    private val getBalanceAndPointUseCase = mockk<GetBalanceAndPointUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = FundsAndInvestmentViewModel(
            getCentralizedUserAssetConfigUseCase,
            getTokopointsBalanceAndPointUseCase,
            getSaldoBalanceUseCase,
            getCoBrandCCBalanceAndPointUseCase,
            getBalanceAndPointUseCase,
            dispatcher
        )
    }

    @Test
    fun `when get assets then return failed`() {
        val isRefreshData = false

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue(result is FundsAndInvestmentResult.Failed)
    }

    @Test
    fun `when get assets in refresh then return failed`() {
        val isRefreshData = true

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue(result is FundsAndInvestmentResult.Failed)
    }

    @Test
    fun `when get assets and return list of assets`() {
        val listVertical = listAssetConfig("1")
        val listHorizontal = listAssetConfig("2")
        val response = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
                assetConfigHorizontal = listHorizontal
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns response
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isNotEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(listHorizontal.size, result.listHorizontal.size)
    }

    @Test
    fun `when get assets toko point and return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.TOKOPOINT)
        val responseAsset = CentralizedUserAssetDataModel(data = CentralizedUserAssetConfig(assetConfigVertical = listVertical))
        val responseTokoPoint = TokopointsBalanceDataModel(data = dataWallet(AccountConstants.WALLET.TOKOPOINT))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getTokopointsBalanceAndPointUseCase(Unit) } returns responseTokoPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets toko point and return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.TOKOPOINT)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getTokopointsBalanceAndPointUseCase(Unit) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets saldo and return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.SALDO)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseSaldo = SaldoBalanceDataModel(data = dataWallet(AccountConstants.WALLET.SALDO))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getSaldoBalanceUseCase(Unit) } returns responseSaldo
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedSubtitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets saldo and return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.SALDO)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getSaldoBalanceUseCase(Unit) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedSubtitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets CoBrand and return success`() {
        val listHorizontal = listAssetConfig(AccountConstants.WALLET.CO_BRAND_CC)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigHorizontal = listHorizontal,
            )
        )
        val responseCoBrand = CoBrandCCBalanceDataModel(data = dataWallet(AccountConstants.WALLET.CO_BRAND_CC))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getCoBrandCCBalanceAndPointUseCase(Unit) } returns responseCoBrand
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isEmpty())
        assertTrue(result.listHorizontal.isNotEmpty())
        assertEquals(listHorizontal.size, result.listHorizontal.size)
        assertEquals(expectedTitle, result.listHorizontal.first().title)
        assertEquals(true, result.listHorizontal.first().hideTitle)
        assertEquals(false, result.listHorizontal.first().isVertical)
        assertEquals(false, result.listHorizontal.first().isFailed)
    }

    @Test
    fun `when get assets CoBrand and return false`() {
        val listHorizontal = listAssetConfig(AccountConstants.WALLET.CO_BRAND_CC)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigHorizontal = listHorizontal,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getCoBrandCCBalanceAndPointUseCase(Unit) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isEmpty())
        assertTrue(result.listHorizontal.isNotEmpty())
        assertEquals(listHorizontal.size, result.listHorizontal.size)
        assertEquals(expectedTitle, result.listHorizontal.first().title)
        assertEquals(true, result.listHorizontal.first().hideTitle)
        assertEquals(false, result.listHorizontal.first().isVertical)
        assertEquals(true, result.listHorizontal.first().isFailed)
    }

    @Test
    fun `when get assets gopay and return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.GOPAY)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseBalanceAndPoint = BalanceAndPointDataModel(data = dataWallet(AccountConstants.WALLET.GOPAY))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(GOPAY_PARTNER_CODE) } returns responseBalanceAndPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets gopay and return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.GOPAY)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(GOPAY_PARTNER_CODE) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets gopay later and return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.GOPAYLATER)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseBalanceAndPoint = BalanceAndPointDataModel(data = dataWallet(AccountConstants.WALLET.GOPAYLATER))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(GOPAYLATER_PARTNER_CODE) } returns responseBalanceAndPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets gopay later and return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.GOPAYLATER)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(GOPAYLATER_PARTNER_CODE) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets gopay later cicil and return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.GOPAYLATERCICIL)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseBalanceAndPoint = BalanceAndPointDataModel(data = dataWallet(AccountConstants.WALLET.GOPAYLATERCICIL))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(GOPAYLATERCICIL_PARTNER_CODE) } returns responseBalanceAndPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets gopay later cicil and return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.GOPAYLATERCICIL)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(GOPAYLATERCICIL_PARTNER_CODE) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets ovo and return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.OVO)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseBalanceAndPoint = BalanceAndPointDataModel(data = dataWallet(AccountConstants.WALLET.OVO))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(OVO_PARTNER_CODE) } returns responseBalanceAndPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets ovo and return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.OVO)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase(OVO_PARTNER_CODE) } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets other and return success`() {
        val listVertical = listAssetConfig("")
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseBalanceAndPoint = BalanceAndPointDataModel(data = dataWallet(""))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase("") } returns responseBalanceAndPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when get assets other and return failed`() {
        val listVertical = listAssetConfig("")
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getBalanceAndPointUseCase("") } throws throwable
        viewModel.getCentralizedUserAssetConfig(isRefreshData)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when refresh assets vertical then return success`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.TOKOPOINT)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseTokoPoint = TokopointsBalanceDataModel(data = dataWallet(AccountConstants.WALLET.TOKOPOINT))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getTokopointsBalanceAndPointUseCase(Unit) } returns responseTokoPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)
        viewModel.refreshItem(itemVertical)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(false, result.listVertical.first().isFailed)
    }

    @Test
    fun `when refresh assets vertical then return failed`() {
        val listVertical = listAssetConfig(AccountConstants.WALLET.TOKOPOINT)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigVertical = listVertical,
            )
        )
        val responseTokoPoint = TokopointsBalanceDataModel(data = dataWallet(AccountConstants.WALLET.TOKOPOINT))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getTokopointsBalanceAndPointUseCase(Unit) } returns responseTokoPoint
        viewModel.getCentralizedUserAssetConfig(isRefreshData)
        coEvery { getTokopointsBalanceAndPointUseCase(Unit) } throws throwable
        viewModel.refreshItem(itemVertical)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isNotEmpty())
        assertTrue(result.listHorizontal.isEmpty())
        assertEquals(listVertical.size, result.listVertical.size)
        assertEquals(expectedTitle, result.listVertical.first().title)
        assertEquals(true, result.listVertical.first().hideTitle)
        assertEquals(true, result.listVertical.first().isVertical)
        assertEquals(true, result.listVertical.first().isFailed)
    }

    @Test
    fun `when refresh assets horizontal then return success`() {
        val listHorizontal = listAssetConfig(AccountConstants.WALLET.CO_BRAND_CC)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigHorizontal = listHorizontal,
            )
        )
        val responseCoBrand = CoBrandCCBalanceDataModel(data = dataWallet(AccountConstants.WALLET.CO_BRAND_CC))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getCoBrandCCBalanceAndPointUseCase(Unit) } returns responseCoBrand
        viewModel.getCentralizedUserAssetConfig(isRefreshData)
        viewModel.refreshItem(itemHorizontal)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isEmpty())
        assertTrue(result.listHorizontal.isNotEmpty())
        assertEquals(listHorizontal.size, result.listHorizontal.size)
        assertEquals(expectedTitle, result.listHorizontal.first().title)
        assertEquals(true, result.listHorizontal.first().hideTitle)
        assertEquals(false, result.listHorizontal.first().isVertical)
        assertEquals(false, result.listHorizontal.first().isFailed)
    }

    @Test
    fun `when refresh assets horizontal then return false`() {
        val listHorizontal = listAssetConfig(AccountConstants.WALLET.CO_BRAND_CC)
        val responseAsset = CentralizedUserAssetDataModel(
            data = CentralizedUserAssetConfig(
                assetConfigHorizontal = listHorizontal,
            )
        )
        val responseCoBrand = CoBrandCCBalanceDataModel(dataWallet(AccountConstants.WALLET.CO_BRAND_CC))

        coEvery { getCentralizedUserAssetConfigUseCase(ASSET_PAGE) } returns responseAsset
        coEvery { getCoBrandCCBalanceAndPointUseCase(Unit) } returns responseCoBrand
        viewModel.getCentralizedUserAssetConfig(isRefreshData)
        coEvery { getCoBrandCCBalanceAndPointUseCase(Unit) } throws throwable
        viewModel.refreshItem(itemHorizontal)

        val result = viewModel.uiState.getOrAwaitValue()
        assertTrue((result as FundsAndInvestmentResult.Content).listVertical.isEmpty())
        assertTrue(result.listHorizontal.isNotEmpty())
        assertEquals(listHorizontal.size, result.listHorizontal.size)
        assertEquals(expectedTitle, result.listHorizontal.first().title)
        assertEquals(true, result.listHorizontal.first().hideTitle)
        assertEquals(false, result.listHorizontal.first().isVertical)
        assertEquals(true, result.listHorizontal.first().isFailed)
    }


    companion object {
        private const val ASSET_PAGE = "asset_page"
        const val expectedTitle = "Title"
        const val expectedSubtitle = "Subtitle"
        const val isRefreshData = false
        val throwable = Throwable()
        val itemHorizontal = WalletUiModel(
            id = "cobrandcc",
            title = expectedTitle,
            subtitle = expectedSubtitle,
            urlImage = "",
            applink = "",
            isFailed = false,
            isActive = false,
            isVertical = false,
            hideTitle = true,
            statusName = "",
            isLoading = false
        )
        val itemVertical = WalletUiModel(
            id = "tokopoints",
            title = expectedTitle,
            subtitle = expectedSubtitle,
            urlImage = "",
            applink = "",
            isFailed = false,
            isActive = false,
            isVertical = true,
            hideTitle = true,
            statusName= "",
            isLoading = false
        )

        fun listAssetConfig(id: String): List<AssetConfig> = listOf(
            AssetConfig(
                id = id,
                title = expectedTitle,
                subtitle = expectedSubtitle,
                hideTitle = true
            )
        )

        fun dataWallet(id: String): WalletappGetAccountBalance =
            WalletappGetAccountBalance(
                id = id,
                title = "Fake Title",
                hideTitle = false
            )
    }

}

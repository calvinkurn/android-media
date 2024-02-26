package com.tokopedia.home_account.fundsAndInvestment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.test.filters.LargeTest
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentResult
import com.tokopedia.home_account.ui.fundsAndInvestment.ui.FundsAndInvestmentScreen
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@UiTest
class FundsAndInvestmentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Before
    fun before() {
        composeTestRule.mainClock.autoAdvance = true
    }

    @Test
    fun when_failed_then_show_error_page() {
        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Failed),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Oops, konten gagal ditampilkan").assertIsDisplayed()
            onNodeWithText("Coba lagi untuk tampilkan konten, ya.").assertIsDisplayed()
            onNodeWithText("Coba Lagi").assertIsDisplayed()
        }
    }

    @Test
    fun when_show_list_at_first_and_second_section() {
        val listVertical = listOf(
            WalletUiModel(id = "1", title = "Gopay", subtitle = "Rp 100.000", isLoading = false, isFailed = false),
            WalletUiModel(id = "2", title = "Coin", subtitle = "1.000", isLoading = false, isFailed = false),
        )
        val listHorizontal = listOf(
            WalletUiModel(id = "3", title = "Tokopedia Card", subtitle = "Cicil 0%", isLoading = false, isFailed = false),
            WalletUiModel(id = "4", title = "Gopay Later", subtitle = "Daftarnya mudah!", isLoading = false, isFailed = false),
        )

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(listVertical, listHorizontal)),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Saldo & Points").assertExists()
            onNodeWithText("Cobain ini juga, yuk").assertExists()
            onNodeWithText("Gopay").assertExists()
            onNodeWithText("Rp 100.000").assertExists()
            onNodeWithText("Coin").assertExists()
            onNodeWithText("1.000").assertExists()
            onNodeWithText("Tokopedia Card").assertExists()
            onNodeWithText("Cicil 0%").assertExists()
            onNodeWithText("Gopay Later").assertExists()
            onNodeWithText("Daftarnya mudah!").assertExists()
        }
    }

    @Test
    fun when_show_list_at_first_section_only() {
        val listVertical = listOf(
            WalletUiModel(id = "1", title = "Gopay", subtitle = "Rp 100.000", isLoading = false, isFailed = false),
            WalletUiModel(id = "2", title = "Coin", subtitle = "1.000", isLoading = false, isFailed = false),
        )

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(listVertical, emptyList())),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Saldo & Points").assertExists()
            onNodeWithText("Cobain ini juga, yuk").assertDoesNotExist()
            onNodeWithText("Gopay").assertExists()
            onNodeWithText("Rp 100.000").assertExists()
            onNodeWithText("Coin").assertExists()
            onNodeWithText("1.000").assertExists()
            onNodeWithText("Tokopedia Card").assertDoesNotExist()
            onNodeWithText("Cicil 0%").assertDoesNotExist()
            onNodeWithText("Gopay Later").assertDoesNotExist()
            onNodeWithText("Daftarnya mudah!").assertDoesNotExist()
        }
    }

    @Test
    fun when_show_list_at_second_section_only() {
        val listHorizontal = listOf(
            WalletUiModel(id = "3", title = "Tokopedia Card", subtitle = "Cicil 0%", isLoading = false, isFailed = false),
            WalletUiModel(id = "4", title = "Gopay Later", subtitle = "Daftarnya mudah!", isLoading = false, isFailed = false),
        )

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(emptyList(), listHorizontal)),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Saldo & Points").assertDoesNotExist()
            onNodeWithText("Cobain ini juga, yuk").assertExists()
            onNodeWithText("Gopay").assertDoesNotExist()
            onNodeWithText("Rp 100.000").assertDoesNotExist()
            onNodeWithText("Coin").assertDoesNotExist()
            onNodeWithText("1.000").assertDoesNotExist()
            onNodeWithText("Tokopedia Card").assertExists()
            onNodeWithText("Cicil 0%").assertExists()
            onNodeWithText("Gopay Later").assertExists()
            onNodeWithText("Daftarnya mudah!").assertExists()
        }
    }

    @Test
    fun when_failed_item_list() {
        val listVertical = listOf(
            WalletUiModel(id = "1", title = "Gopay", subtitle = "Rp 100.000", isLoading = false, isFailed = true)
        )

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(listVertical, emptyList())),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Saldo & Points").assertExists()
            onNodeWithText("Gopay").assertExists()
            onNodeWithText("Rp 100.000").assertDoesNotExist()
            onNodeWithText("Gagal memuat").assertExists()
        }
    }

    @Test
    fun when_item_list_show_aktifkan_in_vertical_list() {
        val listVertical = listOf(
            WalletUiModel(id = "1", title = "Gopay", subtitle = "Rp 100.000", isLoading = false, isFailed = false, isActive = false, isVertical = true)
        )

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(listVertical, emptyList())),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Saldo & Points").assertExists()
            onNodeWithText("Gopay").assertExists()
            onNodeWithText("Rp 100.000").assertDoesNotExist()
            onNodeWithText("Gagal memuat").assertDoesNotExist()
            onNodeWithText("Aktifkan").assertExists()
        }
    }

    @Test
    fun when_item_list_not_show_aktifkan_in_horizontal_list() {
        val listHorizontal = listOf(
            WalletUiModel(id = "1", title = "Tokopedia Care", subtitle = "Cicilan 0%", isLoading = false, isFailed = false, isActive = false, isVertical = false)
        )

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(emptyList(), listHorizontal)),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Cobain ini juga, yuk").assertExists()
            onNodeWithText("Tokopedia Care").assertExists()
            onNodeWithText("Cicilan 0%").assertExists()
            onNodeWithText("Gagal memuat").assertDoesNotExist()
            onNodeWithText("Aktifkan").assertDoesNotExist()
        }
    }

    @Test
    fun check_callback_reload() {
        var callback = false

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Failed),
                onItemClicked = {},
                onBackClicked = {},
                onReloadData = { callback = true }
            )
        }

        composeRule.apply {
            onNodeWithText("Coba Lagi").assertIsDisplayed()
            onNodeWithText("Coba Lagi").performClick()
        }

        assert(callback)
    }

    @Test
    fun check_callback_item_clicked() {
        val expected = WalletUiModel(id = "1", title = "Gopay", subtitle = "Rp 100.000", isLoading = false, isFailed = false)
        val listVertical = listOf(
            expected,
            WalletUiModel(id = "2", title = "Coin", subtitle = "1.000", isLoading = false, isFailed = false),
        )
        val listHorizontal = listOf(
            WalletUiModel(id = "3", title = "Tokopedia Card", subtitle = "Cicil 0%", isLoading = false, isFailed = false),
            WalletUiModel(id = "4", title = "Gopay Later", subtitle = "Daftarnya mudah!", isLoading = false, isFailed = false),
        )
        var result : WalletUiModel? = null

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Content(listVertical, listHorizontal)),
                onItemClicked = { result = it},
                onBackClicked = {},
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithText("Gopay").assertExists()
            onNodeWithText("Gopay").performClick()
        }

        assert(result == expected)
    }

    @Test
    fun check_callback_click_back() {
        var callback = false

        composeRule.setContent {
            FundsAndInvestmentScreen(
                userId = "",
                uiState = MutableLiveData(FundsAndInvestmentResult.Loading(false)),
                onItemClicked = {},
                onBackClicked = { callback = true },
                onReloadData = {}
            )
        }

        composeRule.apply {
            onNodeWithContentDescription("BackButton").assertExists()
            onNodeWithContentDescription("BackButton").performClick()
        }

        assert(callback)
    }
}

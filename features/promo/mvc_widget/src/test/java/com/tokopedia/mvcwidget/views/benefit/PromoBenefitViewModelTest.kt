package com.tokopedia.mvcwidget.views.benefit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.gson.Gson
import com.tokopedia.mvcwidget.data.entity.PromoCatalogResponse
import com.tokopedia.mvcwidget.usecases.GetPromoBenefitBottomSheetUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class PromoBenefitViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    lateinit var sut: PromoBenefitViewModel
    private val getPromoBenefitUseCase: GetPromoBenefitBottomSheetUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        sut = PromoBenefitViewModel(getPromoBenefitUseCase)
    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setId positive`() = runTest {

        coEvery { getPromoBenefitUseCase.invoke(any()) } returns sampleData

        sut.setId(anyString())

        sut.state.test {
            assertEquals("#FFF5F6", awaitItem().bgColor)
        }
    }

    @Test
    fun `setId negative`() = runTest {
        coEvery { getPromoBenefitUseCase.invoke(any()) } throws Exception("some random exception")

        sut.setId(anyString())

        sut.error.test {
            assertEquals(true, awaitItem())
        }
    }
}

private val responseSampleJson = """
        {
    "promoCatalogGetPDEBottomSheet": {
      "resultStatus": {
        "code": "200",
        "message": [
          "Success"
        ],
        "status": "OK"
      },
      "resultList": [
        {
          "productID": 1000,
          "widgetList": [
            {
              "id": "1",
              "widgetType": "pdp_bottomsheet",
              "componentList": [
                {
                  "id": 1,
                  "componentType": "pdp_bs_final_price",
                  "attributeList": [
                    {
                      "type": "text_title_value",
                      "value": "Perkiraan harga hemat"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_title_format",
                      "value": "normal"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp9.000.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 2,
                  "componentType": "pdp_bs_nett_price",
                  "attributeList": [
                    {
                      "type": "text_title_value",
                      "value": "Harga tanpa promo"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_title_format",
                      "value": "normal"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp9.500.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 3,
                  "componentType": "pdp_bs_benefit_cashback",
                  "attributeList": [
                    {
                      "type": "icon_url",
                      "value": "https://images.tokopedia.net/img/gopay_coins.png"
                    },
                    {
                      "type": "text_title_value",
                      "value": "Cashback GoPay Coins"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#0094CF"
                    },
                    {
                      "type": "text_title_format",
                      "value": "bold"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp300.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 4,
                  "componentType": "pdp_bs_benefit_discount",
                  "attributeList": [
                    {
                      "type": "icon_url",
                      "value": "https://images.tokopedia.net/img/discount_icon.png"
                    },
                    {
                      "type": "text_title_value",
                      "value": "Diskon"
                    },
                    {
                      "type": "text_title_color",
                      "value": "#FF7F17"
                    },
                    {
                      "type": "text_title_format",
                      "value": "bold"
                    },
                    {
                      "type": "text_value",
                      "value": "Rp200.000"
                    },
                    {
                      "type": "text_color",
                      "value": "#212121"
                    },
                    {
                      "type": "text_format",
                      "value": "bold"
                    }
                  ]
                },
                {
                  "id": 5,
                  "componentType": "pdp_bs_benefit_tnc",
                  "attributeList": [
                    {
                      "type": "text_html",
                      "value": "<ul><li>Nominal promo bisa berubah dikarenakan waktu pembelian, ketersediaan produk, periode promosi, ketentuan promo.</li><li>Harga akhir akan ditampilkan di halaman \"Pengiriman / Checkout\". Perhatikan sebelum mengkonfirmasi pesanan.</li></ul>"
                    },
                    {
                      "type": "text_color",
                      "value": "#6D7588"
                    }
                  ]
                },
                {
                  "id": 6,
                  "componentType": "pdp_bs_background",
                  "attributeList": [
                    {
                      "type": "background_color",
                      "value": "#FFF5F6"
                    },
                    {
                      "type": "background_image",
                      "value": "https://images.tokopedia.net/img/bs_background_regular.png"
                    }
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
  }
    """.trimIndent()

private val sampleData: PromoCatalogResponse =
    Gson().fromJson(responseSampleJson, PromoCatalogResponse::class.java)

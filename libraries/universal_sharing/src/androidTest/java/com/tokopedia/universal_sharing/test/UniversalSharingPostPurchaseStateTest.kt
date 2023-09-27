package com.tokopedia.universal_sharing.test

import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseProductModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseShopModel
import com.tokopedia.universal_sharing.stub.common.NetworkUtilStub
import com.tokopedia.universal_sharing.test.base.BaseUniversalSharingPostPurchaseBottomSheetTest
import com.tokopedia.universal_sharing.test.robot.postPurchaseRobot
import com.tokopedia.universal_sharing.test.robot.validate
import com.tokopedia.universal_sharing.view.model.UniversalSharingGlobalErrorUiModel
import org.junit.Test

class UniversalSharingPostPurchaseStateTest :
    BaseUniversalSharingPostPurchaseBottomSheetTest() {

    @Test
    fun show_shop_and_product_list() {
        postPurchaseRobot {
            launchActivity {
                val model = UniversalSharingPostPurchaseModel(
                    shopList = listOf(
                        UniversalSharingPostPurchaseShopModel(
                            shopName = "Samsung Official Store",
                            shopType = "official_store",
                            productList = listOf(
                                UniversalSharingPostPurchaseProductModel(
                                    orderId = "12341234",
                                    productId = "123123123",
                                    productName = "Samsung Galaxy A54 5G 8/256GB - Awesome Graphite",
                                    productPrice = "1.000.000",
                                    imageUrl = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png"
                                )
                            )
                        )
                    )
                )
                it.putExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA, model)
            }
        }.validate {
            assertShopItemAt(0)
            assertShopNameAt(0, "Samsung Official Store")
            assertProductItemAt(1)
            assertProductNameAt(1, "Samsung Galaxy A54 5G 8/256GB")
            assertProductPriceAt(1, "1.000.000")
        }
    }

    @Test
    fun show_global_error() {
        postPurchaseRobot {
            launchActivity()
        }.validate {
            assertGlobalErrorType(
                0,
                UniversalSharingGlobalErrorUiModel.ErrorType.ERROR_GENERAL
            )
        }
    }

    @Test
    fun show_network_error() {
        postPurchaseRobot {
            (networkUtil as NetworkUtilStub).isConnectedToNetwork = false
            launchActivity()
        }.validate {
            assertGlobalErrorType(
                0,
                UniversalSharingGlobalErrorUiModel.ErrorType.ERROR_NETWORK
            )
        }
    }
}

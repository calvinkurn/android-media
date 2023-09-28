package com.tokopedia.universal_sharing.test

import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
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
            launchActivity()
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
            launchActivity {
                it.removeExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA)
            }
        }.validate {
            assertGlobalErrorType(
                0,
                UniversalSharingGlobalErrorUiModel.ErrorType.ERROR_GENERAL
            )
        }
    }

    @Test
    fun show_network_error() {
        (networkUtil as NetworkUtilStub).isConnectedToNetwork = false
        postPurchaseRobot {
            launchActivity {
                it.removeExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA)
            }
        }.validate {
            assertGlobalErrorType(
                0,
                UniversalSharingGlobalErrorUiModel.ErrorType.ERROR_NETWORK
            )
        }
    }
}

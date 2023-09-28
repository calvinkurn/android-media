package com.tokopedia.universal_sharing.test

import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.stub.data.response.GqlResponseStub
import com.tokopedia.universal_sharing.test.base.BaseUniversalSharingPostPurchaseBottomSheetTest
import com.tokopedia.universal_sharing.test.robot.postPurchaseRobot
import com.tokopedia.universal_sharing.test.robot.universalSharingRobot
import com.tokopedia.universal_sharing.test.robot.validate
import org.junit.Test
import java.net.UnknownHostException

class UniversalSharingPostPurchaseSharingTest : BaseUniversalSharingPostPurchaseBottomSheetTest() {
    @Test
    fun click_share_open_share_bottom_sheet() {
        postPurchaseRobot {
            launchActivity()
            clickOnShareButton(1)
        }
        universalSharingRobot {}.validate {
            shouldShowThumbnailShare()
        }
    }

    @Test
    fun click_share_show_network_error_toaster() {
        GqlResponseStub.productV3Response.error = UnknownHostException()
        postPurchaseRobot {
            launchActivity()
            clickOnShareButton(1)
        }.validate {
            assertToasterWithSubText(
                context.getString(R.string.universal_sharing_post_purchase_error_network)
            )
        }
    }

    @Test
    fun click_share_unactive_product_show_product_unavailable_toaster() {
        GqlResponseStub.productV3Response.editAndGetResponseObject {
            it.product.status = ""
        }
        postPurchaseRobot {
            launchActivity()
            clickOnShareButton(1)
        }.validate {
            assertToasterWithSubText(
                context.getString(R.string.universal_sharing_post_purchase_product_unavailable)
            )
        }
    }

    @Test
    fun click_share_empty_product_show_product_unavailable_toaster() {
        GqlResponseStub.productV3Response.editAndGetResponseObject {
            it.product.stock = 0
        }
        postPurchaseRobot {
            launchActivity()
            clickOnShareButton(1)
        }.validate {
            assertToasterWithSubText(
                context.getString(R.string.universal_sharing_post_purchase_product_unavailable)
            )
        }
    }
}

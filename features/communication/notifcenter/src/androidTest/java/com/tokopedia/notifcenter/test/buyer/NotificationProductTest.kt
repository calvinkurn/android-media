package com.tokopedia.notifcenter.test.buyer

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.detailResult
import com.tokopedia.notifcenter.test.robot.detailRobot
import com.tokopedia.notifcenter.test.robot.generalResult
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotificationProductTest : BaseNotificationTest() {
    @Test
    fun should_open_bottomsheet_when_click_beli_and_keranjang_in_attached_product_variants() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_product_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject { }

        // When
        launchActivity()
        detailRobot {
            clickCheckoutButtonAt(0)
        }
        detailRobot {
            clickAtcAt(0)
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1988298491",
            "10973651",
            "notifcenter",
            "false",
            ""
        ) // Product from JSON
        generalResult {
            assertIntentData(intent.data, 2)
        }
        detailResult {
            assertLabelVariant(0)
        }
    }

    @Test
    fun should_open_wishlist_when_user_click_cek_wishlist() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_product_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().productData.forEach { data ->
                if (data.productId == "1988288674") {
                    data.typeButton = ProductData.BUTTON_TYPE_WISHLIST
                }
            }
        }

        // When
        launchActivity()
        detailRobot {
            scrollToProductPosition(2)
            clickCheckWishlist(2)
        }

        // Then
        generalResult {
            assertIntentData(Uri.parse(ApplinkConst.NEW_WISHLIST))
        }
    }

    @Test
    fun should_not_show_variant_labels_if_product_is_parent_variant() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_product_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].product?.variant = listOf()
        }

        // When
        launchActivity()
        detailRobot {
            scrollToProductPosition(1)
        }

        // Then
        detailResult {
            assertLabelVariantNotDisplayed(1)
        }
    }

    @Test
    fun should_open_VBS_with_tokonow_if_product_is_parent_variant_when_click_ATC_and_buy() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_product_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].productData[1].variant = listOf()
        }

        // When
        launchActivity()
        detailRobot {
            clickAtcAt(0)
        }
        detailRobot {
            clickCheckoutButtonAt(0)
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1988298491",
            "10973651",
            "notifcenter",
            "false",
            ""
        ) // Product from JSON
        generalResult {
            assertIntentData(intent.data, 2)
        }
    }

    @Test
    fun should_open_VBS_with_tokonow_if_product_is_variants_and_tokonow() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_product_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].productData[1].shop.isTokonow = true
        }

        // When
        launchActivity()
        detailRobot {
            clickAtcAt(0)
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1988298491",
            "10973651",
            "notifcenter",
            "true",
            ""
        ) // Product from JSON
        generalResult {
            assertIntentData(intent.data)
        }
    }

    @Test
    fun should_open_product_detail_page_with_applink_when_users_clicks_on_product() {
        // Given
        GqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_product_only.json"
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject { }

        // When
        launchActivity()
        detailRobot {
            clickProductAt(0)
        }

        // Then
        generalResult {
            assertIntentData(Uri.parse("tokopedia://product/2148833237?extParam=whid=341734&src=notifcenter"))
        }
    }
}

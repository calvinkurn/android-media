package com.tokopedia.topchat.chatroom.view.activity.test.buyer.broadcast

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastResult
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import com.tokopedia.productcard.compact.R as productcardcompactR
import com.tokopedia.promousage.R as promousageR

@UiTest
class TopChatRoomBuyerBroadcastPromoTest : TopchatRoomTest() {
    @Test
    fun assert_broadcast_promo_full() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastBanner(1, isDisplayed())
            assertNewBroadcastMessage(
                1,
                withText("Test Promo Broadcast. \uD83D\uDCE2 PROMO spesial buat {{.Name}}, hanya di toko kami. Yuk, belanja pakai promo ini sebelum periodenya berakhir!")
            )

            assertNewBroadcastPromoProductCarousel(1, 5)
            // product normal
            assertNewBroadcastPromoProduct(
                0,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 1")
            )

            // product pre-order
            assertNewBroadcastPromoProduct(
                1,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 2")
            )
            assertNewBroadcastPromoProduct(
                1,
                productcardcompactR.id.oos_label,
                isDisplayed()
            )
            assertNewBroadcastPromoProduct(
                1,
                productcardcompactR.id.oos_label,
                withText("PreOrder")
            )

            // product oos
            broadcastRobot {
                scrollBroadcastPromoProduct(2)
            }
            assertNewBroadcastPromoProduct(
                2,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 3")
            )
            assertNewBroadcastPromoProduct(
                2,
                productcardcompactR.id.oos_label,
                isDisplayed()
            )
            assertNewBroadcastPromoProduct(
                2,
                productcardcompactR.id.oos_label,
                withText("Stok Habis")
            )

            // product without rating
            broadcastRobot {
                scrollBroadcastPromoProduct(3)
            }
            assertNewBroadcastPromoProduct(
                3,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 4")
            )
            assertNewBroadcastPromoProduct(
                3,
                productcardcompactR.id.rating_icon,
                not(isDisplayed())
            )
            assertNewBroadcastPromoProduct(
                3,
                productcardcompactR.id.rating_typography,
                not(isDisplayed())
            )

            // see more
            broadcastRobot {
                scrollBroadcastPromoProduct(4)
            }
            assertNewBroadcastPromoProduct(
                4,
                R.id.topchat_chatroom_broadcast_see_more_tv,
                withText("Lihat Produk Lainnya")
            )

            assertNewBroadcastVoucherHeader(1, "Makin hemat pakai kupon ini")
            assertNewBroadcastVoucherDesc(1, "Kupon bisa dipilih lewat keranjang, tanpa perlu salin kode.")
            assertNewBroadcastPromoVoucherCarousel(1, 2)

            assertNewBroadcastPromoVoucher(0, promousageR.id.promo_tv_type_mini_card, withText("Cashback"))
            assertNewBroadcastPromoVoucher(0, promousageR.id.promo_tv_title_mini_card, withText("Rp50 rb"))
            assertNewBroadcastPromoVoucher(0, promousageR.id.promo_tv_desc_mini_card, withText("Min. belanja Rp250 rb"))

            assertNewBroadcastPromoVoucher(1, promousageR.id.promo_tv_type_mini_card, withText("Bebas Ongkir"))
            assertNewBroadcastPromoVoucher(1, promousageR.id.promo_tv_title_mini_card, withText("Rp20 rb"))
            assertNewBroadcastPromoVoucher(1, promousageR.id.promo_tv_desc_mini_card, withText("Min. belanja Rp50 rb"))

            assertNewBroadcastPromoTimeStamp(1, "12:00")
        }

        // When
        broadcastRobot {
            clickBroadcastBanner(1)
            clickBroadcastPromoProduct(0)
            clickBroadcastPromoProduct(1)
            clickBroadcastPromoProduct(2)
            clickBroadcastPromoProduct(3)
            clickBroadcastPromoSeeMore(4)
        }

        // Then
        generalResult {
            openPageWithExtra("url", "https://chat.tokopedia.com/tc/v1/redirect/")
            openPageWithApplink("tokopedia://product/215100001?src=chat")
            openPageWithApplink("tokopedia://product/215100002?src=chat")
            openPageWithApplink("tokopedia://product/215100003?src=chat")
            openPageWithApplink("tokopedia://product/215100004?src=chat")
            openPageWithApplink("tokopedia://shop/6559467/product")
        }
    }

    @Test
    fun assert_broadcast_promo_without_banner() {
    }

    @Test
    fun assert_broadcast_without_message() {
    }

    @Test
    fun assert_broadcast_promo_single_product() {
    }

    @Test
    fun assert_broadcast_promo_without_product() {
    }

    @Test
    fun assert_broadcast_promo_single_voucher() {
    }
}

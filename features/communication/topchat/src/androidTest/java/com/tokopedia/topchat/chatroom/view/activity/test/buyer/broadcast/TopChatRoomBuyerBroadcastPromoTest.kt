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
            assertNewBroadcastPromoProductCarouselItem(
                0,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 1")
            )
            assertNewBroadcastPromoProductCarouselItem(
                0,
                productcardcompactR.id.promo_label,
                withText("80%")
            )
            assertNewBroadcastPromoProductCarouselItem(
                0,
                productcardcompactR.id.slash_price_typography,
                withText("50.000")
            )

            assertNewBroadcastPromoProductCarouselItem(
                0,
                productcardcompactR.id.rating_typography,
                withText("5.0  •  terjual 12rb")
            )
            assertNewBroadcastPromoProductCarouselItem(
                0,
                productcardcompactR.id.rating_icon,
                isDisplayed()
            )

            // product pre-order
            assertNewBroadcastPromoProductCarouselItem(
                1,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 2")
            )
            assertNewBroadcastPromoProductCarouselItem(
                1,
                productcardcompactR.id.oos_label,
                isDisplayed()
            )
            assertNewBroadcastPromoProductCarouselItem(
                1,
                productcardcompactR.id.oos_label,
                withText("PreOrder")
            )

            // product without rating
            broadcastRobot {
                scrollBroadcastPromoProduct(2)
            }
            assertNewBroadcastPromoProductCarouselItem(
                2,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 4")
            )
            assertNewBroadcastPromoProductCarouselItem(
                2,
                productcardcompactR.id.rating_icon,
                not(isDisplayed())
            )
            assertNewBroadcastPromoProductCarouselItem(
                2,
                productcardcompactR.id.rating_typography,
                not(isDisplayed())
            )

            // product oos
            broadcastRobot {
                scrollBroadcastPromoProduct(3)
            }
            assertNewBroadcastPromoProductCarouselItem(
                3,
                productcardcompactR.id.product_name_typography,
                withText("Product Attachment 3")
            )
            assertNewBroadcastPromoProductCarouselItem(
                3,
                productcardcompactR.id.oos_label,
                isDisplayed()
            )
            assertNewBroadcastPromoProductCarouselItem(
                3,
                productcardcompactR.id.oos_label,
                withText("Stok Habis")
            )

            // see more
            broadcastRobot {
                scrollBroadcastPromoProduct(4)
            }
            assertNewBroadcastPromoProductCarouselItem(
                4,
                R.id.topchat_chatroom_broadcast_see_more_tv,
                withText("Lihat Produk Lainnya")
            )

            assertNewBroadcastVoucherHeader(1, withText("Makin hemat pakai kupon ini"))
            assertNewBroadcastVoucherDesc(1, withText("Kupon bisa dipilih lewat keranjang, tanpa perlu salin kode."))
            assertNewBroadcastPromoVoucher(1, isDisplayed())
            assertNewBroadcastVoucherCarousel(1, 2)

            assertNewBroadcastVoucher(0, promousageR.id.promo_tv_type_mini_card, withText("Cashback"))
            assertNewBroadcastVoucher(0, promousageR.id.promo_tv_title_mini_card, withText("Rp50 rb"))
            assertNewBroadcastVoucher(0, promousageR.id.promo_tv_desc_mini_card, withText("Min. belanja Rp250 rb"))

            assertNewBroadcastVoucher(1, promousageR.id.promo_tv_type_mini_card, withText("Bebas Ongkir"))
            assertNewBroadcastVoucher(1, promousageR.id.promo_tv_title_mini_card, withText("Rp20 rb"))
            assertNewBroadcastVoucher(1, promousageR.id.promo_tv_desc_mini_card, withText("Min. belanja Rp50 rb"))

            assertNewBroadcastTimeStamp(1, isDisplayed())
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
    fun assert_broadcast_promo_with_hidden_banner() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithHiddenBannerBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastBanner(1, not(isDisplayed()))
            assertNewBroadcastCountdown(1, "Campaign \"Koleksi Spesial\" telah berakhir. Nantikan campaign berikutnya, ya!")
        }
    }

    @Test
    fun assert_broadcast_promo_without_banner() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithoutBannerBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastBanner(1, not(isDisplayed()))
        }
    }

    @Test
    fun assert_broadcast_promo_with_finish_countdown() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithFinishCountdownBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastBanner(1, isDisplayed())
            assertNewBroadcastCountdown(1, "Campaign \"Koleksi Spesial\" telah berakhir. Nantikan campaign berikutnya, ya!")
        }
    }

    @Test
    fun assert_broadcast_without_message() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithoutMessageBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastMessage(
                1,
                not(isDisplayed())
            )
            assertNewBroadcastTimeStamp(1, not(isDisplayed()))
        }
    }

    @Test
    fun assert_broadcast_promo_single_product() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithSingleProductBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastPromoProductSingle(1)
            generalResult {
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_title, withText("Product Attachment 1"))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_price, withText("Rp10.000"))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_slash_price, withText("50.000"))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_discount, withText("80%"))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_icon_product_compact_horizontal_rating, isDisplayed())
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_rating, withText("5.0"))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_product_compact_horizontal_separator, isDisplayed())
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_sold, withText("terjual 12rb"))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_label, not(isDisplayed()))
            }
        }
    }

    @Test
    fun assert_broadcast_promo_single_product_oos() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithSingleProductOOSBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastPromoProductSingle(1)
            generalResult {
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_title, withText("Product Attachment 3"))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_price, withText("Rp30.000"))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_slash_price, not(isDisplayed()))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_discount, not(isDisplayed()))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_icon_product_compact_horizontal_rating, not(isDisplayed()))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_rating, not(isDisplayed()))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_product_compact_horizontal_separator, not(isDisplayed()))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_sold, withText("terjual 12rb"))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_label, withText("Stok Habis"))
            }
        }
    }

    @Test
    fun assert_broadcast_promo_single_product_preorder() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithSingleProductPreOrderBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastPromoProductSingle(1)
            generalResult {
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_title, withText("Product Attachment 2"))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_price, withText("Rp20.000"))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_slash_price, not(isDisplayed()))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_discount, not(isDisplayed()))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_icon_product_compact_horizontal_rating, isDisplayed())
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_rating, withText("5.0"))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_product_compact_horizontal_separator, not(isDisplayed()))
                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_sold, not(isDisplayed()))

                assertViewInRecyclerViewAt(1, R.id.topchat_chatroom_tv_product_compact_horizontal_label, withText("PreOrder"))
            }
        }
    }

    @Test
    fun assert_broadcast_promo_without_product() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithoutProductBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastPromoWithoutProduct(1)
        }
    }

    @Test
    fun assert_broadcast_promo_single_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithSingleVoucherBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastVoucherHeader(1, withText("Makin hemat pakai kupon ini"))
            assertNewBroadcastVoucherDesc(1, withText("Kupon bisa dipilih lewat keranjang, tanpa perlu salin kode."))
            assertNewBroadcastSingleVoucher(1)

            generalResult {
                assertViewInRecyclerViewAt(1, promousageR.id.promo_tv_type_mini_card, withText("Cashback"))
                assertViewInRecyclerViewAt(1, promousageR.id.promo_tv_title_mini_card, withText("Rp50 rb"))
                assertViewInRecyclerViewAt(1, promousageR.id.promo_tv_desc_mini_card, withText("Min. belanja Rp250 rb"))
            }
        }
    }

    @Test
    fun assert_broadcast_promo_without_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastPromoWithoutVoucherBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastPromoVoucher(1, not(isDisplayed()))
            assertNewBroadcastVoucherHeader(1, not(isDisplayed()))
            assertNewBroadcastVoucherDesc(1, not(isDisplayed()))
        }
    }
}

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
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.promousage.R as promousageR

@UiTest
class TopChatRoomBuyerBroadcastFlashSaleTest : TopchatRoomTest() {
    @Test
    fun assert_broadcast_flash_sale_full() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleBuyerResponse
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

            assertNewBroadcastFlashSaleProductCarousel(1, 5)
            // product normal
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardName,
                withText("Product Attachment 1")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardDiscount,
                withText("80%")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardSlashedPrice,
                withText("50.000")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRating,
                withText("5.0")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardLabelCredibility,
                withText("terjual 12rb")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRatingIcon,
                isDisplayed()
            )

            // product pre-order
            assertNewBroadcastFlashSaleProductCarouselItem(
                1,
                productcardR.id.productCardName,
                withText("Product Attachment 2")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                1,
                productcardR.id.productCardLabelPreventiveOverlay,
                withText("PreOrder")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                1,
                productcardR.id.productCardRating,
                withText("5.0")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                1,
                productcardR.id.productCardRatingIcon,
                isDisplayed()
            )

            // product without rating
            broadcastRobot {
                scrollBroadcastFlashSaleProduct(2)
            }
            assertNewBroadcastFlashSaleProductCarouselItem(
                2,
                productcardR.id.productCardName,
                withText("Product Attachment 4")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                2,
                productcardR.id.productCardRatingIcon,
                not(isDisplayed())
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                2,
                productcardR.id.productCardRating,
                not(isDisplayed())
            )

            // product oos
            broadcastRobot {
                scrollBroadcastFlashSaleProduct(3)
            }
            assertNewBroadcastFlashSaleProductCarouselItem(
                3,
                productcardR.id.productCardName,
                withText("Product Attachment 3")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                3,
                productcardR.id.productCardLabelPreventiveBlock,
                withText("Stok Habis")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                3,
                productcardR.id.productCardLabelCredibility,
                withText("terjual 12rb")
            )

            // see more
            broadcastRobot {
                scrollBroadcastFlashSaleProduct(4)
            }
            assertNewBroadcastFlashSaleProductCarouselItem(
                4,
                R.id.topchat_chatroom_broadcast_see_more_tv,
                withText("Lihat Produk Lainnya")
            )

            assertNewBroadcastVoucherHeader(1, withText("Makin hemat pakai kupon ini"))
            assertNewBroadcastVoucherDesc(1, withText("Kupon bisa dipilih lewat keranjang, tanpa perlu salin kode."))
            assertNewBroadcastFlashSaleVoucher(1, isDisplayed())
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
            clickBroadcastFlashSaleProduct(0)
            clickBroadcastFlashSaleProduct(1)
            clickBroadcastFlashSaleProduct(2)
            clickBroadcastFlashSaleProduct(3)
            clickBroadcastFlashSaleSeeMore(4)
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
    fun assert_broadcast_flash_sale_with_hidden_banner() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithHiddenBannerBuyerResponse
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
    fun assert_broadcast_flash_sale_without_banner() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithoutBannerBuyerResponse
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
    fun assert_broadcast_flash_sale_with_finish_countdown() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithFinishCountdownBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastBanner(1, isDisplayed())
        }
    }

    @Test
    fun assert_broadcast_without_message() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithoutMessageBuyerResponse
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
    fun assert_broadcast_flash_sale_single_product() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithSingleProductBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastFlashSaleProductCarousel(1, 1)
            // product normal
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardName,
                withText("Product Attachment 1")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardDiscount,
                withText("80%")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardSlashedPrice,
                withText("50.000")
            )

            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRating,
                withText("5.0")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardLabelCredibility,
                withText("terjual 12rb")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRatingIcon,
                isDisplayed()
            )
        }
    }

    @Test
    fun assert_broadcast_flash_sale_single_product_oos() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithSingleProductOOSBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastFlashSaleProductCarousel(1, 1)
            // product normal
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardName,
                withText("Product Attachment 3")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardDiscount,
                not(isDisplayed())
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardSlashedPrice,
                not(isDisplayed())
            )

            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRating,
                not(isDisplayed())
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardLabelCredibility,
                withText("terjual 12rb")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRatingIcon,
                not(isDisplayed())
            )
        }
    }

    @Test
    fun assert_broadcast_flash_sale_single_product_preorder() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithSingleProductPreOrderBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastFlashSaleProductCarousel(1, 1)
            // product normal
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardName,
                withText("Product Attachment 2")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardDiscount,
                not(isDisplayed())
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardSlashedPrice,
                not(isDisplayed())
            )

            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRating,
                withText("5.0")
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardLabelCredibility,
                not(isDisplayed())
            )
            assertNewBroadcastFlashSaleProductCarouselItem(
                0,
                productcardR.id.productCardRatingIcon,
                isDisplayed()
            )
        }
    }

    @Test
    fun assert_broadcast_flash_sale_without_product() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithoutProductBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastFlashSaleWithoutProduct(1)
        }
    }

    @Test
    fun assert_broadcast_flash_sale_single_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithSingleVoucherBuyerResponse
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
    fun assert_broadcast_flash_sale_without_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.broadcastFlashSaleWithoutVoucherBuyerResponse
        chatAttachmentUseCase.response = chatAttachmentResponse

        // When
        launchChatRoomActivity()
        stubIntents()

        // Then
        broadcastResult {
            assertNewBroadcastFlashSaleVoucher(1, not(isDisplayed()))
            assertNewBroadcastVoucherHeader(1, not(isDisplayed()))
            assertNewBroadcastVoucherDesc(1, not(isDisplayed()))
        }
    }
}

package com.tokopedia.content.product.preview.analytics

import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel

interface ProductPreviewAnalytics {

    /**
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/4459
     * 1 - 18
     */

    interface Factory {
        fun create(productId: String): ProductPreviewAnalytics
    }

    /**
     * 1. swipe left or right to next content / tab
     * 49587
     */
    fun onSwipeContentAndTab(tabName: String, isTabChanged: Boolean)

    /**
     * 2. impress video
     * 49588
     */
    fun onImpressVideo(pageSource: String)

    /**
     * 3. impress ATC button
     * 49589
     */
    fun onImpressATC(pageSource: String)

    /**
     * 4. click content thumbnail in Produk tab
     * 49594
     */
    fun onClickThumbnailProduct()

    /**
     * 5. impress image content
     * 49598
     */
    fun onImpressImage(pageSource: String)

    /**
     * 6. impress Ingatkan Saya button
     * 49600
     */
    fun onImpressRemindMe(pageSource: String)

    /**
     * 7. click Ingatkan Saya button
     * 49601
     */
    fun onClickRemindMe(pageSource: String)

    /**
     * 8. swipe up down to next content in Ulasan tab
     * 49602
     */
    fun onSwipeReviewNextContent()

    /**
     * 9. click account name
     * 49603
     */
    fun onClickReviewAccountName()

    /**
     * 10. Click 3 dots menu
     * 49605
     */
    fun onClickReviewThreeDots()

    /**
     * 11. click Back button to PDP
     * 49606
     */
    fun onClickBackButton(pageSource: String)

    /**
     * 12. click laporkan ulasan in ulasan tab
     * 49650
     */
    fun onClickReviewReport()

    /**
     * 13. click mode nonton in ulasan tab
     * 49651
     */
    fun onClickReviewWatchMode()

    /**
     * 14. click pause/play in video
     * 49845
     */
    fun onClickPauseOrPlayVideo(pageSource: String)

    /**
     * 15. submit report from ulasan tab
     * 49850
     */
    fun onClickSubmitReport()

    /**
     * 16. like/unlike content
     * 49851
     */
    fun onClickLikeOrUnlike()

    /**
     * 17. click ATC without global variant bottomsheet
     * 50427
     */
    fun onAtcWithoutVariant(bottomNavUiModel: BottomNavUiModel, currentTab: String)

    /**
     * 18. click ATC to open global variant bottomsheet
     * 50428
     */
    fun onAtcVariant(bottomNavUiModel: BottomNavUiModel, currentTab: String)

    /**
     * 19. click bottom nav
     * 50664
     */
    fun onClickBottomNav(model: BottomNavUiModel)
}

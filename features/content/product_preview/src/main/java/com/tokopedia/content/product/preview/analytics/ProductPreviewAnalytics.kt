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
    fun onSwipeContentAndTab()

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
     * 4. click ATC button
     * 49590
     */
    fun onClickATC(pageSource: String, bottomNavUiModel: BottomNavUiModel)

    /**
     * 5. click content thumbnail in Produk tab
     * 49594
     */
    fun onClickThumbnailProduct()

    /**
     * 6. impress image content
     * 49598
     */
    fun onImpressImage(pageSource: String)

    /**
     * 7. impress Ingatkan Saya button
     * 49600
     */
    fun onImpressRemindMe(pageSource: String)

    /**
     * 8. click Ingatkan Saya button
     * 49601
     */
    fun onClickRemindMe(pageSource: String)

    /**
     * 9. swipe up down to next content in Ulasan tab
     * 49602
     */
    fun onSwipeReviewNextContent()

    /**
     * 10. click account name
     * 49603
     */
    fun onClickReviewAccountName()

    /**
     * 11. Click 3 dots menu
     * 49605
     */
    fun onClickReviewThreeDots()

    /**
     * 12. click Back button to PDP
     * 49606
     */
    fun onClickBackButton(pageSource: String)

    /**
     * 13. click ATC to global variant bottomsheet
     * 49607
     */
    fun onClickVariantGBVS(pageSource: String)

    /**
     * 14. click laporkan ulasan in ulasan tab
     * 49650
     */
    fun onClickReviewReport()

    /**
     * 15. click mode nonton in ulasan tab
     * 49651
     */
    fun onClickReviewWatchMode()

    /**
     * 16. click pause/play in video
     * 49845
     */
    fun onClickPauseOrPlayVideo(pageSource: String)

    /**
     * 17. submit report from ulasan tab
     * 49850
     */
    fun onClickSubmitReport()

    /**
     * 18. like/unlike content
     * 49851
     */
    fun onClickLikeOrUnlike()
}

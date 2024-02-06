package com.tokopedia.content.product.preview.analytics

interface ProductPreviewAnalytics {

    /**
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/4459
     * 1 - 18
     */

    /**
     * 1. swipe left or right to next content / tab
     * 49587
     */
    fun onSwipeContentAndTab(productId: String)

    /**
     * 2. impress video
     * 49588
     */
    fun onImpressVideo(productId: String)

    /**
     * 3. impress ATC button
     * 49589
     */
    fun onImpressATC(productId: String)

    /**
     * 4. click ATC button
     * 49590
     */
    fun onClickATC(productId: String)

    /**
     * 5. click content thumbnail in Produk tab
     * 49594
     */
    fun onClickThumbnailProduct(productId: String)

    /**
     * 6. impress image content
     * 49598
     */
    fun onImpressImageContent(productId: String)

    /**
     * 7. impress Ingatkan Saya button
     * 49600
     */
    fun onImpressRemindMe(productId: String)

    /**
     * 8. click Ingatkan Saya button
     * 49601
     */
    fun onClickRemindMe(productId: String)

    /**
     * 9. swipe up down to next content in Ulasan tab
     * 49602
     */
    fun onSwipeReviewNextContent(productId: String)

    /**
     * 10. click account name
     * 49603
     */
    fun onClickReviewAccountName(productId: String)

    /**
     * 11. Click 3 dots menu
     * 49605
     */
    fun onClickReviewThreeDots(productId: String)

    /**
     * 12. click Back button to PDP
     * 49606
     */
    fun onClickBackButton(productId: String)

    /**
     * 13. click ATC to global variant bottomsheet
     * 49607
     */
    fun onOpenGBVS(productId: String)

    /**
     * 14. click laporkan ulasan in ulasan tab
     * 49650
     */
    fun onClickReviewReport(productId: String)

    /**
     * 15. click mode nonton in ulasan tab
     * 49651
     */
    fun onClickReviewWatchMode(productId: String)

    /**
     * 16. click pause/play in video
     * 49845
     */
    fun onClickPauseOrPlayVideo(productId: String)

    /**
     * 17. submit report from ulasan tab
     * 49850
     */
    fun onClickSubmitReport(productId: String)

    /**
     * 18. like/unlike content
     * 49851
     */
    fun onClickLikeOrUnlike(productId: String)
}

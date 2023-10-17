package com.tokopedia.stories.creation.analytic


/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
interface StoriesCreationAnalytic {

    /**
     * MyNakama
     * MA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/4305
     * SA : https://mynakama.tokopedia.com/datatracker/requestdetail/view/4326
     */

    /** Row 1 */
    fun openScreenCreationPage(partnerId: String, partnerType: String, storyId: String)

    /** Row 2 */
    fun clickAddProduct(partnerId: String, partnerType: String)

    /** Row 3 */
    fun clickUpload(partnerId: String, partnerType: String, storyId: String)
}

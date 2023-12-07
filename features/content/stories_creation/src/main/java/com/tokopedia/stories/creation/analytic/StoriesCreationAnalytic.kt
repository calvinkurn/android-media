package com.tokopedia.stories.creation.analytic

import com.tokopedia.content.common.ui.model.ContentAccountUiModel


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
    fun openScreenCreationPage(account: ContentAccountUiModel, storyId: String)

    /** Row 2 */
    fun clickAddProduct(account: ContentAccountUiModel)

    /** Row 3 */
    fun clickUpload(account: ContentAccountUiModel, storyId: String)
}

package com.tokopedia.usercomponents.tokopediaplus.common

import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel

class TokopediaPlusParam (
    /**
     * page source
     */
    val pageSource: String,
    /**
     * use this class for get data
     * @see [com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase]
     */
    val tokopediaPlusDataModel: TokopediaPlusDataModel
)
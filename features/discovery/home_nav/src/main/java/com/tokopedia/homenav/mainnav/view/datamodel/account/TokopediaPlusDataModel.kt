package com.tokopedia.homenav.mainnav.view.datamodel.account

import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam

/**
 * Created by frenzel
 */
data class TokopediaPlusDataModel (
    var tokopediaPlusParam: TokopediaPlusParam? = null,

    /**
     * Status
     */
    var isGetTokopediaPlusLoading: Boolean = false,
    var tokopediaPlusError: Throwable? = null
)
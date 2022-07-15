package com.tokopedia.usercomponents.tokopediaplus.common

import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel


interface TokopediaPlusListener {
    fun onClick(pageSource: String, tokopediaPlusDataModel: TokopediaPlusDataModel)
    fun onRetry()
}
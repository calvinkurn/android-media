package com.tokopedia.feedcomponent.view.viewmodel.relatedpost

import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 2019-08-13.
 */
data class RelatedPostItemViewModel @JvmOverloads constructor(
        val data: FeedPostRelated.Datum,
        var impressionHolder: ImpressHolder = ImpressHolder()
)

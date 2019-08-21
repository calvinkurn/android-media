package com.tokopedia.feedcomponent.view.adapter.relatedpost

import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel

/**
 * @author by milhamj on 2019-08-12.
 */
interface RelatedPostTypeFactory {
    fun type (relatedPostViewModel: RelatedPostViewModel): Int
}
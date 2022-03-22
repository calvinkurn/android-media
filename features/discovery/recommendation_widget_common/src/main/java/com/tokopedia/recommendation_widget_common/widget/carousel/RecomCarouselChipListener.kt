package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip

/**
 * Created by yfsx on 01/12/21.
 */
//listener for chip carousel if recom didn't use page name for chips
// (recom and chips data comes from fragment viewmodel)
interface RecomCarouselChipListener {

    fun onChipClicked(annotationChip: AnnotationChip, position: Int)
}
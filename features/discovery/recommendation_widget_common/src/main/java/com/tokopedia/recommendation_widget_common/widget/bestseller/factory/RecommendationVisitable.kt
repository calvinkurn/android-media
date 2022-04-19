package com.tokopedia.recommendation_widget_common.widget.bestseller.factory

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Lukas on 05/11/20.
 */
interface RecommendationVisitable : Visitable<RecommendationTypeFactory>{
    fun visitableId(): String?
    fun equalsWith(b: Any?): Boolean
    fun getChangePayloadFrom(b: Any?): Bundle?
}
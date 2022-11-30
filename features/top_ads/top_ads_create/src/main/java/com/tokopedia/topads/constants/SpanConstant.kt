package com.tokopedia.topads.constants

import androidx.annotation.IntDef


@Retention(AnnotationRetention.SOURCE)
@IntDef(SpanConstant.COLOR_SPAN,SpanConstant.TYPEFACE_SPAN)
@Target(AnnotationTarget.FIELD,AnnotationTarget.CONSTRUCTOR,AnnotationTarget.VALUE_PARAMETER)
annotation class SpanConstant{
    companion object{
        const val COLOR_SPAN = 0
        const val TYPEFACE_SPAN = 1
    }
}

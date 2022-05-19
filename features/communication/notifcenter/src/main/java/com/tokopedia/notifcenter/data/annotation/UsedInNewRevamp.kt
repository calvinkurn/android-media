package com.tokopedia.notifcenter.data.annotation

import androidx.annotation.IntDef

/**
 * Indicate that the old class is used on the new revamp page
 */
@Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.TYPEALIAS
)
annotation class UsedInNewRevamp
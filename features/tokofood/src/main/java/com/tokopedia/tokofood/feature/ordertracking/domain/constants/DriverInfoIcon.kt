package com.tokopedia.tokofood.feature.ordertracking.domain.constants

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class DriverInfoIcon {
    companion object {
        const val VACCINE = "enum_vaksin"
        const val TERMOMETER = "enum_termometer"
        const val TELEPON = "enum_telepon"
    }
}
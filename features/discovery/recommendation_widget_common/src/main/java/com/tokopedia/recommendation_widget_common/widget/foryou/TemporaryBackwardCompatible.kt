package com.tokopedia.recommendation_widget_common.widget.foryou

/**
 * A temporary class marker.
 *
 * Because a few models and classes still in-use in certain old recommendation components,
 * Hence, let's mark it as [TemporaryBackwardCompatible] as of now until the global component
 * got rollout for 100% users with free crash rate.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class TemporaryBackwardCompatible

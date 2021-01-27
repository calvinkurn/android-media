package com.tokopedia.play.widget.di

import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import dagger.MapKey

/**
 * Created by jegul on 08/10/20
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class PlayWidgetSizeKey(val value: PlayWidgetSize)
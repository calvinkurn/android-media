package com.tokopedia.content.common.producttag.di.key

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class FragmentKey(val value: KClass<out Fragment>)
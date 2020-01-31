package com.tokopedia.hotel.destination.view.viewmodel

import com.tokopedia.usecase.coroutines.Result

/**
 * @author by jessica on 05/04/19
 */

sealed class RecentSearchState<out T: Any>
object Shimmering: RecentSearchState<Nothing>()
data class Loaded<out T: Any>(val data: Result<T>): RecentSearchState<T>()

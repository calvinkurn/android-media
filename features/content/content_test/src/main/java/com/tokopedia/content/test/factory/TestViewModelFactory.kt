package com.tokopedia.content.test.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by kenny.hadisaputra on 17/03/22
 */
class TestViewModelFactory (
    private val viewModelProviders: Map<Class<out ViewModel>, () -> ViewModel>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModelProviders[modelClass] ?: viewModelProviders.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
        ?: throw IllegalArgumentException("unknown model class $modelClass")

        return try {
            creator() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
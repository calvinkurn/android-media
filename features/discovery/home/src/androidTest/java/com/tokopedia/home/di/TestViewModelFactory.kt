package com.tokopedia.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TestViewModelFactory(
    private val viewModelProviders: Map<Class<out ViewModel>, () -> ViewModel>
) : ViewModelProvider.Factory {

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

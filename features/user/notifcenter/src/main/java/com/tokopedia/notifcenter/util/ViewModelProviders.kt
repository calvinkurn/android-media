package com.tokopedia.notifcenter.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

typealias Factory = ViewModelProvider.Factory

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
        provider: Factory
) = ViewModelProviders
        .of(this, provider)
        .get(VM::class.java)
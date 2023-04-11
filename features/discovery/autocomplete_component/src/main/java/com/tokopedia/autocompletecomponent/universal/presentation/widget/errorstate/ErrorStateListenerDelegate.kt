package com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContext
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModel
import com.tokopedia.autocompletecomponent.util.contextprovider.ContextProvider
import com.tokopedia.autocompletecomponent.util.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

class ErrorStateListenerDelegate @Inject constructor(
    @UniversalSearchContext context: Context?,
): ErrorStateListener,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onReload() {
        val vm =
            ViewModelProvider(context as FragmentActivity).get(UniversalSearchViewModel::class.java)
        vm.loadData()
    }
}
package com.tokopedia.topads.auto.view.fragment.budget

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.tokopedia.topads.auto.data.repository.AutoTopAdsRepositoy

/**
 * Author errysuprayogi on 16,May,2019
 */
class DailyBudgetViewModelFactory(
        private val context: Context,
        private val repositoy: AutoTopAdsRepositoy
): ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DailyBudgetViewModel(context, repositoy) as T
    }
}
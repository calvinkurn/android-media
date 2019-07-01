package com.tokopedia.browse.categoryNavigation

import android.content.Context
import android.util.Log
import com.tokopedia.browse.categoryNavigation.domain.usecase.GetCategoryConfigUseCase
import rx.Subscriber

object CategoryNavigationConfig {

    var isNewCategoryEnabled: Boolean = false

    fun updateCategoryConfig(context: Context) {

        GetCategoryConfigUseCase(context).execute(object : Subscriber<Boolean>() {
            override fun onNext(isRevampBelanja: Boolean) {
                isNewCategoryEnabled = isRevampBelanja
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                isNewCategoryEnabled = false
            }
        })


    }
}


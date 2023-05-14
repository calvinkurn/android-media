package com.tokopedia.home_account.explicitprofile.di.component

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object ExplicitProfileComponentsBuilder {
    fun getComponent(context: Context): ExplicitProfileComponents {
        return DaggerExplicitProfileComponents.builder()
            .baseAppComponent((context as BaseMainApplication).baseAppComponent)
            .build()
    }
}

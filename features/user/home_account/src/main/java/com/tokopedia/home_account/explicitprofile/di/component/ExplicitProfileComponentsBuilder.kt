package com.tokopedia.home_account.explicitprofile.di.component

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.home_account.explicitprofile.di.module.ExplicitProfileModules

object ExplicitProfileComponentsBuilder {
    fun getComponent(context: Context) : ExplicitProfileComponents {
        return DaggerExplicitProfileComponents.builder()
            .baseAppComponent((context as BaseMainApplication).baseAppComponent)
            .explicitProfileModules(ExplicitProfileModules)
            .build()
    }
}
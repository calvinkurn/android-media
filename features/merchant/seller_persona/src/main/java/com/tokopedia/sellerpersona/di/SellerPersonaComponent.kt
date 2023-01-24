package com.tokopedia.sellerpersona.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerpersona.di.module.SellerPersonaModule
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity
import com.tokopedia.sellerpersona.view.fragment.PersonaOpeningFragment
import com.tokopedia.sellerpersona.view.fragment.PersonaQuestionnaireFragment
import com.tokopedia.sellerpersona.view.fragment.PersonaResultFragment
import dagger.Component

/**
 * Created by @ilhamsuaib on 24/01/23.
 */

@SellerPersonaScope
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [
        SellerPersonaModule::class
    ]
)
interface SellerPersonaComponent {

    fun inject(activity: SellerPersonaActivity)

    fun inject(fragment: PersonaQuestionnaireFragment)

    fun inject(fragment: PersonaOpeningFragment)

    fun inject(fragment: PersonaResultFragment)
}
package com.tokopedia.travel.country_code.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import dagger.Component

/**
 * @author by furqan on 23/12/2019
 */
@TravelCountryCodeScope
@Component(modules = [TravelCountryCodeModule::class, TravelCountryCodeViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TravelCountryCodeComponent {

    fun graphQlRepository(): GraphqlRepository

    fun inject(phoneCodePickerFragment: PhoneCodePickerFragment)

}
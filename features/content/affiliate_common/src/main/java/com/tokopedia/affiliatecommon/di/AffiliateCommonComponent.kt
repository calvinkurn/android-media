package com.tokopedia.affiliatecommon.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * @author by yfsx on 29/03/19.
 */
@AffiliateCommonScope
@Component(
        modules = arrayOf(AffiliateCommonModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface AffiliateCommonComponent {
}
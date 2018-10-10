package com.tokopedia.digital.nostylecategory.digitalcategory.di;

import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.MitraDigitalCategoryFragment;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.MitraDigitalOperatorChooserFragment;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.MitraDigitalProductChooserFragment;


import dagger.Component;

/**
 * Created by Rizky on 30/08/18.
 */
@MitraDigitalCategoryScope
@Component(dependencies = DigitalComponent.class, modules = MitraDigitalCategoryModule.class)
public interface MitraDigitalCategoryComponent {

    void inject(MitraDigitalCategoryFragment mitraDigitalCategoryFragment);

    void inject(MitraDigitalOperatorChooserFragment mitraDigitalOperatorChooserFragment);

    void inject(MitraDigitalProductChooserFragment mitraDigitalProductChooserFragment);

}
package com.tokopedia.mitra.digitalcategory.di;

import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.mitra.digitalcategory.presentation.fragment.MitraDigitalCategoryFragment;
import com.tokopedia.mitra.digitalcategory.presentation.fragment.MitraDigitalOperatorChooserFragment;
import com.tokopedia.mitra.digitalcategory.presentation.fragment.MitraDigitalProductChooserFragment;

import dagger.Component;

/**
 * Created by Rizky on 30/08/18.
 */
@AgentDigitalCategoryScope
@Component(dependencies = DigitalComponent.class, modules = AgentDigitalCategoryModule.class)
public interface AgentDigitalCategoryComponent {

    void inject(MitraDigitalCategoryFragment mitraDigitalCategoryFragment);

    void inject(MitraDigitalOperatorChooserFragment mitraDigitalOperatorChooserFragment);

    void inject(MitraDigitalProductChooserFragment mitraDigitalProductChooserFragment);

}
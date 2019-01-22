package com.tokopedia.digital.nostylecategory.digitalcategory.di;

import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.DigitalCategoryNoStyleFragment;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.DigitalOperatorChooserNoStyleFragment;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.DigitalProductChooserNoStyleFragment;


import dagger.Component;

/**
 * Created by Rizky on 30/08/18.
 */
@DigitalCategoryNoStyleScope
@Component(dependencies = DigitalComponent.class, modules = DigitalCategoryNoStyleModule.class)
public interface DigitalCategoryNoStyleComponent {

    void inject(DigitalCategoryNoStyleFragment digitalCategoryNoStyleFragment);

    void inject(DigitalOperatorChooserNoStyleFragment digitalOperatorChooserNoStyleFragment);

    void inject(DigitalProductChooserNoStyleFragment digitalProductChooserNoStyleFragment);

}
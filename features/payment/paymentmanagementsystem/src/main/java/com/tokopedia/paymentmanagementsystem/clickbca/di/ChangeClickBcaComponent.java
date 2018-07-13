package com.tokopedia.paymentmanagementsystem.clickbca.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.paymentmanagementsystem.clickbca.view.ChangeClickBcaFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ChangeClickBcaScope
@Component(modules = ChangeClickBcaModule.class, dependencies = BaseAppComponent.class)
public interface ChangeClickBcaComponent {
    void inject(ChangeClickBcaFragment changeClickBcaFragment);
}

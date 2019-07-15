package com.tokopedia.instantdebitbca.data.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.instantdebitbca.data.view.fragment.EditLimitFragment;
import com.tokopedia.instantdebitbca.data.view.fragment.InstantDebitBcaFragment;

import dagger.Component;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
@InstantDebitBcaScope
@Component(modules = InstantDebitBcaModule.class, dependencies = BaseAppComponent.class)
public interface InstantDebitBcaComponent {

    void inject(InstantDebitBcaFragment instantDebitBcaFragment);
    void inject(EditLimitFragment editLimitFragment);
}

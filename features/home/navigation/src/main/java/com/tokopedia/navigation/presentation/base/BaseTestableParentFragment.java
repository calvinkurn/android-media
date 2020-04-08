package com.tokopedia.navigation.presentation.base;

import androidx.annotation.RestrictTo;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

/**
 * Created by meta on 19/06/18.
 */
public abstract class BaseTestableParentFragment<F, E extends BaseDaggerPresenter> extends BaseParentFragment {

    @RestrictTo(RestrictTo.Scope.TESTS)
    public abstract void reInitInjector(F component);

    @RestrictTo(RestrictTo.Scope.TESTS)
    public abstract E getPresenter();

    @RestrictTo(RestrictTo.Scope.TESTS)
    public abstract void setPresenter(F presenter);
}

package com.tokopedia.abstraction.base.view.fragment;

import androidx.annotation.RestrictTo;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;

/**
 * Created by meta on 19/06/18.
 */
public abstract class TestableTkpdBaseV4Fragment<F, E extends BaseDaggerPresenter>  extends TkpdBaseV4Fragment {

    @RestrictTo(RestrictTo.Scope.TESTS)
    public abstract void reInitInjector(F component);

    @RestrictTo(RestrictTo.Scope.TESTS)
    public abstract E getPresenter();

    @RestrictTo(RestrictTo.Scope.TESTS)
    public abstract void setPresenter(F presenter);
}

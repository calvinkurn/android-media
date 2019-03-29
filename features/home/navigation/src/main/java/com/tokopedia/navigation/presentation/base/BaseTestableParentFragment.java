package com.tokopedia.navigation.presentation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;

import dagger.Component;

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

package com.tokopedia.withdraw.di;

import com.tokopedia.withdraw.view.fragment.WithdrawPasswordFragment;

import dagger.Component;

/**
 * @author by StevenFredian on 30/07/18.
 */


@DoWithdrawScope
@Component(modules = DoWithdrawModule.class, dependencies = WithdrawComponent.class)
public interface DoWithdrawComponent {

    void inject(WithdrawPasswordFragment fragment);
}

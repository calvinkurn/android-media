package com.tokopedia.reksadana.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.reksadana.view.fragment.BuyFragment;
import com.tokopedia.reksadana.view.fragment.DashBoardFragment;
import com.tokopedia.reksadana.view.fragment.RegisterFragment;
import com.tokopedia.reksadana.view.fragment.SellFragment;
import com.tokopedia.reksadana.view.fragment.TxListFragment;

import dagger.Component;

@ReksaDanaModuleScope
@Component(dependencies = {BaseAppComponent.class})
public interface ReksaDanaComponent {
    void inject(RegisterFragment reksaDanaFragment);
    void inject(DashBoardFragment dashBoardFragment);
    void inject(BuyFragment dashBoardFragment);
    void inject(SellFragment dashBoardFragment);
    void inject(TxListFragment dashBoardFragment);
}

package com.tokopedia.mitra.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public abstract class MitraBaseFragment<P extends CustomerPresenter<V>, V extends CustomerView> extends BaseDaggerFragment{
    protected P presenter;

    public abstract P getPresenter();

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(getPresenter());
    }
}

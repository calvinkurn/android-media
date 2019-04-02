package com.tokopedia.tokocash.autosweepmf.view.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;

import javax.inject.Inject;

public class AutoSweepLimitMapper {
    @Inject
    public AutoSweepLimitMapper() {
    }

    @Nullable
    public AutoSweepLimit transform(@NonNull AutoSweepLimitDomain domain) {
        AutoSweepLimit view = new AutoSweepLimit();
        view.setStatus(domain.isStatus());
        view.setMessage(domain.getMessage());
        view.setSuccess(domain.isSuccess());
        view.setCode(domain.getCode());

        return view;
    }
}

package com.tokopedia.tokocash.autosweepmf.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.data.model.AutoSweepLimitEntity;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;

import javax.inject.Inject;

public class AutoSweepLimitMapperEntity {
    @Inject
    public AutoSweepLimitMapperEntity() {
    }

    @Nullable
    public AutoSweepLimitDomain transform(@NonNull AutoSweepLimitEntity entity) {
        AutoSweepLimitDomain domain = new AutoSweepLimitDomain();
        domain.setStatus(entity.isStatus());

        if (entity.getResult() != null) {
            domain.setMessage(entity.getResult().getMessage());
            domain.setCode(entity.getResult().getCode());
            domain.setSuccess(entity.getResult().isSuccess());
        }
        return domain;
    }
}

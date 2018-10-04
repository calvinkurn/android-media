package com.tokopedia.tokocash.autosweepmf.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.data.CommonConstant;
import com.tokopedia.tokocash.autosweepmf.data.model.AutoSweepDetailEntity;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;

import javax.inject.Inject;

public class AutoSweepDetailMapperEntity {
    @Inject
    public AutoSweepDetailMapperEntity() {
    }

    @Nullable
    public AutoSweepDetailDomain transform(@NonNull AutoSweepDetailEntity entity) {
        AutoSweepDetailDomain domain = new AutoSweepDetailDomain();

        domain.setAccountStatus(entity.getAccountStatus());
        domain.setAmountLimit(entity.getAmountLimit());
        domain.setAutoSweepStatus(entity.getAutoSweepStatus());
        domain.setBalance(entity.getBalance());
        domain.setDashboardLink(entity.getDashboardLink());
        domain.setMfInfoLink(entity.getMfInfoLink());

        if (entity.getShowAutoSweep() == CommonConstant.TRUE_INT) {
            domain.setEnable(true);
        } else {
            domain.setEnable(false);
        }

        if (entity.getText() != null) {
            domain.setTitle(entity.getText().getTitle());
            domain.setContent(entity.getText().getContent());
            domain.setTooltipContent(entity.getText().getTooltipContent());
            domain.setDescription(entity.getText().getDescription());

            if (entity.getText().getDialog() != null) {
                domain.setDialogContent(entity.getText().getDialog().getContent());
                domain.setDialogTitle(entity.getText().getDialog().getTitle());
                domain.setDialogLabelPositive(entity.getText().getDialog().getPositiveLabel());
                domain.setDialogLabelNegative(entity.getText().getDialog().getNegativeLabel());
                domain.setDialogNegativeButtonLink(entity.getText().getDialog().getDialogNegativeLink());
            }
        }

        if (entity.getResult() != null) {
            domain.setMessage(entity.getResult().getMessage());
            domain.setCode(entity.getResult().getCode());
            domain.setSuccess(entity.getResult().isSuccess());
        }

        return domain;
    }
}

package com.tokopedia.tokocash.autosweepmf.data.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.data.CommonConstant;
import com.tokopedia.tokocash.autosweepmf.data.model.ResponseAutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;

import javax.inject.Inject;

public class AutoSweepDetailMapperEntity {
    @Inject
    public AutoSweepDetailMapperEntity() {
    }

    @Nullable
    public AutoSweepDetailDomain transform(@NonNull ResponseAutoSweepDetail entiy) {
        AutoSweepDetailDomain domain = new AutoSweepDetailDomain();

        if (entiy.getData() != null) {
            domain.setAccountStatus(entiy.getData().getAccountStatus());
            domain.setAmountLimit(entiy.getData().getAmountLimit());
            domain.setAutoSweepStatus(entiy.getData().getAutoSweepStatus());
            domain.setBalance(entiy.getData().getBalance());
            domain.setDashboardLink(entiy.getData().getDashboardLink());
            domain.setMfInfoLink(entiy.getData().getMfInfoLink());

            if (entiy.getData().getShowAutoSweep() == CommonConstant.TRUE_INT) {
                domain.setEnable(true);
            } else {
                domain.setEnable(false);
            }

            if (entiy.getData().getText() != null) {
                domain.setTitle(entiy.getData().getText().getTitle());
                domain.setContent(entiy.getData().getText().getContent());
                domain.setTooltipContent(entiy.getData().getText().getTooltipContent());
                domain.setDescription(entiy.getData().getText().getDescription());

                if (entiy.getData().getText().getDialog() != null) {
                    domain.setDialogContent(entiy.getData().getText().getDialog().getContent());
                    domain.setDialogTitle(entiy.getData().getText().getDialog().getTitle());
                    domain.setDialogLabelPositive(entiy.getData().getText().getDialog().getPositiveLabel());
                    domain.setDialogLabelNegative(entiy.getData().getText().getDialog().getNegativeLabel());
                    domain.setDialogNegativeButtonLink(entiy.getData().getText().getDialog().getDialogNegativeLink());
                }
            }
        }

        domain.setMessage(entiy.getMessage());
        domain.setError(entiy.getError());
        domain.setCode(entiy.getCode());
        domain.setLatency(entiy.getLatency());
        return domain;
    }
}

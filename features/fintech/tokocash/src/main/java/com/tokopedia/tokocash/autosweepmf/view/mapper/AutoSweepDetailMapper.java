package com.tokopedia.tokocash.autosweepmf.view.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail;

import javax.inject.Inject;

public class AutoSweepDetailMapper {
    @Inject
    public AutoSweepDetailMapper() {
    }

    @Nullable
    public AutoSweepDetail transform(@NonNull AutoSweepDetailDomain domain) {
        AutoSweepDetail view = new AutoSweepDetail();
        view.setAccountStatus(domain.getAccountStatus());
        view.setAmountLimit(domain.getAmountLimit());
        view.setAutoSweepStatus(domain.getAutoSweepStatus());
        view.setBalance(domain.getBalance());
        view.setTitle(domain.getTitle());
        view.setContent(domain.getContent());
        view.setTooltipContent(domain.getTooltipContent());
        view.setMessage(domain.getMessage());
        view.setSuccess(domain.isSuccess());
        view.setCode(domain.getCode());
        view.setEnable(domain.isEnable());
        view.setDashboardLink(domain.getDashboardLink());
        view.setDescription(domain.getDescription());
        view.setDialogContent(domain.getDialogContent());
        view.setDialogTitle(domain.getDialogTitle());
        view.setDialogLabelPositive(domain.getDialogLabelPositive());
        view.setDialogLabelNegative(domain.getDialogLabelNegative());
        view.setDialogNegativeButtonLink(domain.getDialogNegativeButtonLink());
        view.setMfInfoLink(domain.getMfInfoLink());
        return view;
    }
}

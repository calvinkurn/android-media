package com.tokopedia.topads.auto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.topads.auto.R;
import com.tokopedia.topads.auto.data.TopAdsShopInfo;
import com.tokopedia.topads.auto.data.TopadsBidInfo;
import com.tokopedia.topads.auto.repository.AutoAdsRepository;

import java.text.DecimalFormat;

/**
 * Author errysuprayogi on 09,May,2019
 */
public class DailyBudgetViewModel extends AndroidViewModel {

    private AutoAdsRepository repository;
    public static final double BUDGET_MULTIPLE_BY = 1000;
    private MutableLiveData<TopadsBidInfo> bidInfo;

    public DailyBudgetViewModel(@NonNull Application application) {
        super(application);
        if(repository!=null){
            return;
        }
        repository = AutoAdsRepository.getInstance();
        bidInfo = repository.getBidInfo();
    }

    public LiveData<TopadsBidInfo> getBidInfo() {
        return bidInfo;
    }

    public String getPotentialImpression(double minBid, double maxBid, double bid) {
        return String.format("%,.0f - %,.0f", calculateImpression(maxBid, bid),
                calculateImpression(minBid, bid));
    }

    private double calculateImpression(double bid, double val) {
        return ((100 / 2.5) * (val / bid));
    }

    public String checkBudget(double number, double minDailyBudget, double maxDailyBudget) {
        if (number <= 0) {
            return getApplication().getString(R.string.error_empty_budget);
        } else if (number < minDailyBudget) {
            return String.format(getApplication().getString(R.string.error_minimum_budget), minDailyBudget);
        } else if (number > maxDailyBudget) {
            return String.format(getApplication().getString(R.string.error_maximum_budget), maxDailyBudget);
        } else if (number < maxDailyBudget && number > minDailyBudget && number % BUDGET_MULTIPLE_BY != 0) {
            return getApplication().getString(R.string.error_multiply_budget, String.valueOf(BUDGET_MULTIPLE_BY));
        } else {
            return null;
        }
    }
}

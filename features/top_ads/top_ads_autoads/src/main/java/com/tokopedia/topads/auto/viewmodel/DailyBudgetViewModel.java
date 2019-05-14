package com.tokopedia.topads.auto.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.topads.auto.R;

import java.text.DecimalFormat;

/**
 * Author errysuprayogi on 09,May,2019
 */
public class DailyBudgetViewModel extends AndroidViewModel {

    public static final double BUDGET_MULTIPLE_BY = 1000;
    private LiveData<String> potentialImpression;

    public DailyBudgetViewModel(@NonNull Application application) {
        super(application);
    }

    public String getPotentialImpression(double minBid, double maxBid, double bid) {
        return String.format("%,.0f - %,.0f", calculateImpression(maxBid, bid),
                calculateImpression(minBid, bid));
    }

    private double calculateImpression(double bid, double val) {
        return ((100 / 2.5) * (val / bid));
    }

    public String checkBudget(double number, int minDailyBudget, int maxDailyBudget) {
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

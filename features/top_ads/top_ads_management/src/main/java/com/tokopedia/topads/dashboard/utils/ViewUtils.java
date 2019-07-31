package com.tokopedia.topads.dashboard.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException;
import com.tokopedia.product.manage.item.common.util.TomeException;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.data.exception.ResponseErrorException;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ViewUtils {
    public static String getErrorMessage(Context context, Throwable t) {
        String errorMessage = getErrorMessage(t);
        if (TextUtils.isEmpty(errorMessage)) {
            return getGeneralErrorMessage(context, t);
        } else {
            return errorMessage;
        }
    }

    public static String getErrorMessage(Throwable t) {
        String errorMessage = null;
        if (t instanceof ResponseErrorException) {
            errorMessage = ((ResponseErrorException) t).getErrorList().get(0).getDetail();
        }
        return errorMessage;
    }

    public static String getClickBudgetError(Context context, double clickBudget, double multiply, double maxBid, double minBid) {
        if (clickBudget <= 0) {
            return context.getString(R.string.error_top_ads_click_budget_must_be_filled);
        }
        return getDefaultClickBudgetError(context, clickBudget, maxBid, minBid);
    }

    public static String getKeywordClickBudgetError(Context context, double clickBudget) {
        if (clickBudget < TopAdsConstant.BUDGET_KEYWORD_MULTIPLE_BY) {
            return context.getString(R.string.error_top_ads_click_budget_minimum, String.valueOf(TopAdsConstant.BUDGET_KEYWORD_MULTIPLE_BY));
        }
        return getKeywordDefaultClickBudgetError(context, clickBudget);
    }

    public static String getKeywordDefaultClickBudgetError(Context context, double clickBudget) {
        if (clickBudget % TopAdsConstant.BUDGET_MULTIPLE_BY != 0) {
            return context.getString(R.string.error_top_ads_click_budget_multiple_by, String.valueOf(TopAdsConstant.BUDGET_MULTIPLE_BY));
        }
        if (clickBudget > TopAdsConstant.BUDGET_KEYWORD_MAX) {
            return context.getString(R.string.error_top_ads_click_budget_max, CurrencyFormatter.formatRupiah(String.valueOf(TopAdsConstant.BUDGET_KEYWORD_MAX)));
        }
        return null;
    }

    public static String getDefaultClickBudgetError(Context context, double clickBudget, double maxBid, double minBid) {
        if (clickBudget < maxBid && clickBudget > minBid && clickBudget % TopAdsConstant.BUDGET_MULTIPLE_BY != 0) {
            return context.getString(R.string.error_top_ads_click_budget_multiple_by, String.valueOf(TopAdsConstant.BUDGET_MULTIPLE_BY));
        }
        if (clickBudget > maxBid) {
            return context.getString(R.string.error_top_ads_click_budget_max, CurrencyFormatter.formatRupiah(String.format("%.0f", maxBid)));
        }
        if (clickBudget < minBid) {
            return context.getString(R.string.error_top_ads_click_budget_minimum, CurrencyFormatter.formatRupiah(String.format("%.0f", minBid)));
        }
        return null;
    }

    public static String getDailyBudgetError(Context context, double minDailyBudget, double suggestionBid, double multiply, double dailyBudget) {
        if (dailyBudget <= 0) {
            return context.getString(R.string.error_top_ads_daily_budget_cannot_empyt);
        }
        double calc = suggestionBid * multiply;
        if(dailyBudget < calc && calc >= minDailyBudget){
            return context.getString(R.string.error_top_ads_daily_budget_minimal, CurrencyFormatter.formatRupiah(String.format("%.0f", calc)));
        } else if (dailyBudget < calc && calc <= minDailyBudget){
            return context.getString(R.string.error_top_ads_daily_budget_minimal, CurrencyFormatter.formatRupiah(String.format("%.0f", minDailyBudget)));
        }
        return null;
    }

    public static String getGeneralErrorMessage(@NonNull Context context, Throwable t) {
        if (t instanceof ResponseV4ErrorException) {
            return ((ResponseV4ErrorException) t).getErrorList().get(0);
        } else if (t instanceof ResponseErrorException) {
            return getErrorMessage(t);
        } else if (t instanceof TomeException) {
            return ((TomeException) t).getMessageError().get(0);
        } else if (t instanceof UnknownHostException) {
            return context.getString(com.tokopedia.seller.R.string.msg_no_connection);
        } else if (t instanceof SocketTimeoutException) {
            return context.getString(com.tokopedia.seller.R.string.default_request_error_timeout);
        } else if (t instanceof IOException) {
            return context.getString(com.tokopedia.seller.R.string.default_request_error_internal_server);
        } else {
            return context.getString(com.tokopedia.seller.R.string.default_request_error_unknown);
        }
    }

}

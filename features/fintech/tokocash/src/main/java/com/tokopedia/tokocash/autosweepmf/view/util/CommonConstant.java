package com.tokopedia.tokocash.autosweepmf.view.util;

/**
 * Constant values files for Autosweep mutual fund features
 */
public interface CommonConstant {
    long AUTO_SWEEP_MF_MIN_LIMIT = 10000;
    long AUTO_SWEEP_MF_MAX_LIMIT = 9900000;
    int AUTO_SWEEP_SEEK_BAR_STEPS = 10000;
    int SUCCESS_CODE = 200000;
    int TRUE_INT = 1;
    int FALSE_INT = 0;
    String EXTRA_AVAILABLE_TOKOCASH = "extra_available_tokocash";
    String EXTRA_AUTO_SWEEP_LIMIT = "extra_auto_sweep_limit";
    String NOT_AVAILABLE = "N/A";
    String EVENT_AUTOSWEEPMF_STATUS_CHANGED = "autosweepmf-status-changed";
    String EVENT_KEY_NEEDED_RELOADING = "event_key_needed_reloading";
    String FIREBASE_APP_AUTOSWEEP_MIN_LIMIT = "app_autosweep_min_limit";
    String FIREBASE_APP_AUTOSWEEP_MAX_LIMIT = "app_autosweep_max_limit";

    interface GqlApiKeys {
        String ENABLE = "enable";
        String LIMIT = "amountLimit";
        String QUERY = "query";
        String VARIABLES = "variables";
    }
}

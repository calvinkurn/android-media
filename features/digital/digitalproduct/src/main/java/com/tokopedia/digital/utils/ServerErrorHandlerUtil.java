package com.tokopedia.digital.utils;

import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.exception.ServerErrorRequestDeniedException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorMaintenanceException;
import com.tokopedia.core.network.retrofit.exception.ServerErrorTimeZoneException;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;

/**
 * @author anggaprasetiyo on 5/26/17.
 */

public class ServerErrorHandlerUtil {

    public static void handleError(Throwable serverErrorException) {
        if (serverErrorException instanceof ServerErrorRequestDeniedException) {
            ServerErrorHandler.sendForceLogoutAnalytics(
                    ((ServerErrorRequestDeniedException) serverErrorException).getUrl()
            );
            ServerErrorHandler.showForceLogoutDialog();
        } else if (serverErrorException instanceof ServerErrorMaintenanceException) {
            ServerErrorHandler.showMaintenancePage();
        } else if (serverErrorException instanceof ServerErrorTimeZoneException) {
            ServerErrorHandler.showTimezoneErrorSnackbar();
        } else {
            if (((ServerErrorException) serverErrorException).getErrorCode() >= 500)
                ServerErrorHandler.sendErrorNetworkAnalytics(
                        ((ServerErrorException) serverErrorException).getUrl(),
                        ((ServerErrorException) serverErrorException).getErrorCode()
                );
        }
    }
}

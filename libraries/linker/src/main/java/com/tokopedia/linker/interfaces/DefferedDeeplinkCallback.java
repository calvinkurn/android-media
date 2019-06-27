package com.tokopedia.linker.interfaces;

import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;

public interface DefferedDeeplinkCallback {
    void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData);
    void onError(LinkerError linkerError);
}

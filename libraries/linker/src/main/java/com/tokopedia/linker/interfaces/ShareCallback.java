package com.tokopedia.linker.interfaces;

import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareResult;

public interface ShareCallback {
    void urlCreated(LinkerShareResult linkerShareData);
    void onError(LinkerError linkerError);
}

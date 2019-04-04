package com.tokopedia.kyc.view.presenter;

import com.tokopedia.kyc.model.ConfirmRequestDataContainer;

public interface ITncConfirmation {
    void submitKycTnCConfirmForm(ConfirmRequestDataContainer confirmRequestDataContainer);
}

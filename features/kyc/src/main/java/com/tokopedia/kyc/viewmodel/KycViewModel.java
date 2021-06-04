package com.tokopedia.kyc.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.tokopedia.kyc.model.ConfirmRequestDataContainer;

public class KycViewModel extends AndroidViewModel {
    

    private ConfirmRequestDataContainer confirmRequestDataContainer;

    public KycViewModel(@NonNull Application application) {
        super(application);
        confirmRequestDataContainer = new ConfirmRequestDataContainer();
    }

    public ConfirmRequestDataContainer getConfirmRequestDataContainer() {
        return confirmRequestDataContainer;
    }

    public void setConfirmRequestDataContainer(ConfirmRequestDataContainer confirmRequestDataContainer) {
        this.confirmRequestDataContainer = confirmRequestDataContainer;
    }
}

package com.tokopedia.notifications.inApp.ruleEngine.storage;

import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer;
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;

public class DataConsumerImpl implements DataConsumer {
    @Override
    public void dataShown(long id) {
        RepositoryManager.getInstance().getStorageProvider().updateInAppDataFreq(id).subscribe();
    }

    @Override
    public void viewDismissed(long id){
        RepositoryManager.getInstance().getStorageProvider().viewDismissed(id).subscribe();
    }

    @Override
    public void interactedWithView(long id){
        RepositoryManager.getInstance().getStorageProvider().interactedWithView(id).subscribe();
    }

    @Override
    public void inflationError(long id) {
        RepositoryManager.getInstance().getStorageProvider().deleteRecord(id).subscribe();

    }
}

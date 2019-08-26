package com.tokopedia.notifications.inApp.ruleEngine.storage;

import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer;
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager;

public class DataConsumerImpl implements DataConsumer {
    @Override
    public void dataShown(long id) {
        RepositoryManager.getInstance().getStorageProvider().updateInAppDataFreq(id);
    }

    @Override
    public void viewDismissed(long id){
        RepositoryManager.getInstance().getStorageProvider().viewDismissed(id);
    }

    @Override
    public void dataShown(long id, long newSt){
        RepositoryManager.getInstance().getStorageProvider().updateInAppDataFreq(id, newSt);
    }
}

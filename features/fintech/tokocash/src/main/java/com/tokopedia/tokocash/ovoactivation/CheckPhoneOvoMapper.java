package com.tokopedia.tokocash.ovoactivation;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class CheckPhoneOvoMapper implements Func1<CheckPhoneOvoEntity, CheckPhoneOvoModel> {

    @Inject
    public CheckPhoneOvoMapper() {
    }

    @Override
    public CheckPhoneOvoModel call(CheckPhoneOvoEntity checkPhoneOvoEntity) {
        CheckPhoneOvoModel checkPhoneOvoModel = new CheckPhoneOvoModel();
        checkPhoneOvoModel.setPhoneNumber(checkPhoneOvoEntity.getPhoneNumber());
        checkPhoneOvoModel.setRegistered(checkPhoneOvoEntity.isRegistered());
        checkPhoneOvoModel.setRegisteredApplink(checkPhoneOvoEntity.getRegisteredApplink());
        checkPhoneOvoModel.setNotRegisteredApplink(checkPhoneOvoEntity.getNotRegisteredApplink());
        checkPhoneOvoModel.setChangeMsisdnApplink(checkPhoneOvoEntity.getChangeMsisdnApplink());
        return checkPhoneOvoModel;
    }
}

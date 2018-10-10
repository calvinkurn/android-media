package com.tokopedia.tokocash.ovoactivation.domain;

import com.tokopedia.tokocash.ovoactivation.view.CheckPhoneOvoModel;
import com.tokopedia.tokocash.ovoactivation.view.ErrorModel;
import com.tokopedia.tokocash.ovoactivation.view.PhoneActionModel;

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
        checkPhoneOvoModel.setAllow(checkPhoneOvoEntity.isAllow());

        ErrorModel errorModel = new ErrorModel();
        errorModel.setMessage(checkPhoneOvoEntity.getErrors().getMessage());
        checkPhoneOvoModel.setErrorModel(errorModel);

        PhoneActionModel phoneActionModel = new PhoneActionModel();
        phoneActionModel.setTitlePhoneAction(checkPhoneOvoEntity.getPhoneActionEntity().getTitle());
        phoneActionModel.setDescPhoneAction(checkPhoneOvoEntity.getPhoneActionEntity().getDescription());
        phoneActionModel.setLabelBtnPhoneAction(checkPhoneOvoEntity.getPhoneActionEntity().getText());
        phoneActionModel.setApplinkPhoneAction(checkPhoneOvoEntity.getPhoneActionEntity().getApplink());
        checkPhoneOvoModel.setPhoneActionModel(phoneActionModel);

        return checkPhoneOvoModel;
    }
}

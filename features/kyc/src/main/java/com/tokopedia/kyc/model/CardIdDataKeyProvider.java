package com.tokopedia.kyc.model;

import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.kyc.view.fragment.FragmentCardIDUpload;

import java.util.ArrayList;

public class CardIdDataKeyProvider implements ActionDataProvider<ArrayList<String>, Object> {
    @Override
    public ArrayList<String> getData(int actionId, Object dataObject) {
        ArrayList<String> keysList = new ArrayList<>();
        keysList.add(FragmentCardIDUpload.CARDID_IMG_PATH);
        keysList.add(FragmentCardIDUpload.FLAG_IMG_FLIP);
        return keysList;
    }
}

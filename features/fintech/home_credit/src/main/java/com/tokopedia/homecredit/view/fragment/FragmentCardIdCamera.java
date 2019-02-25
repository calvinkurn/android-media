package com.tokopedia.homecredit.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.otaliastudios.cameraview.Facing;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentCardIdCamera extends HomeCreditKTPFragment{
    private ActionCreator actionCreator;
    private ActionDataProvider actionDataProvider;
    public static String ACTION_CREATOR_ARG = "action_creator_arg";
    public static String ACTION_KEYS_PROVIDER_ARG = "action_keys_provider_arg";

    @Override
    protected void onSuccessImageTakenFromCamera(File imgFile) {
        String imagePath = imgFile.getAbsolutePath();
        boolean toBeFlipped = false;
        hideLoading();
        cameraView.stop();//always call this method if you do not want awkward issues
        getActivity().getSupportFragmentManager().popBackStack();
        if(!TextUtils.isEmpty(imagePath) && actionCreator != null){
            if (cameraView.getFacing().ordinal() == Facing.FRONT.ordinal()){
                toBeFlipped = true;
            }
            ArrayList<String> keysList = (ArrayList<String>) actionDataProvider.getData(1, null);
            HashMap<String , Object> dataMap = new HashMap<>();
            dataMap.put(keysList.get(0), imagePath);
            dataMap.put(keysList.get(1), toBeFlipped);

            actionCreator.actionSuccess(1, dataMap);
        }
        else {
            actionCreator.actionError(1,101);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionCreator = (ActionCreator) getArguments().getSerializable(ACTION_CREATOR_ARG);
        actionDataProvider = (ActionDataProvider) getArguments().getSerializable(ACTION_KEYS_PROVIDER_ARG);
    }


}

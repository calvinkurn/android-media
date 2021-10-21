package com.tokopedia.homecredit.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otaliastudios.cameraview.controls.Facing;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.homecredit.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentSelfieIdCamera extends HomeCreditSelfieFragment {

    public static String ACTION_CREATOR_ARG = "action_creator_arg";
    public static String ACTION_KEYS_PROVIDER_ARG = "action_keys_provider_arg";
    private ActionCreator actionCreator;
    private ActionDataProvider actionDataProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frgament_kyc_selfieid_camera, container, false);
        ((ImageView) view.findViewById(R.id.iv_capture_image)).setImageDrawable(MethodChecker.getDrawable(getActivity(), R.drawable.ic_button_capture));
        return view;
    }

    @Override
    protected void onSuccessImageTakenFromCamera(File imgFile) {
        String imagePath = imgFile.getAbsolutePath();
        boolean toBeFlipped = false;
        hideLoading();
        cameraView.close();//always call this method if you do not want awkward issues
        getActivity().getSupportFragmentManager().popBackStack();
        if (actionCreator != null) {
            if (!TextUtils.isEmpty(imagePath)) {
                if (cameraView.getFacing().ordinal() == Facing.FRONT.ordinal()) {
                    toBeFlipped = true;
                }
                ArrayList<String> keysList = (ArrayList<String>) actionDataProvider.getData(1, null);
                HashMap<String, Object> dataMap = new HashMap<>();
                dataMap.put(keysList.get(0), imagePath);
                dataMap.put(keysList.get(1), toBeFlipped);
                actionCreator.actionSuccess(1, dataMap);
            } else {
                actionCreator.actionError(1, 101);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionCreator = (ActionCreator) getArguments().getSerializable(ACTION_CREATOR_ARG);
        actionDataProvider = (ActionDataProvider) getArguments().getSerializable(ACTION_KEYS_PROVIDER_ARG);
        getArguments().clear();
    }

    @Override
    public void onDestroy() {
        hideLoading();
        cameraView.close();
        super.onDestroy();
    }
}

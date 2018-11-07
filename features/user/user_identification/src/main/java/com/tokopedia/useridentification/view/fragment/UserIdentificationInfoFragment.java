package com.tokopedia.useridentification.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.useridentification.R;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationInfoFragment extends BaseDaggerFragment {

    private ImageView image;
    private TextView title;
    private TextView text;

    public static UserIdentificationInfoFragment createInstance(){
        UserIdentificationInfoFragment fragment = new UserIdentificationInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_user_identification_info, container, false);
        initView(parentView);
        return parentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    private void initView(View parentView){
        image = parentView.findViewById(R.id.main_image);
        title = parentView.findViewById(R.id.title);
        text = parentView.findViewById(R.id.text);
    }
}

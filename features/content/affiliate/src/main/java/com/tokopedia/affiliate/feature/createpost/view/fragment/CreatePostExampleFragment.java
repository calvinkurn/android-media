package com.tokopedia.affiliate.feature.createpost.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostExampleActivity;
import com.tokopedia.design.component.ButtonCompat;

import javax.inject.Inject;

/**
 * @author by milhamj on 10/1/18.
 */
public class CreatePostExampleFragment extends BaseDaggerFragment {

    private ImageView image;
    private TextView title;
    private ButtonCompat okBtn;

    private String imageUrl = "";
    private String titleText = "";


    public static CreatePostExampleFragment createInstance(@NonNull Bundle bundle) {
        CreatePostExampleFragment fragment = new CreatePostExampleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_create_post_example, container, false);
        image = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        okBtn = view.findViewById(R.id.okBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar(savedInstanceState);
        initView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CreatePostExampleActivity.PARAM_IMAGE, imageUrl);
        outState.putString(CreatePostExampleActivity.PARAM_TITLE, titleText);
    }

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imageUrl = savedInstanceState.getString(CreatePostExampleActivity.PARAM_IMAGE, "");
            titleText = savedInstanceState.getString(CreatePostExampleActivity.PARAM_TITLE, "");
        } else if (getArguments() != null) {
            imageUrl = getArguments().getString(CreatePostExampleActivity.PARAM_IMAGE, "");
            titleText = getArguments().getString(CreatePostExampleActivity.PARAM_TITLE, "");
        }
    }

    private void initView() {
        if (!TextUtils.isEmpty(imageUrl)) {
            ImageHandler.loadImage2(image, imageUrl, R.drawable.ic_loading_image);
        }
        if (!TextUtils.isEmpty(titleText)) {
            title.setText(titleText);
        }
        okBtn.setOnClickListener(v -> {
            getActivity().finish();
        });
    }
}

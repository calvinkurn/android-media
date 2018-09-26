package com.tokopedia.affiliate.feature.createpost.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.design.component.ButtonCompat;

public class CreatePostFragment extends BaseDaggerFragment implements CreatePostContract.View {

    private ButtonCompat addImageBtn;

    public static CreatePostFragment createInstance(@NonNull Bundle bundle) {
        CreatePostFragment fragment = new CreatePostFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_create_post, container, false);
        addImageBtn = view.findViewById(R.id.addImageBtn);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addImageBtn.setOnClickListener(view1 -> {

        });
    }
}

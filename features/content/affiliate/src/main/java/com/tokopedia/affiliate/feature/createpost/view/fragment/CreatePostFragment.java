package com.tokopedia.affiliate.feature.createpost.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.createpost.data.pojo.FeedContentForm;
import com.tokopedia.affiliate.feature.createpost.data.pojo.Guide;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliatecommon.view.widget.WrapContentViewPager;
import com.tokopedia.design.component.ButtonCompat;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
        .RESULT_IMAGE_DESCRIPTION_LIST;

public class CreatePostFragment extends BaseDaggerFragment implements CreatePostContract.View {

    private static final int REQUEST_IMAGE_PICKER = 1234;
    private ArrayList<String> selectedImage = new ArrayList<>();

    private TextView title;
    private TextView seeExample;
    private WrapContentViewPager imageViewPager;
    private TabLayout tabLayout;
    private View deleteImageLayout;
    private ButtonCompat doneBtn;
    private ButtonCompat addImageBtn;

    private String productId = "";
    private String adId = "";

    @Inject
    CreatePostContract.Presenter presenter;

    public static CreatePostFragment createInstance(@NonNull Bundle bundle) {
        CreatePostFragment fragment = new CreatePostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent
                = ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
        DaggerCreatePostComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this);
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
        title = view.findViewById(R.id.title);
        seeExample = view.findViewById(R.id.seeExample);
        imageViewPager = view.findViewById(R.id.imageViewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        deleteImageLayout = view.findViewById(R.id.deleteImageLayout);
        doneBtn = view.findViewById(R.id.doneBtn);
        addImageBtn = view.findViewById(R.id.addImageBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initVar(savedInstanceState);
        initView();
        presenter.fetchContentForm(productId, adId);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CreatePostActivity.PARAM_PRODUCT_ID, productId);
        outState.putString(CreatePostActivity.PARAM_AD_ID, adId);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICKER) {
                ArrayList<String> imageListResult
                        = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                ArrayList<String> imageListOriginal
                        = data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE);
                ArrayList<String> imageListDescription
                        = data.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST);
                selectedImage = imageListResult;
            }
        }
    }

    @Override
    public void onSuccessGetContentForm(FeedContentForm feedContentForm) {
        if (!feedContentForm.getGuides().isEmpty()) {
            setupHeader(feedContentForm.getGuides().get(0));
        }
    }

    @Override
    public void onErrorGetContentForm(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), message, () -> {
            presenter.fetchContentForm(productId, adId);
        });
    }

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            productId = savedInstanceState.getString(CreatePostActivity.PARAM_PRODUCT_ID, "");
            adId = savedInstanceState.getString(CreatePostActivity.PARAM_AD_ID, "");
        } else if (getArguments() != null) {
            productId = getArguments().getString(CreatePostActivity.PARAM_PRODUCT_ID, "");
            adId = getArguments().getString(CreatePostActivity.PARAM_AD_ID, "");
        }
    }

    private void initView() {
        doneBtn.setOnClickListener(view -> {
            //TODO milhamj
        });
        addImageBtn.setOnClickListener(view -> {
            startActivityForResult(
                    CreatePostImagePickerActivity.getInstance(
                            Objects.requireNonNull(getActivity()),
                            selectedImage),
                    REQUEST_IMAGE_PICKER);
        });
    }

    private void setupHeader(Guide guide) {
        title.setText(guide.getHeader());
        seeExample.setText(guide.getMoreText());
    }
}

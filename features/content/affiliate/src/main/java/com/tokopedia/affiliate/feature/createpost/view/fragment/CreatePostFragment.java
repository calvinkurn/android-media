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
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Guide;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Medium;
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostExampleActivity;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliate.feature.createpost.view.preference.CreatePostPreference;
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel;
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter;
import com.tokopedia.affiliatecommon.view.widget.WrapContentViewPager;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
        .RESULT_IMAGE_DESCRIPTION_LIST;

public class CreatePostFragment extends BaseDaggerFragment implements CreatePostContract.View {

    private static final String VIEW_MODEL = "view_model";
    private static final int REQUEST_IMAGE_PICKER = 1234;
    private static final int REQUEST_EXAMPLE = 13;

    private View mainView;
    private TextView title;
    private TextView seeExample;
    private WrapContentViewPager imageViewPager;
    private TabLayout tabLayout;
    private TextView setMain;
    private View deleteImageLayout;
    private ButtonCompat doneBtn;
    private ButtonCompat addImageBtn;
    private View loadingView;

    private CreatePostViewModel viewModel;
    private PostImageAdapter adapter;
    private Guide guide;

    @Inject
    CreatePostContract.Presenter presenter;
    @Inject
    CreatePostPreference createPostPreference;

    public static CreatePostFragment createInstance(@NonNull Bundle bundle) {
        CreatePostFragment fragment = new CreatePostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent
                = ((BaseMainApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getBaseAppComponent();
        DaggerCreatePostComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_create_post, container, false);
        mainView = view.findViewById(R.id.mainView);
        title = view.findViewById(R.id.title);
        seeExample = view.findViewById(R.id.seeExample);
        imageViewPager = view.findViewById(R.id.imageViewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        setMain = view.findViewById(R.id.setMain);
        deleteImageLayout = view.findViewById(R.id.deleteImageLayout);
        doneBtn = view.findViewById(R.id.doneBtn);
        addImageBtn = view.findViewById(R.id.addImageBtn);
        loadingView = view.findViewById(R.id.loadingView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView();
        presenter.fetchContentForm(viewModel.getProductId(), viewModel.getAdId());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(VIEW_MODEL, viewModel);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_PICKER:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> imageListResult
                            = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    ArrayList<String> imageListOriginal
                            = data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE);
                    ArrayList<String> imageListDescription
                            = data.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST);

                    viewModel.getImageList().clear();
                    viewModel.getImageList().addAll(imageListResult);
                    setupViewPager();

                }
                break;
            case REQUEST_EXAMPLE:
                goToImagePicker();
                break;
            default:
                break;
        }
    }

    @Override
    public UserSession getUserSession() {
        return new UserSession(getContext());
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetContentForm(FeedContentForm feedContentForm) {
        viewModel.setToken(feedContentForm.getToken());
        if (!feedContentForm.getGuides().isEmpty()) {
            setupHeader(feedContentForm.getGuides().get(0));
        }
        if (!feedContentForm.getMedia().getMedia().isEmpty()) {
            int lastIndex = feedContentForm.getMedia().getMedia().size() - 1;
            Medium medium = feedContentForm.getMedia().getMedia().get(lastIndex);
            viewModel.setPdpImage(medium.getMediaUrl());

            if (viewModel.getImageList().isEmpty()) {
                viewModel.getImageList().clear();
                for (int i = 0; i < lastIndex; i++) {
                    medium = feedContentForm.getMedia().getMedia().get(i);
                    viewModel.getImageList().add(medium.getMediaUrl());
                }
            }
            setupViewPager();
        }
    }

    @Override
    public void onErrorGetContentForm(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, message, () -> {
            presenter.fetchContentForm(viewModel.getProductId(), viewModel.getAdId());
        });
    }

    @Override
    public void onSuccessSubmitPost() {
        //TODO milhamj
        Toast.makeText(getContext(), "Create post success", Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void onErrorSubmitPost(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, message, this::submitPost);
    }

    private void initVar(Bundle savedInstanceState) {
        if (viewModel == null) {
            viewModel = new CreatePostViewModel();
        }
        if (adapter == null) {
            adapter = new PostImageAdapter();
        }

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(VIEW_MODEL);
        } else if (getArguments() != null) {
            viewModel.setProductId(
                    getArguments().getString(CreatePostActivity.PARAM_PRODUCT_ID, "")
            );
            viewModel.setAdId(
                    getArguments().getString(CreatePostActivity.PARAM_AD_ID, "")
            );
        }
    }

    private void initView() {
        doneBtn.setOnClickListener(view -> submitPost());
        addImageBtn.setOnClickListener(view -> {
            if (shouldShowExample()) {
                goToImageExample();
                createPostPreference.setFirstTime(getUserSession().getUserId());
            } else {
                goToImagePicker();
            }
        });
        deleteImageLayout.setOnClickListener(v -> {
            viewModel.getImageList().remove(tabLayout.getSelectedTabPosition());
            adapter.getImageList().remove(tabLayout.getSelectedTabPosition());
            adapter.notifyDataSetChanged();
        });
        setMain.setOnClickListener(v -> {
            viewModel.setMainImageIndex(tabLayout.getSelectedTabPosition());
            updateSetMainView();
        });
    }

    private boolean shouldShowExample() {
        return createPostPreference.isFirstTimeUser(getUserSession().getUserId());
    }

    private void setupHeader(Guide guide) {
        this.guide = guide;
        title.setText(guide.getHeader());
        seeExample.setText(guide.getMoreText());
        seeExample.setOnClickListener(v -> goToImageExample());
    }

    private void setupViewPager() {
        adapter.setList(viewModel.getCompleteList());
        imageViewPager.setAdapter(adapter);
        imageViewPager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(imageViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                deleteImageLayout.setVisibility(
                        tab.getPosition() == adapter.getCount() - 1
                                ? View.GONE
                                : View.VISIBLE
                );
                setMain.setVisibility(tab.getPosition() == adapter.getCount() - 1
                        ? View.INVISIBLE
                        : View.VISIBLE
                );
                updateSetMainView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void updateSetMainView() {
        if (viewModel.getMainImageIndex() == tabLayout.getSelectedTabPosition()) {
            setMain.setTextColor(MethodChecker.getColor(getContext(), R.color.black_38));
            setMain.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    MethodChecker.getDrawable(getContext(), R.drawable.ic_af_check_gray),
                    null
            );
        } else {
            setMain.setTextColor(MethodChecker.getColor(getContext(), R.color.medium_green));
            setMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private void goToImageExample() {
        startActivityForResult(
                CreatePostExampleActivity.createIntent(
                        getContext(),
                        guide != null ? guide.getImageUrl() : "",
                        guide != null ? guide.getImageDescription() : ""
                ),
                REQUEST_EXAMPLE
        );
    }

    private void goToImagePicker() {
        startActivityForResult(
                CreatePostImagePickerActivity.getInstance(
                        Objects.requireNonNull(getActivity()),
                        viewModel.getImageList()),
                REQUEST_IMAGE_PICKER);
    }

    private void submitPost() {
        presenter.submitPost(
                viewModel.getProductId(),
                viewModel.getAdId(),
                viewModel.getToken(),
                viewModel.getCompleteList(),
                viewModel.getMainImageIndex()
        );
    }
}

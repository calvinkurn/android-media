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
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Guide;
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.Medium;
import com.tokopedia.affiliate.feature.createpost.di.DaggerCreatePostComponent;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostExampleActivity;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostImagePickerActivity;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;
import com.tokopedia.affiliate.common.preference.AffiliatePreference;
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel;
import com.tokopedia.affiliatecommon.view.adapter.PostImageAdapter;
import com.tokopedia.affiliatecommon.view.widget.WrapContentViewPager;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

public class CreatePostFragment extends BaseDaggerFragment implements CreatePostContract.View {

    private static final String VIEW_MODEL = "view_model";
    private static final String PARAM_USER_ID = "{user_id}";
    private static final String PRODUCT_ID_QUERY_PARAM = "?product_id=";
    private static final int REQUEST_IMAGE_PICKER = 1234;
    private static final int REQUEST_EXAMPLE = 13;
    private static final int REQUEST_LOGIN = 83;

    @Inject
    CreatePostContract.Presenter presenter;
    @Inject
    AffiliatePreference affiliatePreference;
    @Inject
    AffiliateAnalytics affiliateAnalytics;

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

    public static CreatePostFragment createInstance(@NonNull Bundle bundle) {
        CreatePostFragment fragment = new CreatePostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        BaseAppComponent baseAppComponent
                = ((BaseMainApplication) getActivity().getApplication())
                .getBaseAppComponent();
        DaggerCreatePostComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return AffiliateEventTracking.Screen.BYME_CREATE_POST;
    }

    @Override
    public void onStart() {
        super.onStart();
        affiliateAnalytics.getAnalyticTracker().sendScreen(getActivity(), getScreenName());
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
        if (getUserSession().isLoggedIn()) {
            if (!viewModel.isEdit()) {
                presenter.fetchContentForm(viewModel.getProductId(), viewModel.getAdId());
            } else {
                presenter.fetchEditContentForm(viewModel.getPostId());
            }
        } else {
            startActivityForResult(
                    RouteManager.getIntent(
                            getContext(),
                            ApplinkConst.LOGIN
                    ),
                    REQUEST_LOGIN
            );
        }
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
                    viewModel.setFileImageList(data.getStringArrayListExtra(PICKER_RESULT_PATHS));
                    setupViewPager();
                }
                break;
            case REQUEST_EXAMPLE:
                goToImagePicker();
                break;
            case REQUEST_LOGIN:
                presenter.fetchContentForm(viewModel.getProductId(), viewModel.getAdId());
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
        viewModel.setMaxImage(feedContentForm.getMedia().getMaxMedia());
        if (!feedContentForm.getGuides().isEmpty()) {
            setupHeader(feedContentForm.getGuides().get(0));
        }
        if (!feedContentForm.getMedia().getMedia().isEmpty()) {
            if (viewModel.getUrlImageList().isEmpty()) {
                viewModel.getUrlImageList().clear();
                for (int i = 0; i < feedContentForm.getMedia().getMedia().size(); i++) {
                    Medium medium = feedContentForm.getMedia().getMedia().get(i);
                    viewModel.getUrlImageList().add(medium.getMediaUrl());
                }
            }
            setupViewPager();
            setMain.setVisibility(adapter.getCount() > 1 ? View.VISIBLE : View.INVISIBLE);
            deleteImageLayout.setVisibility(adapter.getCount() > 1 ? View.VISIBLE : View.GONE);
        }
        updateSetMainView();
    }

    @Override
    public void onErrorGetContentForm(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, message,
                () -> presenter.fetchContentForm(viewModel.getProductId(), viewModel.getAdId()));
    }

    @Override
    public void onErrorGetEditContentForm(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, message,
                () -> presenter.fetchEditContentForm(viewModel.getPostId()));
    }

    @Override
    public void onErrorNotAffiliate() {
        if (getActivity() != null) {
            String onboardingApplink = ApplinkConst.AFFILIATE_ONBOARDING
                    .concat(PRODUCT_ID_QUERY_PARAM)
                    .concat(viewModel.getProductId());
            Intent intent = RouteManager.getIntent(getActivity(), onboardingApplink);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onErrorNoQuota() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), R.string.text_full_affiliate_title, Toast.LENGTH_LONG)
                    .show();
            getActivity().finish();
            affiliateAnalytics.onJatahRekomendasiHabisPdp();
        }
    }

    @Override
    public void onSuccessSubmitPost() {
        goToProfile();
        getActivity().finish();
    }

    @Override
    public void onErrorSubmitPost(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, message, this::submitPost);
    }

    @Override
    public void onErrorEditPost(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), mainView, message, this::editPost);
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
            viewModel.setPostId(
                    getArguments().getString(CreatePostActivity.PARAM_POST_ID, "")
            );
            viewModel.setEdit(
                    getArguments().getBoolean(CreatePostActivity.PARAM_IS_EDIT, false)
            );
        }
    }

    private void initView() {
        doneBtn.setOnClickListener(view -> {
            affiliateAnalytics.onSelesaiCreateButtonClicked(viewModel.getProductId());
            if (!viewModel.isEdit()) {
                submitPost();
            } else {
                editPost();
            }
        });
        addImageBtn.setOnClickListener(view -> {
            affiliateAnalytics.onTambahGambarButtonClicked(viewModel.getProductId());
            if (shouldShowExample()) {
                goToImageExample(true);
                affiliatePreference.setFirstTimeCreatePost(getUserSession().getUserId());
            } else {
                goToImagePicker();
            }
        });
        deleteImageLayout.setOnClickListener(v -> {
            if (viewModel.getMainImageIndex() == tabLayout.getSelectedTabPosition()
                    && tabLayout.getSelectedTabPosition() == adapter.getCount() - 2) {
                if (tabLayout.getSelectedTabPosition() - 1 > 0) {
                    viewModel.setMainImageIndex(tabLayout.getSelectedTabPosition() - 1);
                } else {
                    viewModel.setMainImageIndex(0);
                }
                updateSetMainView();
            }

            if (tabLayout.getSelectedTabPosition() < viewModel.getFileImageList().size()) {
                viewModel.getFileImageList().remove(tabLayout.getSelectedTabPosition());
            } else {
                viewModel.getUrlImageList().remove(
                        tabLayout.getSelectedTabPosition()
                                - viewModel.getFileImageList().size()
                );
            }
            adapter.getImageList().remove(tabLayout.getSelectedTabPosition());
            adapter.notifyDataSetChanged();
        });
        setMain.setOnClickListener(v -> {
            viewModel.setMainImageIndex(tabLayout.getSelectedTabPosition());
            updateSetMainView();
        });
    }

    private boolean shouldShowExample() {
        return affiliatePreference.isFirstTimeCreatePost(getUserSession().getUserId());
    }

    private void setupHeader(Guide guide) {
        this.guide = guide;
        title.setText(guide.getHeader());
        seeExample.setText(guide.getMoreText());
        seeExample.setOnClickListener(v -> {
            affiliateAnalytics.onLihatContohButtonClicked(viewModel.getProductId());
            goToImageExample(false);
        });
    }

    private void setupViewPager() {
        adapter.setList(viewModel.getCompleteImageList());
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
                setMain.setVisibility(
                        tab.getPosition() == adapter.getCount() - 1
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
            setMain.setText(R.string.af_main_image);
            setMain.setTextColor(MethodChecker.getColor(getContext(), R.color.black_38));
            setMain.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    MethodChecker.getDrawable(getContext(), R.drawable.ic_af_check_gray),
                    null
            );
        } else {
            setMain.setText(R.string.af_set_main_image);
            setMain.setTextColor(MethodChecker.getColor(getContext(), R.color.medium_green));
            setMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private void goToImageExample(boolean needResult) {
        if (needResult) {
            startActivityForResult(
                    CreatePostExampleActivity.createIntent(
                            getContext(),
                            guide != null ? guide.getImageUrl() : "",
                            guide != null ? guide.getImageDescription() : ""
                    ),
                    REQUEST_EXAMPLE
            );
        } else {
            startActivity(
                    CreatePostExampleActivity.createIntent(
                            getContext(),
                            guide != null ? guide.getImageUrl() : "",
                            guide != null ? guide.getImageDescription() : ""
                    )
            );
        }
    }

    private void goToImagePicker() {
        startActivityForResult(
                CreatePostImagePickerActivity.getInstance(
                        getActivity(),
                        viewModel.getFileImageList(),
                        viewModel.getMaxImage() - viewModel.getUrlImageList().size()
                ),
                REQUEST_IMAGE_PICKER);
    }

    private void goToProfile() {
        String profileApplink = viewModel.isEdit()
                ? ApplinkConst.PROFILE_AFTER_EDIT
                : ApplinkConst.PROFILE_AFTER_POST;
        profileApplink = profileApplink.replace(PARAM_USER_ID, getUserSession().getUserId());
        Intent intent = RouteManager.getIntent(
                getContext(),
                profileApplink
        );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void submitPost() {
        presenter.submitPost(
                viewModel.getProductId(),
                viewModel.getAdId(),
                viewModel.getToken(),
                viewModel.getCompleteImageList(),
                viewModel.getMainImageIndex()
        );
    }

    private void editPost() {
        presenter.editPost(
                viewModel.getPostId(),
                viewModel.getToken(),
                viewModel.getCompleteImageList(),
                viewModel.getMainImageIndex()
        );
    }
}
package com.tokopedia.home.account.presentation.fragment.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.analytics.AccountAnalytics;
import com.tokopedia.home.account.constant.SettingType;
import com.tokopedia.home.account.data.model.AppNotificationSettingModel;
import com.tokopedia.home.account.data.model.SettingEditResponse;
import com.tokopedia.home.account.di.component.EmailNotificationSettingComponent;
import com.tokopedia.home.account.presentation.activity.EmailNotificationSettingActivity;
import com.tokopedia.home.account.presentation.adapter.setting.EmailNotifAdapter;
import com.tokopedia.home.account.presentation.presenter.EmailNotificationPresenter;
import com.tokopedia.home.account.presentation.listener.EmailNotificationView;
import com.tokopedia.home.account.presentation.viewmodel.EmailNotifViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.home.account.AccountConstants.Analytics.*;

public class EmailNotificationSettingFragment extends BaseDaggerFragment implements
        EmailNotificationView, EmailNotifAdapter.OnItemChangeListener,
        EmailNotificationSettingActivity.OnBackPressedFragmentListener {
    @Inject
    EmailNotificationPresenter presenter;
    private RecyclerView recyclerView;
    private EmailNotifAdapter adapter;
    private View loadingView;

    private AccountAnalytics accountAnalytics;

    private boolean isChanged;
    private final static String KEY_CHAT = "flag_chat";
    private final static String KEY_CHAT_ADMIN = "flag_chat_admin";
    private final static String KEY_TALK = "flag_talk";
    private final static String KEY_REVIEW = "flag_review";
    private final static String KEY_BULLETIN = "flag_bulletin";
    private final static String KEY_FLAG_NEWSLETTER = "flag_newsletter";

    public static Fragment createInstance() {
        return new EmailNotificationSettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        NetworkClient.init(getActivity());
        super.onCreate(savedInstanceState);
        accountAnalytics = new AccountAnalytics(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_notification_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.loading_state_view);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        populateSettings();
        getSetting();
    }

    private void getSetting() {
        loadingView.setVisibility(View.VISIBLE);
        presenter.getEmailNotifSetting(true);
    }

    private void populateSettings() {
        List<EmailNotifViewModel> items = new ArrayList<>();
        items.add(new EmailNotifViewModel(getString(R.string.label_review), getString(R.string.label_review_summary), SettingType.FLAG_REVIEW));
        items.add(new EmailNotifViewModel(getString(R.string.label_talk), getString(R.string.label_talk_summary), SettingType.FLAG_TALK));
        items.add(new EmailNotifViewModel(getString(R.string.label_chat), getString(R.string.label_chat_summary), SettingType.FLAG_MESSAGE));
        items.add(new EmailNotifViewModel(getString(R.string.label_chat_admin), getString(R.string.label_chat_admin_summary), SettingType.FLAG_ADMIN_MESSAGE));
        items.add(new EmailNotifViewModel(getString(R.string.label_bulletin), getString(R.string.label_bulletin_summary), SettingType.FLAG_NEWSLETTER));
        adapter = new EmailNotifAdapter(items);
        adapter.setItemChangeListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initInjector() {
        getComponent(EmailNotificationSettingComponent.class).inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onErrorGetNotifSetting(Throwable throwable) {
        loadingView.setVisibility(View.GONE);
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
    }

    @Override
    public void setupNotifSetting(AppNotificationSettingModel notification) {
        loadingView.setVisibility(View.GONE);
        adapter.setNotifSetting(notification);
    }

    @Override
    public void onErrorSaveNotifSetting(Throwable throwable) {
        loadingView.setVisibility(View.GONE);
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
    }

    @Override
    public void onSuccesSaveSetting(SettingEditResponse data) {
        loadingView.setVisibility(View.GONE);
        if (data.getMessageError() == null || data.getMessageError().size() < 1) {
            Toast.makeText(getActivity(), data.getMessageStatus().get(0), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), data.getMessageError().get(0), Toast.LENGTH_SHORT).show();
        }

        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onItemChange(String key, boolean newValue) {
        if (KEY_REVIEW.equals(key)) {
            accountAnalytics.eventClickEmailSetting(String.format("%s %s", REVIEW, NOTIFICATION));
        } else if (KEY_TALK.equals(key)) {
            accountAnalytics.eventClickEmailSetting(String.format("%s %s", DISCUSSION, NOTIFICATION));
        } else if (KEY_CHAT.equals(key)) {
            accountAnalytics.eventClickEmailSetting(String.format("%s %s", CHAT, NOTIFICATION));
        } else if (KEY_CHAT_ADMIN.equals(key)) {
            accountAnalytics.eventClickEmailSetting(String.format("%s %s %s", CHAT, TOKOPEDIA, NOTIFICATION));
        } else if (KEY_BULLETIN.equals(key)) {
            accountAnalytics.eventClickEmailSetting(String.format("%s %s", NEWS_LETTER, NOTIFICATION));
        } else if (KEY_FLAG_NEWSLETTER.equals(key)) {
            accountAnalytics.setNewsletterEmailPref(!newValue);
        }
        isChanged = true;
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            presenter.saveEmailNotifUseCase(adapter.getSelectedSetting());
        } else {
            if (getActivity() != null) getActivity().finish();
        }
    }
}
